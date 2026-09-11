package de.relluem94.minecraft.server.spigot.essentials.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Repository for managing the buy-back history of players.
 * Stores items that players have sold, allowing them to repurchase them.
 *
 * @author rellu
 */
public class BuyBackRepository {

  private final Map<Player, List<ItemStack>> buyBackHistory = new HashMap<>();

  /**
   * Adds a list of items to the buy-back history of the given player.
   *
   * @param player the player whose buy-back history is updated
   * @param items  the list of items to add to the history
   */
  public void addItems(Player player, List<ItemStack> items) {
    buyBackHistory.putIfAbsent(player, new ArrayList<>());
    buyBackHistory.get(player).addAll(items);
  }

  /**
   * Returns the buy-back history of the given player.
   *
   * @param player the player whose buy-back history is retrieved
   * @return a list of items in the player's buy-back history, or an empty list if none exist
   */
  public List<ItemStack> findByPlayer(Player player) {
    return buyBackHistory.getOrDefault(player, new ArrayList<>());
  }

  /**
   * Removes the last item from the buy-back history of the given player.
   * Does nothing if the player has no history or the history is empty.
   *
   * @param player the player whose last buy-back history entry is removed
   */
  public void removeLastEntry(Player player) {
    List<ItemStack> history = buyBackHistory.get(player);
    if (history != null && !history.isEmpty()) {
      history.removeLast();
    }
  }

  /**
   * Deletes the entire buy-back history of the given player.
   *
   * @param player the player whose buy-back history is deleted
   */
  public void deleteByPlayer(Player player) {
    buyBackHistory.remove(player);
  }
}