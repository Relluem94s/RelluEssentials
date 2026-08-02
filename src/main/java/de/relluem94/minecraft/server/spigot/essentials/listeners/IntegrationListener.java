package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.RelluEssentialsIntegration;
import de.relluem94.minecraft.server.spigot.essentials.registry.RelluEssentialsRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.jspecify.annotations.NonNull;

public class IntegrationListener implements ListenerConstruct {


  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onPluginEnable(@NonNull PluginEnableEvent event) {
    if (event.getPlugin() instanceof RelluEssentialsIntegration integration) {
      RelluEssentialsRegistry.getInstance().registerIntegration(integration);
    }
  }

  @EventHandler
  public void onPluginDisable(@NonNull PluginDisableEvent event) {
    if (event.getPlugin() instanceof RelluEssentialsIntegration integration) {
      RelluEssentialsRegistry.getInstance().unregisterIntegration(integration);
    }
  }
}