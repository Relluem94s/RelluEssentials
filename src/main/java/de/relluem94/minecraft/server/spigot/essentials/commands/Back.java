package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BACK;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.HashMap;

public class Back implements CommandExecutor {

    private static HashMap<Player, Location> backPlayerLocation = new HashMap<>();

    public static void addBackPoint(Player p){
        removeBackPoint(p);
        backPlayerLocation.put(p, p.getLocation());
    }

    public static void removeBackPoint(Player p){
        if(backPlayerLocation.containsKey(p)){
            backPlayerLocation.remove(p);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_BACK)) {
            return false;
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }
           
        if(!backPlayerLocation.containsKey(p)){
            p.sendMessage(PLUGIN_COMMAND_BACK_NO_LOCATION);
            return true;
        }
        
        p.teleport(backPlayerLocation.get(p));
        backPlayerLocation.remove(p);
        p.sendMessage(PLUGIN_COMMAND_BACK);
        return true;
    }
}