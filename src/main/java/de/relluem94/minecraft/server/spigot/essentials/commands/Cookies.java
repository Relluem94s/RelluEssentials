package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Cookies implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Permission.isAuthorized(p, 2)) {
                if (args.length == 0) {
                    return getCookies(command, getCookie(p), p);
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                       p.sendMessage(String.format(PLUGIN_COMMAND_COOKIES_PLAYER, target.getCustomName()));

                        return getCookies(command, getCookie(p), target);
                    }
                }
            } else {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }
        }

        return false;
    }

    private boolean getCookies(Command command, ItemStack is, Player p) {
        if (command.getName().equalsIgnoreCase("cookie")) {

            p.getWorld().dropItem(p.getLocation(), is);
            p.sendMessage(String.format(PLUGIN_COMMAND_COOKIES, p.getCustomName()));
            return true;
        }

        return false;
    }

    private ItemStack getCookie(Player p) {
        ItemStack is = new ItemStack(Material.COOKIE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(PLUGIN_COMMAND_COOKIES_DISPLAYNAME);
        im.setLore(Arrays.asList(String.format(PLUGIN_COMMAND_COOKIES_LORE_1, p.getCustomName()),
                PLUGIN_COMMAND_COOKIES_LORE_3));
        is.setItemMeta(im);
        return is;
    }

}
