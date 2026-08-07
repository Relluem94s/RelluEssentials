package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

public class EntityExplodeProtect implements ListenerConstruct {

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEntityExplode(@NotNull EntityExplodeEvent event) {
    for (Block block : event.blockList()) {
      ProtectionEntry protection = RelluEssentials.getInstance().getProtectionRegistry()
          .getProtectionEntry(block.getLocation());
      if (protection != null) {
        RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(protection);
        continue;
      }
      event.setCancelled(true);
    }
  }
}