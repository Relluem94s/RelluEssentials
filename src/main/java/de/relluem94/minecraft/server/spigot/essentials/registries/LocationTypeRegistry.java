package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationTypeRegistry {

  private final List<LocationTypeEntry> locationTypes = new ArrayList<>();

  public void initialize(List<LocationTypeEntry> types) {
    locationTypes.addAll(types);
  }

  public List<LocationTypeEntry> getAll() {
    return Collections.unmodifiableList(locationTypes);
  }
}