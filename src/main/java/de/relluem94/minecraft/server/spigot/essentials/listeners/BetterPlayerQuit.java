package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NonNull;


public class BetterPlayerQuit implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onLeave(@NonNull PlayerQuitEvent e) {
    e.setQuitMessage(null);
    Player p = e.getPlayer();

    if (SudoManager.sudoers.containsKey(p.getUniqueId())) {
      Sudo.exitSudo(Objects.requireNonNull(Bukkit.getPlayer(p.getUniqueId())),
          serviceContext.getTranslationService(), serviceContext.getPlayerService());
    }

    serviceContext.getPlayerService().savePlayer(p);
    serviceContext.getBuyBackService().clearBuyBackHistory(p);

    Bukkit.broadcastMessage(
        serviceContext.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_QUIT_MESSAGE, p.getCustomName()));
    serviceContext.getTeleportService().teleportWorld(p, Constants.PLUGIN_WORLD_LOBBY, true);
    ScoreBoardManager.removePlayer(e.getPlayer().getUniqueId());
    serviceContext.getNpcDialogueService().resetPlayerProgress(e.getPlayer().getUniqueId());
  }
}