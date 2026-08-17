package de.relluem94.minecraft.server.spigot.essentials.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistryKeyTest {

  private Plugin mockPlugin;


  @BeforeEach
  void setUp() throws Exception {
    mockPlugin = mock(Plugin.class);
    when(mockPlugin.getName()).thenReturn("TestPlugin");
    resetInternalPlugin();
  }

  private void resetInternalPlugin() throws Exception {
    Field field = RegistryKey.class.getDeclaredField("registeredPlugin");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  void ofWithKeyShouldThrowExceptionWhenPluginNotInitialized() {
    assertThrows(IllegalStateException.class, () -> RegistryKey.of("test_key"));
  }

  @Test
  void ofWithKeyShouldReturnKeyWithPluginNamespace() {
    RegistryKey.initializeInternalPlugin(mockPlugin);
    RegistryKey key = RegistryKey.of("test_key");
    assertEquals("testplugin:test_key", key.toString());
  }

  @Test
  void ofWithPluginAndKeyShouldReturnKeyWithPluginNamespace() {
    RegistryKey key = RegistryKey.of(mockPlugin, "test_key");
    assertEquals("testplugin:test_key", key.toString());
  }

  @Test
  void ofWithNamespaceAndKeyShouldReturnKey() {
    RegistryKey key = RegistryKey.of("MyNamespace", "TestKey");
    assertEquals("mynamespace:testkey", key.toString());
  }

  @Test
  void fromStringShouldReturnKeyFromValidString() {
    RegistryKey key = RegistryKey.fromString("Namespace:KeyName");
    assertEquals("namespace:keyname", key.toString());
  }

  @Test
  void fromStringShouldThrowExceptionForInvalidFormat() {
    assertThrows(IllegalArgumentException.class, () -> RegistryKey.fromString("invalidformat"));
  }

  @Test
  void toStringShouldReturnFormattedString() {
    RegistryKey key = new RegistryKey("namespace", "key");
    assertEquals("namespace:key", key.toString());
  }
}