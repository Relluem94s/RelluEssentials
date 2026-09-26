package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PluginInformationDao;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PluginInformationRepositoryTest {

  @Mock
  private PluginInformationDao pluginInformationDao;

  @InjectMocks
  private PluginInformationRepository pluginInformationRepository;

  @Test
  void loadReturnsEntryFromDao() {
    PluginInformationEntry expectedEntry = new PluginInformationEntry();
    when(pluginInformationDao.find()).thenReturn(expectedEntry);

    PluginInformationEntry result = pluginInformationRepository.load();

    assertEquals(expectedEntry, result);
    verify(pluginInformationDao).find();
  }

  @Test
  void loadPropagatesDaoException() {
    when(pluginInformationDao.find()).thenThrow(new RuntimeException("DAO failure"));

    assertThrows(RuntimeException.class, () -> pluginInformationRepository.load());
  }

  @Test
  void saveThrowsNotImplementedException() {
    PluginInformationEntry pluginInformationEntry = new PluginInformationEntry();

    assertThrows(NotImplementedException.class, () -> pluginInformationRepository.save(pluginInformationEntry));
  }
}