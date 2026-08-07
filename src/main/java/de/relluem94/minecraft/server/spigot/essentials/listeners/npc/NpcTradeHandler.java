package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BuyBackSlotResolver;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

public class NpcTradeHandler {

  private final ItemHelper disabledItem;
  private final ItemHelper closeItem;
  private final ItemHelper coinsItem;
  private final BuyBackSlotResolver buyBackSlotResolver;
  private final ServiceContext serviceContext;

  public NpcTradeHandler(ServiceContext serviceContext) {
    this.disabledItem = ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow();
    this.closeItem = ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE))
        .orElseThrow();
    this.coinsItem = ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_COINS))
        .orElseThrow();

    this.buyBackSlotResolver = new BuyBackSlotResolver(
        RelluEssentials.getInstance().getBuyBackService(), this.disabledItem.getCustomItem());
    this.serviceContext = serviceContext;
  }

  public void handle(ItemStack clickedItem, Inventory clickedInventory, Player player,
      PlayerEntry playerEntry, int slot, boolean isRightClick) {

    if (closeItem.equalsExact(clickedItem)) {
      InventoryHelper.closeInventory(player);
      return;
    }

    if (disabledItem.equalsExact(clickedItem)) {
      player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_STEP, 1f, 1f);
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
    UUID profileUUID = skullMeta.getOwnerProfile().getUniqueId();
    return Arrays.stream(CustomHeads.values())
        .filter(ch -> !ch.equals(CustomHeads.BAG))
        .anyMatch(ch -> ch.getUUID().equals(profileUUID));
  }

  private void handleCustomHeadTrade(@NonNull ItemStack clickedItem, Inventory clickedInventory,
      Player player, PlayerEntry playerEntry, int slot, boolean isRightClick) {
    ItemMeta itemMeta = clickedItem.getItemMeta();
    if (itemMeta == null) {
      return;
    }

    Integer buyPrice =
        itemMeta.getPersistentDataContainer().has(itemBuyPrice(), PersistentDataType.INTEGER)
            ? itemMeta.getPersistentDataContainer().get(itemBuyPrice(), PersistentDataType.INTEGER)
            : null;

    Integer sellPrice =
        itemMeta.getPersistentDataContainer().has(itemSellPrice(), PersistentDataType.INTEGER)
            ? itemMeta.getPersistentDataContainer().get(itemSellPrice(), PersistentDataType.INTEGER)
            : null;

    if (buyPrice == null || sellPrice == null) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE));
      return;
    }

    String itemDisplayName = itemMeta.hasDisplayName()
        ? itemMeta.getDisplayName()
        : clickedItem.getType().name().toLowerCase().replace('_', ' ');

    int amount = clickedItem.getAmount();

    if (clickedInventory.getType().equals(InventoryType.CHEST)) {
      handleBuy(clickedItem, player, playerEntry, buyPrice, itemDisplayName,
          isRightClick ? 64 : amount, slot);
    } else if (clickedInventory.getType().equals(InventoryType.PLAYER)) {
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
    return CustomHeads.BAG.getUUID().equals(skullMeta.getOwnerProfile().getUniqueId());
  }

  private void handleBagPurchase(@NonNull ItemStack clickedItem, Player player,
      PlayerEntry playerEntry) {
    if (clickedItem.getItemMeta() == null) {
      return;
    }

    BagTypeEntry bagType = findMatchingBagType(clickedItem.getItemMeta().getDisplayName());

    if (bagType == null) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_NO_BAG_FOUND));
      return;
    }

    if (serviceContext.getBagService().hasBag(bagType.getId(), playerEntry)) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_ALREADY_BOUGHT,
                  bagType.getDisplayName()));
      return;
    }

    if (playerEntry.getPurse() < bagType.getCost()) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_NO_COINS,
              PLUGIN_NAME_MONEY));
      return;
    }

    purchaseBag(bagType, player, playerEntry);
  }

  private BagTypeEntry findMatchingBagType(String displayName) {
    return RelluEssentials.getInstance().getBagTypeRegistry().getAll().stream()
        .filter(entry -> entry.getDisplayName().equals(displayName))
        .findFirst()
        .orElse(null);
  }

  private void purchaseBag(BagTypeEntry bagType, Player player, PlayerEntry playerEntry) {
    RelluEssentials.getInstance().getBagService().purchaseBag(bagType, player, playerEntry);
    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_BOUGHT,
            bagType.getDisplayName()));
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

    if (clickedInventory.getType().equals(InventoryType.CHEST)) {
      handleBuy(clickedItem, player, playerEntry, buyPrice, itemDisplayName,
          isRightClick ? 64 : amount, slot);
    } else if (clickedInventory.getType().equals(InventoryType.PLAYER)) {
      handleSell(clickedItem, player, playerEntry, sellPrice, itemDisplayName, slot, isRightClick);
    }
  }

  private String resolveItemDisplayName(@NonNull ItemStack item) {
    Optional<String> enchantmentName = EnchantmentRegistry.findByBookItemStack(item)
        .map(enchantment -> enchantment.getBook().getCustomItem().getItemMeta())
        .filter(meta -> meta != null && meta.hasDisplayName())
        .map(ItemMeta::getDisplayName);

    if (enchantmentName.isPresent()) {
      return enchantmentName.get();
    }

    Optional<String> registeredItemName = ItemRegistry.findByItemStack(item)
        .map(itemHelper -> itemHelper.getCustomItem().getItemMeta())
        .filter(meta -> meta != null && meta.hasDisplayName())
        .map(ItemMeta::getDisplayName);

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
    if (meta.getPersistentDataContainer().has(itemBuyPrice(), PersistentDataType.INTEGER)) {
      return meta.getPersistentDataContainer().get(itemBuyPrice(), PersistentDataType.INTEGER);
    }

    Optional<Integer> enchantmentBuyPrice = EnchantmentRegistry.findByBookItemStack(item)
        .map(enchantment -> enchantment.getBook().getCost());
    if (enchantmentBuyPrice.isPresent()) {
      return enchantmentBuyPrice.get();
    }

    Optional<Integer> registeredItemBuyPrice = ItemRegistry.findByItemStack(item)
        .map(itemHelper -> itemHelper.getCost());
    if (registeredItemBuyPrice.isPresent()) {
      return registeredItemBuyPrice.get();
    }

    return ItemPrice.from(item.getType()).getBuyPrice();
  }


  private Integer resolveSellPrice(ItemStack item, @NonNull ItemMeta meta) {
    if (meta.getPersistentDataContainer().has(itemSellPrice(), PersistentDataType.INTEGER)) {
      return meta.getPersistentDataContainer().get(itemSellPrice(), PersistentDataType.INTEGER);
    }

    Optional<Integer> enchantmentSellPrice = EnchantmentRegistry.findByBookItemStack(item)
        .map(enchantment -> enchantment.getBook().getCost());
    if (enchantmentSellPrice.isPresent()) {
      return enchantmentSellPrice.get();
    }

    Optional<Integer> registeredItemSellPrice = ItemRegistry.findByItemStack(item)
        .map(itemHelper -> itemHelper.getCost());
    if (registeredItemSellPrice.isPresent()) {
      return registeredItemSellPrice.get();
    }

    return ItemPrice.from(item.getType()).getSellPrice();
  }

  private void handleBuy(ItemStack guiItem, Player player, PlayerEntry playerEntry, int buyPrice,
      String itemDisplayName, int amount, int slot) {
    if (buyPrice <= 0) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE));
      return;
    }

    double totalCost = buyPrice * (double) amount;

    if (playerEntry.getPurse() - totalCost < 0) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_ENOUGH_COINS,
                  itemDisplayName, StringHelper.formatDouble(totalCost), PLUGIN_NAME_MONEY,
                  StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
      return;
    }

    if (player.getInventory().firstEmpty() == -1) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_INVENTORY_FULL,
                  itemDisplayName, StringHelper.formatDouble(totalCost)));
      return;
    }

    ItemStack purchasedItem = resolveCleanPurchasedItem(guiItem, amount);

    int resolvedSellPrice = resolveSellPrice(guiItem, guiItem.getItemMeta());
    writeSellPriceToItem(resolvedSellPrice, purchasedItem);

    player.getInventory().addItem(purchasedItem);
    playerEntry.setPurse(playerEntry.getPurse() - totalCost);
    playerEntry.setUpdatedBy(playerEntry.getId());
    playerEntry.setHasToBeUpdated(true);

    if (slot == 49 && RelluEssentials.getInstance().getBuyBackService().hasBuyBackItems(player)) {
      RelluEssentials.getInstance().getBuyBackService().removeBuyBackItem(player);
      player.getOpenInventory().getTopInventory()
          .setItem(49, buyBackSlotResolver.resolveForPlayer(player));
    }

    player.sendMessage(
        serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY, itemDisplayName,
                StringHelper.formatDouble(totalCost), PLUGIN_NAME_MONEY,
                StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
    player.playSound(player, Sound.ENTITY_WANDERING_TRADER_YES, SoundCategory.MASTER, 1f, 1f);
  }

  private ItemStack resolveCleanPurchasedItem(ItemStack guiItem, int amount) {
    ItemStack purchasedItem = EnchantmentRegistry.findByBookItemStack(guiItem)
        .map(enchantment -> enchantment.getBook().getCustomItem().clone())
        .orElseGet(() -> ItemRegistry.findByItemStack(guiItem)
            .map(itemHelper -> itemHelper.getCustomItem().clone())
            .orElseGet(guiItem::clone));

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
        .filter(line -> !line.contains(PLUGIN_NAME_MONEY))
        .toList();

    meta.setLore(filteredLore.isEmpty() ? null : filteredLore);
    item.setItemMeta(meta);
  }

  private void writeSellPriceToItem(int sellPrice, @NonNull ItemStack targetItem) {
    ItemMeta targetMeta = targetItem.getItemMeta();
    if (targetMeta == null) {
      return;
    }
    targetMeta.getPersistentDataContainer()
        .set(itemSellPrice(), PersistentDataType.INTEGER, sellPrice);
    targetItem.setItemMeta(targetMeta);
  }

  private void handleSell(@NonNull ItemStack item, Player player, PlayerEntry playerEntry,
      int sellPrice, String itemDisplayName, int slot, boolean isRightClick) {
    ItemMeta meta = item.getItemMeta();

    if (coinsItem.equalsName(item)) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_NO_PRICE));
      return;
    }

    boolean isRegisteredItem = ItemRegistry.findByItemStack(item).isPresent()
        || EnchantmentRegistry.findByBookItemStack(item).isPresent();

    if (!isRegisteredItem) {
      if (meta == null) {
        return;
      }

      if (!meta.getEnchants().isEmpty()) {
        player.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_ENCHANTED));
        return;
      }

      if (meta instanceof Damageable damageable && damageable.hasDamage()) {
        player.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_USED_ITEM));
        return;
      }

      if (meta.hasDisplayName() && !(meta instanceof SkullMeta)) {
        player.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_RENAMED));
        return;
      }
    }

    if (sellPrice == 0) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_NO_PRICE));
      return;
    }

    double totalEarnings;
    int amount;

    if (isRightClick) {
      amount = removeAllMatchingItemsFromInventory(player, item);
      totalEarnings = sellPrice * (double) amount;
      RelluEssentials.getInstance().getBuyBackService().recordSoldItems(player, item, amount);
    } else {
      amount = item.getAmount();
      totalEarnings = sellPrice * (double) amount;
      ItemStack slotItem = player.getInventory().getItem(slot);
      if (slotItem == null) {
        return;
      }
      RelluEssentials.getInstance().getBuyBackService().recordSoldItems(player, slotItem, amount);
      slotItem.setAmount(0);
    }

    player.getOpenInventory().getTopInventory()
        .setItem(49, buyBackSlotResolver.resolveForPlayer(player));

    playerEntry.setPurse(playerEntry.getPurse() + totalEarnings);
    playerEntry.setUpdatedBy(playerEntry.getId());
    playerEntry.setHasToBeUpdated(true);
    player.sendMessage(
        serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL, itemDisplayName,
                StringHelper.formatDouble(totalEarnings), PLUGIN_NAME_MONEY,
                StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
    player.playSound(player, Sound.ENTITY_WANDERING_TRADER_NO, SoundCategory.MASTER, 1f, 1f);
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
}