package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.TypeUtils;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT_TO;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Teleport implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                p.sendMessage(PLUGIN_COMMAND_TP_INFO);
                return true;
            }
        }
        else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
                        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT)) {
                            p.teleport(target);
                            p.sendMessage(String.format(PLUGIN_COMMAND_TP, target.getCustomName()));
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null && args[0].equals(PLUGIN_COMMAND_NAME_TELEPORT_TO)) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
                        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT)) {
                            target.teleport(p);
                            p.sendMessage(String.format(PLUGIN_COMMAND_TP_TO, target.getCustomName()));
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        else if (args.length == 3) {
            if (TypeUtils.isInt(args[0]) && TypeUtils.isInt(args[1]) && TypeUtils.isInt(args[2])) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT)) {
                            Location l = p.getLocation().clone();
                            
                            l.setX(Integer.parseInt(args[0]));
                            l.setY(Integer.parseInt(args[1]));
                            l.setZ(Integer.parseInt(args[2]));

                            p.teleport(l);
                            p.sendMessage(String.format(PLUGIN_COMMAND_TP, l.getX() + ", " + l.getY() + ", " + l.getZ()));
                            return true;
                        } else {
                            return false;
                        }
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
