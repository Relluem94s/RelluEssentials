package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.UndoHistoryRepository;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Service responsible for managing the undo history of players.
 */
public class UndoHistoryService {

  private final UndoHistoryRepository undoHistoryRepository;

  /**
   * Constructs a new UndoHistoryService.
   *
   * @param undoHistoryRepository the repository used to persist and retrieve history
   */
  public UndoHistoryService(UndoHistoryRepository undoHistoryRepository) {
    this.undoHistoryRepository = undoHistoryRepository;
  }

  /**
   * Adds a new set of history entries for a specific player.
   *
   * @param player  the player for whom the history is being added
   * @param history the list of modification history entries to add
   */
  public void addHistory(Player player, List<ModifyHistoryEntry> history) {
    undoHistoryRepository.add(player, history);
  }

  /**
   * Removes and returns the last entry from the player's undo history.
   *
   * @param player the player whose history should be popped
   * @return the last list of modification history entries, or {@code null} if no history exists
   */
  public @Nullable List<ModifyHistoryEntry> popLastHistory(Player player) {
    if (!undoHistoryRepository.hasHistory(player)) {
      return null;
    }
    List<ModifyHistoryEntry> lastHistory = undoHistoryRepository.findByPlayer(player).getLast();
    undoHistoryRepository.removeLast(player);
    return lastHistory;
  }
}