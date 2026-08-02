package de.relluem94.minecraft.server.spigot.essentials.wrapper;

import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ListenerWrapper {

  private final ListenerConstruct construct;
  private boolean initialised = false;

  public ListenerWrapper(@NotNull ListenerConstruct listenerConstruct) {
    this.construct = listenerConstruct;
  }

  public void init(JavaPlugin javaPlugin, ServiceContext serviceContext) {
    if (initialised) {
      return;
    }
    construct.injectContext(serviceContext);
    Bukkit.getServer().getPluginManager().registerEvents(construct, javaPlugin);
    initialised = true;
  }
}