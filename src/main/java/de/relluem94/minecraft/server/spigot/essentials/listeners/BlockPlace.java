package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jspecify.annotations.NonNull;

/**
 *
 * @author rellu
 */
@ListenerName("BlockPlace")
public class BlockPlace implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void placeBlocks(@NonNull BlockPlaceEvent e) {
    if (e.getBlock().getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)) {
      e.setCancelled(
          !serviceContext.getGroupService().isSenderAuthorized(e.getPlayer(), "mod"));
    }
  }

  @EventHandler
  public void breakBlocks(@NonNull BlockBreakEvent e) {
    if (e.getBlock().getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)) {
      e.setCancelled(
          !serviceContext.getGroupService().isSenderAuthorized(e.getPlayer(), "mod"));
    }
  }
}
