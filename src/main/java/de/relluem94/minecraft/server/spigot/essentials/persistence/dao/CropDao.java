package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.MiscMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;

/**
 * Data Access Object for retrieving crop-related data from the database.
 *
 * @author rellu
 */
public class CropDao {

  private final QueryExecutor queryExecutor;

  /**
   * Creates a new {@code CropDao} with the given {@link QueryExecutor}.
   *
   * @param queryExecutor the executor used to run SQL queries against the database
   */
  public CropDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Retrieves all crop entries from the database.
   *
   * @return a {@link List} of all {@link CropEntry} records
   */
  public List<CropEntry> findAll() {
    return queryExecutor.queryList("getCrops.sql", _ -> {
    }, MiscMapper::mapCrop);
  }
}