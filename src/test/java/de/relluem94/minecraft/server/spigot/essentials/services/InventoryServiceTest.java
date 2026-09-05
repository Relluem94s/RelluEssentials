package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.registries.InventoryRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.model.RegisteredInventory;
import java.util.List;
import java.util.Optional;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  @Mock
  private InventoryRegistry inventoryRegistry;

  @Mock
  private Plugin plugin;

  @Mock
  private RegisteredInventory registeredInventory;

  private InventoryService inventoryService;

  @BeforeEach
  void setUp() {
    inventoryService = new InventoryService(inventoryRegistry);
  }

  @Test
  void createWithPluginAndIdReturnsRegisteredInventory() {
    String pluginName = "TestPlugin";
    String inventoryId = "test_inventory";
    String title = "Test Title";
    int size = 27;
    CustomItem.Type itemFilter = CustomItem.Type.NONE;

    when(plugin.getName()).thenReturn(pluginName);
    RelluEssentialsNamespacedKey expectedKey = new RelluEssentialsNamespacedKey(pluginName, inventoryId);
    when(inventoryRegistry.register(expectedKey, title, size, itemFilter)).thenReturn(registeredInventory);

    RegisteredInventory result = inventoryService.create(plugin, inventoryId, title, size, itemFilter);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(registeredInventory, result)
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createWithPluginAndIdPropagatesNullPointerExceptionForNullPlugin() {
    assertThrows(NullPointerException.class,
        () -> inventoryService.create(null, "id", "title", 27, CustomItem.Type.NONE));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createWithPluginAndIdPropagatesNullPointerExceptionForNullInventoryId() {
    assertThrows(NullPointerException.class,
        () -> inventoryService.create(plugin, null, "title", 27, CustomItem.Type.NONE));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createWithPluginAndIdPropagatesNullPointerExceptionForNullTitle() {
    assertThrows(NullPointerException.class,
        () -> inventoryService.create(plugin, "id", null, 27, CustomItem.Type.NONE));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createWithPluginAndIdPropagatesNullPointerExceptionForNullItemFilter() {
    assertThrows(NullPointerException.class,
        () -> inventoryService.create(plugin, "id", "title", 27, null));
  }

  @Test
  void createWithKeyReturnsRegisteredInventory() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("namespace", "key");
    String title = "Title";
    int size = 54;
    CustomItem.Type itemFilter = CustomItem.Type.TOOL;

    when(inventoryRegistry.register(key, title, size, itemFilter)).thenReturn(registeredInventory);

    RegisteredInventory result = inventoryService.create(key, title, size, itemFilter);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(registeredInventory, result)
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createWithKeyPropagatesNullPointerExceptionForNullKey() {
    assertThrows(NullPointerException.class,
        () -> inventoryService.create(null, "title", 27, CustomItem.Type.NONE));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createWithKeyPropagatesNullPointerExceptionForNullTitle() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("namespace", "key");
    assertThrows(NullPointerException.class,
        () -> inventoryService.create(key, null, 27, CustomItem.Type.NONE));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createWithKeyPropagatesNullPointerExceptionForNullItemFilter() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("namespace", "key");
    assertThrows(NullPointerException.class,
        () -> inventoryService.create(key, "title", 27, null));
  }

  @Test
  void findReturnsOptionalContainingRegisteredInventory() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("namespace", "key");
    when(inventoryRegistry.find(key)).thenReturn(Optional.of(registeredInventory));

    Optional<RegisteredInventory> result = inventoryService.find(key);

    assertNotNull(result);
    assertTrue(result.isPresent());
    assertEquals(registeredInventory, result.get());
  }

  @Test
  void findReturnsEmptyOptionalWhenNotFound() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("namespace", "missing");
    when(inventoryRegistry.find(key)).thenReturn(Optional.empty());

    Optional<RegisteredInventory> result = inventoryService.find(key);

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void findPropagatesNullPointerExceptionForNullKey() {
    assertThrows(NullPointerException.class,
        () -> inventoryService.find(null));
  }

  @Test
  void getAllByNamespaceReturnsListFromRegistry() {
    String namespace = "testnamespace";
    List<RegisteredInventory> expectedList = List.of(registeredInventory);
    when(inventoryRegistry.findAllByNamespace(namespace)).thenReturn(expectedList);

    List<RegisteredInventory> result = inventoryService.getAllByNamespace(namespace);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(1, result.size()),
        () -> assertEquals(registeredInventory, result.getFirst())
    );
  }

  @Test
  void getAllByNamespaceReturnsEmptyListWhenNoneFound() {
    String namespace = "emptyNamespace";
    when(inventoryRegistry.findAllByNamespace(namespace)).thenReturn(List.of());

    List<RegisteredInventory> result = inventoryService.getAllByNamespace(namespace);

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void getAllByNamespacePropagatesNullPointerExceptionForNullNamespace() {
    assertThrows(NullPointerException.class,
        () -> inventoryService.getAllByNamespace(null));
  }
}