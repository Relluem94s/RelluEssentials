package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ProtectionEntry;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.jetbrains.annotations.NotNull;

public class EntityBreakDoorProtect implements Listener {

  @EventHandler
  public void entityBreakDoor(@NotNull EntityBreakDoorEvent e) {
    Block b = e.getBlock();
    Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
    ProtectionEntry protection = RelluEssentials.getInstance().getProtectionRegistry()
        .getProtectionEntry(l);
    if (protection != null) {
      e.setCancelled(true);
    }
  }
}
