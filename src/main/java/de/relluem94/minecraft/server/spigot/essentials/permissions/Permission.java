package de.relluem94.minecraft.server.spigot.essentials.permissions;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Permission {

    public static boolean isAuthorized(Player p, long group) {
        long id = User.getGroup(p).getId();
        return id >= group;
    }

    public static boolean isAuthorized(CommandSender sender, long group) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            return isAuthorized(p, group);
        } else if (sender instanceof BlockCommandSender) {
            return true;
        } else if (sender instanceof ConsoleCommandSender) {
            return true;
        } else {
            return false;
        }
    }
}
