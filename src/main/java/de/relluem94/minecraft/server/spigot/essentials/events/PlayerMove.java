package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author rellu
 */
public class PlayerMove implements Listener {
    
     @EventHandler
    public void onSpawn(PlayerMoveEvent e) {
        boolean isAFK = false;
        Player p = e.getPlayer();
        if(players.getConfig().get("player." + p.getUniqueId() + ".afk") != null){
            isAFK = players.getConfig().getBoolean("player." + p.getUniqueId() + ".afk");
        }
        e.setCancelled(isAFK);
    }
}
