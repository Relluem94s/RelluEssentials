package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ItemRegistryTest {

  @Mock
  private Plugin mockPlugin;

  @Mock
  private CustomItem mockCustomItem;

  @Mock
  private RelluEssentials mockRelluEssentials;

  @Mock
  private RelluEssentialsNamespacedKey mockRegistryKey;

  private ItemRegistry itemRegistry;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(mockPlugin.getName()).thenReturn("TestPlugin");
    when(mockRelluEssentials.getName()).thenReturn("TestPlugin");
    itemRegistry = new ItemRegistry();

    when(mockRegistryKey.toString()).thenReturn("test_key");
  }

  @Test
  void register_ShouldAddItemsToMap() {
    itemRegistry.register(mockRegistryKey, mockCustomItem);

    Optional<CustomItem> found = itemRegistry.findByIdentifier("test_key");
    assertTrue(found.isPresent());
    assertEquals(mockCustomItem, found.get());
  }

  @Test
  void register_ShouldThrowException_WhenKeyAlreadyExists() {
    itemRegistry.register(mockRegistryKey, mockCustomItem);

    assertThrows(IllegalArgumentException.class,
        () -> itemRegistry.register(mockRegistryKey, mockCustomItem));
  }

  @Test
  void find_ShouldReturnEmpty_WhenKeyDoesNotExist() {
    RelluEssentialsNamespacedKey unknownKey = mock(RelluEssentialsNamespacedKey.class);
    when(unknownKey.toString()).thenReturn("unknown");

    Optional<CustomItem> result = itemRegistry.find(unknownKey);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByIdentifier_ShouldReturnCorrectItem() {
    itemRegistry.register(mockRegistryKey, mockCustomItem);

    Optional<CustomItem> result = itemRegistry.findByIdentifier("test_key");

    assertTrue(result.isPresent());
    assertEquals(mockCustomItem, result.get());
  }

  @Test
  void getAll_ShouldReturnAllRegisteredItems() {
    itemRegistry.register(mockRegistryKey, mockCustomItem);

    assertEquals(1, itemRegistry.getAll().size());
    assertTrue(itemRegistry.getAll().containsKey("test_key"));
  }

  @Test
  void getAllByType_ShouldFilterItemsCorrectly() {
    CustomItem typeAItem = mock(CustomItem.class);
    CustomItem typeBItem = mock(CustomItem.class);
    RelluEssentialsNamespacedKey keyA = mock(RelluEssentialsNamespacedKey.class);
    RelluEssentialsNamespacedKey keyB = mock(RelluEssentialsNamespacedKey.class);

    when(keyA.toString()).thenReturn("key_a");
    when(keyB.toString()).thenReturn("key_b");
    when(typeAItem.type()).thenReturn(CustomItem.Type.GADGET);
    when(typeBItem.type()).thenReturn(CustomItem.Type.TOOL);

    itemRegistry.register(keyA, typeAItem);
    itemRegistry.register(keyB, typeBItem);

    var results = itemRegistry.getAllByType(CustomItem.Type.GADGET);

    assertEquals(1, results.size());
    assertEquals(typeAItem, results.getFirst());
  }

  @Test
  void findByItemStack_ShouldReturnItem_WhenMatchFound() {
    ItemStack mockItemStack = mock(org.bukkit.inventory.ItemStack.class);
    ItemStack itemStackFromCustomItem = mock(org.bukkit.inventory.ItemStack.class);

    itemRegistry.register(mockRegistryKey, mockCustomItem);

    when(mockCustomItem.toItemStack()).thenReturn(itemStackFromCustomItem);
    when(itemStackFromCustomItem.isSimilar(mockItemStack)).thenReturn(true);

    Optional<CustomItem> result = itemRegistry.findByItemStack(mockItemStack);

    assertTrue(result.isPresent());
    assertEquals(mockCustomItem, result.get());
  }

  @Test
  void findByItemStack_ShouldReturnEmpty_WhenNoMatchFound() {
    org.bukkit.inventory.ItemStack mockItemStack = mock(org.bukkit.inventory.ItemStack.class);
    org.bukkit.inventory.ItemStack itemStackFromCustomItem = mock(
        org.bukkit.inventory.ItemStack.class);

    itemRegistry.register(mockRegistryKey, mockCustomItem);

    when(mockCustomItem.toItemStack()).thenReturn(itemStackFromCustomItem);
    when(itemStackFromCustomItem.isSimilar(mockItemStack)).thenReturn(false);

    Optional<CustomItem> result = itemRegistry.findByItemStack(mockItemStack);

    assertTrue(result.isEmpty());
  }

  @Test
  void getAllByTypeAndNamespace_ShouldFilterItemsByTypeAndNamespace() {
    CustomItem matchingItem = mock(CustomItem.class);
    CustomItem wrongTypeItem = mock(CustomItem.class);
    CustomItem wrongNamespaceItem = mock(CustomItem.class);

    RelluEssentialsNamespacedKey matchingKey = mock(RelluEssentialsNamespacedKey.class);
    RelluEssentialsNamespacedKey wrongTypeKey = mock(RelluEssentialsNamespacedKey.class);
    RelluEssentialsNamespacedKey wrongNamespaceKey = mock(RelluEssentialsNamespacedKey.class);

    RelluEssentialsNamespacedKey matchingItemKey = mock(RelluEssentialsNamespacedKey.class);
    RelluEssentialsNamespacedKey wrongTypeItemKey = mock(RelluEssentialsNamespacedKey.class);
    RelluEssentialsNamespacedKey wrongNamespaceItemKey = mock(RelluEssentialsNamespacedKey.class);

    when(matchingKey.toString()).thenReturn("namespace_a:gadget_item");
    when(wrongTypeKey.toString()).thenReturn("namespace_a:tool_item");
    when(wrongNamespaceKey.toString()).thenReturn("namespace_b:gadget_item");

    when(matchingItem.type()).thenReturn(CustomItem.Type.GADGET);
    when(matchingItem.relluEssentialsNamespacedKey()).thenReturn(matchingItemKey);
    when(matchingItemKey.getNamespace()).thenReturn("namespace_a");

    when(wrongTypeItem.type()).thenReturn(CustomItem.Type.TOOL);
    when(wrongTypeItem.relluEssentialsNamespacedKey()).thenReturn(wrongTypeItemKey);
    when(wrongTypeItemKey.getNamespace()).thenReturn("namespace_a");

    when(wrongNamespaceItem.type()).thenReturn(CustomItem.Type.GADGET);
    when(wrongNamespaceItem.relluEssentialsNamespacedKey()).thenReturn(wrongNamespaceItemKey);
    when(wrongNamespaceItemKey.getNamespace()).thenReturn("namespace_b");

    itemRegistry.register(matchingKey, matchingItem);
    itemRegistry.register(wrongTypeKey, wrongTypeItem);
    itemRegistry.register(wrongNamespaceKey, wrongNamespaceItem);

    var results = itemRegistry.getAllByTypeAndNamespace(CustomItem.Type.GADGET, "namespace_a");

    assertEquals(1, results.size());
    assertEquals(matchingItem, results.getFirst());
  }
}