package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettingPlayerRegistryTest {

  private SettingPlayerRegistry registry;

  @BeforeEach
  void setUp() {
    registry = new SettingPlayerRegistry();
  }

  @Test
  void putAndFindByIdShouldReturnEntry() {
    SettingPlayerEntry entry = createEntry(1, 100, 10);
    registry.put(entry);

    Optional<SettingPlayerEntry> result = registry.findById(1);

    assertTrue(result.isPresent());
    assertEquals(entry, result.get());
  }

  @Test
  void removeShouldRemoveEntry() {
    SettingPlayerEntry entry = createEntry(1, 100, 10);
    registry.put(entry);
    registry.remove(1);

    Optional<SettingPlayerEntry> result = registry.findById(1);

    assertFalse(result.isPresent());
  }

  @Test
  void findByPlayerIdAndSettingIdShouldReturnCorrectEntry() {
    SettingPlayerEntry entry1 = createEntry(1, 100, 10);
    SettingPlayerEntry entry2 = createEntry(2, 100, 20);
    registry.put(entry1);
    registry.put(entry2);

    Optional<SettingPlayerEntry> result = registry.findByPlayerIdAndSettingId(100, 20);

    assertTrue(result.isPresent());
    assertEquals(2, result.get().getId());
  }


  @Test
  void findByPlayerIdAndSettingIdShouldReturnEmptyWhenCriteriaNotMet() {
    SettingPlayerEntry entry = createEntry(1, 100, 10);
    registry.put(entry);

    assertFalse(registry.findByPlayerIdAndSettingId(100, 99).isPresent());
    assertFalse(registry.findByPlayerIdAndSettingId(99, 10).isPresent());
    assertFalse(registry.findByPlayerIdAndSettingId(99, 99).isPresent());
  }


  @Test
  void findAllByPlayerIdShouldReturnAllEntriesForPlayer() {
    SettingPlayerEntry entry1 = createEntry(1, 100, 10);
    SettingPlayerEntry entry2 = createEntry(2, 100, 20);
    SettingPlayerEntry entry3 = createEntry(3, 200, 10);
    registry.put(entry1);
    registry.put(entry2);
    registry.put(entry3);

    List<SettingPlayerEntry> player100Entries = registry.findAllByPlayerId(100);

    assertEquals(2, player100Entries.size());
    assertTrue(player100Entries.contains(entry1));
    assertTrue(player100Entries.contains(entry2));
    assertFalse(player100Entries.contains(entry3));
  }

  @Test
  void loadAllForPlayerShouldReplaceExistingEntriesForThatPlayer() {
    SettingPlayerEntry oldEntry = createEntry(1, 100, 10);
    SettingPlayerEntry otherPlayerEntry = createEntry(2, 200, 10);
    registry.put(oldEntry);
    registry.put(otherPlayerEntry);

    SettingPlayerEntry newEntry = createEntry(3, 100, 15);
    registry.loadAllForPlayer(100, List.of(newEntry));

    assertEquals(1, registry.findAllByPlayerId(100).size());
    assertEquals(3, registry.findAllByPlayerId(100).get(0).getId());
    assertTrue(registry.findById(2).isPresent());
    assertFalse(registry.findById(1).isPresent());
  }

  private SettingPlayerEntry createEntry(int id, int playerId, int settingId) {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setId(id);
    entry.setPlayerFk(playerId);
    entry.setSettingFk(settingId);
    return entry;
  }
}