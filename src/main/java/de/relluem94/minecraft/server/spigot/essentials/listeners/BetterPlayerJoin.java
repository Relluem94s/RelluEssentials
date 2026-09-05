package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jspecify.annotations.NonNull;

/**
 * Listener that handles player join related events.
 *
 * <p>Manages player registration, tab header/footer assignment, fly and AFK state,
 * join broadcast messages, world group inventory loading, bank interest payouts, lobby item
 * assignment, and scoreboard application on join.
 *
 * <p>Also enforces server capacity limits on login and triggers interest
 * calculation checks before a player fully connects.
 */
@ListenerName("BetterPlayerJoin")
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

  /**
   * Handles the {@link PlayerJoinEvent} to initialize a joining player's session.
   *
   * <p>Suppresses the default join message, registers or loads the player entry,
   * applies tab header and footer, sets fly and AFK state, broadcasts a custom join message</p>
   *
   * <p>Loads world group inventory, pays bank interest, assigns lobby items if the player is in the
   * lobby world, and schedules scoreboard application.</p>
   *
   * @param e the event fired when a player joins the server
   */
  @EventHandler
  public void onJoin(@NonNull PlayerJoinEvent e) {
    e.setJoinMessage(null);
    Player p = e.getPlayer();
    addPlayer(p);

    PluginInformationEntry pie = serviceContext.getPluginInformationService()
        .getPluginInformation();
    p.setPlayerListHeader(pie.getTabHeader());
    p.setPlayerListFooter(pie.getTabFooter());

    serviceContext.getPlayerService().setFlying(p);
    serviceContext.getPlayerService().setAfk(p, true);
    serviceContext.getPluginMetadataService().getPlugin().getServer().broadcastMessage(
        serviceContext.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE, p.getCustomName()));

    serviceContext.getWorldGroupService().loadWorldGroupInventoryForPlayer(p);

    serviceContext.getBankService().payInterestToPlayer(e.getPlayer());

    if (WorldHelper.isInWorld(p, Constants.PLUGIN_WORLD_LOBBY)) {
      PlayerHelper.setLobbyItems(p, serviceContext.getItemService(),
          serviceContext.getPluginMetadataService());
    }

    serviceContext.getSchedulerService().runTaskLater(
        () -> ScoreBoardManager.applyToPlayer(e.getPlayer(), serviceContext.getWorldGroupService()),
        10L);
  }

  /**
   * Handles the {@link PlayerLoginEvent} to enforce the server player capacity limit.
   *
   * <p>Denies login with a translated kick message if the number of currently online
   * players has reached or exceeded the configured maximum player count.
   *
   * @param e the event fired when a player attempts to log in
   */
  @EventHandler
  public void login(PlayerLoginEvent e) {
    int maxPlayers = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getMaxPlayers();
    int onlinePlayers = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getOnlinePlayers().size();

    if (onlinePlayers >= maxPlayers) {
      e.disallow(PlayerLoginEvent.Result.KICK_FULL, serviceContext.getTranslationService()
          .get(MessageKey.PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN));
    }
  }

  /**
   * Handles the {@link AsyncPlayerPreLoginEvent} to trigger an interest check for the connecting
   * player.
   *
   * <p>Invokes the bank service to evaluate whether interest is due for the player
   * identified by the event's unique ID, without forcing an immediate payout.
   *
   * @param e the event fired asynchronously before a player completes login
   */
  @EventHandler
  public void checkInterest(@NonNull AsyncPlayerPreLoginEvent e) {
    serviceContext.getBankService().checkInterest(e.getUniqueId(), false);
  }
}