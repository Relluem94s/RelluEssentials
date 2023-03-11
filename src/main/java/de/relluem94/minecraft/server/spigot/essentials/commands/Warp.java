package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.WarpAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WARP;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Warp implements CommandExecutor {

    //TODO add proper impl

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_WARP)) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    for(LocationEntry le : WarpAPI.getWarps(p.getWorld())){
                        p.sendMessage(le.getLocationName());
                    }

                    return true;
                }
                else if (args.length == 1) {
                    if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                        LocationEntry le = WarpAPI.getWarp(args[0], p.getWorld());
                        if(le != null){
                            if(le.getLocation() != null && le.getLocation().getWorld() != null){
                                Back.addBackPoint(p);
                                p.teleport(le.getLocation());
                                p.sendMessage("warped");
                            }
                            else{
                                p.sendMessage("error world unloaded");
                            }
                        }
                        else{
                            p.sendMessage("no warp");
                        }

                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                } 
                else if (args.length == 2) {
                    if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        if(args[0].equalsIgnoreCase("add")){
                            LocationEntry le = WarpAPI.getWarp(args[0]);
                            if(le == null){
                                int typeId = 3;
                                le = new LocationEntry();
                                le.setLocation(p.getLocation());
                                le.setLocationName(args[1]);
                                le.setLocationType(RelluEssentials.locationTypeEntryList.get(typeId - 1));
                                le.setPlayerId(PlayerAPI.getPlayerEntry(p).getID());
                                RelluEssentials.dBH.insertLocation(le);
                                if(RelluEssentials.dBH.getLocation(p.getLocation(), typeId) != null){
                                    le = RelluEssentials.dBH.getLocation(p.getLocation(), typeId);
                                }

                                WarpAPI.addWarp(le);
                            }
                        }
                        else if(args[0].equalsIgnoreCase("remove")){
                            LocationEntry le = WarpAPI.getWarp(args[0]);
                            if(le != null){
                                RelluEssentials.dBH.deleteLocation(le);
                                WarpAPI.removeWarp(le);
                            }
                        }
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                      
                }
            }
        }
        return false;
    }
}
