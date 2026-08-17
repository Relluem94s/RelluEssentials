package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BagRegistryTest {

  private BagRegistry bagRegistry;

  @BeforeEach
  void setUp() {
    bagRegistry = new BagRegistry();
  }

  @Test
  void register_ShouldStoreBagEntry() {
    BagEntry entry = createEntry(1, 100, 1);
    bagRegistry.register(entry);

    assertTrue(bagRegistry.existsByPlayerId(100));
    assertEquals(1, bagRegistry.findAllByPlayerId(100).size());
  }

  @Test
  void registerAll_ShouldStoreMultipleBagEntries() {
    BagEntry entry1 = createEntry(1, 100, 1);
    BagEntry entry2 = createEntry(2, 100, 2);
    BagEntry entry3 = createEntry(3, 200, 1);

    bagRegistry.registerAll(List.of(entry1, entry2, entry3));

    assertEquals(2, bagRegistry.findAllByPlayerId(100).size());
    assertEquals(1, bagRegistry.findAllByPlayerId(200).size());
    assertEquals(3, bagRegistry.findAll().size());
  }

  @Test
  void unregister_ShouldRemoveSpecificEntry() {
    BagEntry entry1 = createEntry(1, 100, 1);
    BagEntry entry2 = createEntry(2, 100, 2);
    bagRegistry.registerAll(List.of(entry1, entry2));

    bagRegistry.unregister(entry1);

    assertEquals(1, bagRegistry.findAllByPlayerId(100).size());
    assertFalse(bagRegistry.findByPlayerIdAndBagTypeId(100, 1).isPresent());
  }

  @Test
  void findByPlayerIdAndBagTypeId_ShouldReturnCorrectEntry() {
    BagTypeEntry type1 = mock(BagTypeEntry.class);
    when(type1.getId()).thenReturn(1);

    BagEntry entry = createEntry(1, 100, 1);
    entry.setBagType(type1);

    bagRegistry.register(entry);

    Optional<BagEntry> found = bagRegistry.findByPlayerIdAndBagTypeId(100, 1);

    assertTrue(found.isPresent());
    assertEquals(entry, found.get());
  }

  @Test
  void findByPlayerIdAndBagTypeId_ShouldReturnEmptyIfNotFound() {
    bagRegistry.register(createEntry(1, 100, 1));

    Optional<BagEntry> found = bagRegistry.findByPlayerIdAndBagTypeId(100, 99);

    assertTrue(found.isEmpty());
  }

  @Test
  void existsByPlayerIdAndBagTypeId_ShouldReturnBoolean() {
    BagTypeEntry type1 = mock(BagTypeEntry.class);
    when(type1.getId()).thenReturn(1);

    BagEntry entry = createEntry(1, 100, 1);
    entry.setBagType(type1);
    bagRegistry.register(entry);

    assertTrue(bagRegistry.existsByPlayerIdAndBagTypeId(100, 1));
    assertFalse(bagRegistry.existsByPlayerIdAndBagTypeId(100, 2));
  }

  @Test
  void existsByPlayerId_ShouldReturnTrueIfPlayerHasBags() {
    bagRegistry.register(createEntry(1, 100, 1));

    assertTrue(bagRegistry.existsByPlayerId(100));
    assertFalse(bagRegistry.existsByPlayerId(999));
  }

  @Test
  void findAll_ShouldReturnAllEntries() {
    bagRegistry.register(createEntry(1, 100, 1));
    bagRegistry.register(createEntry(2, 200, 1));

    Collection<BagEntry> all = bagRegistry.findAll();

    assertEquals(2, all.size());
  }

  private BagEntry createEntry(int id, int playerId, int bagTypeId) {
    BagEntry entry = new BagEntry();
    entry.setId(id);
    entry.setPlayerId(playerId);
    entry.setBagTypeId(bagTypeId);
    return entry;
  }
}