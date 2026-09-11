package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.SignAction;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignRegistryTest {

  @Mock
  private Plugin plugin;

  @Mock
  private SignAction signAction;

  @Mock
  private SignAction secondSignAction;

  @BeforeEach
  void clearRegistry() throws Exception {
    Field registeredActionsField = SignRegistry.class.getDeclaredField("registeredActions");
    registeredActionsField.setAccessible(true);
    ((Map<?, ?>) registeredActionsField.get(null)).clear();
  }

  @AfterEach
  void clearRegistryAfter() throws Exception {
    Field registeredActionsField = SignRegistry.class.getDeclaredField("registeredActions");
    registeredActionsField.setAccessible(true);
    ((Map<?, ?>) registeredActionsField.get(null)).clear();
  }

  @Test
  void privateConstructorThrowsIllegalStateException() throws Exception {
    Constructor<SignRegistry> constructor = SignRegistry.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }

  @Test
  void registerStoresActionWithCorrectKey() {
    lenient().when(plugin.getName()).thenReturn("TestPlugin");
    lenient().when(signAction.getShorthandBracket()).thenReturn("[tp]");
    lenient().when(signAction.getNameBracket()).thenReturn("[testaction]");
    lenient().when(signAction.getDisplayName()).thenReturn("Test Action");

    SignRegistry.register(plugin, "testaction", signAction);

    Optional<SignAction> found = SignRegistry.find(new RelluEssentialsNamespacedKey("TestPlugin", "testaction"));
    assertTrue(found.isPresent());
    assertEquals(signAction, found.get());
  }

  @Test
  void registerThrowsIllegalArgumentExceptionWhenKeyAlreadyRegistered() {
    when(plugin.getName()).thenReturn("TestPlugin");

    SignRegistry.register(plugin, "testaction", signAction);

    assertThrows(IllegalArgumentException.class,
        () -> SignRegistry.register(plugin, "testaction", secondSignAction));
  }

  @Test
  void findReturnsEmptyWhenKeyNotRegistered() {
    Optional<SignAction> result = SignRegistry.find(new RelluEssentialsNamespacedKey("Unknown", "action"));
    assertFalse(result.isPresent());
  }

  @Test
  void findReturnsCorrectActionForRegisteredKey() {
    when(plugin.getName()).thenReturn("TestPlugin");

    SignRegistry.register(plugin, "myaction", signAction);

    Optional<SignAction> result = SignRegistry.find(new RelluEssentialsNamespacedKey("TestPlugin", "myaction"));
    assertTrue(result.isPresent());
    assertEquals(signAction, result.get());
  }

  @ParameterizedTest
  @ValueSource(strings = {"[tp]", "[testaction]", "Test Action"})
  void findByLineReturnsActionWhenLineMatchesAnyIdentifier(String line) {
    when(plugin.getName()).thenReturn("TestPlugin");
    when(signAction.getShorthandBracket()).thenReturn("[tp]");
    lenient().when(signAction.getNameBracket()).thenReturn("[testaction]");
    lenient().when(signAction.getDisplayName()).thenReturn("Test Action");

    SignRegistry.register(plugin, "testaction", signAction);

    Optional<SignAction> result = SignRegistry.findByLine(line);
    assertTrue(result.isPresent());
    assertEquals(signAction, result.get());
  }

  @ParameterizedTest
  @ValueSource(strings = {"[TP]", "[TESTACTION]", "TEST ACTION"})
  void findByLineIsCaseInsensitive(String line) {
    when(plugin.getName()).thenReturn("TestPlugin");
    when(signAction.getShorthandBracket()).thenReturn("[tp]");
    lenient().when(signAction.getNameBracket()).thenReturn("[testaction]");
    lenient().when(signAction.getDisplayName()).thenReturn("Test Action");

    SignRegistry.register(plugin, "testaction", signAction);

    Optional<SignAction> result = SignRegistry.findByLine(line);
    assertTrue(result.isPresent());
    assertEquals(signAction, result.get());
  }

  @Test
  void findByLineReturnsEmptyWhenNoMatch() {
    when(plugin.getName()).thenReturn("TestPlugin");
    when(signAction.getShorthandBracket()).thenReturn("[tp]");
    when(signAction.getNameBracket()).thenReturn("[testaction]");
    when(signAction.getDisplayName()).thenReturn("Test Action");

    SignRegistry.register(plugin, "testaction", signAction);

    Optional<SignAction> result = SignRegistry.findByLine("[unknown]");
    assertFalse(result.isPresent());
  }

  @ParameterizedTest
  @ValueSource(strings = {"[tp]", "tp", " tp ", "[testaction]", "testaction", "Test Action"})
  void findEntryByLineReturnsEntryWhenLineMatchesAfterNormalization(String line) {
    when(plugin.getName()).thenReturn("TestPlugin");
    lenient().when(signAction.getShorthandBracket()).thenReturn("tp");
    lenient().when(signAction.getNameBracket()).thenReturn("testaction");
    lenient().when(signAction.getDisplayName()).thenReturn("Test Action");

    SignRegistry.register(plugin, "testaction", signAction);

    Optional<Map.Entry<RelluEssentialsNamespacedKey, SignAction>> result = SignRegistry.findEntryByLine(line);
    assertTrue(result.isPresent());
    assertEquals("TestPlugin", result.get().getKey().getNamespace());
    assertEquals("testaction", result.get().getKey().getKey());
    assertEquals(signAction, result.get().getValue());
  }

  @Test
  void findEntryByLineReturnsEmptyWhenNoMatch() {
    when(plugin.getName()).thenReturn("TestPlugin");
    when(signAction.getShorthandBracket()).thenReturn("tp");
    when(signAction.getNameBracket()).thenReturn("testaction");
    when(signAction.getDisplayName()).thenReturn("Test Action");

    SignRegistry.register(plugin, "testaction", signAction);

    Optional<Map.Entry<RelluEssentialsNamespacedKey, SignAction>> result = SignRegistry.findEntryByLine("[unknown]");
    assertFalse(result.isPresent());
  }

  @Test
  void findEntryByLineReturnsCorrectNamespacedKey() {
    when(plugin.getName()).thenReturn("MyPlugin");
    when(signAction.getShorthandBracket()).thenReturn("warp");
    lenient().when(signAction.getNameBracket()).thenReturn("warpaction");
    lenient().when(signAction.getDisplayName()).thenReturn("Warp Action");

    SignRegistry.register(plugin, "warpaction", signAction);

    Optional<Map.Entry<RelluEssentialsNamespacedKey, SignAction>> result = SignRegistry.findEntryByLine("[warp]");
    assertTrue(result.isPresent());
    assertEquals("MyPlugin", result.get().getKey().getNamespace());
    assertEquals("warpaction", result.get().getKey().getKey());
  }

  @Test
  void getAllReturnsAllRegisteredActions() {
    when(plugin.getName()).thenReturn("TestPlugin");

    SignRegistry.register(plugin, "action1", signAction);
    SignRegistry.register(plugin, "action2", secondSignAction);

    Collection<SignAction> all = SignRegistry.getAll();
    assertEquals(2, all.size());
    assertTrue(all.contains(signAction));
    assertTrue(all.contains(secondSignAction));
  }

  @Test
  void getAllReturnsEmptyCollectionWhenNothingRegistered() {
    Collection<SignAction> all = SignRegistry.getAll();
    assertTrue(all.isEmpty());
  }

  @Test
  void getAllReturnsUnmodifiableCollection() {
    when(plugin.getName()).thenReturn("TestPlugin");
    SignRegistry.register(plugin, "action1", signAction);

    Collection<SignAction> all = SignRegistry.getAll();
    assertThrows(UnsupportedOperationException.class, () -> all.add(secondSignAction));
  }

  @Test
  void getAllByNamespaceReturnsOnlyActionsForSpecifiedNamespace() {
    when(plugin.getName()).thenReturn("PluginA");
    Plugin secondPlugin = org.mockito.Mockito.mock(Plugin.class);
    when(secondPlugin.getName()).thenReturn("PluginB");

    SignRegistry.register(plugin, "action1", signAction);
    SignRegistry.register(secondPlugin, "action2", secondSignAction);

    Collection<SignAction> result = SignRegistry.getAllByNamespace("PluginA");
    assertEquals(1, result.size());
    assertTrue(result.contains(signAction));
    assertFalse(result.contains(secondSignAction));
  }

  @Test
  void getAllByNamespaceIsCaseInsensitive() {
    when(plugin.getName()).thenReturn("TestPlugin");

    SignRegistry.register(plugin, "action1", signAction);

    Collection<SignAction> result = SignRegistry.getAllByNamespace("testplugin");
    assertEquals(1, result.size());
    assertTrue(result.contains(signAction));
  }

  @Test
  void getAllByNamespaceReturnsEmptyWhenNamespaceNotFound() {
    when(plugin.getName()).thenReturn("TestPlugin");
    SignRegistry.register(plugin, "action1", signAction);

    Collection<SignAction> result = SignRegistry.getAllByNamespace("UnknownPlugin");
    assertTrue(result.isEmpty());
  }

  @Test
  void getAllByNamespaceReturnsMultipleActionsForSameNamespace() {
    when(plugin.getName()).thenReturn("TestPlugin");

    SignRegistry.register(plugin, "action1", signAction);
    SignRegistry.register(plugin, "action2", secondSignAction);

    Collection<SignAction> result = SignRegistry.getAllByNamespace("TestPlugin");
    assertEquals(2, result.size());
    assertTrue(result.contains(signAction));
    assertTrue(result.contains(secondSignAction));
  }
}