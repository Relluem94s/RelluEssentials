package de.relluem94.minecraft.server.spigot.essentials.npcs.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

/**
 * NPC implementation representing a bag salesman trader.
 *
 * <p>Provides a GUI inventory displaying available bags for purchase,
 * including a close button at the last inventory slot.</p>
 *
 * @author rellu
 */
public class BagSalesmanNpc extends TraderNpc {

  private final ServiceContext serviceContext;

  /**
   * Creates a new BagSalesmanNpc with the given service context.
   *
   * @param serviceContext the service context used to access required services
   */
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
  public Inventory getMainGui() {
    Inventory inv = serviceContext.getBagService().getBagsInventory(true, getTitle());
    inv.setItem(53, resolveCloseItem().toItemStack());

    return inv;
  }
}
