package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.MiscMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;

/**
 * Data Access Object for retrieving drop entries from the database.
 *
 * @author rellu
 */
public class DropDao {

  private final QueryExecutor queryExecutor;

  /**
   * Creates a new {@code DropDao} with the given {@link QueryExecutor}.
   *
   * @param queryExecutor the executor used to run SQL queries against the database
   */
  public DropDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Retrieves all drop entries from the database.
   *
   * @return a {@link java.util.List} of all {@link DropEntry} records
   */
  public List<DropEntry> findAll() {
    return queryExecutor.queryList("getDrops.sql", _ -> {
    }, MiscMapper::mapDrop);
  }
}