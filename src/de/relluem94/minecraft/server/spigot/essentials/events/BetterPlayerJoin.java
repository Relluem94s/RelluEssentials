package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BetterPlayerJoin implements Listener{
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Player p = e.getPlayer();
		p.setCustomName("§8" + p.getName());
		Bukkit.broadcastMessage(String.format(PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));
	}
}
