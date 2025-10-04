package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_WORLD_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_WORLD_NOT_LOADED;

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
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupInventoryEntry;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author rellu
 */
public class WorldHelper {

    private WorldHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
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
     * @param worldEnvironment World.Environment
     * @param structures boolean
     */
    public static void createWorld(String worldName, WorldType type, World.Environment worldEnvironment, boolean structures) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(worldEnvironment);
        wc.type(type);
        wc.generateStructures(structures);
        Bukkit.createWorld(wc);
    }

    public static void createWorld(String worldName, WorldType type, World.Environment worldEnvironment, boolean structures, long seed) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(worldEnvironment);
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
     * @param clonedWorldName String
     * @param originalWorldName String
     * @throws
     * de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException
     * if World was not found
     */
    public static void cloneWorld(String clonedWorldName, String originalWorldName) throws WorldNotFoundException {
        WorldCreator wc = new WorldCreator(clonedWorldName);
        if (Bukkit.getWorld(originalWorldName) != null) {
            wc.copy(Objects.requireNonNull(Bukkit.getWorld(originalWorldName)));
            Bukkit.createWorld(wc);
        } else {
            throw new WorldNotFoundException(String.format(PLUGIN_EXCEPTION_WORLD_NOT_FOUND, originalWorldName));
        }
    }

    @SuppressWarnings("unused")
    public static @Nullable WorldGroupEntry getWorldGroupEntry(Player p){
        for (WorldGroupEntry wge : RelluEssentials.getInstance().worldsMap.keySet()) {
            if(wge == null){
                continue;
            }

            for(WorldEntry we: RelluEssentials.getInstance().worldsMap.get(wge)){
                if(we != null && p.getWorld().getName().equals(we.getName())){
                    return wge;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static @Nullable WorldEntry getWorldEntry(Player p){
        for (WorldGroupEntry wge : RelluEssentials.getInstance().worldsMap.keySet()) {
            if(wge == null){
                continue;
            }

            for(WorldEntry we: RelluEssentials.getInstance().worldsMap.get(wge)){
                if(we != null && p.getWorld().getName().equals(we.getName())){
                    return we;
                }
            }
        }
        return null;
    }


    public static void loadWorldGroupInventory(Player p){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        for (WorldGroupEntry wge : RelluEssentials.getInstance().worldsMap.keySet()) {
            if(wge == null){
                continue;
            }

            for(WorldEntry we: RelluEssentials.getInstance().worldsMap.get(wge)){
                if(we != null && p.getWorld().getName().equals(we.getName())){
                    WorldGroupInventoryEntry worldGroupInventoryEntry = RelluEssentials.getInstance().getDatabaseHelper().getWorldGroupInventory(pe, wge);
                    if(worldGroupInventoryEntry != null){
                        InventoryHelper.createInventory(worldGroupInventoryEntry.getInventory().toString(), p);
                        p.setFoodLevel(worldGroupInventoryEntry.getFoodLevel());
                        ExperienceHelper.setTotalExperience(p, worldGroupInventoryEntry.getTotalExperience());
                        p.setHealth(worldGroupInventoryEntry.getHealth());
                    }
                    else{
                        worldGroupInventoryEntry = new WorldGroupInventoryEntry();
                        worldGroupInventoryEntry.setCreatedBy(pe.getId());
                        worldGroupInventoryEntry.setPlayerId(pe.getId());
                        worldGroupInventoryEntry.setInventory(InventoryHelper.saveInventoryToJSON(p));
                        worldGroupInventoryEntry.setWorldGroupEntry(wge);
                        worldGroupInventoryEntry.setFoodLevel(p.getFoodLevel());
                        worldGroupInventoryEntry.setHealth(p.getHealth());
                        worldGroupInventoryEntry.setTotalExperience(ExperienceHelper.getTotalExperience(p));

                        RelluEssentials.getInstance().getDatabaseHelper().insertWorldGroupInventory(worldGroupInventoryEntry);
                    }
                }
            }
        }

        
    }

    public static boolean saveWorldGroupInventory(Player p, boolean clear){
        return saveWorldGroupInventory(p, p.getWorld(), clear);
    }

    public static boolean saveWorldGroupInventory(Player p, World w, boolean clear){
        boolean entryUpdated = false;
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        for (WorldGroupEntry wge : RelluEssentials.getInstance().worldsMap.keySet()) {
            if(wge == null){
                continue;
            }

            for(WorldEntry we: RelluEssentials.getInstance().worldsMap.get(wge)){
                if(we != null && w.getName().equals(we.getName())){
                    entryUpdated = savePlayerInv(wge, pe, p, clear);
                }
            }
        }
        return entryUpdated;
    }

    @SuppressWarnings("unused")
    public static boolean hasWorldGroupInventory(Player p, World w){
        boolean hasInvInWorldGroup = false;
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        for (WorldGroupEntry wge : RelluEssentials.getInstance().worldsMap.keySet()) {
            if(wge == null){
                continue;
            }

            for(WorldEntry we: RelluEssentials.getInstance().worldsMap.get(wge)){
                if(we != null && w.getName().equals(we.getName())){
                    WorldGroupInventoryEntry worldGroupInventoryEntry = RelluEssentials.getInstance().getDatabaseHelper().getWorldGroupInventory(pe, wge);
                    if(worldGroupInventoryEntry != null){
                        hasInvInWorldGroup = true;
                    }
                }
            }
        }
        return hasInvInWorldGroup;
    }

    private static boolean savePlayerInv(WorldGroupEntry wge, PlayerEntry pe, Player p, boolean clear){
        WorldGroupInventoryEntry worldGroupInventoryEntry = RelluEssentials.getInstance().getDatabaseHelper().getWorldGroupInventory(pe, wge);
        if(worldGroupInventoryEntry == null){
            worldGroupInventoryEntry = new WorldGroupInventoryEntry();
            worldGroupInventoryEntry.setCreatedBy(pe.getId());
            worldGroupInventoryEntry.setPlayerId(pe.getId());
            worldGroupInventoryEntry.setWorldGroupEntry(wge);

            worldGroupInventoryEntry.setInventory(InventoryHelper.saveInventoryToJSON(p));
            worldGroupInventoryEntry.setFoodLevel(p.getFoodLevel());
            worldGroupInventoryEntry.setHealth(p.getHealth());

            worldGroupInventoryEntry.setTotalExperience(ExperienceHelper.getTotalExperience(p));

            if(clear){
                p.setTotalExperience(0);
                p.setLevel(0);
                p.setExp(0);
                p.getInventory().clear();
            }

            RelluEssentials.getInstance().getDatabaseHelper().insertWorldGroupInventory(worldGroupInventoryEntry);
            return false;
        }
       
        worldGroupInventoryEntry.setInventory(InventoryHelper.saveInventoryToJSON(p));

        worldGroupInventoryEntry.setFoodLevel(p.getFoodLevel());
        worldGroupInventoryEntry.setHealth(p.getHealth());
        worldGroupInventoryEntry.setUpdatedBy(pe.getId());

        worldGroupInventoryEntry.setTotalExperience(ExperienceHelper.getTotalExperience(p));
       
        if(clear){
            p.setTotalExperience(0);
            p.setLevel(0);
            p.setExp(0);
            p.getInventory().clear();
        }

        RelluEssentials.getInstance().getDatabaseHelper().updateWorldGroupInventory(worldGroupInventoryEntry);
        return true;
    }
}
