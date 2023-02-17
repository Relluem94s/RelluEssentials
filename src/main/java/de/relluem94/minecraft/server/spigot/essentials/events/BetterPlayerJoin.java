package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_JOIN_MESSAGE;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class BetterPlayerJoin implements Listener {

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
        u.done();
    }

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

        if(RelluEssentials.bankInterestMap.containsKey(e.getPlayer().getUniqueId())){
            BankAccountEntry bae = RelluEssentials.bankInterestMap.get(p.getUniqueId());

            double interest = (bae.getValue() / 100) * bae.getTier().getInterest();
            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_INTEREST, StringHelper.formatDouble(interest)));

            RelluEssentials.dBH.addTransactionToBank(bae.getPlayerId(), bae.getId(), interest, bae.getValue(), bae.getTier().getId());
            RelluEssentials.bankInterestMap.remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void login(PlayerLoginEvent ev){
        int MaxPlayers = Bukkit.getServer().getMaxPlayers();
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        
        if(onlinePlayers >= MaxPlayers){
                ev.disallow(null, EventConstants.PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN);
        }
    }

    @EventHandler
    public void checkInterest(AsyncPlayerPreLoginEvent e){
        PlayerEntry pe = RelluEssentials.playerEntryList.get(e.getUniqueId());
        BankAccountEntry bae = RelluEssentials.dBH.getPlayerBankAccount(pe.getID());
        if(bae != null){
            OfflinePlayer op = Bukkit.getOfflinePlayer(e.getUniqueId());
            long lastPlayedTime = op.getLastPlayed()/1000L;
            
            LocalDate localDate = LocalDate.now();
            ZonedDateTime startOfDayInZone = localDate.atStartOfDay(ZoneId.systemDefault());

            Date lastPlayedDate = new Date(lastPlayedTime*1000L); 
            Date todayDate = new Date(startOfDayInZone.toInstant().toEpochMilli());

            if(lastPlayedDate.before(todayDate)){
                RelluEssentials.bankInterestMap.put(e.getUniqueId(), bae);
            }
        }
    }
}
