package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WARP;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WARP_ERROR_NO_WARP_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WARP_ERROR_WORLD_UNLOADED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WARP_LIST;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WARP_LIST_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WRONG_SUB_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WARP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WARP_ADD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WARP_REMOVE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Warp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_WARP)) {
            return false;
        }

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

            if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_WARP_ADD)){
                return addWarp(args[1], p);
            }
            else if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_WARP_REMOVE)){
                return removeWarp(args[1]);
            }
            else {
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }
        }
        return false;
    }

    private boolean addWarp(String name, Player p){
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
        }

        return true;
    }

    private boolean removeWarp(String name){
        LocationEntry le = RelluEssentials.getInstance().getWarpAPI().getWarp(name);
        if(le != null){
            RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(le);
            RelluEssentials.getInstance().getWarpAPI().removeWarp(le);
        }

        return true;
    }

    private void warp(String name, Player p){
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

        Back.addBackPoint(p);
        p.teleport(le.getLocation());
        p.sendMessage(PLUGIN_COMMAND_WARP);
    }
}