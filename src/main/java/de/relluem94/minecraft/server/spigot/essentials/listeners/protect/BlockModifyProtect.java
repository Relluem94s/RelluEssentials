package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionActionHelper.protectBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionActionHelper.removeProtectionFromBlock;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockModifyProtect implements Listener {

  @EventHandler
  public void placeBlocks(@NotNull BlockPlaceEvent e) {
    e.setCancelled(!protectBlock(e.getPlayer(), e.getBlock()));
  }

  @EventHandler
  public void onBlockBreak(@NotNull BlockBreakEvent e) {
    if (removeProtectionFromBlock(e.getPlayer(), e.getBlock())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockMove(@NotNull BlockFromToEvent e) {
    Block block = e.getToBlock();
    if (RelluEssentials.getInstance().getProtectionRegistry().isProtectableMaterial(block.getType())) {
      if (RelluEssentials.getInstance().getProtectionRegistry().getProtectionEntry(block.getLocation())
          != null) {
        e.setCancelled(true);
      }
    }
  }
}