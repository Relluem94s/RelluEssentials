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
                        p.sendMessage(PLUGIN_COMMAND_WORLD_INFO);
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
                                    Back.addBackPoint(p);
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
                                    p.sendMessage(PLUGIN_COMMAND_WORLD_CREATE_INFO);
                                    return true;
                                case PLUGIN_COMMAND_NAME_WORLD_LOAD:
                                    WorldHelper.loadWorld(args[1]);
                                    p.sendMessage(PLUGIN_COMMAND_WORLD_LOAD_WORLD);
                                    return true;
                                case PLUGIN_COMMAND_NAME_WORLD_UNLOAD:
                                    try {
                                        WorldHelper.unloadWorld(args[1], true);
                                        p.sendMessage(PLUGIN_COMMAND_WORLD_UNLOAD_WORLD);
                                    }
                                    catch (WorldNotLoadedException ex) {
                                         Logger.getLogger(Worlds.class.getName()).log(Level.SEVERE, PLUGIN_COMMAND_WORLD_WORLD_NOT_LOADED, ex);
                                    }
                                    return true;
                                case PLUGIN_COMMAND_NAME_WORLD_UNLOAD_NO_SAVE:
                                    try {
                                        WorldHelper.unloadWorld(args[1], false);
                                        p.sendMessage(PLUGIN_COMMAND_WORLD_UNLOAD_WORLD_NO_SAVE);
                                    }
                                    catch (WorldNotLoadedException ex) {
                                        Logger.getLogger(Worlds.class.getName()).log(Level.SEVERE, PLUGIN_COMMAND_WORLD_WORLD_NOT_LOADED, ex);
                                    }
                                    return true;
                                default:
                                    p.sendMessage(PLUGIN_COMMAND_WORLD_CREATE_INFO);
                                    return true;
                            }
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        }
                    case 5:
                        if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                            if (args[0].equals(PLUGIN_COMMAND_NAME_WORLD_CREATE)) {
                                if(WorldType.getByName(args[2].toUpperCase()) != null && World.Environment.valueOf(args[3].toUpperCase()) != null && Boolean.valueOf(args[4]) != null){
                                    WorldType type = WorldType.getByName(args[2].toUpperCase());
                                    World.Environment world_environment = World.Environment.valueOf(args[3].toUpperCase());
                                    Boolean structures = Boolean.valueOf(args[4]);
                                    WorldHelper.createWorld(args[1], type, world_environment, structures);
                                    p.sendMessage(PLUGIN_COMMAND_WORLD_CREATE_WORLD);
                                }
                                else{
                                    p.sendMessage(PLUGIN_COMMAND_WORLD_WRONG_ARGUMENTS);
                                }
                                return true;
                            } else {
                                p.sendMessage(PLUGIN_COMMAND_WORLD_WRONG_SUBCOMMAND);
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
