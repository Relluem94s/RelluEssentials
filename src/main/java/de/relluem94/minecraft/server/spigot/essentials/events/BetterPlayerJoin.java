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
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;
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
       
        WorldHelper.loadWorldGroupInventory(p);

        if(RelluEssentials.bankInterestMap.containsKey(e.getPlayer().getUniqueId())){
            BankAccountEntry bae = RelluEssentials.bankInterestMap.get(p.getUniqueId());

            double interest = (bae.getValue() / 100) * bae.getTier().getInterest();
            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_INTEREST, StringHelper.formatDouble(interest)));

            RelluEssentials.dBH.addTransactionToBank(bae.getPlayerId(), bae.getId(), interest, bae.getValue(), bae.getTier().getId());
            RelluEssentials.bankInterestMap.remove(p.getUniqueId());
        }

        
    }

    @EventHandler
    public void login(PlayerLoginEvent e){
        int MaxPlayers = Bukkit.getServer().getMaxPlayers();
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();

        if(onlinePlayers >= MaxPlayers){
                e.disallow(null, EventConstants.PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN);
        }
    }

    @EventHandler
    public void checkInterest(AsyncPlayerPreLoginEvent e){
        if(Bukkit.getOfflinePlayer(e.getUniqueId()).hasPlayedBefore()){
            PlayerEntry pe = PlayerAPI.getPlayerEntry(e.getUniqueId());
            if(pe != null){ // Only if Player got deleted in DB
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
                        // TODO check for online players (at 0:00) add to map
                    }
                }
            }
        }        
    }
}