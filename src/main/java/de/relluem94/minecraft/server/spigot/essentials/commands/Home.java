package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.OfflinePlayer;
import static org.bukkit.WorldCreator.name;

public class Home implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("home")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (Permission.isAuthorized(p, 1)) {
                                            if(p.getBedSpawnLocation() != null){
                                                p.teleport(p.getBedSpawnLocation());
                                            }
                                            else{
                                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NO, p.getWorld().getName()));
                                            }
						
						p.sendMessage(String.format(PLUGIN_COMMAND_HOME, p.getWorld().getName()));
						return true;
					}
					else {
						p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
						return true;
					}
				}
			}
			else {
				Player target = null;
                                
                                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                                    if (offlinePlayer.getName().equalsIgnoreCase(args[0])) {
                                        target = offlinePlayer.getPlayer();
                                    }
                                }
                                
				if (target != null) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						if (Permission.isAuthorized(p, 4)) {
                                                        if(target.getBedSpawnLocation() != null){
                                                            p.teleport(target.getBedSpawnLocation());
                                                        }
							
							p.sendMessage(String.format(PLUGIN_COMMAND_HOME, target.getWorld().getName()));
							return true;
						}
						else {
							p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
							return true;
						}
					}
				}
                                else{
                                    return false;
                                }
			}
		}
		return false;
	}
}
