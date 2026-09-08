package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
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
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
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
class FlyTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ServerService serverService;

  @Mock
  private PlayerService playerService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Fly fly;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    fly = new Fly();
    fly.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = fly.getCommands();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(0, result.length)
    );
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = fly.onCommand(nonPlayerSender, command, "fly", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotVip() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = fly.onCommand(player, command, "fly", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandTogglesFlyModeForSelfWhenNoArgsProvided() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.isFlying()).thenReturn(false).thenReturn(true);
    when(playerEntry.getId()).thenReturn(1);
    when(player.getAllowFlight()).thenReturn(true);
    when(player.getCustomName()).thenReturn("TestPlayer");
    when(translationService.get(MessageKey.COMMAND_FLYMODE_ACTIVATED)).thenReturn("activated");
    when(translationService.getWithPrefix(MessageKey.COMMAND_FLYMODE, "TestPlayer", "activated")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = fly.onCommand(player, command, "fly", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(playerEntry).setFlying(true),
        () -> verify(playerEntry).setUpdatedBy(1),
        () -> verify(playerEntry).setHasToBeUpdated(true),
        () -> verify(player).setAllowFlight(true),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandTogglesFlyModeOffForSelfWhenAlreadyFlying() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.isFlying()).thenReturn(true).thenReturn(false);
    when(playerEntry.getId()).thenReturn(1);
    when(player.getAllowFlight()).thenReturn(false);
    when(player.getCustomName()).thenReturn("TestPlayer");
    when(translationService.get(MessageKey.COMMAND_FLYMODE_DEACTIVATED)).thenReturn("deactivated");
    when(translationService.getWithPrefix(MessageKey.COMMAND_FLYMODE, "TestPlayer", "deactivated")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = fly.onCommand(player, command, "fly", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(playerEntry).setFlying(false),
        () -> verify(player).setAllowFlight(false),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenTargetPlayerNotFound() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = fly.onCommand(player, command, "fly", new String[]{"UnknownPlayer"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotModAndTargetIsProvided() {
    Player targetPlayer = mock(Player.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = fly.onCommand(player, command, "fly", new String[]{"TargetPlayer"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(playerService, never()).getPlayerEntry(targetPlayer)
    );
  }

  @Test
  void onCommandTogglesFlyModeForTargetWhenSenderIsMod() {
    Player targetPlayer = mock(Player.class);
    PlayerEntry targetEntry = mock(PlayerEntry.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(playerService.getPlayerEntry(targetPlayer)).thenReturn(targetEntry);
    when(targetEntry.isFlying()).thenReturn(false).thenReturn(true);
    when(targetEntry.getId()).thenReturn(2);
    when(targetPlayer.getCustomName()).thenReturn("TargetPlayer");
    when(targetPlayer.getAllowFlight()).thenReturn(false).thenReturn(true);
    when(translationService.get(MessageKey.COMMAND_FLYMODE_ACTIVATED)).thenReturn("activated");
    when(translationService.getWithPrefix(MessageKey.COMMAND_FLYMODE, "TargetPlayer", "activated")).thenReturn("activated-message");

    boolean result = fly.onCommand(player, command, "fly", new String[]{"TargetPlayer"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage("activated-message"),
        () -> verify(targetPlayer).sendMessage("activated-message"),
        () -> verify(targetEntry).setFlying(true),
        () -> verify(targetEntry).setUpdatedBy(2),
        () -> verify(targetEntry).setHasToBeUpdated(true),
        () -> verify(targetPlayer).setAllowFlight(true)
    );
  }

  @Test
  void onCommandSendsDeactivatedMessageToSenderBeforeTogglingWhenTargetIsFlying() {
    Player targetPlayer = mock(Player.class);
    PlayerEntry targetEntry = mock(PlayerEntry.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(playerService.getPlayerEntry(targetPlayer)).thenReturn(targetEntry);
    when(targetEntry.isFlying()).thenReturn(true).thenReturn(false);
    when(targetEntry.getId()).thenReturn(2);
    when(targetPlayer.getCustomName()).thenReturn("TargetPlayer");
    when(targetPlayer.getAllowFlight()).thenReturn(true);
    when(translationService.get(MessageKey.COMMAND_FLYMODE_DEACTIVATED)).thenReturn("deactivated");
    when(translationService.get(MessageKey.COMMAND_FLYMODE_ACTIVATED)).thenReturn("activated");
    when(translationService.getWithPrefix(MessageKey.COMMAND_FLYMODE, "TargetPlayer", "deactivated")).thenReturn("deactivated-message");

    fly.onCommand(player, command, "fly", new String[]{"TargetPlayer"});

    assertAll(
        () -> verify(player).sendMessage("deactivated-message"),
        () -> verify(targetEntry).setFlying(false),
        () -> verify(targetPlayer).setAllowFlight(false)
    );
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = fly.onTabComplete(unauthorizedSender, command, "fly", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = fly.onTabComplete(player, command, "fly", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenModWithOneArg() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = fly.onTabComplete(player, command, "fly", new String[]{"partial"});

    assertNotNull(result);
    assertAll(
        () -> assertFalse(result.isEmpty()),
        () -> assertTrue(result.contains("OnlinePlayer"))
    );
  }

  @Test
  void onTabCompleteReturnsAllOnlinePlayersWhenModWithEmptyArg() {
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(firstOnlinePlayer.getName()).thenReturn("FirstPlayer");
    when(secondOnlinePlayer.getName()).thenReturn("SecondPlayer");

    List<String> result = fly.onTabComplete(player, command, "fly", new String[]{""});

    assertNotNull(result);
    assertAll(
        () -> assertEquals(2, result.size()),
        () -> assertTrue(result.contains("FirstPlayer")),
        () -> assertTrue(result.contains("SecondPlayer"))
    );
  }
}