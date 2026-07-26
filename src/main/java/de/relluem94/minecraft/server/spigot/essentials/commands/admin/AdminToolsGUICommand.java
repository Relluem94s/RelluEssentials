package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.CustomInventory;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import lombok.NonNull;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class AdminToolsGUICommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        if (!Permission.isAuthorized(player, Groups.getGroup("admin").getId())) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
            return;
        }
        org.bukkit.inventory.Inventory inv = InventoryHelper.getCustomItemInventory(
                new CustomInventory(ItemHelper.Type.ADMIN_TOOL, 9,
                        Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dAdmin Tools"));
        InventoryHelper.openInventory(player, inv);
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && Admin.Commands.ADMIN_TOOLS.getName().equalsIgnoreCase(args[0]);
    }
}