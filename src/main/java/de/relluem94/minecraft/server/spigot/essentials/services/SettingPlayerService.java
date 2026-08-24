package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerSetting;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingPlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingPlayerRepository;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;

/**
 * Service responsible for managing player-specific settings by coordinating
 * between the {@link SettingPlayerRegistry} (cache) and {@link SettingPlayerRepository} (database).
 */
public class SettingPlayerService {

  private final SettingPlayerRegistry settingPlayerRegistry;
  private final SettingPlayerRepository settingPlayerRepository;
  private final ServiceContext serviceContext;

  /**
   * Constructs a new SettingPlayerService.
   *
   * @param settingPlayerRegistry the registry used for caching player settings
   * @param settingPlayerRepository the repository used for persistent storage of player settings
   * @param serviceContext the service context providing access to other services
   */
  public SettingPlayerService(
      SettingPlayerRegistry settingPlayerRegistry,
      SettingPlayerRepository settingPlayerRepository,
      ServiceContext serviceContext
  ) {
    this.settingPlayerRegistry = settingPlayerRegistry;
    this.settingPlayerRepository = settingPlayerRepository;
    this.serviceContext = serviceContext;
  }

  /**
   * Loads all settings for a specific player from the repository into the registry.
   *
   * @param playerId the unique identifier of the player
   */
  @SuppressWarnings("unused")
  public void loadAllForPlayer(int playerId) {
    List<SettingPlayerEntry> entries = settingPlayerRepository.findAllByPlayerId(playerId);
    settingPlayerRegistry.loadAllForPlayer(playerId, entries);
  }

  /**
   * Finds a specific setting entry by its unique ID, checking the registry
   * first and then the repository.
   *
   * @param id the unique identifier of the setting entry
   * @return an {@link Optional} containing the found setting entry, or empty if not found
   */
  public Optional<SettingPlayerEntry> findById(int id) {
    return settingPlayerRegistry.findById(id)
        .or(() -> settingPlayerRepository.findById(id)
            .map(entry -> {
              settingPlayerRegistry.put(entry);
              return entry;
            }));
  }

  /**
   * Finds a specific setting entry for a player by both player ID and setting ID.
   *
   * @param playerId the unique identifier of the player
   * @param settingId the unique identifier of the setting
   * @return an {@link Optional} containing the found setting entry, or empty if not found
   */
  @SuppressWarnings("unused")
  public Optional<SettingPlayerEntry> findByPlayerIdAndSettingId(int playerId, int settingId) {
    return settingPlayerRegistry.findByPlayerIdAndSettingId(playerId, settingId)
        .or(() -> settingPlayerRepository.findAllByPlayerId(playerId).stream()
            .filter(entry -> entry.getSettingFk() == settingId)
            .findFirst()
            .map(entry -> {
              settingPlayerRegistry.put(entry);
              return entry;
            }));
  }

  /**
   * Retrieves all settings for a specific player, loading them
   * into the registry if not already present.
   *
   * @param playerId the unique identifier of the player
   * @return a list of all setting entries for the player
   */
  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    List<SettingPlayerEntry> cached = settingPlayerRegistry.findAllByPlayerId(playerId);
    if (!cached.isEmpty()) {
      return cached;
    }
    List<SettingPlayerEntry> entries = settingPlayerRepository.findAllByPlayerId(playerId);
    settingPlayerRegistry.loadAllForPlayer(playerId, entries);
    return entries;
  }

  /**
   * Inserts a new setting entry into both the repository and the registry.
   *
   * @param entry the setting entry to insert
   */
  public void insert(SettingPlayerEntry entry) {
    settingPlayerRepository.insert(entry);
    settingPlayerRegistry.put(entry);
  }

  /**
   * Updates an existing setting entry in both the repository and the registry.
   *
   * @param entry the setting entry containing updated values
   */
  public void update(SettingPlayerEntry entry) {
    settingPlayerRepository.update(entry);
    settingPlayerRegistry.put(entry);
  }

  /**
   * Performs a soft delete of a setting entry in the repository and removes it from the registry.
   *
   * @param id the unique identifier of the setting entry to delete
   * @param deletedBy the ID of the player performing the deletion
   */
  public void delete(int id, int deletedBy) {
    settingPlayerRepository.softDelete(id, deletedBy);
    settingPlayerRegistry.remove(id);
  }

  /**
   * Checks if a specific setting is active for a given player.
   *
   * @param player the player to check
   * @param playerSetting the setting type to verify
   * @return true if the setting is found and its value is true, false otherwise
   */
  public boolean isSettingActiveForPlayer(Player player, PlayerSetting playerSetting) {
    PlayerEntry playerEntry = serviceContext.getPlayerService().getPlayerEntry(player);
    return findAllByPlayerId(playerEntry.getId()).stream()
        .filter(entry -> playerSetting.name().equals(entry.getSettingEntry().getName()))
        .findFirst()
        .map(SettingPlayerEntry::isValue)
        .orElse(false);
  }
}