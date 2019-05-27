package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

public class PortableCraftingBench implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("craft")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;	
				p.openWorkbench(p.getLocation(), true);
				p.sendMessage(String.format(PLUGIN_COMMAND_CRAFTINGBENCH, p.getCustomName()));
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
}
