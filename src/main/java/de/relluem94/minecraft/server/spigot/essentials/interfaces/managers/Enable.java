package de.relluem94.minecraft.server.spigot.essentials.interfaces.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

/**
 * Defines a contract for components that require an enable lifecycle method.
 *
 * <p>Implementing classes are expected to perform their initialization and registration logic within
 * the {@link #enable()} method. This interface is typically used by manager classes that need to be
 * activated during the plugin startup phase.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * public class EventManager implements IEnable {
 *
 *     @Override
 *     public void enable(RelluEssentials plugin) {
 *         RelluEssentials.getEventWrapperList()
 *             .forEach(eventWrapper -> eventWrapper.init(plugin));
 *     }
 * }
 * }</pre>
 */
public interface Enable {

  /**
   * Enables this component by executing all necessary initialization and registration logic.
   *
   * <p>This method is called during the plugin startup phase and should contain all setup logic
   * required for the implementing component to function correctly.
   * </p>
   */
  void enable(RelluEssentials plugin);
}
