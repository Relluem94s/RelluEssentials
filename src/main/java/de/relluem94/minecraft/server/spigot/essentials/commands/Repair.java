package de.relluem94.minecraft.server.spigot.essentials.commands;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_REPAIR;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Repair implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_REPAIR)) {
            if (args.length == 0) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {

                        ItemStack item = p.getInventory().getItemInMainHand();
                        ItemMeta im = item.getItemMeta();

                        if (im instanceof Damageable dmg && dmg.hasDamage()) {
                            dmg.setDamage(0);
                            item.setItemMeta(im);
                            p.sendMessage(String.format(PLUGIN_COMMAND_REPAIR, p.getInventory().getItemInMainHand().getType().name()));
                        }
                        else{
                            p.sendMessage(String.format(PLUGIN_COMMAND_CANNOT_REPAIR, p.getInventory().getItemInMainHand().getType().name()));
                        }

                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {

                            ItemStack item = target.getInventory().getItemInMainHand();
                            ItemMeta im = item.getItemMeta();

                            if (im instanceof Damageable dmg && dmg.hasDamage()) {
                                dmg.setDamage(0);
                                item.setItemMeta(im);
                                p.sendMessage(String.format(PLUGIN_COMMAND_REPAIR, target.getInventory().getItemInMainHand().getType().name()));
                                target.sendMessage(String.format(PLUGIN_COMMAND_REPAIR_PLAYER, target.getInventory().getItemInMainHand().getType().name()));
                            }
                            else{
                                p.sendMessage(String.format(PLUGIN_COMMAND_CANNOT_REPAIR, p.getInventory().getItemInMainHand().getType().name()));
                            }

                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }
}
