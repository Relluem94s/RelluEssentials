package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;

public class BetterWorlds implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        
        WorldHelper.saveWorldGroupInventory(p, e.getFrom());
        WorldHelper.loadWorldGroupInventory(p);
    }
}