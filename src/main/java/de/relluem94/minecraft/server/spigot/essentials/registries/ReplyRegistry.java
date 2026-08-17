package de.relluem94.minecraft.server.spigot.essentials.registries;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 * Registry that manages bidirectional reply relationships between players.
 */
public class ReplyRegistry {

  private final Map<Player, Player> replyMap = new HashMap<>();

  /**
   * Registers a bidirectional reply relationship between two players.
   * Existing relationships for either player will be removed.
   *
   * @param sender The first player in the relationship.
   * @param target The second player in the relationship.
   */
  public void register(Player sender, Player target) {
    unregister(sender);
    unregister(target);
    replyMap.put(sender, target);
    replyMap.put(target, sender);
  }

  /**
   * Retrieves the player that the given sender can reply to.
   *
   * @param sender The player looking for a reply target.
   * @return The target player, or null if no relationship exists.
   */
  public Player findReplyTarget(Player sender) {
    return replyMap.get(sender);
  }

  /**
   * Checks if the given player has a registered reply target.
   *
   * @param sender The player to check.
   * @return true if a relationship exists, false otherwise.
   */
  public boolean hasReplyTarget(Player sender) {
    return replyMap.containsKey(sender);
  }

  /**
   * Removes the player from any existing reply relationships.
   *
   * @param player The player to unregister.
   */
  public void unregister(Player player) {
    Player target = replyMap.remove(player);
    if (target != null) {
      replyMap.remove(target);
    }
  }
}