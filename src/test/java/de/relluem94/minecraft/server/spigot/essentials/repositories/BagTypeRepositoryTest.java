package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BagDao;
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
class BagTypeRepositoryTest {

  @Mock
  private BagDao bagDao;

  private BagTypeRepository bagTypeRepository;

  @BeforeEach
  void setUp() {
    bagTypeRepository = new BagTypeRepository(bagDao);
  }

  @Test
  void findAllReturnsBagTypeEntriesFromDao() {
    BagTypeEntry firstEntry = mock(BagTypeEntry.class);
    BagTypeEntry secondEntry = mock(BagTypeEntry.class);
    List<BagTypeEntry> expectedEntries = List.of(firstEntry, secondEntry);
    when(bagDao.findAllBagTypes()).thenReturn(expectedEntries);

    List<BagTypeEntry> actualEntries = bagTypeRepository.findAll();

    assertEquals(expectedEntries, actualEntries);
    verify(bagDao).findAllBagTypes();
  }

  @Test
  void findAllReturnsEmptyListWhenDaoReturnsEmptyList() {
    when(bagDao.findAllBagTypes()).thenReturn(List.of());

    List<BagTypeEntry> actualEntries = bagTypeRepository.findAll();

    assertTrue(actualEntries.isEmpty());
    verify(bagDao).findAllBagTypes();
  }

  @Test
  void findAllPropagatesExceptionFromDao() {
    when(bagDao.findAllBagTypes()).thenThrow(new RuntimeException("Database error"));

    assertThrows(RuntimeException.class, () -> bagTypeRepository.findAll());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 99, Integer.MAX_VALUE})
  void findByIdReturnsPresentOptionalWhenDaoFindsEntry(int id) {
    BagTypeEntry expectedEntry = mock(BagTypeEntry.class);
    when(bagDao.findBagTypeById(id)).thenReturn(Optional.of(expectedEntry));

    Optional<BagTypeEntry> result = bagTypeRepository.findById(id);

    assertTrue(result.isPresent());
    assertEquals(expectedEntry, result.get());
    verify(bagDao).findBagTypeById(id);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 99, Integer.MAX_VALUE})
  void findByIdReturnsEmptyOptionalWhenDaoFindsNoEntry(int id) {
    when(bagDao.findBagTypeById(id)).thenReturn(Optional.empty());

    Optional<BagTypeEntry> result = bagTypeRepository.findById(id);

    assertTrue(result.isEmpty());
    verify(bagDao).findBagTypeById(id);
  }

  @Test
  void findByIdPropagatesExceptionFromDao() {
    when(bagDao.findBagTypeById(anyInt())).thenThrow(new RuntimeException("Database error"));

    assertThrows(RuntimeException.class, () -> bagTypeRepository.findById(1));
  }
}