package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registry responsible for managing and providing access to various location types.
 */
public class LocationTypeRegistry {

  private final List<LocationTypeEntry> locationTypes = new ArrayList<>();

  /**
   * Initializes the registry by adding the provided list of location type entries.
   *
   * @param types the list of location type entries to be registered
   */
  public void initialize(List<LocationTypeEntry> types) {
    locationTypes.addAll(types);
  }

  /**
   * Retrieves an unmodifiable list containing all registered location types.
   *
   * @return an unmodifiable list of all location type entries
   */
  public List<LocationTypeEntry> getAll() {
    return Collections.unmodifiableList(locationTypes);
  }
}