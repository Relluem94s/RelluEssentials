package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
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

    /**
     * Checks if Player is in World with Name
     * @param player Player
     * @param worldName String
     * @return boolean
     */
    public static boolean isInWorld(Player player, String worldName) {
        return player.getWorld().getName().equalsIgnoreCase(worldName);
    }
    
    public static boolean isInWorld(CommandSender sender, String m) {
        if (TypeHelper.isPlayer(sender)) {
            Player p = (Player) sender;
            return p.getWorld().getName().equalsIgnoreCase(m);
        } else {
            return true;
        }
    }

    /**
     * Checks a List of Strings (World Names) if Player is in one of it
     * @param player Player
     * @param worlds List of Strings
     * @return boolean
     */
    public static boolean isInWorld(Player player, List<String> worlds) {
        return worlds.contains(player.getWorld().getName());
    }

    /**
     * Checks if CommandSender is in World
     * @param sender CommandSender
     * @param world World
     * @return boolean
     */
    public static boolean isInWorld(CommandSender sender, World world) {
        if (TypeHelper.isPlayer(sender)) {
            Player p = (Player) sender;
            return isInWorld(p, world);
        } else {
            return true;
        }
    }

    /**
     * Checks if Block is in World
     * @param block Block
     * @param world World
     * @return boolean
     */
    public static boolean isInWorld(Block block, World world) {
        return block.getWorld().equals(world);
    }

    /**
     * Checks if Entity is in World
     * @param entity Entity
     * @param world World
     * @return boolean
     */
    public static boolean isInWorld(Entity entity, World world) {
        return entity.getWorld().equals(world);
    }
    
    /**
     * Creates a new World for the Bukkit Server
     * @param worldName String
     * @param type WorldType
     * @param world_environment World.Environment
     * @param structures boolean
     */
    public static void createWorld(String worldName, WorldType type, World.Environment world_environment, boolean structures){
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(world_environment);
        wc.type(type);
        wc.generateStructures(structures);
        Bukkit.createWorld(wc);
    }
    
    /**
     * Creates a World for the Bukkit Server
     * @param worldName String
     */
    public static void loadWorld(String worldName){
        WorldCreator wc = new WorldCreator(worldName);
        Bukkit.createWorld(wc);
    }
    
    /**
     * Unloads a World from the Bukkit Server
     * @param worldName String
     * @param save boolean
     * @throws de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException if World is not loaded
     */
    public static void unloadWorld(String worldName, boolean save) throws WorldNotLoadedException{
        if(Bukkit.getWorld(worldName) != null){
            Bukkit.unloadWorld(worldName, true);
        }
        else{
            throw new WorldNotLoadedException("Can't unload a World (" + worldName + ") that is not loaded. ");
        }
    }
    
    /**
     * Copies a World from another from the Bukkit Server
     * @param worldName String
     * @param copyWorldName String
     * @throws de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException if World was not found
     */
    public static void cloneWorld(String worldName, String copyWorldName) throws WorldNotFoundException{
        WorldCreator wc = new WorldCreator(worldName);
        if(Bukkit.getWorld(copyWorldName) != null){
            wc.copy(Objects.requireNonNull(Bukkit.getWorld(copyWorldName)));
            Bukkit.createWorld(wc);
        }
        else{
            throw new WorldNotFoundException("No World found with name: " + worldName);
        }
    }
}