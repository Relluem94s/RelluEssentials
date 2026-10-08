package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Internal registry for managing {@link CustomMob} instances.
 *
 * @author rellu
 */
public class MobRegistry {

  private final Map<RelluEssentialsNamespacedKey, CustomMobDefinition> registeredDefinitions = new HashMap<>();
  private final List<CustomMob> spawnedMobs = new ArrayList<>();

  /**
   * Registers a mob definition so it can be looked up and spawned by its key.
   *
   * @param definition the definition to register
   */
  public void registerDefinition(CustomMobDefinition definition) {
    registeredDefinitions.put(definition.key(), definition);
  }

  /**
   * Removes a mob definition from the registry by its key.
   *
   * @param key the key of the definition to remove
   */
  public void unregisterDefinition(RelluEssentialsNamespacedKey key) {
    registeredDefinitions.remove(key);
  }

  /**
   * Finds a mob definition by its namespaced key.
   *
   * @param key the key to look up
   * @return an Optional containing the definition if found
   */
  public Optional<CustomMobDefinition> findDefinitionByKey(RelluEssentialsNamespacedKey key) {
    return Optional.ofNullable(registeredDefinitions.get(key));
  }

  /**
   * Returns all registered mob definitions.
   *
   * @return an unmodifiable list of all definitions
   */
  public List<CustomMobDefinition> findAllDefinitions() {
    return List.copyOf(registeredDefinitions.values());
  }

  /**
   * Registers a spawned mob instance.
   *
   * @param mob the spawned mob to register
   */
  public void registerSpawnedMob(CustomMob mob) {
    spawnedMobs.add(mob);
  }

  /**
   * Removes a spawned mob instance from the registry by its entity UUID.
   *
   * @param entityUuid the UUID of the entity to remove
   */
  public void unregisterSpawnedMob(UUID entityUuid) {
    spawnedMobs.removeIf(mob -> mob.getLivingEntity().getUniqueId().equals(entityUuid));
  }

  /**
   * Returns all spawned mob instances.
   *
   * @return an unmodifiable list of all spawned mobs
   */
  public List<CustomMob> findAllSpawned() {
    return List.copyOf(spawnedMobs);
  }

  /**
   * Returns all spawned mob instances that are currently alive.
   *
   * @return a list of alive spawned mobs
   */
  public List<CustomMob> findAllSpawnedAlive() {
    return spawnedMobs.stream().filter(CustomMob::isAlive).toList();
  }

  /**
   * Returns all spawned mob instances that originate from a specific definition key.
   *
   * @param key the definition key to filter by
   * @return a list of spawned mobs matching the given key
   */
  public List<CustomMob> findAllSpawnedByDefinitionKey(RelluEssentialsNamespacedKey key) {
    return spawnedMobs.stream().filter(mob -> mob.getDefinitionKey().equals(key)).toList();
  }
}