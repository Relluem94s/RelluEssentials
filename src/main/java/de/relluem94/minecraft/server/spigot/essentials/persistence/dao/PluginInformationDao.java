package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.MiscMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;

/**
 * Data Access Object for retrieving plugin information from the database.
 *
 * @author rellu
 */
public class PluginInformationDao {

  private final QueryExecutor queryExecutor;

  /**
   * Creates a new {@link PluginInformationDao} with the given {@link QueryExecutor}.
   *
   * @param queryExecutor the executor used to run SQL queries against the database
   */
  public PluginInformationDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Retrieves the plugin information entry from the database.
   *
   * @return the {@link PluginInformationEntry} containing the stored plugin information
   */
  public PluginInformationEntry find() {
    return queryExecutor.querySingle("getPluginInformation.sql", _ -> {},
        MiscMapper::mapPluginInformation);
  }
}