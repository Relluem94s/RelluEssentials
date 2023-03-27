package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WARP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WARP_ADD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WARP_REMOVE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

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

        if (args.length == 0) {
            p.sendMessage(PLUGIN_COMMAND_WARP_LIST_INFO);
            for(LocationEntry le : RelluEssentials.getInstance().getWarpAPI().getWarps(p.getWorld())){
                p.sendMessage(String.format(PLUGIN_COMMAND_WARP_LIST, le.getLocationName()));
            }

            return true;
        }
        else if (args.length == 1) {
            if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            } 

            LocationEntry le = RelluEssentials.getInstance().getWarpAPI().getWarp(args[0], p.getWorld());

            if(le == null){
                p.sendMessage(PLUGIN_COMMAND_WARP_ERROR_NO_WARP_FOUND);
                return true;
            }
            
            if(le.getLocation() != null && le.getLocation().getWorld() != null){
                Back.addBackPoint(p);
                p.teleport(le.getLocation());
                p.sendMessage(PLUGIN_COMMAND_WARP);
                return true;
            }
            
            p.sendMessage(PLUGIN_COMMAND_WARP_ERROR_WORLD_UNLOADED);
            return true;
        } 
        else if (args.length == 2) {
            if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            } 

            if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_WARP_ADD)){
                LocationEntry le = RelluEssentials.getInstance().getWarpAPI().getWarp(args[0]);
                if(le == null){
                    int typeId = 3;
                    le = new LocationEntry();
                    le.setLocation(p.getLocation());
                    le.setLocationName(args[1]);
                    le.setLocationType(RelluEssentials.getInstance().locationTypeEntryList.get(typeId - 1));
                    le.setPlayerId(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p).getID());
                    RelluEssentials.getInstance().getDatabaseHelper().insertLocation(le);
                    if(RelluEssentials.getInstance().getDatabaseHelper().getLocation(p.getLocation(), typeId) != null){
                        le = RelluEssentials.getInstance().getDatabaseHelper().getLocation(p.getLocation(), typeId);
                    }

                    RelluEssentials.getInstance().getWarpAPI().addWarp(le);
                }
            }
            else if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_WARP_REMOVE)){
                LocationEntry le = RelluEssentials.getInstance().getWarpAPI().getWarp(args[0]);
                if(le != null){
                    RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(le);
                    RelluEssentials.getInstance().getWarpAPI().removeWarp(le);
                }
            }
            return true;
        }
        return false;
    }
}