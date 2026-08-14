package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.jetbrains.annotations.NotNull;

@ListenerName("EntityBreakDoorProtect")
public class EntityBreakDoorProtect implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void entityBreakDoor(@NotNull EntityBreakDoorEvent e) {
    Block b = e.getBlock();
    Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
    ProtectionEntry protection = serviceContext.getProtectionService()
        .getProtectionEntry(l);
    if (protection != null) {
      e.setCancelled(true);
    }
  }
}
