package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.entity.Mannequin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jspecify.annotations.NonNull;

public class DamgeNpc implements ListenerConstruct {

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamage(@NonNull EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Mannequin)) {
      return;
    }

    boolean isTrackedNPC = RelluEssentials.getInstance().getNpcService().getNPCs().stream()
        .anyMatch(npc -> event.getEntity().getUniqueId().equals(npc.getEntityUUID()));

    if (isTrackedNPC) {
      event.setCancelled(true);
    }
  }
}