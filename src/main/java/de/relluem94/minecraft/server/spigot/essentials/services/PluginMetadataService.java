package de.relluem94.minecraft.server.spigot.essentials.services;

import org.bukkit.plugin.Plugin;

/**
 * Provides access to the technical metadata of the plugin.
 */
public class PluginMetadataService {

  private final Plugin plugin;

  /**
   * Creates a new instance of {@link PluginMetadataService}.
   *
   * @param plugin the plugin whose metadata will be accessed
   */
  public PluginMetadataService(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Gets the name of the plugin.
   *
   * @return the plugin name
   */
  public String getName() {
    return plugin.getName();
  }

  /**
   * Gets the version of the plugin.
   *
   * @return the plugin version
   */
  public String getVersion() {
    return plugin.getDescription().getVersion();
  }

  /**
   * Gets the main class of the plugin.
   *
   * @return the main class name
   */
  @SuppressWarnings("unused")
  public String getMainClass() {
    return plugin.getDescription().getMain();
  }

  /**
   * Gets the underlying plugin instance.
   *
   * @return the plugin instance
   */
  public Plugin getPlugin() {
    return plugin;
  }
}