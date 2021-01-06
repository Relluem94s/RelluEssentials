package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationTypeEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import java.util.Map;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class Home implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("home")) {
            switch (args.length) {
                case 0:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.USER.getId())) {
                            if (p.getBedSpawnLocation() != null) {
                                p.teleport(p.getBedSpawnLocation());
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME, p.getWorld().getName()));
                            } else {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NO_BED, p.getWorld().getName()));
                            }
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("list")) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            Map<String, Object> map;
                            ConfigurationSection c = players.getConfig().getConfigurationSection("player." + p.getUniqueId() + ".home");

                            if (c != null) {
                                map = c.getValues(false);
                                p.sendMessage(PLUGIN_COMMAND_HOME_LIST);
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    p.sendMessage(PLUGIN_COMMAND_ARG_COLOR + entry.getKey());
                                }
                            } else {
                                p.sendMessage(PLUGIN_COMMAND_HOME_NONE);
                            }
                            return true;
                        }
                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            PlayerEntry pe = playerEntryList.get(p.getUniqueId());
                            LocationEntry le = new LocationEntry();
                            le.setLocationName(args[0]);
                            le.setLocationType(locationTypeEntryList.get(0));
                            le.setPlayerId(pe.getId());
                            
                            if(homeExists(pe, le)){
                                le = getLocationEntry(pe, le);
                                p.teleport(le.getLocation());
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_TP, args[0]));
                                return true;
                            } else {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NOT_FOUND, args[0]));
                                return true;
                            }
                        }
                    }
                    break;
                case 2:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
                        
                        if (args[0].equalsIgnoreCase("set")) {
                            
                            LocationEntry le = new LocationEntry();

                            le.setLocation(p.getLocation());
                            le.setLocationName(args[1]);
                            le.setLocationType(locationTypeEntryList.get(0));
                            le.setPlayerId(pe.getId());

                            if(homeExists(pe, le)){
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_EXISTS, args[1]));
                            }
                            else if (!args[1].equals("death")) {
                                dBH.insertLocation(le);
                                locationEntryList.add(le);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_SET, args[1]));
                            }
                            else {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_RESERVED, args[1]));
                            }
                            return true;
                        } else if (args[0].equalsIgnoreCase("delete")) {
                            
                            LocationEntry le = new LocationEntry();

                            le.setLocation(p.getLocation());
                            le.setLocationName(args[1]);
                            
                            
                            
                            if(homeExists(pe, le)){
                                le = getLocationEntry(pe, le);
                                dBH.deleteLocation(le);
                                locationEntryList.remove(le);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DELETE, args[1]));
                                return true;
                            }
                            else{
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NOT_FOUND, args[1]));
                                return true;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
    
    private boolean homeExists(PlayerEntry pe, LocationEntry le){
        for (LocationEntry fle : locationEntryList) {
            if (fle.getPlayerId() == pe.getId() && fle.getLocationName().equals(le.getLocationName()) && fle.getLocationType().getId() == 1) {
                return true;
            }
        }
        return false;
    }
        
    private LocationEntry getLocationEntry(PlayerEntry pe, LocationEntry le){
        for (LocationEntry fle : locationEntryList) {
            if (fle.getPlayerId() == pe.getId() && fle.getLocationName().equals(le.getLocationName()) && fle.getLocationType().getId() == 1) {
                return fle;
            }
        }
        return null;
    }
}
