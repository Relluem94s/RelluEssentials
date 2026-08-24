package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BagTypeRegistryTest {

  private BagTypeRegistry registry;
  private BagTypeEntry entry1;
  private BagTypeEntry entry2;

  @BeforeEach
  void setUp() {
    registry = new BagTypeRegistry();
    entry1 = new BagTypeEntry();
    entry1.setId(1);
    entry1.setName("leather_bag");
    entry1.setDisplayName("Leather Bag");

    entry2 = new BagTypeEntry();
    entry2.setId(2);
    entry2.setName("iron_bag");
    entry2.setDisplayName("Iron Bag");
  }

  @Test
  void register_ShouldAddEntry_WhenNotPresent() {
    registry.register(entry1);
    assertTrue(registry.contains(entry1));
  }

  @Test
  void register_ShouldThrowException_WhenAlreadyRegistered() {
    registry.register(entry1);
    assertThrows(IllegalArgumentException.class, () -> registry.register(entry1));
  }

  @Test
  void registerAll_ShouldAddMultipleEntries() {
    registry.registerAll(List.of(entry1, entry2));
    assertTrue(registry.contains(entry1));
    assertTrue(registry.contains(entry2));
  }

  @Test
  void unregister_ShouldRemoveEntry_WhenPresent() {
    registry.register(entry1);
    registry.unregister(entry1);
    assertFalse(registry.contains(entry1));
  }

  @Test
  void unregister_ShouldThrowException_WhenNotPresent() {
    assertThrows(IllegalArgumentException.class, () -> registry.unregister(entry1));
  }

  @Test
  void contains_ShouldReturnCorrectBoolean() {
    assertFalse(registry.contains(entry1));
    registry.register(entry1);
    assertTrue(registry.contains(entry1));
  }

  @Test
  void getAll_ShouldReturnUnmodifiableCopy() {
    registry.register(entry1);
    List<BagTypeEntry> all = registry.getAll();
    assertEquals(1, all.size());
    assertThrows(UnsupportedOperationException.class, () -> all.add(entry2));
  }

  @Test
  void findById_ShouldReturnEntry_WhenIdExists() {
    registry.register(entry1);
    Optional<BagTypeEntry> found = registry.findById(1);
    assertTrue(found.isPresent());
    assertEquals(entry1, found.get());
  }

  @Test
  void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
    registry.register(entry1);
    Optional<BagTypeEntry> found = registry.findById(99);
    assertTrue(found.isEmpty());
  }

  @Test
  void findByName_ShouldReturnEntry_WhenNameExists() {
    registry.register(entry1);
    Optional<BagTypeEntry> found = registry.findByName("leather_bag");
    assertTrue(found.isPresent());
    assertEquals(entry1, found.get());
  }

  @Test
  void findByName_ShouldReturnEmpty_WhenNameDoesNotExist() {
    registry.register(entry1);
    Optional<BagTypeEntry> found = registry.findByName("non_existent");
    assertTrue(found.isEmpty());
  }

  @Test
  void findByPartialName_ShouldCoverAllBranches() {
    registry.register(entry1);

    assertTrue(registry.findByPartialName("The Leather Bag is great").isPresent());
    assertTrue(registry.findByPartialName("my leather_bag").isPresent());
    assertTrue(registry.findByPartialName("Leather").isPresent());
    assertTrue(registry.findByPartialName("leather").isPresent());
    assertTrue(registry.findByPartialName("silk").isEmpty());
  }
}