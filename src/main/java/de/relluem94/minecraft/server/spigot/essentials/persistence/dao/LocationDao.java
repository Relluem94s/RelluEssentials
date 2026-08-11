package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;

public class LocationDao {

  private final QueryExecutor queryExecutor;

  public LocationDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public int deleteOutdatedLocations() {
    return queryExecutor.executeUpdateWithCount("cleanupLocations.sql", _ -> {});
  }
}