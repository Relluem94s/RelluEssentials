package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeleportServiceTest {

  @Mock
  private TranslationService translationService;

  @Mock
  private BackService backService;

  @Mock
  private Player player;

  @Mock
  private World world;

  private TeleportService teleportService;

  @BeforeEach
  void setUp() {
    teleportService = new TeleportService(translationService, backService);
  }

  @Test
  void teleportWorldSendsSpawnMessageAndTeleportsPlayer() {
    String worldName = "world";
    Location spawnLocation = new Location(world, 1.0, 64.0, 1.0, 0f, 0f);
    String spawnMessage = "§aSpawn";

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(() -> Bukkit.getWorld(worldName)).thenReturn(world);
      when(world.getSpawnLocation()).thenReturn(spawnLocation);
      when(translationService.getWithPrefix(MessageKey.COMMAND_SPAWN)).thenReturn(spawnMessage);
      when(player.getWorld()).thenReturn(world);
      when(world.getName()).thenReturn(worldName);

      teleportService.teleportWorld(player, worldName);

      assertAll(
          () -> verify(backService).saveBackPoint(player),
          () -> verify(player).teleport(any(Location.class)),
          () -> verify(player).sendMessage(anyString())
      );
    }
  }

  @Test
  void teleportWorldSilentDoesNotSendMessageWhenWorldNotFound() {
    String worldName = "unknown_world";

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(() -> Bukkit.getWorld(worldName)).thenReturn(null);

      teleportService.teleportWorld(player, worldName, true);

      assertAll(
          () -> verify(player, never()).sendMessage(anyString()),
          () -> verify(player, never()).teleport(any(Location.class)),
          () -> verify(backService, never()).saveBackPoint(player)
      );
    }
  }

  @Test
  void teleportWorldNotSilentSendsWorldNotFoundMessageWhenWorldNotFound() {
    String worldName = "unknown_world";
    String notFoundMessage = "§cWorld not found";

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(() -> Bukkit.getWorld(worldName)).thenReturn(null);
      when(translationService.getWithPrefix(MessageKey.PLUGIN_COMMAND_WORLD_NOT_FOUND, worldName))
          .thenReturn(notFoundMessage);

      teleportService.teleportWorld(player, worldName, false);

      assertAll(
          () -> verify(player).sendMessage(notFoundMessage),
          () -> verify(player, never()).teleport(any(Location.class))
      );
    }
  }

  @Test
  void teleportBedSendsNoBedMessageWhenRespawnLocationIsNull() {
    String noBedMessage = "§cNo bed set";
    when(player.getRespawnLocation()).thenReturn(null);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(translationService.getWithPrefix(MessageKey.COMMAND_HOME_NO_BED, "world"))
        .thenReturn(noBedMessage);

    teleportService.teleportBed(player);

    assertAll(
        () -> verify(player).sendMessage(noBedMessage),
        () -> verify(player, never()).teleport(any(Location.class)),
        () -> verify(backService, never()).saveBackPoint(player)
    );
  }

  @Test
  void teleportBedTeleportsPlayerToBedLocation() {
    Location respawnLocation = new Location(world, 10.0, 64.0, 10.0, 90f, 0f);
    String homeMessage = "§aHome";

    when(player.getRespawnLocation()).thenReturn(respawnLocation);
    when(translationService.getWithPrefix(MessageKey.COMMAND_HOME)).thenReturn(homeMessage);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(player.getName()).thenReturn("TestPlayer");

    teleportService.teleportBed(player);

    assertAll(
        () -> verify(backService).saveBackPoint(player),
        () -> verify(player).teleport(any(Location.class)),
        () -> verify(player).sendMessage(anyString())
    );
  }

  @Test
  void teleportBedSilentDoesNotSendErrorMessageWhenWorldIsNull() {
    Location respawnLocationWithNullWorld = new Location(null, 10.0, 64.0, 10.0);

    when(player.getRespawnLocation()).thenReturn(respawnLocationWithNullWorld);
    when(player.getName()).thenReturn("TestPlayer");

    teleportService.teleportBed(player, true);

    assertAll(
        () -> verify(player, never()).teleport(any(Location.class)),
        () -> verify(backService, never()).saveBackPoint(player)
    );
  }

  @Test
  void teleportBedNotSilentSendsWorldNotFoundWhenWorldIsNull() {
    Location respawnLocationWithNullWorld = new Location(null, 10.0, 64.0, 10.0);
    String notFoundMessage = "§cWorld not found";

    when(player.getRespawnLocation()).thenReturn(respawnLocationWithNullWorld);
    when(player.getName()).thenReturn("TestPlayer");
    when(translationService.getWithPrefix(
        eq(MessageKey.PLUGIN_COMMAND_WORLD_NOT_FOUND), anyString()))
        .thenReturn(notFoundMessage);

    teleportService.teleportBed(player, false);

    assertAll(
        () -> verify(player).sendMessage(notFoundMessage),
        () -> verify(player, never()).teleport(any(Location.class))
    );
  }

  @Test
  void teleportHomeTeleportsPlayerToHomeLocationAndSendsMessage() {
    Location homeLocation = new Location(world, 5.0, 70.0, 5.0, 45f, 10f);
    LocationEntry locationEntry = mock(LocationEntry.class);
    String homeName = "myHome";
    String homeMessage = "§aTeleported to home";

    when(locationEntry.getLocation()).thenReturn(homeLocation);
    when(locationEntry.getLocationName()).thenReturn(homeName);
    when(translationService.getWithPrefix(MessageKey.COMMAND_HOME_TP, homeName))
        .thenReturn(homeMessage);
    when(player.getName()).thenReturn("TestPlayer");

    teleportService.teleportHome(player, locationEntry);

    assertAll(
        () -> verify(backService).saveBackPoint(player),
        () -> verify(player).teleport(any(Location.class)),
        () -> verify(player).sendMessage(homeMessage)
    );
  }

  @Test
  void teleportHomeDoesNotTeleportWhenWorldIsNull() {
    Location homeLocationWithNullWorld = new Location(null, 5.0, 70.0, 5.0);
    LocationEntry locationEntry = mock(LocationEntry.class);

    when(locationEntry.getLocation()).thenReturn(homeLocationWithNullWorld);
    when(player.getName()).thenReturn("TestPlayer");

    teleportService.teleportHome(player, locationEntry);

    assertAll(
        () -> verify(player, never()).teleport(any(Location.class)),
        () -> verify(backService, never()).saveBackPoint(player)
    );
  }

  @Test
  void teleportBackTeleportsPlayerAndSendsBackMessage() {
    Location backLocation = new Location(world, 3.0, 65.0, 3.0, 0f, 0f);
    String backMessage = "§aTeleported back";

    when(translationService.getWithPrefix(MessageKey.COMMAND_BACK)).thenReturn(backMessage);

    teleportService.teleportBack(player, backLocation);

    assertAll(
        () -> verify(backService, never()).saveBackPoint(player),
        () -> verify(player).teleport(any(Location.class)),
        () -> verify(player).sendMessage(backMessage)
    );
  }

  @Test
  void teleportBackDoesNotTeleportWhenWorldIsNull() {
    Location backLocationWithNullWorld = new Location(null, 3.0, 65.0, 3.0);

    teleportService.teleportBack(player, backLocationWithNullWorld);

    assertAll(
        () -> verify(player, never()).teleport(any(Location.class)),
        () -> verify(player, never()).sendMessage(anyString())
    );
  }

  @Test
  void teleportWarpTeleportsPlayerAndSendsWarpMessage() {
    Location warpLocation = new Location(world, 20.0, 64.0, 20.0, 180f, 0f);
    String warpMessage = "§aTeleported to warp";

    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP)).thenReturn(warpMessage);

    teleportService.teleportWarp(player, warpLocation);

    assertAll(
        () -> verify(backService).saveBackPoint(player),
        () -> verify(player).teleport(any(Location.class)),
        () -> verify(player).sendMessage(warpMessage)
    );
  }

  @Test
  void teleportWarpDoesNotTeleportWhenWorldIsNull() {
    Location warpLocationWithNullWorld = new Location(null, 20.0, 64.0, 20.0);

    teleportService.teleportWarp(player, warpLocationWithNullWorld);

    assertAll(
        () -> verify(player, never()).teleport(any(Location.class)),
        () -> verify(player, never()).sendMessage(anyString())
    );
  }
}