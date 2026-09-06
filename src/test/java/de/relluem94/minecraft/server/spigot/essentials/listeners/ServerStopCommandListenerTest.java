package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.Collection;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerStopCommandListenerTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private SchedulerService schedulerService;

  @Mock
  private TranslationService translationService;

  @Mock
  private TeleportService teleportService;

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  @Mock
  private ServerCommandEvent serverCommandEvent;

  @Mock
  private PlayerCommandPreprocessEvent playerCommandPreprocessEvent;

  @Mock
  private Player player;

  private ServerStopCommandListener listener;

  private static final String SHUTDOWN_MESSAGE = "Server is shutting down";

  @BeforeEach
  void setUp() {
    listener = new ServerStopCommandListener();
    listener.injectContext(serviceContext);
  }

  @Test
  void injectContextStoresServiceContext() {
    ServerStopCommandListener freshListener = new ServerStopCommandListener();
    freshListener.injectContext(serviceContext);

    assertAll(
        () -> {
          when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
          when(pluginMetadataService.getPlugin()).thenReturn(plugin);
          when(plugin.getServer()).thenReturn(server);
          when(serviceContext.getTranslationService()).thenReturn(translationService);
          when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
          when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
          when(serverCommandEvent.getCommand()).thenReturn("stop");

          freshListener.onServerStopCommand(serverCommandEvent);

          verify(server).broadcastMessage(SHUTDOWN_MESSAGE);
        }
    );
  }

  @Test
  void onServerStopCommandIgnoresNonStopCommand() {
    when(serverCommandEvent.getCommand()).thenReturn("kick");

    listener.onServerStopCommand(serverCommandEvent);

    verifyNoInteractions(serviceContext);
  }

  @Test
  void onServerStopCommandIgnoresCaseInsensitiveNonStopCommand() {
    when(serverCommandEvent.getCommand()).thenReturn("reload");

    listener.onServerStopCommand(serverCommandEvent);

    verifyNoInteractions(serviceContext);
  }

  @Test
  void onServerStopCommandBroadcastsShutdownMessageOnStopCommand() {
    when(serverCommandEvent.getCommand()).thenReturn("stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    listener.onServerStopCommand(serverCommandEvent);

    assertAll(
        () -> verify(server).broadcastMessage(SHUTDOWN_MESSAGE),
        () -> verify(schedulerService, times(2)).runTaskLater(any(), anyLong())
    );
  }

  @Test
  void onServerStopCommandHandlesStopCommandCaseInsensitively() {
    when(serverCommandEvent.getCommand()).thenReturn("STOP");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    listener.onServerStopCommand(serverCommandEvent);

    verify(server).broadcastMessage(SHUTDOWN_MESSAGE);
  }

  @Test
  void onServerStopCommandSchedulesTeleportAndKickTaskWithDelay10() {
    when(serverCommandEvent.getCommand()).thenReturn("stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    listener.onServerStopCommand(serverCommandEvent);

    ArgumentCaptor<Long> delayCaptor = ArgumentCaptor.forClass(Long.class);
    verify(schedulerService, times(2)).runTaskLater(any(), delayCaptor.capture());

    assertAll(
        () -> org.junit.jupiter.api.Assertions.assertEquals(10L, delayCaptor.getAllValues().getFirst()),
        () -> org.junit.jupiter.api.Assertions.assertEquals(20L, delayCaptor.getAllValues().get(1))
    );
  }

  @Test
  void onServerStopCommandSchedulesServerShutdownTaskWithDelay20() {
    when(serverCommandEvent.getCommand()).thenReturn("stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    listener.onServerStopCommand(serverCommandEvent);

    ArgumentCaptor<Long> delayCaptor = ArgumentCaptor.forClass(Long.class);
    verify(schedulerService, times(2)).runTaskLater(any(), delayCaptor.capture());

    org.junit.jupiter.api.Assertions.assertEquals(20L, delayCaptor.getAllValues().get(1));
  }

  @Test
  void onServerStopCommandTeleportAndKickTaskTeleportsAndKicksOnlinePlayers() {
    when(serverCommandEvent.getCommand()).thenReturn("stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getTeleportService()).thenReturn(teleportService);

    Collection<? extends Player> onlinePlayers = List.of(player);
    doReturn(onlinePlayers).when(server).getOnlinePlayers();

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

    listener.onServerStopCommand(serverCommandEvent);

    verify(schedulerService, times(2)).runTaskLater(runnableCaptor.capture(), anyLong());
    runnableCaptor.getAllValues().getFirst().run();

    assertAll(
        () -> verify(teleportService).teleportWorld(eq(player), any(), eq(true)),
        () -> verify(player).kickPlayer(SHUTDOWN_MESSAGE)
    );
  }

  @Test
  void onServerStopCommandShutdownTaskCallsServerShutdown() {
    when(serverCommandEvent.getCommand()).thenReturn("stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

    listener.onServerStopCommand(serverCommandEvent);

    verify(schedulerService, times(2)).runTaskLater(runnableCaptor.capture(), anyLong());
    runnableCaptor.getAllValues().get(1).run();

    verify(server).shutdown();
  }

  @Test
  void onPlayerStopCommandIgnoresNonStopMessage() {
    when(playerCommandPreprocessEvent.getMessage()).thenReturn("/kick");

    listener.onPlayerStopCommand(playerCommandPreprocessEvent);

    verifyNoInteractions(serviceContext);
  }

  @Test
  void onPlayerStopCommandIgnoresNonSlashStopMessage() {
    when(playerCommandPreprocessEvent.getMessage()).thenReturn("stop");

    listener.onPlayerStopCommand(playerCommandPreprocessEvent);

    verifyNoInteractions(serviceContext);
  }

  @Test
  void onPlayerStopCommandCancelsEventOnStopMessage() {
    when(playerCommandPreprocessEvent.getMessage()).thenReturn("/stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    listener.onPlayerStopCommand(playerCommandPreprocessEvent);

    verify(playerCommandPreprocessEvent).setCancelled(true);
  }

  @Test
  void onPlayerStopCommandBroadcastsShutdownMessageOnStopMessage() {
    when(playerCommandPreprocessEvent.getMessage()).thenReturn("/stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    listener.onPlayerStopCommand(playerCommandPreprocessEvent);

    assertAll(
        () -> verify(playerCommandPreprocessEvent).setCancelled(true),
        () -> verify(server).broadcastMessage(SHUTDOWN_MESSAGE),
        () -> verify(schedulerService, times(2)).runTaskLater(any(), anyLong())
    );
  }

  @Test
  void onPlayerStopCommandHandlesStopMessageCaseInsensitively() {
    when(playerCommandPreprocessEvent.getMessage()).thenReturn("/STOP");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    listener.onPlayerStopCommand(playerCommandPreprocessEvent);

    verify(server).broadcastMessage(SHUTDOWN_MESSAGE);
  }

  @Test
  void onPlayerStopCommandSchedulesTeleportAndKickTaskWithDelay10() {
    when(playerCommandPreprocessEvent.getMessage()).thenReturn("/stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    listener.onPlayerStopCommand(playerCommandPreprocessEvent);

    ArgumentCaptor<Long> delayCaptor = ArgumentCaptor.forClass(Long.class);
    verify(schedulerService, times(2)).runTaskLater(any(), delayCaptor.capture());

    assertAll(
        () -> org.junit.jupiter.api.Assertions.assertEquals(10L, delayCaptor.getAllValues().getFirst()),
        () -> org.junit.jupiter.api.Assertions.assertEquals(20L, delayCaptor.getAllValues().get(1))
    );
  }

  @Test
  void onPlayerStopCommandTeleportAndKickTaskTeleportsAndKicksOnlinePlayers() {
    when(playerCommandPreprocessEvent.getMessage()).thenReturn("/stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getTeleportService()).thenReturn(teleportService);

    Collection<? extends Player> onlinePlayers = List.of(player);
    doReturn(onlinePlayers).when(server).getOnlinePlayers();

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

    listener.onPlayerStopCommand(playerCommandPreprocessEvent);

    verify(schedulerService, times(2)).runTaskLater(runnableCaptor.capture(), anyLong());
    runnableCaptor.getAllValues().getFirst().run();

    assertAll(
        () -> verify(teleportService).teleportWorld(eq(player), any(), eq(true)),
        () -> verify(player).kickPlayer(SHUTDOWN_MESSAGE)
    );
  }

  @Test
  void onPlayerStopCommandShutdownTaskCallsServerShutdown() {
    when(playerCommandPreprocessEvent.getMessage()).thenReturn("/stop");
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(SHUTDOWN_MESSAGE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

    listener.onPlayerStopCommand(playerCommandPreprocessEvent);

    verify(schedulerService, times(2)).runTaskLater(runnableCaptor.capture(), anyLong());
    runnableCaptor.getAllValues().get(1).run();

    verify(server).shutdown();
  }

  @Test
  void listenerImplementsListenerConstruct() {
    org.junit.jupiter.api.Assertions.assertInstanceOf(ListenerConstruct.class, listener);
  }
}