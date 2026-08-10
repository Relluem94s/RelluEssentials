package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;

public class ClipboardService {

  private final Map<Player, DoubleStore<Selection, List<ModifyClipboardEntry>>> playerClipboards = new HashMap<>();

  public DoubleStore<Selection, List<ModifyClipboardEntry>> getClipboard(Player player) {
    return playerClipboards.get(player);
  }

  public void setClipboard(Player player, DoubleStore<Selection, List<ModifyClipboardEntry>> clipboard) {
    playerClipboards.put(player, clipboard);
  }

  public void removeClipboard(Player player) {
    playerClipboards.remove(player);
  }
}