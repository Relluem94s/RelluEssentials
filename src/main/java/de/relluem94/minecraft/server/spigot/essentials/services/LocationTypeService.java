package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.LocationTypeRegistry;
import java.util.Optional;

public class LocationTypeService {

  private final LocationTypeRegistry locationTypeRegistry;

  public LocationTypeService(LocationTypeRegistry locationTypeRegistry) {
    this.locationTypeRegistry = locationTypeRegistry;
  }

  public Optional<LocationTypeEntry> findById(int id) {
    return locationTypeRegistry.getAll().stream()
        .filter(lte -> lte.getId() == id)
        .findFirst();
  }

  public Optional<LocationTypeEntry> findByName(LocationType type) {
    return locationTypeRegistry.getAll().stream()
        .filter(lte -> lte.getType().equalsIgnoreCase(type.name()))
        .findFirst();
  }
}