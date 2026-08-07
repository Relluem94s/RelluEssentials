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
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jspecify.annotations.NonNull;

public class BetterPlayerJoin implements ListenerConstruct {

  GroupService groupService;
  TranslationService translationService;
  private BankService bankService;
  private PlayerService playerService;

  @Override
  public void injectContext(ServiceContext context) {
    this.translationService = context.getTranslationService();
    this.groupService = context.getGroupService();
    this.bankService = context.getBankService();
    this.playerService = context.getPlayerService();
  }

  private void addPlayer(@NonNull Player p) {
    PlayerEntry pe = RelluEssentials.getInstance().getDatabaseHelper()
        .getPlayer(p.getUniqueId().toString());
    if (pe == null) {
      pe = new PlayerEntry();
      pe.setCreatedBy(1);
      pe.setName(p.getName());
      pe.setCustomName(p.getDisplayName());
      pe.setGroup(groupService.resolveGroupWithFallback("user"));
      pe.setUuid(p.getUniqueId().toString());
      RelluEssentials.getInstance().getDatabaseHelper().insertPlayer(pe);

      pe = RelluEssentials.getInstance().getDatabaseHelper().getPlayer(p.getUniqueId().toString());
      p.sendMessage(translationService.get(MessageKey.PLUGIN_EVENT_FIRST_JOIN_MESSAGE));
    } else {
      if (pe.getName() == null) {
        pe.setName(p.getName());
        RelluEssentials.getInstance().getDatabaseHelper().updatePlayer(pe);
        pe = RelluEssentials.getInstance().getDatabaseHelper()
            .getPlayer(p.getUniqueId().toString());
      }
    }

    RelluEssentials.getInstance().getPlayerRegistry().putPlayerEntry(p.getUniqueId(), pe);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    if (RelluEssentials.getInstance().isUnitTest()) {
      return;
    }

    e.setJoinMessage(null);
    Player p = e.getPlayer();
    addPlayer(p);

    PluginInformationEntry pie = RelluEssentials.getInstance().getPluginInformation();
    p.setPlayerListHeader(pie.getTabHeader());
    p.setPlayerListFooter(pie.getTabFooter());

    playerService.setFlying(p);
    playerService.setAFK(p, true);
    Bukkit.broadcastMessage(
        translationService.get(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));

    WorldHelper.loadWorldGroupInventory(p);

    bankService.payInterestToPlayer(e.getPlayer());

    if (WorldHelper.isInWorld(p, Constants.PLUGIN_WORLD_LOBBY)) {
      PlayerHelper.setLobbyItems(p);
    }

    Bukkit.getScheduler().runTaskLater(
        RelluEssentials.getInstance(),
        () -> ScoreBoardManager.applyToPlayer(e.getPlayer()),
        10L
    );
  }

  @EventHandler
  public void login(PlayerLoginEvent e) {
    int maxPlayers = Bukkit.getServer().getMaxPlayers();
    int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();

    if (onlinePlayers >= maxPlayers) {
      e.disallow(PlayerLoginEvent.Result.KICK_FULL,
          translationService.get(MessageKey.PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN));
    }
  }

  @EventHandler
  public void checkInterest(AsyncPlayerPreLoginEvent e) {
    if (RelluEssentials.getInstance().isUnitTest()) {
      return;
    }

    bankService.checkInterest(e.getUniqueId(), false);
  }
}