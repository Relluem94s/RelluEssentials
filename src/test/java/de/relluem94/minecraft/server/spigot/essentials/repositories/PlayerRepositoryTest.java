package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PlayerDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerRepositoryTest {

  @Mock
  private PlayerDao playerDao;

  @Mock
  private PlayerEntry playerEntry;

  @Mock
  private PlayerPartnerEntry playerPartnerEntry;

  private PlayerRepository playerRepository;

  @BeforeEach
  void setUp() {
    playerRepository = new PlayerRepository(playerDao);
  }

  @Test
  void findAllReturnsDaoResult() {
    List<PlayerEntry> expectedList = List.of(playerEntry);
    when(playerDao.findAll()).thenReturn(expectedList);

    List<PlayerEntry> result = playerRepository.findAll();

    assertEquals(expectedList, result);
    verify(playerDao).findAll();
  }

  @Test
  void findAllPropagatesDaoException() {
    when(playerDao.findAll()).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> playerRepository.findAll());
  }

  @Test
  void findByUuidReturnsDaoResult() {
    String uuid = "test-uuid";
    when(playerDao.findByUuid(uuid)).thenReturn(playerEntry);

    PlayerEntry result = playerRepository.findByUuid(uuid);

    assertEquals(playerEntry, result);
    verify(playerDao).findByUuid(uuid);
  }

  @Test
  void findByUuidReturnsNullWhenNotFound() {
    String uuid = "unknown-uuid";
    when(playerDao.findByUuid(uuid)).thenReturn(null);

    PlayerEntry result = playerRepository.findByUuid(uuid);

    assertNull(result);
    verify(playerDao).findByUuid(uuid);
  }

  @Test
  void findByUuidPropagatesDaoException() {
    String uuid = "test-uuid";
    when(playerDao.findByUuid(uuid)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> playerRepository.findByUuid(uuid));
  }

  @Test
  void saveDelegatesToDaoInsert() {
    playerRepository.save(playerEntry);

    verify(playerDao).insert(playerEntry);
  }

  @Test
  void savePropagatesDaoException() {
    doThrow(new RuntimeException("db error")).when(playerDao).insert(playerEntry);

    assertThrows(RuntimeException.class, () -> playerRepository.save(playerEntry));
  }

  @Test
  void updateDelegatesToDaoUpdate() {
    playerRepository.update(playerEntry);

    verify(playerDao).update(playerEntry);
  }

  @Test
  void updatePropagatesDaoException() {
    doThrow(new RuntimeException("db error")).when(playerDao).update(playerEntry);

    assertThrows(RuntimeException.class, () -> playerRepository.update(playerEntry));
  }

  @Test
  void findPartnerByPlayerIdReturnsDaoResult() {
    int playerId = 42;
    when(playerDao.findPartnerByPlayerId(playerId)).thenReturn(playerPartnerEntry);

    PlayerPartnerEntry result = playerRepository.findPartnerByPlayerId(playerId);

    assertEquals(playerPartnerEntry, result);
    verify(playerDao).findPartnerByPlayerId(playerId);
  }

  @Test
  void findPartnerByPlayerIdReturnsNullWhenNotFound() {
    int playerId = 99;
    when(playerDao.findPartnerByPlayerId(playerId)).thenReturn(null);

    PlayerPartnerEntry result = playerRepository.findPartnerByPlayerId(playerId);

    assertNull(result);
    verify(playerDao).findPartnerByPlayerId(playerId);
  }

  @Test
  void findPartnerByPlayerIdPropagatesDaoException() {
    int playerId = 42;
    when(playerDao.findPartnerByPlayerId(playerId)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> playerRepository.findPartnerByPlayerId(playerId));
  }

  @Test
  void savePartnerDelegatesToDaoInsertPartner() {
    playerRepository.savePartner(playerPartnerEntry);

    verify(playerDao).insertPartner(playerPartnerEntry);
  }

  @Test
  void savePartnerPropagatesDaoException() {
    doThrow(new RuntimeException("db error")).when(playerDao).insertPartner(playerPartnerEntry);

    assertThrows(RuntimeException.class, () -> playerRepository.savePartner(playerPartnerEntry));
  }

  @Test
  void deletePartnerDelegatesToDaoDeletePartner() {
    playerRepository.deletePartner(playerPartnerEntry);

    verify(playerDao).deletePartner(playerPartnerEntry);
  }

  @Test
  void deletePartnerPropagatesDaoException() {
    doThrow(new RuntimeException("db error")).when(playerDao).deletePartner(playerPartnerEntry);

    assertThrows(RuntimeException.class, () -> playerRepository.deletePartner(playerPartnerEntry));
  }

  @Test
  void updatePartnerDelegatesToDaoUpdatePartner() {
    playerRepository.updatePartner(playerPartnerEntry);

    verify(playerDao).updatePartner(playerPartnerEntry);
  }

  @Test
  void updatePartnerPropagatesDaoException() {
    doThrow(new RuntimeException("db error")).when(playerDao).updatePartner(playerPartnerEntry);

    assertThrows(RuntimeException.class, () -> playerRepository.updatePartner(playerPartnerEntry));
  }
}