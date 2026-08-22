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
}