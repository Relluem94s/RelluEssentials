package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

/* Better Call Soil */
public class BetterSoil implements Listener {

	@EventHandler
	public void onChange(EntityInteractEvent e) {
		if ((e.getEntityType() != EntityType.PLAYER) && (e.getBlock().getType() == Material.FARMLAND)) {
			e.setCancelled(true);
		}
	}
}
