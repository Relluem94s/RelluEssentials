package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("spawn")) {
			if (args.length == 0) {
				 if(sender instanceof Player) {
					 	Player p = (Player) sender;
					 	p.teleport(p.getWorld().getSpawnLocation());
					 	p.sendMessage(String.format(PLUGIN_COMMAND_SPAWN, p.getWorld().getName()));
				 }
				 return true;
			 }
			 else {
				 Player target = Bukkit.getPlayer(args[0]);
				 if(target != null) {
					 target.teleport(target.getWorld().getSpawnLocation());
					 target.sendMessage(String.format(PLUGIN_COMMAND_SPAWN, target.getWorld().getName()));
				 }
				 return true;
			 }
		}
		else {
			return false;
		}
	}
}
