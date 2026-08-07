package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionActionService;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockModifyProtect implements ListenerConstruct {

  private ProtectionActionService protectionActionService;

  @Override
  public void injectContext(ServiceContext context) {
    this.protectionActionService = context.getProtectionActionService();
  }

  @EventHandler
  public void placeBlocks(@NotNull BlockPlaceEvent e) {
    e.setCancelled(!protectionActionService.protectBlock(e.getPlayer(), e.getBlock()));
  }

  @EventHandler
  public void onBlockBreak(@NotNull BlockBreakEvent e) {
    if (protectionActionService.removeProtectionFromBlock(e.getPlayer(), e.getBlock())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockMove(@NotNull BlockFromToEvent e) {
    Block block = e.getToBlock();
    if (RelluEssentials.getInstance().getProtectionRegistry()
        .isProtectableMaterial(block.getType())) {
      if (RelluEssentials.getInstance().getProtectionRegistry()
          .getProtectionEntry(block.getLocation())
          != null) {
        e.setCancelled(true);
      }
    }
  }
}