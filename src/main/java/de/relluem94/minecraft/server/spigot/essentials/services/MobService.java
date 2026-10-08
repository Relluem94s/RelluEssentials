package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobEquipment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class MobService {
  private final List<CustomMob> spawnedMobs = new ArrayList<>();

  public Optional<CustomMob> spawnMob(CustomMobDefinition definition, Location location) {
    World world = location.getWorld();

    if (world == null) {
      return Optional.empty();
    }

    LivingEntity livingEntity = (LivingEntity) world.spawnEntity(location, definition.getEntityType());
    livingEntity.setCustomName(definition.getCustomName());
    livingEntity.setCustomNameVisible(definition.isCustomNameVisible());

    double health = definition.getHealth();
    if (health == 0) {
      health = livingEntity.getHealth();
    }

    livingEntity.setHealth(health);
    livingEntity.addPotionEffects(definition.getPotionEffects());
    livingEntity.setInvisible(definition.isInvisible());
    livingEntity.setCanPickupItems(definition.isCanPickUpItems());

    if (livingEntity.getEquipment() != null) {
      CustomMobEquipment equipment = definition.getEquipment();
      livingEntity.getEquipment().setItemInMainHand(equipment.getMainHand());
      livingEntity.getEquipment().setItemInOffHand(equipment.getOffHand());
      livingEntity.getEquipment().setHelmet(equipment.getHelmet());
      livingEntity.getEquipment().setChestplate(equipment.getChestplate());
      livingEntity.getEquipment().setLeggings(equipment.getLeggings());
      livingEntity.getEquipment().setBoots(equipment.getBoots());
    }

    CustomMob customMob = new CustomMob(livingEntity, definition.getEntityType());
    spawnedMobs.add(customMob);
    return Optional.of(customMob);
  }

  public int countSpawnedMobs() {
    return spawnedMobs.size();
  }

  public int countAliveMobs() {
    return (int) spawnedMobs.stream().filter(CustomMob::isAlive).count();
  }

  public void despawnAll() {
    spawnedMobs.stream().filter(CustomMob::isAlive)
        .forEach(mob -> mob.getLivingEntity().remove());
    spawnedMobs.clear();
  }
}
