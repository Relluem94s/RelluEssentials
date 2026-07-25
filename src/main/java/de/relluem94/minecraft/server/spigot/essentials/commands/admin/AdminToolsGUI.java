package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.CustomInventory;
import org.bukkit.command.CommandSender;

public class AdminToolsGUI {
    public static void adminToolsGUI(@org.jspecify.annotations.NonNull CommandSender sender) {
        org.bukkit.inventory.Inventory inv = InventoryHelper.getCustomItemInventory(
                new CustomInventory(ItemHelper.Type.ADMIN_TOOL, 9,
                        Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dAdmin Tools"));
        InventoryHelper.openInventory(sender, inv);
    }
}
