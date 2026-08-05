package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerRegistry {

  @Getter
  private final Map<UUID, PlayerEntry> playerEntryMap = new HashMap<>();

  public PlayerRegistry() {
  }

  public void putPlayerEntry(UUID uuid, PlayerEntry playerEntry) {
    playerEntryMap.put(uuid, playerEntry);
  }

  public PlayerEntry getPlayerEntry(UUID uuid) {
    return playerEntryMap.get(uuid);
  }

  public PlayerEntry getPlayerEntry(int id) {
    for (PlayerEntry playerEntry : playerEntryMap.values()) {
      if (playerEntry.getId() == id) {
        return playerEntry;
      }
    }
    return null;
  }

  public PlayerEntry getPlayerEntry(Player player) {
    return playerEntryMap.get(player.getUniqueId());
  }

  public void clearPlayerEntries() {
    playerEntryMap.clear();
  }
}