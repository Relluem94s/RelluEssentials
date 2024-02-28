package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.bukkit.World;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;

@Getter
public class WarpAPI {
    private final List<LocationEntry> warps;

    public WarpAPI(List<LocationEntry>  warps){
        this.warps = warps;
    }

    public LocationEntry getWarp(String name){
        for(LocationEntry le : warps){
            if(le != null && le.getLocationName().equals(name)){
                return le;
            }
        }
        return null;
    }

    public LocationEntry getWarp(String name, World world){
        for(LocationEntry le : warps){
            if(le != null && le.getLocation() != null && le.getLocation().getWorld() != null && le.getLocationName().equals(name) && le.getLocation().getWorld().equals(world)){
                return le;
            }
        }
        return null;
    }

    public void removeWarp(LocationEntry le){
        warps.remove(le);
    }

    public void addWarp(LocationEntry le){
        warps.add(le);
    }

    public List<LocationEntry> getWarps(World world){
        List<LocationEntry> filteredWarps = new ArrayList<>();

        for(LocationEntry le : warps){
            if(le != null && le.getLocation() != null && le.getLocation().getWorld() != null && le.getLocation().getWorld().equals(world)){
                filteredWarps.add(le);
            }
        }

        return filteredWarps;
    }

}
