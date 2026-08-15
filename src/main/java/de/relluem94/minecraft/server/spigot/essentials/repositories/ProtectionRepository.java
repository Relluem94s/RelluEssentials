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

public class ProtectionRepository {

  private static final Logger logger = Logger.getLogger(ProtectionRepository.class.getName());

  private final ProtectionDao protectionDao;
  private final LocationDao locationDao;

  public ProtectionRepository(ProtectionDao protectionDao, LocationDao locationDao) {
    this.protectionDao = protectionDao;
    this.locationDao = locationDao;
  }

  public List<Long> removeOutdatedProtections() {
    List<Long> outdatedIds = protectionDao.findOutdatedProtectionIds();
    protectionDao.deleteOutdatedProtections();
    return outdatedIds;
  }

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

  public void remove(ProtectionEntry protectionEntry) {
    protectionDao.deleteById(protectionEntry.getId(), protectionEntry.getLocationEntry().getPlayerId());
    locationDao.deleteById(protectionEntry.getLocationEntry().getId(), protectionEntry.getLocationEntry().getPlayerId());
  }


  public void save(ProtectionEntry protectionEntry) {
    protectionDao.insertProtection(protectionEntry);
  }

  public void updateFlags(ProtectionEntry protectionEntry) {
    protectionDao.updateProtectionFlag(protectionEntry);
  }

  public void updateRights(ProtectionEntry protectionEntry) {
    protectionDao.updateProtectionRight(protectionEntry);
  }

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

  public List<ProtectionLockEntry> loadAllLocks() {
    return protectionDao.findAllLocks();
  }
}