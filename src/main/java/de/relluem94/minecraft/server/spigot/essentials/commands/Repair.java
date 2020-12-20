package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Repair implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("repair")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.MOD.getId())) {

                        ItemStack item = p.getInventory().getItemInMainHand();
                        ItemMeta im = item.getItemMeta();

                        if (im instanceof Damageable) {
                            Damageable dmg = (Damageable) im;
                            if (dmg.hasDamage()) {
                                dmg.setDamage(0);
                            }
                            item.setItemMeta(im);
                        }

                        p.sendMessage(String.format(PLUGIN_COMMAND_REPAIR, p.getInventory().getItemInMainHand().getType().toString()));
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.MOD.getId())) {

                            ItemStack item = target.getInventory().getItemInMainHand();
                            ItemMeta im = item.getItemMeta();

                            if (im instanceof Damageable) {
                                Damageable dmg = (Damageable) im;
                                if (dmg.hasDamage()) {
                                    dmg.setDamage(0);
                                }
                                item.setItemMeta(im);
                            }

                            p.sendMessage(String.format(PLUGIN_COMMAND_REPAIR, target.getInventory().getItemInMainHand().getType().toString()));
                            target.sendMessage(String.format(PLUGIN_COMMAND_REPAIR_PLAYER, target.getInventory().getItemInMainHand().getType().toString()));
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
