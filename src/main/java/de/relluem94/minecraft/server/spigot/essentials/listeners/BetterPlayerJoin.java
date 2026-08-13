package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jspecify.annotations.NonNull;

public class BetterPlayerJoin implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  private void addPlayer(@NonNull Player p) {
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerByUuid(p.getUniqueId().toString());
    if (pe == null) {
      pe = new PlayerEntry();
      pe.setCreatedBy(1);
      pe.setName(p.getName());
      pe.setCustomName(p.getDisplayName());
      pe.setGroup(serviceContext.getGroupService().resolveGroupWithFallback("user"));
      pe.setUuid(p.getUniqueId().toString());
      serviceContext.getPlayerService().registerNewPlayer(pe);

      pe = serviceContext.getPlayerService().getPlayerByUuid(p.getUniqueId().toString());
      p.sendMessage(
          serviceContext.getTranslationService().get(MessageKey.PLUGIN_EVENT_FIRST_JOIN_MESSAGE));
    } else {
      if (pe.getName() == null) {
        pe.setName(p.getName());
        serviceContext.getPlayerService().savePlayer(pe);
        pe = serviceContext.getPlayerService().getPlayerByUuid(p.getUniqueId().toString());
      }
    }

    serviceContext.getPlayerService().putPlayerEntry(p.getUniqueId(), pe);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    if (RelluEssentials.getInstance().isUnitTest()) {
      return;
    }

    e.setJoinMessage(null);
    Player p = e.getPlayer();
    addPlayer(p);

    PluginInformationEntry pie = serviceContext.getPluginInformationService().getPluginInformation();
    p.setPlayerListHeader(pie.getTabHeader());
    p.setPlayerListFooter(pie.getTabFooter());

    serviceContext.getPlayerService().setFlying(p);
    serviceContext.getPlayerService().setAFK(p, true);
    Bukkit.broadcastMessage(
        serviceContext.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));

    serviceContext.getWorldGroupService().loadWorldGroupInventoryForPlayer(p);

    serviceContext.getBankService().payInterestToPlayer(e.getPlayer());

    if (WorldHelper.isInWorld(p, Constants.PLUGIN_WORLD_LOBBY)) {
      PlayerHelper.setLobbyItems(p);
    }

    serviceContext.getSchedulerService().runTaskLater(
        () -> ScoreBoardManager.applyToPlayer(e.getPlayer(), serviceContext.getWorldGroupService()),
        10L
    );
  }

  @EventHandler
  public void login(PlayerLoginEvent e) {
    int maxPlayers = Bukkit.getServer().getMaxPlayers();
    int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();

    if (onlinePlayers >= maxPlayers) {
      e.disallow(PlayerLoginEvent.Result.KICK_FULL,
          serviceContext.getTranslationService()
              .get(MessageKey.PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN));
    }
  }

  @EventHandler
  public void checkInterest(AsyncPlayerPreLoginEvent e) {
    if (RelluEssentials.getInstance().isUnitTest()) {
      return;
    }

    serviceContext.getBankService().checkInterest(e.getUniqueId(), false);
  }
}