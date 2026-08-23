package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 * Service responsible for managing player-specific clipboards. A clipboard stores selection data
 * and a list of modifications.
 */
public class ClipboardService {

  private final Map<Player, DoubleStore<
      Selection, List<ModifyClipboardEntry>>> playerClipboards = new HashMap<>();

  /**
   * Retrieves the clipboard associated with the specified player.
   *
   * @param player The player whose clipboard is being requested.
   * @return The player's clipboard, or null if no clipboard is assigned.
   */
  public DoubleStore<Selection, List<ModifyClipboardEntry>> getClipboard(Player player) {
    return playerClipboards.get(player);
  }

  /**
   * Assigns a new clipboard to the specified player.
   *
   * @param player    The player to whom the clipboard will be assigned.
   * @param clipboard The clipboard data to store.
   */
  public void setClipboard(Player player,
      DoubleStore<Selection, List<ModifyClipboardEntry>> clipboard) {
    playerClipboards.put(player, clipboard);
  }

  /**
   * Removes the clipboard associated with the specified player.
   *
   * @param player The player whose clipboard should be removed.
   */
  public void removeClipboard(Player player) {
    playerClipboards.remove(player);
  }
}