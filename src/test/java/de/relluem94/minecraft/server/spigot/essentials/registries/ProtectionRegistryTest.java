package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProtectionRegistryTest {

  private World mockWorld;
  private Location location1;
  private Location location2;

  @BeforeEach
  void setUp() {
    mockWorld = Mockito.mock(World.class);
    location1 = new Location(mockWorld, 10, 64, 10);
    location2 = new Location(mockWorld, 20, 64, 20);
  }

  @Test
  void testConstructorAndIsProtectableMaterial() {
    ProtectionLockEntry lockEntry = new ProtectionLockEntry();
    lockEntry.setValue(Material.CHEST);

    ProtectionRegistry registry = new ProtectionRegistry(List.of(lockEntry), Map.of());

    assertTrue(registry.isProtectableMaterial(Material.CHEST));
    assertFalse(registry.isProtectableMaterial(Material.STONE));
  }

  @Test
  void testPutAndGetProtectionEntry() {
    ProtectionRegistry registry = new ProtectionRegistry(Collections.emptyList(), Collections.emptyMap());
    ProtectionEntry entry = new ProtectionEntry();
    entry.setId(1);

    registry.putProtectionEntry(location1, entry);

    assertEquals(entry, registry.getProtectionEntry(location1));
    Location sameCoords = new Location(mockWorld, 10, 64, 10, 0, 0);
    assertEquals(entry, registry.getProtectionEntry(sameCoords));
  }

  @Test
  void testRemoveProtectionEntry() {
    ProtectionEntry entry = new ProtectionEntry();
    ProtectionRegistry registry = new ProtectionRegistry(Collections.emptyList(), Map.of(location1, entry));

    registry.removeProtectionEntry(location1);

    assertNull(registry.getProtectionEntry(location1));
  }

  @Test
  void testGetProtectionEntriesOwnedBy() {
    long ownerId = 123L;
    long strangerId = 456L;

    ProtectionEntry entry1 = new ProtectionEntry();
    entry1.setCreatedBy((int) ownerId);
    entry1.setId(1);

    ProtectionEntry entry2 = new ProtectionEntry();
    entry2.setCreatedBy((int) strangerId);
    entry2.setId(2);

    ProtectionRegistry registry = new ProtectionRegistry(Collections.emptyList(), Map.of(
        location1, entry1,
        location2, entry2
    ));

    List<ProtectionEntry> ownedEntries = registry.getProtectionEntriesOwnedBy(ownerId);

    assertEquals(1, ownedEntries.size());
    assertEquals(1, ownedEntries.get(0).getId());
  }

  @Test
  void testGetProtectionEntryList() {
    ProtectionEntry entry = new ProtectionEntry();
    ProtectionRegistry registry = new ProtectionRegistry(Collections.emptyList(), Map.of(location1, entry));

    Map<Location, ProtectionEntry> allEntries = registry.getProtectionEntryList();

    assertEquals(1, allEntries.size());
    assertTrue(allEntries.containsValue(entry));
    assertThrows(UnsupportedOperationException.class, () -> allEntries.put(location2, new ProtectionEntry()));
  }

  @Test
  void testRemoveProtectionEntriesByIds() {
    ProtectionEntry entry1 = new ProtectionEntry();
    entry1.setId(10);

    ProtectionEntry entry2 = new ProtectionEntry();
    entry2.setId(20);

    ProtectionRegistry registry = new ProtectionRegistry(Collections.emptyList(), Map.of(
        location1, entry1,
        location2, entry2
    ));

    registry.removeProtectionEntriesByIds(List.of(10L));

    assertNull(registry.getProtectionEntry(location1));
    assertNotNull(registry.getProtectionEntry(location2));
  }
}