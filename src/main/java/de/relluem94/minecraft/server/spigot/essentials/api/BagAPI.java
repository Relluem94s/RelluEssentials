package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import lombok.Getter;

@Getter
public class BagAPI {
    
    private final List<BagTypeEntry> bagTypeEntryList = new ArrayList<>();

    /**
     * 
     * @param bagTypes List of BagTypeEntry
     */
    public BagAPI(List<BagTypeEntry> bagTypes) {
        bagTypeEntryList.addAll(bagTypes);
    }
}