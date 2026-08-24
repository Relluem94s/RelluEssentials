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

/**
 * Service responsible for managing location entries and their association with location types.
 */
public class LocationService {

  private final LocationRepository locationRepository;
  private final LocationTypeService locationTypeService;

  /**
   * Constructs a new {@link LocationService}.
   *
   * @param locationRepository  the repository for location data.
   * @param locationTypeService the service for location type management.
   */
  public LocationService(LocationRepository locationRepository,
      LocationTypeService locationTypeService) {
    this.locationRepository = locationRepository;
    this.locationTypeService = locationTypeService;
  }

  /**
   * Removes all locations that are considered outdated from the repository.
   *
   * @return the number of removed locations.
   */
  @SuppressWarnings("unused")
  public int removeOutdatedLocations() {
    return locationRepository.removeOutdatedLocations();
  }

  /**
   * Finds a location entry based on the provided Bukkit location and location type.
   *
   * @param location the Bukkit location to search for.
   * @param type     the type of location.
   * @return the found location entry, or null if not found.
   */
  public LocationEntry findByLocationAndType(@NotNull Location location, LocationType type) {
    LocationTypeEntry typeEntry = resolveType(type);
    return locationRepository.findByLocationAndType(location, typeEntry.getId());
  }

  /**
   * Finds a location entry by its unique identifier.
   *
   * @param id the ID of the location entry.
   * @return the found location entry, or null if not found.
   */
  public LocationEntry findById(int id) {
    return locationRepository.findById(id);
  }

  /**
   * Saves a location entry to the repository.
   *
   * @param locationEntry the location entry to save.
   */
  public void save(@NotNull LocationEntry locationEntry) {
    locationRepository.save(locationEntry);
  }

  /**
   * Saves a location entry and fetches the persisted version from the repository.
   *
   * @param locationEntry the location entry to save.
   * @return the persisted location entry, or the provided entry if no persisted version is found.
   */
  public LocationEntry saveAndFetch(@NotNull LocationEntry locationEntry) {
    locationRepository.save(locationEntry);
    LocationEntry persisted = locationRepository.findByLocationAndType(
        locationEntry.getLocation(), locationEntry.getLocationType().getId());
    return persisted != null ? persisted : locationEntry;
  }

  /**
   * Deletes a specific location entry.
   *
   * @param locationEntry the location entry to delete.
   */
  public void delete(@NotNull LocationEntry locationEntry) {
    locationRepository.delete(locationEntry);
  }

  /**
   * Deletes a location entry by its ID and the associated player ID.
   *
   * @param id       the ID of the location entry.
   * @param playerId the ID of the player owning the location.
   */
  @SuppressWarnings("unused")
  public void deleteById(int id, int playerId) {
    locationRepository.deleteById(id, playerId);
  }

  /**
   * Finds all location entries for a specific player and location type.
   *
   * @param playerId the ID of the player.
   * @param type     the type of location.
   * @return a list of location entries matching the criteria.
   */
  public List<LocationEntry> findByPlayerAndType(int playerId, LocationType type) {
    LocationTypeEntry typeEntry = resolveType(type);
    return locationRepository.findByPlayerAndType(playerId, typeEntry.getId());
  }

  /**
   * Resolves a {@link LocationType} enum into its corresponding {@link LocationTypeEntry} model.
   *
   * @param type the location type enum.
   * @return the corresponding location type entry.
   * @throws IllegalStateException if the location type cannot be found.
   */
  public LocationTypeEntry resolveType(LocationType type) {
    return locationTypeService.findByName(type)
        .orElseThrow(() -> new IllegalStateException(PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND));
  }

  /**
   * Builds a new location entry with the provided details.
   *
   * @param location    the Bukkit location.
   * @param name        the name of the location.
   * @param type        the type of location.
   * @param playerId    the ID of the player.
   * @return a new location entry instance.
   */
  public LocationEntry buildLocationEntry(@NotNull Location location, String name,
      LocationType type, int playerId) {
    LocationTypeEntry typeEntry = resolveType(type);
    LocationEntry locationEntry = new LocationEntry();
    locationEntry.setLocation(location);
    locationEntry.setLocationName(name);
    locationEntry.setLocationType(typeEntry);
    locationEntry.setPlayerId(playerId);
    return locationEntry;
  }

  /**
   * Builds a new location entry using the current location of a player.
   *
   * @param player     the player whose location will be used.
   * @param name       the name of the location.
   * @param type       the type of location.
   * @param playerId   the ID of the player.
   * @return a new location entry instance.
   */
  public LocationEntry buildLocationEntry(@NotNull Player player, String name, LocationType type,
      int playerId) {
    return buildLocationEntry(player.getLocation(), name, type, playerId);
  }

  /**
   * Finds all location entries of a specific type.
   *
   * @param type the type of location.
   * @return a list of location entries of the specified type.
   */
  public List<LocationEntry> findByType(LocationType type) {
    LocationTypeEntry typeEntry = resolveType(type);
    return locationRepository.findByType(typeEntry.getId());
  }
}