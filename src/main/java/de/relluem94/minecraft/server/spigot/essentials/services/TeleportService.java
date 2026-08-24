package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Service responsible for handling various teleportation logic within the server.
 */
public class TeleportService {

  private final TranslationService translationService;
  private final BackService backService;

  /**
   * Constructs a new TeleportService.
   *
   * @param translationService the service used for retrieving translated messages
   * @param backService        the service used for saving the player's previous location
   */
  public TeleportService(TranslationService translationService, BackService backService) {
    this.translationService = translationService;
    this.backService = backService;
  }

  /**
   * Teleports a player to the spawn location of a specific world.
   *
   * @param player     the player to teleport
   * @param worldName  the name of the target world
   */
  public void teleportWorld(Player player, String worldName) {
    teleportWorld(player, worldName, false);
  }

  /**
   * Teleports a player to the spawn location of a specific world.
   *
   * @param player    the player to teleport
   * @param worldName the name of the target world
   * @param silent    whether to suppress error messages if the world is not found
   */
  public void teleportWorld(Player player, String worldName, boolean silent) {
    World world = resolveWorldByName(player, worldName, silent);
    if (world == null) {
      return;
    }

    performTeleport(translationService.getWithPrefix(MessageKey.COMMAND_SPAWN),
        player, world, silent, world.getSpawnLocation(), true);
  }

  /**
   * Teleports a player to their bed location.
   *
   * @param player the player to teleport
   */
  public void teleportBed(Player player) {
    teleportBed(player, false);
  }

  /**
   * Teleports a player to their bed location.
   *
   * @param player the player to teleport
   * @param silent whether to suppress error messages if no bed is set
   */
  public void teleportBed(@NotNull Player player, boolean silent) {
    if (player.getRespawnLocation() == null) {
      player.sendMessage(translationService.getWithPrefix(
          MessageKey.COMMAND_HOME_NO_BED, player.getWorld().getName()));
      return;
    }

    Location respawnLocation = player.getRespawnLocation();
    World world = resolveWorldByLocation(player, silent, respawnLocation,
        "world_of_bed_from_" + player.getName().toLowerCase());
    if (world == null) {
      return;
    }

    performTeleport(translationService.getWithPrefix(MessageKey.COMMAND_HOME),
        player, world, silent, respawnLocation, true);
  }

  /**
   * Teleports a player to a saved home location.
   *
   * @param player       the player to teleport
   * @param locationEntry the entry containing the home location details
   */
  public void teleportHome(@NotNull Player player, LocationEntry locationEntry) {
    Location home = locationEntry.getLocation();

    World world = resolveWorldByLocation(player, false, home,
        "world_of_home_from_" + player.getName().toLowerCase());
    if (world == null) {
      return;
    }

    performTeleport("", player, world, true, home, true);
    player.sendMessage(translationService.getWithPrefix(
        MessageKey.COMMAND_HOME_TP, locationEntry.getLocationName()));
  }

  /**
   * Teleports a player back to their previous location.
   *
   * @param player   the player to teleport
   * @param location the location to teleport to
   */
  public void teleportBack(Player player, @NotNull Location location) {
    World world = location.getWorld();
    if (world == null) {
      return;
    }

    performTeleport("", player, world, true, location, false);
    player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_BACK));
  }

  /**
   * Teleports a player to a warp location.
   *
   * @param player   the player to teleport
   * @param location the location of the warp
   */
  public void teleportWarp(Player player, @NotNull Location location) {
    World world = location.getWorld();
    if (world == null) {
      return;
    }

    performTeleport("", player, world, true, location, true);
    player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_WARP));
  }

  private @Nullable World resolveWorldByName(Player player, String worldName, boolean silent) {
    World world = Bukkit.getWorld(worldName);
    if (world == null && !silent) {
      player.sendMessage(translationService.getWithPrefix(
          MessageKey.PLUGIN_COMMAND_WORLD_NOT_FOUND, worldName));
    }
    return world;
  }

  private @Nullable World resolveWorldByLocation(Player player, boolean silent,
      @NotNull Location location, String worldName) {
    World world = location.getWorld();
    if (world == null && !silent) {
      player.sendMessage(translationService.getWithPrefix(
          MessageKey.PLUGIN_COMMAND_WORLD_NOT_FOUND, worldName));
    }
    return world;
  }

  private void performTeleport(String message, Player player, World world, boolean silent,
      @NotNull Location location, boolean saveBackPoint) {
    if (saveBackPoint) {
      backService.saveBackPoint(player);
    }

    Location targetLocation = new Location(world,
        location.getX(), location.getY(), location.getZ(),
        location.getYaw(), location.getPitch());
    player.teleport(targetLocation);

    if (!silent) {
      player.sendMessage(String.format(message, player.getWorld().getName()));
    }
  }
}