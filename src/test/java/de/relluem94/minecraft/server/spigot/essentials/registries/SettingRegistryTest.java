package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettingRegistryTest {

  private SettingRegistry settingRegistry;

  @BeforeEach
  void setUp() {
    settingRegistry = new SettingRegistry();
  }

  @Test
  void loadAllShouldPopulateRegistry() {
    SettingEntry entry1 = new SettingEntry(1, LocalDateTime.now(), 100, LocalDateTime.now(), 100, "setting.one");
    SettingEntry entry2 = new SettingEntry(2, LocalDateTime.now(), 101, LocalDateTime.now(), 101, "setting.two");

    settingRegistry.loadAll(List.of(entry1, entry2));

    assertEquals(2, settingRegistry.getAll().size());
    assertTrue(settingRegistry.findById(1).isPresent());
    assertTrue(settingRegistry.findByName("setting.one").isPresent());
  }

  @Test
  void loadAllShouldClearPreviousSettings() {
    SettingEntry entry1 = new SettingEntry(1, LocalDateTime.now(), 100, LocalDateTime.now(), 100, "setting.one");
    settingRegistry.loadAll(List.of(entry1));

    SettingEntry entry2 = new SettingEntry(2, LocalDateTime.now(), 101, LocalDateTime.now(), 101, "setting.two");
    settingRegistry.loadAll(List.of(entry2));

    assertEquals(1, settingRegistry.getAll().size());
    assertFalse(settingRegistry.findById(1).isPresent());
    assertTrue(settingRegistry.findById(2).isPresent());
  }

  @Test
  void findByIdShouldReturnEmptyWhenNotFound() {
    settingRegistry.loadAll(List.of(new SettingEntry(1, LocalDateTime.now(), 1, LocalDateTime.now(), 1, "name")));

    Optional<SettingEntry> result = settingRegistry.findById(999);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByNameShouldReturnEmptyWhenNotFound() {
    settingRegistry.loadAll(List.of(new SettingEntry(1, LocalDateTime.now(), 1, LocalDateTime.now(), 1, "name")));

    Optional<SettingEntry> result = settingRegistry.findByName("non.existent");

    assertTrue(result.isEmpty());
  }

  @Test
  void findByIdShouldReturnCorrectEntry() {
    SettingEntry entry = new SettingEntry(42, LocalDateTime.now(), 1, LocalDateTime.now(), 1, "target");
    settingRegistry.loadAll(List.of(entry));

    Optional<SettingEntry> result = settingRegistry.findById(42);

    assertTrue(result.isPresent());
    assertEquals("target", result.get().getName());
  }

  @Test
  void findByNameShouldReturnCorrectEntry() {
    SettingEntry entry = new SettingEntry(42, LocalDateTime.now(), 1, LocalDateTime.now(), 1, "target");
    settingRegistry.loadAll(List.of(entry));

    Optional<SettingEntry> result = settingRegistry.findByName("target");

    assertTrue(result.isPresent());
    assertEquals(42, result.get().getId());
  }

  @Test
  void getAllShouldReturnUnmodifiableList() {
    SettingEntry entry = new SettingEntry(1, LocalDateTime.now(), 1, LocalDateTime.now(), 1, "name");
    settingRegistry.loadAll(List.of(entry));

    List<SettingEntry> allSettings = settingRegistry.getAll();

    assertEquals(1, allSettings.size());
    assertThrows(UnsupportedOperationException.class, () -> allSettings.add(entry));
  }
}