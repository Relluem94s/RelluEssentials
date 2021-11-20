package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.relluem94.minecraft.server.spigot.essentials.permissions.User;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_QUIT_MESSAGE;

public class BetterPlayerQuit implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player p = e.getPlayer();
        Bukkit.broadcastMessage(String.format(PLUGIN_EVENT_QUIT_MESSAGE, p.getCustomName()));
        User.getUserByPlayerName(p.getName());
        User.removeUser(p.getName());
    }
}
