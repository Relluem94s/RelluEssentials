package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.stringHelper;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.implode;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class Broadcast implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("broadcast")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("title")) {
                    return broadcast(sender, args, 1, false);
                } else {
                    return broadcast(sender, args, 0, true);
                }
            } else {
                sender.sendMessage(PLUGIN_COMMAND_BROADCAST_INFO);
                return true;
            }
        }
        return false;
    }

    private boolean broadcast(CommandSender sender, String[] args, int start, boolean chat) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!Permission.isAuthorized(p, Groups.MOD.getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }
        }

        String message = implode(start, args);
        message = stringHelper.replaceSymbols(replaceColor(message));

        if (chat) {
            Bukkit.broadcastMessage(PLUGIN_BROADCAST_NAME + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + message);
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(PLUGIN_BROADCAST_NAME, message, 5, 80, 5);
            }
        }
        return true;
    }
}
