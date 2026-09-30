package de.relluem94.minecraft.server.spigot.essentials.models;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a cuboid selection in a Minecraft world defined by two corner positions.
 * Precomputes the minimum and maximum block coordinates along each axis for efficient
 * boundary checks.
 *
 * @author rellu
 */
@Getter
public class Selection {

  private final Location pos1;
  private final Location pos2;
  final int minX;
  final int maxX;
  final int minY;
  final int maxY;
  final int minZ;
  final int maxZ;
  World world;
  @Setter
  private Location originalPivot;
  @Setter
  private Vector pivotPlayerOffset;

  /**
   * Creates a new cuboid selection from two corner positions.
   * Automatically derives the world and the minimum and maximum block coordinates along each axis.
   *
   * @param pos1 the first corner of the selection, must not be null
   * @param pos2 the second corner of the selection, must not be null
   */
  public Selection(@NotNull Location pos1, @NotNull Location pos2) {
    this.pos1 = pos1;
    this.pos2 = pos2;

    minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
    maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
    minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
    maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
    minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
    maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

    world = pos1.getWorld();
  }
}
