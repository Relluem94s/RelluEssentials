package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class MOTD implements ListenerConstruct {


  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onPing(ServerListPingEvent e) {
    PluginInformationEntry pie = RelluEssentials.getInstance().getPluginInformation();
    e.setMotd(pie.getMotdMessage());
    e.setMaxPlayers(pie.getMotdPlayers());
  }
}