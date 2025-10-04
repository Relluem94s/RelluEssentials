package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 *
 * @author rellu
 */
public class ChatHelper {

    private ChatHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

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
     * @param type Prefix to add before message
     * @param message Message to send
     * @param repeat how often should the message be sent
     */
    public static void consoleSendMessage(String type, String message, int repeat) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for(int i= 0; i <= repeat; i++){
            console.sendMessage(type + " " + message);
        }
    }

    /**
     * 
     * @param message String
     * @param p Player
     * @param channel String
     * @param group GroupEntry
     */
    public static void sendMessageInChannel(String message, Player p, String channel, GroupEntry group) {
        message = message.replaceFirst(channel, "");
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (Permission.isAuthorized(op, group.getId())) {
                sendMessage(op, p.getCustomName() + group.getPrefix() + PLUGIN_FORMS_SPACER_CHANNEL + PLUGIN_COLOR_MESSAGE + replaceColor(message));
            }
        }
    }

    /**
     * 
     * @param message String
     * @param sender String
     * @param channel String
     * @param group GroupEntry
     */
    public static void sendMessageInChannel(String message, String sender, String channel, GroupEntry group) {
        message = message.replaceFirst(channel, "");
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (Permission.isAuthorized(op, group.getId())) {
                sendMessage(op, sender + group.getPrefix() + PLUGIN_FORMS_SPACER_CHANNEL + PLUGIN_COLOR_MESSAGE + replaceColor(message));
            }
        }
    }

    /**
     * 
     * @param p Player
     * @param message String
     */
    public static void sendMessageInActionBar(Player p, String message){
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static void msg(CommandSender sender, Player target, String[] args, int start) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return;
        }

        String message = implode(start, args);

        Player p = (Player) sender;
        if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            message = replaceSymbols(replaceColor(message));
        }

        if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            target.sendMessage(p.getCustomName() + PLUGIN_COMMAND_MSG_SPACER_IN + message);
            p.sendMessage(target.getCustomName() + PLUGIN_COMMAND_MSG_SPACER_OUT + message);
        } else {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
        }
    }
}
