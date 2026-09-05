package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

  @Mock
  private ItemRegistry itemRegistry;

  @InjectMocks
  private ItemService itemService;

  @SuppressWarnings("DataFlowIssue")
  @Test
  void constructorThrowsNullPointerExceptionWhenItemRegistryIsNull() {
    assertThrows(NullPointerException.class, () -> new ItemService(null));
  }

  @Test
  void registerDelegatesToItemRegistry() {
    CustomItem customItem = mock(CustomItem.class);
    RelluEssentialsNamespacedKey key = mock(RelluEssentialsNamespacedKey.class);
    when(customItem.relluEssentialsNamespacedKey()).thenReturn(key);

    itemService.register(customItem);

    verify(itemRegistry).register(key, customItem);
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void registerThrowsNullPointerExceptionWhenCustomItemIsNull() {
    assertThrows(NullPointerException.class, () -> itemService.register(null));
  }

  @Test
  void findReturnsItemWhenKeyExists() {
    RelluEssentialsNamespacedKey key = mock(RelluEssentialsNamespacedKey.class);
    CustomItem customItem = mock(CustomItem.class);
    when(itemRegistry.find(key)).thenReturn(Optional.of(customItem));

    Optional<CustomItem> result = itemService.find(key);

    assertTrue(result.isPresent());
    assertEquals(customItem, result.get());
  }

  @Test
  void findReturnsEmptyWhenKeyDoesNotExist() {
    RelluEssentialsNamespacedKey key = mock(RelluEssentialsNamespacedKey.class);
    when(itemRegistry.find(key)).thenReturn(Optional.empty());

    Optional<CustomItem> result = itemService.find(key);

    assertFalse(result.isPresent());
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void findThrowsNullPointerExceptionWhenKeyIsNull() {
    assertThrows(NullPointerException.class, () -> itemService.find(null));
  }

  @Test
  void findByItemStackReturnsItemWhenItemStackMatches() {
    ItemStack itemStack = mock(ItemStack.class);
    CustomItem customItem = mock(CustomItem.class);
    when(itemRegistry.findByItemStack(itemStack)).thenReturn(Optional.of(customItem));

    Optional<CustomItem> result = itemService.findByItemStack(itemStack);

    assertTrue(result.isPresent());
    assertEquals(customItem, result.get());
  }

  @Test
  void findByItemStackReturnsEmptyWhenNoMatch() {
    ItemStack itemStack = mock(ItemStack.class);
    when(itemRegistry.findByItemStack(itemStack)).thenReturn(Optional.empty());

    Optional<CustomItem> result = itemService.findByItemStack(itemStack);

    assertFalse(result.isPresent());
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void findByItemStackThrowsNullPointerExceptionWhenItemStackIsNull() {
    assertThrows(NullPointerException.class, () -> itemService.findByItemStack(null));
  }

  @Test
  void isItemStackReturnsTrueWhenItemStackMatchesRegisteredItem() {
    RelluEssentialsNamespacedKey identifier = mock(RelluEssentialsNamespacedKey.class);
    ItemStack itemStack = mock(ItemStack.class);
    ItemStack registeredItemStack = mock(ItemStack.class);
    CustomItem customItem = mock(CustomItem.class);

    when(itemRegistry.find(identifier)).thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(registeredItemStack);
    when(registeredItemStack.isSimilar(itemStack)).thenReturn(true);

    boolean result = itemService.isItemStack(identifier, itemStack);

    assertTrue(result);
  }

  @Test
  void isItemStackReturnsFalseWhenItemStackDoesNotMatchRegisteredItem() {
    RelluEssentialsNamespacedKey identifier = mock(RelluEssentialsNamespacedKey.class);
    ItemStack itemStack = mock(ItemStack.class);
    ItemStack registeredItemStack = mock(ItemStack.class);
    CustomItem customItem = mock(CustomItem.class);

    when(itemRegistry.find(identifier)).thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(registeredItemStack);
    when(registeredItemStack.isSimilar(itemStack)).thenReturn(false);

    boolean result = itemService.isItemStack(identifier, itemStack);

    assertFalse(result);
  }

  @Test
  void isItemStackReturnsFalseWhenIdentifierNotRegistered() {
    RelluEssentialsNamespacedKey identifier = mock(RelluEssentialsNamespacedKey.class);
    ItemStack itemStack = mock(ItemStack.class);

    when(itemRegistry.find(identifier)).thenReturn(Optional.empty());

    boolean result = itemService.isItemStack(identifier, itemStack);

    assertFalse(result);
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void isItemStackThrowsNullPointerExceptionWhenIdentifierIsNull() {
    ItemStack itemStack = mock(ItemStack.class);
    assertThrows(NullPointerException.class, () -> itemService.isItemStack(null, itemStack));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void isItemStackThrowsNullPointerExceptionWhenItemStackIsNull() {
    RelluEssentialsNamespacedKey identifier = mock(RelluEssentialsNamespacedKey.class);
    assertThrows(NullPointerException.class, () -> itemService.isItemStack(identifier, null));
  }

  @Test
  void getAllByTypeReturnsFilteredItems() {
    CustomItem.Type type = CustomItem.Type.values()[0];
    List<CustomItem> expectedItems = List.of(mock(CustomItem.class));
    when(itemRegistry.getAllByType(type)).thenReturn(expectedItems);

    List<CustomItem> result = itemService.getAllByType(type);

    assertAll(
        () -> assertEquals(expectedItems.size(), result.size()),
        () -> assertEquals(expectedItems, result)
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void getAllByTypeThrowsNullPointerExceptionWhenTypeIsNull() {
    assertThrows(NullPointerException.class, () -> itemService.getAllByType(null));
  }

  @Test
  void getAllByTypeAndNamespaceReturnsFilteredItems() {
    CustomItem.Type type = CustomItem.Type.values()[0];
    String namespace = "test_namespace";
    List<CustomItem> expectedItems = List.of(mock(CustomItem.class));
    when(itemRegistry.getAllByTypeAndNamespace(type, namespace)).thenReturn(expectedItems);

    List<CustomItem> result = itemService.getAllByTypeAndNamespace(type, namespace);

    assertAll(
        () -> assertEquals(expectedItems.size(), result.size()),
        () -> assertEquals(expectedItems, result)
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void getAllByTypeAndNamespaceThrowsNullPointerExceptionWhenTypeIsNull() {
    assertThrows(NullPointerException.class,
        () -> itemService.getAllByTypeAndNamespace(null, "namespace"));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void getAllByTypeAndNamespaceThrowsNullPointerExceptionWhenNamespaceIsNull() {
    CustomItem.Type type = CustomItem.Type.values()[0];
    assertThrows(NullPointerException.class,
        () -> itemService.getAllByTypeAndNamespace(type, null));
  }

  @Test
  void getAllReturnsAllRegisteredItems() {
    Map<String, CustomItem> expectedItems = Map.of("key", mock(CustomItem.class));
    when(itemRegistry.getAll()).thenReturn(expectedItems);

    Map<String, CustomItem> result = itemService.getAll();

    assertAll(
        () -> assertEquals(expectedItems.size(), result.size()),
        () -> assertEquals(expectedItems, result)
    );
  }

  @Test
  void hasKeyReturnsTrueWhenKeyExistsInItemMeta() {
    NamespacedKey key = mock(NamespacedKey.class);
    ItemStack itemStack = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer persistentDataContainer = mock(PersistentDataContainer.class);
    PersistentDataType<?, ?> persistentDataType = PersistentDataType.STRING;

    when(itemStack.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(persistentDataContainer.has(key, persistentDataType)).thenReturn(true);

    boolean result = itemService.hasKey(key, itemStack, persistentDataType);

    assertTrue(result);
  }

  @Test
  void hasKeyReturnsFalseWhenKeyDoesNotExistInItemMeta() {
    NamespacedKey key = mock(NamespacedKey.class);
    ItemStack itemStack = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer persistentDataContainer = mock(PersistentDataContainer.class);
    PersistentDataType<?, ?> persistentDataType = PersistentDataType.STRING;

    when(itemStack.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(persistentDataContainer.has(key, persistentDataType)).thenReturn(false);

    boolean result = itemService.hasKey(key, itemStack, persistentDataType);

    assertFalse(result);
  }

  @Test
  void hasKeyReturnsFalseWhenItemMetaIsNull() {
    NamespacedKey key = mock(NamespacedKey.class);
    ItemStack itemStack = mock(ItemStack.class);
    PersistentDataType<?, ?> persistentDataType = PersistentDataType.STRING;

    when(itemStack.getItemMeta()).thenReturn(null);

    boolean result = itemService.hasKey(key, itemStack, persistentDataType);

    assertFalse(result);
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void hasKeyThrowsNullPointerExceptionWhenKeyIsNull() {
    ItemStack itemStack = mock(ItemStack.class);
    assertThrows(NullPointerException.class,
        () -> itemService.hasKey(null, itemStack, PersistentDataType.STRING));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void hasKeyThrowsNullPointerExceptionWhenItemStackIsNull() {
    NamespacedKey key = mock(NamespacedKey.class);
    assertThrows(NullPointerException.class,
        () -> itemService.hasKey(key, null, PersistentDataType.STRING));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void hasKeyThrowsNullPointerExceptionWhenPersistentDataTypeIsNull() {
    NamespacedKey key = mock(NamespacedKey.class);
    ItemStack itemStack = mock(ItemStack.class);
    assertThrows(NullPointerException.class,
        () -> itemService.hasKey(key, itemStack, null));
  }
}