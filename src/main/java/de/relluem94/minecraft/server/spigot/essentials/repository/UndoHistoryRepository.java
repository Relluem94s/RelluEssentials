package de.relluem94.minecraft.server.spigot.essentials.repository;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;

public class UndoHistoryRepository {

  private final Map<Player, List<List<ModifyHistoryEntry>>> storage = new HashMap<>();

  public void add(Player player, List<ModifyHistoryEntry> history) {
    storage.computeIfAbsent(player, k -> new ArrayList<>()).add(history);
  }

  public List<List<ModifyHistoryEntry>> findByPlayer(Player player) {
    return storage.getOrDefault(player, new ArrayList<>());
  }

  public void removeLast(Player player) {
    List<List<ModifyHistoryEntry>> playerHistory = storage.get(player);
    if (playerHistory != null && !playerHistory.isEmpty()) {
      playerHistory.removeLast();
    }
  }

  public boolean hasHistory(Player player) {
    List<List<ModifyHistoryEntry>> playerHistory = storage.get(player);
    return playerHistory != null && !playerHistory.isEmpty();
  }
}