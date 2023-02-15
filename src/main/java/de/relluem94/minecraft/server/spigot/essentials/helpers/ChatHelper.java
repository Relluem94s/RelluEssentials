package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

/**
 *
 * @author rellu
 */
public class ChatHelper {

    /**
     *
     * @param sender CommandSender
     * @param message Message to send
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (isPlayer(sender)) {
            Player p = (Player) sender;
            p.sendMessage(message);
        } else {
            consoleSendMessage(message, "");
        }
    }

    /**
     *
     * @param type Prefix to add before message
     * @param message Message to send
     */
    public static void consoleSendMessage(String type, String message) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(type + " " + message);
    }

    /**
     * 
     * @param message
     * @param p
     * @param channel
     * @param group
     */
    public static void sendMessageInChannel(String message, Player p, String channel, GroupEntry group) {
        message = message.replaceFirst(channel, "");
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (Permission.isAuthorized(op, group.getId())) {
                sendMessage(op, p.getCustomName() + group.getPrefix() + PLUGIN_SPACER_CHANNEL + PLUGIN_MESSAGE_COLOR + replaceColor(message));
            }
        }
    }

    /**
     * 
     * @param message
     * @param sender
     * @param channel
     * @param group
     */
    public static void sendMessageInChannel(String message, String sender, String channel, GroupEntry group) {
        message = message.replaceFirst(channel, "");
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (Permission.isAuthorized(op, group.getId())) {
                sendMessage(op, sender + group.getPrefix() + PLUGIN_SPACER_CHANNEL + PLUGIN_MESSAGE_COLOR + replaceColor(message));
            }
        }
    }
}
