package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author rellu
 */
public class TypeHelper {
    public static boolean isInt(String s) {
        try {
            @SuppressWarnings("unused")
            int i = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            @SuppressWarnings("unused")
            double i = Double.parseDouble(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean isFloat(String s) {
        try {
            @SuppressWarnings("unused")
            double i = Float.parseFloat(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }
    
     public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static boolean isCMDBlock(CommandSender sender) {
        return sender instanceof BlockCommandSender;
    }

    public static boolean isConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender;
    }
}
