package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.EnchantmentService;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcTradeHandlerTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private ItemService itemService;
  @Mock
  private EnchantmentService enchantmentService;
  @Mock
  private BagService bagService;
  @Mock
  private BuyBackService buyBackService;
  @Mock
  private TranslationService translationService;
  @Mock
  private PluginMetadataService pluginMetadataService;
  @Mock
  private CustomItem disabledCustomItem;
  @Mock
  private CustomItem closeCustomItem;
  @Mock
  private CustomItem coinsCustomItem;
  @Mock
  private ItemStack disabledItemStack;
  @Mock
  private ItemStack closeItemStack;
  @Mock
  private ItemStack coinsItemStack;
  @Mock
  private Player player;
  @Mock
  private PlayerInventory playerInventory;
  @Mock
  private Location location;

  private NamespacedKey buyPriceKey;
  private NamespacedKey sellPriceKey;

  private PlayerEntry playerEntry;
  private NpcTradeHandler npcTradeHandler;

  private boolean testIsChestInventory = false;

  @BeforeEach
  void setUp() {
    when(serviceContext.getItemService()).thenReturn(itemService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getBuyBackService()).thenReturn(buyBackService);
    when(pluginMetadataService.getName()).thenReturn("relluessentials");

    when(disabledCustomItem.toItemStack()).thenReturn(disabledItemStack);
    lenient().when(closeCustomItem.toItemStack()).thenReturn(closeItemStack);
    lenient().when(coinsCustomItem.toItemStack()).thenReturn(coinsItemStack);

    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(disabledCustomItem))
        .thenReturn(Optional.of(closeCustomItem))
        .thenReturn(Optional.of(coinsCustomItem));

    playerEntry = new PlayerEntry();
    playerEntry.setId(1);
    playerEntry.setPurse(1000.0);

    buyPriceKey = NamespacedKey.fromString("relluessentials:buy_price");
    sellPriceKey = NamespacedKey.fromString("relluessentials:sell_price");

    npcTradeHandler = new NpcTradeHandler(serviceContext, buyPriceKey, sellPriceKey) {
      @Override
      protected boolean isChestInventory(Inventory inventory) {
        return testIsChestInventory;
      }

      @Override
      protected boolean isPlayerInventory(Inventory inventory) {
        return !testIsChestInventory;
      }
    };
  }

  @Test
  void constructorThrowsNoSuchElementExceptionWhenDisabledItemNotFound() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> new NpcTradeHandler(serviceContext));
  }

  @Test
  void handleClosesInventoryWhenCloseItemIsClicked() {
    ItemStack clickedItem = mock(ItemStack.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(clickedItem.isSimilar(closeItemStack)).thenReturn(true);

    npcTradeHandler.handle(clickedItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).closeInventory();
  }

  @Test
  void handlePlaysChickenSoundWhenDisabledItemIsClicked() {
    ItemStack clickedItem = mock(ItemStack.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(clickedItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(clickedItem)).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    npcTradeHandler.handle(clickedItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).playSound(location, "entity.chicken.step", 1f, 1f);
  }

  @Test
  void handleSendsBagNotFoundMessageWhenBagItemHasNoMatchingBagType() {
    when(serviceContext.getBagService()).thenReturn(bagService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);

    ItemStack bagItemStack = mock(ItemStack.class);
    SkullMeta skullMeta = mock(SkullMeta.class);
    org.bukkit.profile.PlayerProfile ownerProfile = mock(org.bukkit.profile.PlayerProfile.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(bagItemStack.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(bagItemStack)).thenReturn(false);
    when(bagItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
    when(bagItemStack.getItemMeta()).thenReturn(skullMeta);
    when(skullMeta.getOwnerProfile()).thenReturn(ownerProfile);
    when(ownerProfile.getUniqueId()).thenReturn(CustomHeads.BAG.getUuid());
    when(skullMeta.getDisplayName()).thenReturn("TestBag");
    when(bagService.findBagTypeByPartialName(anyString())).thenReturn(Optional.empty());
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_NO_BAG_FOUND)).thenReturn("No bag found");

    npcTradeHandler.handle(bagItemStack, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("No bag found");
  }

  @Test
  void handleSendsAlreadyBoughtMessageWhenPlayerAlreadyHasBag() {
    when(serviceContext.getBagService()).thenReturn(bagService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);

    ItemStack bagItemStack = mock(ItemStack.class);
    SkullMeta skullMeta = mock(SkullMeta.class);
    org.bukkit.profile.PlayerProfile ownerProfile = mock(org.bukkit.profile.PlayerProfile.class);
    Inventory clickedInventory = mock(Inventory.class);
    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(1);
    bagTypeEntry.setDisplayName("TestBag");
    bagTypeEntry.setCost(100);

    when(bagItemStack.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(bagItemStack)).thenReturn(false);
    when(bagItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
    when(bagItemStack.getItemMeta()).thenReturn(skullMeta);
    when(skullMeta.getOwnerProfile()).thenReturn(ownerProfile);
    when(ownerProfile.getUniqueId()).thenReturn(CustomHeads.BAG.getUuid());
    when(skullMeta.getDisplayName()).thenReturn("TestBag");
    when(bagService.findBagTypeByPartialName(anyString())).thenReturn(Optional.of(bagTypeEntry));
    when(bagService.hasBag(1, playerEntry)).thenReturn(true);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BAGS_ALREADY_BOUGHT), anyString())).thenReturn(
        "Already bought");

    npcTradeHandler.handle(bagItemStack, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("Already bought");
  }

  @Test
  void handleSendsNoCoinsMessageWhenPlayerCannotAffordBag() {
    when(serviceContext.getBagService()).thenReturn(bagService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);

    ItemStack bagItemStack = mock(ItemStack.class);
    SkullMeta skullMeta = mock(SkullMeta.class);
    org.bukkit.profile.PlayerProfile ownerProfile = mock(org.bukkit.profile.PlayerProfile.class);
    Inventory clickedInventory = mock(Inventory.class);
    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(1);
    bagTypeEntry.setDisplayName("TestBag");
    bagTypeEntry.setCost(9999);

    playerEntry.setPurse(0.0);

    when(bagItemStack.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(bagItemStack)).thenReturn(false);
    when(bagItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
    when(bagItemStack.getItemMeta()).thenReturn(skullMeta);
    when(skullMeta.getOwnerProfile()).thenReturn(ownerProfile);
    when(ownerProfile.getUniqueId()).thenReturn(CustomHeads.BAG.getUuid());
    when(skullMeta.getDisplayName()).thenReturn("TestBag");
    when(bagService.findBagTypeByPartialName(anyString())).thenReturn(Optional.of(bagTypeEntry));
    when(bagService.hasBag(1, playerEntry)).thenReturn(false);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BAGS_NO_COINS), anyString())).thenReturn(
        "No coins");

    npcTradeHandler.handle(bagItemStack, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("No coins");
  }

  @Test
  void handlePurchasesBagSuccessfullyWhenAllConditionsMet() {
    when(serviceContext.getBagService()).thenReturn(bagService);

    ItemStack bagItemStack = mock(ItemStack.class);
    SkullMeta skullMeta = mock(SkullMeta.class);
    org.bukkit.profile.PlayerProfile ownerProfile = mock(org.bukkit.profile.PlayerProfile.class);
    Inventory clickedInventory = mock(Inventory.class);
    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(1);
    bagTypeEntry.setDisplayName("TestBag");
    bagTypeEntry.setCost(100);

    playerEntry.setPurse(500.0);

    when(bagItemStack.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(bagItemStack)).thenReturn(false);
    when(bagItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
    when(bagItemStack.getItemMeta()).thenReturn(skullMeta);
    when(skullMeta.getOwnerProfile()).thenReturn(ownerProfile);
    when(ownerProfile.getUniqueId()).thenReturn(CustomHeads.BAG.getUuid());
    when(skullMeta.getDisplayName()).thenReturn("TestBag");
    when(bagService.findBagTypeByPartialName(anyString())).thenReturn(Optional.of(bagTypeEntry));
    when(bagService.hasBag(1, playerEntry)).thenReturn(false);

    npcTradeHandler.handle(bagItemStack, clickedInventory, player, playerEntry, 0, false);

    verify(bagService).purchaseBag(bagTypeEntry, player, playerEntry);
  }

  @Test
  void handleSendsNotTradeableMessageWhenCustomHeadHasNoPrices() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack headItemStack = mock(ItemStack.class);
    SkullMeta skullMeta = mock(SkullMeta.class);
    org.bukkit.profile.PlayerProfile ownerProfile = mock(org.bukkit.profile.PlayerProfile.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    UUID nonBagCustomHeadUuid = findNonBagCustomHeadUuid();

    when(headItemStack.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(headItemStack)).thenReturn(false);
    when(headItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
    when(headItemStack.getItemMeta()).thenReturn(skullMeta);
    when(skullMeta.getOwnerProfile()).thenReturn(ownerProfile);
    when(ownerProfile.getUniqueId()).thenReturn(nonBagCustomHeadUuid);
    when(skullMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE)).thenReturn("Not tradeable");

    npcTradeHandler.handle(headItemStack, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("Not tradeable");
  }

  @Test
  void handleBuysCustomHeadFromChestInventoryOnLeftClick() {
    testIsChestInventory = true;

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack headItemStack = mock(ItemStack.class);
    SkullMeta skullMeta = mock(SkullMeta.class);
    org.bukkit.profile.PlayerProfile ownerProfile = mock(org.bukkit.profile.PlayerProfile.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);
    PersistentDataContainer purchasedPdc = mock(PersistentDataContainer.class);

    UUID nonBagCustomHeadUuid = findNonBagCustomHeadUuid();

    when(headItemStack.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(headItemStack)).thenReturn(false);
    when(headItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
    when(headItemStack.getItemMeta()).thenReturn(skullMeta);
    when(skullMeta.getOwnerProfile()).thenReturn(ownerProfile);
    when(ownerProfile.getUniqueId()).thenReturn(nonBagCustomHeadUuid);
    when(skullMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(50, 25);
    when(skullMeta.hasDisplayName()).thenReturn(true);
    when(skullMeta.getDisplayName()).thenReturn("TestHead");
    when(headItemStack.getAmount()).thenReturn(1);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.firstEmpty()).thenReturn(0);
    when(headItemStack.clone()).thenReturn(headItemStack);
    when(skullMeta.getLore()).thenReturn(null);
    when(purchasedPdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(false);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BUY), any(), any(), any(), any(),
        any())).thenReturn("Bought!");
    when(buyBackService.hasBuyBackItems(player)).thenReturn(false);

    npcTradeHandler.handle(headItemStack, clickedInventory, player, playerEntry, 0, false);

    verify(translationService).getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BUY), any(), any(), any(), any(), any());
  }

  @Test
  void handleSendsNotEnoughCoinsMessageWhenBuyingItemWithInsufficientFunds() {
    testIsChestInventory = true;

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    playerEntry.setPurse(0.0);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(100, 50);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_ENOUGH_COINS), any(), any(), any(),
        any(), any())).thenReturn("Not enough coins");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("Not enough coins");
  }

  @Test
  void handleSendsInventoryFullMessageWhenPlayerInventoryIsFullOnBuy() {
    testIsChestInventory = true;

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    playerEntry.setPurse(1000.0);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.firstEmpty()).thenReturn(-1);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BUY_INVENTORY_FULL), any(), any())).thenReturn(
        "Inventory full");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("Inventory full");
  }

  @Test
  void handleSendsNotTradeableMessageWhenBuyPriceIsZero() {
    testIsChestInventory = true;

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(0, 0);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE)).thenReturn("Not tradeable");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("Not tradeable");
  }

  @Test
  void handleSendsNoPriceMessageWhenSellingCoinsItem() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_NO_PRICE)).thenReturn("No price");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("No price");
  }

  @Test
  void handleSendsEnchantedItemMessageWhenSellingEnchantedUnregisteredItem() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(false);
    when(itemMeta.getEnchants()).thenReturn(java.util.Map.of(mock(org.bukkit.enchantments.Enchantment.class), 1));
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_ENCHANTED)).thenReturn("Enchanted item");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("Enchanted item");
  }

  @Test
  void handleSendsDamagedItemMessageWhenSellingDamagedUnregisteredItem() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    DamageableItemMeta damageableMeta = mock(DamageableItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(damageableMeta);
    when(damageableMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(false);
    when(damageableMeta.getEnchants()).thenReturn(java.util.Map.of());
    when(damageableMeta.hasDamage()).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_USED_ITEM)).thenReturn("Used item");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("Used item");
  }

  @Test
  void handleSendsRenamedItemMessageWhenSellingRenamedUnregisteredItem() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(false);
    when(itemMeta.getEnchants()).thenReturn(java.util.Map.of());
    when(itemMeta.hasDisplayName()).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_RENAMED)).thenReturn("Renamed item");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("Renamed item");
  }

  @Test
  void handleSendsNoPriceMessageWhenSellPriceIsZero() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(false);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(false);
    when(itemMeta.getEnchants()).thenReturn(java.util.Map.of());
    when(itemMeta.hasDisplayName()).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_NO_PRICE)).thenReturn("No price");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player).sendMessage("No price");
  }

  @Test
  void handleSellsItemOnLeftClickFromPlayerInventory() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);
    org.bukkit.inventory.InventoryView inventoryView = mock(org.bukkit.inventory.InventoryView.class);
    Inventory topInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(false);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItem(anyInt())).thenReturn(regularItem);
    when(player.getOpenInventory()).thenReturn(inventoryView);
    when(inventoryView.getTopInventory()).thenReturn(topInventory);
    when(buyBackService.hasBuyBackItems(player)).thenReturn(false);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_SELL), any(), any(), any(), any(),
        any())).thenReturn("Sold!");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 5, false);

    verify(player).sendMessage("Sold!");
  }

  @Test
  void handleSellsAllMatchingItemsOnRightClickFromPlayerInventory() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    ItemStack inventoryItem1 = mock(ItemStack.class);
    ItemStack inventoryItem2 = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);
    org.bukkit.inventory.InventoryView inventoryView = mock(org.bukkit.inventory.InventoryView.class);
    Inventory topInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(false);
    when(player.getInventory()).thenReturn(playerInventory);
    when(inventoryItem1.isSimilar(regularItem)).thenReturn(true);
    when(inventoryItem1.getAmount()).thenReturn(5);
    when(inventoryItem2.isSimilar(regularItem)).thenReturn(true);
    when(inventoryItem2.getAmount()).thenReturn(3);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{inventoryItem1, inventoryItem2});
    when(player.getOpenInventory()).thenReturn(inventoryView);
    when(inventoryView.getTopInventory()).thenReturn(topInventory);
    when(buyBackService.hasBuyBackItems(player)).thenReturn(false);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_SELL), any(), any(), any(), any(),
        any())).thenReturn("Sold all!");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 5, true);

    verify(player).sendMessage("Sold all!");
    verify(playerInventory).remove(inventoryItem1);
    verify(playerInventory).remove(inventoryItem2);
  }

  @Test
  void handleDoesNothingWhenItemMetaIsNullForRegularItem() {
    ItemStack regularItem = mock(ItemStack.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(null);

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void handleDoesNothingWhenBagItemMetaIsNull() {
    ItemStack bagItemStack = mock(ItemStack.class);
    SkullMeta skullMeta = mock(SkullMeta.class);
    org.bukkit.profile.PlayerProfile ownerProfile = mock(org.bukkit.profile.PlayerProfile.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(bagItemStack.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(bagItemStack)).thenReturn(false);
    when(bagItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
    when(bagItemStack.getItemMeta())
        .thenReturn(skullMeta)
        .thenReturn(null);
    when(skullMeta.getOwnerProfile()).thenReturn(ownerProfile);
    when(ownerProfile.getUniqueId()).thenReturn(CustomHeads.BAG.getUuid());

    npcTradeHandler.handle(bagItemStack, clickedInventory, player, playerEntry, 0, false);

    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void handleUpdatesPlayerPurseAfterSuccessfulSell() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    playerEntry.setPurse(100.0);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);
    org.bukkit.inventory.InventoryView inventoryView = mock(org.bukkit.inventory.InventoryView.class);
    Inventory topInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(2);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(false);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItem(anyInt())).thenReturn(regularItem);
    when(player.getOpenInventory()).thenReturn(inventoryView);
    when(inventoryView.getTopInventory()).thenReturn(topInventory);
    when(buyBackService.hasBuyBackItems(player)).thenReturn(false);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_SELL), any(), any(), any(), any(),
        any())).thenReturn("Sold!");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 5, false);

    assert playerEntry.getPurse() == 110.0;
    assert playerEntry.isHasToBeUpdated();
  }

  @Test
  void handleUpdatesPlayerPurseAfterSuccessfulBuy() {
    testIsChestInventory = true;

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    playerEntry.setPurse(500.0);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);
    org.bukkit.inventory.InventoryView inventoryView = mock(org.bukkit.inventory.InventoryView.class);
    Inventory topInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(50, 25);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.firstEmpty()).thenReturn(0);
    when(regularItem.clone()).thenReturn(regularItem);
    when(itemMeta.getLore()).thenReturn(null);
    lenient().when(player.getOpenInventory()).thenReturn(inventoryView);
    lenient().when(inventoryView.getTopInventory()).thenReturn(topInventory);
    lenient().when(buyBackService.hasBuyBackItems(player)).thenReturn(false);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BUY), any(), any(), any(), any(),
        any())).thenReturn("Bought!");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, false);

    assert playerEntry.getPurse() == 450.0;
    assert playerEntry.isHasToBeUpdated();
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void handleBuysCorrectAmountBasedOnClickType(boolean isRightClick) {
    testIsChestInventory = true;

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    playerEntry.setPurse(100000.0);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);
    org.bukkit.inventory.InventoryView inventoryView = mock(org.bukkit.inventory.InventoryView.class);
    Inventory topInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(1, 1);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.firstEmpty()).thenReturn(0);
    when(regularItem.clone()).thenReturn(regularItem);
    when(itemMeta.getLore()).thenReturn(null);
    when(player.getOpenInventory()).thenReturn(inventoryView);
    when(inventoryView.getTopInventory()).thenReturn(topInventory);
    when(buyBackService.hasBuyBackItems(player)).thenReturn(false);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BUY), any(), any(), any(), any(),
        any())).thenReturn("Bought!");

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 0, isRightClick);

    verify(regularItem).setAmount(isRightClick ? 64 : 1);
  }

  @Test
  void handleDoesNothingWhenLeftClickSellAndSlotItemIsNull() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);

    ItemStack regularItem = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    PersistentDataContainer pdc = mock(PersistentDataContainer.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(regularItem.isSimilar(closeItemStack)).thenReturn(false);
    when(disabledItemStack.isSimilar(regularItem)).thenReturn(false);
    when(regularItem.getType()).thenReturn(Material.STONE);
    when(regularItem.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(pdc);
    when(pdc.has(any(), eq(PersistentDataType.INTEGER))).thenReturn(true);
    when(pdc.get(any(), eq(PersistentDataType.INTEGER))).thenReturn(10, 5);
    when(regularItem.getAmount()).thenReturn(1);
    when(enchantmentService.findByBookItemStack(regularItem)).thenReturn(Optional.empty());
    when(itemService.findByItemStack(regularItem)).thenReturn(Optional.empty());
    when(coinsItemStack.isSimilar(regularItem)).thenReturn(false);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItem(anyInt())).thenReturn(null);

    npcTradeHandler.handle(regularItem, clickedInventory, player, playerEntry, 5, false);

    verify(player, never()).sendMessage(anyString());
  }

  private UUID findNonBagCustomHeadUuid() {
    for (CustomHeads ch : CustomHeads.values()) {
      if (!ch.equals(CustomHeads.BAG)) {
        return ch.getUuid();
      }
    }
    throw new IllegalStateException("No non-bag custom head found");
  }

  private interface DamageableItemMeta extends ItemMeta, Damageable {

  }
}