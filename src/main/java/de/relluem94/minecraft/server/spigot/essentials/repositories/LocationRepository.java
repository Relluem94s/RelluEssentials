package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;

public class LocationRepository {

  private final LocationDao locationDao;

  public LocationRepository(LocationDao locationDao) {
    this.locationDao = locationDao;
  }

  public int removeOutdatedLocations() {
    return locationDao.deleteOutdatedLocations();
  }
}