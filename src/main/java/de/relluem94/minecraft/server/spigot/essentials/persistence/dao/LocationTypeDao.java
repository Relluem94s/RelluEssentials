package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.LocationMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 * Data Access Object for retrieving location type data from the database.
 */
@AllArgsConstructor
public class LocationTypeDao {

  private final QueryExecutor queryExecutor;

  /**
   * Retrieves all location type entries from the database.
   *
   * @return a list of all {@link LocationTypeEntry} records
   */
  public List<LocationTypeEntry> findAll() {
    return queryExecutor.queryList(
        "getLocationTypes.sql",
        _ -> {},
        LocationMapper::mapLocationType
    );
  }
}