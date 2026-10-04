package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PluginMetadataServiceTest {

  @Mock
  private Plugin plugin;

  private PluginMetadataService pluginMetadataService;

  @BeforeEach
  void setUp() {
    pluginMetadataService = new PluginMetadataService(plugin);
  }

  @Test
  void getNameReturnsPluginName() {
    Mockito.when(plugin.getName()).thenReturn("TestPlugin");

    String result = pluginMetadataService.getName();

    assertEquals("TestPlugin", result);
  }

  @Test
  void getVersionReturnsPluginVersion() {
    PluginDescriptionFile descriptionFile = new PluginDescriptionFile("TestPlugin", "1.0.0", "de.test.Main");
    Mockito.when(plugin.getDescription()).thenReturn(descriptionFile);

    String result = pluginMetadataService.getVersion();

    assertEquals("1.0.0", result);
  }

  @Test
  void getMainClassReturnsPluginMainClass() {
    PluginDescriptionFile descriptionFile = new PluginDescriptionFile("TestPlugin", "1.0.0", "de.test.Main");
    Mockito.when(plugin.getDescription()).thenReturn(descriptionFile);

    String result = pluginMetadataService.getMainClass();

    assertEquals("de.test.Main", result);
  }

  @Test
  void getPluginReturnsPluginInstance() {
    Plugin result = pluginMetadataService.getPlugin();

    assertEquals(plugin, result);
  }

  @Test
  void getAllFieldsReturnCorrectValues() {
    PluginDescriptionFile descriptionFile = new PluginDescriptionFile("TestPlugin", "1.0.0", "de.test.Main");
    Mockito.when(plugin.getName()).thenReturn("TestPlugin");
    Mockito.when(plugin.getDescription()).thenReturn(descriptionFile);

    assertAll(
        () -> assertEquals("TestPlugin", pluginMetadataService.getName()),
        () -> assertEquals("1.0.0", pluginMetadataService.getVersion()),
        () -> assertEquals("de.test.Main", pluginMetadataService.getMainClass()),
        () -> assertEquals(plugin, pluginMetadataService.getPlugin())
    );
  }
}