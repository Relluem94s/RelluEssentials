package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerPartnerEntry;

public class PlayerAPI {

    private Map<UUID, PlayerEntry> playerEntryMap = new HashMap<>();
    private Multimap<Integer, BagEntry> playerBagEntryMap = ArrayListMultimap.create();


    public PlayerAPI(List<BagEntry> bagEntries){
        for(BagEntry b: bagEntries){
            playerBagEntryMap.put(b.getPlayerId(), b);
        }

    }

    public void putPlayerEntry(UUID uuid, PlayerEntry pe){
        playerEntryMap.put(uuid, pe);
    }

    public PlayerEntry getPlayerEntry(UUID uuid){
        return playerEntryMap.get(uuid);
    }

    public PlayerEntry getPlayerEntry(int id){
        for(PlayerEntry pe : playerEntryMap.values()){
            if(pe.getId() == id){
                return pe;
            }
        }
        return null;
    }

    public PlayerEntry getPlayerEntry(Player player){
        return playerEntryMap.get(player.getUniqueId());
    }

    public Map<UUID, PlayerEntry> getPlayerEntryMap(){
        return playerEntryMap;
    }

    /**
     * 
     * @param playerFK
     * @return Collection of BagEntries
     */
    public Collection<BagEntry> getPlayerBagList(int playerFK){
        return playerBagEntryMap.get(playerFK);
    }

    /**
     * Adds Player Bag to internal List
     * @param playerFK
     * @param bagEntry
     */
    public void putPlayerBagEntry(int playerFK, @Nonnull BagEntry bagEntry){
        playerBagEntryMap.put(playerFK, bagEntry);
    }

    /**
     * Gives back Multimap of Integer (Player ID) and BagEntry of Player
     * @return Multimap of Integer and BagEntry
     */
    public Multimap<Integer, BagEntry> getPlayerBagMap() {
        return playerBagEntryMap;
    }

    public PlayerPartnerEntry getPartner(PlayerEntry pe){
        if(pe.getPartner() == null){
            return RelluEssentials.getInstance().getDatabaseHelper().getPlayerPartner(pe.getId());
        }
        
        return pe.getPartner();
    }

    public void reloadPlayerHomes() {
        List<PlayerEntry> pel = RelluEssentials.getInstance().getDatabaseHelper().getPlayers();

        playerEntryMap.clear();

        pel.forEach(p -> RelluEssentials.getInstance().getPlayerAPI().putPlayerEntry(UUID.fromString(p.getUuid()), p));
    }
}