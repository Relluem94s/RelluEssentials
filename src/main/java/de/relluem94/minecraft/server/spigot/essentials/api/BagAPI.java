package de.relluem94.minecraft.server.spigot.essentials.api;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagTypeEntry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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