package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class NpcGuiCommand implements SubCommand {

  private final ServiceContext serviceContext;

  public NpcGuiCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!serviceContext.getGroupService().isSenderAuthorized(player, "admin")) {
      player.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }

    ItemHelper disabledItem = ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow();

    org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(18,
            Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dNPCs"),
        disabledItem.getCustomItem()
    );

    for (int i = 0; i < serviceContext.getTraderNpcRegistry().getNPCs().size();
        i++) {
      inv.setItem(i,
          serviceContext.getTraderNpcRegistry().getNPCs().get(i).getItemHelper()
              .getCustomItem());
    }

    InventoryHelper.openInventory(player, inv);
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.NPC.getName().equalsIgnoreCase(args[0]);
  }
}