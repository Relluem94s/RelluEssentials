package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
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
class AfkTest {

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

  private Afk afk;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    afk = new Afk();
    afk.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = afk.getCommands();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(0, result.length)
    );
  }

  @Test
  void onCommandSendsNotAPlayerWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = afk.onCommand(nonPlayerSender, command, "afk", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsPermissionMissingWhenSenderIsNotUser() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = afk.onCommand(player, command, "afk", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSetsAfkOnSelfWhenNoArgs() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    boolean result = afk.onCommand(player, command, "afk", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(playerService).setAfk(player, false)
    );
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenTargetNotFound() {
    String targetName = "UnknownPlayer";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = afk.onCommand(player, command, "afk", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(playerService, never()).setAfk(player, false)
    );
  }

  @Test
  void onCommandSendsPermissionMissingWhenSenderIsNotModAndTargetProvided() {
    String targetName = "TargetPlayer";
    Player targetPlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(targetPlayer);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = afk.onCommand(player, command, "afk", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(playerService, never()).setAfk(targetPlayer, false)
    );
  }

  @Test
  void onCommandSetsAfkOnTargetWhenSenderIsModAndTargetExists() {
    String targetName = "TargetPlayer";
    Player targetPlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(targetPlayer);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    boolean result = afk.onCommand(player, command, "afk", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(playerService).setAfk(targetPlayer, false)
    );
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender nonModSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonModSender, "mod")).thenReturn(false);

    List<String> result = afk.onTabComplete(nonModSender, command, "afk", new String[]{"a"});

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonPlayerSender, "mod")).thenReturn(true);

    List<String> result = afk.onTabComplete(nonPlayerSender, command, "afk", new String[]{"a"});

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = afk.onTabComplete(player, command, "afk", new String[]{"arg1", "arg2"});

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void onTabCompleteReturnsOnlinePlayersWhenSenderIsModPlayerAndOneArg() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = afk.onTabComplete(player, command, "afk", new String[]{"partial"});

    assertNotNull(result);
  }
}