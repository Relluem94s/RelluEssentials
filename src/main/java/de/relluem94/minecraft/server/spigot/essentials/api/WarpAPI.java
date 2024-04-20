package de.relluem94.minecraft.server.spigot.essentials.api;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record WarpAPI(List<LocationEntry> warps) {

    public @Nullable LocationEntry getWarp(String name) {
        for (LocationEntry le : warps) {
            if (le != null && le.getLocationName().equals(name)) {
                return le;
            }
        }
        return null;
    }

    public @Nullable LocationEntry getWarp(String name, World world) {
        for (LocationEntry le : warps) {
            if (le != null && le.getLocation() != null && le.getLocation().getWorld() != null && le.getLocationName().equals(name) && le.getLocation().getWorld().equals(world)) {
                return le;
            }
        }
        return null;
    }

    public void removeWarp(LocationEntry le) {
        warps.remove(le);
    }

    public void addWarp(LocationEntry le) {
        warps.add(le);
    }

    public @NotNull List<LocationEntry> getWarps(World world) {
        List<LocationEntry> filteredWarps = new ArrayList<>();

        for (LocationEntry le : warps) {
            if (le != null && le.getLocation() != null && le.getLocation().getWorld() != null && le.getLocation().getWorld().equals(world)) {
                filteredWarps.add(le);
            }
        }

        return filteredWarps;
    }
}