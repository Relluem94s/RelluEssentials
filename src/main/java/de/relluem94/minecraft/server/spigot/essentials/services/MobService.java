package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobEquipment;
import de.relluem94.minecraft.server.spigot.essentials.registries.MobRegistry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;

/**
 * Service for managing custom mobs, including registration, spawning, and despawning.
 *
 * @author rellu
 */
@AllArgsConstructor
public class MobService {
  private final MobRegistry mobRegistry;
  private static final String NBT_MOB_DEFINITION_KEY = "mob_definition_key";

  /**
   * Registers a {@link CustomMobDefinition} in the mob registry.
   *
   * @param definition the custom mob definition to register
   */
  public void registerDefinition(CustomMobDefinition definition) {
    mobRegistry.registerDefinition(definition);
  }

  /**
   * Finds a registered {@link CustomMobDefinition} by its {@link RelluEssentialsNamespacedKey}.
   *
   * @param key the namespaced key identifying the definition
   * @return an {@link Optional} containing the definition if found, otherwise empty
   */
  public Optional<CustomMobDefinition> findDefinition(RelluEssentialsNamespacedKey key) {
    return mobRegistry.findDefinitionByKey(key);
  }

  /**
   * Spawns a custom mob at the given location using the definition identified by the provided key.
   *
   * @param definitionKey the namespaced key identifying the {@link CustomMobDefinition} to use
   * @param location the location at which the mob should be spawned
   * @return an {@link Optional} containing the spawned {@link CustomMob} if successful, otherwise empty
   */
  public Optional<CustomMob> spawnMob(RelluEssentialsNamespacedKey definitionKey, Location location) {
    return mobRegistry.findDefinitionByKey(definitionKey)
        .flatMap(definition -> spawnMobFromDefinition(definition, location));
  }

  /**
   * Spawns a custom mob at the given location using the provided {@link CustomMobDefinition}.
   *
   * @param definition the custom mob definition to use for spawning
   * @param location the location at which the mob should be spawned
   * @return an {@link Optional} containing the spawned {@link CustomMob} if successful, otherwise empty
   */
  public Optional<CustomMob> spawnMob(CustomMobDefinition definition, Location location) {
    return spawnMobFromDefinition(definition, location);
  }

  private Optional<CustomMob> spawnMobFromDefinition(CustomMobDefinition definition, Location location) {
    World world = location.getWorld();

    if (world == null) {
      return Optional.empty();
    }

    LivingEntity livingEntity = (LivingEntity) world.spawnEntity(location, definition.entityType());
    livingEntity.setCustomName(definition.customName());
    livingEntity.setCustomNameVisible(definition.isCustomNameVisible());

    double health = definition.health();
    if (health == 0) {
      health = livingEntity.getHealth();
    }

    livingEntity.setHealth(health);
    livingEntity.addPotionEffects(definition.potionEffects());
    livingEntity.setInvisible(definition.invisible());
    livingEntity.setCanPickupItems(definition.canPickUpItems());

    applyEquipment(livingEntity, definition.equipment());
    applyDefinitionKeyNbtTag(livingEntity, definition.key());

    CustomMob customMob = new CustomMob(livingEntity, definition.entityType(), definition.key());
    mobRegistry.registerSpawnedMob(customMob);
    return Optional.of(customMob);
  }

  private void applyEquipment(LivingEntity livingEntity, CustomMobEquipment equipment) {
    if (livingEntity.getEquipment() == null) {
      return;
    }
    livingEntity.getEquipment().setItemInMainHand(equipment.mainHand());
    livingEntity.getEquipment().setItemInOffHand(equipment.offHand());
    livingEntity.getEquipment().setHelmet(equipment.helmet());
    livingEntity.getEquipment().setChestplate(equipment.chestplate());
    livingEntity.getEquipment().setLeggings(equipment.leggings());
    livingEntity.getEquipment().setBoots(equipment.boots());
  }

  private void applyDefinitionKeyNbtTag(LivingEntity livingEntity, RelluEssentialsNamespacedKey definitionKey) {
    NamespacedKey nbtKey = NamespacedKey.fromString(definitionKey.getNamespace() + ":" + NBT_MOB_DEFINITION_KEY);
    if (nbtKey == null) {
      return;
    }
    livingEntity.getPersistentDataContainer().set(nbtKey, PersistentDataType.STRING, definitionKey.toString());
  }

  /**
   * Returns the total number of mobs that have been spawned, including dead ones.
   *
   * @return the total count of spawned mobs
   */
  public int countSpawnedMobs() {
    return mobRegistry.findAllSpawned().size();
  }

  /**
   * Returns the number of currently alive spawned mobs.
   *
   * @return the count of alive spawned mobs
   */
  public int countAliveMobs() {
    return mobRegistry.findAllSpawnedAlive().size();
  }

  /**
   * Removes all alive spawned mobs from the world and unregisters all spawned mobs from the registry.
   */
  public void despawnAll() {
    mobRegistry.findAllSpawnedAlive().forEach(mob -> mob.getLivingEntity().remove());
    mobRegistry.findAllSpawned().forEach(mob -> mobRegistry.unregisterSpawnedMob(mob.getLivingEntity().getUniqueId()));
  }

  /**
   * Unregisters a {@link CustomMobDefinition} from the mob registry by its key.
   *
   * @param key the namespaced key identifying the definition to remove
   */
  public void unregisterDefinition(RelluEssentialsNamespacedKey key) {
    mobRegistry.unregisterDefinition(key);
  }

  /**
   * Returns all registered {@link CustomMobDefinition} instances.
   *
   * @return an unmodifiable list of all registered definitions
   */
  public List<CustomMobDefinition> findAllDefinitions() {
    return mobRegistry.findAllDefinitions();
  }

  /**
   * Unregisters a spawned {@link CustomMob} from the registry by its entity UUID.
   *
   * @param entityUuid the UUID of the entity to unregister
   */
  public void unregisterSpawnedMob(UUID entityUuid) {
    mobRegistry.unregisterSpawnedMob(entityUuid);
  }

  /**
   * Returns all spawned {@link CustomMob} instances, including dead ones.
   *
   * @return an unmodifiable list of all spawned mobs
   */
  public List<CustomMob> findAllSpawned() {
    return mobRegistry.findAllSpawned();
  }

  /**
   * Returns all spawned {@link CustomMob} instances that are currently alive.
   *
   * @return a list of alive spawned mobs
   */
  public List<CustomMob> findAllSpawnedAlive() {
    return mobRegistry.findAllSpawnedAlive();
  }

  /**
   * Returns all spawned {@link CustomMob} instances that originate from the definition identified by the given key.
   *
   * @param key the namespaced key of the definition to filter by
   * @return a list of spawned mobs matching the given definition key
   */
  public List<CustomMob> findAllSpawnedByDefinitionKey(RelluEssentialsNamespacedKey key) {
    return mobRegistry.findAllSpawnedByDefinitionKey(key);
  }
}
