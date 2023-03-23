package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.File;
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

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupInventoryEntry;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_WORLD_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_WORLD_NOT_LOADED;

/**
 *
 * @author rellu
 */
public class WorldHelper {

    private WorldHelper() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    /**
     * Checks if Player is in World with Name
     *
     * @param player Player
     * @param worldName String
     * @return boolean
     */
    public static boolean isInWorld(Player player, String worldName) {
        return player.getWorld().getName().equalsIgnoreCase(worldName);
    }

    /**
     * Checks if Command Sender is in World with Name
     *
     * @param sender CommandSender
     * @param worldName String
     * @return boolean
     */
    public static boolean isInWorld(CommandSender sender, String worldName) {
        if (TypeHelper.isPlayer(sender)) {
            return isInWorld((Player) sender, worldName);
        } else {
            return true;
        }
    }

    /**
     * Checks a List of Strings (World Names) if Player is in one of it
     *
     * @param player Player
     * @param worlds List of Strings
     * @return boolean
     */
    public static boolean isInWorld(Player player, List<String> worlds) {
        return worlds.contains(player.getWorld().getName());
    }

    /**
     * Checks if CommandSender is in World
     *
     * @param sender CommandSender
     * @param world World
     * @return boolean
     */
    public static boolean isInWorld(CommandSender sender, World world) {
        if (TypeHelper.isPlayer(sender)) {
            return isInWorld((Player) sender, world);
        } else {
            return true;
        }
    }

    /**
     * Checks if Block is in World
     *
     * @param block Block
     * @param world World
     * @return boolean
     */
    public static boolean isInWorld(Block block, World world) {
        return block.getWorld().equals(world);
    }

    /**
     * Checks if Entity is in World
     *
     * @param entity Entity
     * @param world World
     * @return boolean
     */
    public static boolean isInWorld(Entity entity, World world) {
        return entity.getWorld().equals(world);
    }

    /**
     * Creates a new World for the Bukkit Server
     *
     * @param worldName String
     * @param type WorldType
     * @param world_environment World.Environment
     * @param structures boolean
     */
    public static void createWorld(String worldName, WorldType type, World.Environment world_environment, boolean structures) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(world_environment);
        wc.type(type);
        wc.generateStructures(structures);
        Bukkit.createWorld(wc);
    }

    public static void createWorld(String worldName, WorldType type, World.Environment world_environment, boolean structures, long seed) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(world_environment);
        wc.type(type);
        wc.generateStructures(structures);
        wc.seed(seed);
        Bukkit.createWorld(wc);
    }

/**
     * Creates a World for the Bukkit Server
     *
     * @param worldName String
     */
    public static boolean worldExists(String worldName) {
        return new File(Bukkit.getWorldContainer(), worldName).exists();
    }


    /**
     * Creates a World for the Bukkit Server
     *
     * @param worldName String
     */
    public static void loadWorld(String worldName) {
        WorldCreator wc = new WorldCreator(worldName);
        Bukkit.createWorld(wc);
    }

    /**
     * Unloads a World from the Bukkit Server
     *
     * @param worldName String
     * @param save boolean
     * @throws
     * de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException
     * if World is not loaded
     */
    public static void unloadWorld(String worldName, boolean save) throws WorldNotLoadedException {
        if (Bukkit.getWorld(worldName) != null) {
            Bukkit.unloadWorld(worldName, save);
        } else {
            throw new WorldNotLoadedException(String.format(PLUGIN_EXCEPTION_WORLD_NOT_LOADED, worldName));
        }
    }

    /**
     * Copies a World from another from the Bukkit Server
     *
     * @param worldName String
     * @param copyWorldName String
     * @throws
     * de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException
     * if World was not found
     */
    public static void cloneWorld(String worldName, String copyWorldName) throws WorldNotFoundException {
        WorldCreator wc = new WorldCreator(worldName);
        if (Bukkit.getWorld(copyWorldName) != null) {
            wc.copy(Objects.requireNonNull(Bukkit.getWorld(copyWorldName)));
            Bukkit.createWorld(wc);
        } else {
            throw new WorldNotFoundException(String.format(PLUGIN_EXCEPTION_WORLD_NOT_FOUND, copyWorldName));
        }
    }

    public static WorldGroupEntry getWorldGroupEntry(Player p){
        for (WorldGroupEntry wge : RelluEssentials.worldsMap.keySet()) {
            for(WorldEntry we: RelluEssentials.worldsMap.get(wge)){
                if(p.getWorld().getName().equals(we.getName())){
                    return wge;
                }
            }
        }
        return null;
    }

    public static WorldEntry getWorldEntry(Player p){
        for (WorldGroupEntry wge : RelluEssentials.worldsMap.keySet()) {
            for(WorldEntry we: RelluEssentials.worldsMap.get(wge)){
                if(p.getWorld().getName().equals(we.getName())){
                    return we;
                }
            }
        }
        return null;
    }


    public static void loadWorldGroupInventory(Player p){
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
        for (WorldGroupEntry wge : RelluEssentials.worldsMap.keySet()) {
            for(WorldEntry we: RelluEssentials.worldsMap.get(wge)){
                if(p.getWorld().getName().equals(we.getName())){
                    WorldGroupInventoryEntry wgie = RelluEssentials.dBH.getWorldGroupInventory(pe, wge);
                    if(wgie != null){
                        InventoryHelper.createInventory(wgie.getInventory().toString(), p); 
                        p.setFoodLevel(wgie.getFoodLevel());
                        ExperienceHelper.setTotalExperience(p, wgie.getTotalExperience());
                        p.setHealth(wgie.getHealth());
                    }
                    else{
                        wgie = new WorldGroupInventoryEntry();
                        wgie.setCreatedby(pe.getID());
                        wgie.setPlayerId(pe.getID());
                        wgie.setInventory(InventoryHelper.saveInventoryToJSON(p));
                        wgie.setWorldGroup(wge);
                        wgie.setFoodLevel(p.getFoodLevel());
                        wgie.setHealth(p.getHealth());
                        wgie.setTotalExperience(ExperienceHelper.getTotalExperience(p));

                        RelluEssentials.dBH.insertWorldGroupInventory(wgie);
                    }
                }
            }
        }

        
    }

    public static boolean saveWorldGroupInventory(Player p){
        return saveWorldGroupInventory(p, p.getWorld());
    }

    public static boolean saveWorldGroupInventory(Player p, World w){
        boolean entryUpdated = false;
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
        for (WorldGroupEntry wge : RelluEssentials.worldsMap.keySet()) {
            for(WorldEntry we: RelluEssentials.worldsMap.get(wge)){
                if(w.getName().equals(we.getName())){
                    WorldGroupInventoryEntry wgie = RelluEssentials.dBH.getWorldGroupInventory(pe, wge);
                    if(wgie == null){
                        wgie = new WorldGroupInventoryEntry();
                        wgie.setCreatedby(pe.getID());
                        wgie.setPlayerId(pe.getID());
                        wgie.setWorldGroup(wge);

                        wgie.setInventory(InventoryHelper.saveInventoryToJSON(p));
                        wgie.setFoodLevel(p.getFoodLevel());
                        wgie.setHealth(p.getHealth());

                        wgie.setTotalExperience(ExperienceHelper.getTotalExperience(p));
                        p.setTotalExperience(0);
                        p.setLevel(0);
                        p.setExp(0);

                        RelluEssentials.dBH.insertWorldGroupInventory(wgie);
                    }
                    else{
                        wgie.setInventory(InventoryHelper.saveInventoryToJSON(p));

                        wgie.setFoodLevel(p.getFoodLevel());
                        wgie.setHealth(p.getHealth());
                        wgie.setUpdatedBy(pe.getID());

                        wgie.setTotalExperience(ExperienceHelper.getTotalExperience(p));
                        p.setTotalExperience(0);
                        p.setLevel(0);
                        p.setExp(0);

                        RelluEssentials.dBH.updateWorldGroupInventory(wgie);
                        p.getInventory().clear();
                        entryUpdated = true;
                    }
                }
            }
        }
        return entryUpdated;
    }

    public static boolean hasWorldGroupInventory(Player p, World w){
        boolean hasInvInWorldGroup = false;
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
        for (WorldGroupEntry wge : RelluEssentials.worldsMap.keySet()) {
            for(WorldEntry we: RelluEssentials.worldsMap.get(wge)){
                if(w.getName().equals(we.getName())){
                    WorldGroupInventoryEntry wgie = RelluEssentials.dBH.getWorldGroupInventory(pe, wge);
                    if(wgie != null){
                        hasInvInWorldGroup = true;
                    }
                }
            }
        }
        return hasInvInWorldGroup;
    }
}
