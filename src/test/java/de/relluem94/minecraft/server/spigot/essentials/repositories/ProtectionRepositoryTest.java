package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.ProtectionDao;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProtectionRepositoryTest {

  @Mock
  private ProtectionDao protectionDao;

  @Mock
  private LocationDao locationDao;

  @Mock
  private ProtectionEntry protectionEntry;

  @Mock
  private LocationEntry locationEntry;

  @Mock
  private Location location;

  @Mock
  private ProtectionLockEntry protectionLockEntry;

  private ProtectionRepository protectionRepository;

  @BeforeEach
  void setUp() {
    protectionRepository = new ProtectionRepository(protectionDao, locationDao);
  }

  @Test
  void removeOutdatedProtectionsReturnsOutdatedIds() {
    List<Long> expectedIds = List.of(1L, 2L, 3L);
    when(protectionDao.findOutdatedProtectionIds()).thenReturn(expectedIds);

    List<Long> result = protectionRepository.removeOutdatedProtections();

    assertEquals(expectedIds, result);
    verify(protectionDao).findOutdatedProtectionIds();
    verify(protectionDao).deleteOutdatedProtections();
  }

  @Test
  void removeOutdatedProtectionsReturnsEmptyListWhenNoneOutdated() {
    when(protectionDao.findOutdatedProtectionIds()).thenReturn(List.of());

    List<Long> result = protectionRepository.removeOutdatedProtections();

    assertTrue(result.isEmpty());
    verify(protectionDao).deleteOutdatedProtections();
  }

  @Test
  void removeOutdatedProtectionsPropagatesDaoExceptionOnFind() {
    when(protectionDao.findOutdatedProtectionIds()).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> protectionRepository.removeOutdatedProtections());
  }

  @Test
  void removeOutdatedProtectionsPropagatesDaoExceptionOnDelete() {
    when(protectionDao.findOutdatedProtectionIds()).thenReturn(List.of(1L));
    doThrow(new RuntimeException("DB error")).when(protectionDao).deleteOutdatedProtections();

    assertThrows(RuntimeException.class, () -> protectionRepository.removeOutdatedProtections());
  }

  @Test
  void loadAllReturnsMappedProtectionsByLocation() {
    int locationFk = 10;
    when(protectionDao.findAll()).thenReturn(List.of(protectionEntry));
    when(protectionEntry.getLocationFk()).thenReturn(locationFk);
    when(locationDao.findById(locationFk)).thenReturn(locationEntry);
    when(locationEntry.getLocation()).thenReturn(location);

    Map<Location, ProtectionEntry> result = protectionRepository.loadAll();

    assertEquals(1, result.size());
    assertTrue(result.containsKey(location));
    assertEquals(protectionEntry, result.get(location));
    verify(protectionEntry).setLocationEntry(locationEntry);
  }

  @Test
  void loadAllSkipsEntriesWithMissingLocationEntry() {
    Logger logger = Logger.getLogger("de.relluem94.minecraft.server.spigot.essentials.repositories.ProtectionRepository");
    logger.setUseParentHandlers(false);
    logger.setLevel(Level.OFF);

    int locationFk = 10;
    when(protectionDao.findAll()).thenReturn(List.of(protectionEntry));
    when(protectionEntry.getLocationFk()).thenReturn(locationFk);
    when(locationDao.findById(locationFk)).thenReturn(null);

    Map<Location, ProtectionEntry> result = protectionRepository.loadAll();

    assertTrue(result.isEmpty());
    verify(protectionEntry, never()).setLocationEntry(locationEntry);
  }

  @Test
  void loadAllReturnsEmptyMapWhenNoProtectionsExist() {
    when(protectionDao.findAll()).thenReturn(List.of());

    Map<Location, ProtectionEntry> result = protectionRepository.loadAll();

    assertTrue(result.isEmpty());
  }

  @Test
  void loadAllPropagatesDaoException() {
    when(protectionDao.findAll()).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> protectionRepository.loadAll());
  }

  @Test
  void removeDelegatesDeletionToProtectionDaoAndLocationDao() {
    int protectionId = 1;
    int locationId = 10;
    int playerId = 3;
    when(protectionEntry.getId()).thenReturn(protectionId);
    when(protectionEntry.getLocationEntry()).thenReturn(locationEntry);
    when(locationEntry.getPlayerId()).thenReturn(playerId);
    when(locationEntry.getId()).thenReturn(locationId);

    protectionRepository.remove(protectionEntry);

    verify(protectionDao).deleteById(protectionId, playerId);
    verify(locationDao).deleteById(locationId, playerId);
  }

  @Test
  void removePropagatesDaoExceptionOnProtectionDelete() {
    int protectionId = 1;
    int playerId = 3;
    when(protectionEntry.getId()).thenReturn(protectionId);
    when(protectionEntry.getLocationEntry()).thenReturn(locationEntry);
    when(locationEntry.getPlayerId()).thenReturn(playerId);
    doThrow(new RuntimeException("DB error")).when(protectionDao).deleteById(protectionId, playerId);

    assertThrows(RuntimeException.class, () -> protectionRepository.remove(protectionEntry));
  }

  @Test
  void removePropagatesDaoExceptionOnLocationDelete() {
    int protectionId = 1;
    int locationId = 2;
    int playerId = 3;
    when(protectionEntry.getId()).thenReturn(protectionId);
    when(protectionEntry.getLocationEntry()).thenReturn(locationEntry);
    when(locationEntry.getPlayerId()).thenReturn(playerId);
    when(locationEntry.getId()).thenReturn(locationId);
    doThrow(new RuntimeException("DB error")).when(locationDao).deleteById(locationId, playerId);

    assertThrows(RuntimeException.class, () -> protectionRepository.remove(protectionEntry));
  }

  @Test
  void saveDelegatesInsertToProtectionDao() {
    protectionRepository.save(protectionEntry);

    verify(protectionDao).insertProtection(protectionEntry);
  }

  @Test
  void savePropagatesDaoException() {
    doThrow(new RuntimeException("DB error")).when(protectionDao).insertProtection(protectionEntry);

    assertThrows(RuntimeException.class, () -> protectionRepository.save(protectionEntry));
  }

  @Test
  void updateFlagsDelegatesUpdateToProtectionDao() {
    protectionRepository.updateFlags(protectionEntry);

    verify(protectionDao).updateProtectionFlag(protectionEntry);
  }

  @Test
  void updateFlagsPropagatesDaoException() {
    doThrow(new RuntimeException("DB error")).when(protectionDao).updateProtectionFlag(protectionEntry);

    assertThrows(RuntimeException.class, () -> protectionRepository.updateFlags(protectionEntry));
  }

  @Test
  void updateRightsDelegatesUpdateToProtectionDao() {
    protectionRepository.updateRights(protectionEntry);

    verify(protectionDao).updateProtectionRight(protectionEntry);
  }

  @Test
  void updateRightsPropagatesDaoException() {
    doThrow(new RuntimeException("DB error")).when(protectionDao).updateProtectionRight(protectionEntry);

    assertThrows(RuntimeException.class, () -> protectionRepository.updateRights(protectionEntry));
  }

  @Test
  void findByLocationReturnsEnrichedProtectionEntry() {
    int locationFk = 10;
    when(protectionDao.getProtectionByLocation(location)).thenReturn(protectionEntry);
    when(protectionEntry.getLocationFk()).thenReturn(locationFk);
    when(locationDao.findById(locationFk)).thenReturn(locationEntry);

    ProtectionEntry result = protectionRepository.findByLocation(location);

    assertNotNull(result);
    assertEquals(protectionEntry, result);
    verify(protectionEntry).setLocationEntry(locationEntry);
  }

  @Test
  void findByLocationReturnsProtectionEntryWithoutLocationEntryWhenLocationEntryNotFound() {
    int locationFk = 10;
    when(protectionDao.getProtectionByLocation(location)).thenReturn(protectionEntry);
    when(protectionEntry.getLocationFk()).thenReturn(locationFk);
    when(locationDao.findById(locationFk)).thenReturn(null);

    ProtectionEntry result = protectionRepository.findByLocation(location);

    assertNotNull(result);
    assertEquals(protectionEntry, result);
    verify(protectionEntry, never()).setLocationEntry(locationEntry);
  }

  @Test
  void findByLocationReturnsNullWhenNoProtectionExists() {
    when(protectionDao.getProtectionByLocation(location)).thenReturn(null);

    ProtectionEntry result = protectionRepository.findByLocation(location);

    assertNull(result);
    verify(locationDao, never()).findById(anyInt());
  }

  @Test
  void findByLocationPropagatesDaoException() {
    when(protectionDao.getProtectionByLocation(location)).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> protectionRepository.findByLocation(location));
  }

  @Test
  void loadAllLocksReturnsListOfProtectionLockEntries() {
    List<ProtectionLockEntry> expectedLocks = List.of(protectionLockEntry);
    when(protectionDao.findAllLocks()).thenReturn(expectedLocks);

    List<ProtectionLockEntry> result = protectionRepository.loadAllLocks();

    assertEquals(expectedLocks, result);
    verify(protectionDao).findAllLocks();
  }

  @Test
  void loadAllLocksReturnsEmptyListWhenNoLocksExist() {
    when(protectionDao.findAllLocks()).thenReturn(List.of());

    List<ProtectionLockEntry> result = protectionRepository.loadAllLocks();

    assertTrue(result.isEmpty());
    verify(protectionDao).findAllLocks();
  }

  @Test
  void loadAllLocksPropagatesDaoException() {
    when(protectionDao.findAllLocks()).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> protectionRepository.loadAllLocks());
  }
}