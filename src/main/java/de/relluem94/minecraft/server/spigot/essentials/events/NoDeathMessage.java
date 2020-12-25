package main.java.de.relluem94.minecraft.server.spigot.essentials.events;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EVENT_DEATH;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NoDeathMessage implements Listener {

    @EventHandler
    public void onSpawn(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();
        String loc = "";
        Location l = p.getLocation();
        loc += l.getX() + " " + l.getY() + " " + l.getZ();
        
        p.sendMessage(String.format(PLUGIN_EVENT_DEATH, loc));
        e.setKeepLevel(true);
        e.setDroppedExp(0);
        e.setDeathMessage(null);
    }
}
