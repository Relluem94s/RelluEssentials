package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NonNull;

/**
 * Listener responsible for handling player disconnection logic, including cleaning up player data,
 * sudo status, and broadcasting quit messages.
 */
@ListenerName("BetterPlayerQuit")
public class BetterPlayerQuit implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Handles the player quit event to perform cleanup and broadcast the departure message.
   *
   * @param event the player quit event
   */
  @EventHandler
  public void onLeave(@NonNull PlayerQuitEvent event) {
    event.setQuitMessage(null);
    Player p = event.getPlayer();

    if (SudoManager.sudoers.containsKey(p.getUniqueId())) {
      Sudo.exitSudo(Objects.requireNonNull(
          serviceContext.getPluginMetadataService().getPlugin().getServer()
              .getPlayer(p.getUniqueId())), serviceContext);
    }

    serviceContext.getPlayerService().savePlayer(p);
    serviceContext.getBuyBackService().clearBuyBackHistory(p);

    serviceContext.getPluginMetadataService().getPlugin().getServer().broadcastMessage(
        serviceContext.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_QUIT_MESSAGE, p.getCustomName()));
    serviceContext.getTeleportService().teleportWorld(p, Constants.PLUGIN_WORLD_LOBBY, true);
    ScoreBoardManager.removePlayer(p.getUniqueId());
    serviceContext.getNpcDialogueProgressService().resetPlayerProgress(p.getUniqueId());
    serviceContext.getClipboardService().removeClipboard(p);
  }
}