package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/* Better Call Soil */
public class BetterSoil implements Listener {

    @EventHandler
    public void onChange(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            Block b = e.getClickedBlock();
            if (b.getType().equals(Material.FARMLAND) && (!e.getPlayer().isSneaking())) {
                    e.setUseInteractedBlock(Event.Result.DENY);
                    e.setCancelled(true);
                
            }
        }
    }

    @EventHandler
    public void onChange(EntityInteractEvent e) {
        Block b = e.getBlock();
        if (b.getType().equals(Material.FARMLAND)) {
            e.setCancelled(true);
        }
    }
}
