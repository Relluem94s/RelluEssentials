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
class SuicideTest {

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

  private Suicide suicide;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    suicide = new Suicide();
    suicide.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = suicide.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayerAndNotConsoleAndNotCmdBlock() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = suicide.onCommand(nonPlayerSender, command, "suicide", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerLacksUserPermission() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = suicide.onCommand(player, command, "suicide", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandKillsPlayerWhenNoArgsAndHasUserPermission() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(player.getCustomName()).thenReturn("TestPlayer");
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUICIDE, "TestPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = suicide.onCommand(player, command, "suicide", new String[]{});

    assertTrue(result);
    verify(player).setHealth(0);
    verify(serverService).broadcastMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenTargetNotFoundAndSenderIsPlayer() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = suicide.onCommand(player, command, "suicide", new String[]{"UnknownPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerLacksModPermissionForTarget() {
    Player targetPlayer = mock(Player.class);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = suicide.onCommand(player, command, "suicide", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(targetPlayer, never()).setHealth(0);
  }

  @Test
  void onCommandKillsTargetWhenSenderHasModPermission() {
    Player targetPlayer = mock(Player.class);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(targetPlayer.getCustomName()).thenReturn("TargetPlayer");
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUICIDE, "TargetPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = suicide.onCommand(player, command, "suicide", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(targetPlayer).setHealth(0);
    verify(serverService).broadcastMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = suicide.onTabComplete(unauthorizedSender, command, "suicide", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = suicide.onTabComplete(player, command, "suicide", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenSenderIsModWithOneArg() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = suicide.onTabComplete(player, command, "suicide", new String[]{"partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsAllOnlinePlayerNamesWhenSenderIsModWithEmptyArg() {
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(firstOnlinePlayer.getName()).thenReturn("FirstPlayer");
    when(secondOnlinePlayer.getName()).thenReturn("SecondPlayer");

    List<String> result = suicide.onTabComplete(player, command, "suicide", new String[]{""});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("FirstPlayer"));
    assertTrue(result.contains("SecondPlayer"));
  }


  @Test
  void onCommandSendsToLessArgumentsWhenConsoleProvideNoArgs() {
    CommandSender consoleSender = mock(org.bukkit.command.ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = suicide.onCommand(consoleSender, command, "suicide", new String[]{});

    assertTrue(result);
    verify(consoleSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenConsoleProvideUnknownPlayerName() {
    CommandSender consoleSender = mock(org.bukkit.command.ConsoleCommandSender.class);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = suicide.onCommand(consoleSender, command, "suicide", new String[]{"UnknownPlayer"});

    assertTrue(result);
    verify(consoleSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandKillsTargetWhenConsoleProvideValidPlayerName() {
    CommandSender consoleSender = mock(org.bukkit.command.ConsoleCommandSender.class);
    Player targetPlayer = mock(Player.class);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(targetPlayer.getCustomName()).thenReturn("TargetPlayer");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "TargetPlayer")).thenReturn(TRANSLATED_MESSAGE);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUICIDE, "TargetPlayer")).thenReturn("suicide-message");

    boolean result = suicide.onCommand(consoleSender, command, "suicide", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(targetPlayer).setHealth(0);
    verify(serverService).broadcastMessage("suicide-message");
  }

  @Test
  void onCommandKillsTargetWhenCmdBlockProvidesValidPlayerName() {
    CommandSender cmdBlockSender = mock(org.bukkit.command.BlockCommandSender.class);
    Player targetPlayer = mock(Player.class);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(targetPlayer.getCustomName()).thenReturn("TargetPlayer");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "TargetPlayer")).thenReturn(TRANSLATED_MESSAGE);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUICIDE, "TargetPlayer")).thenReturn("suicide-message");

    boolean result = suicide.onCommand(cmdBlockSender, command, "suicide", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(targetPlayer).setHealth(0);
    verify(serverService).broadcastMessage("suicide-message");
  }
}