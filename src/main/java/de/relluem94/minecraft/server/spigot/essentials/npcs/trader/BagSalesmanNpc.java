package de.relluem94.minecraft.server.spigot.essentials.npcs.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

public class BagSalesmanNpc extends TraderNpc {

  private final ServiceContext serviceContext;

  public BagSalesmanNpc(ServiceContext serviceContext) {
    super(ItemConstants.PLUGIN_ITEM_NPC_BAGSALESMAN, Profession.LEATHERWORKER, Type.TRADER);
    this.serviceContext = serviceContext;
  }

  private CustomItem resolveCloseItem() {
    return serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE)).orElseThrow();
  }

  @Override
  public Inventory getMainGUI() {
    Inventory inv = serviceContext.getBagService().getBagsInventory(true, getTitle());
    inv.setItem(53, resolveCloseItem().toItemStack());

    return inv;
  }
}
