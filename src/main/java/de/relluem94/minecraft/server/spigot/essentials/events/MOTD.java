package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MOTD implements Listener {

    @EventHandler
    public void onSpawn(ServerListPingEvent e) {
        String message = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getString("motd.message") + "";
        int players = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getInt("motd.players");
        
        e.setMotd(message);
        e.setMaxPlayers(players);
    }
}
