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
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WhereTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ServerService serverService;

  @Mock
  private MessageService messageService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Where where;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    where = new Where();
    where.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getMessageService()).thenReturn(messageService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = where.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayerAndNoArgsProvided() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = where.onCommand(nonPlayerSender, command, "where", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerLacksUserPermissionAndNoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = where.onCommand(player, command, "where", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsLocationMessageWhenPlayerHasUserPermissionAndNoArgsProvided() {
    Location location = mock(Location.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(player.getCustomName()).thenReturn("TestPlayer");
    when(player.getLocation()).thenReturn(location);
    when(messageService.locationToString(location)).thenReturn("world, 10, 64, 20");
    when(translationService.getWithPrefix(MessageKey.COMMAND_WHERE, "TestPlayer", "world, 10, 64, 20")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = where.onCommand(player, command, "where", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenSenderLacksModPermissionAndArgProvided() {
    CommandSender nonModSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonModSender, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = where.onCommand(nonModSender, command, "where", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(nonModSender).sendMessage(TRANSLATED_MESSAGE);
    verify(serverService, never()).getPlayer("TargetPlayer");
  }

  @Test
  void onCommandSendsTargetNotAPlayerMessageWhenTargetNotFoundAndArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = where.onCommand(player, command, "where", new String[]{"UnknownPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTargetLocationWhenModRequestsExistingPlayerLocation() {
    Player targetPlayer = mock(Player.class);
    Location location = mock(Location.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(targetPlayer.getCustomName()).thenReturn("TargetPlayer");
    when(targetPlayer.getLocation()).thenReturn(location);
    when(messageService.locationToString(location)).thenReturn("world, 5, 70, 15");
    when(translationService.getWithPrefix(MessageKey.COMMAND_WHERE, "TargetPlayer", "world, 5, 70, 15")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = where.onCommand(player, command, "where", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderLacksModPermission() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = where.onTabComplete(unauthorizedSender, command, "where", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = where.onTabComplete(player, command, "where", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenModWithOneArg() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = where.onTabComplete(player, command, "where", new String[]{"partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsAllOnlinePlayerNamesWhenModWithEmptyArg() {
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(firstOnlinePlayer.getName()).thenReturn("FirstPlayer");
    when(secondOnlinePlayer.getName()).thenReturn("SecondPlayer");

    List<String> result = where.onTabComplete(player, command, "where", new String[]{""});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("FirstPlayer"));
    assertTrue(result.contains("SecondPlayer"));
  }
}