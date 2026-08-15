package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_LOBBY;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

@ListenerName("ServerStopCommand")
public class ServerStopCommandListener implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onServerStopCommand(ServerCommandEvent event) {
    if (!event.getCommand().equalsIgnoreCase("stop")) {
      return;
    }
    executeShutdownSequence();
  }

  @EventHandler
  public void onPlayerStopCommand(PlayerCommandPreprocessEvent event) {
    if (!event.getMessage().equalsIgnoreCase("/stop")) {
      return;
    }
    event.setCancelled(true);
    executeShutdownSequence();
  }

  private void executeShutdownSequence() {
    Bukkit.broadcastMessage(
        serviceContext.getTranslationService().get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN));

    serviceContext.getSchedulerService().runTaskLater(() -> {
      Bukkit.getOnlinePlayers().forEach(op -> {
        serviceContext.getTeleportService().teleportWorld(op, PLUGIN_WORLD_LOBBY, true);
        op.kickPlayer(serviceContext.getTranslationService()
            .get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN));
      });
    }, 10L);

    Bukkit.getServer().getScheduler()
        .runTaskLater(RelluEssentials.getInstance(), Bukkit.getServer()::shutdown, 20L);
  }
}
