package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.model.Npc;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NonNull;

public class NpcSpawner {

  private final NpcMannequinCommandBuilder commandBuilder;

  public NpcSpawner() {
    this.commandBuilder = new NpcMannequinCommandBuilder();
  }

  public Optional<UUID> spawnMannequin(@NonNull Npc npc, Set<UUID> alreadyManagedUUIDs) {
    World world = Bukkit.getWorld(npc.getWorldName());
    if (world == null) {
      return Optional.empty();
    }

    Location spawnLocation = new Location(world, npc.getX(), npc.getY(), npc.getZ());
    String summonCommand = commandBuilder.buildSummonCommand(npc.getX(), npc.getY(), npc.getZ(),
        npc.getProfileName());

    Optional<? extends Player> playerInWorld = world.getPlayers().stream().findFirst();
    if (playerInWorld.isEmpty()) {
      return Optional.empty();
    }

    Bukkit.dispatchCommand(playerInWorld.get(), summonCommand);

    return findNewlySpawnedUnmanagedMannequin(world, spawnLocation, alreadyManagedUUIDs)
        .map(this::applyMannequinAttributes);
  }

  public void spawnMannequinAsync(@NonNull Npc npc, Set<UUID> alreadyManagedUUIDs,
      java.util.function.Consumer<Optional<UUID>> callback) {
    World world = Bukkit.getWorld(npc.getWorldName());
    if (world == null) {
      callback.accept(Optional.empty());
      return;
    }

    Location spawnLocation = new Location(world, npc.getX(), npc.getY(), npc.getZ());
    String summonCommand = commandBuilder.buildSummonCommand(npc.getX(), npc.getY(), npc.getZ(),
        npc.getProfileName());

    Optional<? extends Player> playerInWorld = world.getPlayers().stream().findFirst();
    if (playerInWorld.isEmpty()) {
      callback.accept(Optional.empty());
      return;
    }

    Bukkit.dispatchCommand(playerInWorld.get(), summonCommand);

    new BukkitRunnable() {
      @Override
      public void run() {
        Optional<UUID> result = findNewlySpawnedUnmanagedMannequin(world, spawnLocation,
            alreadyManagedUUIDs)
            .map(NpcSpawner.this::applyMannequinAttributes);
        callback.accept(result);
      }
    }.runTaskLater(RelluEssentials.getInstance(), 2L);
  }


  public void despawnMannequin(UUID entityUUID) {
    Entity entity = Bukkit.getEntity(entityUUID);
    if (entity != null) {
      entity.remove();
    }
  }

  public void despawnAllMannequinsInAllWorlds(Set<String> managedProfileNames) {
    Bukkit.getWorlds().forEach(world ->
        world.getEntities().stream()
            .filter(entity -> entity.getType().name().equalsIgnoreCase("MANNEQUIN"))
            .filter(entity -> isManagedByPlugin(entity, managedProfileNames))
            .forEach(Entity::remove)
    );
  }

  private boolean isManagedByPlugin(Entity entity, Set<String> managedProfileNames) {
    return managedProfileNames.contains(entity.getName());
  }


  private UUID applyMannequinAttributes(UUID entityUUID) {
    Entity entity = Bukkit.getEntity(entityUUID);

    if (entity instanceof LivingEntity livingEntity) {
      livingEntity.setCanPickupItems(false);
      livingEntity.setCollidable(false);
    }
    return entityUUID;
  }

  private @NonNull Optional<UUID> findNewlySpawnedUnmanagedMannequin(@NonNull World world,
      Location spawnLocation, Set<UUID> alreadyManagedUUIDs) {
    return world.getEntities().stream()
        .filter(entity -> entity.getType().name().equalsIgnoreCase("MANNEQUIN"))
        .filter(entity -> !alreadyManagedUUIDs.contains(entity.getUniqueId()))
        .filter(entity -> isWithinSpawnRadius(entity.getLocation(), spawnLocation))
        .map(Entity::getUniqueId)
        .findFirst();
  }

  private boolean isWithinSpawnRadius(@NonNull Location entityLocation, Location targetLocation) {
    double spawnRadiusTolerance = 0.5;
    return entityLocation.distanceSquared(targetLocation) <= spawnRadiusTolerance;
  }
}