package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_JOIN_MESSAGE;

public class BetterPlayerJoin implements Listener {

    private void addPlayer(Player p) {
        PlayerEntry pe = dBH.getPlayer(p.getUniqueId().toString());
        if (pe == null) {
            pe = new PlayerEntry();
            pe.setCreatedby(1);
            pe.setName(p.getName());
            pe.setCustomName(p.getDisplayName());
            pe.setGroup(Groups.getGroup("user"));
            pe.setUUID(p.getUniqueId().toString());
            dBH.insertPlayer(pe);

            pe = dBH.getPlayer(p.getUniqueId().toString());
        }
        else{
            if(pe.getName() == null){
                pe.setName(p.getName());
                dBH.updatePlayer(pe);
                pe = dBH.getPlayer(p.getUniqueId().toString());
            }
        }

        PlayerAPI.putPlayerEntry(p.getUniqueId(), pe);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(RelluEssentials.getInstance().isUnitTest){
            return;
        }

        e.setJoinMessage(null);
        Player p = e.getPlayer();
        addPlayer(p);

        p.setPlayerListHeader(pie.getTabheader());
        p.setPlayerListFooter(pie.getTabfooter());

        PlayerHelper.setFlying(p);
        PlayerHelper.setAFK(p, true);
        Bukkit.broadcastMessage(String.format(PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));
       
        WorldHelper.loadWorldGroupInventory(p);

        BankerHelper.doInterest(e.getPlayer());
    }

    @EventHandler
    public void login(PlayerLoginEvent e){
        int maxPlayers = Bukkit.getServer().getMaxPlayers();
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();

        if(onlinePlayers >= maxPlayers){
                e.disallow(null, EventConstants.PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN);
        }
    }

    @EventHandler
    public void checkInterest(AsyncPlayerPreLoginEvent e){
        BankerHelper.checkInterest(e.getUniqueId(), false);
    }
}