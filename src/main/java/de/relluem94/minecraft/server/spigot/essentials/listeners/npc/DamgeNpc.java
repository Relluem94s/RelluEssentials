package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.entity.Mannequin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jspecify.annotations.NonNull;

@ListenerName("DamgeNpc")
public class DamgeNpc implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    serviceContext = context;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamage(@NonNull EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Mannequin)) {
      return;
    }

    boolean isTrackedNPC = serviceContext.getNpcService().getNPCs().stream()
        .anyMatch(npc -> event.getEntity().getUniqueId().equals(npc.getEntityUUID()));

    if (isTrackedNPC) {
      event.setCancelled(true);
    }
  }
}