package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

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

    public static PlayerEntry getPlayerEntry(Player p){
        return playerEntryMap.get(p.getUniqueId());
    }

    public static HashMap<UUID, PlayerEntry> getPlayerEntryMap(){
        return playerEntryMap;
    }

    public static Collection<BagEntry> getPlayerBagList(int player_fk){
        return playerBagEntryMap.get(player_fk);
    }

    public static void putPlayerBagEntry(int player_fk, BagEntry be){
        playerBagEntryMap.put(player_fk, be);
    }

    public static  Multimap<Integer, BagEntry> getPlayerBagMap() {
        return playerBagEntryMap;
    }


}
