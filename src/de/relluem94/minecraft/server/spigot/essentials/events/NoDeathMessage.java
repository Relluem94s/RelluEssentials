package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/* Better Call Soil */
public class NoDeathMessage implements Listener {

	@EventHandler
	public void onSpawn(PlayerDeathEvent e) {
		e.setKeepLevel(true);
		e.setDeathMessage(null);
	}
}
