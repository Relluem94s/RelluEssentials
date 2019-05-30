package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.permissions.groups.UserGroup;

public class BetterPlayerJoin implements Listener{
	
	private FileConfiguration config = Bukkit.getPluginManager().getPlugin("RelluEssentials").getConfig();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Player p = e.getPlayer();

		addPlayer(p);
		
		Bukkit.broadcastMessage(String.format(PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));
	}
	
	private void addPlayer(Player p) {
		if(config.getString("player." + p.getUniqueId() + ".group") == null) {
			new User(p, new UserGroup());
		}
		else {
			String groupName = config.getString("player." + p.getUniqueId() + ".group");
			new User(p, groupName);
		}
	}
}
