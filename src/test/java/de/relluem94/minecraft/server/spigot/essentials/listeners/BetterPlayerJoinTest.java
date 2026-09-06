package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginInformationService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import java.util.List;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetterPlayerJoinTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PlayerService playerService;

  @Mock
  private GroupService groupService;

  @Mock
  private TranslationService translationService;

  @Mock
  private PluginInformationService pluginInformationService;

  @Mock
  private WorldGroupService worldGroupService;

  @Mock
  private BankService bankService;

  @Mock
  private ItemService itemService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private SchedulerService schedulerService;

  @Mock
  private PlayerJoinEvent playerJoinEvent;

  @Mock
  private PlayerLoginEvent playerLoginEvent;

  @Mock
  private AsyncPlayerPreLoginEvent asyncPlayerPreLoginEvent;

  @Mock
  private Player player;

  @Mock
  private Server server;

  @Mock
  private PluginInformationEntry pluginInformationEntry;

  @Mock
  private org.bukkit.plugin.Plugin plugin;

  private BetterPlayerJoin betterPlayerJoin;

  private final UUID playerUuid = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    betterPlayerJoin = new BetterPlayerJoin();
    betterPlayerJoin.injectContext(serviceContext);
  }

  @Test
  void onJoinRegistersNewPlayerWhenNotFound() {
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getPluginInformationService()).thenReturn(pluginInformationService);
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(serviceContext.getBankService()).thenReturn(bankService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    when(playerJoinEvent.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(player.getName()).thenReturn("TestPlayer");
    when(player.getDisplayName()).thenReturn("TestPlayer");
    when(player.getCustomName()).thenReturn("TestPlayer");

    when(playerService.getPlayerByUuid(playerUuid.toString())).thenReturn(null).thenReturn(new PlayerEntry());
    when(groupService.resolveGroupWithFallback("user")).thenReturn(null);
    when(translationService.get(MessageKey.PLUGIN_EVENT_FIRST_JOIN_MESSAGE)).thenReturn("Welcome!");
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE), any())).thenReturn("joined!");

    when(pluginInformationService.getPluginInformation()).thenReturn(pluginInformationEntry);
    when(pluginInformationEntry.getTabHeader()).thenReturn("header");
    when(pluginInformationEntry.getTabFooter()).thenReturn("footer");

    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.isInWorld(player, Constants.PLUGIN_WORLD_LOBBY)).thenReturn(false);

      betterPlayerJoin.onJoin(playerJoinEvent);
    }

    verify(playerJoinEvent).setJoinMessage(null);
    verify(playerService).registerNewPlayer(any(PlayerEntry.class));
    verify(player).sendMessage("Welcome!");
    verify(playerService).putPlayerEntry(eq(playerUuid), any(PlayerEntry.class));
    verify(player).setPlayerListHeader("header");
    verify(player).setPlayerListFooter("footer");
    verify(playerService).setFlying(player);
    verify(playerService).setAfk(player, true);
    verify(server).broadcastMessage("joined!");
    verify(worldGroupService).loadWorldGroupInventoryForPlayer(player);
    verify(bankService).payInterestToPlayer(player);
    verify(schedulerService).runTaskLater(any(), eq(10L));
  }

  @Test
  void onJoinUpdatesExistingPlayerNameWhenNameIsNull() {
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getPluginInformationService()).thenReturn(pluginInformationService);
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(serviceContext.getBankService()).thenReturn(bankService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    PlayerEntry existingEntry = new PlayerEntry();
    existingEntry.setName(null);

    PlayerEntry reloadedEntry = new PlayerEntry();
    reloadedEntry.setName("TestPlayer");

    when(playerJoinEvent.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(player.getName()).thenReturn("TestPlayer");
    when(player.getCustomName()).thenReturn("TestPlayer");

    when(playerService.getPlayerByUuid(playerUuid.toString()))
        .thenReturn(existingEntry)
        .thenReturn(reloadedEntry);

    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE), any())).thenReturn("joined!");

    when(pluginInformationService.getPluginInformation()).thenReturn(pluginInformationEntry);
    when(pluginInformationEntry.getTabHeader()).thenReturn("header");
    when(pluginInformationEntry.getTabFooter()).thenReturn("footer");

    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.isInWorld(player, Constants.PLUGIN_WORLD_LOBBY)).thenReturn(false);

      betterPlayerJoin.onJoin(playerJoinEvent);
    }

    verify(playerService).savePlayer(existingEntry);
    verify(playerService, times(2)).getPlayerByUuid(playerUuid.toString());
  }

  @Test
  void onJoinDoesNotUpdateExistingPlayerNameWhenNameIsPresent() {
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getPluginInformationService()).thenReturn(pluginInformationService);
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(serviceContext.getBankService()).thenReturn(bankService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    PlayerEntry existingEntry = new PlayerEntry();
    existingEntry.setName("TestPlayer");

    when(playerJoinEvent.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(player.getCustomName()).thenReturn("TestPlayer");

    when(playerService.getPlayerByUuid(playerUuid.toString())).thenReturn(existingEntry);
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE), any())).thenReturn("joined!");

    when(pluginInformationService.getPluginInformation()).thenReturn(pluginInformationEntry);
    when(pluginInformationEntry.getTabHeader()).thenReturn("header");
    when(pluginInformationEntry.getTabFooter()).thenReturn("footer");

    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.isInWorld(player, Constants.PLUGIN_WORLD_LOBBY)).thenReturn(false);

      betterPlayerJoin.onJoin(playerJoinEvent);
    }

    verify(playerService, never()).savePlayer(any(PlayerEntry.class));
    verify(playerService, times(1)).getPlayerByUuid(playerUuid.toString());
  }

  @Test
  void onJoinAssignsLobbyItemsWhenPlayerIsInLobbyWorld() {
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getPluginInformationService()).thenReturn(pluginInformationService);
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(serviceContext.getBankService()).thenReturn(bankService);
    when(serviceContext.getItemService()).thenReturn(itemService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    PlayerEntry existingEntry = new PlayerEntry();
    existingEntry.setName("TestPlayer");

    when(playerJoinEvent.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);

    when(playerService.getPlayerByUuid(playerUuid.toString())).thenReturn(existingEntry);
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE), any())).thenReturn("joined!");

    when(pluginInformationService.getPluginInformation()).thenReturn(pluginInformationEntry);
    when(pluginInformationEntry.getTabHeader()).thenReturn("header");
    when(pluginInformationEntry.getTabFooter()).thenReturn("footer");

    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class);
        MockedStatic<PlayerHelper> playerHelperMock = mockStatic(PlayerHelper.class)) {

      worldHelperMock.when(() -> WorldHelper.isInWorld(player, Constants.PLUGIN_WORLD_LOBBY)).thenReturn(true);

      betterPlayerJoin.onJoin(playerJoinEvent);

      playerHelperMock.verify(() -> PlayerHelper.setLobbyItems(player, itemService, pluginMetadataService));
    }
  }

  @Test
  void onJoinSchedulesScoreboardApplication() {
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getPluginInformationService()).thenReturn(pluginInformationService);
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(serviceContext.getBankService()).thenReturn(bankService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    PlayerEntry existingEntry = new PlayerEntry();
    existingEntry.setName("TestPlayer");

    when(playerJoinEvent.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(player.getCustomName()).thenReturn("TestPlayer");

    when(playerService.getPlayerByUuid(playerUuid.toString())).thenReturn(existingEntry);
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE), any())).thenReturn("joined!");

    when(pluginInformationService.getPluginInformation()).thenReturn(pluginInformationEntry);
    when(pluginInformationEntry.getTabHeader()).thenReturn("header");
    when(pluginInformationEntry.getTabFooter()).thenReturn("footer");

    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.isInWorld(player, Constants.PLUGIN_WORLD_LOBBY)).thenReturn(false);

      betterPlayerJoin.onJoin(playerJoinEvent);
    }

    verify(schedulerService).runTaskLater(any(Runnable.class), eq(10L));
  }

  @Test
  void loginAllowsPlayerWhenBelowMaxCapacity() {
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getMaxPlayers()).thenReturn(20);
    doReturn(List.of()).when(server).getOnlinePlayers();

    betterPlayerJoin.login(playerLoginEvent);

    verify(playerLoginEvent, never()).disallow(any(), any());
  }

  @Test
  void loginDeniesPlayerWhenAtMaxCapacity() {
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getMaxPlayers()).thenReturn(1);

    Player onlinePlayer = mock(Player.class);
    doReturn(List.of(onlinePlayer)).when(server).getOnlinePlayers();
    when(translationService.get(MessageKey.PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN)).thenReturn("Server is full!");

    betterPlayerJoin.login(playerLoginEvent);

    verify(playerLoginEvent).disallow(PlayerLoginEvent.Result.KICK_FULL, "Server is full!");
  }

  @Test
  void loginDeniesPlayerWhenExceedingMaxCapacity() {
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getMaxPlayers()).thenReturn(1);

    Player onlinePlayer1 = mock(Player.class);
    Player onlinePlayer2 = mock(Player.class);
    doReturn(List.of(onlinePlayer1, onlinePlayer2)).when(server).getOnlinePlayers();
    when(translationService.get(MessageKey.PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN)).thenReturn("Server is full!");

    betterPlayerJoin.login(playerLoginEvent);

    verify(playerLoginEvent).disallow(PlayerLoginEvent.Result.KICK_FULL, "Server is full!");
  }

  @Test
  void checkInterestInvokesBankServiceWithPlayerUuid() {
    when(serviceContext.getBankService()).thenReturn(bankService);
    when(asyncPlayerPreLoginEvent.getUniqueId()).thenReturn(playerUuid);

    betterPlayerJoin.checkInterest(asyncPlayerPreLoginEvent);

    verify(bankService).checkInterest(playerUuid, false);
  }

  @Test
  void injectContextStoresServiceContext() {
    ServiceContext newContext = mock(ServiceContext.class);
    betterPlayerJoin.injectContext(newContext);

    when(newContext.getBankService()).thenReturn(bankService);
    when(newContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getMaxPlayers()).thenReturn(20);
    doReturn(List.of()).when(server).getOnlinePlayers();
    when(asyncPlayerPreLoginEvent.getUniqueId()).thenReturn(playerUuid);

    betterPlayerJoin.checkInterest(asyncPlayerPreLoginEvent);
    betterPlayerJoin.login(playerLoginEvent);

    verify(newContext, atLeastOnce()).getBankService();
    verify(newContext, atLeastOnce()).getPluginMetadataService();
  }

  @Test
  void onJoinScheduledTaskAppliesScoreboardToPlayer() {
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getPluginInformationService()).thenReturn(pluginInformationService);
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(serviceContext.getBankService()).thenReturn(bankService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    PlayerEntry existingEntry = new PlayerEntry();
    existingEntry.setName("TestPlayer");

    when(playerJoinEvent.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(player.getCustomName()).thenReturn("TestPlayer");

    when(playerService.getPlayerByUuid(playerUuid.toString())).thenReturn(existingEntry);
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_JOIN_MESSAGE), any())).thenReturn("joined!");

    when(pluginInformationService.getPluginInformation()).thenReturn(pluginInformationEntry);
    when(pluginInformationEntry.getTabHeader()).thenReturn("header");
    when(pluginInformationEntry.getTabFooter()).thenReturn("footer");

    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class);
        MockedStatic<ScoreBoardManager> scoreBoardManagerMock = mockStatic(ScoreBoardManager.class)) {

      worldHelperMock.when(() -> WorldHelper.isInWorld(player, Constants.PLUGIN_WORLD_LOBBY)).thenReturn(false);

      betterPlayerJoin.onJoin(playerJoinEvent);

      verify(schedulerService).runTaskLater(runnableCaptor.capture(), eq(10L));
      runnableCaptor.getValue().run();

      scoreBoardManagerMock.verify(() -> ScoreBoardManager.applyToPlayer(player, worldGroupService));
    }
  }
}