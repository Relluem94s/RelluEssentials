package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_DEATH_DELETE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_DELETE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_EXISTS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_LIST;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_LIST_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_NONE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_NO_BED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_RESERVED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_SET;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_HOME_TP;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME_DELETE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME_LIST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME_SET;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.locationToString;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

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
                                Back.addBackPoint(p);
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
                            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());

                            if (!pe.getHomes().isEmpty()) {
                                p.sendMessage(PLUGIN_COMMAND_HOME_LIST);
                                pe.getHomes().stream().forEachOrdered(fle -> p.sendMessage(String.format(PLUGIN_COMMAND_HOME_LIST_NAME, fle.getLocationName(), locationToString(fle.getLocation()))));
                            } else {
                                p.sendMessage(PLUGIN_COMMAND_HOME_NONE);
                            }

                            if(!pe.getDeaths().isEmpty()){
                                p.sendMessage(PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS);
                                pe.getDeaths().stream().forEachOrdered(fle -> p.sendMessage(String.format(PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS_NAME, fle.getLocationName(), locationToString(fle.getLocation()))));
                            }
                            return true;
                        }
                    } else {
                        if (isPlayer(sender)) {
                            Player p = (Player) sender;
                            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
                            LocationEntry le = new LocationEntry();
                            le.setLocationName(args[0]);
                            le.setLocationType(RelluEssentials.getInstance().locationTypeEntryList.get(0));
                            le.setPlayerId(pe.getId());

                            if (homeExists(pe, le) || deathExists(pe, le)) {
                                le = getLocationEntry(pe, le);
                                Back.addBackPoint(p);
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
                        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());

                        LocationEntry le = new LocationEntry();
                        le.setLocation(p.getLocation());
                        le.setLocationName(args[1]);
                        le.setLocationType(RelluEssentials.getInstance().locationTypeEntryList.get(0));
                        le.setPlayerId(pe.getId());

                        if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_HOME_SET)) {
                            if (homeExists(pe, le)) {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_EXISTS, args[1]));
                            } else if (!args[1].startsWith("death_")) {
                                RelluEssentials.getInstance().getDatabaseHelper().insertLocation(le);
                                pe.getHomes().add(le);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_SET, args[1]));
                            } else {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_RESERVED, args[1]));
                            }
                            return true;
                        } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_HOME_DELETE)) {
                            if (homeExists(pe, le)) {
                                le = getLocationEntry(pe, le);
                                RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(le);
                                pe.getHomes().remove(le);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DELETE, args[1]));
                                return true;
                            }
                            else if(deathExists(pe, le)){
                                le = getLocationEntry(pe, le);
                                RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(le);
                                pe.getDeaths().remove(le);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DEATH_DELETE, args[1]));
                                return true;
                            }
                            else if(le.getLocationName().startsWith("death_") && le.getLocationName().contains("*")){
                                for(LocationEntry dle : pe.getDeaths()){
                                    p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DEATH_DELETE, dle.getLocationName()));
                                    RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(dle);
                                }

                                pe.getDeaths().clear();
                                return true;
                            }
                            else {
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
        return pe.getHomes().stream().anyMatch(fle -> (fle.getLocationName().equals(le.getLocationName())));
    }

    private boolean deathExists(PlayerEntry pe, LocationEntry le) {
        return pe.getDeaths().stream().anyMatch(fle -> (fle.getLocationName().equals(le.getLocationName())));
    }

    private LocationEntry getLocationEntry(PlayerEntry pe, LocationEntry le) {
        for (LocationEntry fle : pe.getHomes()) {
            if (fle.getLocationName().equals(le.getLocationName())) {
                return fle;
            }
        }

        for (LocationEntry fle : pe.getDeaths()) {
            if (fle.getLocationName().equals(le.getLocationName())) {
                return fle;
            }
        }
        return null;
    }
}
