package de.relluem94.minecraft.server.spigot.essentials.commands;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_NIGHT;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Night implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_NIGHT)) {
            return false;
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            p.getWorld().setTime(18000L);
            p.sendMessage(String.format(PLUGIN_COMMAND_NIGHT, p.getWorld().getName()));
            return true;
        }

        World world = Bukkit.getWorld(args[0]);

        if(world == null){
            p.sendMessage(String.format(PLUGIN_COMMAND_WORLD_NOT_FOUND, args[0]));
            return true;
        }

        world.setTime(18000L);
        p.sendMessage(String.format(PLUGIN_COMMAND_NIGHT, world.getName()));
        return true;
    }
}