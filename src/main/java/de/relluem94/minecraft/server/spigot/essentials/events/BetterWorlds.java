package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.WorldInitEvent;

import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;

public class BetterWorlds implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        
        WorldHelper.saveWorldGroupInventory(p, e.getFrom());
        WorldHelper.loadWorldGroupInventory(p);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void worldInit(WorldInitEvent e){
        e.getWorld().setKeepSpawnInMemory(false);
    }
}