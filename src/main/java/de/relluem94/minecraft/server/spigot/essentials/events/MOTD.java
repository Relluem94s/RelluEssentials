package main.java.de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

public class MOTD implements Listener {

	@EventHandler
	public void onSpawn(ServerListPingEvent e) {
		e.setMotd(PLUGIN_EVENT_MOTD);
		e.setMaxPlayers(PLUGIN_EVENT_MOTD_MAXPLAYERS);
	}
}
