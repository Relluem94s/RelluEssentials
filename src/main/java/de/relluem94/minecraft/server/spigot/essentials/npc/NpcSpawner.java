package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.model.Npc;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mannequin;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.jspecify.annotations.NonNull;

public class NpcSpawner {

  private final NamespacedKey npcIdKey;

  public NpcSpawner() {
    this.npcIdKey = new NamespacedKey(RelluEssentials.getInstance(), "npc_id");
  }

  public Optional<UUID> spawnMannequin(@NonNull Npc npc) {
    World world = Bukkit.getWorld(npc.getWorldName());
    if (world == null) {
      return Optional.empty();
    }

    Location spawnLocation = new Location(world, npc.getX(), npc.getY(), npc.getZ());

    Optional<UUID> existingMannequin = findExistingMannequinByNpcId(world, npc.getId().toString());
    if (existingMannequin.isPresent()) {
      return existingMannequin.map(this::applyMannequinAttributes);
    }

    return spawnAndTagMannequin(world, spawnLocation, npc);
  }

  private Optional<UUID> spawnAndTagMannequin(@NonNull World world, @NonNull Location spawnLocation, @NonNull Npc npc) {
    Entity spawnedEntity = world.spawnEntity(spawnLocation, EntityType.MANNEQUIN);

    if (spawnedEntity instanceof Mannequin mannequin) {
      PlayerProfile profile = Bukkit.createPlayerProfile(npc.getProfileName());
      mannequin.setPlayerProfile(profile);
      mannequin.getPersistentDataContainer().set(npcIdKey, PersistentDataType.STRING, npc.getId().toString());
      NpcMannequinAttributeApplier.applyAttributes(mannequin);
      return Optional.of(mannequin.getUniqueId());
    }

    return Optional.empty();
  }

  private UUID applyMannequinAttributes(UUID entityUUID) {
    Entity entity = Bukkit.getEntity(entityUUID);
    if (entity instanceof Mannequin mannequin) {
      NpcMannequinAttributeApplier.applyAttributes(mannequin);
    }
    return entityUUID;
  }

  private Optional<UUID> findExistingMannequinByNpcId(@NonNull World world, @NonNull String npcId) {
    return world.getEntities().stream()
        .filter(entity -> entity.getType().name().equalsIgnoreCase("MANNEQUIN"))
        .filter(entity -> {
          String storedId = entity.getPersistentDataContainer().get(npcIdKey, PersistentDataType.STRING);
          return npcId.equals(storedId);
        })
        .map(Entity::getUniqueId)
        .findFirst();
  }

  public void despawnMannequin(UUID entityUUID) {
    Entity entity = Bukkit.getEntity(entityUUID);
    if (entity != null) {
      entity.remove();
    }
  }
}