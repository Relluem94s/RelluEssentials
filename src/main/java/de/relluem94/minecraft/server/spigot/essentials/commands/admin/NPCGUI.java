package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import org.bukkit.command.CommandSender;

public class NPCGUI {
    public static void npcGUI(@org.jspecify.annotations.NonNull CommandSender sender) {
        org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(
                InventoryHelper.createInventory(18,
                        Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dNPCs"),
                CustomItems.npc_gui_disabled.getCustomItem()
        );

        for (int i = 0; i < RelluEssentials.getInstance().getNpcAPI().getNPCs().size(); i++) {
            inv.setItem(i, RelluEssentials.getInstance().getNpcAPI().getNPCs().get(i).getItemHelper().getCustomItem());
        }

        InventoryHelper.openInventory(sender, inv);
    }
}
