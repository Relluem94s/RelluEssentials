package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Day implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("day")) {
			if (args.length == 0) {
				 if(sender instanceof Player) {
					 	Player p = (Player) sender;
					 	p.getWorld().setTime(0L);
					 	p.sendMessage(String.format(PLUGIN_COMMAND_DAY, p.getWorld().getName()));
				 }
				 return true;
			 }
			 else {
				 return false;
			 }
		}
		else {
			return false;
		}
		 
	}
}
