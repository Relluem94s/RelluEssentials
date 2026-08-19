package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing {@link SettingPlayerEntry} instances. Provides methods to store, retrieve,
 * and filter player-specific settings.
 */
public class SettingPlayerRegistry {

  private final Map<Integer, SettingPlayerEntry> entriesById = new ConcurrentHashMap<>();

  /**
   * Loads all settings for a specific player by removing existing entries for that player and
   * adding the provided list of entries.
   *
   * @param playerId the unique identifier of the player
   * @param entries  the list of settings to load for the player
   */
  public void loadAllForPlayer(int playerId, List<SettingPlayerEntry> entries) {
    entriesById.entrySet().removeIf(e -> e.getValue().getPlayerFk() == playerId);
    entries.forEach(entry -> entriesById.put(entry.getId(), entry));
  }

  /**
   * Finds a specific setting entry by its unique ID.
   *
   * @param id the unique identifier of the entry
   * @return an {@link Optional} containing the entry if found, or empty otherwise
   */
  public Optional<SettingPlayerEntry> findById(int id) {
    return Optional.ofNullable(entriesById.get(id));
  }

  /**
   * Finds a specific setting entry for a given player and setting type.
   *
   * @param playerId  the unique identifier of the player
   * @param settingId the unique identifier of the setting
   * @return an {@link Optional} containing the entry if found, or empty otherwise
   */
  public Optional<SettingPlayerEntry> findByPlayerIdAndSettingId(int playerId, int settingId) {
    return entriesById.values().stream()
        .filter(entry -> entry.getPlayerFk() == playerId && entry.getSettingFk() == settingId)
        .findFirst();
  }

  /**
   * Retrieves all setting entries associated with a specific player.
   *
   * @param playerId the unique identifier of the player
   * @return an unmodifiable list of all settings for the player
   */
  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    return entriesById.values().stream()
        .filter(entry -> entry.getPlayerFk() == playerId).toList();
  }

  /**
   * Adds or updates a setting entry in the registry.
   *
   * @param entry the entry to be stored
   */
  public void put(SettingPlayerEntry entry) {
    entriesById.put(entry.getId(), entry);
  }

  /**
   * Removes a setting entry from the registry by its ID.
   *
   * @param id the unique identifier of the entry to remove
   */
  public void remove(int id) {
    entriesById.remove(id);
  }
}