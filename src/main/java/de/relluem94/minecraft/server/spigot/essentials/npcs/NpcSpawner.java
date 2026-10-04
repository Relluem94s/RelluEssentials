package de.relluem94.minecraft.server.spigot.essentials.npcs;

import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mannequin;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.jspecify.annotations.NonNull;

/**
 * Handles the spawning and despawning of NPC mannequins in the world.
 * Ensures that each NPC is uniquely identified via persistent data and applies
 * the appropriate visual attributes upon spawning or reloading.
 *
 * @author rellu
 */
public class NpcSpawner {

  private final Server server;
  private final NamespacedKey npcIdKey;
  private final NpcMannequinAttributeApplier npcMannequinAttributeApplier;

  /**
   * Creates a new NpcSpawner with the required dependencies.
   *
   * @param server the Bukkit server instance used to access worlds and entities
   * @param npcIdKey the namespaced key used to store and retrieve the NPC ID in persistent data
   * @param npcMannequinAttributeApplier the applier responsible for configuring mannequin attributes
   */
  public NpcSpawner(Server server, NamespacedKey npcIdKey,
      NpcMannequinAttributeApplier npcMannequinAttributeApplier) {
    this.server = server;
    this.npcIdKey = npcIdKey;
    this.npcMannequinAttributeApplier = npcMannequinAttributeApplier;
  }

  /**
   * Spawns a mannequin entity for the given NPC in the appropriate world.
   * If a mannequin tagged with the NPC's ID already exists in the world,
   * its attributes are reapplied and its UUID is returned instead of spawning a new one.
   *
   * @param npc the NPC data used to determine spawn location, profile, and identity
   * @return an {@link Optional} containing the UUID of the spawned or existing mannequin,
   *         or an empty {@link Optional} if the world does not exist or spawning failed
   */
  public Optional<UUID> spawnMannequin(@NonNull Npc npc) {
    World world = server.getWorld(npc.getWorldName());
    if (world == null) {
      return Optional.empty();
    }

    Location spawnLocation = new Location(world, npc.getX() + 0.5, npc.getY(), npc.getZ() + 0.5,
        npc.getYaw(), npc.getPitch());

    Optional<UUID> existingMannequin = findExistingMannequinByNpcId(world, npc.getId().toString());
    if (existingMannequin.isPresent()) {
      return existingMannequin.map(this::applyMannequinAttributes);
    }

    return spawnAndTagMannequin(world, spawnLocation, npc);
  }

  private Optional<UUID> spawnAndTagMannequin(@NonNull World world, @NonNull Location spawnLocation,
      @NonNull Npc npc) {
    Entity spawnedEntity = world.spawnEntity(spawnLocation, EntityType.MANNEQUIN);

    if (spawnedEntity instanceof Mannequin mannequin) {
      PlayerProfile profile = server.createPlayerProfile(npc.getProfileName());
      mannequin.setPlayerProfile(profile);
      mannequin.getPersistentDataContainer()
          .set(npcIdKey, PersistentDataType.STRING, npc.getId().toString());
      npcMannequinAttributeApplier.applyAttributes(mannequin);
      return Optional.of(mannequin.getUniqueId());
    }

    return Optional.empty();
  }

  private UUID applyMannequinAttributes(UUID entityUuid) {
    Entity entity = server.getEntity(entityUuid);
    if (entity instanceof Mannequin mannequin) {
      npcMannequinAttributeApplier.applyAttributes(mannequin);
    }
    return entityUuid;
  }

  private Optional<UUID> findExistingMannequinByNpcId(@NonNull World world, @NonNull String npcId) {
    return world.getEntities().stream()
        .filter(entity -> entity.getType().name().equalsIgnoreCase("MANNEQUIN"))
        .filter(entity -> {
          String storedId = entity.getPersistentDataContainer()
              .get(npcIdKey, PersistentDataType.STRING);
          return npcId.equals(storedId);
        })
        .map(Entity::getUniqueId)
        .findFirst();
  }

  /**
   * Removes the mannequin entity with the given UUID from the world.
   * Does nothing if no entity with the given UUID exists.
   *
   * @param entityUuid the UUID of the mannequin entity to remove
   */
  public void despawnMannequin(UUID entityUuid) {
    Entity entity = server.getEntity(entityUuid);
    if (entity != null) {
      entity.remove();
    }
  }
}