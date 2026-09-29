package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import org.bukkit.event.Listener;

/**
 * Represents the base contract for all event listener implementations in the plugin.
 *
 * <p>Implementations of this interface are Bukkit event listeners that receive a shared {@link ServiceContext}
 * instance, allowing them to access services and configuration managed by the plugin's core.</p>
 *
 * <p>Every listener that should participate in the centralized service handling and lifecycle
 * management must implement this interface.</p>
 *
 * @author rellu
 * @version 2.0
 * @since 4.4
 */
public interface ListenerConstruct extends Listener {

  /**
   * Injects the shared {@link ServiceContext} into this listener.
   *
   * <p>This method is called by the plugin infrastructure to provide access to core services,
   * registries, and configuration before the listener starts handling events.</p>
   *
   * @param context the service context providing access to shared services and configuration
   *                required by this listener
   */
  void injectContext(ServiceContext context);
}