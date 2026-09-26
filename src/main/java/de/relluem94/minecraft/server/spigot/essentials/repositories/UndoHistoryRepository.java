package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 * Repository for managing undo history entries per player.
 *
 * <p>Stores a list of history snapshots for each player, where each snapshot
 * represents a set of block modifications that can be undone as a single operation.
 */
public class UndoHistoryRepository {

  private final Map<Player, List<List<ModifyHistoryEntry>>> storage = new HashMap<>();

  /**
   * Adds a new history snapshot for the given player.
   *
   * @param player  the player whose history is being updated
   * @param history the list of block modification entries representing a single undoable operation
   */
  public void add(Player player, List<ModifyHistoryEntry> history) {
    storage.computeIfAbsent(player, _ -> new ArrayList<>()).add(history);
  }

  /**
   * Returns all history snapshots stored for the given player.
   *
   * @param player the player whose history snapshots are requested
   * @return a list of history snapshots, or an empty list if no history exists for the player
   */
  public List<List<ModifyHistoryEntry>> findByPlayer(Player player) {
    return storage.getOrDefault(player, new ArrayList<>());
  }

  /**
   * Removes the most recent history snapshot for the given player.
   *
   * <p>Does nothing if the player has no stored history.
   *
   * @param player the player whose most recent history snapshot should be removed
   */
  public void removeLast(Player player) {
    List<List<ModifyHistoryEntry>> playerHistory = storage.get(player);
    if (playerHistory != null && !playerHistory.isEmpty()) {
      playerHistory.removeLast();
    }
  }

  /**
   * Checks whether the given player has at least one stored history snapshot.
   *
   * @param player the player to check
   * @return {@code true} if the player has at least one history snapshot, {@code false} otherwise
   */
  public boolean hasHistory(Player player) {
    List<List<ModifyHistoryEntry>> playerHistory = storage.get(player);
    return playerHistory != null && !playerHistory.isEmpty();
  }
}