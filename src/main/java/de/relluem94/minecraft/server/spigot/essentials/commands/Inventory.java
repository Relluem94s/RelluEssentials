package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_INVENTORY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_INVENTORY_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_INVENTORY;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Inventory implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_INVENTORY)) {
            return false;
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;
        
        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true; 
        }


        if (args.length == 0) {
            p.openInventory(p.getInventory());
                p.sendMessage(PLUGIN_COMMAND_INVENTORY);
                return true; // TODO BUGS Clicked Items will be placed anywhere and overwrite other items..  
        }
        
        Player target = Bukkit.getPlayer(args[0]).getPlayer();
        if (target == null) {
            sender.sendMessage(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER);
            return true;
        }
        
        p.openInventory(target.getInventory());
        p.sendMessage(String.format(PLUGIN_COMMAND_INVENTORY_PLAYER, target.getCustomName()));
        return true;
    }
}