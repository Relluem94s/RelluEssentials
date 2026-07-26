package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author rellu
 */
public class BlockPlace implements Listener {

    @EventHandler
    public void placeBlocks(@NonNull BlockPlaceEvent e) {
        if(e.getBlock().getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)){
          e.setCancelled(!Permission.isAuthorized(e.getPlayer(), Groups.getGroup("mod").getId()));
        }
    }

    @EventHandler
    public void breakBlocks(@NonNull BlockBreakEvent e) {
        if(e.getBlock().getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)){
          e.setCancelled(!Permission.isAuthorized(e.getPlayer(), Groups.getGroup("mod").getId()));
        }
    }
}
