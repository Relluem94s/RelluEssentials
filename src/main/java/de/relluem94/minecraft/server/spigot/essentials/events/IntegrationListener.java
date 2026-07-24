package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.api.RelluEssentialsAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.RelluEssentialsIntegration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.jspecify.annotations.NonNull;

public class IntegrationListener implements Listener {

    @EventHandler
    public void onPluginEnable(@NonNull PluginEnableEvent event) {
        if (event.getPlugin() instanceof RelluEssentialsIntegration integration) {
            RelluEssentialsAPI.getInstance().registerIntegration(integration);
        }
    }

    @EventHandler
    public void onPluginDisable(@NonNull PluginDisableEvent event) {
        if (event.getPlugin() instanceof RelluEssentialsIntegration integration) {
            RelluEssentialsAPI.getInstance().unregisterIntegration(integration);
        }
    }
}