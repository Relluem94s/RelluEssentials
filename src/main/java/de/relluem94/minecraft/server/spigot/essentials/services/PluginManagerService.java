package de.relluem94.minecraft.server.spigot.essentials.services;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Service responsible for interacting with the Bukkit PluginManager.
 */
public class PluginManagerService {

  private final Plugin plugin;

  /**
   * Creates a new PluginManagerService.
   *
   * @param plugin the plugin instance used to access the server
   */
  public PluginManagerService(Plugin plugin) {
    this.plugin = plugin;
  }

  private PluginManager pluginManager() {
    return plugin.getServer().getPluginManager();
  }

  /**
   * Calls a specific event.
   *
   * @param event the event to be called
   */
  public void callEvent(Event event) {
    pluginManager().callEvent(event);
  }

  /**
   * Checks if a specific plugin is enabled.
   *
   * @param pluginName the name of the plugin to check
   * @return true if the plugin is enabled, false otherwise
   */
  @SuppressWarnings("unused")
  public boolean isPluginEnabled(String pluginName) {
    return pluginManager().isPluginEnabled(pluginName);
  }

  /**
   * Retrieves a plugin instance by its name.
   *
   * @param pluginName the name of the plugin to retrieve
   * @return the plugin instance, or null if not found
   */
  @SuppressWarnings("unused")
  public Plugin getPlugin(String pluginName) {
    return pluginManager().getPlugin(pluginName);
  }
}