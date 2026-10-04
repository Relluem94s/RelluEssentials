package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.SettingDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingRepositoryTest {

  @Mock
  private SettingDao settingDao;

  @Mock
  private SettingEntry settingEntry;

  private SettingRepository settingRepository;

  @BeforeEach
  void setUp() {
    settingRepository = new SettingRepository(settingDao);
  }

  @Test
  void findAllReturnsAllSettingEntries() {
    List<SettingEntry> expectedEntries = List.of(settingEntry);
    when(settingDao.findAll()).thenReturn(expectedEntries);

    List<SettingEntry> result = settingRepository.findAll();

    assertEquals(expectedEntries, result);
    verify(settingDao).findAll();
  }

  @Test
  void findAllReturnsEmptyListWhenNoEntriesExist() {
    when(settingDao.findAll()).thenReturn(List.of());

    List<SettingEntry> result = settingRepository.findAll();

    assertEquals(List.of(), result);
    verify(settingDao).findAll();
  }

  @Test
  void findAllPropagatesExceptionFromDao() {
    when(settingDao.findAll()).thenThrow(new RuntimeException("Database error"));

    assertThrows(RuntimeException.class, () -> settingRepository.findAll());
    verify(settingDao).findAll();
  }
}