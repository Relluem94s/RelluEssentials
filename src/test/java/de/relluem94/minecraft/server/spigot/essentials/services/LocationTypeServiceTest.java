package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.LocationTypeRegistry;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationTypeServiceTest {

  @Mock
  private LocationTypeRegistry locationTypeRegistry;

  @Mock
  private LocationTypeEntry locationTypeEntryFirst;

  @Mock
  private LocationTypeEntry locationTypeEntrySecond;

  private LocationTypeService locationTypeService;

  @BeforeEach
  void setUp() {
    locationTypeService = new LocationTypeService(locationTypeRegistry);
  }

  @Test
  void findByIdReturnsMatchingEntry() {
    when(locationTypeEntryFirst.getId()).thenReturn(1);
    when(locationTypeRegistry.getAll()).thenReturn(List.of(locationTypeEntryFirst, locationTypeEntrySecond));

    Optional<LocationTypeEntry> result = locationTypeService.findById(1);

    assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(locationTypeEntryFirst, result.get())
    );
  }

  @Test
  void findByIdReturnsEmptyWhenNoEntryMatchesId() {
    when(locationTypeEntryFirst.getId()).thenReturn(1);
    when(locationTypeRegistry.getAll()).thenReturn(List.of(locationTypeEntryFirst));

    Optional<LocationTypeEntry> result = locationTypeService.findById(99);

    assertAll(
        () -> assertFalse(result.isPresent())
    );
  }

  @Test
  void findByIdReturnsEmptyWhenRegistryIsEmpty() {
    when(locationTypeRegistry.getAll()).thenReturn(List.of());

    Optional<LocationTypeEntry> result = locationTypeService.findById(1);

    assertAll(
        () -> assertFalse(result.isPresent())
    );
  }

  @Test
  void findByNameReturnsMatchingEntry() {
    when(locationTypeEntryFirst.getType()).thenReturn(LocationType.DEATH.name());
    when(locationTypeRegistry.getAll()).thenReturn(List.of(locationTypeEntryFirst, locationTypeEntrySecond));

    Optional<LocationTypeEntry> result = locationTypeService.findByName(LocationType.DEATH);

    assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(locationTypeEntryFirst, result.get())
    );
  }

  @Test
  void findByNameReturnsMatchingEntryIgnoringCase() {
    when(locationTypeEntryFirst.getType()).thenReturn(LocationType.HOME.name().toLowerCase());
    when(locationTypeRegistry.getAll()).thenReturn(List.of(locationTypeEntryFirst));

    Optional<LocationTypeEntry> result = locationTypeService.findByName(LocationType.HOME);

    assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(locationTypeEntryFirst, result.get())
    );
  }

  @Test
  void findByNameReturnsEmptyWhenNoEntryMatchesType() {
    when(locationTypeEntryFirst.getType()).thenReturn(LocationType.HOME.name());
    when(locationTypeRegistry.getAll()).thenReturn(List.of(locationTypeEntryFirst));

    Optional<LocationTypeEntry> result = locationTypeService.findByName(LocationType.DEATH);

    assertAll(
        () -> assertFalse(result.isPresent())
    );
  }

  @Test
  void findByNameReturnsEmptyWhenRegistryIsEmpty() {
    when(locationTypeRegistry.getAll()).thenReturn(List.of());

    Optional<LocationTypeEntry> result = locationTypeService.findByName(LocationType.HOME);

    assertAll(
        () -> assertFalse(result.isPresent())
    );
  }
}