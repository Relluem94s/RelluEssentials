package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author rellu
 */
public class WorldHelper {

    public static boolean isInWorld(CommandSender sender, String m) {
        if (TypeHelper.isPlayer(sender)) {
            Player p = (Player) sender;
            return p.getWorld().getName().equalsIgnoreCase(m);
        } else {
            return true;
        }
    }

    public static boolean isInWorld(Player p, String m) {
        return p.getWorld().getName().equalsIgnoreCase(m);
    }

    public static boolean isInWorld(Player p, List<String> worlds) {
        return worlds.contains(p.getWorld().getName());
    }

    public static boolean isInWorld(CommandSender sender, World w) {
        if (TypeHelper.isPlayer(sender)) {
            Player p = (Player) sender;
            return isInWorld(p, w);
        } else {
            return true;
        }
    }

    public static boolean isInWorld(Block b, World m) {
        return b.getWorld().equals(m);
    }

    public static boolean isInWorld(Entity e, World m) {
        return e.getWorld().equals(m);
    }
    
    public static void createWorld(String worldName, WorldType type, World.Environment world_environment, boolean structures){
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(world_environment);
        wc.type(type);
        wc.generateStructures(structures);
        Bukkit.createWorld(wc);
    }
    
    public static void loadWorld(String worldName){
        WorldCreator wc = new WorldCreator(worldName);
        Bukkit.createWorld(wc);
    }
    
    public static void unloadWorld(String worldName, boolean save){
        Bukkit.unloadWorld(worldName, true);
    }
    
    /**
     * Copies a World from another
     * @param worldName String
     * @param copyWorldName String
     */
    public static void cloneWorld(String worldName, String copyWorldName){
        WorldCreator wc = new WorldCreator(worldName);
        if(Bukkit.getWorld(copyWorldName) != null){
            wc.copy(Objects.requireNonNull(Bukkit.getWorld(copyWorldName)));
        }
        
        Bukkit.createWorld(wc);
    }
}