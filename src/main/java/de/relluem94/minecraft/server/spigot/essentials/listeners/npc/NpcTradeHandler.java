package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BuyBackSlotResolver;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

/**
 * Handles all trade interactions between players and NPC trader inventories.
 *
 * <p>Processes buy and sell actions for regular items, custom heads, and bags.
 * Delegates to specialized sub-handlers based on the type of clicked item.</p>
 *
 * @author rellu
 */
public class NpcTradeHandler {

  private final CustomItem disabledItem;
  private final CustomItem closeItem;
  private final CustomItem coinsItem;
  private final BuyBackSlotResolver buyBackSlotResolver;
  private final ServiceContext serviceContext;
  private final NamespacedKey buyPriceKey;
  private final NamespacedKey sellPriceKey;

  /**
   * Creates a new {@code NpcTradeHandler} with explicitly provided {@link org.bukkit.NamespacedKey}
   * instances for buy and sell price lookups.
   *
   * <p>Intended for use in tests where static key creation via
   * {@link de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants}
   * is not available due to the absence of a real Bukkit plugin instance.</p>
   *
   * @param serviceContext the service context providing access to all required services
   * @param buyPriceKey    the namespaced key used to read buy prices from item persistent data
   * @param sellPriceKey   the namespaced key used to read and write sell prices from item
   *                       persistent data
   */
  NpcTradeHandler(ServiceContext serviceContext, NamespacedKey buyPriceKey, NamespacedKey sellPriceKey) {
    this.disabledItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED)).orElseThrow();
    this.closeItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE)).orElseThrow();
    this.coinsItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_COINS)).orElseThrow();

    this.buyBackSlotResolver = new BuyBackSlotResolver(serviceContext.getBuyBackService(),
        this.disabledItem.toItemStack());
    this.serviceContext = serviceContext;
    this.buyPriceKey = buyPriceKey;
    this.sellPriceKey = sellPriceKey;
  }

  /**
   * Creates a new {@code NpcTradeHandler} and resolves all required GUI items from the
   * item service.
   *
   * @param serviceContext the service context providing access to all required services
   * @throws java.util.NoSuchElementException if any required GUI item cannot be found in the item service
   */
  public NpcTradeHandler(ServiceContext serviceContext) {
    this(serviceContext, itemBuyPrice(), itemSellPrice());
  }

  /**
   * Handles a player interaction with an item inside an NPC trader inventory.
   *
   * <p>Delegates to the appropriate handler based on the clicked item type:
   * close item, disabled item, bag item, custom head item, or regular tradeable item.</p>
   *
   * @param clickedItem      the item the player clicked on
   * @param clickedInventory the inventory in which the click occurred
   * @param player           the player who performed the interaction
   * @param playerEntry      the database entry of the interacting player
   * @param slot             the slot index that was clicked
   * @param isRightClick     {@code true} if the player used a right-click, {@code false}
   *                                     for left-click
   */
  public void handle(ItemStack clickedItem, Inventory clickedInventory, Player player,
      PlayerEntry playerEntry, int slot, boolean isRightClick) {

    if (clickedItem.isSimilar(closeItem.toItemStack())) {
      InventoryHelper.closeInventory(player);
      return;
    }

    if (disabledItem.toItemStack().isSimilar(clickedItem)) {
      player.playSound(player.getLocation(), "entity.chicken.step", 1f, 1f);
      return;
    }

    if (isBagItem(clickedItem)) {
      handleBagPurchase(clickedItem, player, playerEntry);
      return;
    }

    if (isCustomHeadItem(clickedItem)) {
      handleCustomHeadTrade(clickedItem, clickedInventory, player, playerEntry, slot, isRightClick);
      return;
    }

    handleItemTrade(clickedItem, clickedInventory, player, playerEntry, slot, isRightClick);
  }

  private boolean isCustomHeadItem(@NonNull ItemStack item) {
    if (!Material.PLAYER_HEAD.equals(item.getType())) {
      return false;
    }
    if (!(item.getItemMeta() instanceof SkullMeta skullMeta)) {
      return false;
    }
    if (skullMeta.getOwnerProfile() == null) {
      return false;
    }
    UUID profileUuid = skullMeta.getOwnerProfile().getUniqueId();
    return Arrays.stream(CustomHeads.values()).filter(ch -> !ch.equals(CustomHeads.BAG))
        .anyMatch(ch -> ch.getUuid().equals(profileUuid));
  }

  private void handleCustomHeadTrade(@NonNull ItemStack clickedItem, Inventory clickedInventory,
      Player player, PlayerEntry playerEntry, int slot, boolean isRightClick) {
    ItemMeta itemMeta = clickedItem.getItemMeta();
    if (itemMeta == null) {
      return;
    }

    Integer buyPrice =
        itemMeta.getPersistentDataContainer().has(buyPriceKey, PersistentDataType.INTEGER)
            ? itemMeta.getPersistentDataContainer().get(buyPriceKey, PersistentDataType.INTEGER)
            : null;

    Integer sellPrice =
        itemMeta.getPersistentDataContainer().has(sellPriceKey, PersistentDataType.INTEGER)
            ? itemMeta.getPersistentDataContainer().get(sellPriceKey, PersistentDataType.INTEGER)
            : null;

    if (buyPrice == null || sellPrice == null) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE));
      return;
    }

    String itemDisplayName = itemMeta.hasDisplayName() ? itemMeta.getDisplayName()
        : clickedItem.getType().name().toLowerCase().replace('_', ' ');

    int amount = clickedItem.getAmount();

    if (isChestInventory(clickedInventory)) {
      handleBuy(clickedItem, player, playerEntry, buyPrice, itemDisplayName,
          isRightClick ? 64 : amount, slot);
    } else if (isPlayerInventory(clickedInventory)) {
      handleSell(clickedItem, player, playerEntry, sellPrice, itemDisplayName, slot, isRightClick);
    }
  }

  private boolean isBagItem(@NonNull ItemStack item) {
    if (!Material.PLAYER_HEAD.equals(item.getType())) {
      return false;
    }
    if (!(item.getItemMeta() instanceof SkullMeta skullMeta)) {
      return false;
    }
    if (skullMeta.getOwnerProfile() == null) {
      return false;
    }
    return CustomHeads.BAG.getUuid().equals(skullMeta.getOwnerProfile().getUniqueId());
  }

  private void handleBagPurchase(@NonNull ItemStack clickedItem, Player player,
      PlayerEntry playerEntry) {
    if (clickedItem.getItemMeta() == null) {
      return;
    }

    Optional<BagTypeEntry> bagTypeOptional = serviceContext.getBagService()
        .findBagTypeByPartialName(clickedItem.getItemMeta().getDisplayName());

    if (bagTypeOptional.isEmpty()) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_NO_BAG_FOUND));
      return;
    }

    BagTypeEntry bagType = bagTypeOptional.get();
    if (serviceContext.getBagService().hasBag(bagType.getId(), playerEntry)) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_ALREADY_BOUGHT,
              bagType.getDisplayName()));
      return;
    }

    if (playerEntry.getPurse() < bagType.getCost()) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_NO_COINS, PLUGIN_NAME_MONEY));
      return;
    }

    serviceContext.getBagService().purchaseBag(bagType, player, playerEntry);
  }

  private void handleItemTrade(@NonNull ItemStack clickedItem, Inventory clickedInventory,
      Player player, PlayerEntry playerEntry, int slot, boolean isRightClick) {
    ItemMeta itemMeta = clickedItem.getItemMeta();
    if (itemMeta == null) {
      return;
    }

    Integer buyPrice = resolveBuyPrice(clickedItem, itemMeta);
    Integer sellPrice = resolveSellPrice(clickedItem, itemMeta);

    if (buyPrice == null || sellPrice == null) {
      return;
    }

    String itemDisplayName = resolveItemDisplayName(clickedItem);
    int amount = clickedItem.getAmount();

    if (isChestInventory(clickedInventory)) {
      handleBuy(clickedItem, player, playerEntry, buyPrice, itemDisplayName,
          isRightClick ? 64 : amount, slot);
    } else if (isPlayerInventory(clickedInventory)) {
      handleSell(clickedItem, player, playerEntry, sellPrice, itemDisplayName, slot, isRightClick);
    }
  }

  private String resolveItemDisplayName(@NonNull ItemStack item) {
    Optional<String> enchantmentName = serviceContext.getEnchantmentService()
        .findByBookItemStack(item)
        .map(enchantment -> enchantment.createEnchantedBook().getItemMeta())
        .filter(ItemMeta::hasDisplayName).map(ItemMeta::getDisplayName);

    if (enchantmentName.isPresent()) {
      return enchantmentName.get();
    }

    Optional<String> registeredItemName = serviceContext.getItemService().findByItemStack(item)
        .map(itemHelper -> itemHelper.toItemStack().getItemMeta())
        .filter(ItemMeta::hasDisplayName).map(ItemMeta::getDisplayName);

    if (registeredItemName.isPresent()) {
      return registeredItemName.get();
    }

    ItemMeta meta = item.getItemMeta();
    if (meta != null && meta.hasDisplayName()) {
      return meta.getDisplayName();
    }

    return item.getType().name().toLowerCase().replace('_', ' ');
  }

  private Integer resolveBuyPrice(ItemStack item, @NonNull ItemMeta meta) {
    if (meta.getPersistentDataContainer().has(buyPriceKey, PersistentDataType.INTEGER)) {
      return meta.getPersistentDataContainer().get(buyPriceKey, PersistentDataType.INTEGER);
    }

    Optional<Integer> enchantmentBuyPrice = serviceContext.getEnchantmentService()
        .findByBookItemStack(item).map(EnchantmentHelper::getCost);
    if (enchantmentBuyPrice.isPresent()) {
      return enchantmentBuyPrice.get();
    }

    Optional<Integer> registeredItemBuyPrice = serviceContext.getItemService().findByItemStack(item)
        .map(CustomItem::cost);
    return registeredItemBuyPrice.orElseGet(() -> ItemPrice.from(item.getType()).getBuyPrice());

  }

  private Integer resolveSellPrice(ItemStack item, @NonNull ItemMeta meta) {
    if (meta.getPersistentDataContainer().has(sellPriceKey, PersistentDataType.INTEGER)) {
      return meta.getPersistentDataContainer().get(sellPriceKey, PersistentDataType.INTEGER);
    }

    Optional<Integer> enchantmentSellPrice = serviceContext.getEnchantmentService()
        .findByBookItemStack(item).map(EnchantmentHelper::getCost);
    if (enchantmentSellPrice.isPresent()) {
      return enchantmentSellPrice.get();
    }

    Optional<Integer> registeredItemSellPrice = serviceContext.getItemService()
        .findByItemStack(item).map(CustomItem::cost);
    return registeredItemSellPrice.orElseGet(() -> ItemPrice.from(item.getType()).getSellPrice());
  }

  private void handleBuy(ItemStack guiItem, Player player, PlayerEntry playerEntry, int buyPrice,
      String itemDisplayName, int amount, int slot) {
    if (buyPrice <= 0) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE));
      return;
    }

    double totalCost = buyPrice * (double) amount;

    if (playerEntry.getPurse() - totalCost < 0) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_ENOUGH_COINS, itemDisplayName,
              StringHelper.formatDouble(totalCost), PLUGIN_NAME_MONEY,
              StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
      return;
    }

    if (player.getInventory().firstEmpty() == -1) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_INVENTORY_FULL, itemDisplayName,
              StringHelper.formatDouble(totalCost)));
      return;
    }

    ItemStack purchasedItem = resolveCleanPurchasedItem(guiItem, amount);

    if (guiItem.getItemMeta() == null) {
      return;
    }

    int resolvedSellPrice = resolveSellPrice(guiItem, guiItem.getItemMeta());
    writeSellPriceToItem(resolvedSellPrice, purchasedItem);

    player.getInventory().addItem(purchasedItem);
    playerEntry.setPurse(playerEntry.getPurse() - totalCost);
    playerEntry.setUpdatedBy(playerEntry.getId());
    playerEntry.setHasToBeUpdated(true);

    if (slot == 49 && serviceContext.getBuyBackService().hasBuyBackItems(player)) {
      serviceContext.getBuyBackService().removeBuyBackItem(player);
      player.getOpenInventory().getTopInventory()
          .setItem(49, buyBackSlotResolver.resolveForPlayer(player));
    }

    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY, itemDisplayName,
            StringHelper.formatDouble(totalCost), PLUGIN_NAME_MONEY,
            StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
    player.playSound(player, "entity.wandering_trader.yes", SoundCategory.MASTER, 1f, 1f);
  }

  private ItemStack resolveCleanPurchasedItem(ItemStack guiItem, int amount) {
    ItemStack purchasedItem = serviceContext.getEnchantmentService().findByBookItemStack(guiItem)
        .map(enchantment -> enchantment.createEnchantedBook().clone()).orElseGet(
            () -> serviceContext.getItemService().findByItemStack(guiItem)
                .map(itemHelper -> itemHelper.toItemStack().clone()).orElseGet(guiItem::clone));

    removePriceLoreFromItem(purchasedItem);
    purchasedItem.setAmount(amount);
    return purchasedItem;
  }

  private void removePriceLoreFromItem(@NonNull ItemStack item) {
    ItemMeta meta = item.getItemMeta();
    if (meta == null || meta.getLore() == null) {
      return;
    }

    List<String> filteredLore = meta.getLore().stream()
        .filter(line -> !line.contains(PLUGIN_NAME_MONEY)).toList();

    meta.setLore(filteredLore.isEmpty() ? null : filteredLore);
    item.setItemMeta(meta);
  }

  private void writeSellPriceToItem(int sellPrice, @NonNull ItemStack targetItem) {
    ItemMeta targetMeta = targetItem.getItemMeta();
    if (targetMeta == null) {
      return;
    }
    targetMeta.getPersistentDataContainer()
        .set(sellPriceKey, PersistentDataType.INTEGER, sellPrice);
    targetItem.setItemMeta(targetMeta);
  }

  private void handleSell(@NonNull ItemStack item, Player player, PlayerEntry playerEntry,
      int sellPrice, String itemDisplayName, int slot, boolean isRightClick) {
    ItemMeta meta = item.getItemMeta();

    if (coinsItem.toItemStack().isSimilar(item)) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_NO_PRICE));
      return;
    }

    boolean isRegisteredItem = serviceContext.getItemService().findByItemStack(item).isPresent()
        || serviceContext.getEnchantmentService().findByBookItemStack(item).isPresent() || (
        meta != null && meta.getPersistentDataContainer()
            .has(sellPriceKey, PersistentDataType.INTEGER));

    if (!isRegisteredItem) {
      if (meta == null) {
        return;
      }

      if (!meta.getEnchants().isEmpty()) {
        player.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_ENCHANTED));
        return;
      }

      if (meta instanceof Damageable damageable && damageable.hasDamage()) {
        player.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_USED_ITEM));
        return;
      }

      if (meta.hasDisplayName() && !(meta instanceof SkullMeta)) {
        player.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_RENAMED));
        return;
      }
    }

    if (sellPrice == 0) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_NO_PRICE));
      return;
    }

    double totalEarnings;
    int amount;

    if (isRightClick) {
      amount = removeAllMatchingItemsFromInventory(player, item);
      totalEarnings = sellPrice * (double) amount;
      serviceContext.getBuyBackService().recordSoldItems(player, item, amount);
    } else {
      amount = item.getAmount();
      totalEarnings = sellPrice * (double) amount;
      ItemStack slotItem = player.getInventory().getItem(slot);
      if (slotItem == null) {
        return;
      }
      serviceContext.getBuyBackService().recordSoldItems(player, slotItem, amount);
      slotItem.setAmount(0);
    }

    player.getOpenInventory().getTopInventory()
        .setItem(49, buyBackSlotResolver.resolveForPlayer(player));

    playerEntry.setPurse(playerEntry.getPurse() + totalEarnings);
    playerEntry.setUpdatedBy(playerEntry.getId());
    playerEntry.setHasToBeUpdated(true);
    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL, itemDisplayName,
            StringHelper.formatDouble(totalEarnings), PLUGIN_NAME_MONEY,
            StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
    player.playSound(player, "entity.wandering_trader.no", SoundCategory.MASTER, 1f, 1f);
  }

  private int removeAllMatchingItemsFromInventory(@NonNull Player player, ItemStack targetItem) {
    int totalAmount = 0;
    for (ItemStack inventoryItem : player.getInventory().getContents()) {
      if (inventoryItem != null && inventoryItem.isSimilar(targetItem)) {
        totalAmount += inventoryItem.getAmount();
        player.getInventory().remove(inventoryItem);
      }
    }
    return totalAmount;
  }

  protected boolean isChestInventory(Inventory inventory) {
    return inventory.getType().equals(InventoryType.CHEST);
  }

  protected boolean isPlayerInventory(Inventory inventory) {
    return inventory.getType().equals(InventoryType.PLAYER);
  }
}