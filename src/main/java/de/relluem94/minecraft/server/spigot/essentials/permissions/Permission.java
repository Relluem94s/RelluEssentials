package de.relluem94.minecraft.server.spigot.essentials.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Permission {

    /**
     * Checks if Player is Authorized Player has to be in a Group with id >=
     * group
     *
     * @param p Player
     * @param group long
     * @return
     */
    public static boolean isAuthorized(Player p, long group) {
        long id = User.getGroup(p).getId();
        return id >= group;
    }

    /**
     * Checks if CommandSender is Authorized
     *
     * @param sender CommandSender
     * @param group long
     * @return boolean
     */
    public static boolean isAuthorized(CommandSender sender, long group) {
        if (isPlayer(sender)) {
            return isAuthorized((Player) sender, group);
        } else if (isCMDBlock(sender)) {
            return true;
        } else if (isConsole(sender)) {
            return true;
        }

        return false;
    }
}
