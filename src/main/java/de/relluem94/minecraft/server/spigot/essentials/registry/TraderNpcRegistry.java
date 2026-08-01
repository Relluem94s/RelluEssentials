package de.relluem94.minecraft.server.spigot.essentials.registry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;

import de.relluem94.minecraft.server.spigot.essentials.enums.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.TraderNPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.TraderNpc.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


public class TraderNpcRegistry {

  private final List<ItemStack> npcItemStack = new ArrayList<>();
  private final List<String> npcName = new ArrayList<>();
  private final List<String> npcTraderTitle = new ArrayList<>();
  private final List<TraderNpc> npcs = new ArrayList<>();

  private ItemHelper resolveDisabledItem() {
    return ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow();
  }

  private ItemHelper resolveCloseItem() {
    return ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE))
        .orElseThrow();
  }

  public void init(List<TraderNPCEntry> traderNpcEntryList) {
    for (TraderNPCEntry ne : traderNpcEntryList) {
      new TraderNpc(ne) {
        @Override
        public Inventory getMainGUI() {
          Inventory inv = InventoryHelper.fillInventory(
              InventoryHelper.createInventory(NpcHelper.INV_SIZE, getTitle()),
              resolveDisabledItem().getCustomItem());
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
                  languageHelper.get(MessageKey.PLUGIN_ITEM_BUY_PRICE_MESSAGE,
                      PLUGIN_NAME_MONEY,
                      String.valueOf(buyPricePerItem),
                      PLUGIN_NAME_MONEY,
                      String.valueOf(buyPricePerItem * 64)),
                  languageHelper.get(MessageKey.PLUGIN_ITEM_SELL_PRICE_MESSAGE,
                      PLUGIN_NAME_MONEY,
                      String.valueOf(sellPricePerItem),
                      PLUGIN_NAME_MONEY,
                      String.valueOf(sellPricePerItem * 64))
              ));

              itemStack.setItemMeta(itemMeta);

              inv.setItem(slot, itemStack);
            }
            slot++;
          }
          inv.setItem(53, resolveCloseItem().getCustomItem());
          return inv;
        }
      };
    }
  }

  /**
   * Gives back a List of NPCs
   *
   * @return List of NPC
   */
  public List<TraderNpc> getNPCs() {
    return npcs;
  }

  /**
   * Adds a NPC.
   *
   * @param traderNpc NPC
   */
  public void addNPC(TraderNpc traderNpc) {
    npcs.add(traderNpc);
    npcItemStack.add(traderNpc.getItemHelper().getCustomItem());
    npcName.add(traderNpc.getName());

    if (traderNpc.getType().equals(Type.TRADER)) {
      npcTraderTitle.add(traderNpc.getTitle());
    }
  }

  /**
   * Gives back a List of ItemStacks (Spawn Eggs).
   *
   * @return List of ItemStack
   */
  public List<ItemStack> getNPCItemStackList() {
    return npcItemStack;
  }

  /**
   * Gives back a List of Strings with NPC Names.
   *
   * @return List of Strings
   */
  public List<String> getNPCNameList() {
    return npcName;
  }

  /**
   * Gives back a List of Strings with Trader NPC GUI Titles.
   *
   * @return List of Strings
   */
  public List<String> getNPCTraderTitleList() {
    return npcTraderTitle;
  }

  /**
   * Gives back a NPC from index.
   *
   * @param index int
   * @return NPC
   */
  public TraderNpc getNPC(int index) {
    return npcs.get(index);
  }
}