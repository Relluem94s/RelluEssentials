package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TeleportHelper.teleportWarp;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CommandName("warp")
public class Warp implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        } 

        if (args.length == 0) {
            p.sendMessage(PLUGIN_COMMAND_WARP_LIST_INFO);
            for(LocationEntry le : RelluEssentials.getInstance().getWarpAPI().getWarps(p.getWorld())){
                p.sendMessage(String.format(PLUGIN_COMMAND_WARP_LIST, le.getLocationName()));
            }

            return true;
        }
        else if (args.length == 1) {
            warp(args[0], p);
            return true;
        } 
        else if (args.length == 2) {
            if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            } 

            if(args[0].equalsIgnoreCase(Commands.ADD.getName())){
                addWarp(args[1], p);
                return true;
            }
            else if(args[0].equalsIgnoreCase(Commands.REMOVE.getName())){
                removeWarp(args[1], p);
                return true;
            }
            else {
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }
        }
        return false;
    }

    private void addWarp(String name, Player p){
        LocationEntry le = RelluEssentials.getInstance().getWarpAPI().getWarp(name);
        if(le == null){
            int typeId = 3;
            le = new LocationEntry();
            le.setLocation(p.getLocation());
            le.setLocationName(name);
            le.setLocationType(RelluEssentials.getInstance().locationTypeEntryList.get(typeId - 1));
            le.setPlayerId(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p).getId());
            RelluEssentials.getInstance().getDatabaseHelper().insertLocation(le);
            if(RelluEssentials.getInstance().getDatabaseHelper().getLocation(p.getLocation(), typeId) != null){
                le = RelluEssentials.getInstance().getDatabaseHelper().getLocation(p.getLocation(), typeId);
            }

            RelluEssentials.getInstance().getWarpAPI().addWarp(le);
            p.sendMessage(String.format(PLUGIN_COMMAND_WARP_ADD, name));
        }
    }

    private void removeWarp(String name, Player p){
        LocationEntry le = RelluEssentials.getInstance().getWarpAPI().getWarp(name);
        if(le != null){
            RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(le);
            RelluEssentials.getInstance().getWarpAPI().removeWarp(le);
            p.sendMessage(String.format(PLUGIN_COMMAND_WARP_REMOVE, name));
        }
    }

    private void warp(String name, @NotNull Player p){
        LocationEntry le = RelluEssentials.getInstance().getWarpAPI().getWarp(name, p.getWorld());

        if(le == null){
            p.sendMessage(PLUGIN_COMMAND_WARP_ERROR_NO_WARP_FOUND);
            return;
        }
        
        if(le.getLocation() == null){
            p.sendMessage(PLUGIN_COMMAND_WARP_ERROR_WORLD_UNLOADED);
            return;
        }

        if(le.getLocation().getWorld() == null){
            p.sendMessage(PLUGIN_COMMAND_WARP_ERROR_WORLD_UNLOADED);
            return;
        }

        teleportWarp(p, le.getLocation());
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("user").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        Player p = (Player) commandSender;

        switch (strings.length){
            case 1:
                if (Permission.isAuthorized(commandSender, Groups.getGroup("admin").getId())) {
                    tabList.addAll(TabCompleterHelper.getCommands(getCommands()));
                }

                tabList.addAll(TabCompleterHelper.getWarps(p.getWorld()));


                break;
            case 2:
                if (!Permission.isAuthorized(commandSender, Groups.getGroup("admin").getId())) {
                    return tabList;
                }

                if(Commands.ADD.getName().equalsIgnoreCase(strings[1])){
                    return tabList;
                }

                tabList.addAll(TabCompleterHelper.getWarps(p.getWorld()));
                return tabList;
            default:
                break;
        }

        return tabList;
    }

    @Getter
    public enum Commands implements CommandsEnum {

        ADD("add"),
        REMOVE("remove");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }
}