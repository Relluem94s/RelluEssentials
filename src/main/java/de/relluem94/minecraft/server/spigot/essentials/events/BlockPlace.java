package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

/**
 *
 * @author rellu
 */
public class BlockPlace implements Listener {

    @EventHandler
    public void placeBlocks(BlockPlaceEvent e) {
        if(e.getBlock().getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)){
          e.setCancelled(!Permission.isAuthorized(e.getPlayer(), Groups.getGroup("mod").getId()));
        }
      // has to be reimplemented
    }

    @EventHandler
    public void breakBlocks(BlockBreakEvent e) {
        if(e.getBlock().getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)){
          e.setCancelled(!Permission.isAuthorized(e.getPlayer(), Groups.getGroup("mod").getId()));
        }
      // has to be reimplemented
    }
}
