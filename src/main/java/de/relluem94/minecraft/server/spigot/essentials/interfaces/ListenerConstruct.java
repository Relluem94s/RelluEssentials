package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import org.bukkit.event.Listener;

public interface ListenerConstruct extends Listener {

  void injectContext(ServiceContext context);

}
