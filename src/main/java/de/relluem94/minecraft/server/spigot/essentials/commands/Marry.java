package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MARRY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MARRY_ACCEPT;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.HashMap;

public class Marry implements CommandExecutor {

    HashMap<Player, Player> marryAcceptList = new HashMap<>();

    private void addMarryEntry(Player p, Player t){
        marryAcceptList.put(t, p);
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), new Runnable() {
    
            @Override
            public void run() {
                if(hasMarryEntry(t)){
                    p.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                    t.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                    removeMarryEntry(t);
                }
            }
        }, 20*60*2);
    }

    private boolean hasMarryEntry(Player t) {
        return marryAcceptList.containsKey(t);
    }

    private void removeMarryEntry(Player t) {
        marryAcceptList.remove(t);
    }


    public void marry(Player p, Player t){
        t.sendMessage(String.format(PLUGIN_COMMAND_MARRY_MARRIED, p.getCustomName()));
        p.sendMessage(String.format(PLUGIN_COMMAND_MARRY_MARRIED, t.getCustomName()));
        // TODO insert in db
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_MARRY)) {
            return false;
        }

        Player p = null;
        if (isPlayer(sender)) {
            p = (Player) sender;
        }

        if(p == null){
            return false;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(PLUGIN_COMMAND_TP_INFO);
            return true;
        }
        
        if (args.length == 1) {
            if(args[0].equals(PLUGIN_COMMAND_NAME_MARRY_ACCEPT)){
                if(hasMarryEntry(p)){
                    marry(p, marryAcceptList.get(p));
                    removeMarryEntry(p);
                    return true;
                }

                p.sendMessage(PLUGIN_COMMAND_MARRY_ACCEPT_NO_REQUEST);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER);
                return false;
            }

            addMarryEntry(p, target);
            p.sendMessage(String.format(PLUGIN_COMMAND_MARRY_SEND_REQUEST, target.getCustomName()));
            return true;
        }
        return false;
    }
}