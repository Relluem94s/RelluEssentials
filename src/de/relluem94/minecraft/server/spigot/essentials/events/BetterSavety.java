package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

/* Better Call Soil */
public class BetterSavety implements Listener {
	
	private String[] strings2block = {"/pl", "/bukkit", "/ver"};
	
	@EventHandler
	public void onSpawn(PlayerCommandPreprocessEvent e) {
		for(String s2b: strings2block) {
			if(!Permission.isAuthorized(e.getPlayer(), 8)) {
				if(e.getMessage().toLowerCase().startsWith(s2b)) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
				}
			}
		}
	}
}
