package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

public class EntityExplodeProtect implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEntityExplode(@NotNull EntityExplodeEvent event) {
    for (Block block : event.blockList()) {
      boolean blockWasProtected = serviceContext.getProtectionService()
          .removeExplodedBlockProtectionOrCancelExplosion(block);
      if (!blockWasProtected) {
        event.setCancelled(true);
      }
    }
  }
}