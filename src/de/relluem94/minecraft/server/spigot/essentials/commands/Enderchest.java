package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Enderchest implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("enderchest")) {
			if (args.length == 0) {
				 if(sender instanceof Player) {
					 	Player p = (Player) sender;
					 	p.openInventory(p.getEnderChest());
					 	p.sendMessage(PLUGIN_COMMAND_ENDERCHEST);
				 }
				 return true;
			 }
			 else {
				 Player target = Bukkit.getPlayer(args[0]);
				 if(target != null) {
					 if(sender instanceof Player) {
						 	Player p = (Player) sender;
						 	p.openInventory(target.getEnderChest());
							p.sendMessage(String.format(PLUGIN_COMMAND_ENDERCHEST_PLAYER, target.getCustomName()));
					 }
					 
				 }
				 return true;
			 }
		}
		else {
			return false;
		}
	}
}
