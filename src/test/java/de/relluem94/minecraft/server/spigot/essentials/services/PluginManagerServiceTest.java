package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PluginManagerServiceTest {

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  @Mock
  private PluginManager pluginManager;

  @Mock
  private Event event;

  @Mock
  private Plugin retrievedPlugin;

  private PluginManagerService pluginManagerService;

  @BeforeEach
  void setUp() {
    when(plugin.getServer()).thenReturn(server);
    when(server.getPluginManager()).thenReturn(pluginManager);
    pluginManagerService = new PluginManagerService(plugin);
  }

  @Test
  void callEventDelegatesEventToPluginManager() {
    pluginManagerService.callEvent(event);

    verify(pluginManager).callEvent(event);
  }

  @Test
  void callEventPropagatesRuntimeException() {
    doThrow(new RuntimeException("event error")).when(pluginManager).callEvent(event);

    org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> pluginManagerService.callEvent(event));
  }

  @Test
  void isPluginEnabledReturnsTrueWhenPluginIsEnabled() {
    when(pluginManager.isPluginEnabled("TestPlugin")).thenReturn(true);

    assertAll(
        () -> assertTrue(pluginManagerService.isPluginEnabled("TestPlugin"))
    );
  }

  @Test
  void isPluginEnabledReturnsFalseWhenPluginIsDisabled() {
    when(pluginManager.isPluginEnabled("TestPlugin")).thenReturn(false);

    assertAll(
        () -> assertFalse(pluginManagerService.isPluginEnabled("TestPlugin"))
    );
  }

  @Test
  void isPluginEnabledPropagatesRuntimeException() {
    when(pluginManager.isPluginEnabled("TestPlugin")).thenThrow(new RuntimeException("manager error"));

    org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> pluginManagerService.isPluginEnabled("TestPlugin"));
  }

  @Test
  void getPluginReturnsPluginInstanceWhenPluginExists() {
    when(pluginManager.getPlugin("TestPlugin")).thenReturn(retrievedPlugin);

    assertAll(
        () -> assertEquals(retrievedPlugin, pluginManagerService.getPlugin("TestPlugin"))
    );
  }

  @Test
  void getPluginReturnsNullWhenPluginDoesNotExist() {
    when(pluginManager.getPlugin("UnknownPlugin")).thenReturn(null);

    assertAll(
        () -> assertNull(pluginManagerService.getPlugin("UnknownPlugin"))
    );
  }

  @Test
  void getPluginPropagatesRuntimeException() {
    when(pluginManager.getPlugin("TestPlugin")).thenThrow(new RuntimeException("manager error"));

    org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> pluginManagerService.getPlugin("TestPlugin"));
  }
}