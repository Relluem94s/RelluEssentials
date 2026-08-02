package de.relluem94.minecraft.server.spigot.essentials.interfaces.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

/**
 * Defines a contract for components that require a disable lifecycle method.
 *
 * <p>Implementing classes are expected to perform their cleanup and unregistration logic within the
 * {@link #disable(RelluEssentials plugin)} method. This interface is typically used by manager classes that need to be
 * deactivated during the plugin shutdown phase.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * public class ConfigManager implements IDisable {
 *
 *     @Override
 *     public void disable() {
 *         RelluEssentials.getInstance().saveConfig();
 *     }
 * }
 * }</pre>
 */
public interface Disable {

  /**
   * Disables this component by executing all necessary cleanup and unregistration logic.
   *
   * <p>This method is called during the plugin shutdown phase and should contain all teardown logic
   * required to cleanly shut down the implementing component.
   * </p>
   */
  void disable(RelluEssentials plugin);
}
