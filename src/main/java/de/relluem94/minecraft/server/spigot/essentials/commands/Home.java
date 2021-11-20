package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationTypeEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME_DELETE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME_LIST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME_SET;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.locationToString;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Home implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_HOME)) {
            switch (args.length) {
                case 0:
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
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
                    if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_HOME_LIST)) {
                        if (isPlayer(sender)) {
                            Player p = (Player) sender;
                            PlayerEntry pe = playerEntryList.get(p.getUniqueId());

                            if (hasHomes(pe)) {
                                p.sendMessage(PLUGIN_COMMAND_HOME_LIST);
                                locationEntryList.stream().filter(fle -> (fle.getPlayerId() == pe.getID() && fle.getLocationType().getId() == 1)).forEachOrdered(fle -> {
                                    p.sendMessage(PLUGIN_COMMAND_COLOR + "Name: " + PLUGIN_COMMAND_ARG_COLOR + fle.getLocationName() + " §7(" + locationToString(fle.getLocation()) + "§7)");
                                });
                                // @TODO remove String and add to Strings.java
                                locationEntryList.stream().filter(fle -> (fle.getPlayerId() == pe.getID() && fle.getLocationType().getId() == 2)).forEachOrdered(fle -> {
                                    p.sendMessage(PLUGIN_COMMAND_COLOR + "Todespunkt: " + PLUGIN_COMMAND_ARG_COLOR + fle.getLocationName() + " §7(" + locationToString(fle.getLocation()) + "§7)");
                                });
                            } else {
                                p.sendMessage(PLUGIN_COMMAND_HOME_NONE);
                            }
                            return true;
                        }
                    } else {
                        if (isPlayer(sender)) {
                            Player p = (Player) sender;
                            PlayerEntry pe = playerEntryList.get(p.getUniqueId());
                            LocationEntry le = new LocationEntry();
                            le.setLocationName(args[0]);
                            le.setLocationType(locationTypeEntryList.get(0));
                            le.setPlayerId(pe.getID());

                            if (homeExists(pe, le)) {
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
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        PlayerEntry pe = playerEntryList.get(p.getUniqueId());

                        LocationEntry le = new LocationEntry();
                        le.setLocation(p.getLocation());
                        le.setLocationName(args[1]);
                        le.setLocationType(locationTypeEntryList.get(0));
                        le.setPlayerId(pe.getID());

                        if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_HOME_SET)) {
                            if (homeExists(pe, le)) {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_EXISTS, args[1]));
                            } else if (!args[1].startsWith("death_")) {
                                dBH.insertLocation(le);
                                locationEntryList.add(le);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_SET, args[1]));
                            } else {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_RESERVED, args[1]));
                            }
                            return true;
                        } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_HOME_DELETE)) {
                            if (homeExists(pe, le)) {
                                le = getLocationEntry(pe, le);
                                dBH.deleteLocation(le);
                                locationEntryList.remove(le);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DELETE, args[1]));
                                return true;
                            } else {
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

    private boolean homeExists(PlayerEntry pe, LocationEntry le) {
        return locationEntryList.stream().anyMatch(fle -> (fle.getPlayerId() == pe.getID() && fle.getLocationName().equals(le.getLocationName()) && (fle.getLocationType().getId() == 1 | fle.getLocationType().getId() == 2)));
    }

    private boolean hasHomes(PlayerEntry pe) {
        return locationEntryList.stream().anyMatch(fle -> (fle.getPlayerId() == pe.getID() && (fle.getLocationType().getId() == 1 | fle.getLocationType().getId() == 2)));
    }

    private LocationEntry getLocationEntry(PlayerEntry pe, LocationEntry le) {
        for (LocationEntry fle : locationEntryList) {
            if (fle.getPlayerId() == pe.getID() && fle.getLocationName().equals(le.getLocationName()) && (fle.getLocationType().getId() == 1 | fle.getLocationType().getId() == 2)) {
                return fle;
            }
        }
        return null;
    }
}
