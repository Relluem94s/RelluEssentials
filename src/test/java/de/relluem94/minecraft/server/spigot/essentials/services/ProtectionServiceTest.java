package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.ProtectionRepository;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProtectionServiceTest {

  @Mock
  private ProtectionRepository protectionRepository;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PlayerService playerService;

  @Mock
  private World world;

  @Mock
  private Server server;

  private ProtectionService protectionService;

  private Location buildLocation(double x, double y, double z) {
    return new Location(world, x, y, z);
  }

  private ProtectionEntry buildProtectionEntry(Location location, int playerId, int id) {
    LocationEntry locationEntry = new LocationEntry();
    locationEntry.setPlayerId(playerId);
    locationEntry.setLocation(location);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setId(id);
    protectionEntry.setCreatedBy(playerId);
    protectionEntry.setLocationEntry(locationEntry);

    return protectionEntry;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    Field serverField = Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, server);

    lenient().when(server.getWorld(any(String.class))).thenReturn(world);
    lenient().when(world.getName()).thenReturn("world");

    ProtectionLockEntry chestLock = new ProtectionLockEntry();
    chestLock.setValue(Material.CHEST);

    List<ProtectionLockEntry> locks = List.of(chestLock);
    Map<Location, ProtectionEntry> entries = new HashMap<>();

    protectionService = new ProtectionService(locks, entries, protectionRepository, serviceContext);
  }

  @Test
  void removeExplodedBlockProtectionOrCancelExplosionReturnsTrueWhenProtectionExists() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 10);
    protectionService.putProtectionEntry(location, protectionEntry);

    Block block = mock(Block.class);
    when(block.getLocation()).thenReturn(location);

    boolean result = protectionService.removeExplodedBlockProtectionOrCancelExplosion(block);

    assertAll(
        () -> assertTrue(result),
        () -> assertNull(protectionService.getProtectionEntry(location))
    );
    verify(protectionRepository).remove(protectionEntry);
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void removeExplodedBlockProtectionOrCancelExplosionReturnsFalseWhenNoProtectionExists() {
    Location location = buildLocation(1, 2, 3);

    Block block = mock(Block.class);
    when(block.getLocation()).thenReturn(location);

    boolean result = protectionService.removeExplodedBlockProtectionOrCancelExplosion(block);

    assertAll(
        () -> assertFalse(result)
    );
    verify(protectionRepository, never()).remove(null);
  }

  @Test
  void getProtectionEntryReturnsEntryWhenPresent() {
    Location location = buildLocation(4, 5, 6);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 20);
    protectionService.putProtectionEntry(location, protectionEntry);

    ProtectionEntry result = protectionService.getProtectionEntry(location);

    assertAll(
        () -> assertEquals(protectionEntry, result),
        () -> assertEquals(20, result.getId())
    );
  }

  @Test
  void getProtectionEntryReturnsNullWhenNotPresent() {
    Location location = buildLocation(99, 99, 99);

    ProtectionEntry result = protectionService.getProtectionEntry(location);

    assertNull(result);
  }

  @Test
  void removeProtectionEntryRemovesEntryFromRegistry() {
    Location location = buildLocation(7, 8, 9);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 30);
    protectionService.putProtectionEntry(location, protectionEntry);

    protectionService.removeProtectionEntry(location);

    assertNull(protectionService.getProtectionEntry(location));
  }

  @Test
  void putProtectionEntryStoresEntryInRegistry() {
    Location location = buildLocation(10, 11, 12);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 2, 40);

    protectionService.putProtectionEntry(location, protectionEntry);

    ProtectionEntry result = protectionService.getProtectionEntry(location);

    assertAll(
        () -> assertEquals(protectionEntry, result),
        () -> assertEquals(40, result.getId()),
        () -> assertEquals(2, result.getCreatedBy())
    );
  }

  @Test
  void getProtectionEntriesOwnedByReturnsOnlyMatchingEntries() {
    Location locationA = buildLocation(1, 1, 1);
    Location locationB = buildLocation(2, 2, 2);
    Location locationC = buildLocation(3, 3, 3);

    ProtectionEntry entryOwnedByPlayer1 = buildProtectionEntry(locationA, 1, 1);
    ProtectionEntry entryAlsoOwnedByPlayer1 = buildProtectionEntry(locationB, 1, 2);
    ProtectionEntry entryOwnedByPlayer2 = buildProtectionEntry(locationC, 2, 3);

    protectionService.putProtectionEntry(locationA, entryOwnedByPlayer1);
    protectionService.putProtectionEntry(locationB, entryAlsoOwnedByPlayer1);
    protectionService.putProtectionEntry(locationC, entryOwnedByPlayer2);

    List<ProtectionEntry> result = protectionService.getProtectionEntriesOwnedBy(1L);

    assertAll(
        () -> assertEquals(2, result.size()),
        () -> assertTrue(result.contains(entryOwnedByPlayer1)),
        () -> assertTrue(result.contains(entryAlsoOwnedByPlayer1)),
        () -> assertFalse(result.contains(entryOwnedByPlayer2))
    );
  }

  @Test
  void getProtectionEntriesOwnedByReturnsEmptyListWhenNoMatch() {
    Location location = buildLocation(5, 5, 5);
    ProtectionEntry entry = buildProtectionEntry(location, 3, 50);
    protectionService.putProtectionEntry(location, entry);

    List<ProtectionEntry> result = protectionService.getProtectionEntriesOwnedBy(99L);

    assertTrue(result.isEmpty());
  }

  @Test
  void getAllProtectionEntriesReturnsAllStoredEntries() {
    Location locationA = buildLocation(1, 1, 1);
    Location locationB = buildLocation(2, 2, 2);

    ProtectionEntry entryA = buildProtectionEntry(locationA, 1, 1);
    ProtectionEntry entryB = buildProtectionEntry(locationB, 2, 2);

    protectionService.putProtectionEntry(locationA, entryA);
    protectionService.putProtectionEntry(locationB, entryB);

    Map<Location, ProtectionEntry> result = protectionService.getAllProtectionEntries();

    assertAll(
        () -> assertEquals(2, result.size()),
        () -> assertTrue(result.containsValue(entryA)),
        () -> assertTrue(result.containsValue(entryB))
    );
  }

  @Test
  void isProtectableMaterialReturnsTrueForRegisteredMaterial() {
    assertTrue(protectionService.isProtectableMaterial(Material.CHEST));
  }

  @Test
  void isProtectableMaterialReturnsFalseForUnregisteredMaterial() {
    assertFalse(protectionService.isProtectableMaterial(Material.DIRT));
  }

  @Test
  void saveProtectionAndAddToRegistryPersistsAndRegistersEntry() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 60);
    ProtectionEntry savedProtectionEntry = buildProtectionEntry(location, 1, 60);

    when(protectionRepository.findByLocation(location)).thenReturn(savedProtectionEntry);

    protectionService.saveProtectionAndAddToRegistry(location, protectionEntry);

    assertAll(
        () -> verify(protectionRepository).save(protectionEntry),
        () -> verify(protectionRepository).findByLocation(location),
        () -> assertEquals(savedProtectionEntry, protectionService.getProtectionEntry(location))
    );
  }

  @Test
  void saveProtectionAndAddToRegistryDoesNotAddToRegistryWhenRepositoryReturnsNull() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 60);

    when(protectionRepository.findByLocation(location)).thenReturn(null);

    protectionService.saveProtectionAndAddToRegistry(location, protectionEntry);

    assertAll(
        () -> verify(protectionRepository).save(protectionEntry),
        () -> assertNull(protectionService.getProtectionEntry(location))
    );
  }

  @Test
  void updateProtectionFlagsUpdatesRepositoryAndRegistry() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 70);

    protectionService.updateProtectionFlags(protectionEntry);

    assertAll(
        () -> verify(protectionRepository).updateFlags(protectionEntry),
        () -> assertEquals(protectionEntry, protectionService.getProtectionEntry(location))
    );
  }

  @Test
  void updateProtectionRightsUpdatesRepositoryAndRegistry() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 80);

    protectionService.updateProtectionRights(protectionEntry);

    assertAll(
        () -> verify(protectionRepository).updateRights(protectionEntry),
        () -> assertEquals(protectionEntry, protectionService.getProtectionEntry(location))
    );
  }

  @Test
  void findProtectionByLocationDelegatesToRepository() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 90);

    when(protectionRepository.findByLocation(location)).thenReturn(protectionEntry);

    ProtectionEntry result = protectionService.findProtectionByLocation(location);

    assertAll(
        () -> assertEquals(protectionEntry, result),
        () -> verify(protectionRepository).findByLocation(location)
    );
  }

  @Test
  void findProtectionByLocationReturnsNullWhenNotFound() {
    Location location = buildLocation(99, 99, 99);

    when(protectionRepository.findByLocation(location)).thenReturn(null);

    ProtectionEntry result = protectionService.findProtectionByLocation(location);

    assertNull(result);
  }

  @Test
  void deleteProtectionAndRemoveFromRegistryRemovesFromBoth() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 100);
    protectionService.putProtectionEntry(location, protectionEntry);

    protectionService.deleteProtectionAndRemoveFromRegistry(protectionEntry);

    assertAll(
        () -> verify(protectionRepository).remove(protectionEntry),
        () -> assertNull(protectionService.getProtectionEntry(location))
    );
  }

  @Test
  void removeOutdatedProtectionsFromDatabaseAndRegistryReturnsCorrectCount() {
    Location locationA = buildLocation(1, 1, 1);
    Location locationB = buildLocation(2, 2, 2);

    ProtectionEntry entryA = buildProtectionEntry(locationA, 1, 1);
    ProtectionEntry entryB = buildProtectionEntry(locationB, 2, 2);

    protectionService.putProtectionEntry(locationA, entryA);
    protectionService.putProtectionEntry(locationB, entryB);

    when(protectionRepository.removeOutdatedProtections()).thenReturn(List.of(1L, 2L));

    int result = protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry();

    assertAll(
        () -> assertEquals(2, result),
        () -> assertNull(protectionService.getProtectionEntry(locationA)),
        () -> assertNull(protectionService.getProtectionEntry(locationB))
    );
  }

  @Test
  void removeOutdatedProtectionsFromDatabaseAndRegistryReturnsZeroWhenNoneDeleted() {
    when(protectionRepository.removeOutdatedProtections()).thenReturn(List.of());

    int result = protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry();

    assertEquals(0, result);
  }

  @Test
  void playerOwnsProtectionReturnsTrueWhenPlayerIsOwner() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 5, 10);

    Player player = mock(Player.class);
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(5);

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    boolean result = protectionService.playerOwnsProtection(protectionEntry, player);

    assertTrue(result);
  }

  @Test
  void playerOwnsProtectionReturnsFalseWhenPlayerIsNotOwner() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 5, 10);

    Player player = mock(Player.class);
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(99);

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    boolean result = protectionService.playerOwnsProtection(protectionEntry, player);

    assertFalse(result);
  }

  @Test
  void playerOwnsProtectionReturnsTrueWhenProtectionEntryIsNull() {
    Player player = mock(Player.class);
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(1);

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    boolean result = protectionService.playerOwnsProtection(null, player);

    assertTrue(result);
  }

  @Test
  void removeBlockProtectionIfExistsDeletesProtectionWhenMaterialIsProtectableAndProtectionExists() {
    Location location = buildLocation(1, 2, 3);
    ProtectionEntry protectionEntry = buildProtectionEntry(location, 1, 10);
    protectionService.putProtectionEntry(location, protectionEntry);

    Block block = mock(Block.class);
    when(block.getType()).thenReturn(Material.CHEST);
    when(block.getLocation()).thenReturn(location);

    protectionService.removeBlockProtectionIfExists(block);

    assertAll(
        () -> verify(protectionRepository).remove(protectionEntry),
        () -> assertNull(protectionService.getProtectionEntry(location))
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void removeBlockProtectionIfExistsDoesNothingWhenMaterialIsNotProtectable() {
    Block block = mock(Block.class);
    when(block.getType()).thenReturn(Material.DIRT);

    protectionService.removeBlockProtectionIfExists(block);

    verify(protectionRepository, never()).remove(null);
    verify(block, never()).getLocation();
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void removeBlockProtectionIfExistsDoesNothingWhenProtectionDoesNotExist() {
    Location location = buildLocation(1, 2, 3);

    Block block = mock(Block.class);
    when(block.getType()).thenReturn(Material.CHEST);
    when(block.getLocation()).thenReturn(location);

    protectionService.removeBlockProtectionIfExists(block);

    verify(protectionRepository, never()).remove(null);
  }
}