package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.entity.Mannequin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jspecify.annotations.NonNull;

public class DamgeNpc implements Listener {

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