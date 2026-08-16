package de.relluem94.minecraft.server.spigot.essentials.npcs;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
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

public class NpcSpawner {

  private final Server server;
  private final NamespacedKey npcIdKey;
  private final NpcMannequinAttributeApplier npcMannequinAttributeApplier;

  public NpcSpawner(Server server, ServiceContext serviceContext) {
    this.server = server;
    this.npcIdKey = new NamespacedKey(RelluEssentials.getInstance(), "npc_id");
    npcMannequinAttributeApplier = new NpcMannequinAttributeApplier(serviceContext);
  }

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

  public void despawnMannequin(UUID entityUuid) {
    Entity entity = server.getEntity(entityUuid);
    if (entity != null) {
      entity.remove();
    }
  }
}