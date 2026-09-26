package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.SettingPlayerDao;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingPlayerRepositoryTest {

  @Mock
  private SettingPlayerDao settingPlayerDao;

  @Mock
  private SettingPlayerEntry settingPlayerEntry;

  private SettingPlayerRepository settingPlayerRepository;

  @BeforeEach
  void setUp() {
    settingPlayerRepository = new SettingPlayerRepository(settingPlayerDao);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 42, 100})
  void findAllByPlayerIdReturnsDaoResult(int playerId) {
    List<SettingPlayerEntry> expectedEntries = List.of(settingPlayerEntry);
    when(settingPlayerDao.findAllByPlayerId(playerId)).thenReturn(expectedEntries);

    List<SettingPlayerEntry> result = settingPlayerRepository.findAllByPlayerId(playerId);

    assertEquals(expectedEntries, result);
    verify(settingPlayerDao).findAllByPlayerId(playerId);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 42, 100})
  void findAllByPlayerIdReturnsEmptyListWhenNoEntriesExist(int playerId) {
    when(settingPlayerDao.findAllByPlayerId(playerId)).thenReturn(List.of());

    List<SettingPlayerEntry> result = settingPlayerRepository.findAllByPlayerId(playerId);

    assertTrue(result.isEmpty());
    verify(settingPlayerDao).findAllByPlayerId(playerId);
  }

  @Test
  void findAllByPlayerIdPropagatesDaoException() {
    when(settingPlayerDao.findAllByPlayerId(anyInt())).thenThrow(new RuntimeException("dao error"));

    assertThrows(RuntimeException.class, () -> settingPlayerRepository.findAllByPlayerId(1));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 42, 100})
  void findByIdReturnsPresentOptionalWhenEntryExists(int id) {
    when(settingPlayerDao.findById(id)).thenReturn(Optional.of(settingPlayerEntry));

    Optional<SettingPlayerEntry> result = settingPlayerRepository.findById(id);

    assertTrue(result.isPresent());
    assertEquals(settingPlayerEntry, result.get());
    verify(settingPlayerDao).findById(id);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 42, 100})
  void findByIdReturnsEmptyOptionalWhenEntryDoesNotExist(int id) {
    when(settingPlayerDao.findById(id)).thenReturn(Optional.empty());

    Optional<SettingPlayerEntry> result = settingPlayerRepository.findById(id);

    assertTrue(result.isEmpty());
    verify(settingPlayerDao).findById(id);
  }

  @Test
  void findByIdPropagatesDaoException() {
    when(settingPlayerDao.findById(anyInt())).thenThrow(new RuntimeException("dao error"));

    assertThrows(RuntimeException.class, () -> settingPlayerRepository.findById(1));
  }

  @Test
  void insertDelegatesToDao() {
    settingPlayerRepository.insert(settingPlayerEntry);

    verify(settingPlayerDao).insert(settingPlayerEntry);
  }

  @Test
  void insertPropagatesDaoException() {
    doThrow(new RuntimeException("dao error")).when(settingPlayerDao).insert(any());

    assertThrows(RuntimeException.class, () -> settingPlayerRepository.insert(settingPlayerEntry));
  }

  @Test
  void updateDelegatesToDao() {
    settingPlayerRepository.update(settingPlayerEntry);

    verify(settingPlayerDao).update(settingPlayerEntry);
  }

  @Test
  void updatePropagatesDaoException() {
    doThrow(new RuntimeException("dao error")).when(settingPlayerDao).update(any());

    assertThrows(RuntimeException.class, () -> settingPlayerRepository.update(settingPlayerEntry));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 42, 100})
  void softDeleteDelegatesToDaoWithCorrectArguments(int id) {
    int deletedBy = 99;

    settingPlayerRepository.softDelete(id, deletedBy);

    verify(settingPlayerDao).softDelete(id, deletedBy);
  }

  @Test
  void softDeletePropagatesDaoException() {
    doThrow(new RuntimeException("dao error")).when(settingPlayerDao).softDelete(anyInt(), anyInt());

    assertThrows(RuntimeException.class, () -> settingPlayerRepository.softDelete(1, 2));
  }
}