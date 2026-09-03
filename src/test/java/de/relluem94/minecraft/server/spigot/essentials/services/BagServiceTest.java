package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper.BAG_SIZE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BagRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BagTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagTypeRepository;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BagServiceTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private BagRegistry bagRegistry;
  @Mock
  private BagRepository bagRepository;
  @Mock
  private BagTypeRegistry bagTypeRegistry;
  @Mock
  private BagTypeRepository bagTypeRepository;
  @Mock
  private ChatService chatService;
  @Mock
  private TranslationService translationService;
  @Mock
  private ItemService itemService;
  @Mock
  private PluginMetadataService pluginMetadataService;
  @Mock
  private PlayerEntry playerEntry;
  @Mock
  private BagTypeEntry bagTypeEntry;
  @Mock
  private BagEntry bagEntry;
  @Mock
  private GroupEntry adminGroup;

  private BagService bagService;

  @Mock
  private Server server;
  @Mock
  private ItemFactory itemFactory;
  @Mock
  private Inventory mockInventory;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    lenient().when(server.getItemFactory()).thenReturn(itemFactory);
    lenient().when(server.createInventory(isNull(), anyInt(), anyString())).thenReturn(mockInventory);
    lenient().when(mockInventory.getSize()).thenReturn(54);

    Field serverField = Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, server);

    when(bagTypeRepository.findAll()).thenReturn(new ArrayList<>());
    when(bagRepository.findAll()).thenReturn(new ArrayList<>());
    when(bagTypeRegistry.getAll()).thenReturn(new ArrayList<>());

    bagService = new BagService(
        serviceContext,
        bagRegistry,
        bagRepository,
        bagTypeRegistry,
        bagTypeRepository
    );
  }

  @Test
  void findBagReturnsPresentOptionalWhenBagExists() {
    when(bagRegistry.findByPlayerIdAndBagTypeId(1, 2)).thenReturn(Optional.of(bagEntry));

    Optional<BagEntry> result = bagService.findBag(1, 2);

    assertTrue(result.isPresent());
    assertEquals(bagEntry, result.get());
  }

  @Test
  void findBagReturnsEmptyOptionalWhenBagDoesNotExist() {
    when(bagRegistry.findByPlayerIdAndBagTypeId(1, 2)).thenReturn(Optional.empty());

    Optional<BagEntry> result = bagService.findBag(1, 2);

    assertTrue(result.isEmpty());
  }

  @Test
  void findBagsReturnsCollectionForPlayer() {
    List<BagEntry> bags = List.of(bagEntry);
    when(bagRegistry.findAllByPlayerId(1)).thenReturn(bags);

    Collection<BagEntry> result = bagService.findBags(1);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(1, result.size()),
        () -> assertTrue(result.contains(bagEntry))
    );
  }

  @Test
  void findBagsReturnsEmptyCollectionWhenNoBagsForPlayer() {
    when(bagRegistry.findAllByPlayerId(1)).thenReturn(new ArrayList<>());

    Collection<BagEntry> result = bagService.findBags(1);

    assertTrue(result.isEmpty());
  }

  @Test
  void findBagTypeByPartialNameReturnsPresentOptionalWhenFound() {
    when(bagTypeRegistry.findByPartialName("stone")).thenReturn(Optional.of(bagTypeEntry));

    Optional<BagTypeEntry> result = bagService.findBagTypeByPartialName("stone");

    assertTrue(result.isPresent());
    assertEquals(bagTypeEntry, result.get());
  }

  @Test
  void findBagTypeByPartialNameReturnsEmptyOptionalWhenNotFound() {
    when(bagTypeRegistry.findByPartialName("unknown")).thenReturn(Optional.empty());

    Optional<BagTypeEntry> result = bagService.findBagTypeByPartialName("unknown");

    assertTrue(result.isEmpty());
  }

  @Test
  void findBagTypeByIdReturnsPresentOptionalWhenFound() {
    when(bagTypeRegistry.findById(5)).thenReturn(Optional.of(bagTypeEntry));

    Optional<BagTypeEntry> result = bagService.findBagTypeById(5);

    assertTrue(result.isPresent());
    assertEquals(bagTypeEntry, result.get());
  }

  @Test
  void findBagTypeByIdReturnsEmptyOptionalWhenNotFound() {
    when(bagTypeRegistry.findById(99)).thenReturn(Optional.empty());

    Optional<BagTypeEntry> result = bagService.findBagTypeById(99);

    assertTrue(result.isEmpty());
  }

  @Test
  void hasBagReturnsTrueWhenPlayerOwnsBagOfType() {
    when(bagRegistry.existsByPlayerIdAndBagTypeId(1, 2)).thenReturn(true);

    boolean result = bagService.hasBag(1, 2);

    assertTrue(result);
  }

  @Test
  void hasBagReturnsFalseWhenPlayerDoesNotOwnBagOfType() {
    when(bagRegistry.existsByPlayerIdAndBagTypeId(1, 2)).thenReturn(false);

    boolean result = bagService.hasBag(1, 2);

    assertFalse(result);
  }

  @Test
  void hasBagWithPlayerEntryReturnsTrueWhenPlayerOwnsBagOfType() {
    when(playerEntry.getId()).thenReturn(1);
    when(bagRegistry.existsByPlayerIdAndBagTypeId(1, 2)).thenReturn(true);

    boolean result = bagService.hasBag(2, playerEntry);

    assertTrue(result);
  }

  @Test
  void hasBagWithPlayerEntryReturnsFalseWhenPlayerDoesNotOwnBagOfType() {
    when(playerEntry.getId()).thenReturn(1);
    when(bagRegistry.existsByPlayerIdAndBagTypeId(1, 2)).thenReturn(false);

    boolean result = bagService.hasBag(2, playerEntry);

    assertFalse(result);
  }

  @Test
  void hasBagsReturnsTrueWhenPlayerOwnsAtLeastOneBag() {
    when(bagRegistry.existsByPlayerId(1)).thenReturn(true);

    boolean result = bagService.hasBags(1);

    assertTrue(result);
  }

  @Test
  void hasBagsReturnsFalseWhenPlayerOwnsNoBags() {
    when(bagRegistry.existsByPlayerId(1)).thenReturn(false);

    boolean result = bagService.hasBags(1);

    assertFalse(result);
  }


  @Test
  void getSlotByItemStackReturnsNegativeOneWhenItemNotInBagType() {
    ItemStack queriedItem = new ItemStack(Material.STONE, 5);
    BagTypeEntry emptyBagType = mock(BagTypeEntry.class);
    when(bagEntry.getBagType()).thenReturn(emptyBagType);

    for (int i = 0; i < BAG_SIZE; i++) {
      when(emptyBagType.getSlotName(i)).thenReturn(null);
    }

    when(serviceContext.getItemService()).thenReturn(itemService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("relluessentials");

    CustomItem disabledItemEntry = mock(CustomItem.class);
    ItemStack disabledItemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    when(disabledItemEntry.toItemStack()).thenReturn(disabledItemStack);
    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledItemEntry));

    int result = bagService.getSlotByItemStack(bagEntry, queriedItem);

    assertEquals(-1, result);
  }

  @Test
  void getSlotByItemStackReturnsCorrectSlotWhenItemMatchesSlot() {
    BagTypeEntry typedBagType = mock(BagTypeEntry.class);
    when(bagEntry.getBagType()).thenReturn(typedBagType);

    for (int i = 0; i < BAG_SIZE; i++) {
      if (i == 3) {
        when(typedBagType.getSlotName(i)).thenReturn("STONE");
      } else {
        when(typedBagType.getSlotName(i)).thenReturn(null);
      }
    }

    when(serviceContext.getItemService()).thenReturn(itemService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("relluessentials");

    CustomItem disabledItemEntry = mock(CustomItem.class);
    ItemStack disabledItemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    when(disabledItemEntry.toItemStack()).thenReturn(disabledItemStack);
    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledItemEntry));

    when(itemFactory.getItemMeta(any(Material.class))).thenReturn(null);
    doReturn(true).when(itemFactory).equals(isNull(), isNull());

    ItemStack stoneItem = new ItemStack(Material.STONE, 1);
    int result = bagService.getSlotByItemStack(bagEntry, stoneItem);

    assertEquals(3, result);
  }

  @Test
  void collectItemsSkipsItemsNotInBagBlocks() {
    Item droppedItem = mock(Item.class);
    ItemStack itemStack = new ItemStack(Material.DIAMOND, 1);
    when(droppedItem.getItemStack()).thenReturn(itemStack);

    Player player = mock(Player.class);

    List<Item> result = bagService.collectItems(List.of(droppedItem), player, playerEntry);

    assertTrue(result.isEmpty());
  }

  @Test
  void collectItemsCollectsMatchingItemsIntoCorrectBagSlot() {
    BagTypeEntry typedBagType = mock(BagTypeEntry.class);
    BagEntry matchingBagEntry = mock(BagEntry.class);
    when(matchingBagEntry.getBagType()).thenReturn(typedBagType);

    for (int i = 0; i < BAG_SIZE; i++) {
      if (i == 0) {
        when(typedBagType.getSlotName(i)).thenReturn("STONE");
      } else {
        when(typedBagType.getSlotName(i)).thenReturn(null);
      }
    }

    when(playerEntry.getId()).thenReturn(1);
    when(bagRegistry.findAllByPlayerId(1)).thenReturn(List.of(matchingBagEntry));
    when(matchingBagEntry.getSlotValue(0)).thenReturn(10);

    Item droppedItem = mock(Item.class);
    ItemStack itemStack = new ItemStack(Material.STONE, 5);
    when(droppedItem.getItemStack()).thenReturn(itemStack);
    when(droppedItem.getName()).thenReturn("stone");

    Player player = mock(Player.class);
    when(serviceContext.getChatService()).thenReturn(chatService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_BAG_COLLECT), anyInt(), anyString()))
        .thenReturn("collected");

    BagService serviceWithBlocks = buildServiceWithBagTypeBlocks(typedBagType);

    List<Item> result = serviceWithBlocks.collectItems(List.of(droppedItem), player, playerEntry);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertTrue(result.contains(droppedItem))
    );
    verify(matchingBagEntry).setSlotValue(0, 15);
    verify(matchingBagEntry).setHasToBeUpdated(true);
    verify(player).playSound(player, "entity.item.pickup", SoundCategory.PLAYERS, 0.5F, 1);
  }

  @Test
  void collectItemStacksSkipsItemsNotInBagBlocks() {
    ItemStack itemStack = new ItemStack(Material.DIAMOND, 3);
    Player player = mock(Player.class);

    List<ItemStack> result = bagService.collectItemStacks(List.of(itemStack), player, playerEntry);

    assertTrue(result.isEmpty());
  }

  @Test
  void collectItemStacksCollectsMatchingItemStacks() {
    BagTypeEntry typedBagType = mock(BagTypeEntry.class);
    BagEntry matchingBagEntry = mock(BagEntry.class);
    when(matchingBagEntry.getBagType()).thenReturn(typedBagType);

    for (int i = 0; i < BAG_SIZE; i++) {
      if (i == 0) {
        when(typedBagType.getSlotName(i)).thenReturn("STONE");
      } else {
        when(typedBagType.getSlotName(i)).thenReturn(null);
      }
    }

    when(playerEntry.getId()).thenReturn(1);
    when(bagRegistry.findAllByPlayerId(1)).thenReturn(List.of(matchingBagEntry));
    when(matchingBagEntry.getSlotValue(0)).thenReturn(5);

    ItemStack itemStack = new ItemStack(Material.STONE, 3);
    Player player = mock(Player.class);
    when(serviceContext.getChatService()).thenReturn(chatService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_BAG_COLLECT), anyInt(), anyString()))
        .thenReturn("collected");

    BagService serviceWithBlocks = buildServiceWithBagTypeBlocks(typedBagType);

    List<ItemStack> result = serviceWithBlocks.collectItemStacks(List.of(itemStack), player, playerEntry);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertTrue(result.contains(itemStack))
    );
    verify(matchingBagEntry).setSlotValue(0, 8);
    verify(matchingBagEntry).setHasToBeUpdated(true);
  }

  @Test
  void collectItemReturnsFalseWhenItemNotInBagBlocks() {
    Item droppedItem = mock(Item.class);
    ItemStack itemStack = new ItemStack(Material.DIAMOND, 1);
    when(droppedItem.getItemStack()).thenReturn(itemStack);
    Player player = mock(Player.class);

    boolean result = bagService.collectItem(droppedItem, player, playerEntry);

    assertFalse(result);
  }

  @Test
  void collectItemReturnsFalseWhenNoMatchingBagSlot() {
    BagTypeEntry typedBagType = mock(BagTypeEntry.class);

    for (int i = 0; i < BAG_SIZE; i++) {
      when(typedBagType.getSlotName(i)).thenReturn("STONE");
    }

    Item droppedItem = mock(Item.class);
    ItemStack diamondStack = new ItemStack(Material.DIAMOND, 2);
    when(droppedItem.getItemStack()).thenReturn(diamondStack);
    Player player = mock(Player.class);

    BagService serviceWithBlocks = buildServiceWithBagTypeBlocks(typedBagType);

    boolean result = serviceWithBlocks.collectItem(droppedItem, player, playerEntry);

    assertFalse(result);
  }

  @Test
  void collectItemReturnsTrueAndUpdatesBagWhenItemMatches() {
    BagTypeEntry typedBagType = mock(BagTypeEntry.class);
    BagEntry matchingBagEntry = mock(BagEntry.class);
    when(matchingBagEntry.getBagType()).thenReturn(typedBagType);

    for (int i = 0; i < BAG_SIZE; i++) {
      if (i == 0) {
        when(typedBagType.getSlotName(i)).thenReturn("STONE");
      } else {
        when(typedBagType.getSlotName(i)).thenReturn(null);
      }
    }

    when(playerEntry.getId()).thenReturn(1);
    when(bagRegistry.findAllByPlayerId(1)).thenReturn(List.of(matchingBagEntry));
    when(matchingBagEntry.getSlotValue(0)).thenReturn(0);

    Item droppedItem = mock(Item.class);
    ItemStack stoneStack = new ItemStack(Material.STONE, 4);
    when(droppedItem.getItemStack()).thenReturn(stoneStack);
    when(droppedItem.getName()).thenReturn("stone");

    Player player = mock(Player.class);
    when(serviceContext.getChatService()).thenReturn(chatService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_BAG_COLLECT), anyInt(), anyString()))
        .thenReturn("collected");

    BagService serviceWithBlocks = buildServiceWithBagTypeBlocks(typedBagType);

    boolean result = serviceWithBlocks.collectItem(droppedItem, player, playerEntry);

    assertTrue(result);
    verify(matchingBagEntry).setSlotValue(0, 4);
    verify(matchingBagEntry).setHasToBeUpdated(true);
  }

  @Test
  void savePendingBagUpdatesSkipsBagEntriesWithNoPendingUpdate() {
    BagEntry nonUpdatedEntry = mock(BagEntry.class);
    when(nonUpdatedEntry.isHasToBeUpdated()).thenReturn(false);
    when(bagRegistry.findAll()).thenReturn(List.of(nonUpdatedEntry));

    bagService.savePendingBagUpdates(adminGroup);

    verify(bagRepository, never()).update(any());
    verify(chatService, never()).sendMessageInChannel(anyString(), anyString(), anyString(), any());
  }

  @Test
  void savePendingBagUpdatesSkipsNullBagEntries() {
    List<BagEntry> listWithNull = new ArrayList<>();
    listWithNull.add(null);
    when(bagRegistry.findAll()).thenReturn(listWithNull);

    bagService.savePendingBagUpdates(adminGroup);

    verify(bagRepository, never()).update(any());
  }

  @Test
  void savePendingBagUpdatesPersistsAndNotifiesWhenPendingUpdatesExist() {
    BagEntry pendingEntry = mock(BagEntry.class);
    when(pendingEntry.isHasToBeUpdated()).thenReturn(true);
    when(bagRegistry.findAll()).thenReturn(List.of(pendingEntry));
    when(serviceContext.getChatService()).thenReturn(chatService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(eq(MessageKey.PLUGIN_BAGS_SAVED), anyInt())).thenReturn("saved");

    bagService.savePendingBagUpdates(adminGroup);

    verify(bagRepository).update(pendingEntry);
    verify(pendingEntry).setHasToBeUpdated(false);
    verify(chatService).sendMessageInChannel(anyString(), anyString(), anyString(), eq(adminGroup));
  }

  @Test
  void getBagTypeNamesForPlayerReturnsLowercaseNames() {
    BagEntry entryOne = mock(BagEntry.class);
    BagTypeEntry typeOne = mock(BagTypeEntry.class);
    when(entryOne.getBagType()).thenReturn(typeOne);
    when(typeOne.getName()).thenReturn("StoneBAG");

    when(bagRegistry.findAllByPlayerId(1)).thenReturn(List.of(entryOne));

    List<String> result = bagService.getBagTypeNamesForPlayer(1);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals("stonebag", result.getFirst())
    );
  }

  @Test
  void getBagTypeNamesForPlayerReturnsEmptyListWhenNoBags() {
    when(bagRegistry.findAllByPlayerId(1)).thenReturn(new ArrayList<>());

    List<String> result = bagService.getBagTypeNamesForPlayer(1);

    assertTrue(result.isEmpty());
  }

  @Test
  void getItemStacksReturnsArrayOfSizeBagSize() {
    BagTypeEntry typedBagType = mock(BagTypeEntry.class);
    for (int i = 0; i < BAG_SIZE; i++) {
      when(typedBagType.getSlotName(i)).thenReturn(null);
    }

    when(serviceContext.getItemService()).thenReturn(itemService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("relluessentials");

    CustomItem disabledItemEntry = mock(CustomItem.class);
    ItemStack disabledItemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    when(disabledItemEntry.toItemStack()).thenReturn(disabledItemStack);
    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledItemEntry));

    ItemStack[] result = bagService.getItemStacks(typedBagType);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(BAG_SIZE, result.length)
    );
  }

  @Test
  void getItemStacksReturnsItemStackWithMaterialWhenSlotNameIsValid() {
    BagTypeEntry typedBagType = mock(BagTypeEntry.class);
    for (int i = 0; i < BAG_SIZE; i++) {
      if (i == 0) {
        when(typedBagType.getSlotName(i)).thenReturn("STONE");
      } else {
        when(typedBagType.getSlotName(i)).thenReturn(null);
      }
    }

    when(serviceContext.getItemService()).thenReturn(itemService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("relluessentials");

    CustomItem disabledItemEntry = mock(CustomItem.class);
    ItemStack disabledItemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    when(disabledItemEntry.toItemStack()).thenReturn(disabledItemStack);
    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledItemEntry));

    ItemStack[] result = bagService.getItemStacks(typedBagType);

    assertAll(
        () -> assertEquals(Material.STONE, result[0].getType()),
        () -> assertEquals(1, result[0].getAmount())
    );
  }

  @Test
  void getItemStacksReturnsAirMaterialWhenSlotNameIsInvalidMaterial() {
    BagTypeEntry typedBagType = mock(BagTypeEntry.class);
    for (int i = 0; i < BAG_SIZE; i++) {
      when(typedBagType.getSlotName(i)).thenReturn("NOT_A_REAL_MATERIAL_XYZ");
    }

    ItemStack[] result = bagService.getItemStacks(typedBagType);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(BAG_SIZE, result.length),
        () -> assertEquals(Material.AIR, result[0].getType())
    );
  }

  @Test
  void getBagInventoryReturnsNullWhenPlayerDoesNotOwnBag() {
    when(playerEntry.getId()).thenReturn(1);
    when(bagRegistry.findByPlayerIdAndBagTypeId(1, 5)).thenReturn(Optional.empty());

    var result = bagService.getBagInventory(5, playerEntry);

    assertNull(result);
  }

  @Test
  void getBagInventoryReturnsPopulatedInventoryWhenPlayerOwnsBag() {
    when(playerEntry.getId()).thenReturn(1);

    BagTypeEntry typedBagType = mock(BagTypeEntry.class);
    BagEntry ownedBagEntry = mock(BagEntry.class);
    when(ownedBagEntry.getBagType()).thenReturn(typedBagType);
    when(typedBagType.getDisplayName()).thenReturn("StoneBag");

    for (int i = 0; i < BAG_SIZE; i++) {
      when(typedBagType.getSlotName(i)).thenReturn(null);
    }

    when(bagRegistry.findByPlayerIdAndBagTypeId(1, 5)).thenReturn(Optional.of(ownedBagEntry));
    when(serviceContext.getItemService()).thenReturn(itemService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("relluessentials");

    CustomItem disabledItemEntry = mock(CustomItem.class);
    ItemStack disabledItemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    when(disabledItemEntry.toItemStack()).thenReturn(disabledItemStack);
    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledItemEntry));

    var result = bagService.getBagInventory(5, playerEntry);

    assertNotNull(result);
  }

  @Test
  void purchaseBagDeductsCostAndRegistersNewBag() {
    when(playerEntry.getId()).thenReturn(1);
    when(playerEntry.getPurse()).thenReturn(500.0);
    when(bagTypeEntry.getCost()).thenReturn(100);
    when(bagTypeEntry.getDisplayName()).thenReturn("StoneBag");
    when(bagRepository.insert(1, 0)).thenReturn(bagEntry);
    when(bagTypeEntry.getId()).thenReturn(0);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BAGS_BOUGHT), anyString()))
        .thenReturn("Bought!");

    Player player = mock(Player.class);

    bagService.purchaseBag(bagTypeEntry, player, playerEntry);

    assertAll(
        () -> verify(playerEntry).setPurse(400L),
        () -> verify(playerEntry).setUpdatedBy(1),
        () -> verify(playerEntry).setHasToBeUpdated(true),
        () -> verify(bagRepository).insert(1, 0),
        () -> verify(bagRegistry).register(bagEntry),
        () -> verify(player).sendMessage("Bought!")
    );
  }

  private void stubDisabledItemResolution() {
    lenient().when(serviceContext.getItemService()).thenReturn(itemService);
    lenient().when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    lenient().when(pluginMetadataService.getName()).thenReturn("relluessentials");

    CustomItem disabledItemEntry = mock(CustomItem.class);
    ItemStack disabledItemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    lenient().when(disabledItemEntry.toItemStack()).thenReturn(disabledItemStack);
    lenient().when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledItemEntry));
  }

  private BagService buildServiceWithBagTypeBlocks(BagTypeEntry typedBagType) {
    BagTypeRepository localBagTypeRepository = mock(BagTypeRepository.class);
    BagRepository localBagRepository = mock(BagRepository.class);
    BagTypeRegistry localBagTypeRegistry = mock(BagTypeRegistry.class);

    when(localBagTypeRepository.findAll()).thenReturn(List.of(typedBagType));
    when(localBagRepository.findAll()).thenReturn(new ArrayList<>());
    when(localBagTypeRegistry.getAll()).thenReturn(List.of(typedBagType));

    stubDisabledItemResolution();
    lenient().when(itemFactory.getItemMeta(any(Material.class))).thenReturn(null);
    lenient().doReturn(true).when(itemFactory).equals(isNull(), isNull());

    return new BagService(
        serviceContext,
        bagRegistry,
        localBagRepository,
        localBagTypeRegistry,
        localBagTypeRepository
    );
  }
}