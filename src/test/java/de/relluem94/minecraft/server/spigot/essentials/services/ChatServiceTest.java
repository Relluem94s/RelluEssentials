package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ReplyRegistry;
import java.util.Collection;
import java.util.List;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private ReplyRegistry replyRegistry;

  @Mock
  private GroupService groupService;

  @Mock
  private TranslationService translationService;

  @Mock
  private Player senderPlayer;

  @Mock
  private Player targetPlayer;

  @Mock
  private ConsoleCommandSender consoleCommandSender;

  private ChatService chatService;

  @BeforeEach
  void setUp() {
    chatService = new ChatService(serviceContext, replyRegistry);
  }

  @Test
  void sendMessageSendsToPlayerWhenSenderIsPlayer() {
    chatService.sendMessage(senderPlayer, "hello");

    verify(senderPlayer).sendMessage("hello");
  }

  @Test
  void sendMessageSendsToConsoleWhenSenderIsNotPlayer() {
    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getConsoleSender).thenReturn(consoleCommandSender);

      chatService.sendMessage(consoleCommandSender, "hello");

      verify(consoleCommandSender).sendMessage(" hello");
    }
  }

  @Test
  void consoleSendMessageSendsFormattedMessage() {
    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getConsoleSender).thenReturn(consoleCommandSender);

      chatService.consoleSendMessage("INFO", "server started");

      verify(consoleCommandSender).sendMessage("INFO server started");
    }
  }

  @Test
  void consoleSendMessageWithRepeatSendsMessageCorrectNumberOfTimes() {
    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getConsoleSender).thenReturn(consoleCommandSender);

      chatService.consoleSendMessage("INFO", "repeated", 2);

      verify(consoleCommandSender, Mockito.times(3)).sendMessage("INFO repeated");
    }
  }

  @Test
  void consoleSendMessageWithRepeatZeroSendsOnce() {
    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getConsoleSender).thenReturn(consoleCommandSender);

      chatService.consoleSendMessage("INFO", "once", 0);

      verify(consoleCommandSender, Mockito.times(1)).sendMessage("INFO once");
    }
  }

  @Test
  void sendMessageInChannelWithPlayerSenderSendsToAuthorizedPlayers() {
    GroupEntry group = new GroupEntry(1, "admin", "[A] ");
    Player authorizedPlayer = mock(Player.class);
    Player unauthorizedPlayer = mock(Player.class);
    Collection<? extends Player> onlinePlayers = List.of(authorizedPlayer, unauthorizedPlayer);

    when(senderPlayer.getCustomName()).thenReturn("SenderName");
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(authorizedPlayer, "admin")).thenReturn(true);
    when(groupService.isSenderAuthorized(unauthorizedPlayer, "admin")).thenReturn(false);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getOnlinePlayers).thenReturn(onlinePlayers);

      chatService.sendMessageInChannel("!admin hello world", senderPlayer, "!admin", group);

      verify(authorizedPlayer).sendMessage(anyString());
      verify(unauthorizedPlayer, never()).sendMessage(anyString());
    }
  }

  @Test
  void sendMessageInChannelWithPlayerSenderStripsChannelFromMessage() {
    GroupEntry group = new GroupEntry(1, "admin", "[A] ");
    Collection<? extends Player> onlinePlayers = List.of(senderPlayer);

    when(senderPlayer.getCustomName()).thenReturn("SenderName");
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(senderPlayer, "admin")).thenReturn(true);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getOnlinePlayers).thenReturn(onlinePlayers);

      chatService.sendMessageInChannel("!admin hello world", senderPlayer, "!admin", group);

      ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
      verify(senderPlayer).sendMessage(messageCaptor.capture());
      String sentMessage = messageCaptor.getValue();

      assertAll(() -> assertTrue(sentMessage.contains("SenderName")),
          () -> assertTrue(sentMessage.contains("[A] ")),
          () -> assertFalse(sentMessage.contains("!admin")));
    }
  }

  @Test
  void sendMessageInChannelWithSenderNameSendsToAuthorizedPlayers() {
    GroupEntry group = new GroupEntry(1, "mod", "[M] ");
    Player authorizedPlayer = mock(Player.class);
    Collection<? extends Player> onlinePlayers = List.of(authorizedPlayer);

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(authorizedPlayer, "mod")).thenReturn(true);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getOnlinePlayers).thenReturn(onlinePlayers);

      chatService.sendMessageInChannel("!mod hello", "ConsoleSender", "!mod", group);

      verify(authorizedPlayer).sendMessage(anyString());
    }
  }

  @Test
  void sendMessageInChannelWithSenderNameStripsChannelFromMessage() {
    GroupEntry group = new GroupEntry(1, "mod", "[M] ");
    Player authorizedPlayer = mock(Player.class);
    Collection<? extends Player> onlinePlayers = List.of(authorizedPlayer);

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(authorizedPlayer, "mod")).thenReturn(true);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getOnlinePlayers).thenReturn(onlinePlayers);

      chatService.sendMessageInChannel("!mod hello", "ConsoleSender", "!mod", group);

      ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
      verify(authorizedPlayer).sendMessage(messageCaptor.capture());
      String sentMessage = messageCaptor.getValue();

      assertAll(() -> assertTrue(sentMessage.contains("ConsoleSender")),
          () -> assertTrue(sentMessage.contains("[M] ")),
          () -> assertFalse(sentMessage.contains("!mod")));
    }
  }

  @Test
  void sendMessageInActionBarSendsActionBarMessage() {
    org.bukkit.entity.Player.Spigot spigot = mock(org.bukkit.entity.Player.Spigot.class);
    when(senderPlayer.spigot()).thenReturn(spigot);

    chatService.sendMessageInActionBar(senderPlayer, "action bar text");

    ArgumentCaptor<TextComponent> componentCaptor = ArgumentCaptor.forClass(TextComponent.class);
    verify(spigot).sendMessage(Mockito.eq(ChatMessageType.ACTION_BAR), componentCaptor.capture());
    assertEquals("action bar text", componentCaptor.getValue().getText());
  }

  @Test
  void registerReplyDelegatesToRegistry() {
    chatService.registerReply(senderPlayer, targetPlayer);

    verify(replyRegistry).register(senderPlayer, targetPlayer);
  }

  @Test
  void hasReplyTargetReturnsTrueWhenRegistryContainsTarget() {
    when(replyRegistry.hasReplyTarget(senderPlayer)).thenReturn(true);

    boolean result = chatService.hasReplyTarget(senderPlayer);

    assertTrue(result);
  }

  @Test
  void hasReplyTargetReturnsFalseWhenRegistryHasNoTarget() {
    when(replyRegistry.hasReplyTarget(senderPlayer)).thenReturn(false);

    boolean result = chatService.hasReplyTarget(senderPlayer);

    assertFalse(result);
  }

  @Test
  void findReplyTargetReturnsTargetFromRegistry() {
    when(replyRegistry.findReplyTarget(senderPlayer)).thenReturn(targetPlayer);

    Player result = chatService.findReplyTarget(senderPlayer);

    assertEquals(targetPlayer, result);
  }

  @Test
  void findReplyTargetReturnsNullWhenNoTargetRegistered() {
    when(replyRegistry.findReplyTarget(senderPlayer)).thenReturn(null);

    Player result = chatService.findReplyTarget(senderPlayer);

    assertNull(result);
  }

  @Test
  void sendPrivateMessageSendsNotPlayerMessageWhenSenderIsConsole() {
    String permissionMissingMessage = "Not a player";
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(
        permissionMissingMessage);

    chatService.sendPrivateMessage(consoleCommandSender, targetPlayer, new String[]{"hello"}, 0);

    verify(consoleCommandSender).sendMessage(permissionMissingMessage);
  }

  @Test
  void sendPrivateMessageSendsToTargetAndSenderWhenAuthorized() {
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(senderPlayer, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(senderPlayer, "vip")).thenReturn(false);
    when(senderPlayer.getCustomName()).thenReturn("Sender");
    when(targetPlayer.getCustomName()).thenReturn("Target");

    chatService.sendPrivateMessage(senderPlayer, targetPlayer, new String[]{"msg", "hello"}, 1);

    assertAll(() -> verify(targetPlayer).sendMessage(anyString()),
        () -> verify(senderPlayer).sendMessage(anyString()));
  }

  @Test
  void sendPrivateMessageFormatsTargetMessageWithSenderName() {
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(senderPlayer, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(senderPlayer, "vip")).thenReturn(false);
    when(senderPlayer.getCustomName()).thenReturn("Sender");
    when(targetPlayer.getCustomName()).thenReturn("Target");

    chatService.sendPrivateMessage(senderPlayer, targetPlayer, new String[]{"hello"}, 0);

    ArgumentCaptor<String> targetCaptor = ArgumentCaptor.forClass(String.class);
    verify(targetPlayer).sendMessage(targetCaptor.capture());
    assertTrue(targetCaptor.getValue().contains("Sender"));
  }

  @Test
  void sendPrivateMessageFormatsSenderMessageWithTargetName() {
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(senderPlayer, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(senderPlayer, "vip")).thenReturn(false);
    when(senderPlayer.getCustomName()).thenReturn("Sender");
    when(targetPlayer.getCustomName()).thenReturn("Target");

    chatService.sendPrivateMessage(senderPlayer, targetPlayer, new String[]{"hello"}, 0);

    ArgumentCaptor<String> senderCaptor = ArgumentCaptor.forClass(String.class);
    verify(senderPlayer).sendMessage(senderCaptor.capture());
    assertTrue(senderCaptor.getValue().contains("Target"));
  }

  @Test
  void sendPrivateMessageSendsPermissionMissingWhenNotAuthorized() {
    String permissionMissingMessage = "Permission missing";
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(senderPlayer, "user")).thenReturn(false);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(
        permissionMissingMessage);

    chatService.sendPrivateMessage(senderPlayer, targetPlayer, new String[]{"hello"}, 0);

    assertAll(() -> verify(senderPlayer).sendMessage(permissionMissingMessage),
        () -> verify(targetPlayer, never()).sendMessage(anyString()));
  }

  @Test
  void sendPrivateMessageAppliesColorAndSymbolsForVipSender() {
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(senderPlayer, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(senderPlayer, "vip")).thenReturn(true);
    when(senderPlayer.getCustomName()).thenReturn("VipSender");
    when(targetPlayer.getCustomName()).thenReturn("Target");

    chatService.sendPrivateMessage(senderPlayer, targetPlayer, new String[]{"hello"}, 0);

    verify(targetPlayer).sendMessage(anyString());
    verify(senderPlayer).sendMessage(anyString());
  }


  @Test
  void sendMessageInChannelWithSenderNameDoesNotSendToUnauthorizedPlayers() {
    GroupEntry group = new GroupEntry(1, "mod", "[M] ");
    Player authorizedPlayer = mock(Player.class);
    Player unauthorizedPlayer = mock(Player.class);
    Collection<? extends Player> onlinePlayers = List.of(authorizedPlayer, unauthorizedPlayer);

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(authorizedPlayer, "mod")).thenReturn(true);
    when(groupService.isSenderAuthorized(unauthorizedPlayer, "mod")).thenReturn(false);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getOnlinePlayers).thenReturn(onlinePlayers);

      chatService.sendMessageInChannel("!mod hello", "ConsoleSender", "!mod", group);

      verify(authorizedPlayer).sendMessage(anyString());
      verify(unauthorizedPlayer, never()).sendMessage(anyString());
    }
  }
}