package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

/**
 * Registry responsible for managing and retrieving {@link PlayerEntry} objects.
 */
@NoArgsConstructor
public class PlayerRegistry {

  /**
   * Map containing the mapping between player UUIDs and their corresponding entries.
   */
  @Getter
  private final Map<UUID, PlayerEntry> playerEntryMap = new HashMap<>();

  /**
   * Associates a player entry with a specific UUID.
   *
   * @param uuid        the unique identifier of the player
   * @param playerEntry the entry to be stored
   */
  public void putPlayerEntry(UUID uuid, PlayerEntry playerEntry) {
    playerEntryMap.put(uuid, playerEntry);
  }

  /**
   * Retrieves the player entry associated with the given UUID.
   *
   * @param uuid the unique identifier of the player
   * @return the player entry, or {@code null} if no entry exists for the UUID
   */
  public PlayerEntry getPlayerEntry(UUID uuid) {
    return playerEntryMap.get(uuid);
  }

  /**
   * Retrieves the player entry associated with the given integer ID.
   *
   * @param id the unique integer ID of the player
   * @return the player entry, or {@code null} if no entry matches the ID
   */
  public PlayerEntry getPlayerEntry(int id) {
    for (PlayerEntry playerEntry : playerEntryMap.values()) {
      if (playerEntry.getId() == id) {
        return playerEntry;
      }
    }
    return null;
  }

  /**
   * Retrieves the player entry associated with the given Bukkit Player.
   *
   * @param player the Bukkit player instance
   * @return the player entry, or {@code null} if no entry exists for the player
   */
  public PlayerEntry getPlayerEntry(Player player) {
    return playerEntryMap.get(player.getUniqueId());
  }

  /**
   * Retrieves a list of all currently registered player entries.
   *
   * @return a new list containing all player entries
   */
  public List<PlayerEntry> getAllPlayerEntries() {
    return new ArrayList<>(playerEntryMap.values());
  }

  /**
   * Removes all player entries from the registry.
   */
  public void clearPlayerEntries() {
    playerEntryMap.clear();
  }
}