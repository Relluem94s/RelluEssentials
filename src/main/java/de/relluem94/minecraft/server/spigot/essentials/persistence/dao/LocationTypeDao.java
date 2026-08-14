package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.LocationMapper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LocationTypeDao {

  private final QueryExecutor queryExecutor;

  public List<LocationTypeEntry> findAll() {
    return queryExecutor.queryList("getLocationTypes.sql", _ -> {}, LocationMapper::mapLocationType);
  }
}