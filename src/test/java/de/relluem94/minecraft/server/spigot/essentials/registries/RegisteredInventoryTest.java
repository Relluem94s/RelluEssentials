package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RegisteredInventoryTest {

  private Player mockPlayer;
  private RegistryKey registryKey;

  @BeforeEach
  void setUp() {
    mockPlayer = mock(Player.class);
    registryKey = new RegistryKey("test", "key");
  }

  @Test
  void testWithFixedItemAddsToList() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, ItemHelper.Type.TOOL);

    ItemHelper item = mock(ItemHelper.class);

    registeredInventory.withFixedItem(item);

    assertEquals(1, registeredInventory.getFixedItems().size());
    assertEquals(item, registeredInventory.getFixedItems().get(0));
  }

  @Test
  void testGetFixedItemsIsUnmodifiable() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, ItemHelper.Type.TOOL);
    ItemHelper item = mock(ItemHelper.class);

    registeredInventory.withFixedItem(item);
    List<ItemHelper> items = registeredInventory.getFixedItems();

    assertThrows(UnsupportedOperationException.class, () -> items.add(mock(ItemHelper.class)));
  }

  @Test
  void testOpenForLogicFlow() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, ItemHelper.Type.TOOL);

    ItemHelper extraItem = mock(ItemHelper.class);
    Inventory mockInventory = mock(Inventory.class);

    try (MockedStatic<InventoryHelper> mockedInventoryHelper = mockStatic(InventoryHelper.class)) {
      mockedInventoryHelper.when(() -> InventoryHelper.getCustomItemInventory(any(de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory.class)))
          .thenReturn(mockInventory);

      assertDoesNotThrow(() -> registeredInventory.openFor(mockPlayer, extraItem));

      mockedInventoryHelper.verify(() -> InventoryHelper.openInventory(eq(mockPlayer), eq(mockInventory)));
    }
  }

  @Test
  void testOpenForWithTypeFilterLogicFlow() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, ItemHelper.Type.TOOL);

    ItemHelper extraItem = mock(ItemHelper.class);
    Inventory mockInventory = mock(Inventory.class);

    try (MockedStatic<InventoryHelper> mockedInventoryHelper = mockStatic(InventoryHelper.class);
        MockedStatic<ItemRegistry> mockedItemRegistry = mockStatic(ItemRegistry.class)) {

      mockedItemRegistry.when(() -> ItemRegistry.getAllByType(any(ItemHelper.Type.class)))
          .thenReturn(Collections.emptyList());

      mockedInventoryHelper.when(() -> InventoryHelper.getCustomItemInventory(any(de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory.class), any(ItemHelper.Type.class)))
          .thenReturn(mockInventory);

      assertDoesNotThrow(() -> registeredInventory.openForWithTypeFilter(mockPlayer, extraItem));

      mockedInventoryHelper.verify(() -> InventoryHelper.openInventory(eq(mockPlayer), eq(mockInventory)));
    }
  }

  @Test
  void testConstructorSetsFields() {
    String title = "Custom Title";
    int size = 27;
    ItemHelper.Type type = ItemHelper.Type.GADGET;

    RegisteredInventory registeredInventory = new RegisteredInventory(registryKey, title, size, type);

    assertEquals(registryKey, registeredInventory.getRegistryKey());
    assertEquals(title, registeredInventory.getTitle());
    assertEquals(size, registeredInventory.getSize());
    assertEquals(type, registeredInventory.getItemFilter());
    assertTrue(registeredInventory.getFixedItems().isEmpty());
  }


  @Test
  void testOpenForWithoutExtraItems() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, ItemHelper.Type.TOOL);
    Inventory mockInventory = mock(Inventory.class);

    try (MockedStatic<InventoryHelper> mockedInventoryHelper = mockStatic(InventoryHelper.class)) {
      mockedInventoryHelper.when(() -> InventoryHelper.getCustomItemInventory(any(de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory.class)))
          .thenReturn(mockInventory);

      assertDoesNotThrow(() -> registeredInventory.openFor(mockPlayer));

      mockedInventoryHelper.verify(() -> InventoryHelper.openInventory(eq(mockPlayer), eq(mockInventory)));
    }
  }

  @Test
  void testOpenForWithTypeFilterWithoutExtraItems() {
    RegisteredInventory registeredInventory = new RegisteredInventory(
        registryKey, "Test GUI", 9, ItemHelper.Type.TOOL);
    Inventory mockInventory = mock(Inventory.class);

    try (MockedStatic<InventoryHelper> mockedInventoryHelper = mockStatic(InventoryHelper.class);
        MockedStatic<ItemRegistry> mockedItemRegistry = mockStatic(ItemRegistry.class)) {

      mockedItemRegistry.when(() -> ItemRegistry.getAllByType(any(ItemHelper.Type.class)))
          .thenReturn(Collections.emptyList());

      mockedInventoryHelper.when(() -> InventoryHelper.getCustomItemInventory(any(de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory.class), any(ItemHelper.Type.class)))
          .thenReturn(mockInventory);

      assertDoesNotThrow(() -> registeredInventory.openForWithTypeFilter(mockPlayer));

      mockedInventoryHelper.verify(() -> InventoryHelper.openInventory(eq(mockPlayer), eq(mockInventory)));
    }
  }
}