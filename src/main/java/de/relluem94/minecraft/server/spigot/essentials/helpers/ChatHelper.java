package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author rellu
 */
public class ChatHelper {
    public static void sendMessage(CommandSender sender, String m) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(m);
        } else {
            consoleSendMessage(m, "");
        }
    }

    public static void consoleSendMessage(String type, String message) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(type + " " + message);
    }
}
