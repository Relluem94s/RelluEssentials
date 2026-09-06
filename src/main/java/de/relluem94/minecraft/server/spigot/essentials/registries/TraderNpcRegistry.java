package de.relluem94.minecraft.server.spigot.essentials.registries;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc.Type;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Registry responsible for managing and initializing Trader NPCs.
 */
public class TraderNpcRegistry {

  private final List<ItemStack> npcItemStack = new ArrayList<>();
  private final List<String> npcName = new ArrayList<>();
  private final List<String> npcTraderTitle = new ArrayList<>();
  /**
   * Retrieves the list of registered NPCs.
   */
  @Getter
  private final List<TraderNpc> npcs = new ArrayList<>();
  private final CustomItem disabledItem;
  private final CustomItem closeItem;
  private final TranslationService translationService;

  /**
   * Constructs a new TraderNpcRegistry.
   *
   * @param serviceContext the service context containing necessary services
   */
  public TraderNpcRegistry(ServiceContext serviceContext) {
    this.disabledItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED)).orElseThrow();
    this.closeItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE)).orElseThrow();
    this.translationService = serviceContext.getTranslationService();
  }

  /**
   * Initializes the registry with a list of Trader NPC entries.
   *
   * @param traderNpcEntryList the list of entries used to create NPCs
   */
  public void init(List<TraderNpcEntry> traderNpcEntryList) {
    for (TraderNpcEntry ne : traderNpcEntryList) {
      TraderNpc traderNpc = new TraderNpc(ne) {
        @Override
        public Inventory getMainGUI() {
          Inventory inv = InventoryHelper.fillInventory(
              InventoryHelper.createInventory(NpcHelper.INV_SIZE, getTitle()),
              disabledItem.toItemStack());
          int slot = 0;
          for (int i = 0; i < ne.getSlotNames().length; i++) {
            slot = InventoryHelper.getNextSlot(slot);
            if (!ne.getSlotName(i).equals("AIR")) {
              ItemStack itemStack = new ItemStack(Material.valueOf(ne.getSlotName(i)), 1);

              int buyPricePerItem = ItemPrice.from(itemStack.getType()).getBuyPrice();
              int sellPricePerItem = ItemPrice.from(itemStack.getType()).getSellPrice();

              ItemMeta itemMeta = itemStack.getItemMeta();
              Objects.requireNonNull(itemMeta).getPersistentDataContainer()
                  .set(itemSellPrice(), PersistentDataType.INTEGER, sellPricePerItem);
              Objects.requireNonNull(itemMeta).getPersistentDataContainer()
                  .set(itemBuyPrice(), PersistentDataType.INTEGER, buyPricePerItem);

              itemMeta.setLore(List.of(
                  translationService.get(MessageKey.PLUGIN_ITEM_BUY_PRICE_MESSAGE,
                      PLUGIN_NAME_MONEY, String.valueOf(buyPricePerItem), PLUGIN_NAME_MONEY,
                      String.valueOf(buyPricePerItem * 64)),
                  translationService.get(MessageKey.PLUGIN_ITEM_SELL_PRICE_MESSAGE,
                      PLUGIN_NAME_MONEY, String.valueOf(sellPricePerItem), PLUGIN_NAME_MONEY,
                      String.valueOf(sellPricePerItem * 64))));

              itemStack.setItemMeta(itemMeta);

              inv.setItem(slot, itemStack);
            }
            slot++;
          }
          inv.setItem(53, closeItem.toItemStack());
          return inv;
        }
      };
      addNpc(traderNpc);
    }
  }

  /**
   * Adds a new NPC to the registry and updates associated lists.
   *
   * @param traderNpc the NPC to add
   */
  public void addNpc(TraderNpc traderNpc) {
    npcs.add(traderNpc);
    npcItemStack.add(traderNpc.getCustomItem().toItemStack());
    npcName.add(traderNpc.getName());

    if (traderNpc.getType().equals(Type.TRADER) || traderNpc.getType().equals(Type.ENCHANTER)
        || traderNpc.getType().equals(Type.BEEKEEPER)) {
      npcTraderTitle.add(traderNpc.getTitle());
    }
  }

  /**
   * Retrieves the list of ItemStacks used to spawn the NPCs.
   *
   * @return a list of {@link ItemStack}
   */
  public List<ItemStack> getNpcItemStackList() {
    return npcItemStack;
  }

  /**
   * Retrieves the list of names of the registered NPCs.
   *
   * @return a list of NPC names
   */
  public List<String> getNpcNameList() {
    return npcName;
  }

  /**
   * Retrieves the list of titles for Trader-type NPCs.
   *
   * @return a list of NPC trader titles
   */
  public List<String> getNpcTraderTitleList() {
    return npcTraderTitle;
  }

  /**
   * Retrieves a specific NPC by its index in the registry.
   *
   * @param index the index of the NPC to retrieve
   * @return the {@link TraderNpc} at the specified index
   */
  public TraderNpc getNpc(int index) {
    return npcs.get(index);
  }
}