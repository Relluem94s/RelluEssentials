package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import de.relluem94.minecraft.server.spigot.essentials.registries.RelluEssentialsRegistry;

/**
 * Defines the contract for integrating external plugins with the RelluEssentials core.
 *
 * <p>Implementations of this interface allow third-party plugins to participate in the
 * RelluEssentials lifecycle and access its registries during initialization and shutdown.</p>
 *
 * @author rellu
 * @version 2
 * @since 4.4
 */
public interface RelluEssentialsIntegration {

  /**
   * Returns the human-readable name of the integrating plugin.
   *
   * <p>This name is typically used for logging, diagnostics, and status reporting within
   * RelluEssentials.</p>
   *
   * @return the display name of the integrating plugin
   */
  String getPluginName();

  /**
   * Returns the version string of the integrating plugin.
   *
   * <p>The version is used to identify the exact release of the plugin that is currently
   * integrated with RelluEssentials.</p>
   *
   * @return the version of the integrating plugin
   */
  String getPluginVersion();

  /**
   * Called when RelluEssentials is initialized and ready for integrations to register
   * their features.
   *
   * <p>Use the provided registry to register commands, services, listeners, or other
   * integration points with the RelluEssentials core.</p>
   *
   * @param api the RelluEssentials registry providing access to core integration hooks
   */
  void onRelluEssentialsInit(RelluEssentialsRegistry api);

  /**
   * Called when RelluEssentials is shutting down and integrations should release resources.
   *
   * <p>Implementations should use this hook to unregister components, stop running tasks,
   * and perform any necessary cleanup related to the integration.</p>
   */
  void onRelluEssentialsShutdown();
}