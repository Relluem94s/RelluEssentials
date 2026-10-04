package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.TraderNpcDao;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraderNpcRepositoryTest {

  @Mock
  private TraderNpcDao traderNpcDao;

  @InjectMocks
  private TraderNpcRepository traderNpcRepository;

  @Test
  void loadAllReturnsAllEntriesFromDao() {
    List<TraderNpcEntry> expectedEntries = List.of(new TraderNpcEntry(), new TraderNpcEntry());
    when(traderNpcDao.findAll()).thenReturn(expectedEntries);

    List<TraderNpcEntry> actualEntries = traderNpcRepository.loadAll();

    assertAll(
        () -> assertEquals(expectedEntries.size(), actualEntries.size()),
        () -> assertEquals(expectedEntries, actualEntries)
    );
    verify(traderNpcDao).findAll();
  }

  @Test
  void loadAllReturnsEmptyListWhenDaoReturnsNoEntries() {
    when(traderNpcDao.findAll()).thenReturn(List.of());

    List<TraderNpcEntry> actualEntries = traderNpcRepository.loadAll();

    assertAll(
        () -> assertEquals(0, actualEntries.size()),
        () -> assertEquals(List.of(), actualEntries)
    );
    verify(traderNpcDao).findAll();
  }

  @Test
  void loadAllPropagatesExceptionFromDao() {
    when(traderNpcDao.findAll()).thenThrow(new RuntimeException("dao failure"));

    assertThrows(RuntimeException.class, () -> traderNpcRepository.loadAll());
  }
}