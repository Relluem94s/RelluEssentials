package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import org.bukkit.Material;

/**
 *
 * @author rellu
 */
public class BlockHistoryHelper {

    private BlockHistoryHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    /**
     * Sets a Block on the Location of the BlockHistoryEntry (bh) with the
     * specified Material from the database
     *
     * @param bh BlockHistoryEntry
     */
    public static void setBlock(BlockHistoryEntry bh) {
        Material m = Material.getMaterial(bh.getMaterial());
        if (m == null) {
            m = Material.AIR;
        }
        if (bh.getLocation() != null && bh.getLocation().getLocation() != null) {
            bh.getLocation().getLocation().getBlock().setType(m);
        }
    }
}
