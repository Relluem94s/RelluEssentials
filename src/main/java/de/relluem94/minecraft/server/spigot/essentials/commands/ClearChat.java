package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.bukkit.Bukkit;

public class ClearChat implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("cc")) {
            if (Permission.isAuthorized(sender, Groups.getGroup("mod").getId())) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    for (int i = 0; i <= 60; i++) {
                        sendMessage(player, "");
                    }
                });
                return true;
            } else {
                sendMessage(sender, PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }
        }
        return false;
    }
}
