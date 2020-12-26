package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class Message implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("msg")) {
            if (args.length > 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.USER.getId())) {
                            String message = "";
                            for (int i = 1; args.length > i; i++) {
                                if(args[i] == null){
                                    break;
                                }
                                message += args[i] + " ";
                            }
                            target.sendMessage(p.getCustomName() + PLUGIN_SPACER + message);
                            p.sendMessage(target.getCustomName() + PLUGIN_SPACER_MSG + message);
                            return true;
                        }
                    }
                }
            }
            else {
                // How to use
            }
        }
        return false;
    }
}
