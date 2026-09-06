package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_LOBBY;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jspecify.annotations.NonNull;

/**
 * Listener that intercepts stop commands issued by both the server console and players,
 * executing a controlled shutdown sequence that broadcasts a shutdown message,
 * teleports all online players to the lobby world, kicks them, and then shuts down the server.
 */
@ListenerName("ServerStopCommand")
public class ServerStopCommandListener implements ListenerConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for accessing plugin services.
   *
   * @param context the {@link ServiceContext} providing access to all required services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Handles stop commands issued via the server console.
   * Triggers the shutdown sequence when the command equals {@code stop}.
   *
   * @param event the {@link ServerCommandEvent} containing the issued command
   */
  @EventHandler
  public void onServerStopCommand(@NonNull ServerCommandEvent event) {
    if (!event.getCommand().equalsIgnoreCase("stop")) {
      return;
    }
    executeShutdownSequence();
  }

  /**
   * Handles stop commands issued by a player in chat.
   * Cancels the command and triggers the shutdown sequence when the message equals {@code /stop}.
   *
   * @param event the {@link PlayerCommandPreprocessEvent} containing the player's message
   */
  @EventHandler
  public void onPlayerStopCommand(@NonNull PlayerCommandPreprocessEvent event) {
    if (!event.getMessage().equalsIgnoreCase("/stop")) {
      return;
    }
    event.setCancelled(true);
    executeShutdownSequence();
  }

  private void executeShutdownSequence() {
    Server server = serviceContext.getPluginMetadataService().getPlugin().getServer();
    server.broadcastMessage(
        serviceContext.getTranslationService().get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN));

    serviceContext.getSchedulerService().runTaskLater(() -> server.getOnlinePlayers()
        .forEach(op -> {
          serviceContext.getTeleportService().teleportWorld(op, PLUGIN_WORLD_LOBBY, true);
          op.kickPlayer(serviceContext.getTranslationService()
              .get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN));
        }), 10L);

    serviceContext.getSchedulerService().runTaskLater(server::shutdown, 20L);
  }
}
