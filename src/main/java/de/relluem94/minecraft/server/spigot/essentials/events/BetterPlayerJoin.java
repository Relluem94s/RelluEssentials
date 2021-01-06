package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EVENT_JOIN_MESSAGE;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.permissions.groups.UserGroup;

public class BetterPlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        addPlayer(p);
        PlayerHelper.setFlying(p);
        PlayerHelper.setAFK(p, true);
        PlayerHelper.sendTablist(p, pie.getTabheader(), pie.getTabfooter());
        Bukkit.broadcastMessage(String.format(PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));
    }

    private void addPlayer(Player p) {
        
        PlayerEntry pe = dBH.getPlayer(p.getUniqueId().toString());
        
        if (pe == null) {
            pe = new PlayerEntry();
            pe.setCreatedby(1);
            pe.setGroup(new GroupEntry(new UserGroup()));
            pe.setUuid(p.getUniqueId().toString());
            dBH.insertPlayer(pe);
        }
        playerEntryList.put(p.getUniqueId(), pe);
        User u = new User(p, Group.getGroupFromId(pe.getGroup().getId()));
    }
}
