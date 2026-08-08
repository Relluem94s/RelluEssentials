package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.repositories.CropRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.DropRuleRepository;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.Random;
import org.bukkit.Material;

public class BlockDropService {

  private final DropRuleRepository dropRuleRepository;
  private final CropRepository cropRepository;
  private final Random random = new Random();

  public BlockDropService(DropRuleRepository dropRuleRepository, CropRepository cropRepository) {
    this.dropRuleRepository = dropRuleRepository;
    this.cropRepository = cropRepository;
  }

  public int resolveDropAmount(Material material, int currentAmount) {
    if (currentAmount != 1 || !dropRuleRepository.hasDropRule(material)) {
      return currentAmount;
    }
    DoubleStore<Integer, Integer> range = dropRuleRepository.getDropRule(material);
    return random.nextInt(range.getSecondValue()) + range.getValue();
  }

  public boolean hasDropRule(Material material) {
    return dropRuleRepository.hasDropRule(material);
  }

  public boolean isSeed(Material material) {
    return cropRepository.isSeed(material);
  }

  public Material getPlantForSeed(Material seed) {
    return cropRepository.getPlant(seed);
  }
}