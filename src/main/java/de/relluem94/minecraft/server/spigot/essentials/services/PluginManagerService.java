package de.relluem94.minecraft.server.spigot.essentials.services;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PluginManagerService {

  private final Plugin plugin;

  public PluginManagerService(Plugin plugin) {
    this.plugin = plugin;
  }

  private PluginManager pluginManager() {
    return plugin.getServer().getPluginManager();
  }

  public void callEvent(Event event) {
    pluginManager().callEvent(event);
  }

  public boolean isPluginEnabled(String pluginName) {
    return pluginManager().isPluginEnabled(pluginName);
  }

  public Plugin getPlugin(String pluginName) {
    return pluginManager().getPlugin(pluginName);
  }
}