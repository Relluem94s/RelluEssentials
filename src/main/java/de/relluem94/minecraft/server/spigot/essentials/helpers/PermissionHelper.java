package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.Generated;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionHelper {

  private static PlayerRegistry playerRegistry = null;

  PermissionHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static void injectPlayerAPI(PlayerRegistry playerRegistry) {
    PermissionHelper.playerRegistry = playerRegistry;
  }

  @Generated
  private static PlayerRegistry resolvePlayerAPI() {
    if (playerRegistry != null) {
      return playerRegistry;
    }
    return RelluEssentials.getInstance().getPlayerRegistry();
  }

  /**
   * Checks if Player is Authorized Player has to be in a Group with id >= group
   *
   * @param player  Player
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
   * @param sender  CommandSender
   * @param groupId long
   * @return boolean
   */
  public static boolean isAuthorized(CommandSender sender, long groupId) {
    if (isPlayer(sender)) {
      return isAuthorized((Player) sender, groupId);
    } else if (isCMDBlock(sender)) {
        return true;
    } else {
        return isConsole(sender);
    }
  }
}
