package de.relluem94.minecraft.server.spigot.essentials.registries;

import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc.Type;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraderNpcRegistryTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private ItemService itemService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private TranslationService translationService;

  @Mock
  private CustomItem disabledItem;

  @Mock
  private CustomItem closeItem;

  @Mock
  private ItemStack disabledItemStack;

  @Mock
  private ItemStack closeItemStack;

  @Mock
  private ItemFactory itemFactory;

  @Mock
  private ItemMeta itemMeta;

  private MockedStatic<Bukkit> mockedBukkit;

  private TraderNpcRegistry traderNpcRegistry;

  @Mock
  private RelluEssentials relluEssentials;

  private MockedStatic<RelluEssentials> mockedRelluEssentials;

  @BeforeEach
  void setUp() {
    mockedBukkit = mockStatic(Bukkit.class);
    mockedRelluEssentials = mockStatic(RelluEssentials.class);
    mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentials);
    mockedBukkit.when(Bukkit::getItemFactory).thenReturn(itemFactory);
    lenient().when(relluEssentials.getName()).thenReturn("relluessentials");
    lenient().when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);
    lenient().when(itemFactory.equals(any(), any())).thenReturn(false);
    lenient().when(itemFactory.isApplicable(any(), any(ItemStack.class))).thenReturn(true);
    lenient().when(itemMeta.clone()).thenReturn(itemMeta);

    when(serviceContext.getItemService()).thenReturn(itemService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(pluginMetadataService.getName()).thenReturn("TestPlugin");
    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledItem))
        .thenReturn(Optional.of(closeItem));
    lenient().when(disabledItem.toItemStack()).thenReturn(disabledItemStack);
    lenient().when(closeItem.toItemStack()).thenReturn(closeItemStack);

    traderNpcRegistry = new TraderNpcRegistry(serviceContext);
  }

  @AfterEach
  void tearDown() {
    mockedBukkit.close();
    mockedRelluEssentials.close();
  }


  @Test
  void constructorThrowsNoSuchElementExceptionWhenDisabledItemNotFound() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.empty());

    assertThrows(Exception.class, () -> new TraderNpcRegistry(serviceContext));
  }

  @Test
  void constructorThrowsNoSuchElementExceptionWhenCloseItemNotFound() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledItem))
        .thenReturn(Optional.empty());

    assertThrows(Exception.class, () -> new TraderNpcRegistry(serviceContext));
  }

  @Test
  void getNpcsReturnsEmptyListWhenNoNpcsAdded() {
    assertTrue(traderNpcRegistry.getNpcs().isEmpty());
  }

  @Test
  void getNpcItemStackListReturnsEmptyListWhenNoNpcsAdded() {
    assertTrue(traderNpcRegistry.getNpcItemStackList().isEmpty());
  }

  @Test
  void getNpcNameListReturnsEmptyListWhenNoNpcsAdded() {
    assertTrue(traderNpcRegistry.getNpcNameList().isEmpty());
  }

  @Test
  void getNpcTraderTitleListReturnsEmptyListWhenNoNpcsAdded() {
    assertTrue(traderNpcRegistry.getNpcTraderTitleList().isEmpty());
  }

  @Test
  void addNpcIncreasesNpcListSize() {
    TraderNpc traderNpc = buildTraderNpcMock(Type.TRADER, "TraderName", "TraderTitle");

    traderNpcRegistry.addNpc(traderNpc);

    assertEquals(1, traderNpcRegistry.getNpcs().size());
  }

  @Test
  void addNpcAddsItemStackToNpcItemStackList() {
    TraderNpc traderNpc = buildTraderNpcMock(Type.TRADER, "TraderName", "TraderTitle");

    traderNpcRegistry.addNpc(traderNpc);

    assertEquals(1, traderNpcRegistry.getNpcItemStackList().size());
    assertEquals(disabledItemStack, traderNpcRegistry.getNpcItemStackList().getFirst());
  }

  @Test
  void addNpcAddsNameToNpcNameList() {
    TraderNpc traderNpc = buildTraderNpcMock(Type.TRADER, "TraderName", "TraderTitle");

    traderNpcRegistry.addNpc(traderNpc);

    assertEquals(1, traderNpcRegistry.getNpcNameList().size());
    assertEquals("TraderName", traderNpcRegistry.getNpcNameList().getFirst());
  }

  @ParameterizedTest
  @EnumSource(value = Type.class, names = {"TRADER", "ENCHANTER", "BEEKEEPER"})
  void addNpcAddsTitleToNpcTraderTitleListForTraderEnchantedAndBeekeeper(Type type) {
    TraderNpc traderNpc = buildTraderNpcMock(type, "NpcName", "NpcTitle");

    traderNpcRegistry.addNpc(traderNpc);

    assertEquals(1, traderNpcRegistry.getNpcTraderTitleList().size());
    assertEquals("NpcTitle", traderNpcRegistry.getNpcTraderTitleList().getFirst());
  }

  @ParameterizedTest
  @EnumSource(value = Type.class, names = {"TRADER", "ENCHANTER", "BEEKEEPER"}, mode = EnumSource.Mode.EXCLUDE)
  void addNpcDoesNotAddTitleToNpcTraderTitleListForNonTraderTypes(Type type) {
    TraderNpc traderNpc = buildTraderNpcMock(type, "NpcName", "NpcTitle");

    traderNpcRegistry.addNpc(traderNpc);

    assertTrue(traderNpcRegistry.getNpcTraderTitleList().isEmpty());
  }

  @Test
  void addMultipleNpcsAllFieldsAreTrackedCorrectly() {
    TraderNpc firstNpc = buildTraderNpcMock(Type.TRADER, "FirstTrader", "FirstTitle");
    TraderNpc secondNpc = buildTraderNpcMock(Type.ENCHANTER, "SecondTrader", "SecondTitle");

    traderNpcRegistry.addNpc(firstNpc);
    traderNpcRegistry.addNpc(secondNpc);

    assertEquals(2, traderNpcRegistry.getNpcs().size());
    assertEquals(2, traderNpcRegistry.getNpcItemStackList().size());
    assertEquals(2, traderNpcRegistry.getNpcNameList().size());
    assertEquals(2, traderNpcRegistry.getNpcTraderTitleList().size());
  }

  @Test
  void getNpcReturnsCorrectNpcAtIndex() {
    TraderNpc firstNpc = buildTraderNpcMock(Type.TRADER, "FirstTrader", "FirstTitle");
    TraderNpc secondNpc = buildTraderNpcMock(Type.ENCHANTER, "SecondTrader", "SecondTitle");

    traderNpcRegistry.addNpc(firstNpc);
    traderNpcRegistry.addNpc(secondNpc);

    assertEquals(firstNpc, traderNpcRegistry.getNpc(0));
    assertEquals(secondNpc, traderNpcRegistry.getNpc(1));
  }

  @Test
  void getNpcThrowsIndexOutOfBoundsExceptionWhenIndexInvalid() {
    assertThrows(IndexOutOfBoundsException.class, () -> traderNpcRegistry.getNpc(0));
  }

  @Test
  void initCreatesNpcsFromEntryList() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("TraderName", Type.TRADER, new String[0]);

    traderNpcRegistry.init(List.of(entry));

    assertEquals(1, traderNpcRegistry.getNpcs().size());
    assertNotNull(traderNpcRegistry.getNpc(0));
  }

  @Test
  void initWithEmptyListResultsInNoNpcs() {
    traderNpcRegistry.init(List.of());

    assertTrue(traderNpcRegistry.getNpcs().isEmpty());
  }

  @Test
  void initWithMultipleEntriesCreatesAllNpcs() {
    TraderNpcEntry firstEntry = buildTraderNpcEntryMock("First", Type.TRADER, new String[0]);
    TraderNpcEntry secondEntry = buildTraderNpcEntryMock("Second", Type.ENCHANTER, new String[0]);

    traderNpcRegistry.init(List.of(firstEntry, secondEntry));

    assertEquals(2, traderNpcRegistry.getNpcs().size());
  }

  @Test
  void initCreatedNpcHasCorrectName() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("ExpectedName", Type.TRADER, new String[0]);

    traderNpcRegistry.init(List.of(entry));

    assertEquals("ExpectedName", traderNpcRegistry.getNpc(0).getName());
  }

  @Test
  void initCreatedNpcHasCorrectTitle() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("NpcName", Type.TRADER, new String[0]);

    traderNpcRegistry.init(List.of(entry));

    assertEquals("§8Rellu§cEssentials§r§f§7 >> §fNpcName", traderNpcRegistry.getNpc(0).getTitle());
  }

  @Test
  void getMainGuiSetsCloseItemOnSlot53() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("NpcName", Type.TRADER, new String[0]);
    Inventory inventory = mock(Inventory.class);
    PersistentDataContainer persistentDataContainer = mock(PersistentDataContainer.class);

    mockedBukkit.when(() -> Bukkit.createInventory(any(), eq(NpcHelper.INV_SIZE), any(String.class)))
        .thenReturn(inventory);
    lenient().when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

    traderNpcRegistry.init(List.of(entry));
    traderNpcRegistry.getNpc(0).getMainGUI();

    verify(inventory).setItem(53, closeItemStack);
  }

  @Test
  void getMainGuiSetsDisabledItemsAsFiller() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("NpcName", Type.TRADER, new String[0]);
    Inventory inventory = mock(Inventory.class);
    PersistentDataContainer persistentDataContainer = mock(PersistentDataContainer.class);

    mockedBukkit.when(() -> Bukkit.createInventory(any(), eq(NpcHelper.INV_SIZE), any(String.class)))
        .thenReturn(inventory);
    when(inventory.getSize()).thenReturn(NpcHelper.INV_SIZE);
    lenient().when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

    traderNpcRegistry.init(List.of(entry));
    traderNpcRegistry.getNpc(0).getMainGUI();

    verify(inventory, times(NpcHelper.INV_SIZE)).setItem(any(Integer.class), eq(disabledItemStack));
  }


  @Test
  void getMainGuiWithNonAirSlotSetsLoreWithPrices() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("NpcName", Type.TRADER, new String[]{"STONE"});
    when(entry.getSlotName(0)).thenReturn("STONE");
    Inventory inventory = mock(Inventory.class);
    PersistentDataContainer persistentDataContainer = mock(PersistentDataContainer.class);
    ItemMeta slotItemMeta = mock(ItemMeta.class);

    mockedBukkit.when(() -> Bukkit.createInventory(any(), eq(NpcHelper.INV_SIZE), any(String.class)))
        .thenReturn(inventory);
    when(slotItemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(itemFactory.getItemMeta(eq(Material.STONE))).thenReturn(slotItemMeta);
    lenient().when(translationService.get(any(MessageKey.class), any(String.class), any(String.class), any(String.class), any(String.class))).thenReturn("price line");

    traderNpcRegistry.init(List.of(entry));
    traderNpcRegistry.getNpc(0).getMainGUI();

    verify(slotItemMeta).setLore(any());
  }

  @Test
  void getMainGuiWithNonAirSlotSetsBuyAndSellPriceInPersistentData() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("NpcName", Type.TRADER, new String[]{"STONE"});
    when(entry.getSlotName(0)).thenReturn("STONE");
    Inventory inventory = mock(Inventory.class);
    PersistentDataContainer persistentDataContainer = mock(PersistentDataContainer.class);

    mockedBukkit.when(() -> Bukkit.createInventory(any(), eq(NpcHelper.INV_SIZE), any(String.class)))
        .thenReturn(inventory);
    when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    lenient().when(translationService.get(any(MessageKey.class), any(String.class), any(String.class), any(String.class), any(String.class))).thenReturn("price line");

    traderNpcRegistry.init(List.of(entry));
    traderNpcRegistry.getNpc(0).getMainGUI();

    verify(persistentDataContainer).set(eq(itemSellPrice()), eq(PersistentDataType.INTEGER),
        eq(ItemPrice.from(Material.STONE).getSellPrice()));
    verify(persistentDataContainer).set(eq(itemBuyPrice()), eq(PersistentDataType.INTEGER),
        eq(ItemPrice.from(Material.STONE).getBuyPrice()));
  }

  @Test
  void getMainGuiWithAirSlotDoesNotSetItemForThatSlot() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("NpcName", Type.TRADER, new String[]{"AIR"});
    when(entry.getSlotName(0)).thenReturn("AIR");
    Inventory inventory = mock(Inventory.class);
    PersistentDataContainer persistentDataContainer = mock(PersistentDataContainer.class);

    mockedBukkit.when(() -> Bukkit.createInventory(any(), eq(NpcHelper.INV_SIZE), any(String.class)))
        .thenReturn(inventory);
    when(inventory.getSize()).thenReturn(NpcHelper.INV_SIZE);
    lenient().when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

    traderNpcRegistry.init(List.of(entry));
    traderNpcRegistry.getNpc(0).getMainGUI();

    verify(inventory, times(NpcHelper.INV_SIZE)).setItem(any(Integer.class), eq(disabledItemStack));
    verify(inventory).setItem(53, closeItemStack);
  }

  @Test
  void getMainGuiReturnsInventory() {
    TraderNpcEntry entry = buildTraderNpcEntryMock("NpcName", Type.TRADER, new String[0]);
    Inventory inventory = mock(Inventory.class);
    PersistentDataContainer persistentDataContainer = mock(PersistentDataContainer.class);

    mockedBukkit.when(() -> Bukkit.createInventory(any(), eq(NpcHelper.INV_SIZE), any(String.class)))
        .thenReturn(inventory);
    lenient().when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

    traderNpcRegistry.init(List.of(entry));
    Inventory result = traderNpcRegistry.getNpc(0).getMainGUI();

    assertNotNull(result);
    assertEquals(inventory, result);
  }

  private TraderNpc buildTraderNpcMock(Type type, String name, String title) {
    TraderNpc traderNpc = mock(TraderNpc.class);
    CustomItem customItem = mock(CustomItem.class);

    when(traderNpc.getType()).thenReturn(type);
    when(traderNpc.getName()).thenReturn(name);
    lenient().when(traderNpc.getTitle()).thenReturn(title);
    when(traderNpc.getCustomItem()).thenReturn(customItem);
    when(customItem.toItemStack()).thenReturn(disabledItemStack);

    return traderNpc;
  }

  private TraderNpcEntry buildTraderNpcEntryMock(String name, Type type, String[] slotNames) {
    TraderNpcEntry entry = mock(TraderNpcEntry.class);
    CustomItem customItem = mock(CustomItem.class);

    when(entry.getName()).thenReturn(name);
    when(entry.getType()).thenReturn(type);
    lenient().when(entry.getSlotNames()).thenReturn(slotNames);
    lenient().when(customItem.toItemStack()).thenReturn(disabledItemStack);

    return entry;
  }
}