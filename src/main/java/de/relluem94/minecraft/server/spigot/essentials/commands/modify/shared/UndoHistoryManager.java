package de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.addUndoHistory;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class UndoHistoryManager {

  public void add(Player player, List<ModifyHistoryEntry> history) {
    addUndoHistory(player, history);
  }

  public @Nullable List<ModifyHistoryEntry> popLastHistory(Player player) {
    List<List<ModifyHistoryEntry>> playerUndo = RelluEssentials.getInstance().undo.get(player);
    if (playerUndo == null || playerUndo.isEmpty()) {
      return null;
    }
    return playerUndo.removeLast();
  }
}