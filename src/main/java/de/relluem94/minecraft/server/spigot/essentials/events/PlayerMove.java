package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author rellu
 */
public class PlayerMove implements Listener {
    
     @EventHandler
    public void onMove(PlayerMoveEvent e) {
        e.setCancelled(playerEntryList.get(e.getPlayer().getUniqueId()).isAFK());
    }
}
