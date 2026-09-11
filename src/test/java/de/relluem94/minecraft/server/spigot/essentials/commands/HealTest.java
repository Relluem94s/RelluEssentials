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
class HealTest {

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

  private Heal heal;

  private static final String TRANSLATED_MESSAGE = "translated-message";
  private static final double DEFAULT_MAX_HEALTH = 20.0;

  @BeforeEach
  void setUp() {
    heal = new Heal() {
      @Override
      protected double getMaxHealth(Player p) {
        return DEFAULT_MAX_HEALTH;
      }
    };
    heal.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = heal.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsTooFewArgumentsWhenConsoleProvideNoArgs() {
    ConsoleCommandSender console = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(console, command, "heal", new String[]{});

    assertTrue(result);
    verify(console).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandHealsTargetWhenConsoleProvideValidPlayerName() {
    ConsoleCommandSender console = mock(ConsoleCommandSender.class);

    when(serverService.getPlayer("TargetPlayer")).thenReturn(player);
    when(translationService.getWithPrefix(MessageKey.COMMAND_HEAL)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(console, command, "heal", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).setHealth(DEFAULT_MAX_HEALTH);
    verify(player).setFoodLevel(20);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }


  @Test
  void onCommandSendsTargetNotAPlayerWhenConsoleProvideUnknownPlayerName() {
    ConsoleCommandSender console = mock(ConsoleCommandSender.class);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(console, command, "heal", new String[]{"UnknownPlayer"});

    assertTrue(result);
    verify(console).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNeitherPlayerNorConsoleNorCmdBlock() {
    CommandSender unknownSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(unknownSender, command, "heal", new String[]{});

    assertTrue(result);
    verify(unknownSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotMod() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(player, command, "heal", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTooManyArgumentsWhenPlayerProvidesArgs() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(player, command, "heal", new String[]{"extraArg"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandHealsPlayerWhenPlayerIsModeAndNoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_HEAL)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(player, command, "heal", new String[]{});

    assertTrue(result);
    verify(player).setHealth(DEFAULT_MAX_HEALTH);
    verify(player).setFoodLevel(20);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = heal.onTabComplete(unauthorizedSender, command, "heal", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsPlayer() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = heal.onTabComplete(player, command, "heal", new String[]{});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    ConsoleCommandSender console = mock(ConsoleCommandSender.class);
    when(groupService.isSenderAuthorized(console, "mod")).thenReturn(true);

    List<String> result = heal.onTabComplete(console, command, "heal", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenConsoleIsModWithOneArg() {
    ConsoleCommandSender console = mock(ConsoleCommandSender.class);
    Player onlinePlayer = mock(Player.class);

    when(groupService.isSenderAuthorized(console, "mod")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = heal.onTabComplete(console, command, "heal", new String[]{"partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsAllOnlinePlayersWhenConsoleIsModWithEmptyArg() {
    ConsoleCommandSender console = mock(ConsoleCommandSender.class);
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);

    when(groupService.isSenderAuthorized(console, "mod")).thenReturn(true);
    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(firstOnlinePlayer.getName()).thenReturn("FirstPlayer");
    when(secondOnlinePlayer.getName()).thenReturn("SecondPlayer");

    List<String> result = heal.onTabComplete(console, command, "heal", new String[]{""});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("FirstPlayer"));
    assertTrue(result.contains("SecondPlayer"));
  }

  @Test
  void onCommandSendsTooFewArgumentsWhenConsoleProvideNoArgsAndHasNoTarget() {
    ConsoleCommandSender console = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(console, command, "heal", new String[]{});

    assertTrue(result);
    verify(serverService, never()).getPlayer(org.mockito.ArgumentMatchers.anyString());
  }

  @Test
  void onCommandHealsTargetWhenCommandBlockProvidesValidPlayerName() {
    BlockCommandSender commandBlock = mock(BlockCommandSender.class);

    when(serverService.getPlayer("TargetPlayer")).thenReturn(player);
    when(translationService.getWithPrefix(MessageKey.COMMAND_HEAL)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = heal.onCommand(commandBlock, command, "heal", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).setHealth(DEFAULT_MAX_HEALTH);
    verify(player).setFoodLevel(20);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }
}