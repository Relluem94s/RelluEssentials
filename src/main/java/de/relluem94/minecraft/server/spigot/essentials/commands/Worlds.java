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

            if (!isPlayer(sender)) {
                sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
                return true;
            }

            Player p = (Player) sender;

            if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }

            if(args.length == 0){
                p.sendMessage(PLUGIN_COMMAND_WORLD_INFO);
                return true;
            }

            if(args.length == 1){
                if (!args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_WORLD_LIST)) {
                    teleportWorld(p, args[0]);
                    return true;
                }

                if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }

                sendMessage(p, PLUGIN_COMMAND_WORLD);
                Bukkit.getWorlds().forEach(w -> sendMessage(p, w.getName()));
                return true;
            }

            if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }

            if(args.length == 2){
                switch (args[0]) {
                    case PLUGIN_COMMAND_NAME_WORLD_LOAD:
                        WorldHelper.loadWorld(args[1]);
                        p.sendMessage(PLUGIN_COMMAND_WORLD_LOAD_WORLD);
                        return true;
                    case PLUGIN_COMMAND_NAME_WORLD_UNLOAD:
                        return unloadWorld(p, args[1], true);
                    case PLUGIN_COMMAND_NAME_WORLD_UNLOAD_NO_SAVE:
                        return unloadWorld(p, args[1], false);
                    case PLUGIN_COMMAND_NAME_WORLD_CREATE:
                    default:
                        p.sendMessage(PLUGIN_COMMAND_WORLD_CREATE_INFO);
                        return true;
                }
            }

            if(args.length == 5){
                if (!args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_WORLD_CREATE)) {
                    p.sendMessage(PLUGIN_COMMAND_WORLD_WRONG_SUBCOMMAND);
                    return true;
                }

                createWorld(p, args);
                return true;
            }

        }
        return false;
    }

    private void createWorld(Player p, String[] args){
        if(WorldType.getByName(args[2].toUpperCase()) != null && World.Environment.valueOf(args[3].toUpperCase()) != null && Boolean.valueOf(args[4]) != null){
            WorldType type = WorldType.getByName(args[2].toUpperCase());
            World.Environment worldEnvironment = World.Environment.valueOf(args[3].toUpperCase());
            Boolean structures = Boolean.valueOf(args[4]);
            WorldHelper.createWorld(args[1], type, worldEnvironment, structures);
            p.sendMessage(PLUGIN_COMMAND_WORLD_CREATE_WORLD);
        }
        else{
            p.sendMessage(PLUGIN_COMMAND_WORLD_WRONG_ARGUMENTS);
        }
    }

    private boolean unloadWorld(Player p, String name, boolean save){
        try {
            WorldHelper.unloadWorld(name, save);
            p.sendMessage(save ? PLUGIN_COMMAND_WORLD_UNLOAD_WORLD : PLUGIN_COMMAND_WORLD_UNLOAD_WORLD_NO_SAVE);
            
        }
        catch (WorldNotLoadedException ex) {
                Logger.getLogger(Worlds.class.getName()).log(Level.SEVERE, PLUGIN_COMMAND_WORLD_WORLD_NOT_LOADED, ex);
        }
        return true;
    }

    private void teleportWorld(Player p, String name){
        World w = Bukkit.getWorld(name);
        if (w == null) {
            p.sendMessage(String.format(PLUGIN_COMMAND_WORLD_NOT_FOUND, name));
            return;
        }

        Back.addBackPoint(p);
        p.teleport(w.getSpawnLocation());
        p.sendMessage(String.format(PLUGIN_COMMAND_SPAWN, p.getWorld().getName()));
    }
}
