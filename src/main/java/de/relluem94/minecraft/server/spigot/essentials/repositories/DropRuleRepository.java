package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.rellulib.stores.DoubleStore;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;

public class DropRuleRepository {

  private final Map<Material, DoubleStore<Integer, Integer>> dropRules = new EnumMap<>(Material.class);

  public void register(Material material, DoubleStore<Integer, Integer> range) {
    dropRules.put(material, range);
  }

  public boolean hasDropRule(Material material) {
    return dropRules.containsKey(material);
  }

  public DoubleStore<Integer, Integer> getDropRule(Material material) {
    return dropRules.get(material);
  }
}