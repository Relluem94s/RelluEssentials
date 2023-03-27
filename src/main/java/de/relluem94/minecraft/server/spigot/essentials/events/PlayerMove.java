package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

/**
 *
 * @author rellu
 */
public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        e.setCancelled(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getPlayer().getUniqueId()).isAFK());
    }
}
