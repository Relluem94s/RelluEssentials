package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationTypeEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_DEATH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_DEATH_TP;

public class NoDeathMessage implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setKeepLevel(true);
        e.setDroppedExp(0);
        e.setDeathMessage(null);

        Player p = e.getEntity().getPlayer();

        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
        LocationEntry le = new LocationEntry();
        le.setLocation(p.getLocation());
        le.setLocationName("death_" + (int) (Math.random() * 94 - 1) + 1);
        le.setLocationType(locationTypeEntryList.get(1));
        le.setPlayerId(pe.getID());
        dBH.insertLocation(le);
        locationEntryList.add(le);
        p.sendMessage(String.format(PLUGIN_EVENT_DEATH, le.getLocationName(), (int) le.getLocation().getX(), (int) le.getLocation().getY(), (int) le.getLocation().getZ(), le.getLocation().getWorld().getName()));
        Bukkit.getConsoleSender().getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " [\"\",{\"text\":\"" + PLUGIN_EVENT_DEATH_TP + "\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/home " + le.getLocationName() + "\"}}]");
    }
}
