package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_LOBBY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.Collection;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExitTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ServerService serverService;

  @Mock
  private TeleportService teleportService;

  @Mock
  private SchedulerService schedulerService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  @Mock
  private Command command;

  @Mock
  private Player player;

  @Mock
  private ConsoleCommandSender consoleSender;

  private Exit exit;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    exit = new Exit();
    exit.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getTeleportService()).thenReturn(teleportService);
    lenient().when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    lenient().when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    lenient().when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    lenient().when(plugin.getServer()).thenReturn(server);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = exit.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandBroadcastsShutdownMessageAndSchedulesTasksWhenSenderIsConsole() {
    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = exit.onCommand(consoleSender, command, "exit", new String[]{});

    assertTrue(result);
    verify(serverService).broadcastMessage(TRANSLATED_MESSAGE);
    verify(schedulerService).runTaskLater(any(Runnable.class), eq(10L));
    verify(schedulerService).runTaskLater(any(Runnable.class), eq(20L));
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayerAndNotConsole() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = exit.onCommand(nonPlayerSender, command, "exit", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerLacksUserGroup() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = exit.onCommand(player, command, "exit", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(teleportService, never()).teleportWorld(any(), any(), eq(true));
  }

  @Test
  void onCommandTeleportsAndKicksPlayerWhenPlayerHasUserGroup() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.get(MessageKey.COMMAND_EXIT_KICK_MESSAGE)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = exit.onCommand(player, command, "exit", new String[]{});

    assertTrue(result);
    verify(teleportService).teleportWorld(eq(player), any(), eq(true));
    verify(player).kickPlayer(TRANSLATED_MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyList() {
    List<String> result = exit.onTabComplete(consoleSender, command, "exit", new String[]{});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onCommandTeleportsAndKicksAllOnlinePlayersWhenSenderIsConsole() {
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);
    Collection<? extends Player> onlinePlayers = List.of(firstOnlinePlayer, secondOnlinePlayer);

    when(translationService.get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN)).thenReturn(TRANSLATED_MESSAGE);
    doReturn(onlinePlayers).when(serverService).getOnlinePlayers();

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

    exit.onCommand(consoleSender, command, "exit", new String[]{});

    verify(schedulerService).runTaskLater(runnableCaptor.capture(), eq(10L));
    runnableCaptor.getValue().run();

    verify(teleportService).teleportWorld(eq(firstOnlinePlayer), eq(PLUGIN_WORLD_LOBBY), eq(true));
    verify(firstOnlinePlayer).kickPlayer(TRANSLATED_MESSAGE);
    verify(teleportService).teleportWorld(eq(secondOnlinePlayer), eq(PLUGIN_WORLD_LOBBY), eq(true));
    verify(secondOnlinePlayer).kickPlayer(TRANSLATED_MESSAGE);
  }
}