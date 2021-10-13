package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

public class Day implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("day")) {
            if (args.length == 0) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        p.getWorld().setTime(0L);
                        sendMessage(p, String.format(PLUGIN_COMMAND_DAY, p.getWorld().getName()));
                        return true;
                    } else {
                        sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
