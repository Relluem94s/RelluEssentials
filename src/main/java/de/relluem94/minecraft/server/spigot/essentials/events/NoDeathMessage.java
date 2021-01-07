package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationTypeEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EVENT_DEATH;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NoDeathMessage implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();
        
        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
        LocationEntry le = new LocationEntry();
        le.setLocation(p.getLocation());
        le.setLocationName("death");
        le.setLocationType(locationTypeEntryList.get(1));
        le.setPlayerId(pe.getId());
        dBH.insertLocation(le);
        locationEntryList.add(le);

        String loc = le.getLocation().getX() + " " + le.getLocation().getY() + " " + le.getLocation().getZ();
        p.sendMessage(String.format(PLUGIN_EVENT_DEATH, loc));
        e.setKeepLevel(true);
        e.setDroppedExp(0);
        e.setDeathMessage(null);
    }
}
