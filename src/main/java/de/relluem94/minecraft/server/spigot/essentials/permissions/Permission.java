package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.*;

public class Permission {
    private static PlayerAPI injectedPlayerAPI = null;

    private Permission() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static void injectPlayerAPI(PlayerAPI playerAPI) {
        injectedPlayerAPI = playerAPI;
    }

    private static PlayerAPI resolvePlayerAPI() {
        if (injectedPlayerAPI != null) {
            return injectedPlayerAPI;
        }
        return RelluEssentials.getInstance().getPlayerAPI();
    }

    /**
     * Checks if Player is Authorized Player has to be in a Group with id >=
     * group
     *
     * @param player Player
     * @param groupId long
     * @return boolean
     */
    public static boolean isAuthorized(Player player, long groupId) {
        long playerGroupId = resolvePlayerAPI().getPlayerEntry(player).getGroup().getId();
        return playerGroupId >= groupId;
    }

    /**
     * Checks if CommandSender is Authorized
     *
     * @param sender CommandSender
     * @param groupId long
     * @return boolean
     */
    public static boolean isAuthorized(CommandSender sender, long groupId) {
        if (isPlayer(sender)) {
            return isAuthorized((Player) sender, groupId);
        } else if (isCMDBlock(sender)) {
            return true;
        } else return isConsole(sender);
    }
}
