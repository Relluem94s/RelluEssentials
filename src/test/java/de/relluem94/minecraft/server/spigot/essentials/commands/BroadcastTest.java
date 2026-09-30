package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BroadcastTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private GroupService groupService;

  @Mock
  private TranslationService translationService;

  @Mock
  private ServerService serverService;

  @Mock
  private CommandSender commandSender;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Broadcast broadcast;

  @BeforeEach
  void setUp() {
    broadcast = new Broadcast();
    broadcast.injectContext(serviceContext);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void onCommandNoArgsSendsInfoMessage() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_BROADCAST_INFO)).thenReturn("info");

    boolean result = broadcast.onCommand(commandSender, command, "broadcast", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(commandSender).sendMessage("info"),
        () -> verify(groupService, never()).isSenderAuthorized(any(), anyString())
    );
  }

  @Test
  void onCommandUnauthorizedSenderSendsPermissionMissingMessage() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn("no permission");

    boolean result = broadcast.onCommand(commandSender, command, "broadcast", new String[]{"hello"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(commandSender).sendMessage("no permission"),
        () -> verify(serverService, never()).broadcastMessage(anyString())
    );
  }

  @Test
  void onCommandChatSubCommandBroadcastsChatMessage() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);

    boolean result = broadcast.onCommand(commandSender, command, "broadcast", new String[]{"chat", "hello", "world"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(serverService).broadcastMessage(anyString()),
        () -> verify(serverService, never()).getOnlinePlayers()
    );
  }

  @Test
  void onCommandTitleSubCommandBroadcastsTitleToAllPlayers() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);
    doReturn(List.of(player)).when(serverService).getOnlinePlayers();

    boolean result = broadcast.onCommand(commandSender, command, "broadcast", new String[]{"title", "hello"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(serverService).getOnlinePlayers(),
        () -> verify(player).sendTitle(anyString(), anyString(), eq(5), eq(80), eq(5)),
        () -> verify(serverService, never()).broadcastMessage(anyString())
    );
  }

  @Test
  void onCommandNoSubCommandDefaultsToChatBroadcast() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);

    boolean result = broadcast.onCommand(commandSender, command, "broadcast", new String[]{"hello", "world"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(serverService).broadcastMessage(anyString()),
        () -> verify(serverService, never()).getOnlinePlayers()
    );
  }

  @Test
  void onCommandChatSubCommandCaseInsensitive() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);

    boolean result = broadcast.onCommand(commandSender, command, "broadcast", new String[]{"CHAT", "hello"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(serverService).broadcastMessage(anyString())
    );
  }

  @Test
  void onCommandTitleSubCommandCaseInsensitive() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);
    doReturn(List.of(player)).when(serverService).getOnlinePlayers();


    boolean result = broadcast.onCommand(commandSender, command, "broadcast", new String[]{"TITLE", "hello"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendTitle(anyString(), anyString(), eq(5), eq(80), eq(5))
    );
  }

  @Test
  void onTabCompleteUnauthorizedSenderReturnsEmptyList() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(false);

    List<String> result = broadcast.onTabComplete(commandSender, command, "broadcast", new String[]{"c"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteMoreThanOneArgReturnsEmptyList() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);

    List<String> result = broadcast.onTabComplete(commandSender, command, "broadcast", new String[]{"chat", "hello"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteAuthorizedSenderWithOneArgReturnsSubCommands() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);

    List<String> result = broadcast.onTabComplete(commandSender, command, "broadcast", new String[]{"c"});

    assertNotNull(result);
    assertAll(
        () -> assertTrue(result.contains("chat")),
        () -> assertTrue(result.contains("title"))
    );
  }

  @Test
  void getCommandsReturnsAllBroadcastSubCommands() {
    CommandsEnum[] commands = broadcast.getCommands();

    assertAll(
        () -> assertNotNull(commands),
        () -> assertEquals(2, commands.length),
        () -> assertEquals("title", commands[0].getName()),
        () -> assertEquals("chat", commands[1].getName())
    );
  }

  @Test
  void commandsEnumTitleHasCorrectName() {
    Broadcast.Commands titleCommand = Broadcast.Commands.TITLE;

    assertAll(
        () -> assertEquals("title", titleCommand.getName()),
        () -> assertNotNull(titleCommand.getSubCommands()),
        () -> assertEquals(0, titleCommand.getSubCommands().length)
    );
  }

  @Test
  void commandsEnumChatHasCorrectName() {
    Broadcast.Commands chatCommand = Broadcast.Commands.CHAT;

    assertAll(
        () -> assertEquals("chat", chatCommand.getName()),
        () -> assertNotNull(chatCommand.getSubCommands()),
        () -> assertEquals(0, chatCommand.getSubCommands().length)
    );
  }

  @Test
  void onCommandTitleBroadcastsSendsToMultiplePlayers() {
    Player secondPlayer = org.mockito.Mockito.mock(Player.class);
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);
    doReturn(List.of(player, secondPlayer)).when(serverService).getOnlinePlayers();

    boolean result = broadcast.onCommand(commandSender, command, "broadcast", new String[]{"title", "hello"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendTitle(anyString(), anyString(), eq(5), eq(80), eq(5)),
        () -> verify(secondPlayer).sendTitle(anyString(), anyString(), eq(5), eq(80), eq(5))
    );
  }

  @Test
  void onCommandChatBroadcastsWithPrefixAndMessage() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);

    broadcast.onCommand(commandSender, command, "broadcast", new String[]{"chat", "testMessage"});

    verify(serverService).broadcastMessage(anyString());
  }
}