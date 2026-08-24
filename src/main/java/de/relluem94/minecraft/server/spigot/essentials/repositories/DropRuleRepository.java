package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.DropDao;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;

public class DropRuleRepository {

  private final DropDao dropDao;
  private final Map<Material, DoubleStore<Integer, Integer>> dropRuleMap = new EnumMap<>(Material.class);

  public DropRuleRepository(DropDao dropDao) {
    this.dropDao = dropDao;
    dropDao.findAll().forEach(de -> dropRuleMap.put(de.getMaterial(), new DoubleStore<>(de.getMin(), de.getMax())));
  }

  public boolean hasDropRule(Material material) {
    return dropRuleMap.containsKey(material);
  }

  public DoubleStore<Integer, Integer> getDropRule(Material material) {
    return dropRuleMap.get(material);
  }
}