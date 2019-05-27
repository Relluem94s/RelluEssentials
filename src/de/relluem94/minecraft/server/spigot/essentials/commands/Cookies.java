package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Cookies implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			if(sender instanceof Player) {
				Player p = (Player) sender;	
				return getCookies(command, getCookie(p), p);
			}
		}
		else {
			 Player target = Bukkit.getPlayer(args[0]);
			 if(target != null) {
				 if(sender instanceof Player) {
						Player p = (Player) sender;	
						return getCookies(command, getCookie(target, p), p);
				 }
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
		else {
			return false;
		}
		
	}
	
	private ItemStack getCookie(Player p) {
		ItemStack is = new ItemStack(Material.COOKIE, 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(PLUGIN_COMMAND_COOKIES_DISPLAYNAME);
		im.setLore(Arrays.asList(String.format(PLUGIN_COMMAND_COOKIES_LORE_1, p.getCustomName()), PLUGIN_COMMAND_COOKIES_LORE_3));
		is.setItemMeta(im);	
		return is;
	}
	
	private ItemStack getCookie(Player t, Player p) {
		ItemStack is = new ItemStack(Material.COOKIE, 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(PLUGIN_COMMAND_COOKIES_DISPLAYNAME);
		im.setLore(Arrays.asList(String.format(PLUGIN_COMMAND_COOKIES_LORE_1, p.getCustomName()), PLUGIN_COMMAND_COOKIES_LORE_2));
		is.setItemMeta(im);
		return is;
	}

}
