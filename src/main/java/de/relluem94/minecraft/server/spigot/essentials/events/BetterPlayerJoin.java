package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EVENT_JOIN_MESSAGE;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
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
        String header = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getString("tab.header");
        String footer = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getString("tab.footer");
        PlayerHelper.sendTablist(p, header, footer);
        Bukkit.broadcastMessage(String.format(PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));
    }

    private void addPlayer(Player p) {
        
        PlayerEntry pe = dBH.getPlayer(p.getUniqueId().toString());
        
        if (dBH.getPlayer(p.getUniqueId().toString()).getUUID() == null ) {
            User u = new User(p, new UserGroup());
            pe = new PlayerEntry();
            pe.setCreatedby(1);
            pe.setGroupID(u.getGroup().getId());
            pe.setUuid(p.getUniqueId().toString());
            dBH.insertPlayer(pe);
        } else {
            User u = new User(p, Group.getGroupFromId(pe.getGroupID()));
        }
        playerEntryList.put(p.getUniqueId(), pe);
    }
}
