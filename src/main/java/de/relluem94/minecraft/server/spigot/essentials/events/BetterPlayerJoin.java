package main.java.de.relluem94.minecraft.server.spigot.essentials.events;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EVENT_JOIN_MESSAGE;
import main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups.UserGroup;

public class BetterPlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        addPlayer(p);
        PlayerHelper.setFlying(p);
        Bukkit.broadcastMessage(String.format(PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));
    }

    private void addPlayer(Player p) {
        if (players.getConfig().getString("player." + p.getUniqueId() + ".group") == null) {
            User u = new User(p, new UserGroup());
        } else {
            String groupName = players.getConfig().getString("player." + p.getUniqueId() + ".group");
            User u = new User(p, groupName);
        }
    }
}
