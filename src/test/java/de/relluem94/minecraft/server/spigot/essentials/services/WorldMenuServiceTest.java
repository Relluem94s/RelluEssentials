package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorldMenuServiceTest {

  @Mock
  private ItemService itemService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private Player player;

  @Mock
  private JavaPlugin plugin;

  @Mock
  private Server server;

  @Mock
  private World world;

  @Mock
  private Inventory inventory;

  @Mock
  private ItemStack disabledItemStack;

  @Mock
  private ItemStack globeHead;

  @Mock
  private ItemMeta globeHeadMeta;

  @Mock
  private CustomItem customItem;

  private WorldMenuService worldMenuService;

  private static <T> T argThat(java.util.function.Predicate<T> predicate) {
    return org.mockito.ArgumentMatchers.argThat(predicate::test);
  }

  @BeforeEach
  void setUp() {
    worldMenuService = new WorldMenuService(itemService, pluginMetadataService);
  }

  @Test
  void openWorldMenuPlacesWorldHeadsForEachLoadedWorld() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(disabledItemStack);
    when(pluginMetadataService.getName()).thenReturn("TestPlugin");
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getWorlds()).thenReturn(List.of(world));
    when(world.getName()).thenReturn("world");
    when(globeHead.getItemMeta()).thenReturn(globeHeadMeta);

    try (MockedStatic<InventoryHelper> inventoryHelperMock = mockStatic(InventoryHelper.class);
        MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {

      inventoryHelperMock
          .when(() -> InventoryHelper.createInventory(anyInt(), anyString()))
          .thenReturn(inventory);
      inventoryHelperMock
          .when(() -> InventoryHelper.fillInventory(any(Inventory.class), any(ItemStack.class)))
          .thenReturn(inventory);
      playerHeadHelperMock
          .when(() -> PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE))
          .thenReturn(globeHead);

      worldMenuService.openWorldMenu(player);

      inventoryHelperMock.verify(() -> InventoryHelper.createInventory(eq(18),
          eq(Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dWorlds")));
      inventoryHelperMock.verify(() -> InventoryHelper.fillInventory(inventory, disabledItemStack));
      playerHeadHelperMock.verify(() -> PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE), times(1));
      verify(globeHeadMeta).setDisplayName("world");
      verify(globeHead).setItemMeta(globeHeadMeta);
      verify(inventory).setItem(0, globeHead);
      inventoryHelperMock.verify(() -> InventoryHelper.openInventory(player, inventory));
    }
  }

  @Test
  void openWorldMenuPlacesEachWorldAtCorrectInventorySlot() {
    World secondWorld = org.mockito.Mockito.mock(World.class);
    ItemStack secondGlobeHead = org.mockito.Mockito.mock(ItemStack.class);
    ItemMeta secondGlobeHeadMeta = org.mockito.Mockito.mock(ItemMeta.class);

    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(disabledItemStack);
    when(pluginMetadataService.getName()).thenReturn("TestPlugin");
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getWorlds()).thenReturn(List.of(world, secondWorld));
    when(world.getName()).thenReturn("world");
    when(secondWorld.getName()).thenReturn("world_nether");
    when(globeHead.getItemMeta()).thenReturn(globeHeadMeta);
    when(secondGlobeHead.getItemMeta()).thenReturn(secondGlobeHeadMeta);

    try (MockedStatic<InventoryHelper> inventoryHelperMock = mockStatic(InventoryHelper.class);
        MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {

      inventoryHelperMock
          .when(() -> InventoryHelper.createInventory(anyInt(), anyString()))
          .thenReturn(inventory);
      inventoryHelperMock
          .when(() -> InventoryHelper.fillInventory(any(Inventory.class), any(ItemStack.class)))
          .thenReturn(inventory);
      playerHeadHelperMock
          .when(() -> PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE))
          .thenReturn(globeHead)
          .thenReturn(secondGlobeHead);

      worldMenuService.openWorldMenu(player);

      verify(inventory).setItem(0, globeHead);
      verify(inventory).setItem(1, secondGlobeHead);
      inventoryHelperMock.verify(() -> InventoryHelper.openInventory(player, inventory));
    }
  }

  @Test
  void openWorldMenuSkipsInventoryPopulationWhenGlobeHeadMetaIsNull() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(disabledItemStack);
    when(pluginMetadataService.getName()).thenReturn("TestPlugin");
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getWorlds()).thenReturn(List.of(world));
    when(globeHead.getItemMeta()).thenReturn(null);

    try (MockedStatic<InventoryHelper> inventoryHelperMock = mockStatic(InventoryHelper.class);
        MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {

      inventoryHelperMock
          .when(() -> InventoryHelper.createInventory(anyInt(), anyString()))
          .thenReturn(inventory);
      inventoryHelperMock
          .when(() -> InventoryHelper.fillInventory(any(Inventory.class), any(ItemStack.class)))
          .thenReturn(inventory);
      playerHeadHelperMock
          .when(() -> PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE))
          .thenReturn(globeHead);

      worldMenuService.openWorldMenu(player);

      verify(inventory, never()).setItem(anyInt(), any(ItemStack.class));
      inventoryHelperMock.verify(() -> InventoryHelper.openInventory(player, inventory), never());
    }
  }

  @Test
  void openWorldMenuThrowsNoSuchElementExceptionWhenDisabledItemNotFound() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.empty());
    when(pluginMetadataService.getName()).thenReturn("TestPlugin");

    try (MockedStatic<InventoryHelper> inventoryHelperMock = mockStatic(InventoryHelper.class);
        MockedStatic<PlayerHeadHelper> ignored = mockStatic(PlayerHeadHelper.class)) {

      inventoryHelperMock
          .when(() -> InventoryHelper.createInventory(anyInt(), anyString()))
          .thenReturn(inventory);

      assertThrows(NoSuchElementException.class, () -> worldMenuService.openWorldMenu(player));
    }
  }

  @Test
  void openWorldMenuOpensInventoryWithNoWorldsWhenServerHasNoWorlds() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(disabledItemStack);
    when(pluginMetadataService.getName()).thenReturn("TestPlugin");
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getWorlds()).thenReturn(List.of());

    try (MockedStatic<InventoryHelper> inventoryHelperMock = mockStatic(InventoryHelper.class);
        MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {

      inventoryHelperMock
          .when(() -> InventoryHelper.createInventory(anyInt(), anyString()))
          .thenReturn(inventory);
      inventoryHelperMock
          .when(() -> InventoryHelper.fillInventory(any(Inventory.class), any(ItemStack.class)))
          .thenReturn(inventory);

      worldMenuService.openWorldMenu(player);

      playerHeadHelperMock.verify(() -> PlayerHeadHelper.getCustomSkull(any()), never());
      verify(inventory, never()).setItem(anyInt(), any(ItemStack.class));
      inventoryHelperMock.verify(() -> InventoryHelper.openInventory(player, inventory));
    }
  }

  @Test
  void openWorldMenuUsesCorrectNamespacedKeyForDisabledItem() {
    when(pluginMetadataService.getName()).thenReturn("TestPlugin");
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.empty());

    try (MockedStatic<InventoryHelper> inventoryHelperMock = mockStatic(InventoryHelper.class);
        MockedStatic<PlayerHeadHelper> ignored = mockStatic(PlayerHeadHelper.class)) {

      inventoryHelperMock
          .when(() -> InventoryHelper.createInventory(anyInt(), anyString()))
          .thenReturn(inventory);

      assertThrows(NoSuchElementException.class, () -> worldMenuService.openWorldMenu(player));

      verify(itemService).find(argThat(key -> "TestPlugin".equals(key.getNamespace())
          && PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED.equals(key.getKey())));
    }
  }
}