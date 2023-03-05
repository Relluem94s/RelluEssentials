package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionLockEntry;

public class ProtectionAPI {

    private static List<Material> protectionLocksList = new ArrayList<>();
    private static HashMap<Location, ProtectionEntry> protectionEntryMap = new HashMap<>();


    public ProtectionAPI(List<ProtectionLockEntry> protectionLocksEntryList, HashMap<Location, ProtectionEntry> protectionEntryMap){
        for(ProtectionLockEntry ple: protectionLocksEntryList){
            addProtectionMaterial(ple.getValue());
        }
        ProtectionAPI.protectionEntryMap.putAll(protectionEntryMap);
    }

    /**
     * 
     * @param l
     * @return
     */
    public static ProtectionEntry getProtectionEntry(Location l){
        return protectionEntryMap.get(l);
    }

    /**
     * 
     * @param l
     */
    public static void removeProtectionEntry(Location l){
        protectionEntryMap.remove(l);
    }

    public static void putProtectionEntry(Location l, ProtectionEntry pe){
        protectionEntryMap.put(l, pe);
    }
    
     /**
     * Use this Method to add a Material that should be protectable via API
     * @param m Material
     */
    public static void addProtectionMaterial(Material m){
        protectionLocksList.add(m);
    }

    /**
     * Use this Method to get a List of all protectable Materials
     * @return List<Material> 
     */
    public static List<Material> getMaterialProtectionList(){
        return protectionLocksList;
    }

    /**
     * 
     * @return
     */
    public static HashMap<Location, ProtectionEntry> getProtectionEntryList(){
        return protectionEntryMap;
    }

}