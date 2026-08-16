package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND;

import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.LocationRepository;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LocationService {

  private final LocationRepository locationRepository;
  private final LocationTypeService locationTypeService;

  public LocationService(LocationRepository locationRepository,
      LocationTypeService locationTypeService) {
    this.locationRepository = locationRepository;
    this.locationTypeService = locationTypeService;
  }

  public int removeOutdatedLocations() {
    return locationRepository.removeOutdatedLocations();
  }

  public LocationEntry findByLocationAndType(@NotNull Location location, LocationType type) {
    LocationTypeEntry typeEntry = resolveType(type);
    return locationRepository.findByLocationAndType(location, typeEntry.getId());
  }

  public LocationEntry findById(int id) {
    return locationRepository.findById(id);
  }

  public void save(@NotNull LocationEntry locationEntry) {
    locationRepository.save(locationEntry);
  }

  public LocationEntry saveAndFetch(@NotNull LocationEntry locationEntry) {
    locationRepository.save(locationEntry);
    LocationEntry persisted = locationRepository.findByLocationAndType(
        locationEntry.getLocation(), locationEntry.getLocationType().getId());
    return persisted != null ? persisted : locationEntry;
  }

  public void delete(@NotNull LocationEntry locationEntry) {
    locationRepository.delete(locationEntry);
  }

  public void deleteById(int id, int playerId) {
    locationRepository.deleteById(id, playerId);
  }

  public List<LocationEntry> findByPlayerAndType(int playerId, LocationType type) {
    LocationTypeEntry typeEntry = resolveType(type);
    return locationRepository.findByPlayerAndType(playerId, typeEntry.getId());
  }

  public LocationTypeEntry resolveType(LocationType type) {
    return locationTypeService.findByName(type)
        .orElseThrow(() -> new IllegalStateException(PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND));
  }

  public LocationEntry buildLocationEntry(@NotNull Location location, String name, LocationType type,
      int playerId) {
    LocationTypeEntry typeEntry = resolveType(type);
    LocationEntry locationEntry = new LocationEntry();
    locationEntry.setLocation(location);
    locationEntry.setLocationName(name);
    locationEntry.setLocationType(typeEntry);
    locationEntry.setPlayerId(playerId);
    return locationEntry;
  }

  public LocationEntry buildLocationEntry(@NotNull Player player, String name, LocationType type,
      int playerId) {
    return buildLocationEntry(player.getLocation(), name, type, playerId);
  }

  public List<LocationEntry> findByType(LocationType type) {
    LocationTypeEntry typeEntry = resolveType(type);
    return locationRepository.findByType(typeEntry.getId());
  }
}