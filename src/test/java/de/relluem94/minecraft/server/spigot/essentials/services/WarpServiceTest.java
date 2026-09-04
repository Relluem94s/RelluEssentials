package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import java.util.List;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WarpServiceTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private LocationService locationService;

  @Mock
  private World world;

  @Mock
  private Player player;

  @Mock
  private LocationEntry locationEntry;

  private WarpService warpService;

  @BeforeEach
  void setUp() {
    when(serviceContext.getLocationService()).thenReturn(locationService);
    warpService = new WarpService(serviceContext);
  }

  @Test
  void findWarpByNameReturnsMatchingWarp() {
    when(locationEntry.getLocationName()).thenReturn("spawn");
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    Optional<LocationEntry> result = warpService.findWarpByName("spawn");

    assertTrue(result.isPresent());
    assertEquals(locationEntry, result.get());
  }

  @Test
  void findWarpByNameReturnsEmptyWhenNoMatch() {
    when(locationEntry.getLocationName()).thenReturn("other");
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    Optional<LocationEntry> result = warpService.findWarpByName("spawn");

    assertFalse(result.isPresent());
  }

  @Test
  void findWarpByNameReturnsEmptyWhenListIsEmpty() {
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of());

    Optional<LocationEntry> result = warpService.findWarpByName("spawn");

    assertFalse(result.isPresent());
  }

  @Test
  void findWarpByNameAndWorldReturnsMatchingWarp() {
    org.bukkit.Location bukkitLocation = mock(org.bukkit.Location.class);
    when(locationEntry.getLocationName()).thenReturn("spawn");
    when(locationEntry.getLocation()).thenReturn(bukkitLocation);
    when(bukkitLocation.getWorld()).thenReturn(world);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    Optional<LocationEntry> result = warpService.findWarpByNameAndWorld("spawn", world);

    assertTrue(result.isPresent());
    assertEquals(locationEntry, result.get());
  }

  @Test
  void findWarpByNameAndWorldReturnsEmptyWhenNameDoesNotMatch() {
    org.bukkit.Location bukkitLocation = mock(org.bukkit.Location.class);
    when(locationEntry.getLocationName()).thenReturn("other");
    when(locationEntry.getLocation()).thenReturn(bukkitLocation);
    when(bukkitLocation.getWorld()).thenReturn(world);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    Optional<LocationEntry> result = warpService.findWarpByNameAndWorld("spawn", world);

    assertFalse(result.isPresent());
  }

  @Test
  void findWarpByNameAndWorldReturnsEmptyWhenWorldDoesNotMatch() {
    World differentWorld = mock(World.class);
    org.bukkit.Location bukkitLocation = mock(org.bukkit.Location.class);
    when(locationEntry.getLocationName()).thenReturn("spawn");
    when(locationEntry.getLocation()).thenReturn(bukkitLocation);
    when(bukkitLocation.getWorld()).thenReturn(differentWorld);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    Optional<LocationEntry> result = warpService.findWarpByNameAndWorld("spawn", world);

    assertFalse(result.isPresent());
  }

  @Test
  void findWarpByNameAndWorldReturnsEmptyWhenLocationIsNull() {
    when(locationEntry.getLocation()).thenReturn(null);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    Optional<LocationEntry> result = warpService.findWarpByNameAndWorld("spawn", world);

    assertFalse(result.isPresent());
  }

  @Test
  void findWarpByNameAndWorldReturnsEmptyWhenLocationWorldIsNull() {
    org.bukkit.Location bukkitLocation = mock(org.bukkit.Location.class);
    when(locationEntry.getLocation()).thenReturn(bukkitLocation);
    when(bukkitLocation.getWorld()).thenReturn(null);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    Optional<LocationEntry> result = warpService.findWarpByNameAndWorld("spawn", world);

    assertFalse(result.isPresent());
  }

  @Test
  void findWarpsByWorldReturnsWarpsInWorld() {
    org.bukkit.Location bukkitLocation = mock(org.bukkit.Location.class);
    when(locationEntry.getLocation()).thenReturn(bukkitLocation);
    when(bukkitLocation.getWorld()).thenReturn(world);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    List<LocationEntry> result = warpService.findWarpsByWorld(world);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(locationEntry, result.getFirst())
    );
  }

  @Test
  void findWarpsByWorldReturnsEmptyListWhenNoWarpsInWorld() {
    World differentWorld = mock(World.class);
    org.bukkit.Location bukkitLocation = mock(org.bukkit.Location.class);
    when(locationEntry.getLocation()).thenReturn(bukkitLocation);
    when(bukkitLocation.getWorld()).thenReturn(differentWorld);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    List<LocationEntry> result = warpService.findWarpsByWorld(world);

    assertTrue(result.isEmpty());
  }

  @Test
  void findWarpsByWorldExcludesEntriesWithNullLocation() {
    when(locationEntry.getLocation()).thenReturn(null);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    List<LocationEntry> result = warpService.findWarpsByWorld(world);

    assertTrue(result.isEmpty());
  }

  @Test
  void findWarpsByWorldExcludesEntriesWithNullLocationWorld() {
    org.bukkit.Location bukkitLocation = mock(org.bukkit.Location.class);
    when(locationEntry.getLocation()).thenReturn(bukkitLocation);
    when(bukkitLocation.getWorld()).thenReturn(null);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    List<LocationEntry> result = warpService.findWarpsByWorld(world);

    assertTrue(result.isEmpty());
  }

  @Test
  void getWarpNamesByWorldReturnsNamesOfWarpsInWorld() {
    org.bukkit.Location bukkitLocation = mock(org.bukkit.Location.class);
    when(locationEntry.getLocation()).thenReturn(bukkitLocation);
    when(bukkitLocation.getWorld()).thenReturn(world);
    when(locationEntry.getLocationName()).thenReturn("spawn");
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    List<String> result = warpService.getWarpNamesByWorld(world);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals("spawn", result.getFirst())
    );
  }

  @Test
  void getWarpNamesByWorldReturnsEmptyListWhenNoWarpsInWorld() {
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of());

    List<String> result = warpService.getWarpNamesByWorld(world);

    assertTrue(result.isEmpty());
  }

  @Test
  void warpExistsReturnsTrueWhenWarpFound() {
    when(locationEntry.getLocationName()).thenReturn("spawn");
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    boolean result = warpService.warpExists("spawn");

    assertTrue(result);
  }

  @Test
  void warpExistsReturnsFalseWhenWarpNotFound() {
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of());

    boolean result = warpService.warpExists("spawn");

    assertFalse(result);
  }

  @Test
  void addWarpReturnsTrueAndSavesWhenWarpDoesNotExist() {
    LocationEntry builtEntry = mock(LocationEntry.class);
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of());
    when(locationService.buildLocationEntry(player, "spawn", LocationType.WARP, 1)).thenReturn(builtEntry);

    boolean result = warpService.addWarp("spawn", player, 1);

    assertAll(
        () -> assertTrue(result),
        () -> verify(locationService).saveAndFetch(builtEntry)
    );
  }

  @Test
  void addWarpReturnsFalseWhenWarpAlreadyExists() {
    when(locationEntry.getLocationName()).thenReturn("spawn");
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    boolean result = warpService.addWarp("spawn", player, 1);

    assertAll(
        () -> assertFalse(result),
        () -> verify(locationService, never()).saveAndFetch(any())
    );
  }

  @Test
  void removeWarpReturnsTrueAndDeletesWhenWarpExists() {
    when(locationEntry.getLocationName()).thenReturn("spawn");
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of(locationEntry));

    boolean result = warpService.removeWarp("spawn");

    assertAll(
        () -> assertTrue(result),
        () -> verify(locationService).delete(locationEntry)
    );
  }

  @Test
  void removeWarpReturnsFalseWhenWarpDoesNotExist() {
    when(locationService.findByType(LocationType.WARP)).thenReturn(List.of());

    boolean result = warpService.removeWarp("spawn");

    assertAll(
        () -> assertFalse(result),
        () -> verify(locationService, never()).delete(any())
    );
  }
}