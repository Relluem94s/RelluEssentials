package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.ChatService;
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
class MessageTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ChatService chatService;

  @Mock
  private ServerService serverService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Message message;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    message = new Message();
    message.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getChatService()).thenReturn(chatService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = message.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = message.onCommand(nonPlayerSender, command, "msg", new String[]{"target", "hello"});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsMsgInfoWhenArgsLengthIsZero() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_MSG_INFO)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = message.onCommand(player, command, "msg", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsMsgInfoWhenArgsLengthIsOne() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_MSG_INFO)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = message.onCommand(player, command, "msg", new String[]{"targetOnly"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPlayerOfflineMessageWhenTargetNotFound() {
    when(serverService.getPlayer("OfflinePlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_MSG_PLAYER_OFFLINE)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = message.onCommand(player, command, "msg", new String[]{"OfflinePlayer", "hello"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(chatService, never()).registerReply(player, null);
  }

  @Test
  void onCommandRegistersReplyAndSendsPrivateMessageWhenTargetIsOnline() {
    Player targetPlayer = mock(Player.class);
    when(serverService.getPlayer("OnlinePlayer")).thenReturn(targetPlayer);

    boolean result = message.onCommand(player, command, "msg", new String[]{"OnlinePlayer", "hello", "world"});

    assertTrue(result);
    verify(chatService).registerReply(player, targetPlayer);
    verify(chatService).sendPrivateMessage(player, targetPlayer, new String[]{"OnlinePlayer", "hello", "world"}, 1);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotAuthorized() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "user")).thenReturn(false);

    List<String> result = message.onTabComplete(unauthorizedSender, command, "msg", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonPlayerSender, "user")).thenReturn(true);

    List<String> result = message.onTabComplete(nonPlayerSender, command, "msg", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = message.onTabComplete(player, command, "msg", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenAuthorizedPlayerWithOneArg() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = message.onTabComplete(player, command, "msg", new String[]{"partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsAllOnlinePlayersWhenAuthorizedPlayerWithEmptyArg() {
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(firstOnlinePlayer.getName()).thenReturn("FirstPlayer");
    when(secondOnlinePlayer.getName()).thenReturn("SecondPlayer");

    List<String> result = message.onTabComplete(player, command, "msg", new String[]{""});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("FirstPlayer"));
    assertTrue(result.contains("SecondPlayer"));
  }
}