package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;


/**
 *
 * @author Relluem94
 */
public class BlockHelper {

    @Setter
    private Material type;
    private final HashMap<Location, Long> locations = new HashMap<>();
    public BlockHelper(Material type) {
        this.type = type;
    }

    public void addLocation(Location location, Long delay) {
        locations.put(location, delay);
    }

    public void putAll(BlockHelper setBlockHelper){
        locations.putAll(setBlockHelper.locations);
    }

    public void setBlocks(long addDelay) {
        locations.forEach((location, delay) -> Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RelluEssentials.getInstance(), () -> location.getBlock().setType(type), Math.abs(delay + addDelay)));
    }

    public void setBlocks() {
        setBlocks(0);
    }

    public static boolean checkBlockAt(Location location, Material mat) {
        return location.getBlock().getType() == mat;
    }
}