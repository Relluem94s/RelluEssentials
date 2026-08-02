package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ProtectionEntry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.jetbrains.annotations.NotNull;

public class BlockPistonProtect implements ListenerConstruct {

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onBlockPistonExtend(@NotNull BlockPistonExtendEvent e) {
    for (Block b : e.getBlocks()) {
      ProtectionEntry protection = RelluEssentials.getInstance().getProtectionRegistry()
          .getProtectionEntry(b.getLocation());
      if (protection != null || isProtected(b, BlockFace.UP) || isProtected(b, BlockFace.DOWN)) {
        e.setCancelled(!b.getType().equals(Material.WATER));
        break;
      }
    }
  }

  @EventHandler
  public void onBlockPistonRetract(@NotNull BlockPistonRetractEvent e) {
    for (Block b : e.getBlocks()) {
      ProtectionEntry protection = RelluEssentials.getInstance().getProtectionRegistry()
          .getProtectionEntry(b.getLocation());
      if (protection != null || isProtected(b, BlockFace.UP) || isProtected(b, BlockFace.DOWN)) {
        e.setCancelled(true);
        break;
      }
    }
  }

  private boolean isProtected(@NotNull Block b, BlockFace bf) {
    return RelluEssentials.getInstance().getProtectionRegistry()
        .getProtectionEntry(b.getRelative(bf).getLocation()) != null;
  }
}
