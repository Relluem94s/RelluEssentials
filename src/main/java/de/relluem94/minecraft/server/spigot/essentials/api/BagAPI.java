package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;

public class BagAPI {
    
    private static List<BagTypeEntry> bagTypeEntryList = new ArrayList<BagTypeEntry>();

    /**
     * 
     * @param bagTypes List of BagTypeEntry
     */
    public BagAPI(List<BagTypeEntry> bagTypes) {
        bagTypeEntryList.addAll(bagTypes);
    }

    /**
     * 
     * Gives back a List of all BagTypes
     * @return List of BagTypeEntry
     */
    public static List<BagTypeEntry> getBagTypeEntryList(){
        return bagTypeEntryList;
    }
}