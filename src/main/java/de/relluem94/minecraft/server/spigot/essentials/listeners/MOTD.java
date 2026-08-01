package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PluginInformationEntry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MOTD implements Listener {

  @EventHandler
  public void onPing(ServerListPingEvent e) {
    PluginInformationEntry pie = RelluEssentials.getInstance().getPluginInformation();
    e.setMotd(pie.getMotdMessage());
    e.setMaxPlayers(pie.getMotdPlayers());
  }
}