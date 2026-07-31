package de.relluem94.minecraft.server.spigot.essentials.npc.trader;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

public class BagSalesmanNpc extends TraderNpc {

  public BagSalesmanNpc() {
    super(ItemConstants.PLUGIN_ITEM_NPC_BAGSALESMAN, Profession.LEATHERWORKER, Type.TRADER);
  }

  @Override
  public Inventory getMainGUI() {
    Inventory inv = BagHelper.getBags(true, getTitle());
    inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());
    return inv;
  }
}
