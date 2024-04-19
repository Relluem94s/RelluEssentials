package de.relluem94.minecraft.server.spigot.essentials.commands;

import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.rellulib.utils.StringUtils.*;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_RENAME;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Rename implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_RENAME)) {
            if (args.length >= 1) {
                if (isPlayer(sender)) {
                    rename((Player) sender, args);
                    return true;
                }
            } else {
                sender.sendMessage(PLUGIN_COMMAND_RENAME_INFO);
                return true;
            }
        }
        return false;
    }

    private void rename(Player p, String[] args) {

        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return;
        }

        String message = implode(0, args);
        message = replaceSymbols(replaceColor(message));
        ItemStack is = p.getInventory().getItemInMainHand();
        ItemMeta im = is.getItemMeta();
        if (!is.getType().equals(Material.AIR) && im != null) {
            im.setDisplayName(message);
            is.setItemMeta(im);
            p.sendMessage(PLUGIN_COMMAND_RENAME);

        } else {
            p.sendMessage(PLUGIN_COMMAND_RENAME_AIR);
        }
    }
}
