package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_LOBBY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.ClipboardService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcDialogueProgressService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetterPlayerQuitTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PlayerService playerService;

  @Mock
  private BuyBackService buyBackService;

  @Mock
  private TranslationService translationService;

  @Mock
  private TeleportService teleportService;

  @Mock
  private NpcDialogueProgressService npcDialogueProgressService;

  @Mock
  private ClipboardService clipboardService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  @Mock
  private Player player;

  @Mock
  private PlayerEntry playerEntry;

  @Mock
  private PlayerQuitEvent playerQuitEvent;

  private BetterPlayerQuit betterPlayerQuit;

  private MockedStatic<ScoreBoardManager> scoreBoardManagerMock;

  private UUID playerUuid;

  @BeforeEach
  void setUp() {
    betterPlayerQuit = new BetterPlayerQuit();
    betterPlayerQuit.injectContext(serviceContext);

    playerUuid = UUID.randomUUID();

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getBuyBackService()).thenReturn(buyBackService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getTeleportService()).thenReturn(teleportService);
    when(serviceContext.getNpcDialogueProgressService()).thenReturn(npcDialogueProgressService);
    when(serviceContext.getClipboardService()).thenReturn(clipboardService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    when(playerQuitEvent.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(player.getCustomName()).thenReturn("TestPlayer");
    when(translationService.get(MessageKey.PLUGIN_EVENT_QUIT_MESSAGE, "TestPlayer")).thenReturn(
        "TestPlayer left the game");

    scoreBoardManagerMock = mockStatic(ScoreBoardManager.class);
  }

  @AfterEach
  void tearDown() {
    scoreBoardManagerMock.close();
  }

  @Test
  void onLeavePlayerNotInSudoShouldPerformAllCleanupOperations() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    assertAll(() -> verify(playerQuitEvent).setQuitMessage(null),
        () -> verify(playerService).savePlayer(player),
        () -> verify(buyBackService).clearBuyBackHistory(player),
        () -> verify(server).broadcastMessage("TestPlayer left the game"),
        () -> verify(teleportService).teleportWorld(player, PLUGIN_WORLD_LOBBY, true),
        () -> verify(npcDialogueProgressService).resetPlayerProgress(playerUuid),
        () -> verify(clipboardService).removeClipboard(player));
    scoreBoardManagerMock.verify(() -> ScoreBoardManager.removePlayer(playerUuid));
  }

  @Test
  void onLeavePlayerInSudoShouldExitSudoBeforeCleanup() {
    SudoManager.sudoers.put(playerUuid, playerEntry);

    when(server.getPlayer(playerUuid)).thenReturn(player);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.commands.Sudo> sudoMock = mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.commands.Sudo.class)) {

      betterPlayerQuit.onLeave(playerQuitEvent);

      assertAll(() -> sudoMock.verify(
              () -> de.relluem94.minecraft.server.spigot.essentials.commands.Sudo.exitSudo(player,
                  serviceContext)),
          () -> verify(playerService).savePlayer(player),
          () -> verify(buyBackService).clearBuyBackHistory(player),
          () -> verify(server).broadcastMessage("TestPlayer left the game"),
          () -> verify(teleportService).teleportWorld(player, PLUGIN_WORLD_LOBBY, true),
          () -> verify(npcDialogueProgressService).resetPlayerProgress(playerUuid),
          () -> verify(clipboardService).removeClipboard(player));
      scoreBoardManagerMock.verify(() -> ScoreBoardManager.removePlayer(playerUuid));
    }
  }

  @Test
  void onLeaveQuitMessageShouldBeSetToNull() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    verify(playerQuitEvent).setQuitMessage(null);
  }

  @Test
  void onLeavePlayerNotInSudoShouldNotCallExitSudo() {
    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.commands.Sudo> sudoMock = mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.commands.Sudo.class)) {

      betterPlayerQuit.onLeave(playerQuitEvent);

      sudoMock.verify(() -> de.relluem94.minecraft.server.spigot.essentials.commands.Sudo.exitSudo(
          Mockito.any(), Mockito.any()), never());
    }
  }

  @Test
  void onLeaveShouldBroadcastQuitMessageWithPlayerCustomName() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    assertAll(
        () -> verify(translationService).get(MessageKey.PLUGIN_EVENT_QUIT_MESSAGE, "TestPlayer"),
        () -> verify(server).broadcastMessage("TestPlayer left the game"));
  }

  @Test
  void onLeaveShouldRemovePlayerFromScoreBoard() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    scoreBoardManagerMock.verify(() -> ScoreBoardManager.removePlayer(playerUuid));
  }

  @Test
  void onLeaveShouldTeleportPlayerToLobbyWorld() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    verify(teleportService).teleportWorld(player, PLUGIN_WORLD_LOBBY, true);
  }

  @Test
  void onLeaveShouldResetNpcDialogueProgress() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    verify(npcDialogueProgressService).resetPlayerProgress(playerUuid);
  }

  @Test
  void onLeaveShouldRemovePlayerClipboard() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    verify(clipboardService).removeClipboard(player);
  }

  @Test
  void onLeaveShouldSavePlayerData() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    verify(playerService).savePlayer(player);
  }

  @Test
  void onLeaveShouldClearBuyBackHistory() {
    betterPlayerQuit.onLeave(playerQuitEvent);

    verify(buyBackService).clearBuyBackHistory(player);
  }

  @Test
  void injectContextShouldStoreServiceContext() {
    BetterPlayerQuit freshListener = new BetterPlayerQuit();
    freshListener.injectContext(serviceContext);

    when(playerQuitEvent.getPlayer()).thenReturn(player);
    when(player.getUniqueId()).thenReturn(playerUuid);
    when(player.getCustomName()).thenReturn("TestPlayer");

    freshListener.onLeave(playerQuitEvent);

    verify(serviceContext).getPlayerService();
  }
}