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
import de.relluem94.minecraft.server.spigot.essentials.services.BackService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeleportTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ServerService serverService;

  @Mock
  private BackService backService;

  @Mock
  private SchedulerService schedulerService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Teleport teleport;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    teleport = new Teleport();
    teleport.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getBackService()).thenReturn(backService);
    lenient().when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    lenient().when(command.getName()).thenReturn("teleport");
  }

  @Test
  void getCommandsReturnsBothSubCommands() {
    CommandsEnum[] result = teleport.getCommands();

    assertNotNull(result);
    assertEquals(2, result.length);
  }

  @Test
  void commandsEnumAcceptHasCorrectName() {
    assertEquals("accept", Teleport.Commands.ACCEPT.getName());
  }

  @Test
  void commandsEnumToHasCorrectName() {
    assertEquals("to", Teleport.Commands.TO.getName());
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(nonPlayerSender, command, "teleport", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotVip() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsInfoMessageWhenNoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_TP_INFO, "teleport", "teleport", "accept", "teleport", "to", "teleport"))
        .thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsAcceptNoRequestWhenNoTeleportRequestPending() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_ACCEPT_NO_REQUEST)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"accept"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenPlayerNotFoundWithOneArg() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("Unknown")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "Unknown")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"Unknown"});

    assertFalse(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsRequestWhenPlayerIsNotModAndTargetExists() {
    Player target = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("Target")).thenReturn(target);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(player.getCustomName()).thenReturn("Sender");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_SEND_REQUEST, target.getCustomName())).thenReturn(TRANSLATED_MESSAGE);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, "Sender")).thenReturn("request-message");

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"Target"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(target).sendMessage("request-message");
  }

  @Test
  void onCommandTeleportsDirectlyWhenSenderIsModAndTargetExists() {
    Player target = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("Target")).thenReturn(target);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(target.getCustomName()).thenReturn("Target");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "Target")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"Target"});

    assertTrue(result);
    verify(backService).saveBackPoint(player);
    verify(player).teleport(target);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenTwoArgsAndTargetNotFound() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("Unknown")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "Unknown")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"to", "Unknown"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsInfoWhenTwoArgsAndFirstArgIsNotTo() {
    Player target = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("Target")).thenReturn(target);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_TP_INFO, "teleport", "teleport", "accept", "teleport", "to", "teleport"))
        .thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"invalid", "Target"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsToRequestWhenTwoArgsAndPlayerIsNotMod() {
    Player target = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("Target")).thenReturn(target);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(player.getCustomName()).thenReturn("Sender");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, "Sender")).thenReturn("to-request-message");

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"to", "Target"});

    assertTrue(result);
    verify(target).sendMessage("to-request-message");
  }

  @Test
  void onCommandTeleportsToTargetWhenTwoArgsAndSenderIsMod() {
    Player target = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("Target")).thenReturn(target);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getCustomName()).thenReturn("Sender");
    when(target.getCustomName()).thenReturn("Target");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_TO, "Sender")).thenReturn("tp-to-message");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "Target")).thenReturn("tp-message");

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"to", "Target"});

    assertTrue(result);
    verify(backService).saveBackPoint(player);
    verify(player).teleport(target);
    verify(player).sendMessage("tp-to-message");
    verify(target).sendMessage("tp-message");
  }

  @Test
  void onCommandSendsPermissionMissingWhenThreeArgsAndNotMod() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"10", "64", "20"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsInvalidWhenThreeArgsAndXIsNotInt() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_INVALID)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"notAnInt", "64", "20"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandTeleportsToLocationWhenThreeValidCoordinatesAndIsMod() {
    World world = mock(World.class);
    Location location = new Location(world, 0, 0, 0);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "10.0, 64.0, 20.0")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"10", "64", "20"});

    assertTrue(result);
    verify(backService).saveBackPoint(player);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTooManyArgumentsWhenFourArgsProvided() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"a", "b", "c", "d"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandAcceptsTeleportRequestWhenPendingEntryExists() {
    Player requester = mock(Player.class);

    when(groupService.isSenderAuthorized(requester, "vip")).thenReturn(true);
    when(serverService.getPlayer("Receiver")).thenReturn(player);
    when(groupService.isSenderAuthorized(requester, "mod")).thenReturn(false);
    when(requester.getCustomName()).thenReturn("Requester");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_SEND_REQUEST, player.getCustomName())).thenReturn("request-sent");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, "Requester")).thenReturn("request-target");

    teleport.onCommand(requester, command, "teleport", new String[]{"Receiver"});

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(requester.getCustomName()).thenReturn("Requester");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "Requester")).thenReturn("teleported-message");

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"accept"});

    assertTrue(result);
    verify(backService).saveBackPoint(requester);
    verify(requester).teleport(player);
    verify(requester).sendMessage("teleported-message");
  }

  @Test
  void onCommandAcceptsTeleportToRequestWhenPendingToEntryExists() {
    Player requester = mock(Player.class);

    when(groupService.isSenderAuthorized(requester, "vip")).thenReturn(true);
    when(serverService.getPlayer("Receiver")).thenReturn(player);
    when(groupService.isSenderAuthorized(requester, "mod")).thenReturn(false);
    when(requester.getCustomName()).thenReturn("Requester");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, "Requester")).thenReturn("to-request-target");

    teleport.onCommand(requester, command, "teleport", new String[]{"to", "Receiver"});

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(player.getCustomName()).thenReturn("Receiver");
    lenient().when(translationService.getWithPrefix(MessageKey.COMMAND_TP_TO, "Receiver")).thenReturn("tp-to-message");
    lenient().when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "Requester")).thenReturn("tp-message");

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"accept"});

    assertTrue(result);
    verify(backService).saveBackPoint(player);
    verify(player).teleport(requester);
    verify(player).sendMessage("tp-to-message");
    verify(requester).sendMessage("tp-message");
  }

  @Test
  void teleportSavesBackPointAndTeleportsAndNotifiesTarget() {
    Player target = mock(Player.class);
    when(target.getCustomName()).thenReturn("Target");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "Target")).thenReturn(TRANSLATED_MESSAGE);

    teleport.teleport(player, target);

    verify(backService).saveBackPoint(target);
    verify(target).teleport(player);
    verify(target).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void teleportToSavesBackPointAndTeleportsAndNotifiesBothPlayers() {
    Player target = mock(Player.class);
    when(player.getCustomName()).thenReturn("Sender");
    when(target.getCustomName()).thenReturn("Target");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_TO, "Sender")).thenReturn("tp-to-message");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "Target")).thenReturn("tp-message");

    teleport.teleportTo(player, target);

    verify(backService).saveBackPoint(player);
    verify(player).teleport(target);
    verify(player).sendMessage("tp-to-message");
    verify(target).sendMessage("tp-message");
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotUser() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "user")).thenReturn(false);

    List<String> result = teleport.onTabComplete(unauthorizedSender, command, "teleport", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(consoleSender, "user")).thenReturn(true);

    List<String> result = teleport.onTabComplete(consoleSender, command, "teleport", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsSubCommandsAndOnlinePlayersWhenOneArgProvided() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = teleport.onTabComplete(player, command, "teleport", new String[]{"partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
    assertTrue(result.contains("accept"));
    assertTrue(result.contains("to"));
  }

  @Test
  void onTabCompleteExcludesSenderFromOnlinePlayersWhenOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    doReturn(List.of(player)).when(serverService).getOnlinePlayers();

    List<String> result = teleport.onTabComplete(player, command, "teleport", new String[]{"partial"});

    assertNotNull(result);
    verify(serverService).getOnlinePlayers();
  }

  @Test
  void onTabCompleteReturnsOnlinePlayersWhenTwoArgsAndFirstArgIsTo() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = teleport.onTabComplete(player, command, "teleport", new String[]{"to", "partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenTwoArgsAndFirstArgIsNotTo() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = teleport.onTabComplete(player, command, "teleport", new String[]{"accept", "something"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenThreeArgsProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = teleport.onTabComplete(player, command, "teleport", new String[]{"to", "Target", "extra"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onCommandDoesNotTeleportWhenYCoordinateIsNotInt() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"10", "notAnInt", "20"});

    assertTrue(result);
    verify(backService, never()).saveBackPoint(player);
  }

  @Test
  void onCommandDoesNotTeleportWhenZCoordinateIsNotInt() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"10", "64", "notAnInt"});

    assertTrue(result);
    verify(backService, never()).saveBackPoint(player);
  }

  @Test
  void addTeleportEntrySchedulerRemovesEntryAndNotifiesBothPlayersAfterExpiry() {
    Player requester = mock(Player.class);
    org.mockito.ArgumentCaptor<Runnable> runnableCaptor = org.mockito.ArgumentCaptor.forClass(Runnable.class);

    when(groupService.isSenderAuthorized(requester, "vip")).thenReturn(true);
    when(serverService.getPlayer("Receiver")).thenReturn(player);
    when(groupService.isSenderAuthorized(requester, "mod")).thenReturn(false);
    when(requester.getCustomName()).thenReturn("Requester");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, "Requester")).thenReturn("request-target");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_SEND_REQUEST, player.getCustomName())).thenReturn("request-sent");

    teleport.onCommand(requester, command, "teleport", new String[]{"Receiver"});

    verify(schedulerService).runTaskLater(runnableCaptor.capture(), org.mockito.ArgumentMatchers.eq(20 * 60 * 2L));

    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_EXPIRED)).thenReturn("expired-message");

    runnableCaptor.getValue().run();

    verify(requester).sendMessage("expired-message");
    verify(player).sendMessage("expired-message");

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_ACCEPT_NO_REQUEST)).thenReturn("no-request");

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"accept"});

    assertTrue(result);
    verify(player).sendMessage("no-request");
  }

  @Test
  void addTeleportToEntrySchedulerRemovesEntryAndNotifiesBothPlayersAfterExpiry() {
    Player requester = mock(Player.class);
    org.mockito.ArgumentCaptor<Runnable> runnableCaptor = org.mockito.ArgumentCaptor.forClass(Runnable.class);

    when(groupService.isSenderAuthorized(requester, "vip")).thenReturn(true);
    when(serverService.getPlayer("Receiver")).thenReturn(player);
    when(groupService.isSenderAuthorized(requester, "mod")).thenReturn(false);
    when(requester.getCustomName()).thenReturn("Requester");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, "Requester")).thenReturn("to-request-target");

    teleport.onCommand(requester, command, "teleport", new String[]{"to", "Receiver"});

    verify(schedulerService).runTaskLater(runnableCaptor.capture(), org.mockito.ArgumentMatchers.eq(20 * 60 * 2L));

    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_EXPIRED)).thenReturn("expired-message");

    runnableCaptor.getValue().run();

    verify(requester).sendMessage("expired-message");
    verify(player).sendMessage("expired-message");

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_ACCEPT_NO_REQUEST)).thenReturn("no-request");

    boolean result = teleport.onCommand(player, command, "teleport", new String[]{"accept"});

    assertTrue(result);
    verify(player).sendMessage("no-request");
  }

  @Test
  void addTeleportEntrySchedulerDoesNotNotifyWhenEntryAlreadyRemoved() {
    Player requester = mock(Player.class);
    org.mockito.ArgumentCaptor<Runnable> runnableCaptor = org.mockito.ArgumentCaptor.forClass(Runnable.class);

    when(groupService.isSenderAuthorized(requester, "vip")).thenReturn(true);
    when(serverService.getPlayer("Receiver")).thenReturn(player);
    when(groupService.isSenderAuthorized(requester, "mod")).thenReturn(false);
    when(requester.getCustomName()).thenReturn("Requester");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, "Requester")).thenReturn("request-target");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_SEND_REQUEST, player.getCustomName())).thenReturn("request-sent");

    teleport.onCommand(requester, command, "teleport", new String[]{"Receiver"});

    verify(schedulerService).runTaskLater(runnableCaptor.capture(), org.mockito.ArgumentMatchers.eq(20 * 60 * 2L));

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(requester.getCustomName()).thenReturn("Requester");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "Requester")).thenReturn("teleported-message");

    teleport.onCommand(player, command, "teleport", new String[]{"accept"});

    runnableCaptor.getValue().run();

    verify(requester, never()).sendMessage(org.mockito.ArgumentMatchers.eq("expired-message"));
    verify(player, never()).sendMessage(org.mockito.ArgumentMatchers.eq("expired-message"));
  }

  @Test
  void addTeleportToEntrySchedulerDoesNotNotifyWhenEntryAlreadyRemoved() {
    Player requester = mock(Player.class);
    org.mockito.ArgumentCaptor<Runnable> runnableCaptor = org.mockito.ArgumentCaptor.forClass(Runnable.class);

    when(groupService.isSenderAuthorized(requester, "vip")).thenReturn(true);
    when(serverService.getPlayer("Receiver")).thenReturn(player);
    when(groupService.isSenderAuthorized(requester, "mod")).thenReturn(false);
    when(requester.getCustomName()).thenReturn("Requester");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TP_REQUEST_TARGET, "Requester")).thenReturn("to-request-target");

    teleport.onCommand(requester, command, "teleport", new String[]{"to", "Receiver"});

    verify(schedulerService).runTaskLater(runnableCaptor.capture(), org.mockito.ArgumentMatchers.eq(20 * 60 * 2L));

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(player.getCustomName()).thenReturn("Receiver");
    lenient().when(translationService.getWithPrefix(MessageKey.COMMAND_TP_TO, "Receiver")).thenReturn("tp-to-message");
    lenient().when(translationService.getWithPrefix(MessageKey.COMMAND_TP, "Requester")).thenReturn("tp-message");

    teleport.onCommand(player, command, "teleport", new String[]{"accept"});

    runnableCaptor.getValue().run();

    verify(requester, never()).sendMessage(org.mockito.ArgumentMatchers.eq("expired-message"));
    verify(player, never()).sendMessage(org.mockito.ArgumentMatchers.eq("expired-message"));
  }

  @Test
  void onTabCompleteExcludesSenderFromSuggestionsWhenTwoArgsAndFirstArgIsTo() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    doReturn(List.of(player)).when(serverService).getOnlinePlayers();

    List<String> result = teleport.onTabComplete(player, command, "teleport", new String[]{"to", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}