package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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
class TitleTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ServerService serverService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Title title;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    title = new Title();
    title.injectContext(serviceContext);

    org.mockito.Mockito.lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    org.mockito.Mockito.lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    org.mockito.Mockito.lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = title.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsTooLessArgumentsMessageWhenNoArgsProvided() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = title.onCommand(player, command, "title", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTooLessArgumentsMessageWhenOnlyOneArgProvided() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = title.onCommand(player, command, "title", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenSenderIsNotMod() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = title.onCommand(player, command, "title", new String[]{"TargetPlayer", "Hello"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTargetNotAPlayerMessageWhenTargetNotFound() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = title.onCommand(player, command, "title", new String[]{"UnknownPlayer", "Hello"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTitleToTargetWithOnlyTitleArgument() {
    Player targetPlayer = mock(Player.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);

    boolean result = title.onCommand(player, command, "title", new String[]{"TargetPlayer", "§aHello"});

    assertTrue(result);
    verify(targetPlayer).sendTitle("§aHello", "", 5, 80, 5);
  }

  @Test
  void onCommandSendsTitleAndSubtitleToTargetWhenBothProvided() {
    Player targetPlayer = mock(Player.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);

    boolean result = title.onCommand(player, command, "title", new String[]{"TargetPlayer", "§aHello", "§bWorld"});

    assertTrue(result);
    verify(targetPlayer).sendTitle("§aHello", "§bWorld ", 5, 80, 5);
  }

  @Test
  void onCommandSendsTitleWithMultiWordSubtitleJoined() {
    Player targetPlayer = mock(Player.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);

    boolean result = title.onCommand(player, command, "title", new String[]{"TargetPlayer", "§aHello", "§bBig", "World"});

    assertTrue(result);
    verify(targetPlayer).sendTitle("§aHello", "§bBig World ", 5, 80, 5);
  }

  @Test
  void onCommandDoesNotSendTitleToSenderWhenTargetNotFound() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    title.onCommand(player, command, "title", new String[]{"UnknownPlayer", "Hello"});

    verify(player, never()).sendTitle(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(),
        org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = title.onTabComplete(unauthorizedSender, command, "title", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(consoleSender, "mod")).thenReturn(true);

    List<String> result = title.onTabComplete(consoleSender, command, "title", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = title.onTabComplete(player, command, "title", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenModWithOneArg() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = title.onTabComplete(player, command, "title", new String[]{"partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsAllOnlinePlayersWhenModWithEmptyArg() {
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(firstOnlinePlayer.getName()).thenReturn("FirstPlayer");
    when(secondOnlinePlayer.getName()).thenReturn("SecondPlayer");

    List<String> result = title.onTabComplete(player, command, "title", new String[]{""});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("FirstPlayer"));
    assertTrue(result.contains("SecondPlayer"));
  }
}