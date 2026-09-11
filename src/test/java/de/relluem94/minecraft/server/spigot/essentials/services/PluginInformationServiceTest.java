package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PluginInformationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PluginInformationServiceTest {

  @Mock
  private PluginInformationRepository pluginInformationRepository;

  @Mock
  private PluginInformationEntry pluginInformationEntry;

  private PluginInformationService pluginInformationService;

  @BeforeEach
  void setUp() {
    pluginInformationService = new PluginInformationService(pluginInformationRepository);
  }

  @Test
  void loadSetsPluginInformationFromRepository() {
    when(pluginInformationRepository.load()).thenReturn(pluginInformationEntry);

    pluginInformationService.load();

    assertAll(
        () -> assertEquals(pluginInformationEntry, pluginInformationService.getPluginInformation()),
        () -> verify(pluginInformationRepository, times(1)).load()
    );
  }

  @Test
  void loadPropagatesExceptionFromRepository() {
    when(pluginInformationRepository.load()).thenThrow(new RuntimeException("load failed"));

    assertThrows(RuntimeException.class, () -> pluginInformationService.load());
  }

  @Test
  void applyPatchedInformationReplacesPreviousPluginInformation() {
    PluginInformationEntry patchedEntry = mock(PluginInformationEntry.class);

    pluginInformationService.applyPatchedInformation(patchedEntry);

    assertEquals(patchedEntry, pluginInformationService.getPluginInformation());
  }

  @Test
  void updateTabHeaderSetsHeaderAndPersists() {
    when(pluginInformationRepository.load()).thenReturn(pluginInformationEntry);
    pluginInformationService.load();

    pluginInformationService.updateTabHeader("newHeader");

    assertAll(
        () -> verify(pluginInformationEntry, times(1)).setTabHeader("newHeader"),
        () -> verify(pluginInformationRepository, times(1)).save(pluginInformationEntry)
    );
  }

  @Test
  void updateTabHeaderPropagatesExceptionFromRepository() {
    when(pluginInformationRepository.load()).thenReturn(pluginInformationEntry);
    pluginInformationService.load();
    doThrow(new RuntimeException("save failed")).when(pluginInformationRepository).save(pluginInformationEntry);

    assertThrows(RuntimeException.class, () -> pluginInformationService.updateTabHeader("newHeader"));
  }

  @Test
  void updateTabFooterSetsFooterAndPersists() {
    when(pluginInformationRepository.load()).thenReturn(pluginInformationEntry);
    pluginInformationService.load();

    pluginInformationService.updateTabFooter("newFooter");

    assertAll(
        () -> verify(pluginInformationEntry, times(1)).setTabFooter("newFooter"),
        () -> verify(pluginInformationRepository, times(1)).save(pluginInformationEntry)
    );
  }

  @Test
  void updateTabFooterPropagatesExceptionFromRepository() {
    when(pluginInformationRepository.load()).thenReturn(pluginInformationEntry);
    pluginInformationService.load();
    doThrow(new RuntimeException("save failed")).when(pluginInformationRepository).save(pluginInformationEntry);

    assertThrows(RuntimeException.class, () -> pluginInformationService.updateTabFooter("newFooter"));
  }

  @Test
  void updateMotdSetsMotdAndPersists() {
    when(pluginInformationRepository.load()).thenReturn(pluginInformationEntry);
    pluginInformationService.load();

    pluginInformationService.updateMotd("newMotd");

    assertAll(
        () -> verify(pluginInformationEntry, times(1)).setMotdMessage("newMotd"),
        () -> verify(pluginInformationRepository, times(1)).save(pluginInformationEntry)
    );
  }

  @Test
  void updateMotdPropagatesExceptionFromRepository() {
    when(pluginInformationRepository.load()).thenReturn(pluginInformationEntry);
    pluginInformationService.load();
    doThrow(new RuntimeException("save failed")).when(pluginInformationRepository).save(pluginInformationEntry);

    assertThrows(RuntimeException.class, () -> pluginInformationService.updateMotd("newMotd"));
  }
}