package main.java.de.relluem94.minecraft.server.spigot.essentials.events;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EVENT_JOIN_MESSAGE;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups.UserGroup;
import org.bukkit.Material;

public class BetterPlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        addPlayer(p);
        if (User.getGroup(p).getId() >= 2) {
            if (players.getConfig().getBoolean("player." + p.getUniqueId() + ".fly")) {
                p.setAllowFlight(true);
                if (p.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.AIR)) {
                    p.setFlying(true);
                }
            }
        }
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
