package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sun implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("sun")) {
			if (args.length == 0) {
				 if(sender instanceof Player) {
					 	Player p = (Player) sender;
					 	p.getWorld().setWeatherDuration(0);
					 	p.getWorld().setStorm(false);
					 	p.getWorld().setWeatherDuration(1000000);
					 	p.sendMessage(String.format(PLUGIN_COMMAND_SUN, p.getWorld().getName()));
				 }
				 return true;
			 }
			 else {
				 Player target = Bukkit.getPlayer(args[0]);
				 if(target != null) {
					 target.setPlayerWeather(WeatherType.CLEAR);
					 target.sendMessage(PLUGIN_COMMAND_SUN_PLAYER);
				 }
				 return true;
			 }
		}
		else {
			return false;
		}
		 
	}
}
