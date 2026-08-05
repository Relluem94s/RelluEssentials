package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_COMMAND_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper.BAG_SIZE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessageInChannel;
import static de.relluem94.minecraft.server.spigot.essentials.listeners.BetterChatFormat.ADMIN_CHANNEL;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.BagRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

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

  public Collection<BagEntry> getBags(int playerFK) {
    return bagRegistry.findAllByPlayerId(playerFK);
  }

  public Inventory getBagsInventory(PlayerEntry pe) {
    String MAIN_GUI = translationService.get(MessageKey.PLUGIN_BAG_GUI_TITLE);
    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI),
        resolveDisabledItem());
    ListIterator<BagTypeEntry> bagTypeEntryListIterator = RelluEssentials.getInstance()
        .getBagTypeRegistry()
        .getAll().listIterator();
    int slot = 0;
    while (bagTypeEntryListIterator.hasNext()) {
      slot = InventoryHelper.getNextSlot(slot);
      BagTypeEntry bte = bagTypeEntryListIterator.next();
      if (hasBag(pe.getId(), bte.getId())) {
        inv.setItem(slot, getItem(bte, false).getCustomItem());
        slot++;
      }
    }
    return inv;
  }

  public Inventory getBagsInventory(boolean npc, String title) {
    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, title),
        resolveDisabledItem());

    ListIterator<BagTypeEntry> bagTypeEntryListIterator = RelluEssentials.getInstance()
        .getBagTypeRegistry()
        .getAll().listIterator();

    int slot = 0;
    while (bagTypeEntryListIterator.hasNext()) {
      slot = InventoryHelper.getNextSlot(slot);
      BagTypeEntry bte = bagTypeEntryListIterator.next();
      inv.setItem(slot, getItem(bte, npc).getCustomItem());
      slot++;
    }
    return inv;
  }


  public @Nullable Inventory getBagInventory(int type, @NotNull PlayerEntry pe) {
    Optional<BagEntry> optionalBagEntry = findBag(pe.getId(), type);

    if (!optionalBagEntry.isPresent()) {
      return null;
    }
    BagEntry bagEntry = optionalBagEntry.get();

    Inventory inv = InventoryHelper.createInventory(54,
        PLUGIN_FORMS_COMMAND_PREFIX + bagEntry.getBagType().getDisplayName());
    InventoryHelper.fillInventory(inv, resolveDisabledItem());

    inv.setItem(10, getItemStack(bagEntry, 0));
    inv.setItem(11, getItemStack(bagEntry, 1));
    inv.setItem(12, getItemStack(bagEntry, 2));
    inv.setItem(13, getItemStack(bagEntry, 3));
    inv.setItem(14, getItemStack(bagEntry, 4));
    inv.setItem(15, getItemStack(bagEntry, 5));
    inv.setItem(16, getItemStack(bagEntry, 6));

    inv.setItem(19, getItemStack(bagEntry, 7));
    inv.setItem(20, getItemStack(bagEntry, 8));
    inv.setItem(21, getItemStack(bagEntry, 9));
    inv.setItem(22, getItemStack(bagEntry, 10));
    inv.setItem(23, getItemStack(bagEntry, 11));
    inv.setItem(24, getItemStack(bagEntry, 12));
    inv.setItem(25, getItemStack(bagEntry, 13));

    inv.setItem(28, getItemStack(bagEntry, 14));
    inv.setItem(29, getItemStack(bagEntry, 15));
    inv.setItem(30, getItemStack(bagEntry, 16));
    inv.setItem(31, getItemStack(bagEntry, 17));
    inv.setItem(32, getItemStack(bagEntry, 18));
    inv.setItem(33, getItemStack(bagEntry, 19));
    inv.setItem(34, getItemStack(bagEntry, 20));

    inv.setItem(37, getItemStack(bagEntry, 21));
    inv.setItem(38, getItemStack(bagEntry, 22));
    inv.setItem(39, getItemStack(bagEntry, 23));
    inv.setItem(40, getItemStack(bagEntry, 24));
    inv.setItem(41, getItemStack(bagEntry, 25));
    inv.setItem(42, getItemStack(bagEntry, 26));
    inv.setItem(43, getItemStack(bagEntry, 27));

    return inv;
  }

  public void purchaseBag(@NonNull BagTypeEntry bagType, @NonNull Player player, @NonNull PlayerEntry playerEntry) {
    playerEntry.setPurse(playerEntry.getPurse() - bagType.getCost());
    playerEntry.setUpdatedBy(playerEntry.getId());
    playerEntry.setHasToBeUpdated(true);
    databaseHelper.insertBag(bagType.getId(), playerEntry.getId());
    BagEntry newBagEntry = databaseHelper.getBag(bagType.getId(), playerEntry.getId());
    bagRegistry.register(newBagEntry);

    player.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_BOUGHT,
        bagType.getDisplayName()));
  }


  private ItemStack resolveDisabledItem() {
    return ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow()
        .getCustomItem();
  }

  @Contract("_, _ -> new")
  private @NotNull ItemHelper getItem(BagTypeEntry bte, boolean npc) {
    String[] lore;
    if (npc) {
      lore = new String[]{
          translationService.get(MessageKey.PLUGIN_BAG_CLICK_TO_BUY),
          translationService.get(MessageKey.PLUGIN_BAG_COST_TO_BUY, bte.getCost())
      };
    } else {
      lore = new String[]{translationService.get(MessageKey.PLUGIN_BAG_CLICK_TO_OPEN)};
    }
    return new ItemHelper(PlayerHeadHelper.getCustomSkull(CustomHeads.BAG), bte.getDisplayName(),
        Type.NPC_GUI, Rarity.NONE, Arrays.asList(lore));
  }

  public ItemStack @NotNull [] getItemStacks(BagTypeEntry bte) {
    ItemStack[] isa = new ItemStack[BAG_SIZE];
    for (int i = 0; i < BAG_SIZE; i++) {
      isa[i] = getItemStack(bte, i);
    }
    return isa;
  }

  private ItemStack getItemStack(@NotNull BagTypeEntry bte, int slot) {
    String name = bte.getSlotName(slot);

    if (name == null) {
      return resolveDisabledItem();
    }

    Material mat = Material.matchMaterial(name);

    if (mat == null) {
      mat = Material.AIR;
    }

    return new ItemStack(mat, 1);
  }


  private ItemStack getItemStack(@NotNull BagEntry be, int slot) {
    String name = be.getBagType().getSlotName(slot);
    int value = be.getSlotValue(slot);

    if (name == null) {
      return resolveDisabledItem();
    }

    Material mat = Material.matchMaterial(name);

    if (mat == null) {
      mat = Material.AIR;
    }

    if (Material.AIR.equals(mat)) {
      return resolveDisabledItem();
    }

    ItemStack is = new ItemStack(mat, 1);
    ItemMeta im = is.getItemMeta();

    if (im == null) {
      return is;
    }

    List<String> lore = new ArrayList<>();
    lore.add(translationService.get(MessageKey.PLUGIN_BAG_AMOUNT, value));
    lore.add(translationService.get(MessageKey.PLUGIN_BAG_RETRIEVE));

    im.setLore(lore);
    is.setItemMeta(im);

    return is;
  }
}