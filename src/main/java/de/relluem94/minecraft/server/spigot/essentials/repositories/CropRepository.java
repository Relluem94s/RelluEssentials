package de.relluem94.minecraft.server.spigot.essentials.repositories;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;

public class CropRepository {

  private final Map<Material, Material> seedToPlantMap = new EnumMap<>(Material.class);

  public void register(Material seed, Material plant) {
    seedToPlantMap.put(seed, plant);
  }

  public boolean isSeed(Material material) {
    return seedToPlantMap.containsKey(material);
  }

  public Material getPlant(Material seed) {
    return seedToPlantMap.get(seed);
  }
}