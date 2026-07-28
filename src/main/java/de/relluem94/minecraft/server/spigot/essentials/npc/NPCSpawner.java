package de.relluem94.minecraft.server.spigot.essentials.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

public class NPCSpawner {

    private final NPCMannequinCommandBuilder commandBuilder;

    public NPCSpawner() {
        this.commandBuilder = new NPCMannequinCommandBuilder();
    }

    public Optional<UUID> spawnMannequin(@NonNull NPC npc) {
        World world = Bukkit.getWorld(npc.getWorldName());
        if (world == null) {
            return Optional.empty();
        }

        Location spawnLocation = new Location(world, npc.getX(), npc.getY(), npc.getZ());
        String summonCommand = commandBuilder.buildSummonCommand(npc.getX(), npc.getY(), npc.getZ(), npc.getProfileName());

        Optional<? extends Player> playerInWorld = world.getPlayers().stream().findFirst();
        if (playerInWorld.isEmpty()) {
            return Optional.empty();
        }

        Bukkit.dispatchCommand(playerInWorld.get(), summonCommand);

        return findNewlySpawnedMannequin(world, spawnLocation).map(this::applyMannequinAttributes);
    }

    public void despawnMannequin(UUID entityUUID) {
        Entity entity = Bukkit.getEntity(entityUUID);
        if (entity != null) {
            entity.remove();
        }
    }

    private UUID applyMannequinAttributes(UUID entityUUID) {
        Entity entity = Bukkit.getEntity(entityUUID);

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setCanPickupItems(false);
            livingEntity.setCollidable(false);
        }
        return entityUUID;
    }

    private @NonNull Optional<UUID> findNewlySpawnedMannequin(@NonNull World world, Location spawnLocation) {
        return world.getEntities().stream()
                .filter(entity -> entity.getType().name().equalsIgnoreCase("MANNEQUIN"))
                .filter(entity -> isWithinSpawnRadius(entity.getLocation(), spawnLocation))
                .map(Entity::getUniqueId)
                .findFirst();
    }

    private boolean isWithinSpawnRadius(@NonNull Location entityLocation, Location targetLocation) {
        double spawnRadiusTolerance = 0.5;
        return entityLocation.distanceSquared(targetLocation) <= spawnRadiusTolerance;
    }
}