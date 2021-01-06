package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EVENT_DEATH;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NoDeathMessage implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();
        
        Location l = p.getLocation();
        players.setLocation("player." + p.getUniqueId() + ".home.death", l);
        //TODO add location save.
        
        String loc = l.getX() + " " + l.getY() + " " + l.getZ();
        p.sendMessage(String.format(PLUGIN_EVENT_DEATH, loc));
        e.setKeepLevel(true);
        e.setDroppedExp(0);
        e.setDeathMessage(null);
    }
}
