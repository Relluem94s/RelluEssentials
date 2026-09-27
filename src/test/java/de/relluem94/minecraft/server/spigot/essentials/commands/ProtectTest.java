package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProtectTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private PlayerService playerService;

  @Mock
  private ServerService serverService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  @Mock
  private PlayerEntry playerEntry;

  private Protect protect;

  private static final String TRANSLATED_MESSAGE = "translated-message";
  private static final UUID PLAYER_UUID = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    protect = new Protect();
    protect.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(player.getUniqueId()).thenReturn(PLAYER_UUID);
    lenient().when(playerService.getPlayerEntry(PLAYER_UUID)).thenReturn(playerEntry);
  }

  @Test
  void getCommandsReturnsAllSubCommands() {
    CommandsEnum[] result = protect.getCommands();

    assertNotNull(result);
    assertEquals(Protect.Commands.values().length, result.length);
  }

  @Test
  void commandsEnumAddHasCorrectName() {
    assertEquals("add", Protect.Commands.ADD.getName());
  }

  @Test
  void commandsEnumRemoveHasCorrectName() {
    assertEquals("remove", Protect.Commands.REMOVE.getName());
  }

  @Test
  void commandsEnumInfoHasCorrectName() {
    assertEquals("info", Protect.Commands.INFO.getName());
  }

  @Test
  void commandsEnumFlagHasCorrectNameAndSubCommands() {
    assertEquals("flag", Protect.Commands.FLAG.getName());
    assertEquals(2, Protect.Commands.FLAG.getSubCommands().length);
  }

  @Test
  void commandsEnumRightHasCorrectNameAndSubCommands() {
    assertEquals("right", Protect.Commands.RIGHT.getName());
    assertEquals(2, Protect.Commands.RIGHT.getSubCommands().length);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(consoleSender, command, "protect", new String[]{});

    assertTrue(result);
    verify(consoleSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotAuthorized() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsCommandInfoWhenNoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_PROTECT_COMMAND_INFO,
        "protect",
        Protect.Commands.ADD.getName(),
        Protect.Commands.REMOVE.getName(),
        Protect.Commands.FLAG.getName(),
        Protect.Commands.RIGHT.getName(),
        Protect.Commands.INFO.getName()
    )).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsProtectAddAndSetsStateWhenArgIsAdd() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_ADD)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"add"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(playerEntry).setPlayerState(PlayerState.PROTECTION_ADD);
  }

  @Test
  void onCommandSendsProtectRemoveAndSetsStateWhenArgIsRemove() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_REMOVE)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"remove"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(playerEntry).setPlayerState(PlayerState.PROTECTION_REMOVE);
  }

  @Test
  void onCommandSendsProtectFlagInfoWhenArgIsFlag() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_PROTECT_FLAG,
        "protect",
        Protect.Commands.FLAG.getName(),
        Protect.Commands.FLAG.getSubCommands()[1],
        Protect.Commands.FLAG.getSubCommands()[0]
    )).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"flag"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsProtectRightInfoWhenArgIsRight() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_PROTECT_RIGHT,
        "protect",
        Protect.Commands.RIGHT.getName(),
        Protect.Commands.RIGHT.getSubCommands()[1],
        Protect.Commands.RIGHT.getSubCommands()[0]
    )).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"right"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsProtectInfoAndSetsStateWhenArgIsInfo() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_INFO)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"info"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(playerEntry).setPlayerState(PlayerState.PROTECTION_INFO);
  }

  @Test
  void onCommandSendsWrongSubCommandWhenOneUnknownArgProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"unknown"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsProtectFlagAddAndSetsStateWhenFlagAddWithValidFlag() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_FLAG_ADD)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"flag", "add", "ALLOW_PUBLIC"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(playerEntry).setPlayerState(PlayerState.PROTECTION_FLAG_ADD);
    verify(playerEntry).setPlayerStateParameter("ALLOW_PUBLIC");
  }

  @Test
  void onCommandSendsFlagNotFoundWhenFlagAddWithInvalidFlag() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_FLAG_NOT_FOUND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"flag", "add", "INVALIDFLAG"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsProtectFlagRemoveAndSetsStateWhenFlagRemoveWithValidFlag() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_FLAG_REMOVE)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"flag", "remove", "ALLOW_PUBLIC"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(playerEntry).setPlayerState(PlayerState.PROTECTION_FLAG_REMOVE);
    verify(playerEntry).setPlayerStateParameter("ALLOW_PUBLIC");
  }

  @Test
  void onCommandSendsFlagNotFoundWhenFlagRemoveWithInvalidFlag() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_FLAG_NOT_FOUND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"flag", "remove", "INVALIDFLAG"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsWrongSubCommandWhenFlagWithUnknownSubCommand() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"flag", "unknown", "INTERACT"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsRightAddAndSetsStateWhenRightAddWithKnownPlayer() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_RIGHT_ADD)).thenReturn(TRANSLATED_MESSAGE);

    Player onlinePlayer = mock(Player.class);
    UUID onlineUuid = UUID.randomUUID();
    lenient().when(onlinePlayer.getName()).thenReturn("KnownPlayer");
    lenient().when(onlinePlayer.getUniqueId()).thenReturn(onlineUuid);
    lenient().doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();

    boolean result = protect.onCommand(player, command, "protect", new String[]{"right", "add", "KnownPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(playerEntry).setPlayerState(PlayerState.PROTECTION_RIGHT_ADD);
  }

  @Test
  void onCommandSendsPlayerNotFoundWhenRightAddWithUnknownPlayer() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_RIGHT_PLAYER_NOTFOUND, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    try (MockedStatic<PlayerHelper> mockedStatic = mockStatic(PlayerHelper.class)) {
      mockedStatic.when(() -> PlayerHelper.getOfflinePlayerByName("UnknownPlayer")).thenReturn(null);

      boolean result = protect.onCommand(player, command, "protect", new String[]{"right", "add", "UnknownPlayer"});

      assertTrue(result);
      verify(player).sendMessage(TRANSLATED_MESSAGE);
    }
  }

  @Test
  void onCommandSendsRightRemoveAndSetsStateWhenRightRemoveWithKnownPlayer() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_RIGHT_REMOVE)).thenReturn(TRANSLATED_MESSAGE);

    Player onlinePlayer = mock(Player.class);
    UUID onlineUuid = UUID.randomUUID();
    lenient().when(onlinePlayer.getName()).thenReturn("KnownPlayer");
    lenient().when(onlinePlayer.getUniqueId()).thenReturn(onlineUuid);
    lenient().doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();

    boolean result = protect.onCommand(player, command, "protect", new String[]{"right", "remove", "KnownPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(playerEntry).setPlayerState(PlayerState.PROTECTION_RIGHT_REMOVE);
  }

  @Test
  void onCommandSendsPlayerNotFoundWhenRightRemoveWithUnknownPlayer() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PROTECT_RIGHT_PLAYER_NOTFOUND, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    try (MockedStatic<PlayerHelper> mockedStatic = mockStatic(PlayerHelper.class)) {
      mockedStatic.when(() -> PlayerHelper.getOfflinePlayerByName("UnknownPlayer")).thenReturn(null);

      boolean result = protect.onCommand(player, command, "protect", new String[]{"right", "remove", "UnknownPlayer"});

      assertTrue(result);
      verify(player).sendMessage(TRANSLATED_MESSAGE);
    }
  }

  @Test
  void onCommandSendsWrongSubCommandWhenRightWithUnknownSubCommand() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"right", "unknown", "SomePlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsWrongSubCommandWhenThreeArgsAndUnknownFirstArg() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"unknown", "add", "INTERACT"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsWrongSubCommandWhenTwoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"flag", "add"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsWrongSubCommandWhenFourArgsProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = protect.onCommand(player, command, "protect", new String[]{"flag", "add", "INTERACT", "extra"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotAuthorized() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "user")).thenReturn(false);

    List<String> result = protect.onTabComplete(unauthorizedSender, command, "protect", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(consoleSender, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(consoleSender, command, "protect", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsAllCommandsWhenOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"a"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsFlagSubCommandsWhenTwoArgsAndFirstArgIsFlag() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"flag", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("add"));
    assertTrue(result.contains("remove"));
  }

  @Test
  void onTabCompleteReturnsRightSubCommandsWhenTwoArgsAndFirstArgIsRight() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"right", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("add"));
    assertTrue(result.contains("remove"));
  }

  @Test
  void onTabCompleteReturnsEmptyWhenTwoArgsAndFirstArgIsUnknown() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"unknown", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"add", "remove"})
  void onTabCompleteReturnsProtectionFlagsWhenThreeArgsAndFlagSubCommand(String subCommand) {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"flag", subCommand, "partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"add", "remove"})
  void onTabCompleteReturnsOnlinePlayersWhenThreeArgsAndRightSubCommand(String subCommand) {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"right", subCommand, "partial"});

    assertNotNull(result);
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsEmptyWhenThreeArgsAndUnknownFirstArg() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"unknown", "add", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenThreeArgsAndFlagButUnknownSubCommand() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"flag", "unknown", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenThreeArgsAndRightButUnknownSubCommand() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"right", "unknown", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenFourArgsProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = protect.onTabComplete(player, command, "protect", new String[]{"flag", "add", "INTERACT", "extra"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}