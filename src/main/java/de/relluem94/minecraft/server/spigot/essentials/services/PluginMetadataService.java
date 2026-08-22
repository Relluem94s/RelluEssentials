package de.relluem94.minecraft.server.spigot.essentials.services;

import org.bukkit.plugin.Plugin;

/**
 * Provides access to the technical metadata of the plugin.
 */
public class PluginMetadataService {

  private final Plugin plugin;

  public PluginMetadataService(Plugin plugin) {
    this.plugin = plugin;
  }

  public String getName() {
    return plugin.getName();
  }

  public String getVersion() {
    return plugin.getDescription().getVersion();
  }

  public String getMainClass() {
    return plugin.getDescription().getMain();
  }

  public Plugin getPlugin() {
    return plugin;
  }
}