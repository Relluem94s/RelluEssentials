package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Home implements CommandExecutor {

    private FileConfiguration config = Bukkit.getPluginManager().getPlugin("RelluEssentials").getConfig();

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
                            if(config.get("player." + p.getUniqueId() + ".home." + args[0]) != null){
                                double x, y, z;
                                float yaw, pitch;
                                String world;
                                x = config.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".x");
                                y = config.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".y");
                                z = config.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".z");
                                yaw = (float) config.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".yaw");
                                pitch = (float) config.getDouble("player." + p.getUniqueId() + ".home." + args[0] + ".pitch");
                                world = config.getString("player." + p.getUniqueId() + ".home." + args[0] + ".world");

                                Location l = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

                                p.teleport(l);
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
                            if(config.get("player." + p.getUniqueId() + ".home." + args[1]) == null){
                                Location l = p.getLocation();
                                config.set("player." + p.getUniqueId() + ".home." + args[1] + ".x", l.getX());
                                config.set("player." + p.getUniqueId() + ".home." + args[1] + ".y", l.getBlockY());
                                config.set("player." + p.getUniqueId() + ".home." + args[1] + ".z", l.getBlockZ());
                                config.set("player." + p.getUniqueId() + ".home." + args[1] + ".yaw", l.getYaw());
                                config.set("player." + p.getUniqueId() + ".home." + args[1] + ".pitch", l.getPitch());
                                config.set("player." + p.getUniqueId() + ".home." + args[1] + ".world", l.getWorld().getName());
                            }
                            else{
                                // home exists
                            }
                            
                        } else if (args[0].equalsIgnoreCase("delete")) {
                            if(config.get("player." + p.getUniqueId() + ".home." + args[1]) != null){
                                config.set("player." + p.getUniqueId() + ".home" + args[1], null);
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
