package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationEntry;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarpRegistry {

  private List<LocationEntry> warps;

  public WarpRegistry(List<LocationEntry> warps) {
    this.warps = warps;
  }


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
      if (le != null && le.getLocation() != null && le.getLocation().getWorld() != null
          && le.getLocationName().equals(name) && le.getLocation().getWorld().equals(world)) {
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
      if (le != null && le.getLocation() != null && le.getLocation().getWorld() != null
          && le.getLocation().getWorld().equals(world)) {
        filteredWarps.add(le);
      }
    }

    return filteredWarps;
  }
}