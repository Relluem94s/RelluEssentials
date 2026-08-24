package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.registries.model.RegisteredInventory;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RegisteredInventoryTest {

  private Player mockPlayer;
  private RelluEssentialsNamespacedKey registryKey;
  private ItemService mockItemService;

  @BeforeEach
  void setUp() {
    mockPlayer = mock(Player.class);
    registryKey = new RelluEssentialsNamespacedKey("test", "key");
    mockItemService = mock(ItemService.class);
  }

  @Test
  void testWithFixedItemAddsToList() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, CustomItem.Type.TOOL);

    CustomItem item = mock(CustomItem.class);
    registeredInventory.withFixedItem(item);
    assertEquals(1, registeredInventory.getFixedItems().size());
    assertEquals(item, registeredInventory.getFixedItems().getFirst());
  }

  @Test
  void testGetFixedItemsIsUnmodifiable() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, CustomItem.Type.TOOL);
    CustomItem item = mock(CustomItem.class);

    registeredInventory.withFixedItem(item);
    List<CustomItem> items = registeredInventory.getFixedItems();

    assertThrows(UnsupportedOperationException.class, () -> items.add(mock(CustomItem.class)));
  }

  @Test
  void testOpenForLogicFlow() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, CustomItem.Type.TOOL);

    CustomItem extraItem = mock(CustomItem.class);
    Inventory mockInventory = mock(Inventory.class);

    try (MockedStatic<InventoryHelper> mockedInventoryHelper = mockStatic(InventoryHelper.class)) {
      mockedInventoryHelper.when(() -> InventoryHelper.getCustomItemInventory(
              any(de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory.class)))
          .thenReturn(mockInventory);

      assertDoesNotThrow(() -> registeredInventory.openFor(mockPlayer, extraItem));

      mockedInventoryHelper.verify(
          () -> InventoryHelper.openInventory(eq(mockPlayer), eq(mockInventory)));
    }
  }

  @Test
  void testOpenForWithTypeFilterLogicFlow() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, CustomItem.Type.TOOL);

    CustomItem extraItem = mock(CustomItem.class);
    Inventory mockInventory = mock(Inventory.class);

    try (MockedStatic<InventoryHelper> mockedInventoryHelper = mockStatic(InventoryHelper.class)) {
      when(mockItemService.getAllByType(CustomItem.Type.TOOL)).thenReturn(Collections.emptyList());
      mockedInventoryHelper.when(() -> InventoryHelper.getCustomItemInventory(
              any(de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory.class),
              any()))
          .thenReturn(mockInventory);

      assertDoesNotThrow(
          () -> registeredInventory.openForWithTypeFilter(mockItemService, mockPlayer, extraItem));
      mockedInventoryHelper.verify(() -> InventoryHelper.openInventory(eq(mockPlayer), eq(mockInventory)));
    }
  }

  @Test
  void testConstructorSetsFields() {
    String title = "Custom Title";
    int size = 27;
    CustomItem.Type type = CustomItem.Type.GADGET;

    RegisteredInventory registeredInventory = new RegisteredInventory(registryKey, title, size,
        type);

    assertEquals(registryKey, registeredInventory.getRelluEssentialsNamespacedKey());
    assertEquals(title, registeredInventory.getTitle());
    assertEquals(size, registeredInventory.getSize());
    assertEquals(type, registeredInventory.getItemFilter());
    assertTrue(registeredInventory.getFixedItems().isEmpty());
  }


  @Test
  void testOpenForWithoutExtraItems() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, CustomItem.Type.TOOL);
    Inventory mockInventory = mock(Inventory.class);

    try (MockedStatic<InventoryHelper> mockedInventoryHelper = mockStatic(InventoryHelper.class)) {
      mockedInventoryHelper.when(() -> InventoryHelper.getCustomItemInventory(
              any(de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory.class)))
          .thenReturn(mockInventory);

      assertDoesNotThrow(() -> registeredInventory.openFor(mockPlayer));

      mockedInventoryHelper.verify(
          () -> InventoryHelper.openInventory(eq(mockPlayer), eq(mockInventory)));
    }
  }

  @Test
  void testOpenForWithTypeFilterWithoutExtraItems() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, CustomItem.Type.TOOL);
    Inventory mockInventory = mock(Inventory.class);

    try (MockedStatic<InventoryHelper> mockedInventoryHelper = mockStatic(InventoryHelper.class)) {
      when(mockItemService.getAllByType(CustomItem.Type.TOOL)).thenReturn(Collections.emptyList());
      mockedInventoryHelper.when(() -> InventoryHelper.getCustomItemInventory(
              any(de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory.class),
              any(CustomItem.Type.class)))
          .thenReturn(mockInventory);
      assertDoesNotThrow(
          () -> registeredInventory.openForWithTypeFilter(mockItemService, mockPlayer));
      mockedInventoryHelper.verify(
          () -> InventoryHelper.openInventory(eq(mockPlayer), eq(mockInventory)));
    }
  }
}