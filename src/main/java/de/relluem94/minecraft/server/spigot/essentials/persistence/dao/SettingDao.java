package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.SettingMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;

/**
 * Data Access Object for retrieving setting entries from the database.
 *
 * @author rellu
 */
public class SettingDao {

  private final QueryExecutor queryExecutor;

  /**
   * Creates a new {@code SettingDao} with the given {@link QueryExecutor}.
   *
   * @param queryExecutor the executor used to run SQL queries against the database
   */
  public SettingDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Retrieves all setting entries from the database.
   *
   * @return a {@link java.util.List} of all {@link SettingEntry} records
   */
  public List<SettingEntry> findAll() {
    return queryExecutor.queryList("getAllSettings.sql", _ -> {
    }, SettingMapper::mapSetting);
  }
}