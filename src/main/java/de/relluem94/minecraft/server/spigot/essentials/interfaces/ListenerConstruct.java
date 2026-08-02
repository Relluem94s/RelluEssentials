package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import org.bukkit.event.Listener;

public interface ListenerConstruct extends Listener {

  void injectContext(ServiceContext context);

}
