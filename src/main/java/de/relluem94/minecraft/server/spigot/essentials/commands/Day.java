package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Day implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("day")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (Permission.isAuthorized(p, 4)) {
						p.getWorld().setTime(0L);
						p.sendMessage(String.format(PLUGIN_COMMAND_DAY, p.getWorld().getName()));
						return true;
					}
					else {
						p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
						return true;
					}
				}
			}
		}
		return false;
	}
}
