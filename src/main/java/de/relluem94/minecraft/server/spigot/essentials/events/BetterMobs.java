package main.java.de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;



public class BetterMobs implements Listener{
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		EntityType et = e.getEntity().getType();
		if(et == EntityType.PHANTOM) {
			e.setCancelled(true);
		}
		else if(et == EntityType.IRON_GOLEM) {
			Location l = e.getEntity().getLocation();
			l.getWorld().spawnEntity(l, et);
		}
	}
}
