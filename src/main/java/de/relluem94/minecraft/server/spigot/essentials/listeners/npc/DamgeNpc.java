package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.entity.Mannequin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jspecify.annotations.NonNull;

/**
 * Listener that protects tracked NPCs from taking damage.
 *
 * <p>Every {@link org.bukkit.event.entity.EntityDamageEvent} targeting a
 * {@link org.bukkit.entity.Mannequin} is checked against the NPCs managed by the NPC service.
 * If the mannequin belongs to a tracked NPC, the damage event is canceled.</p>
 */
@ListenerName("DamgeNpc")
public class DamgeNpc implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    serviceContext = context;
  }

  /**
   * Cancels damage dealt to mannequins that are tracked as NPCs.
   *
   * <p>Events for entities that are not mannequins, or for mannequins that are not tracked
   * by the NPC service, are left untouched. The handler runs at
   * {@link EventPriority#HIGHEST} so that the cancellation takes precedence
   * over other listeners.</p>
   *
   * @param event the damage event to inspect and possibly cancel
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamage(@NonNull EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Mannequin)) {
      return;
    }

    boolean isTrackedNpc = serviceContext.getNpcService().getNpcs().stream()
        .anyMatch(npc -> event.getEntity().getUniqueId().equals(npc.getEntityUuid()));

    if (isTrackedNpc) {
      event.setCancelled(true);
    }
  }
}