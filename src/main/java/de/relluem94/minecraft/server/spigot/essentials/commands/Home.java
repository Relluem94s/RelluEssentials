package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import java.util.Iterator;
import java.util.Map;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;


public class Home implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("home")) {     
            switch(args.length){
                case 0:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.USER.getId())) {
                            if (p.getBedSpawnLocation() != null) {
                                p.teleport(p.getBedSpawnLocation());
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME, p.getWorld().getName()));
                            } else {
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NO_BED, p.getWorld().getName()));
                            }
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("list")) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            Map<String, Object> map;
                            ConfigurationSection c = players.getConfig().getConfigurationSection("player." + p.getUniqueId() + ".home");
                            
                            if(c != null){
                                map = c.getValues(false);
                                p.sendMessage(PLUGIN_COMMAND_HOME_LIST);
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    p.sendMessage(PLUGIN_COMMAND_ARG_COLOR + entry.getKey());
                                }
                            }
                            else{
                                p.sendMessage(PLUGIN_COMMAND_HOME_NONE);
                            }
                            return true;
                        }
                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if(players.getConfig().get("player." + p.getUniqueId() + ".home." + args[0]) != null){
                                double x, y, z;
                                float yaw, pitch;
                                String world;
                                x = players.getConfig().getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".x");
                                y = players.getConfig().getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".y");
                                z = players.getConfig().getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".z");
                                yaw = (float) players.getConfig().getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".yaw");
                                pitch = (float) players.getConfig().getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".pitch");
                                world = players.getConfig().getString("player." + p.getUniqueId() + ".home." + args[0] + ".world");

                                Location l = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

                                p.teleport(l);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_TP, args[0]));
                                return true;
                            }
                            else{
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NOT_FOUND, args[0]));
                                return true;
                            }
                        }
                    }
                    break;
                case 2:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;

                        if (args[0].equalsIgnoreCase("set")) {
                            if(players.getConfig().get("player." + p.getUniqueId() + ".home." + args[1]) == null){
                                if(!args[1].equals("death")){
                                    Location l = p.getLocation();
                                    players.getConfig().set("player." + p.getUniqueId() + ".home." + args[1] + ".x", l.getX());
                                    players.getConfig().set("player." + p.getUniqueId() + ".home." + args[1] + ".y", l.getBlockY());
                                    players.getConfig().set("player." + p.getUniqueId() + ".home." + args[1] + ".z", l.getBlockZ());
                                    players.getConfig().set("player." + p.getUniqueId() + ".home." + args[1] + ".yaw", l.getYaw());
                                    players.getConfig().set("player." + p.getUniqueId() + ".home." + args[1] + ".pitch", l.getPitch());
                                    players.getConfig().set("player." + p.getUniqueId() + ".home." + args[1] + ".world", l.getWorld().getName());
                                    p.sendMessage(String.format(PLUGIN_COMMAND_HOME_SET, args[1]));
                                }
                                else{
                                    p.sendMessage(String.format(PLUGIN_COMMAND_HOME_RESERVED, args[1]));
                                }
                                
                                return true;
                            }
                            else{
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_EXISTS, args[1]));
                                return true;
                            }
                            
                        } else if (args[0].equalsIgnoreCase("delete")) {
                            if(players.getConfig().get("player." + p.getUniqueId() + ".home." + args[1]) != null){
                                players.getConfig().set("player." + p.getUniqueId() + ".home." + args[1], null);
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DELETE, args[1]));
                                return true;
                            }
                            else{
                                p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NOT_FOUND, args[1]));
                                return true;
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
