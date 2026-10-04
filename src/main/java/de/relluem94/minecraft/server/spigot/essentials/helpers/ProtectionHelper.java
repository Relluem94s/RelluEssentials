package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_RIGHTS;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class providing helper methods for block protection logic,
 * including flag checks, rights validation, ownership verification,
 * and door-specific location resolution.
 *
 * @author rellu
 */
public class ProtectionHelper {

  private ProtectionHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Checks whether the given block implements the {@link Openable} interface.
   *
   * @param b the block to check
   * @return {@code true} if the block is openable, {@code false} otherwise
   */
  public static boolean isOpenAble(Block b) {
    return b.getBlockData() instanceof Openable;
  }

  /**
   * Checks whether the given protection entry has the specified flag set.
   * Supports both {@link JSONArray} and {@link String} flag storage formats.
   *
   * @param protection the protection entry to inspect
   * @param flag       the flag to check for
   * @return {@code true} if the flag is present, {@code false} otherwise
   */
  public static boolean hasFlag(ProtectionEntry protection, ProtectionFlags flag) {
    JSONObject flags = protection.getFlags();
    return !flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS) && flags.get(
        PLUGIN_EVENT_PROTECT_FLAGS) instanceof JSONArray && flags.getJSONArray(
        PLUGIN_EVENT_PROTECT_FLAGS).toList().contains(flag.name())
        || !flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS) && flags.get(
            PLUGIN_EVENT_PROTECT_FLAGS) instanceof String && flags.get(PLUGIN_EVENT_PROTECT_FLAGS)
            .equals(flag.name()
            );
  }

  /**
   * Checks whether the player with the given ID has access rights to the protection entry.
   * Returns {@code true} if no rights restrictions are defined.
   *
   * @param protection the protection entry to inspect
   * @param playerId   the internal ID of the player
   * @return {@code true} if the player has rights or no restrictions exist, {@code false} otherwise
   */
  public static boolean hasRights(ProtectionEntry protection, int playerId) {
    JSONObject rights = protection.getRights();
    if (!rights.isEmpty() && rights.has(PLUGIN_EVENT_PROTECT_RIGHTS)) {
      return !rights.getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS).toList().contains(playerId);
    }
    return true;
  }

  /**
   * Checks whether the player with the given ID is the owner of the protection entry.
   *
   * @param protection the protection entry to inspect
   * @param playerId   the internal ID of the player
   * @return {@code true} if the player is the owner, {@code false} otherwise
   */
  public static boolean isOwner(ProtectionEntry protection, int playerId) {
    return playerId == protection.getCreatedBy();
  }

  /**
   * Resolves the base location of a block, adjusting for the lower half of a door
   * when the given block represents the top half.
   *
   * @param b the block to resolve the location for
   * @return the adjusted {@link org.bukkit.Location}, or {@code null} if the block is {@code null}
   */
  public static Location getLocationFromBlockAlternateForDoor(Block b) {
    if (b != null) {
      Location l = b.getLocation();
      if (isOpenAble(b)) {
        Openable openable = (Openable) b.getBlockData();
        if (openable instanceof Door) {
          Door door = (Door) b.getBlockData();
          if (door.getHalf().equals(Half.TOP)) {
            l.add(0, -1, 0);
          }
        }
      }
      return l;
    }
    return null;
  }

  /**
   * Resolves the adjacent door block that forms the other half of a double door,
   * based on the facing direction and hinge side of the given door.
   *
   * @param door  the door block data used to determine facing and hinge
   * @param block the reference block from which to calculate the adjacent position
   * @return the adjacent openable {@link org.bukkit.block.Block}, or {@code null} if not found
   */
  public static Block getOtherPart(Door door, Block block) {
    Location l = block.getLocation().clone();
    if (door != null) {
      l = addLocationFromOtherPart(l, door.getFacing(), door.getHinge());
    }
    if (isOpenAble(l.getBlock())) {
      return l.getBlock();
    }

    return null;
  }

  private static Location addLocationFromOtherPart(Location location, BlockFace blockFace,
      Hinge hinge) {
    int plus = 1;
    int minus = -1;

    if (hinge.equals(Hinge.RIGHT)) {
      plus = -1;
      minus = 1;
    }

    if (blockFace.equals(BlockFace.EAST)) {
      location = location.add(0, 0, plus);
    }
    if (blockFace.equals(BlockFace.WEST)) {
      location = location.add(0, 0, minus);
    }
    if (blockFace.equals(BlockFace.SOUTH)) {
      location = location.add(minus, 0, 0);
    }
    if (blockFace.equals(BlockFace.NORTH)) {
      location = location.add(plus, 0, 0);
    }
    return location;
  }
}