package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jspecify.annotations.NonNull;

/**
 *
 * @author rellu
 */
public class BlockPlace implements ListenerConstruct {

  private GroupService groupService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @EventHandler
  public void placeBlocks(@NonNull BlockPlaceEvent e) {
    if (e.getBlock().getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)) {
      e.setCancelled(
          !groupService.isSenderAuthorized(e.getPlayer(), "mod"));
    }
  }

  @EventHandler
  public void breakBlocks(@NonNull BlockBreakEvent e) {
    if (e.getBlock().getWorld().getName().equals(Constants.PLUGIN_WORLD_LOBBY)) {
      e.setCancelled(
          !groupService.isSenderAuthorized(e.getPlayer(), "mod"));
    }
  }
}
