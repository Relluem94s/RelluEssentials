package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsMobDeathEvent;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jspecify.annotations.NonNull;

/**
 * Listens for entity death events and fires {@link RelluEssentialsMobDeathEvent} when the dying entity is a registered
 * custom mob.
 *
 * @author rellu
 */
@ListenerName("CustomMobDeathListener")
public class CustomMobDeathListener implements ListenerConstruct {

  private ServiceContext serviceContext;

  /**
   * Handles the Bukkit {@link EntityDeathEvent} and checks whether the entity is a custom mob.
   *
   * @param event the entity death event
   */
  @EventHandler
  public void onEntityDeath(@NonNull EntityDeathEvent event) {
    LivingEntity livingEntity = event.getEntity();

    serviceContext.getMobService()
        .findSpawnedMobByUuid(livingEntity.getUniqueId())
        .ifPresent(customMob -> serviceContext.getMobService()
            .findDefinition(customMob.getDefinitionKey())
            .ifPresent(definition -> {
              serviceContext.getMobService().unregisterSpawnedMob(livingEntity.getUniqueId());
              serviceContext.getPluginManagerService()
                  .callEvent(new RelluEssentialsMobDeathEvent(customMob, definition));
            }));
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }
}