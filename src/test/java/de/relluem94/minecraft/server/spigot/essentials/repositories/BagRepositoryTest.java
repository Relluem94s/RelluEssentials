package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BagDao;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BagRepositoryTest {

  @Mock
  private BagDao bagDao;

  @Mock
  private BagEntry bagEntry;

  private BagRepository bagRepository;

  @BeforeEach
  void setUp() {
    bagRepository = new BagRepository(bagDao);
  }

  @Test
  void findAllReturnsBagEntryList() {
    List<BagEntry> expectedBags = List.of(bagEntry);
    when(bagDao.findAllBags()).thenReturn(expectedBags);

    List<BagEntry> result = bagRepository.findAll();

    assertEquals(expectedBags, result);
    verify(bagDao).findAllBags();
  }

  @Test
  void findAllReturnsEmptyListWhenNoBagsExist() {
    when(bagDao.findAllBags()).thenReturn(List.of());

    List<BagEntry> result = bagRepository.findAll();

    assertTrue(result.isEmpty());
    verify(bagDao).findAllBags();
  }

  @Test
  void findAllPropagatesDaoException() {
    when(bagDao.findAllBags()).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> bagRepository.findAll());
  }

  @ParameterizedTest
  @CsvSource({"1, 1", "2, 3", "99, 42"})
  void findByPlayerIdAndBagTypeIdReturnsMatchingEntry(int playerId, int bagTypeId) {
    when(bagDao.findBagByPlayerIdAndBagTypeId(playerId, bagTypeId)).thenReturn(Optional.of(bagEntry));

    Optional<BagEntry> result = bagRepository.findByPlayerIdAndBagTypeId(playerId, bagTypeId);

    assertTrue(result.isPresent());
    assertEquals(bagEntry, result.get());
    verify(bagDao).findBagByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  @ParameterizedTest
  @CsvSource({"1, 1", "2, 3", "99, 42"})
  void findByPlayerIdAndBagTypeIdReturnsEmptyWhenNotFound(int playerId, int bagTypeId) {
    when(bagDao.findBagByPlayerIdAndBagTypeId(playerId, bagTypeId)).thenReturn(Optional.empty());

    Optional<BagEntry> result = bagRepository.findByPlayerIdAndBagTypeId(playerId, bagTypeId);

    assertTrue(result.isEmpty());
    verify(bagDao).findBagByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  @Test
  void findByPlayerIdAndBagTypeIdPropagatesDaoException() {
    when(bagDao.findBagByPlayerIdAndBagTypeId(anyInt(), anyInt())).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> bagRepository.findByPlayerIdAndBagTypeId(1, 1));
  }

  @ParameterizedTest
  @CsvSource({"1, 1", "2, 3", "99, 42"})
  void insertReturnsNewlyCreatedBagEntry(int playerId, int bagTypeId) {
    when(bagDao.findBagByPlayerIdAndBagTypeId(playerId, bagTypeId)).thenReturn(Optional.of(bagEntry));

    BagEntry result = bagRepository.insert(playerId, bagTypeId);

    assertEquals(bagEntry, result);
    verify(bagDao).insertBag(playerId, bagTypeId);
    verify(bagDao).findBagByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  @Test
  void insertThrowsWhenCreatedEntryCannotBeFound() {
    when(bagDao.findBagByPlayerIdAndBagTypeId(anyInt(), anyInt())).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> bagRepository.insert(1, 1));
  }

  @Test
  void insertPropagatesDaoExceptionOnInsert() {
    doThrow(new RuntimeException("DB error")).when(bagDao).insertBag(anyInt(), anyInt());

    assertThrows(RuntimeException.class, () -> bagRepository.insert(1, 1));
  }

  @Test
  void insertPropagatesDaoExceptionOnFind() {
    when(bagDao.findBagByPlayerIdAndBagTypeId(anyInt(), anyInt())).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> bagRepository.insert(1, 1));
  }

  @Test
  void updateDelegatesUpdateToBagDao() {
    bagRepository.update(bagEntry);

    verify(bagDao).updateBag(bagEntry);
  }

  @Test
  void updatePropagatesDaoException() {
    doThrow(new RuntimeException("DB error")).when(bagDao).updateBag(bagEntry);

    assertThrows(RuntimeException.class, () -> bagRepository.update(bagEntry));
  }
}