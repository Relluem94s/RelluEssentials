package de.relluem94.minecraft.server.spigot.essentials.events.npc;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
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

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;

public class NPCTradeHandler {

    public void handle(ItemStack clickedItem, Inventory clickedInventory, Player player, PlayerEntry playerEntry, int slot, boolean isRightClick) {
        if (CustomItems.npc_gui_close.equalsExact(clickedItem)) {
            InventoryHelper.closeInventory(player);
            return;
        }

        if (CustomItems.npc_gui_disabled.equalsExact(clickedItem)) {
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_STEP, 1f, 1f);
            return;
        }

        if (isBagItem(clickedItem)) {
            handleBagPurchase(clickedItem, player, playerEntry);
            return;
        }

        handleItemTrade(clickedItem, clickedInventory, player, playerEntry, slot, isRightClick);
    }

    private boolean isBagItem(@NonNull ItemStack item) {
        return Material.PLAYER_HEAD.equals(item.getType())
                && item.getItemMeta() instanceof SkullMeta skullMeta
                && skullMeta.getOwnerProfile() != null
                && CustomHeads.BAG.getName().equals(skullMeta.getOwnerProfile().getName());
    }

    private void handleBagPurchase(@NonNull ItemStack clickedItem, Player player, PlayerEntry playerEntry) {
        if(clickedItem.getItemMeta() == null){
            return;
        }

        BagTypeEntry bagType = findMatchingBagType(clickedItem.getItemMeta().getDisplayName());

        if (bagType == null) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_NO_BAG_FOUND));
            return;
        }

        if (BagHelper.hasBag(bagType.getId(), playerEntry)) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_ALREADY_BOUGHT, bagType.getDisplayName()));
            return;
        }

        if (playerEntry.getPurse() < bagType.getCost()) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_NO_COINS, PLUGIN_NAME_MONEY));
            return;
        }

        purchaseBag(bagType, player, playerEntry);
    }

    private BagTypeEntry findMatchingBagType(String displayName) {
        return RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList().stream()
                .filter(entry -> entry.getDisplayName().equals(displayName))
                .findFirst()
                .orElse(null);
    }

    private void purchaseBag(BagTypeEntry bagType, Player player, PlayerEntry playerEntry) {
        playerEntry.setPurse(playerEntry.getPurse() - bagType.getCost());
        playerEntry.setUpdatedBy(playerEntry.getId());
        playerEntry.setHasToBeUpdated(true);
        RelluEssentials.getInstance().getDatabaseHelper().insertBag(bagType.getId(), playerEntry.getId());
        RelluEssentials.getInstance().getPlayerAPI().putPlayerBagEntry(
                playerEntry.getId(),
                RelluEssentials.getInstance().getDatabaseHelper().getBag(bagType.getId(), playerEntry.getId())
        );
        player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_BOUGHT, bagType.getDisplayName()));
    }

    private void handleItemTrade(@NonNull ItemStack clickedItem, Inventory clickedInventory, Player player, PlayerEntry playerEntry, int slot, boolean isRightClick) {
        ItemMeta itemMeta = clickedItem.getItemMeta();
        if (itemMeta == null) return;

        Integer buyPrice = resolveBuyPrice(clickedItem, itemMeta);
        Integer sellPrice = resolveSellPrice(clickedItem, itemMeta);

        if (buyPrice == null || sellPrice == null) return;

        String itemDisplayName = clickedItem.getType().name().toLowerCase().replace('_', ' ');
        int amount = clickedItem.getAmount();

        if (clickedInventory.getType().equals(InventoryType.CHEST)) {
            handleBuy(clickedItem, player, playerEntry, buyPrice, itemDisplayName, isRightClick ? 64 : amount);
        } else if (clickedInventory.getType().equals(InventoryType.PLAYER)) {
            handleSell(clickedItem, player, playerEntry, sellPrice, itemDisplayName, slot, isRightClick);
        }
    }

    private Integer resolveBuyPrice(ItemStack item, @NonNull ItemMeta meta) {
        if (meta.getPersistentDataContainer().has(itemBuyPrice(), PersistentDataType.INTEGER)) {
            return meta.getPersistentDataContainer().get(itemBuyPrice(), PersistentDataType.INTEGER);
        }
        return ItemPrice.from(item.getType()).getBuyPrice();
    }

    private Integer resolveSellPrice(ItemStack item, @NonNull ItemMeta meta) {
        if (meta.getPersistentDataContainer().has(itemSellPrice(), PersistentDataType.INTEGER)) {
            return meta.getPersistentDataContainer().get(itemSellPrice(), PersistentDataType.INTEGER);
        }
        return ItemPrice.from(item.getType()).getSellPrice();
    }

    private void handleBuy(ItemStack item, Player player, PlayerEntry playerEntry, int buyPrice, String itemDisplayName, int amount) {
        if (buyPrice <= 0) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE));
            return;
        }

        double totalCost = buyPrice * (double) amount;

        if (playerEntry.getPurse() - totalCost < 0) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_NOT_ENOUGH_COINS, itemDisplayName, StringHelper.formatDouble(totalCost), PLUGIN_NAME_MONEY, StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY_INVENTORY_FULL, itemDisplayName, StringHelper.formatDouble(totalCost)));
            return;
        }

        player.getInventory().addItem(new ItemStack(item.getType(), amount));
        playerEntry.setPurse(playerEntry.getPurse() - totalCost);
        playerEntry.setUpdatedBy(playerEntry.getId());
        playerEntry.setHasToBeUpdated(true);
        player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BUY, itemDisplayName, StringHelper.formatDouble(totalCost), PLUGIN_NAME_MONEY, StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
        player.playSound(player, Sound.ENTITY_WANDERING_TRADER_YES, SoundCategory.MASTER, 1f, 1f);
    }

    private void handleSell(@NonNull ItemStack item, Player player, PlayerEntry playerEntry, int sellPrice, String itemDisplayName, int slot, boolean isRightClick) {
        Damageable damageable = (Damageable) item.getItemMeta();
        ItemMeta meta = item.getItemMeta();

        if(meta == null){
            return;
        }

        if (!meta.getEnchants().isEmpty()) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_ENCHANTED));
            return;
        }

        if (damageable != null && damageable.hasDamage()) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_USED_ITEM));
            return;
        }

        if (sellPrice == 0) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_NO_PRICE));
            return;
        }

        if (meta.hasDisplayName() && !(meta instanceof SkullMeta)) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL_RENAMED));
            return;
        }

        double totalEarnings;
        int amount;

        if (isRightClick) {
            amount = removeAllMatchingItemsFromInventory(player, item);
            totalEarnings = sellPrice * (double) amount;
        } else {
            amount = item.getAmount();
            totalEarnings = sellPrice * (double) amount;
            ItemStack slotItem = player.getInventory().getItem(slot);
            if (slotItem == null) return;
            slotItem.setAmount(0);
        }

        playerEntry.setPurse(playerEntry.getPurse() + totalEarnings);
        playerEntry.setUpdatedBy(playerEntry.getId());
        playerEntry.setHasToBeUpdated(true);
        player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SELL, itemDisplayName, StringHelper.formatDouble(totalEarnings), PLUGIN_NAME_MONEY, StringHelper.formatDouble(playerEntry.getPurse()), PLUGIN_NAME_MONEY));
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