package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.DropDao;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;

/**
 * Repository for managing drop rules associated with specific materials. Provides access to minimum
 * and maximum drop amounts per material.
 *
 * @author rellu
 */
public class DropRuleRepository {

  private final Map<Material, DoubleStore<Integer, Integer>> dropRuleMap = new EnumMap<>(
      Material.class);

  /**
   * Creates a new {@link DropRuleRepository} and loads all drop rules from the given
   * {@link DropDao}.
   *
   * @param dropDao the data access object used to retrieve all persisted drop rules
   */
  public DropRuleRepository(DropDao dropDao) {
    dropDao.findAll().forEach(
        de -> dropRuleMap.put(de.getMaterial(), new DoubleStore<>(de.getMin(), de.getMax())));
  }

  /**
   * Returns whether a drop rule exists for the given material.
   *
   * @param material the material to check
   * @return {@code true} if a drop rule exists for the given material, {@code false} otherwise
   */
  public boolean hasDropRule(Material material) {
    return dropRuleMap.containsKey(material);
  }

  /**
   * Returns the drop rule for the given material as a {@link DoubleStore} containing the minimum
   * and maximum drop amount.
   *
   * @param material the material whose drop rule is to be retrieved
   * @return a {@link DoubleStore} with the minimum and maximum drop amount, or {@code null} if no
   *     rule exists
   */
  public DoubleStore<Integer, Integer> getDropRule(Material material) {
    return dropRuleMap.get(material);
  }
}