package de.relluem94.minecraft.server.spigot.essentials.wrappers;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for a {@link ListenerConstruct} that manages its initialization
 * and registration within the Bukkit event system.
 */
public class ListenerWrapper {

  private final ListenerConstruct construct;
  private boolean initialised = false;

  /**
   * Constructs a new ListenerWrapper.
   *
   * @param listenerConstruct the listener implementation to be wrapped
   */
  public ListenerWrapper(@NotNull ListenerConstruct listenerConstruct) {
    this.construct = listenerConstruct;
  }

  /**
   * Initializes the listener by injecting the service context and
   * registering it with the Bukkit plugin manager. This method only
   * performs the registration once.
   *
   * @param javaPlugin the plugin instance to register the events under
   * @param serviceContext the context to be injected into the listener
   */
  public void init(JavaPlugin javaPlugin, ServiceContext serviceContext) {
    if (initialised) {
      return;
    }
    construct.injectContext(serviceContext);
    Bukkit.getServer().getPluginManager().registerEvents(construct, javaPlugin);
    initialised = true;
  }
}