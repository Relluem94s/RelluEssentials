package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MOTD implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        e.setMotd(pie.getMotdMessage() );
        e.setMaxPlayers(pie.getMotdPlayers());
    }
}
