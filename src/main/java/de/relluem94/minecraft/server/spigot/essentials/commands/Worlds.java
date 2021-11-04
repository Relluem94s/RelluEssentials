package de.relluem94.minecraft.server.spigot.essentials.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.WorldType;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;

import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WORLD_CREATE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WORLD_LIST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WORLD_LOAD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WORLD_UNLOAD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WORLD_UNLOAD_NO_SAVE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Worlds implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_WORLD)) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                switch (args.length) {
                    case 0:
                        // HOW TO USE COMMANDS
                        return true;
                    case 1:
                        if (args[0].equals(PLUGIN_COMMAND_NAME_WORLD_LIST)) {
                            if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                                sendMessage(p, PLUGIN_COMMAND_WORLD);
                                Bukkit.getWorlds().forEach(w -> {
                                    sendMessage(p, w.getName());
                                });
                                return true;
                            } else {
                                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                                return true;
                            }
                        } else {
                            if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                                World w = Bukkit.getWorld(args[0]);
                                if (w != null) {
                                    p.teleport(w.getSpawnLocation());
                                    p.sendMessage(String.format(PLUGIN_COMMAND_SPAWN, p.getWorld().getName()));
                                } else {
                                    p.sendMessage(String.format(PLUGIN_COMMAND_WORLD_NOT_FOUND, args[0]));
                                }
                                return true;
                            } else {
                                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                                return true;
                            }
                        }

                    case 2:
                        if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                            switch (args[0]) {
                                case PLUGIN_COMMAND_NAME_WORLD_CREATE:
                                    // HOW TO USE CREATE
                                    break;
                                case PLUGIN_COMMAND_NAME_WORLD_LOAD:
                                    WorldHelper.loadWorld(args[1]);
                                    return true;
                                case PLUGIN_COMMAND_NAME_WORLD_UNLOAD:
                                    try {
                                        WorldHelper.unloadWorld(args[1], true);
                                    } catch (WorldNotLoadedException ex) {
                                        Logger.getLogger(Worlds.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    return true;
                                case PLUGIN_COMMAND_NAME_WORLD_UNLOAD_NO_SAVE:
                                    try {
                                        WorldHelper.unloadWorld(args[1], false);
                                    } catch (WorldNotLoadedException ex) {
                                        Logger.getLogger(Worlds.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                return true;
                                default:
                                    // HOW TO USE
                                    return true;
                            }
                        } else {
                            // NO PERM
                        }
                    case 5:
                        if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                            if (args[0].equals(PLUGIN_COMMAND_NAME_WORLD_CREATE)) {
                                WorldType type = WorldType.getByName(args[2].toUpperCase());
                                World.Environment world_environment = World.Environment.valueOf(args[3].toUpperCase());
                                Boolean structures = Boolean.valueOf(args[4]);
                                WorldHelper.createWorld(args[1], type, world_environment, structures);
                                return true;
                            }
                            else{
                                // WRONG SUB COMMAND
                            }
                        }
                    default:
                        break;
                }
            }
        }
        return false;
    }
}
