package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.ProtectionDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;

/**
 * Repository for managing {@link ProtectionEntry} instances and their associated
 * {@link LocationEntry} data.
 *
 * <p>Acts as an abstraction layer between the service layer and the persistence layer,
 * coordinating operations across {@link ProtectionDao} and {@link LocationDao}.
 */
public class ProtectionRepository {

  private static final Logger logger = Logger.getLogger(ProtectionRepository.class.getName());

  private final ProtectionDao protectionDao;
  private final LocationDao locationDao;

  /**
   * Creates a new {@code ProtectionRepository} with the given DAOs.
   *
   * @param protectionDao the DAO used to access protection data
   * @param locationDao   the DAO used to access location data
   */
  public ProtectionRepository(ProtectionDao protectionDao, LocationDao locationDao) {
    this.protectionDao = protectionDao;
    this.locationDao = locationDao;
  }

  /**
   * Removes all outdated protections from the persistence layer and returns their IDs.
   *
   * @return a list of IDs belonging to the protections that were removed
   */
  public List<Long> removeOutdatedProtections() {
    List<Long> outdatedIds = protectionDao.findOutdatedProtectionIds();
    protectionDao.deleteOutdatedProtections();
    return outdatedIds;
  }

  /**
   * Loads all {@link ProtectionEntry} instances from the persistence layer and
   * maps them by their {@link Location}.
   *
   * <p>Each {@link ProtectionEntry} is enriched with its associated {@link LocationEntry}.
   * Entries whose corresponding {@link LocationEntry} cannot be found are skipped and logged at
   * {@link java.util.logging.Level#SEVERE}.
   *
   * @return a map of {@link Location} to {@link ProtectionEntry} for all valid protection entries
   */
  public Map<Location, ProtectionEntry> loadAll() {
    Map<Location, ProtectionEntry> protectionsByLocation = new HashMap<>();
    protectionDao.findAll().forEach(protectionEntry -> {
      LocationEntry locationEntry = locationDao.findById(protectionEntry.getLocationFk());
      if (locationEntry != null) {
        protectionEntry.setLocationEntry(locationEntry);
        protectionsByLocation.put(locationEntry.getLocation(), protectionEntry);
      } else {
        logger.log(Level.SEVERE,
            "ProtectionEntry ({0}) without LocationEntry found.",
            protectionEntry.getId());
      }
    });
    return protectionsByLocation;
  }

  /**
   * Removes the given {@link ProtectionEntry} and its associated {@link LocationEntry}
   * from the persistence layer.
   *
   * @param protectionEntry the protection entry to remove
   */
  public void remove(ProtectionEntry protectionEntry) {
    protectionDao.deleteById(protectionEntry.getId(),
        protectionEntry.getLocationEntry().getPlayerId());
    locationDao.deleteById(protectionEntry.getLocationEntry().getId(),
        protectionEntry.getLocationEntry().getPlayerId());
  }

  /**
   * Persists the given {@link ProtectionEntry} to the persistence layer.
   *
   * @param protectionEntry the protection entry to save
   */
  public void save(ProtectionEntry protectionEntry) {
    protectionDao.insertProtection(protectionEntry);
  }

  /**
   * Updates the flags of the given {@link ProtectionEntry} in the persistence layer.
   *
   * @param protectionEntry the protection entry whose flags are to be updated
   */
  public void updateFlags(ProtectionEntry protectionEntry) {
    protectionDao.updateProtectionFlag(protectionEntry);
  }

  /**
   * Updates the access rights of the given {@link ProtectionEntry} in the persistence layer.
   *
   * @param protectionEntry the protection entry whose rights are to be updated
   */
  public void updateRights(ProtectionEntry protectionEntry) {
    protectionDao.updateProtectionRight(protectionEntry);
  }

  /**
   * Retrieves the {@link ProtectionEntry} associated with the given {@link Location}.
   *
   * <p>If a matching entry is found, it is enriched with its associated {@link LocationEntry}.
   *
   * @param location the location to look up
   * @return the {@link ProtectionEntry} at the given location, or {@code null} if none exists
   */
  public ProtectionEntry findByLocation(Location location) {
    ProtectionEntry protectionEntry = protectionDao.getProtectionByLocation(location);
    if (protectionEntry != null) {
      LocationEntry locationEntry = locationDao.findById(protectionEntry.getLocationFk());
      if (locationEntry != null) {
        protectionEntry.setLocationEntry(locationEntry);
      }
    }
    return protectionEntry;
  }

  /**
   * Loads all {@link ProtectionLockEntry} instances from the persistence layer.
   *
   * @return a list of all protection lock entries
   */
  public List<ProtectionLockEntry> loadAllLocks() {
    return protectionDao.findAllLocks();
  }
}