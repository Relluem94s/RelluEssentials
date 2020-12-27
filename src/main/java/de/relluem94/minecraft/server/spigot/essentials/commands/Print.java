package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.stringHelper;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.implode;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class Print implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("print")) {
            if (args.length >= 1) {
                return print(sender, args, 0);
            } else {
                sender.sendMessage(PLUGIN_COMMAND_MSG_INFO);
                return true;
            }
        }
        return false;
    }

    private boolean print(CommandSender sender, String[] args, int start) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!Permission.isAuthorized(p, Groups.MOD.getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }
        }

        String message = implode(start, args);
        message = stringHelper.replaceSymbols(replaceColor(message));
        
        Bukkit.broadcastMessage((sender instanceof Player ? ((Player)sender).getCustomName() : sender.getName()) + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + message);
        return true;
    }
}
