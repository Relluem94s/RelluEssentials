package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

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

    public static LocationEntry getWarp(String name, World world){
        for(LocationEntry le : WarpAPI.warps){
            if(le != null && le.getLocation() != null && le.getLocation().getWorld() != null && le.getLocationName().equals(name) && le.getLocation().getWorld().equals(world)){
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

    public static List<LocationEntry> getWarps(){
        return warps;
    }

    public static List<LocationEntry> getWarps(World world){
        List<LocationEntry> filteredWarps = new ArrayList<>();

        for(LocationEntry le : warps){
            if(le != null && le.getLocation() != null && le.getLocation().getWorld() != null && le.getLocation().getWorld().equals(world)){
                filteredWarps.add(le);
            }
        }

        return filteredWarps;
    }

}
