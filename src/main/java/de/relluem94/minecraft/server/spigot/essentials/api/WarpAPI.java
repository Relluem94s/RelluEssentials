package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;

public class WarpAPI {
    private static List<LocationEntry> warps = new ArrayList<>();

    public WarpAPI(List<LocationEntry>  warps){
        WarpAPI.warps.addAll(warps);
    }

    public static LocationEntry getWarp(String name){
        for(LocationEntry le : WarpAPI.warps){
            if(le != null && le.getLocationName().equals(name)){
                return le;
            }
        }
        return null;
    }

    public static void removeWarp(LocationEntry le){
        WarpAPI.warps.remove(le);
    }

    public static void addWarp(LocationEntry le){
        WarpAPI.warps.add(le);
    }

}
