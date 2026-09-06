package de.relluem94.minecraft.server.spigot.essentials.services.tasks;

import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import java.util.HashMap;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Service for scheduling and applying block changes at specific world locations. Delegates all task
 * scheduling to the provided {@link SchedulerService}.
 */
public class BlockService {

  private final SchedulerService schedulerService;
  private final Server server;
  private final HashMap<Location, Long> locations = new HashMap<>();

  @Setter
  private Material targetMaterial;

  /**
   * Creates a new {@code BlockService} with the given scheduler and initial block material.
   *
   * @param schedulerService the scheduler service used to delay block placements
   * @param targetMaterial   the material to place at the registered locations
   * @param server           the server to create Blockdata
   */
  public BlockService(SchedulerService schedulerService, Material targetMaterial, Server server) {
    this.schedulerService = schedulerService;
    this.server = server;
    this.targetMaterial = targetMaterial;
  }

  /**
   * Returns whether the block at the given location matches the specified material.
   *
   * @param location the location to check
   * @param material the material to compare against
   * @return {@code true} if the block at the location is of the given material, {@code false}
   *     otherwise
   */
  public static boolean isBlockOfMaterial(@NotNull Location location, Material material) {
    return location.getBlock().getType() == material;
  }

  /**
   * Registers a location with an associated delay for deferred block placement.
   *
   * @param location the world location to register
   * @param delay    the delay in ticks before the block is placed at this location
   */
  public void addLocation(Location location, Long delay) {
    locations.put(location, delay);
  }

  /**
   * Copies all location-delay entries from another {@code BlockService} into this one.
   *
   * @param other the source {@code BlockService} whose locations are merged into this instance
   */
  public void mergeLocations(@NotNull BlockService other) {
    locations.putAll(other.locations);
  }

  /**
   * Schedules block placements for all registered locations, adding the given base delay to each
   * location's individual delay value.
   *
   * @param additionalDelay the extra delay in ticks added to each location's delay
   */
  public void applyBlocks(long additionalDelay) {
    locations.forEach((location, delay) -> schedulerService.scheduleSyncDelayedTask(
        () -> location.getBlock().setType(targetMaterial), Math.abs(delay + additionalDelay)));
  }

  /**
   * Schedules block placements for all registered locations using only their individual delays.
   */
  public void applyBlocks() {
    applyBlocks(0);
  }

  /**
   * Schedules Material replacements for all registered locations using only their individual
   * delays.
   *
   * @param additionalDelay the extra delay in ticks added to each location's delay
   */
  public void applyMaterial(long additionalDelay) {
    locations.forEach((location, delay) -> schedulerService.scheduleSyncDelayedTask(() -> {
      try {
        Block block = location.getBlock();
        String existingDataString = block.getBlockData().getAsString();
        String newDataString = existingDataString.replace(
            block.getType().getKeyOrThrow().toString(), targetMaterial.getKeyOrThrow().toString());
        block.setBlockData(server.createBlockData(newDataString));
      } catch (IllegalArgumentException e) {
        location.getBlock().setType(targetMaterial);
      }
    }, Math.abs(delay + additionalDelay)));
  }
}