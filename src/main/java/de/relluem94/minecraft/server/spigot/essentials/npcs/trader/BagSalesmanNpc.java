package de.relluem94.minecraft.server.spigot.essentials.npcs.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

public class BagSalesmanNpc extends TraderNpc {

  private final ServiceContext serviceContext;
  private final RelluEssentials relluEssentials;

  public BagSalesmanNpc(ServiceContext serviceContext, RelluEssentials relluEssentials) {
    super(ItemConstants.PLUGIN_ITEM_NPC_BAGSALESMAN, Profession.LEATHERWORKER, Type.TRADER);
    this.serviceContext = serviceContext;
    this.relluEssentials = relluEssentials;
  }

  private ItemHelper resolveCloseItem() {
    return ItemRegistry.find(
            RegistryKey.of(relluEssentials, PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE))
        .orElseThrow();
  }

  @Override
  public Inventory getMainGUI() {
    Inventory inv = serviceContext.getBagService().getBagsInventory(true, getTitle());
    inv.setItem(53, resolveCloseItem().getCustomItem());

    return inv;
  }
}
