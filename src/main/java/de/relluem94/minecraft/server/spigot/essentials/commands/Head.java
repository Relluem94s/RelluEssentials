package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HEAD;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Head implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_HEAD)) {
            if (args.length == 1) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        String owner = args[0];
                        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
                        SkullMeta sm = (SkullMeta) itemStack.getItemMeta();
                        sm.setOwningPlayer(Bukkit.getOfflinePlayer(Bukkit.getPlayer(owner).getUniqueId()));
                        // ERROR IF NOT ONLINE
                        
                        sm.setDisplayName(owner);
                        itemStack.setItemMeta((ItemMeta) sm);
                        p.getInventory().addItem(itemStack);
                        p.updateInventory();
                        sendMessage(p, String.format(PLUGIN_COMMAND_HEAD, owner));
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
