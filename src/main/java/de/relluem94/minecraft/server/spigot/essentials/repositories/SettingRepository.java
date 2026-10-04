package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.SettingDao;
import java.util.List;

/**
 * Repository for accessing and managing {@link SettingEntry} data.
 * Delegates persistence operations to the underlying {@link SettingDao}.
 */
public class SettingRepository {

  private final SettingDao settingDao;

  /**
   * Creates a new {@code SettingRepository} with the given data access object.
   *
   * @param settingDao the DAO used to perform persistence operations on settings
   */
  public SettingRepository(SettingDao settingDao) {
    this.settingDao = settingDao;
  }

  /**
   * Retrieves all available setting entries.
   *
   * @return a {@link List} of all {@link SettingEntry} instances
   */
  public List<SettingEntry> findAll() {
    return settingDao.findAll();
  }
}