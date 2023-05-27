package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionLockEntry;

public class ProtectionAPI {

    private List<Material> protectionLocksList = new ArrayList<>();
    private HashMap<Location, ProtectionEntry> protectionEntryMap = new HashMap<>();


    public ProtectionAPI(List<ProtectionLockEntry> protectionLocksEntryList, Map<Location, ProtectionEntry> protectionEntryMap){
        for(ProtectionLockEntry ple: protectionLocksEntryList){
            addProtectionMaterial(ple.getValue());
        }
        protectionEntryMap.putAll(protectionEntryMap);
        for(ProtectionEntry pe : protectionEntryMap.values()){
            System.out.println(pe.getMaterialName() + " " + pe.getLocation().getId() + " " + pe.getLocation().getWorld() + " " + (Bukkit.getWorld(pe.getLocation().getWorld()) != null));
        }
    }

    /**
     * 
     * @param l
     * @return
     */
    public ProtectionEntry getProtectionEntry(Location l){
        return protectionEntryMap.get(l);
    }

    /**
     * 
     * @param l
     */
    public void removeProtectionEntry(Location l){
        protectionEntryMap.remove(l);
    }

    public void putProtectionEntry(Location l, ProtectionEntry pe){
        protectionEntryMap.put(l, pe);
    }
    
     /**
     * Use this Method to add a Material that should be protectable via API
     * @param m Material
     */
    public void addProtectionMaterial(Material m){
        protectionLocksList.add(m);
    }

    /**
     * Use this Method to get a List of all protectable Materials
     * @return List<Material> 
     */
    public List<Material> getMaterialProtectionList(){
        return protectionLocksList;
    }

    /**
     * 
     * @return
     */
    public Map<Location, ProtectionEntry> getProtectionEntryList(){
        return protectionEntryMap;
    }

}