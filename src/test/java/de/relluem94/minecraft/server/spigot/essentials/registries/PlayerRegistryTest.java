package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PlayerRegistryTest {

  private PlayerRegistry playerRegistry;
  private UUID testUuid;
  private PlayerEntry testEntry;

  @BeforeEach
  void setUp() {
    playerRegistry = new PlayerRegistry();
    testUuid = UUID.randomUUID();
    testEntry = new PlayerEntry();
    testEntry.setUuid(testUuid.toString());
    testEntry.setId(1);
  }

  @Test
  void testPutAndGetPlayerEntryByUuid() {
    playerRegistry.putPlayerEntry(testUuid, testEntry);

    PlayerEntry retrieved = playerRegistry.getPlayerEntry(testUuid);

    assertNotNull(retrieved);
    assertEquals(testEntry, retrieved);
  }

  @Test
  void testGetPlayerEntryById() {
    PlayerEntry entryWithId = new PlayerEntry();
    entryWithId.setId(99);

    playerRegistry.putPlayerEntry(testUuid, entryWithId);

    PlayerEntry retrieved = playerRegistry.getPlayerEntry(99);

    assertNotNull(retrieved);
    assertEquals(99, retrieved.getId());
  }

  @Test
  void testGetPlayerEntryByIdWithNoMatch() {
    PlayerEntry entryWithDifferentId = new PlayerEntry();
    entryWithDifferentId.setId(1);

    playerRegistry.putPlayerEntry(testUuid, entryWithDifferentId);

    PlayerEntry retrieved = playerRegistry.getPlayerEntry(99);

    assertNull(retrieved);
  }


  @Test
  void testGetPlayerEntryByIdNotFound() {
    PlayerEntry retrieved = playerRegistry.getPlayerEntry(999);

    assertNull(retrieved);
  }

  @Test
  void testGetPlayerEntryByPlayer() {
    Player mockPlayer = Mockito.mock(Player.class);
    when(mockPlayer.getUniqueId()).thenReturn(testUuid);

    playerRegistry.putPlayerEntry(testUuid, testEntry);

    PlayerEntry retrieved = playerRegistry.getPlayerEntry(mockPlayer);

    assertNotNull(retrieved);
    assertEquals(testEntry, retrieved);
  }

  @Test
  void testGetAllPlayerEntries() {
    PlayerEntry entry2 = new PlayerEntry();
    entry2.setId(2);

    playerRegistry.putPlayerEntry(testUuid, testEntry);
    playerRegistry.putPlayerEntry(UUID.randomUUID(), entry2);

    List<PlayerEntry> allEntries = playerRegistry.getAllPlayerEntries();

    assertEquals(2, allEntries.size());
    assertTrue(allEntries.contains(testEntry));
    assertTrue(allEntries.contains(entry2));
  }

  @Test
  void testClearPlayerEntries() {
    playerRegistry.putPlayerEntry(testUuid, testEntry);

    playerRegistry.clearPlayerEntries();

    assertTrue(playerRegistry.getAllPlayerEntries().isEmpty());
    assertNull(playerRegistry.getPlayerEntry(testUuid));
  }

  @Test
  void testGetPlayerEntryMapGetter() {
    playerRegistry.putPlayerEntry(testUuid, testEntry);

    assertFalse(playerRegistry.getPlayerEntryMap().isEmpty());
    assertEquals(1, playerRegistry.getPlayerEntryMap().size());
  }
}