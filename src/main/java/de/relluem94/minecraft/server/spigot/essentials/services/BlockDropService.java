package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.repositories.CropRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.DropRuleRepository;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.Random;
import org.bukkit.Material;

/**
 * Service responsible for handling custom block drop logic.
 */
public class BlockDropService {

  private final DropRuleRepository dropRuleRepository;
  private final CropRepository cropRepository;
  private final Random random = new Random();

  /**
   * Constructs a new BlockDropService.
   *
   * @param dropRuleRepository the repository containing custom drop rules
   * @param cropRepository     the repository containing crop and seed information
   */
  public BlockDropService(DropRuleRepository dropRuleRepository, CropRepository cropRepository) {
    this.dropRuleRepository = dropRuleRepository;
    this.cropRepository = cropRepository;
  }

  /**
   * Calculates the amount of items to drop based on configured drop rules.
   * If no rule exists or the current amount is not 1, the original amount is returned.
   *
   * @param material      the material being broken
   * @param currentAmount the current amount of the item
   * @return the new amount of items to drop
   */
  public int resolveDropAmount(Material material, int currentAmount) {
    if (currentAmount != 1 || !dropRuleRepository.hasDropRule(material)) {
      return currentAmount;
    }
    DoubleStore<Integer, Integer> range = dropRuleRepository.getDropRule(material);
    return random.nextInt(range.getSecondValue()) + range.getValue();
  }

  /**
   * Checks if there is a custom drop rule configured for the given material.
   *
   * @param material the material to check
   * @return true if a drop rule exists, false otherwise
   */
  public boolean hasDropRule(Material material) {
    return dropRuleRepository.hasDropRule(material);
  }

  /**
   * Determines if the given material is classified as a seed.
   *
   * @param material the material to check
   * @return true if the material is a seed, false otherwise
   */
  public boolean isSeed(Material material) {
    return cropRepository.isSeed(material);
  }

  /**
   * Retrieves the plant material associated with a specific seed.
   *
   * @param seed the seed material
   * @return the corresponding plant material
   */
  public Material getPlantForSeed(Material seed) {
    return cropRepository.getPlant(seed);
  }
}