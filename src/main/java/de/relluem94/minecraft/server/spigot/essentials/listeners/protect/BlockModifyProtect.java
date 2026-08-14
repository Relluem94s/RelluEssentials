package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

@ListenerName("BlockModifyProtect")
public class BlockModifyProtect implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void placeBlocks(@NotNull BlockPlaceEvent e) {
    e.setCancelled(
        !serviceContext.getProtectionActionService().protectBlock(e.getPlayer(), e.getBlock()));
  }

  @EventHandler
  public void onBlockBreak(@NotNull BlockBreakEvent e) {
    if (serviceContext.getProtectionActionService()
        .removeProtectionFromBlock(e.getPlayer(), e.getBlock())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockMove(@NotNull BlockFromToEvent e) {
    Block block = e.getToBlock();
    if (serviceContext.getProtectionService()
        .isProtectableMaterial(block.getType())) {
      if (serviceContext.getProtectionService()
          .getProtectionEntry(block.getLocation())
          != null) {
        e.setCancelled(true);
      }
    }
  }
}