package de.relluem94.minecraft.server.spigot.essentials.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EventWrapper {

  private final Listener listener;
  private boolean initialised = false;

  public EventWrapper(@NotNull Listener listener) {
    this.listener = listener;
  }

  public void init(JavaPlugin javaPlugin) {
    if (initialised) {
      return;
    }

    Bukkit.getServer().getPluginManager().registerEvents(listener, javaPlugin);
    initialised = true;
  }
}