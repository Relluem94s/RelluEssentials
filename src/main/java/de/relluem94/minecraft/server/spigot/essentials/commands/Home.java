package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.Location;


public class Home implements CommandExecutor {

    // Should be the homes.yml would make the sames easier. 
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("home")) {     
            switch(args.length){
                case 0:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, 1)) {
                            if (p.getBedSpawnLocation() != null) {
                                p.teleport(p.getBedSpawnLocation());
                            } else {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NO, p.getWorld().getName()));
                            }

                            p.sendMessage(String.format(PLUGIN_COMMAND_HOME, p.getWorld().getName()));
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("list")) {
                        //for each entry print message
                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if(players.get("player." + p.getUniqueId() + ".home." + args[0]) != null){
                                double x, y, z;
                                float yaw, pitch;
                                String world;
                                x = players.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".x");
                                y = players.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".y");
                                z = players.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".z");
                                yaw = (float) players.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".yaw");
                                pitch = (float) players.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".pitch");
                                world = players.getString("player." + p.getUniqueId() + ".home." + args[0] + ".world");

                                Location l = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

                                p.teleport(l);
                                return true;
                            }
                            else{
                                // home does not exists
                            }
                        }
                    }
                    break;
                case 2:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;

                        if (args[0].equalsIgnoreCase("set")) {
                            if(players.get("player." + p.getUniqueId() + ".home." + args[1]) == null){
                                Location l = p.getLocation();
                                players.set("player." + p.getUniqueId() + ".home." + args[1] + ".x", l.getX());
                                players.set("player." + p.getUniqueId() + ".home." + args[1] + ".y", l.getBlockY());
                                players.set("player." + p.getUniqueId() + ".home." + args[1] + ".z", l.getBlockZ());
                                players.set("player." + p.getUniqueId() + ".home." + args[1] + ".yaw", l.getYaw());
                                players.set("player." + p.getUniqueId() + ".home." + args[1] + ".pitch", l.getPitch());
                                players.set("player." + p.getUniqueId() + ".home." + args[1] + ".world", l.getWorld().getName());
                                return true;
                            }
                            else{
                                // home exists
                            }
                            
                        } else if (args[0].equalsIgnoreCase("delete")) {
                            if(players.get("player." + p.getUniqueId() + ".home." + args[1]) != null){
                                players.set("player." + p.getUniqueId() + ".home" + args[1], null);
                                return true;
                            }
                            else{
                                // home does not exists
                            }
                        } 
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
