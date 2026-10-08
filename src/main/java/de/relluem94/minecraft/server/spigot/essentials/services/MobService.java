package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobEquipment;
import de.relluem94.minecraft.server.spigot.essentials.registries.MobRegistry;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

@AllArgsConstructor
public class MobService {
  private final MobRegistry mobRegistry;

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
    mobRegistry.register(customMob);
    return Optional.of(customMob);
  }

  public int countSpawnedMobs() {
    return mobRegistry.findAll().size();
  }

  public int countAliveMobs() {
    return mobRegistry.findAllAlive().size();
  }

  public void despawnAll() {
    mobRegistry.findAllAlive().forEach(mob -> mob.getLivingEntity().remove());
    mobRegistry.findAll().forEach(mob -> mobRegistry.unregister(mob.getLivingEntity().getUniqueId()));
  }
}
