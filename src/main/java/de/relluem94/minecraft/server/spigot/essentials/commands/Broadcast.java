package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_BROADCAST_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_BROADCAST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BROADCAST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BROADCAST_TITLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

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

        if (!Permission.isAuthorized(sender, Groups.getGroup("mod").getId())) {
            sendMessage(sender, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_BROADCAST_TITLE)) {
            broadcast(args, 1, false);
            return true;
        } 

        broadcast(args, 0, true);
        return true;
    }

    private void broadcast(String[] args, int start, boolean chat) {
        String message = implode(start, args);
        message = replaceSymbols(replaceColor(message));

        if (chat) {
            Bukkit.broadcastMessage(PLUGIN_NAME_BROADCAST + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_COLOR_MESSAGE + message);
            return;
        }

        for (Player op : Bukkit.getOnlinePlayers()) {
            op.sendTitle(PLUGIN_NAME_BROADCAST, message, 5, 80, 5);
        }
    }
}