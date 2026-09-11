package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.Generated;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * Utility class providing helper methods for block modification operations,
 * including clipboard management, selection handling, rotation, undo functionality,
 * and protection checks.
 *
 * @author rellu
 */
public class ModifyHelper {

  private ModifyHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Normalizes a yaw angle to the nearest cardinal direction (0, 90, 180, 270 degrees).
   *
   * @param yaw the raw yaw angle to normalize
   * @return the normalized yaw angle rounded to the nearest 90-degree step
   */
  public static float normalizeYaw(float yaw) {
    yaw = ((yaw % 360) + 360) % 360;
    return Math.round(yaw / 90.0f) * 90.0f % 360.0f;
  }

  /**
   * Converts world-relative coordinates to local coordinates based on the given yaw rotation.
   *
   * @param dx the world-relative X offset
   * @param dz the world-relative Z offset
   * @param yaw the yaw angle used to determine the rotation
   * @return an array containing the local X and Z coordinates after applying the rotation
   */
  @Contract(pure = true)
  public static int @NonNull [] worldToLocal(int dx, int dz, float yaw) {
    int roundedYaw = ((Math.round(yaw) % 360) + 360) % 360;
    return switch (roundedYaw) {
      case 90  -> new int[]{ dz, -dx};
      case 180 -> new int[]{-dx, -dz};
      case 270 -> new int[]{-dz,  dx};
      default  -> new int[]{ dx,  dz};
    };
  }

  /**
   * Converts local relative coordinates back to world coordinates based on the given yaw rotation.
   *
   * @param relX the local relative X coordinate
   * @param relZ the local relative Z coordinate
   * @param yaw the yaw angle used to determine the rotation
   * @return an array containing the world X and Z coordinates after applying the rotation
   */
  @Contract(pure = true)
  public static int @NonNull [] relativeToWorld(int relX, int relZ, float yaw) {
    int roundedYaw = ((Math.round(yaw) % 360) + 360) % 360;
    return switch (roundedYaw) {
      case 90  -> new int[]{-relZ,  relX};
      case 180 -> new int[]{-relX, -relZ};
      case 270 -> new int[]{ relZ, -relX};
      default  -> new int[]{ relX,  relZ};
    };
  }

  /**
   * Rotates the given {@link BlockData} by the specified yaw angle.
   *
   * @param original the original {@link BlockData} to rotate
   * @param yaw the yaw angle determining the rotation amount
   * @return a new {@link BlockData} instance rotated according to the given yaw
   */
  public static BlockData rotateBlockData(BlockData original, float yaw) {
    int roundedYaw = ((Math.round(yaw) % 360) + 360) % 360;
    BlockData rotated = original.clone();
    switch (roundedYaw) {
      case 90  -> rotated.rotate(StructureRotation.CLOCKWISE_90);
      case 180 -> rotated.rotate(StructureRotation.CLOCKWISE_180);
      case 270 -> rotated.rotate(StructureRotation.COUNTERCLOCKWISE_90);
      default  -> {}
    }
    return rotated;
  }

  /**
   * Resolves the world {@link Block} corresponding to a clipboard entry,
   * applying the given yaw rotation relative to the player's target location.
   *
   * @param entry the {@link ModifyClipboardEntry} containing relative position and block data
   * @param yaw the yaw angle used to rotate the relative position into world space
   * @param playerTargetLoc the world location used as the paste origin
   * @return the {@link Block} at the resolved world position
   */
  public static @NonNull Block getBlock(@NonNull ModifyClipboardEntry entry, float yaw,
      @NonNull Location playerTargetLoc) {
    int relX = entry.getLocation().getBlockX();
    int relY = entry.getLocation().getBlockY();
    int relZ = entry.getLocation().getBlockZ();

    int[] world = relativeToWorld(relX, relZ, yaw);

    Location newLoc = new Location(
        playerTargetLoc.getWorld(),
        playerTargetLoc.getBlockX() + world[0],
        playerTargetLoc.getBlockY() + relY,
        playerTargetLoc.getBlockZ() + world[1]
    );
    return newLoc.getBlock();
  }

  /**
   * Computes a selection with positions relative to the given player target location,
   * preserving the yaw and pitch of the target location on both positions.
   *
   * @param selection the original absolute {@link Selection}
   * @param playerTargetLoc the location used as the reference origin for relativization
   * @return a new {@link Selection} with positions relative to the player target location
   */
  public static @NotNull Selection getRelativeCopySelection(@NotNull Selection selection,
      Location playerTargetLoc) {
    Location pos1 = selection.getPos1().clone().subtract(playerTargetLoc);
    Location pos2 = selection.getPos2().clone().subtract(playerTargetLoc);
    pos1.setYaw(playerTargetLoc.getYaw());
    pos1.setPitch(playerTargetLoc.getPitch());
    pos2.setYaw(playerTargetLoc.getYaw());
    pos2.setPitch(playerTargetLoc.getPitch());
    return new Selection(pos1, pos2);
  }

  /**
   * Determines whether the given {@link Material} represents a plant.
   *
   * <p>Recognized plant materials include flowers, saplings, crops, bamboo, sugar cane,
   * cactus, sweet berry bushes, kelp, sea pickles, and lily pads.</p>
   *
   * @param material the {@link Material} to check
   * @return {@code true} if the material is a plant, {@code false} otherwise
   *
   */
  @Generated // This cant be Tested due to Static Bukkit Calls
  public static boolean isPlantMaterial(Material material) {
    return Tag.FLOWERS.isTagged(material)
        || Tag.SAPLINGS.isTagged(material)
        || Tag.CROPS.isTagged(material)
        || material == Material.BAMBOO
        || material == Material.BAMBOO_SAPLING
        || material == Material.SUGAR_CANE
        || material == Material.CACTUS
        || material == Material.SWEET_BERRY_BUSH
        || material == Material.KELP
        || material == Material.SEA_PICKLE
        || material == Material.LILY_PAD;
  }

  /**
   * Creates a {@link ModifyClipboardEntry} for the given block by computing its position
   * relative to the player's target location and transforming it into local coordinates
   * based on the player's current yaw.
   *
   * @param block the {@link Block} to create a clipboard entry for
   * @param p the {@link Player} whose yaw is used for coordinate transformation
   * @param playerTargetLoc the location used as the copy origin reference point
   * @return a {@link ModifyClipboardEntry} containing the local position and block data
   */
  public static @NonNull ModifyClipboardEntry getModifyClipboardEntry(@NonNull Block block,
      @NonNull Player p, @NonNull Location playerTargetLoc) {
    float yaw = normalizeYaw(p.getLocation().getYaw());

    Location relLoc = new Location(
        block.getWorld(),
        block.getX() - playerTargetLoc.getBlockX(),
        block.getY() - playerTargetLoc.getBlockY(),
        block.getZ() - playerTargetLoc.getBlockZ()
    );

    int[] local = worldToLocal(relLoc.getBlockX(), relLoc.getBlockZ(), yaw);
    Location localLoc = new Location(block.getWorld(), local[0], relLoc.getBlockY(), local[1]);
    return new ModifyClipboardEntry(localLoc, block.getType(), block.getBlockData());
  }

  /**
   * Rotates the given list of {@link ModifyClipboardEntry} elements 90 degrees clockwise
   * and computes a new {@link Selection} that fits the rotated entries.
   *
   * <p>The rotated entries are normalized so that their minimum X and Z coordinates
   * start at zero.</p>
   *
   * @param entries the list of {@link ModifyClipboardEntry} elements to rotate
   * @param selection the original {@link Selection} used to preserve Y bounds and world reference
   * @return a {@link DoubleStore} containing the new rotated {@link Selection}
   *         and the list of rotated and normalized {@link ModifyClipboardEntry} elements
   */
  public static @NonNull DoubleStore<Selection, List<ModifyClipboardEntry>> rotate(
      @NotNull List<ModifyClipboardEntry> entries, @NotNull Selection selection) {
    List<ModifyClipboardEntry> rotatedEntries = entries.stream()
        .map(entry -> {
          Location oldLocation = entry.getLocation();
          int newX = oldLocation.getBlockZ();
          int newZ = -oldLocation.getBlockX();
          int newY = oldLocation.getBlockY();

          Location newLocation = new Location(oldLocation.getWorld(), newX, newY, newZ);
          return new ModifyClipboardEntry(newLocation, entry.getMaterial(), entry.getData());
        })
        .toList();

    int minX = rotatedEntries.stream().mapToInt(e -> e.getLocation().getBlockX()).min().orElse(0);
    int minZ = rotatedEntries.stream().mapToInt(e -> e.getLocation().getBlockZ()).min().orElse(0);

    List<ModifyClipboardEntry> normalizedEntries = rotatedEntries.stream()
        .map(entry -> {
          Location loc = entry.getLocation();
          Location normalizedLoc = new Location(
              loc.getWorld(),
              loc.getBlockX() - minX,
              loc.getBlockY(),
              loc.getBlockZ() - minZ + 1
          );
          return new ModifyClipboardEntry(normalizedLoc, entry.getMaterial(), entry.getData());
        })
        .collect(Collectors.toList());

    int maxX = normalizedEntries.stream().mapToInt(e -> e.getLocation().getBlockX()).max()
        .orElse(0);
    int maxZ = normalizedEntries.stream().mapToInt(e -> e.getLocation().getBlockZ()).max()
        .orElse(0);
    int minY = selection.getMinY();
    int maxY = selection.getMaxY();

    Location newPos1 = new Location(selection.getWorld(), 0, minY, 0,
        selection.getPos1().getYaw(), selection.getPos1().getPitch());
    Location newPos2 = new Location(selection.getWorld(), maxX, maxY, maxZ,
        selection.getPos2().getYaw(), selection.getPos2().getPitch());

    Selection rotatedSelection = new Selection(newPos1, newPos2);
    return new DoubleStore<>(rotatedSelection, normalizedEntries);
  }

  /**
   * Reverts a block to its previous state as recorded in the given {@link ModifyHistoryEntry}.
   *
   * @param entry the {@link ModifyHistoryEntry} containing the location, material,
   *              and block data to restore
   */
  public static void undo(@NotNull ModifyHistoryEntry entry) {
    entry.getLocation().getBlock().setType(entry.getMaterial());
    entry.getLocation().getBlock().setBlockData(entry.getData());
  }

  /**
   * Checks whether the given {@link Block} has an associated protection entry and removes it
   * from both the registry and the protection store if present.
   *
   * @param block the {@link Block} whose protection should be checked and potentially removed
   */
  public static void checkAndRemoveProtection(Block block) {
    if (RelluEssentials.getInstance().getServiceContext().getProtectionService()
        .isProtectableMaterial(block.getType())) {
      ProtectionEntry protection = RelluEssentials.getInstance().getServiceContext()
          .getProtectionService()
          .getProtectionEntry(block.getLocation());

      if (protection != null) {
        RelluEssentials.getInstance().getServiceContext().getProtectionService()
            .deleteProtectionAndRemoveFromRegistry(protection);
        RelluEssentials.getInstance().getServiceContext().getProtectionService()
            .removeProtectionEntry(block.getLocation());
      }
    }
  }

  /**
   * Iterates over every {@link Block} within the given {@link Selection} and applies
   * the provided action to each block.
   *
   * @param selection the {@link Selection} defining the bounds of blocks to iterate over
   * @param action the {@link Consumer} action to apply to each {@link Block}
   */
  public static void forEachBlock(@NotNull Selection selection, Consumer<Block> action) {
    for (int y = selection.getMinY(); y <= selection.getMaxY(); y++) {
      for (int x = selection.getMinX(); x <= selection.getMaxX(); x++) {
        for (int z = selection.getMinZ(); z <= selection.getMaxZ(); z++) {
          Block block = new Location(selection.getWorld(), x, y, z).getBlock();
          action.accept(block);
        }
      }
    }
  }
}
