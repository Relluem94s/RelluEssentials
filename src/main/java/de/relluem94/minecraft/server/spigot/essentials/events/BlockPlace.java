package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author rellu
 */
public class BlockPlace implements Listener {

    @EventHandler
    public void placeBlocks(BlockPlaceEvent e) {
      // has to be reimplemented
    }
}
