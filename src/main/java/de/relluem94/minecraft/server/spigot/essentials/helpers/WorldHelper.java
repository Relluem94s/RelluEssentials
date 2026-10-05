package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Provides utility methods for world-related checks in a Minecraft Spigot environment.
 *
 * @author rellu
 */
public class WorldHelper {

  private WorldHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Checks whether the given player is currently in the world with the specified name.
   *
   * @param player the player whose current world is checked
   * @param worldName the name of the world to check against
   * @return {@code true} if the player is in the world with the given name, {@code false} otherwise
   */
  public static boolean isInWorld(Player player, String worldName) {
    return player.getWorld().getName().equalsIgnoreCase(worldName);
  }

  /**
   * Checks whether the given command sender is currently in the world with the specified name.
   * If the sender is not a player, this method returns {@code true}.
   *
   * @param sender the command sender whose current world is checked
   * @param worldName the name of the world to check against
   * @return {@code true} if the sender is in the world with the given name or is not a player, {@code false} otherwise
   */
  public static boolean isInWorld(CommandSender sender, String worldName) {
    if (TypeHelper.isPlayer(sender)) {
      return isInWorld((Player) sender, worldName);
    }
    return true;
  }

  /**
   * Checks whether the given player is currently in one of the specified worlds.
   *
   * @param player the player whose current world is checked
   * @param worlds a list of world names to check against
   * @return {@code true} if the player's current world is contained in the given list, {@code false} otherwise
   */
  public static boolean isInWorld(Player player, List<String> worlds) {
    return worlds.contains(player.getWorld().getName());
  }

  /**
   * Checks whether the given command sender is currently in the specified world.
   * If the sender is not a player, this method returns {@code true}.
   *
   * @param sender the command sender whose current world is checked
   * @param world the world to check against
   * @return {@code true} if the sender is in the given world or is not a player, {@code false} otherwise
   */
  public static boolean isInWorld(CommandSender sender, World world) {
    if (TypeHelper.isPlayer(sender)) {
      return isInWorld((Player) sender, world);
    }
    return true;
  }

  /**
   * Checks whether the given block is located in the specified world.
   *
   * @param block the block whose world is checked
   * @param world the world to check against
   * @return {@code true} if the block is in the given world, {@code false} otherwise
   */
  public static boolean isInWorld(Block block, World world) {
    return block.getWorld().equals(world);
  }

  /**
   * Checks whether the given entity is currently in the specified world.
   *
   * @param entity the entity whose current world is checked
   * @param world the world to check against
   * @return {@code true} if the entity is in the given world, {@code false} otherwise
   */
  public static boolean isInWorld(Entity entity, World world) {
    return entity.getWorld().equals(world);
  }

  private static boolean isInWorld(Player player, World world) {
    return player.getWorld().equals(world);
  }
}