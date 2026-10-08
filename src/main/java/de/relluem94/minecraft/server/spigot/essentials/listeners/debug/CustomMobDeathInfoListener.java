package de.relluem94.minecraft.server.spigot.essentials.listeners.debug;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsMobDeathEvent;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

/**
 * Listens for {@link RelluEssentialsMobDeathEvent} and logs death information including mob statistics and entity
 * details to the console.
 *
 * @author rellu
 */
// @ListenerName("CustomMobDeathInfoListener")
// Uncomment @ListenerName to Enable
public class CustomMobDeathInfoListener implements ListenerConstruct {

  private ServiceContext serviceContext;

  /**
   * Handles the {@link RelluEssentialsMobDeathEvent} and logs relevant death and entity info.
   *
   * @param event the custom mob death event
   */
  @EventHandler
  public void onCustomMobDeath(@NonNull RelluEssentialsMobDeathEvent event) {
    String mobUuid = event
        .getCustomMob()
        .getLivingEntity()
        .getUniqueId()
        .toString();
    String entityType = event
        .getCustomMob()
        .getLivingEntity()
        .getType()
        .name();
    String definitionKey = event
        .getDefinition()
        .key()
        .toString();

    int aliveMobs = serviceContext
        .getMobService()
        .countAliveMobs();
    int totalSpawnedMobs = serviceContext
        .getMobService()
        .countSpawnedMobs();
    int totalDefinitions = serviceContext
        .getMobService()
        .findAllDefinitions()
        .size();

    serviceContext
        .getPluginMetadataService()
        .getPlugin()
        .getLogger()
        .info("[MobDeath] Definition: " + definitionKey + " | UUID: " + mobUuid + " | Type: " + entityType
            + " | Alive: " + aliveMobs + " | Total: " + totalSpawnedMobs + " | Definitions: " + totalDefinitions);
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }
}