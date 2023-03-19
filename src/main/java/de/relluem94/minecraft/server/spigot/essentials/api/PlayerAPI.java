package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerPartnerEntry;

public class PlayerAPI {

    private static HashMap<UUID, PlayerEntry> playerEntryMap = new HashMap<>();
    private static Multimap<Integer, BagEntry> playerBagEntryMap = ArrayListMultimap.create();


    public PlayerAPI(List<BagEntry> bagEntries){
        for(BagEntry b: bagEntries){
            playerBagEntryMap.put(b.getPlayerId(), b);
        }

    }

    public static void putPlayerEntry(UUID uuid, PlayerEntry pe){
        playerEntryMap.put(uuid, pe);
    }

    public static PlayerEntry getPlayerEntry(UUID uuid){
        return playerEntryMap.get(uuid);
    }

    public static PlayerEntry getPlayerEntry(int id){
        for(PlayerEntry pe : playerEntryMap.values()){
            if(pe.getID() == id){
                return pe;
            }
        }
        return null;
    }

    public static PlayerEntry getPlayerEntry(Player player){
        return playerEntryMap.get(player.getUniqueId());
    }

    public static HashMap<UUID, PlayerEntry> getPlayerEntryMap(){
        return playerEntryMap;
    }

    /**
     * 
     * @param player_fk
     * @return Collection of BagEntries
     */
    public static Collection<BagEntry> getPlayerBagList(int player_fk){
        return playerBagEntryMap.get(player_fk);
    }

    /**
     * Adds Player Bag to internal List
     * @param player_fk
     * @param bagEntry
     */
    public static void putPlayerBagEntry(int player_fk, BagEntry bagEntry){
        playerBagEntryMap.put(player_fk, bagEntry);
    }

    /**
     * Gives back Multimap of Integer (Player ID) and BagEntry of Player
     * @return Multimap of Integer and BagEntry
     */
    public static Multimap<Integer, BagEntry> getPlayerBagMap() {
        return playerBagEntryMap;
    }

    public static PlayerPartnerEntry getPartner(PlayerEntry pe){
        if(pe.getPartner() == null){
            return RelluEssentials.dBH.getPlayerPartner(pe.getID());
        }
        
        return pe.getPartner();
    }
}