package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.rellulib.utils.StringUtils.*;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BROADCAST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BROADCAST_TITLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Broadcast implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_BROADCAST)) {
            return false;
        }

        if (args.length <= 1) {
            sendMessage(sender, PLUGIN_COMMAND_BROADCAST_INFO);
            return true;
        }

        Player p = null;
        if (isPlayer(sender)) {
            p = (Player) sender; 
        }

        if (p != null && !Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_BROADCAST_TITLE)) {
            return broadcast(args, 1, false);
        } 

        return broadcast(args, 0, true);
    }

    private boolean broadcast(String[] args, int start, boolean chat) {
        String message = implode(start, args);
        message = replaceSymbols(replaceColor(message));

        if (chat) {
            Bukkit.broadcastMessage(PLUGIN_BROADCAST_NAME + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + message);
            return true;
        }

        for (Player op : Bukkit.getOnlinePlayers()) {
            op.sendTitle(PLUGIN_BROADCAST_NAME, message, 5, 80, 5);
        }
        return true;
    }
}