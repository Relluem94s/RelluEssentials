package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_COMMAND_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper.BAG_SIZE;
import static de.relluem94.minecraft.server.spigot.essentials.listeners.BetterChatFormat.ADMIN_CHANNEL;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BagRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BagTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagTypeRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;
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
  private final BagRepository bagRepository;
  private final BagTypeRegistry bagTypeRegistry;
  private final ServiceContext serviceContext;
  private final List<ItemStack> bagBlocks2collect = new ArrayList<>();

  public BagService(
      ServiceContext serviceContext,
      BagRegistry bagRegistry,
      BagRepository bagRepository,
      BagTypeRegistry bagTypeRegistry,
      BagTypeRepository bagTypeRepository
  ) {
    this.serviceContext = serviceContext;
    this.bagRegistry = bagRegistry;
    this.bagRepository = bagRepository;
    this.bagTypeRegistry = bagTypeRegistry;
    for (BagTypeEntry bagTypeEntry : this.bagTypeRegistry.getAll()) {
      Collections.addAll(this.bagBlocks2collect, getItemStacks(bagTypeEntry));
    }
  }

  /**
   * Finds a {@link BagEntry} by player id and bag type id from the in-memory registry.
   *
   * @param playerId  the player id to search for
   * @param bagTypeId the bag type id to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<BagEntry> findBag(int playerId, int bagTypeId) {
    return bagRegistry.findByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  /**
   * Returns all {@link BagEntry} instances for the given player id from the in-memory registry.
   *
   * @param playerId the player id to look up
   * @return a {@link Collection} of {@link BagEntry} instances
   */
  public Collection<BagEntry> findBags(int playerId) {
    return bagRegistry.findAllByPlayerId(playerId);
  }

  /**
   * Finds a {@link BagTypeEntry} by a partial name match.
   *
   * @param displayName the partial name to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<BagTypeEntry> findBagTypeByPartialName(String displayName) {
    return bagTypeRegistry.findByPartialName(displayName);
  }

  /**
   * Finds a {@link BagTypeEntry} by its unique id.
   *
   * @param id the unique id to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<BagTypeEntry> findBagTypeById(int id) {
    return bagTypeRegistry.findById(id);
  }

  /**
   * Checks whether the given player owns a bag of the given type.
   *
   * @param playerId  the player id to check
   * @param bagTypeId the bag type id to check
   * @return {@code true} if the player owns a bag of this type, {@code false} otherwise
   */
  public boolean hasBag(int playerId, int bagTypeId) {
    return bagRegistry.existsByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  /**
   * Checks whether the given player owns a bag of the given type.
   *
   * @param bagTypeId   the bag type id to check
   * @param playerEntry the {@link PlayerEntry} to check
   * @return {@code true} if the player owns a bag of this type, {@code false} otherwise
   */
  public boolean hasBag(int bagTypeId, @NotNull PlayerEntry playerEntry) {
    return hasBag(playerEntry.getId(), bagTypeId);
  }

  /**
   * Checks whether the given player owns any bags.
   *
   * @param playerId the player id to check
   * @return {@code true} if the player owns at least one bag, {@code false} otherwise
   */
  public boolean hasBags(int playerId) {
    return bagRegistry.existsByPlayerId(playerId);
  }

  /**
   * Inserts a new bag for the given player and bag type, persists it to the database,
   * and registers it in the in-memory registry.
   *
   * @param playerId  the player id to insert for
   * @param bagTypeId the bag type id to insert
   * @return the newly created and registered {@link BagEntry}
   */
  public BagEntry insertBag(int playerId, int bagTypeId) {
    BagEntry newBagEntry = bagRepository.insert(playerId, bagTypeId);
    bagRegistry.register(newBagEntry);
    return newBagEntry;
  }

  /**
   * Persists the current state of the given {@link BagEntry} to the database.
   *
   * @param bagEntry the {@link BagEntry} to update
   */
  public void updateBag(BagEntry bagEntry) {
    bagRepository.update(bagEntry);
  }

  /**
   * Returns the inventory slot index for the given {@link ItemStack} within the given
   * {@link BagEntry}, or {@code -1} if the item is not part of this bag type.
   *
   * @param bagEntry  the {@link BagEntry} to search in
   * @param itemStack the {@link ItemStack} to find the slot for
   * @return the slot index, or {@code -1} if not found
   */
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

  /**
   * Collects all matching dropped {@link Item} entities into the player's bags.
   *
   * @param droppedItems the list of dropped {@link Item} entities to check
   * @param player       the {@link Player} collecting the items
   * @param playerEntry  the {@link PlayerEntry} of the collecting player
   * @return a {@link List} of {@link Item} entities that were collected
   */
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

        bagEntry.setSlotValue(slot,
            bagEntry.getSlotValue(slot) + droppedItem.getItemStack().getAmount());
        bagEntry.setHasToBeUpdated(true);
        serviceContext.getChatService().sendMessageInActionBar(player,
            serviceContext.getTranslationService().get(MessageKey.PLUGIN_EVENT_BAG_COLLECT,
                droppedItem.getItemStack().getAmount(), droppedItem.getName()));
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
        collectedItems.add(droppedItem);
      }
    }

    return collectedItems;
  }

  /**
   * Collects all matching {@link ItemStack} instances into the player's bags.
   *
   * @param itemStacks  the list of {@link ItemStack} instances to check
   * @param player      the {@link Player} collecting the items
   * @param playerEntry the {@link PlayerEntry} of the collecting player
   * @return a {@link List} of {@link ItemStack} instances that were collected
   */
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
        serviceContext.getChatService().sendMessageInActionBar(player,
            serviceContext.getTranslationService().get(MessageKey.PLUGIN_EVENT_BAG_COLLECT,
                itemStack.getAmount(),
                itemStack.getType().name().replace("_", " ").toLowerCase()));
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
        collectedStacks.add(itemStack);
      }
    }

    return collectedStacks;
  }

  /**
   * Collects a single dropped {@link Item} into the player's bags if it matches any bag slot.
   *
   * @param droppedItem the dropped {@link Item} to collect
   * @param player      the {@link Player} collecting the item
   * @param playerEntry the {@link PlayerEntry} of the collecting player
   * @return {@code true} if the item was collected, {@code false} otherwise
   */
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

      bagEntry.setSlotValue(slot,
          bagEntry.getSlotValue(slot) + droppedItem.getItemStack().getAmount());
      bagEntry.setHasToBeUpdated(true);
      serviceContext.getChatService().sendMessageInActionBar(player,
          serviceContext.getTranslationService().get(MessageKey.PLUGIN_EVENT_BAG_COLLECT,
              droppedItem.getItemStack().getAmount(), droppedItem.getName()));
      player.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
      droppedItem.getItemStack().setAmount(0);
      return true;
    }

    return false;
  }

  /**
   * Flushes all pending bag updates to the database and notifies the admin group.
   *
   * @param adminGroup the {@link GroupEntry} representing the admin group to notify
   */
  public void savePendingBagUpdates(GroupEntry adminGroup) {
    int updatedBagCount = 0;

    for (BagEntry bagEntry : bagRegistry.findAll()) {
      if (bagEntry == null || !bagEntry.isHasToBeUpdated()) {
        continue;
      }

      bagRepository.update(bagEntry);
      bagEntry.setHasToBeUpdated(false);
      updatedBagCount++;
    }

    if (updatedBagCount != 0) {
      serviceContext.getChatService().sendMessageInChannel(
          serviceContext.getTranslationService().get(MessageKey.PLUGIN_BAGS_SAVED, updatedBagCount),
          PLUGIN_NAME_CHAT_CONSOLE,
          ADMIN_CHANNEL,
          adminGroup
      );
    }
  }

  /**
   * Returns all {@link BagEntry} instances for the given player id.
   *
   * @param playerId the player id to look up
   * @return a {@link Collection} of {@link BagEntry} instances
   */
  public Collection<BagEntry> getBags(int playerId) {
    return bagRegistry.findAllByPlayerId(playerId);
  }

  /**
   * Builds and returns the bag overview {@link Inventory} for the given player,
   * showing only bags the player owns.
   *
   * @param playerEntry the {@link PlayerEntry} of the player
   * @return the populated bag overview {@link Inventory}
   */
  public Inventory getBagsInventory(PlayerEntry playerEntry) {
    String title = serviceContext.getTranslationService().get(MessageKey.PLUGIN_BAG_GUI_TITLE);
    Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(54, title), resolveDisabledItem());
    ListIterator<BagTypeEntry> bagTypeEntryListIterator = bagTypeRegistry.getAll().listIterator();
    int slot = 0;
    while (bagTypeEntryListIterator.hasNext()) {
      slot = InventoryHelper.getNextSlot(slot);
      BagTypeEntry bte = bagTypeEntryListIterator.next();
      if (hasBag(playerEntry.getId(), bte.getId())) {
        inv.setItem(slot, getItem(bte, false).getCustomItem());
        slot++;
      }
    }
    return inv;
  }

  /**
   * Builds and returns a bag overview {@link Inventory} showing all bag types,
   * optionally in NPC context.
   *
   * @param npc   {@code true} if displayed in NPC context, {@code false} otherwise
   * @param title the title of the inventory
   * @return the populated bag overview {@link Inventory}
   */
  public Inventory getBagsInventory(boolean npc, String title) {
    Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(54, title), resolveDisabledItem());
    ListIterator<BagTypeEntry> bagTypeEntryListIterator = bagTypeRegistry.getAll().listIterator();
    int slot = 0;
    while (bagTypeEntryListIterator.hasNext()) {
      slot = InventoryHelper.getNextSlot(slot);
      BagTypeEntry bte = bagTypeEntryListIterator.next();
      inv.setItem(slot, getItem(bte, npc).getCustomItem());
      slot++;
    }
    return inv;
  }

  /**
   * Builds and returns the detailed bag {@link Inventory} for the given player and bag type,
   * showing all slot contents with their amounts.
   *
   * @param bagTypeId   the bag type id to open
   * @param playerEntry the {@link PlayerEntry} of the player
   * @return the populated bag {@link Inventory}, or {@code null} if the player does not own this bag
   */
  public @Nullable Inventory getBagInventory(int bagTypeId, @NotNull PlayerEntry playerEntry) {
    Optional<BagEntry> optionalBagEntry = findBag(playerEntry.getId(), bagTypeId);

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

  /**
   * Purchases a bag of the given type for the given player, deducting the cost,
   * persisting the new bag to the database, and registering it in the in-memory registry.
   *
   * @param bagType     the {@link BagTypeEntry} to purchase
   * @param player      the {@link Player} purchasing the bag
   * @param playerEntry the {@link PlayerEntry} of the purchasing player
   */
  public void purchaseBag(
      @NonNull BagTypeEntry bagType,
      @NonNull Player player,
      @NonNull PlayerEntry playerEntry
  ) {
    playerEntry.setPurse(playerEntry.getPurse() - bagType.getCost());
    playerEntry.setUpdatedBy(playerEntry.getId());
    playerEntry.setHasToBeUpdated(true);
    BagEntry newBagEntry = bagRepository.insert(bagType.getId(), playerEntry.getId());
    bagRegistry.register(newBagEntry);

    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BAGS_BOUGHT, bagType.getDisplayName()));
  }

  private ItemStack resolveDisabledItem() {
    return serviceContext.getItemService().find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow()
        .getCustomItem();
  }

  @Contract("_, _ -> new")
  private @NotNull ItemHelper getItem(BagTypeEntry bte, boolean npc) {
    String[] lore;
    if (npc) {
      lore = new String[]{
          serviceContext.getTranslationService().get(MessageKey.PLUGIN_BAG_CLICK_TO_BUY),
          serviceContext.getTranslationService().get(MessageKey.PLUGIN_BAG_COST_TO_BUY,
              bte.getCost())
      };
    } else {
      lore = new String[]{
          serviceContext.getTranslationService().get(MessageKey.PLUGIN_BAG_CLICK_TO_OPEN)};
    }
    return new ItemHelper(PlayerHeadHelper.getCustomSkull(CustomHeads.BAG), bte.getDisplayName(),
        Type.NPC_GUI, Rarity.NONE, Arrays.asList(lore));
  }

  /**
   * Returns an array of {@link ItemStack} instances representing all slots of the given
   * {@link BagTypeEntry}.
   *
   * @param bte the {@link BagTypeEntry} to build item stacks for
   * @return an array of {@link ItemStack} instances with one entry per bag slot
   */
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
    lore.add(serviceContext.getTranslationService().get(MessageKey.PLUGIN_BAG_AMOUNT, value));
    lore.add(serviceContext.getTranslationService().get(MessageKey.PLUGIN_BAG_RETRIEVE));

    im.setLore(lore);
    is.setItemMeta(im);

    return is;
  }

  public List<String> getBagTypeNamesForPlayer(int playerId) {
    return findBags(playerId)
        .stream()
        .map(bag -> bag.getBagType().getName().toLowerCase())
        .collect(Collectors.toList());
  }

}