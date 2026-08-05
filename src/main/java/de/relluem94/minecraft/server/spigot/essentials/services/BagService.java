package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper.BAG_SIZE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper.getItemStacks;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessageInChannel;
import static de.relluem94.minecraft.server.spigot.essentials.listeners.BetterChatFormat.ADMIN_CHANNEL;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.BagRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BagService {

  private final BagRegistry bagRegistry;
  private final DatabaseHelper databaseHelper;
  private final TranslationService translationService;
  private final List<ItemStack> bagBlocks2collect;

  public BagService(
      BagRegistry bagRegistry,
      DatabaseHelper databaseHelper,
      TranslationService translationService,
      List<ItemStack> bagBlocks2collect
  ) {
    this.bagRegistry = bagRegistry;
    this.databaseHelper = databaseHelper;
    this.translationService = translationService;
    this.bagBlocks2collect = bagBlocks2collect;
  }

  public Optional<BagEntry> findBag(int playerId, int bagTypeId) {
    return bagRegistry.findByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  public Collection<BagEntry> findBags(int playerId) {
    return bagRegistry.findAllByPlayerId(playerId);
  }

  public boolean hasBag(int playerId, int bagTypeId) {
    return bagRegistry.existsByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  public boolean hasBag(int bagTypeId, @NotNull PlayerEntry playerEntry) {
    return hasBag(playerEntry.getId(), bagTypeId);
  }

  public boolean hasBags(int playerId) {
    return bagRegistry.existsByPlayerId(playerId);
  }

  public int getSlotByItemStack(@NotNull BagEntry bagEntry, ItemStack itemStack) {
    List<ItemStack> slotItemStacks = Arrays.asList(getItemStacks(bagEntry.getBagType()));
    ItemStack itemStackWithoutAmount = ItemHelper.getCleanItemStack(itemStack);

    if (!slotItemStacks.contains(itemStackWithoutAmount)) {
      return -1;
    }

    for (int slotIndex = 0; slotIndex < BAG_SIZE; slotIndex++) {
      if (slotItemStacks.get(slotIndex).equals(itemStackWithoutAmount)) {
        return slotIndex;
      }
    }

    return -1;
  }

  public @NotNull List<Item> collectItems(
      @NotNull List<Item> droppedItems,
      Player player,
      PlayerEntry playerEntry
  ) {
    List<Item> collectedItems = new ArrayList<>();
    ListIterator<Item> iterator = droppedItems.listIterator();

    while (iterator.hasNext()) {
      Item droppedItem = iterator.next();
      ItemStack itemWithoutAmount = droppedItem.getItemStack().clone();
      itemWithoutAmount.setAmount(1);

      if (!bagBlocks2collect.contains(itemWithoutAmount)) {
        continue;
      }

      Collection<BagEntry> playerBags = findBags(playerEntry.getId());
      for (BagEntry bagEntry : playerBags) {
        int slot = getSlotByItemStack(bagEntry, itemWithoutAmount);
        if (slot == -1) {
          continue;
        }

        bagEntry.setSlotValue(slot, bagEntry.getSlotValue(slot) + droppedItem.getItemStack().getAmount());
        bagEntry.setHasToBeUpdated(true);
        ChatHelper.sendMessageInActionBar(player,
            translationService.get(MessageKey.PLUGIN_EVENT_BAG_COLLECT,
                droppedItem.getItemStack().getAmount(), droppedItem.getName()));
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
        collectedItems.add(droppedItem);
      }
    }

    return collectedItems;
  }

  public @NotNull List<ItemStack> collectItemStacks(
      @NotNull List<ItemStack> itemStacks,
      Player player,
      PlayerEntry playerEntry
  ) {
    List<ItemStack> collectedStacks = new ArrayList<>();
    ListIterator<ItemStack> iterator = itemStacks.listIterator();

    while (iterator.hasNext()) {
      ItemStack itemStack = iterator.next();
      ItemStack itemWithoutAmount = itemStack.clone();
      itemWithoutAmount.setAmount(1);

      if (!bagBlocks2collect.contains(itemWithoutAmount)) {
        continue;
      }

      Collection<BagEntry> playerBags = findBags(playerEntry.getId());
      for (BagEntry bagEntry : playerBags) {
        int slot = getSlotByItemStack(bagEntry, itemWithoutAmount);
        if (slot == -1) {
          continue;
        }

        bagEntry.setSlotValue(slot, bagEntry.getSlotValue(slot) + itemStack.getAmount());
        bagEntry.setHasToBeUpdated(true);
        ChatHelper.sendMessageInActionBar(player,
            translationService.get(MessageKey.PLUGIN_EVENT_BAG_COLLECT,
                itemStack.getAmount(),
                itemStack.getType().name().replace("_", " ").toLowerCase()));
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
        collectedStacks.add(itemStack);
      }
    }

    return collectedStacks;
  }

  public boolean collectItem(
      @NotNull Item droppedItem,
      Player player,
      PlayerEntry playerEntry
  ) {
    ItemStack itemWithoutAmount = droppedItem.getItemStack().clone();
    itemWithoutAmount.setAmount(1);

    if (!bagBlocks2collect.contains(itemWithoutAmount)) {
      return false;
    }

    Collection<BagEntry> playerBags = findBags(playerEntry.getId());
    for (BagEntry bagEntry : playerBags) {
      int slot = getSlotByItemStack(bagEntry, itemWithoutAmount);
      if (slot == -1) {
        continue;
      }

      bagEntry.setSlotValue(slot, bagEntry.getSlotValue(slot) + droppedItem.getItemStack().getAmount());
      bagEntry.setHasToBeUpdated(true);
      ChatHelper.sendMessageInActionBar(player,
          translationService.get(MessageKey.PLUGIN_EVENT_BAG_COLLECT,
              droppedItem.getItemStack().getAmount(), droppedItem.getName()));
      player.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
      droppedItem.getItemStack().setAmount(0);
      return true;
    }

    return false;
  }

  public void savePendingBagUpdates(GroupEntry adminGroup) {
    int updatedBagCount = 0;

    for (BagEntry bagEntry : bagRegistry.findAll()) {
      if (bagEntry == null || !bagEntry.isHasToBeUpdated()) {
        continue;
      }

      databaseHelper.updateBagEntry(bagEntry);
      bagEntry.setHasToBeUpdated(false);
      updatedBagCount++;
    }

    if (updatedBagCount != 0) {
      sendMessageInChannel(
          translationService.get(MessageKey.PLUGIN_BAGS_SAVED, updatedBagCount),
          PLUGIN_NAME_CHAT_CONSOLE,
          ADMIN_CHANNEL,
          adminGroup
      );
    }
  }
}