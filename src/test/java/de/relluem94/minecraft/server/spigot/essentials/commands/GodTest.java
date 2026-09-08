package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GodTest {

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

  private God god;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    god = new God();
    god.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = god.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsTooFewArgumentsWhenConsoleProvideNoArgs() {
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(consoleSender, command, "god", new String[]{});

    assertTrue(result);
    verify(consoleSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTooFewArgumentsWhenCommandBlockProvidesNoArgs() {
    BlockCommandSender blockSender = mock(BlockCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(blockSender, command, "god", new String[]{});

    assertTrue(result);
    verify(blockSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandTogglesGodModeOnWhenConsoleTargetsExistingPlayer() {
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(player);
    when(player.isInvulnerable()).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_GOD_ON)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(consoleSender, command, "god", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(player).setInvulnerable(true);
  }

  @Test
  void onCommandTogglesGodModeOffWhenConsoleTargetsInvulnerablePlayer() {
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(player);
    when(player.isInvulnerable()).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_GOD_OFF)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(consoleSender, command, "god", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(player).setInvulnerable(false);
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenConsoleTargetsUnknownPlayer() {
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(consoleSender, command, "god", new String[]{"UnknownPlayer"});

    assertTrue(result);
    verify(consoleSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenCommandBlockTargetsUnknownPlayer() {
    BlockCommandSender blockSender = mock(BlockCommandSender.class);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(blockSender, command, "god", new String[]{"UnknownPlayer"});

    assertTrue(result);
    verify(blockSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNeitherPlayerNorConsoleNorCommandBlock() {
    CommandSender unknownSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(unknownSender, command, "god", new String[]{});

    assertTrue(result);
    verify(unknownSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotMod() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(player, command, "god", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTooManyArgumentsWhenPlayerProvideArgs() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(player, command, "god", new String[]{"someArg"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandTogglesGodModeOnForPlayerWhenNotInvulnerable() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.isInvulnerable()).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_GOD_ON)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(player, command, "god", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(player).setInvulnerable(true);
  }

  @Test
  void onCommandTogglesGodModeOffForPlayerWhenAlreadyInvulnerable() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.isInvulnerable()).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_GOD_OFF)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = god.onCommand(player, command, "god", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(player).setInvulnerable(false);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotAuthorized() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = god.onTabComplete(unauthorizedSender, command, "god", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsPlayer() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = god.onTabComplete(player, command, "god", new String[]{""});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    when(groupService.isSenderAuthorized(consoleSender, "mod")).thenReturn(true);

    List<String> result = god.onTabComplete(consoleSender, command, "god", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenConsoleIsAuthorizedWithOneArg() {
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(consoleSender, "mod")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = god.onTabComplete(consoleSender, command, "god", new String[]{"partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsAllOnlinePlayersWhenConsoleIsAuthorizedWithEmptyArg() {
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(consoleSender, "mod")).thenReturn(true);
    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(firstOnlinePlayer.getName()).thenReturn("FirstPlayer");
    when(secondOnlinePlayer.getName()).thenReturn("SecondPlayer");

    List<String> result = god.onTabComplete(consoleSender, command, "god", new String[]{""});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("FirstPlayer"));
    assertTrue(result.contains("SecondPlayer"));
  }
}