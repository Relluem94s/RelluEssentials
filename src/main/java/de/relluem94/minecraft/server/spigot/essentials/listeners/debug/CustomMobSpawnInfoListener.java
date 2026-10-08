package de.relluem94.minecraft.server.spigot.essentials.listeners.debug;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsMobSpawnEvent;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

/**
 * Listens for {@link RelluEssentialsMobSpawnEvent} and logs spawn information including mob statistics and entity
 * details to the console.
 *
 * @author rellu
 */
// @ListenerName("CustomMobSpawnInfoListener")
// Uncomment @ListenerName to Enable
public class CustomMobSpawnInfoListener implements ListenerConstruct {

  private ServiceContext serviceContext;

  /**
   * Handles the {@link RelluEssentialsMobSpawnEvent} and logs relevant spawn and entity info.
   *
   * @param event the custom mob spawn event
   */
  @EventHandler
  public void onCustomMobSpawn(@NonNull RelluEssentialsMobSpawnEvent event) {
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
    String entityLocation = event
        .getCustomMob()
        .getLivingEntity()
        .getLocation()
        .toString();
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
        .info("[MobSpawn] Definition: " + definitionKey + " | UUID: " + mobUuid + " | Type: " + entityType
            + " | Location: " + entityLocation + " | Alive: " + aliveMobs + " | Total: " + totalSpawnedMobs
            + " | Definitions: " + totalDefinitions);

    // event.setCancelled(true);
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }
}