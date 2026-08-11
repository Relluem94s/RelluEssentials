package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class MOTD implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onPing(ServerListPingEvent e) {
    PluginInformationEntry pie = serviceContext.getPluginInformationService()
        .getPluginInformation();
    e.setMotd(pie.getMotdMessage());
    e.setMaxPlayers(pie.getMotdPlayers());
  }
}