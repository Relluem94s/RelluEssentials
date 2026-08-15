package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import java.util.List;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class LocationRepository {

  private final LocationDao locationDao;

  public LocationRepository(LocationDao locationDao) {
    this.locationDao = locationDao;
  }

  public int removeOutdatedLocations() {
    return locationDao.deleteOutdatedLocations();
  }

  public LocationEntry findByLocationAndType(@NotNull Location location, int typeId) {
    return locationDao.getLocation(location, typeId);
  }

  public LocationEntry findById(int id) {
    return locationDao.findById(id);
  }

  public void save(@NotNull LocationEntry locationEntry) {
    locationDao.insertLocation(locationEntry);
  }

  public void delete(@NotNull LocationEntry locationEntry) {
    locationDao.deleteLocation(locationEntry);
  }

  public void deleteById(int id, int playerId) {
    locationDao.deleteById(id, playerId);
  }

  public List<LocationEntry> findByPlayerAndType(int playerId, int typeId) {
    return locationDao.getLocations(playerId, typeId);
  }

  public List<LocationEntry> findByType(int typeId) {
    return locationDao.getLocationsByType(typeId);
  }

}