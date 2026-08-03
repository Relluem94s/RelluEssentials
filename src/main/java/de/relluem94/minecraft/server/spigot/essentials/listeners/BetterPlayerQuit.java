package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.translationService;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TeleportHelper.teleportWorld;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
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

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onLeave(@NonNull PlayerQuitEvent e) {
    e.setQuitMessage(null);
    Player p = e.getPlayer();

    if (SudoManager.sudoers.containsKey(p.getUniqueId())) {
      Sudo.exitSudo(Objects.requireNonNull(Bukkit.getPlayer(p.getUniqueId())));
    }

    PlayerHelper.savePlayer(p);
    RelluEssentials.getInstance().getBuyBackService().clearBuyBackHistory(p);

    Bukkit.broadcastMessage(
        translationService.get(MessageKey.PLUGIN_EVENT_QUIT_MESSAGE, p.getCustomName()));
    teleportWorld(p, Constants.PLUGIN_WORLD_LOBBY, true);
    ScoreBoardManager.removePlayer(e.getPlayer().getUniqueId());
    RelluEssentials.getInstance()
        .getNpcDialogueTracker()
        .resetPlayerProgress(e.getPlayer().getUniqueId());
  }
}