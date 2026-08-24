package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import java.util.List;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Repository responsible for managing the persistence and retrieval of location entries.
 */
public class LocationRepository {

  private final LocationDao locationDao;

  /**
   * Creates a new LocationRepository.
   *
   * @param locationDao the data access object used for database operations
   */
  public LocationRepository(LocationDao locationDao) {
    this.locationDao = locationDao;
  }

  /**
   * Removes locations that are no longer valid based on the defined expiration logic.
   *
   * @return the number of outdated locations removed
   */
  public int removeOutdatedLocations() {
    return locationDao.deleteOutdatedLocations();
  }

  /**
   * Finds a specific location entry based on coordinates and type.
   *
   * @param location the bukkit location to search for
   * @param typeId the identifier for the location type
   * @return the matching location entry, or null if not found
   */
  public LocationEntry findByLocationAndType(@NotNull Location location, int typeId) {
    return locationDao.getLocation(location, typeId);
  }

  /**
   * Finds a location entry by its unique identifier.
   *
   * @param id the unique identifier of the location entry
   * @return the matching location entry, or null if not found
   */
  public LocationEntry findById(int id) {
    return locationDao.findById(id);
  }

  /**
   * Saves a new location entry to the database.
   *
   * @param locationEntry the entry to be saved
   */
  public void save(@NotNull LocationEntry locationEntry) {
    locationDao.insertLocation(locationEntry);
  }

  /**
   * Deletes a specific location entry.
   *
   * @param locationEntry the entry to be deleted
   */
  public void delete(@NotNull LocationEntry locationEntry) {
    locationDao.deleteLocation(locationEntry);
  }

  /**
   * Deletes a location entry by its ID and the owner's player ID.
   *
   * @param id the unique identifier of the location entry
   * @param playerId the identifier of the player who owns the location
   */
  public void deleteById(int id, int playerId) {
    locationDao.deleteById(id, playerId);
  }

  /**
   * Retrieves all location entries belonging to a specific player and type.
   *
   * @param playerId the identifier of the player
   * @param typeId the identifier for the location type
   * @return a list of matching location entries
   */
  public List<LocationEntry> findByPlayerAndType(int playerId, int typeId) {
    return locationDao.getLocations(playerId, typeId);
  }

  /**
   * Retrieves all location entries of a specific type.
   *
   * @param typeId the identifier for the location type
   * @return a list of matching location entries
   */
  public List<LocationEntry> findByType(int typeId) {
    return locationDao.getLocationsByType(typeId);
  }
}