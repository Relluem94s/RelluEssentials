package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

public class Rellu implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (Permission.isAuthorized(p, 4)) {
                                    if(args[0].equalsIgnoreCase("save")){
                                        RelluEssentials.saveConfigs();
                                        p.sendMessage(PLUGIN_COMMAND_RELLU_SAVE);
                                        return true;
                                    }
                                    else if(args[0].equalsIgnoreCase("reload")){
                                        RelluEssentials.reloadConfigs();
                                        p.sendMessage(PLUGIN_COMMAND_RELLU_RELOAD);
                                        return true;
                                    }
                                    else{
                                        p.sendMessage(PLUGIN_COMMAND_RELLU_WRONG_COMMAND);
                                        return true;
                                    }
				}
				else {
					p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
					return true;
				}
			}
		}
                else{
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, 4)) {
                            p.sendMessage(PLUGIN_COMMAND_RELLU_OPTIONS);
                            return true;
                        }
                        else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                }
		return false;
	}
}
