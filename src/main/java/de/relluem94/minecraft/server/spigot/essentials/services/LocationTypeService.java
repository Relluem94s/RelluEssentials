package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.LocationTypeRegistry;
import java.util.Optional;

/**
 * Service responsible for retrieving {@link LocationTypeEntry} instances from the {@link LocationTypeRegistry}.
 */
public class LocationTypeService {

  private final LocationTypeRegistry locationTypeRegistry;

  /**
   * Constructs a new {@code LocationTypeService}.
   *
   * @param locationTypeRegistry the registry used to look up location types
   */
  public LocationTypeService(LocationTypeRegistry locationTypeRegistry) {
    this.locationTypeRegistry = locationTypeRegistry;
  }

  /**
   * Finds a location type entry by its unique identifier.
   *
   * @param id the unique identifier of the location type
   * @return an {@link Optional} containing the found entry, or empty if no entry matches the id
   */
  public Optional<LocationTypeEntry> findById(int id) {
    return locationTypeRegistry.getAll().stream()
        .filter(lte -> lte.getId() == id)
        .findFirst();
  }

  /**
   * Finds a location type entry by its {@link LocationType} enum value.
   *
   * @param type the location type to search for
   * @return an {@link Optional} containing the found entry, or empty if no entry matches the type name
   */
  public Optional<LocationTypeEntry> findByName(LocationType type) {
    return locationTypeRegistry.getAll().stream()
        .filter(lte -> lte.getType().equalsIgnoreCase(type.name()))
        .findFirst();
  }
}