package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_INVALID;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TP;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TP_ACCEPT_NO_REQUEST;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TP_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TP_REQUEST_EXPIRED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TP_REQUEST_TARGET;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TP_SEND_REQUEST;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TP_TO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT_ACCEPT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT_TO;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.TypeUtils;

public class Teleport implements CommandExecutor {

    HashMap<Player, Player> telportAcceptList = new HashMap<>();
    HashMap<Player, Player> telportToAcceptList = new HashMap<>();

    private void addTeleportEntry(Player p, Player t){
        telportAcceptList.put(t, p);
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
        telportToAcceptList.put(t, p);
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
        telportAcceptList.remove(t);
    }

    private void removeToTeleportEntry(Player t) {
        telportToAcceptList.remove(t);
    }

    private boolean hasTeleportEntry(Player t){
        return telportAcceptList.containsKey(t);
    }

    private boolean hasToTeleportEntry(Player t){
        return telportToAcceptList.containsKey(t);
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT)) {
            return false;
        }
        
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
            if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT_ACCEPT)){
                if(hasTeleportEntry(p)){
                    teleport(p, telportAcceptList.get(p));
                    removeTeleportEntry(p);
                    return true;
                }

                if(hasToTeleportEntry(p)){
                    teleportTo(p, telportToAcceptList.get(p));
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

            if(!args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT_TO)){
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
}