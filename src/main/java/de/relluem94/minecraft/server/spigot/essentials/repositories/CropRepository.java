package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.CropDao;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;

/**
 * Repository for managing seed-to-plant mappings used in crop growth logic.
 *
 * <p>Loads initial mappings from the persistence layer and allows runtime registration
 * of additional seed-to-plant relationships.
 *
 * @author rellu
 */
public class CropRepository {

  private final Map<Material, Material> seedToPlantMap = new EnumMap<>(Material.class);

  /**
   * Creates a new {@code CropRepository} and populates the seed-to-plant map
   * with all entries provided by the given {@link CropDao}.
   *
   * @param cropDao the data access object used to load initial crop entries
   */
  public CropRepository(CropDao cropDao) {
    cropDao.findAll().forEach(ce -> seedToPlantMap.put(ce.getSeed(), ce.getPlant()));
  }

  /**
   * Registers a seed-to-plant mapping at runtime.
   *
   * <p>If a mapping for the given seed already exists, it will be overwritten.
   *
   * @param seed  the seed {@link Material} to register
   * @param plant the plant {@link Material} that grows from the given seed
   */
  public void register(Material seed, Material plant) {
    seedToPlantMap.put(seed, plant);
  }

  /**
   * Checks whether the given {@link Material} is a registered seed.
   *
   * @param material the material to check
   * @return {@code true} if the material is a known seed, {@code false} otherwise
   */
  public boolean isSeed(Material material) {
    return seedToPlantMap.containsKey(material);
  }

  /**
   * Returns the plant {@link Material} associated with the given seed.
   *
   * @param seed the seed {@link Material} to look up
   * @return the corresponding plant material, or {@code null} if the seed is not registered
   */
  public Material getPlant(Material seed) {
    return seedToPlantMap.get(seed);
  }
}