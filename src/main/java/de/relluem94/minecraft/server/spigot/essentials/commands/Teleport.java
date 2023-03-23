package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.TypeUtils;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT_TO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT_ACCEPT;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.HashMap;

public class Teleport implements CommandExecutor {

    HashMap<Player, Player> telportAcceptList = new HashMap<>();
    HashMap<Player, Player> telportToAcceptList = new HashMap<>();

    private void addTeleportEntry(Player p, Player t){
        telportAcceptList.put(t, p);
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), new Runnable() {
    
            @Override
            public void run() {
                if(hasTeleportEntry(t)){
                    p.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                    t.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                    removeTeleportEntry(t);
                }
            }
            
        }, 20*60*2L);
    }

   
    private void addTeleportToEntry(Player p, Player t){
        telportToAcceptList.put(t, p);
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), new Runnable() {
    
            @Override
            public void run() {
                if(hasToTeleportEntry(t)){
                    p.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                    t.sendMessage(PLUGIN_COMMAND_TP_REQUEST_EXPIRED);
                    removeToTeleportEntry(t);
                }
            }
            
        }, 20*60*2L);
    }

    private void removeTeleportEntry(Player t) {
        telportAcceptList.remove(t);
    }

    private void removeToTeleportEntry(Player t) {
        telportToAcceptList.remove(t);
    }

    private boolean hasTeleportEntry(Player t){
        return telportAcceptList.containsKey(t);
    }

    private boolean hasToTeleportEntry(Player t){
        return telportToAcceptList.containsKey(t);
    }

    public void teleport(Player p, Player t){
        Back.addBackPoint(t);
        t.teleport(p);
        p.sendMessage(String.format(PLUGIN_COMMAND_TP, t.getCustomName()));
    }

    public void teleportTo(Player p, Player t){
        Back.addBackPoint(p);
        p.teleport(t);
        t.sendMessage(String.format(PLUGIN_COMMAND_TP_TO, p.getCustomName()));
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT)) {
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
            if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT_ACCEPT)){
                if(hasTeleportEntry(p)){
                    teleport(p, telportAcceptList.get(p));
                    removeTeleportEntry(p);
                    return true;
                }

                if(hasToTeleportEntry(p)){
                    teleportTo(p, telportToAcceptList.get(p));
                    removeToTeleportEntry(p);
                    return true;
                }

                p.sendMessage(PLUGIN_COMMAND_TP_ACCEPT_NO_REQUEST);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
                return false;
            }

            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                addTeleportEntry(p, target);
                p.sendMessage(String.format(PLUGIN_COMMAND_TP_SEND_REQUEST, target.getCustomName()));
                return true;
            }

            Back.addBackPoint(p);
            p.teleport(target);
            p.sendMessage(String.format(PLUGIN_COMMAND_TP, target.getCustomName()));
            return true;
        }
       
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                return false;
            }

            if(!args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_TELEPORT_TO)){
                p.sendMessage(PLUGIN_COMMAND_TP_INFO);
                return true;
            }

            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                addTeleportToEntry(p, target);
                return true;
            }
            teleportTo(p, target);
            return true;
        }
        
        if (args.length == 3) {
            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }

            if (!TypeUtils.isInt(args[0])) {
                return false;
            }

            if (!TypeUtils.isInt(args[1])) {
                return false;
            }

            if (!TypeUtils.isInt(args[2])) {
                return false;
            }



            Location l = p.getLocation().clone();
            
            l.setX(Integer.parseInt(args[0]));
            l.setY(Integer.parseInt(args[1]));
            l.setZ(Integer.parseInt(args[2]));

            Back.addBackPoint(p);
            p.teleport(l);
            p.sendMessage(String.format(PLUGIN_COMMAND_TP, l.getX() + ", " + l.getY() + ", " + l.getZ()));

        }

        return false;
    }
}