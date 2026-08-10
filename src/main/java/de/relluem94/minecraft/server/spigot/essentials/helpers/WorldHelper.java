package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_WORLD_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_WORLD_NOT_LOADED;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import java.io.File;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldHelper {

  private WorldHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static boolean isInWorld(Player player, String worldName) {
    return player.getWorld().getName().equalsIgnoreCase(worldName);
  }

  public static boolean isInWorld(CommandSender sender, String worldName) {
    if (TypeHelper.isPlayer(sender)) {
      return isInWorld((Player) sender, worldName);
    }
    return true;
  }

  public static boolean isInWorld(Player player, List<String> worlds) {
    return worlds.contains(player.getWorld().getName());
  }

  public static boolean isInWorld(CommandSender sender, World world) {
    if (TypeHelper.isPlayer(sender)) {
      return isInWorld((Player) sender, world);
    }
    return true;
  }

  public static boolean isInWorld(Block block, World world) {
    return block.getWorld().equals(world);
  }

  public static boolean isInWorld(Entity entity, World world) {
    return entity.getWorld().equals(world);
  }

  public static void createWorld(String worldName, WorldType type,
      World.Environment worldEnvironment, boolean structures) {
    WorldCreator worldCreator = new WorldCreator(worldName);
    worldCreator.environment(worldEnvironment);
    worldCreator.type(type);
    worldCreator.generateStructures(structures);
    Bukkit.createWorld(worldCreator);
  }

  public static void createWorld(String worldName, WorldType type,
      World.Environment worldEnvironment, boolean structures, long seed) {
    WorldCreator worldCreator = new WorldCreator(worldName);
    worldCreator.environment(worldEnvironment);
    worldCreator.type(type);
    worldCreator.generateStructures(structures);
    worldCreator.seed(seed);
    Bukkit.createWorld(worldCreator);
  }

  public static boolean worldExists(String worldName) {
    return new File(Bukkit.getWorldContainer(), worldName).exists();
  }

  public static void loadWorld(String worldName) {
    Bukkit.createWorld(new WorldCreator(worldName));
  }

  public static void unloadWorld(String worldName, boolean save) throws WorldNotLoadedException {
    if (Bukkit.getWorld(worldName) != null) {
      Bukkit.unloadWorld(worldName, save);
    } else {
      throw new WorldNotLoadedException(String.format(PLUGIN_EXCEPTION_WORLD_NOT_LOADED, worldName));
    }
  }

  public static void cloneWorld(String clonedWorldName, String originalWorldName)
      throws WorldNotFoundException {
    World originalWorld = Bukkit.getWorld(originalWorldName);
    if (originalWorld == null) {
      throw new WorldNotFoundException(
          String.format(PLUGIN_EXCEPTION_WORLD_NOT_FOUND, originalWorldName));
    }
    WorldCreator worldCreator = new WorldCreator(clonedWorldName);
    worldCreator.copy(Objects.requireNonNull(originalWorld));
    Bukkit.createWorld(worldCreator);
  }

  private static boolean isInWorld(Player player, World world) {
    return player.getWorld().equals(world);
  }
}