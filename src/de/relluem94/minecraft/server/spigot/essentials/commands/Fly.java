package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		 if (args.length == 0) {
			 if(sender instanceof Player) {
				 Player p = (Player) sender;
				 return flyMode(command, p);
			 }
		 }
		 else {
			 Player target = Bukkit.getPlayer(args[0]);
			 if(target != null) {
				 return flyMode(command, target);
			 }
		 }
		 return false;
	 }
	 
	 
	 private boolean flyMode(Command command, Player p) {
		 if (command.getName().equalsIgnoreCase("fly")) {
			 p.setAllowFlight(!p.getAllowFlight());
			 p.sendMessage(String.format(PLUGIN_COMMAND_FLYMODE, p.getName(), p.getAllowFlight()? "aktiviert" : "deaktiviert"));
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

}
