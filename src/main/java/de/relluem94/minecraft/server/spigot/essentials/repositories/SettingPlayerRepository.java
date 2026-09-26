package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.SettingPlayerDao;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing {@link SettingPlayerEntry} persistence operations.
 *
 * <p>Acts as an abstraction layer between the application logic and the
 * underlying {@link SettingPlayerDao}, providing a clean API for
 * creating, reading, updating, and soft-deleting player settings.
 *
 * @author rellu
 */
public class SettingPlayerRepository {

  private final SettingPlayerDao settingPlayerDao;

  /**
   * Creates a new {@code SettingPlayerRepository} with the given {@link SettingPlayerDao}.
   *
   * @param settingPlayerDao the DAO used to perform persistence operations on player settings
   */
  public SettingPlayerRepository(SettingPlayerDao settingPlayerDao) {
    this.settingPlayerDao = settingPlayerDao;
  }

  /**
   * Retrieves all setting entries associated with the given player ID.
   *
   * @param playerId the ID of the player whose settings are to be retrieved
   * @return a {@link List} of {@link SettingPlayerEntry} objects belonging to the given player,
   *         or an empty list if none exist
   */
  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    return settingPlayerDao.findAllByPlayerId(playerId);
  }

  /**
   * Retrieves a single setting entry by its unique ID.
   *
   * @param id the unique identifier of the setting entry
   * @return an {@link Optional} containing the found {@link SettingPlayerEntry},
   *         or {@link Optional#empty()} if no entry exists with the given ID
   */
  public Optional<SettingPlayerEntry> findById(int id) {
    return settingPlayerDao.findById(id);
  }

  /**
   * Persists a new {@link SettingPlayerEntry} to the data store.
   *
   * @param entry the setting entry to insert
   */
  public void insert(SettingPlayerEntry entry) {
    settingPlayerDao.insert(entry);
  }

  /**
   * Updates an existing {@link SettingPlayerEntry} in the data store.
   *
   * @param entry the setting entry containing updated values
   */
  public void update(SettingPlayerEntry entry) {
    settingPlayerDao.update(entry);
  }

  /**
   * Marks a setting entry as deleted without physically removing it from the data store.
   *
   * @param id        the unique identifier of the setting entry to soft-delete
   * @param deletedBy the ID of the player or actor performing the deletion
   */
  public void softDelete(int id, int deletedBy) {
    settingPlayerDao.softDelete(id, deletedBy);
  }
}