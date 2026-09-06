package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingServiceTest {

  @Mock
  private SettingRegistry settingRegistry;

  @Mock
  private SettingRepository settingRepository;

  @InjectMocks
  private SettingService settingService;

  @Test
  void loadAllFetchesFromRepositoryAndLoadsIntoRegistry() {
    List<SettingEntry> settings = List.of(mock(SettingEntry.class), mock(SettingEntry.class));
    when(settingRepository.findAll()).thenReturn(settings);

    settingService.loadAll();

    verify(settingRepository).findAll();
    verify(settingRegistry).loadAll(settings);
  }

  @Test
  void loadAllPropagatesRepositoryException() {
    when(settingRepository.findAll()).thenThrow(new RuntimeException("repository failure"));

    assertThrows(RuntimeException.class, () -> settingService.loadAll());
  }

  @Test
  void loadAllPropagatesRegistryException() {
    List<SettingEntry> settings = List.of(mock(SettingEntry.class));
    when(settingRepository.findAll()).thenReturn(settings);
    doThrow(new RuntimeException("registry failure")).when(settingRegistry).loadAll(settings);

    assertThrows(RuntimeException.class, () -> settingService.loadAll());
  }

  @Test
  void findByIdReturnsSettingWhenFound() {
    SettingEntry settingEntry = mock(SettingEntry.class);
    when(settingRegistry.findById(1)).thenReturn(Optional.of(settingEntry));

    Optional<SettingEntry> result = settingService.findById(1);

    assertTrue(result.isPresent());
    assertEquals(settingEntry, result.get());
  }

  @Test
  void findByIdReturnsEmptyWhenNotFound() {
    when(settingRegistry.findById(99)).thenReturn(Optional.empty());

    Optional<SettingEntry> result = settingService.findById(99);

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void findByIdPropagatesRegistryException() {
    when(settingRegistry.findById(anyInt())).thenThrow(new RuntimeException("registry failure"));

    assertThrows(RuntimeException.class, () -> settingService.findById(1));
  }

  @Test
  void findByNameReturnsSettingWhenFound() {
    SettingEntry settingEntry = mock(SettingEntry.class);
    when(settingRegistry.findByName("test-setting")).thenReturn(Optional.of(settingEntry));

    Optional<SettingEntry> result = settingService.findByName("test-setting");

    assertTrue(result.isPresent());
    assertEquals(settingEntry, result.get());
  }

  @Test
  void findByNameReturnsEmptyWhenNotFound() {
    when(settingRegistry.findByName("unknown")).thenReturn(Optional.empty());

    Optional<SettingEntry> result = settingService.findByName("unknown");

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void findByNamePropagatesRegistryException() {
    when(settingRegistry.findByName(anyString())).thenThrow(new RuntimeException("registry failure"));

    assertThrows(RuntimeException.class, () -> settingService.findByName("test-setting"));
  }

  @Test
  void getAllReturnsAllSettingsFromRegistry() {
    List<SettingEntry> settings = List.of(mock(SettingEntry.class), mock(SettingEntry.class));
    when(settingRegistry.getAll()).thenReturn(settings);

    List<SettingEntry> result = settingService.getAll();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(2, result.size()),
        () -> assertEquals(settings, result)
    );
  }

  @Test
  void getAllReturnsEmptyListWhenNoSettingsLoaded() {
    when(settingRegistry.getAll()).thenReturn(List.of());

    List<SettingEntry> result = settingService.getAll();

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void getAllPropagatesRegistryException() {
    when(settingRegistry.getAll()).thenThrow(new RuntimeException("registry failure"));

    assertThrows(RuntimeException.class, () -> settingService.getAll());
  }
}