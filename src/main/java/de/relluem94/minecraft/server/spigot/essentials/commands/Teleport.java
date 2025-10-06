package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_INVALID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TP_ACCEPT_NO_REQUEST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TP_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TP_REQUEST_EXPIRED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TP_REQUEST_TARGET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TP_SEND_REQUEST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TP_TO;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.TypeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("teleport")
public class Teleport implements CommandConstruct {

    private final HashMap<Player, Player> teleportAcceptList = new HashMap<>();
    private final HashMap<Player, Player> teleportToAcceptList = new HashMap<>();

    private void addTeleportEntry(Player p, Player t){
        teleportAcceptList.put(t, p);
        t.sendMessage(String.format(PLUGIN_COMMAND_TP_REQUEST_TARGET, p.getCustomName()));
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
            if(hasTeleportEntry(t)){
                p.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                t.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                removeTeleportEntry(t);
            } 
        }, 20*60*2L);
    }

   
    private void addTeleportToEntry(Player p, Player t){
        teleportToAcceptList.put(t, p);
        t.sendMessage(String.format(PLUGIN_COMMAND_TP_REQUEST_TARGET, p.getCustomName()));
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
            if(hasToTeleportEntry(t)){
                p.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                t.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                removeToTeleportEntry(t);
            }
        }, 20*60*2L);
    }

    private void removeTeleportEntry(Player t) {
        teleportAcceptList.remove(t);
    }

    private void removeToTeleportEntry(Player t) {
        teleportToAcceptList.remove(t);
    }

    private boolean hasTeleportEntry(Player t){
        return teleportAcceptList.containsKey(t);
    }

    private boolean hasToTeleportEntry(Player t){
        return teleportToAcceptList.containsKey(t);
    }

    public void teleport(Player p, Player t){
        Back.addBackPoint(t);
        t.teleport(p);
        t.sendMessage(String.format(PLUGIN_COMMAND_TP, t.getCustomName()));
    }

    public void teleportTo(Player p, Player t){
        Back.addBackPoint(p);
        p.teleport(t);
        p.sendMessage(String.format(PLUGIN_COMMAND_TP_TO, p.getCustomName()));
        t.sendMessage(String.format(PLUGIN_COMMAND_TP, t.getCustomName()));
    }


    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(PLUGIN_COMMAND_TP_INFO);
            return true;
        }
        
        if (args.length == 1) {
            if(args[0].equalsIgnoreCase(Commands.ACCEPT.getName())){
                if(hasTeleportEntry(p)){
                    teleport(p, teleportAcceptList.get(p));
                    removeTeleportEntry(p);
                    return true;
                }

                if(hasToTeleportEntry(p)){
                    teleportTo(p, teleportToAcceptList.get(p));
                    removeToTeleportEntry(p);
                    return true;
                }

                p.sendMessage(PLUGIN_COMMAND_TP_ACCEPT_NO_REQUEST);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
                return false;
            }

            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                addTeleportEntry(p, target);
                p.sendMessage(String.format(PLUGIN_COMMAND_TP_SEND_REQUEST, target.getCustomName()));
                return true;
            }

            Back.addBackPoint(p);
            p.teleport(target);
            p.sendMessage(String.format(PLUGIN_COMMAND_TP, target.getCustomName()));
            return true;
        }
       
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                p.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[1]));
                return true;
            }

            if(!args[0].equalsIgnoreCase(Commands.TO.getName())){
                p.sendMessage(PLUGIN_COMMAND_TP_INFO);
                return true;
            }

            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                addTeleportToEntry(p, target);
                return true;
            }
            teleportTo(p, target);
            return true;
        }
        
        if (args.length == 3) {
            teleportToLocation(p, args[0], args[1], args[2]);
            return true;
        }

        p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
        return true;
    }

    private void teleportToLocation(Player p, String x, String y, String z){
        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return;
        }

        if (!TypeUtils.isInt(x)) {
            p.sendMessage(PLUGIN_COMMAND_INVALID);
            return;
        }

        if (!TypeUtils.isInt(y)) {
            return;
        }

        if (!TypeUtils.isInt(z)) {
            return;
        }

        Location l = p.getLocation().clone();
        
        l.setX(Integer.parseInt(x));
        l.setY(Integer.parseInt(y));
        l.setZ(Integer.parseInt(z));

        Back.addBackPoint(p);
        p.teleport(l);
        p.sendMessage(String.format(PLUGIN_COMMAND_TP, l.getX() + ", " + l.getY() + ", " + l.getZ()));

    }

    @Override
    public CommandsEnum[] getCommands() {
        return Sign.Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        ACCEPT("accept"),
        TO("to");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("user").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        if(strings.length == 1){
            tabList.addAll(TabCompleterHelper.getCommands(getCommands()));
        }

        return tabList;
    }
}