package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import java.util.List;
import org.bukkit.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationRepositoryTest {

  @Mock
  private LocationDao locationDao;

  @Mock
  private Location location;

  @Mock
  private LocationEntry locationEntry;

  private LocationRepository locationRepository;

  @BeforeEach
  void setUp() {
    locationRepository = new LocationRepository(locationDao);
  }

  @Test
  void removeOutdatedLocationsReturnsDeletedCount() {
    when(locationDao.deleteOutdatedLocations()).thenReturn(5);

    int result = locationRepository.removeOutdatedLocations();

    assertEquals(5, result);
    verify(locationDao).deleteOutdatedLocations();
  }

  @Test
  void removeOutdatedLocationsReturnsZeroWhenNoneDeleted() {
    when(locationDao.deleteOutdatedLocations()).thenReturn(0);

    int result = locationRepository.removeOutdatedLocations();

    assertEquals(0, result);
  }

  @Test
  void removeOutdatedLocationsPropagatesException() {
    when(locationDao.deleteOutdatedLocations()).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationRepository.removeOutdatedLocations());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 5})
  void findByLocationAndTypeReturnsEntry(int typeId) {
    when(locationDao.getLocation(location, typeId)).thenReturn(locationEntry);

    LocationEntry result = locationRepository.findByLocationAndType(location, typeId);

    assertEquals(locationEntry, result);
    verify(locationDao).getLocation(location, typeId);
  }

  @Test
  void findByLocationAndTypeReturnsNullWhenNotFound() {
    when(locationDao.getLocation(location, 1)).thenReturn(null);

    LocationEntry result = locationRepository.findByLocationAndType(location, 1);

    assertNull(result);
  }

  @Test
  void findByLocationAndTypePropagatesException() {
    when(locationDao.getLocation(location, 1)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationRepository.findByLocationAndType(location, 1));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 42, 100})
  void findByIdReturnsEntry(int id) {
    when(locationDao.findById(id)).thenReturn(locationEntry);

    LocationEntry result = locationRepository.findById(id);

    assertEquals(locationEntry, result);
    verify(locationDao).findById(id);
  }

  @Test
  void findByIdReturnsNullWhenNotFound() {
    when(locationDao.findById(99)).thenReturn(null);

    LocationEntry result = locationRepository.findById(99);

    assertNull(result);
  }

  @Test
  void findByIdPropagatesException() {
    when(locationDao.findById(1)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationRepository.findById(1));
  }

  @Test
  void saveDelegatesToDao() {
    locationRepository.save(locationEntry);

    verify(locationDao).insertLocation(locationEntry);
  }

  @Test
  void savePropagatesException() {
    doThrow(new RuntimeException("db error")).when(locationDao).insertLocation(locationEntry);

    assertThrows(RuntimeException.class, () -> locationRepository.save(locationEntry));
  }

  @Test
  void deleteDelegatesToDao() {
    locationRepository.delete(locationEntry);

    verify(locationDao).deleteLocation(locationEntry);
  }

  @Test
  void deletePropagatesException() {
    doThrow(new RuntimeException("db error")).when(locationDao).deleteLocation(locationEntry);

    assertThrows(RuntimeException.class, () -> locationRepository.delete(locationEntry));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 10, 50})
  void deleteByIdDelegatesToDao(int id) {
    locationRepository.deleteById(id, 7);

    verify(locationDao).deleteById(id, 7);
  }

  @Test
  void deleteByIdPropagatesException() {
    doThrow(new RuntimeException("db error")).when(locationDao).deleteById(1, 7);

    assertThrows(RuntimeException.class, () -> locationRepository.deleteById(1, 7));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void findByPlayerAndTypeReturnsEntries(int typeId) {
    List<LocationEntry> entries = List.of(locationEntry);
    when(locationDao.getLocations(10, typeId)).thenReturn(entries);

    List<LocationEntry> result = locationRepository.findByPlayerAndType(10, typeId);

    assertEquals(entries, result);
    verify(locationDao).getLocations(10, typeId);
  }

  @Test
  void findByPlayerAndTypeReturnsEmptyList() {
    when(locationDao.getLocations(10, 1)).thenReturn(List.of());

    List<LocationEntry> result = locationRepository.findByPlayerAndType(10, 1);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByPlayerAndTypePropagatesException() {
    when(locationDao.getLocations(10, 1)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationRepository.findByPlayerAndType(10, 1));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void findByTypeReturnsEntries(int typeId) {
    List<LocationEntry> entries = List.of(locationEntry);
    when(locationDao.getLocationsByType(typeId)).thenReturn(entries);

    List<LocationEntry> result = locationRepository.findByType(typeId);

    assertEquals(entries, result);
    verify(locationDao).getLocationsByType(typeId);
  }

  @Test
  void findByTypeReturnsEmptyList() {
    when(locationDao.getLocationsByType(1)).thenReturn(List.of());

    List<LocationEntry> result = locationRepository.findByType(1);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByTypePropagatesException() {
    when(locationDao.getLocationsByType(1)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationRepository.findByType(1));
  }
}