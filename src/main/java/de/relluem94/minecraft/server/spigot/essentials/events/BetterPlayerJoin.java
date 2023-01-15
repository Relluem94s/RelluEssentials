package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_JOIN_MESSAGE;

public class BetterPlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        addPlayer(p);

        p.setPlayerListHeader(pie.getTabheader());
        p.setPlayerListFooter(pie.getTabfooter());

        PlayerHelper.setFlying(p);
        PlayerHelper.setAFK(p, true);
        Bukkit.broadcastMessage(String.format(PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));
    }

    private void addPlayer(Player p) {
        PlayerEntry pe = dBH.getPlayer(p.getUniqueId().toString());
        if (pe == null) {
            pe = new PlayerEntry();
            pe.setCreatedby(1);
            pe.setCustomName(p.getDisplayName());
            pe.setGroup(Groups.getGroup("user"));
            pe.setUUID(p.getUniqueId().toString());
            dBH.insertPlayer(pe);
        }
        playerEntryList.put(p.getUniqueId(), pe);
        User u = new User(p);
        u.getPlayer().setScoreboard(RelluEssentials.board);
    }
}
