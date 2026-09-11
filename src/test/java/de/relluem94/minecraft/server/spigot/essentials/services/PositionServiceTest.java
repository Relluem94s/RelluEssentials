package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.PositionRegistry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

  @Mock
  private PositionRegistry positionRegistry;

  @Mock
  private TranslationService translationService;

  @Mock
  private Player player;

  @Mock
  private World world;

  private PositionService positionService;

  @BeforeEach
  void setUp() {
    positionService = new PositionService(positionRegistry, translationService);
  }

  @Test
  void hasPositionsReturnsTrueWhenPlayerIsRegistered() {
    when(positionRegistry.contains(player)).thenReturn(true);

    assertTrue(positionService.hasPositions(player));
  }

  @Test
  void hasPositionsReturnsFalseWhenPlayerIsNotRegistered() {
    when(positionRegistry.contains(player)).thenReturn(false);

    assertFalse(positionService.hasPositions(player));
  }

  @Test
  void ensurePositionsExistCreatesStoreWhenPlayerHasNoPositions() {
    when(positionRegistry.contains(player)).thenReturn(false);

    positionService.ensurePositionsExist(player);

    verify(positionRegistry).put(eq(player), any());
  }

  @Test
  void ensurePositionsExistDoesNotCreateStoreWhenPlayerAlreadyHasPositions() {
    when(positionRegistry.contains(player)).thenReturn(true);

    positionService.ensurePositionsExist(player);

    verify(positionRegistry, never()).put(any(), any());
  }

  @Test
  void setFirstPositionStoresLocationWhenPlayerAlreadyHasStore() {
    Location firstLocation = new Location(world, 1, 2, 3);
    Location existingSecond = new Location(world, 4, 5, 6);
    DoubleStore<Location, Location> existingStore = new DoubleStore<>(null, existingSecond);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, existingStore);

    when(positionRegistry.contains(player)).thenReturn(true);
    when(positionRegistry.getAll()).thenReturn(registryMap);

    positionService.setFirstPosition(player, firstLocation);

    verify(positionRegistry).put(eq(player), any());
  }

  @Test
  void setFirstPositionCreatesNewStoreWhenPlayerHasNoExistingStore() {
    Location firstLocation = new Location(world, 1, 2, 3);

    when(positionRegistry.contains(player)).thenReturn(false);

    positionService.setFirstPosition(player, firstLocation);

    verify(positionRegistry).put(eq(player), any());
  }

  @Test
  void setSecondPositionStoresLocationWhenPlayerAlreadyHasStore() {
    Location secondLocation = new Location(world, 4, 5, 6);
    Location existingFirst = new Location(world, 1, 2, 3);
    DoubleStore<Location, Location> existingStore = new DoubleStore<>(existingFirst, null);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, existingStore);

    when(positionRegistry.contains(player)).thenReturn(true);
    when(positionRegistry.getAll()).thenReturn(registryMap);

    positionService.setSecondPosition(player, secondLocation);

    verify(positionRegistry).put(eq(player), any());
  }

  @Test
  void setSecondPositionCreatesNewStoreWhenPlayerHasNoExistingStore() {
    Location secondLocation = new Location(world, 4, 5, 6);

    when(positionRegistry.contains(player)).thenReturn(false);

    positionService.setSecondPosition(player, secondLocation);

    verify(positionRegistry).put(eq(player), any());
  }

  @Test
  void removeFirstPositionSetsFirstLocationToNull() {
    DoubleStore<Location, Location> store = new DoubleStore<>(new Location(world, 1, 2, 3), null);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    when(positionRegistry.contains(player)).thenReturn(true);
    when(positionRegistry.getAll()).thenReturn(registryMap);

    positionService.removeFirstPosition(player);

    assertNull(store.getValue());
  }

  @Test
  void removeSecondPositionSetsSecondLocationToNull() {
    DoubleStore<Location, Location> store = new DoubleStore<>(null, new Location(world, 4, 5, 6));
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    when(positionRegistry.contains(player)).thenReturn(true);
    when(positionRegistry.getAll()).thenReturn(registryMap);

    positionService.removeSecondPosition(player);

    assertNull(store.getSecondValue());
  }

  @Test
  void shiftPositionsMovesFirstAndSecondLocationByDirectionAndAmount() {
    Location firstLocation = new Location(world, 0, 0, 0);
    Location secondLocation = new Location(world, 10, 10, 10);
    DoubleStore<Location, Location> store = new DoubleStore<>(firstLocation, secondLocation);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    when(positionRegistry.contains(player)).thenReturn(true);
    when(positionRegistry.getAll()).thenReturn(registryMap);

    positionService.shiftPositions(player, new Vector(1, 0, 0), 5);

    assertAll(
        () -> assertEquals(5.0, firstLocation.getX()),
        () -> assertEquals(0.0, firstLocation.getY()),
        () -> assertEquals(0.0, firstLocation.getZ()),
        () -> assertEquals(15.0, secondLocation.getX()),
        () -> assertEquals(10.0, secondLocation.getY()),
        () -> assertEquals(10.0, secondLocation.getZ())
    );
  }

  @Test
  void shiftPositionsOnlyShiftsNonNullLocations() {
    Location firstLocation = new Location(world, 0, 0, 0);
    DoubleStore<Location, Location> store = new DoubleStore<>(firstLocation, null);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    when(positionRegistry.contains(player)).thenReturn(true);
    when(positionRegistry.getAll()).thenReturn(registryMap);

    positionService.shiftPositions(player, new Vector(0, 1, 0), 3);

    assertAll(
        () -> assertEquals(0.0, firstLocation.getX()),
        () -> assertEquals(3.0, firstLocation.getY()),
        () -> assertEquals(0.0, firstLocation.getZ())
    );
  }

  @Test
  void expandOrDecreasePositionsExpandsFartherLocationWhenExpandIsTrue() {
    Location firstLocation = new Location(world, 0, 0, 0);
    Location secondLocation = new Location(world, 10, 0, 0);
    DoubleStore<Location, Location> store = new DoubleStore<>(firstLocation, secondLocation);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    Location playerLocation = new Location(world, 0, 0, 0);
    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.getLocation()).thenReturn(playerLocation);

    positionService.expandOrDecreasePositions(player, new Vector(1, 0, 0), 5, true);

    assertAll(
        () -> assertEquals(0.0, firstLocation.getX()),
        () -> assertEquals(15.0, secondLocation.getX())
    );
  }

  @Test
  void expandOrDecreasePositionsDecreasesFartherLocationWhenExpandIsFalse() {
    Location firstLocation = new Location(world, 0, 0, 0);
    Location secondLocation = new Location(world, 10, 0, 0);
    DoubleStore<Location, Location> store = new DoubleStore<>(firstLocation, secondLocation);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    Location playerLocation = new Location(world, 0, 0, 0);
    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.getLocation()).thenReturn(playerLocation);

    positionService.expandOrDecreasePositions(player, new Vector(1, 0, 0), 5, false);

    assertAll(
        () -> assertEquals(0.0, firstLocation.getX()),
        () -> assertEquals(5.0, secondLocation.getX())
    );
  }

  @Test
  void clearPositionsRemovesPlayerFromRegistry() {
    positionService.clearPositions(player);

    verify(positionRegistry).remove(player);
  }

  @Test
  void getPositionsReturnsStoreForPlayer() {
    DoubleStore<Location, Location> store = new DoubleStore<>(
        new Location(world, 1, 2, 3),
        new Location(world, 4, 5, 6)
    );
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    when(positionRegistry.getAll()).thenReturn(registryMap);

    DoubleStore<Location, Location> result = positionService.getPositions(player);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(store.getValue(), result.getValue()),
        () -> assertEquals(store.getSecondValue(), result.getSecondValue())
    );
  }

  @Test
  void tickHighlightsRemovesOfflinePlayer() {
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, new DoubleStore<>(new Location(world, 0, 0, 0), null));

    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.isOnline()).thenReturn(false);

    positionService.tickHighlights();

    verify(positionRegistry).remove(player);
  }

  @Test
  void tickHighlightsRemovesPlayerWithBothLocationsNull() {
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, new DoubleStore<>(null, null));

    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.isOnline()).thenReturn(true);

    positionService.tickHighlights();

    verify(positionRegistry).remove(player);
  }

  @Test
  void tickHighlightsDrawsBoundingBoxWhenBothLocationsAreInSameWorld() {
    Location firstLocation = new Location(world, 0, 0, 0);
    Location secondLocation = new Location(world, 2, 2, 2);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, new DoubleStore<>(firstLocation, secondLocation));

    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.isOnline()).thenReturn(true);

    positionService.tickHighlights();

    verify(player, never()).sendMessage(any(String.class));
  }

  @Test
  void tickHighlightsSendsMessageWhenLocationsAreInDifferentWorlds() {
    World otherWorld = mock(World.class);
    Location firstLocation = new Location(world, 0, 0, 0);
    Location secondLocation = new Location(otherWorld, 10, 10, 10);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, new DoubleStore<>(firstLocation, secondLocation));

    String expectedMessage = "Different worlds message";
    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.isOnline()).thenReturn(true);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_POSITION_HIGHLIGHTING_DIFFERENT_WORLDS))
        .thenReturn(expectedMessage);

    positionService.tickHighlights();

    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void tickHighlightsDrawsSingleBoxWhenOnlyFirstLocationExists() {
    Location firstLocation = new Location(world, 0, 0, 0);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, new DoubleStore<>(firstLocation, null));

    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.isOnline()).thenReturn(true);

    positionService.tickHighlights();

    verify(player, never()).sendMessage(any(String.class));
  }

  @Test
  void tickHighlightsDrawsSingleBoxWhenOnlySecondLocationExists() {
    Location secondLocation = new Location(world, 5, 5, 5);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, new DoubleStore<>(null, secondLocation));

    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.isOnline()).thenReturn(true);

    positionService.tickHighlights();

    verify(player, never()).sendMessage(any(String.class));
  }

  @Test
  void tickHighlightsSpawnsParticlesForSingleBlock() {
    Location firstLocation = new Location(world, 0, 0, 0);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, new DoubleStore<>(firstLocation, null));

    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.isOnline()).thenReturn(true);

    positionService.tickHighlights();

    verify(player, times(24)).spawnParticle(eq(Particle.COMPOSTER), any(Location.class), eq(1));
  }


  @Test
  void expandOrDecreasePositionsExpandsFirstLocationWhenBothLocationsHaveEqualProjection() {
    Location firstLocation = new Location(world, 5, 0, 0);
    Location secondLocation = new Location(world, 5, 0, 0);
    DoubleStore<Location, Location> store = new DoubleStore<>(firstLocation, secondLocation);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    Location playerLocation = new Location(world, 0, 0, 0);
    when(positionRegistry.getAll()).thenReturn(registryMap);
    when(player.getLocation()).thenReturn(playerLocation);

    positionService.expandOrDecreasePositions(player, new Vector(1, 0, 0), 5, true);

    assertAll(
        () -> assertEquals(10.0, firstLocation.getX()),
        () -> assertEquals(5.0, secondLocation.getX())
    );
  }

  @Test
  void shiftPositionsOnlyShiftsSecondLocationWhenFirstLocationIsNull() {
    Location secondLocation = new Location(world, 0, 0, 0);
    DoubleStore<Location, Location> store = new DoubleStore<>(null, secondLocation);
    Map<Player, DoubleStore<Location, Location>> registryMap = new HashMap<>();
    registryMap.put(player, store);

    when(positionRegistry.contains(player)).thenReturn(true);
    when(positionRegistry.getAll()).thenReturn(registryMap);

    positionService.shiftPositions(player, new Vector(0, 1, 0), 3);

    assertAll(
        () -> assertEquals(0.0, secondLocation.getX()),
        () -> assertEquals(3.0, secondLocation.getY()),
        () -> assertEquals(0.0, secondLocation.getZ())
    );
  }
}