package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.UndoHistoryRepository;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class UndoHistoryService {

  private final UndoHistoryRepository undoHistoryRepository;

  public UndoHistoryService(UndoHistoryRepository undoHistoryRepository) {
    this.undoHistoryRepository = undoHistoryRepository;
  }

  public void addHistory(Player player, List<ModifyHistoryEntry> history) {
    undoHistoryRepository.add(player, history);
  }

  public @Nullable List<ModifyHistoryEntry> popLastHistory(Player player) {
    if (!undoHistoryRepository.hasHistory(player)) {
      return null;
    }
    List<ModifyHistoryEntry> lastHistory = undoHistoryRepository.findByPlayer(player).getLast();
    undoHistoryRepository.removeLast(player);
    return lastHistory;
  }
}