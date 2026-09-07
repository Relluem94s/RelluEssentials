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
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameModeSpectatorTest {

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

  private GameModeSpectator gameModeSpectator;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    gameModeSpectator = new GameModeSpectator();
    gameModeSpectator.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = gameModeSpectator.getCommands();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(0, result.length)
    );
  }

  @Test
  void onCommandSendsPermissionMissingWhenSenderIsNotMod() {
    CommandSender nonModSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonModSender, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = gameModeSpectator.onCommand(nonModSender, command, "gmsp", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(nonModSender).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSetsTargetSpectatorWhenOneArgAndTargetExists() {
    String targetName = "TargetPlayer";
    String targetCustomName = "TargetCustomName";
    String spectatorTranslation = "Spectator";
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(player);
    when(player.getCustomName()).thenReturn(targetCustomName);
    when(translationService.get(MessageKey.COMMAND_GAMEMODE_SPECTATOR)).thenReturn(spectatorTranslation);
    when(translationService.getWithPrefix(MessageKey.COMMAND_GAMEMODE, targetCustomName, spectatorTranslation)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = gameModeSpectator.onCommand(player, command, "gmsp", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).setGameMode(GameMode.SPECTATOR),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenOneArgAndTargetNotFound() {
    String targetName = "UnknownPlayer";
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(null);
    when(translationService.get(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetName)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = gameModeSpectator.onCommand(player, command, "gmsp", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(player, never()).setGameMode(GameMode.SPECTATOR)
    );
  }

  @Test
  void onCommandSendsNotAPlayerWhenNoArgsAndSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonPlayerSender, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = gameModeSpectator.onCommand(nonPlayerSender, command, "gmsp", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSetsSelfSpectatorWhenNoArgsAndSenderIsPlayer() {
    String customName = "SenderCustomName";
    String spectatorTranslation = "Spectator";
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getCustomName()).thenReturn(customName);
    when(translationService.get(MessageKey.COMMAND_GAMEMODE_SPECTATOR)).thenReturn(spectatorTranslation);
    when(translationService.getWithPrefix(MessageKey.COMMAND_GAMEMODE, customName, spectatorTranslation)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = gameModeSpectator.onCommand(player, command, "gmsp", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).setGameMode(GameMode.SPECTATOR),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender nonModSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonModSender, "mod")).thenReturn(false);

    List<String> result = gameModeSpectator.onTabComplete(nonModSender, command, "gmsp", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = gameModeSpectator.onTabComplete(player, command, "gmsp", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayersWhenSenderIsModAndOneArg() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = gameModeSpectator.onTabComplete(player, command, "gmsp", new String[]{"partial"});

    assertNotNull(result);
  }
}