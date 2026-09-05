package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.LocationRepository;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

  @Mock
  private LocationRepository locationRepository;

  @Mock
  private LocationTypeService locationTypeService;

  @Mock
  private World world;

  @Mock
  private Server server;

  private LocationService locationService;

  private LocationTypeEntry buildLocationTypeEntry(int id, LocationType type) {
    LocationTypeEntry typeEntry = new LocationTypeEntry();
    typeEntry.setId(id);
    typeEntry.setType(type.name());
    return typeEntry;
  }

  private LocationEntry buildLocationEntry(Location location, String name, LocationTypeEntry typeEntry, int playerId) {
    LocationEntry entry = new LocationEntry();
    entry.setLocation(location);
    entry.setLocationName(name);
    entry.setLocationType(typeEntry);
    entry.setPlayerId(playerId);
    return entry;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    Field serverField = Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, server);

    lenient().when(server.getWorld(any(String.class))).thenReturn(world);
    lenient().when(world.getName()).thenReturn("world");

    locationService = new LocationService(locationRepository, locationTypeService);
  }

  @Test
  void removeOutdatedLocationsReturnsCountFromRepository() {
    when(locationRepository.removeOutdatedLocations()).thenReturn(3);

    int result = locationService.removeOutdatedLocations();

    assertEquals(3, result);
  }

  @Test
  void findByLocationAndTypeDelegatesToRepositoryWithResolvedTypeId() {
    Location location = new Location(world, 1, 2, 3);
    LocationTypeEntry typeEntry = buildLocationTypeEntry(5, LocationType.HOME);
    LocationEntry expectedEntry = buildLocationEntry(location, "home", typeEntry, 1);

    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(typeEntry));
    when(locationRepository.findByLocationAndType(location, 5)).thenReturn(expectedEntry);

    LocationEntry result = locationService.findByLocationAndType(location, LocationType.HOME);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(expectedEntry.getLocationName(), result.getLocationName()),
        () -> assertEquals(expectedEntry.getPlayerId(), result.getPlayerId()),
        () -> assertEquals(expectedEntry.getLocationType(), result.getLocationType()),
        () -> verify(locationRepository).findByLocationAndType(location, 5)
    );
  }

  @Test
  void findByLocationAndTypeThrowsWhenTypeNotFound() {
    Location location = new Location(world, 1, 2, 3);

    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.empty());

    IllegalStateException exception = assertThrows(IllegalStateException.class,
        () -> locationService.findByLocationAndType(location, LocationType.HOME));

    assertEquals(PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND, exception.getMessage());
  }

  @Test
  void findByIdDelegatesToRepository() {
    LocationTypeEntry typeEntry = buildLocationTypeEntry(1, LocationType.WARP);
    Location location = new Location(world, 1, 2, 3);
    LocationEntry expectedEntry = buildLocationEntry(location, "warp1", typeEntry, 2);

    when(locationRepository.findById(42)).thenReturn(expectedEntry);

    LocationEntry result = locationService.findById(42);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(expectedEntry.getLocationName(), result.getLocationName()),
        () -> assertEquals(expectedEntry.getPlayerId(), result.getPlayerId()),
        () -> verify(locationRepository).findById(42)
    );
  }

  @Test
  void findByIdReturnsNullWhenNotFound() {
    when(locationRepository.findById(99)).thenReturn(null);

    LocationEntry result = locationService.findById(99);

    assertNull(result);
  }

  @Test
  void saveDelegatesToRepository() {
    Location location = new Location(world, 1, 2, 3);
    LocationTypeEntry typeEntry = buildLocationTypeEntry(1, LocationType.HOME);
    LocationEntry entry = buildLocationEntry(location, "home", typeEntry, 1);

    locationService.save(entry);

    verify(locationRepository).save(entry);
  }

  @Test
  void saveAndFetchReturnsPersistentEntryWhenFoundAfterSave() {
    Location location = new Location(world, 1, 2, 3);
    LocationTypeEntry typeEntry = buildLocationTypeEntry(2, LocationType.DEATH);
    LocationEntry entryToSave = buildLocationEntry(location, "death", typeEntry, 3);
    LocationEntry persistedEntry = buildLocationEntry(location, "death", typeEntry, 3);
    persistedEntry.setId(10);

    when(locationRepository.findByLocationAndType(location, 2)).thenReturn(persistedEntry);

    LocationEntry result = locationService.saveAndFetch(entryToSave);

    assertAll(
        () -> verify(locationRepository).save(entryToSave),
        () -> verify(locationRepository).findByLocationAndType(location, 2),
        () -> assertEquals(10, result.getId()),
        () -> assertEquals(persistedEntry.getLocationName(), result.getLocationName()),
        () -> assertEquals(persistedEntry.getPlayerId(), result.getPlayerId()),
        () -> assertEquals(persistedEntry.getLocationType(), result.getLocationType())
    );
  }

  @Test
  void saveAndFetchReturnsOriginalEntryWhenRepositoryReturnsNull() {
    Location location = new Location(world, 1, 2, 3);
    LocationTypeEntry typeEntry = buildLocationTypeEntry(2, LocationType.DEATH);
    LocationEntry entryToSave = buildLocationEntry(location, "death", typeEntry, 3);

    when(locationRepository.findByLocationAndType(location, 2)).thenReturn(null);

    LocationEntry result = locationService.saveAndFetch(entryToSave);

    assertAll(
        () -> verify(locationRepository).save(entryToSave),
        () -> assertEquals(entryToSave, result)
    );
  }

  @Test
  void deleteDelegatesToRepository() {
    Location location = new Location(world, 1, 2, 3);
    LocationTypeEntry typeEntry = buildLocationTypeEntry(1, LocationType.HOME);
    LocationEntry entry = buildLocationEntry(location, "home", typeEntry, 1);

    locationService.delete(entry);

    verify(locationRepository).delete(entry);
  }

  @Test
  void deleteByIdDelegatesToRepository() {
    locationService.deleteById(5, 10);

    verify(locationRepository).deleteById(5, 10);
  }

  @Test
  void findByPlayerAndTypeDelegatesToRepositoryWithResolvedTypeId() {
    LocationTypeEntry typeEntry = buildLocationTypeEntry(3, LocationType.WARP);
    Location location = new Location(world, 1, 2, 3);
    LocationEntry entry = buildLocationEntry(location, "warp", typeEntry, 7);

    when(locationTypeService.findByName(LocationType.WARP)).thenReturn(Optional.of(typeEntry));
    when(locationRepository.findByPlayerAndType(7, 3)).thenReturn(List.of(entry));

    List<LocationEntry> result = locationService.findByPlayerAndType(7, LocationType.WARP);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(entry.getLocationName(), result.getFirst().getLocationName()),
        () -> assertEquals(entry.getPlayerId(), result.getFirst().getPlayerId()),
        () -> verify(locationRepository).findByPlayerAndType(7, 3)
    );
  }

  @Test
  void findByPlayerAndTypeThrowsWhenTypeNotFound() {
    when(locationTypeService.findByName(LocationType.WARP)).thenReturn(Optional.empty());

    IllegalStateException exception = assertThrows(IllegalStateException.class,
        () -> locationService.findByPlayerAndType(7, LocationType.WARP));

    assertEquals(PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND, exception.getMessage());
  }

  @Test
  void resolveTypeReturnsTypeEntryWhenFound() {
    LocationTypeEntry typeEntry = buildLocationTypeEntry(4, LocationType.PROTECTION);

    when(locationTypeService.findByName(LocationType.PROTECTION)).thenReturn(Optional.of(typeEntry));

    LocationTypeEntry result = locationService.resolveType(LocationType.PROTECTION);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(4, result.getId()),
        () -> assertEquals(LocationType.PROTECTION.name(), result.getType())
    );
  }

  @Test
  void resolveTypeThrowsWhenTypeNotFound() {
    when(locationTypeService.findByName(LocationType.PROTECTION)).thenReturn(Optional.empty());

    IllegalStateException exception = assertThrows(IllegalStateException.class,
        () -> locationService.resolveType(LocationType.PROTECTION));

    assertEquals(PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND, exception.getMessage());
  }

  @Test
  void buildLocationEntryFromLocationSetsAllFields() {
    Location location = new Location(world, 10, 20, 30, 45f, 10f);
    LocationTypeEntry typeEntry = buildLocationTypeEntry(1, LocationType.HOME);

    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(typeEntry));

    LocationEntry result = locationService.buildLocationEntry(location, "myHome", LocationType.HOME, 5);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("myHome", result.getLocationName()),
        () -> assertEquals(5, result.getPlayerId()),
        () -> assertEquals(typeEntry, result.getLocationType()),
        () -> assertEquals(10, result.getX()),
        () -> assertEquals(20, result.getY()),
        () -> assertEquals(30, result.getZ()),
        () -> assertEquals(45f, result.getYaw()),
        () -> assertEquals(10f, result.getPitch()),
        () -> assertEquals("world", result.getWorld())
    );
  }

  @Test
  void buildLocationEntryFromLocationThrowsWhenTypeNotFound() {
    Location location = new Location(world, 1, 2, 3);

    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class,
        () -> locationService.buildLocationEntry(location, "home", LocationType.HOME, 1));
  }

  @Test
  void buildLocationEntryFromPlayerSetsAllFieldsUsingPlayerLocation() {
    Location location = new Location(world, 5, 10, 15, 90f, 5f);
    LocationTypeEntry typeEntry = buildLocationTypeEntry(1, LocationType.HOME);

    Player player = mock(Player.class);
    when(player.getLocation()).thenReturn(location);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(typeEntry));

    LocationEntry result = locationService.buildLocationEntry(player, "playerHome", LocationType.HOME, 8);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("playerHome", result.getLocationName()),
        () -> assertEquals(8, result.getPlayerId()),
        () -> assertEquals(typeEntry, result.getLocationType()),
        () -> assertEquals(5, result.getX()),
        () -> assertEquals(10, result.getY()),
        () -> assertEquals(15, result.getZ()),
        () -> assertEquals(90f, result.getYaw()),
        () -> assertEquals(5f, result.getPitch()),
        () -> assertEquals("world", result.getWorld())
    );
  }

  @Test
  void buildLocationEntryFromPlayerThrowsWhenTypeNotFound() {
    Location location = new Location(world, 1, 2, 3);
    Player player = mock(Player.class);
    when(player.getLocation()).thenReturn(location);

    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class,
        () -> locationService.buildLocationEntry(player, "home", LocationType.HOME, 1));
  }

  @Test
  void findByTypeDelegatesToRepositoryWithResolvedTypeId() {
    LocationTypeEntry typeEntry = buildLocationTypeEntry(6, LocationType.BLOCK_HISTORY);
    Location location = new Location(world, 1, 2, 3);
    LocationEntry entry = buildLocationEntry(location, "block", typeEntry, 4);

    when(locationTypeService.findByName(LocationType.BLOCK_HISTORY)).thenReturn(Optional.of(typeEntry));
    when(locationRepository.findByType(6)).thenReturn(List.of(entry));

    List<LocationEntry> result = locationService.findByType(LocationType.BLOCK_HISTORY);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(entry.getLocationName(), result.getFirst().getLocationName()),
        () -> assertEquals(entry.getPlayerId(), result.getFirst().getPlayerId()),
        () -> verify(locationRepository).findByType(6)
    );
  }

  @Test
  void findByTypeThrowsWhenTypeNotFound() {
    when(locationTypeService.findByName(LocationType.BLOCK_HISTORY)).thenReturn(Optional.empty());

    IllegalStateException exception = assertThrows(IllegalStateException.class,
        () -> locationService.findByType(LocationType.BLOCK_HISTORY));

    assertEquals(PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND, exception.getMessage());
  }
}