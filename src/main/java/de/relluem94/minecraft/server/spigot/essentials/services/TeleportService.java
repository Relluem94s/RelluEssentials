package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeleportService {

  private final TranslationService translationService;
  private final BackService backService;

  public TeleportService(TranslationService translationService, BackService backService) {
    this.translationService = translationService;
    this.backService = backService;
  }

  public void teleportWorld(Player player, String worldName) {
    teleportWorld(player, worldName, false);
  }

  public void teleportWorld(Player player, String worldName, boolean silent) {
    World world = resolveWorldByName(player, worldName, silent);
    if (world == null) return;

    performTeleport(translationService.getWithPrefix(MessageKey.COMMAND_SPAWN),
        player, world, silent, world.getSpawnLocation(), true);
  }

  public void teleportBed(Player player) {
    teleportBed(player, false);
  }

  public void teleportBed(@NotNull Player player, boolean silent) {
    if (player.getRespawnLocation() == null) {
      player.sendMessage(translationService.getWithPrefix(
          MessageKey.COMMAND_HOME_NO_BED, player.getWorld().getName()));
      return;
    }

    Location respawnLocation = player.getRespawnLocation();
    World world = resolveWorldByLocation(player, silent, respawnLocation,
        "world_of_bed_from_" + player.getName().toLowerCase());
    if (world == null) return;

    performTeleport(translationService.getWithPrefix(MessageKey.COMMAND_HOME),
        player, world, silent, respawnLocation, true);
  }

  public void teleportHome(@NotNull Player player, LocationEntry locationEntry) {
    Location home = locationEntry.getLocation();

    World world = resolveWorldByLocation(player, false, home,
        "world_of_home_from_" + player.getName().toLowerCase());
    if (world == null) return;

    performTeleport("", player, world, true, home, true);
    player.sendMessage(translationService.getWithPrefix(
        MessageKey.COMMAND_HOME_TP, locationEntry.getLocationName()));
  }

  public void teleportBack(Player player, @NotNull Location location) {
    World world = location.getWorld();
    if (world == null) return;

    performTeleport("", player, world, true, location, false);
    player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_BACK));
  }

  public void teleportWarp(Player player, @NotNull Location location) {
    World world = location.getWorld();
    if (world == null) return;

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