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
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionsGroupTest {

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

  private PermissionsGroup permissionsGroup;

  @BeforeEach
  void setUp() {
    permissionsGroup = new PermissionsGroup();
    permissionsGroup.injectContext(serviceContext);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] commands = permissionsGroup.getCommands();
    assertNotNull(commands);
    assertEquals(0, commands.length);
  }

  @Test
  void onCommandReturnsTrueAndSendsTooFewArgumentsMessageWhenNoArgsProvided() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn("too few args");

    boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{});

    assertTrue(result);
    verify(sender).sendMessage("too few args");
  }

  @Test
  void onCommandReturnsTrueAndSendsTooFewArgumentsMessageWhenOnlyOneArgProvided() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn("too few args");

    boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"PlayerName"});

    assertTrue(result);
    verify(sender).sendMessage("too few args");
  }

  @Test
  void onCommandReturnsTrueAndSendsTooManyArgumentsMessageWhenMoreThanTwoArgsProvided() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS)).thenReturn("too many args");

    boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"PlayerName", "mod", "extra"});

    assertTrue(result);
    verify(sender).sendMessage("too many args");
  }

  @Test
  void onCommandReturnsTrueAndSendsNotAPlayerMessageWhenTargetPlayerNotFound() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "UnknownPlayer"))
        .thenReturn("not a player");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("UnknownPlayer")).thenReturn(null);

      boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"UnknownPlayer", "mod"});

      assertTrue(result);
      verify(sender).sendMessage("not a player");
    }
  }

  @Test
  void onCommandReturnsTrueAndSendsNotAPlayerMessageWhenPlayerEntryNotFound() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
    Player onlinePlayer = mock(Player.class);

    when(offlinePlayer.getPlayer()).thenReturn(onlinePlayer);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(onlinePlayer)).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "KnownPlayer"))
        .thenReturn("not a player");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("KnownPlayer")).thenReturn(offlinePlayer);

      boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"KnownPlayer", "mod"});

      assertTrue(result);
      verify(sender).sendMessage("not a player");
    }
  }

  @Test
  void onCommandReturnsTrueAndSendsGroupNotFoundMessageWhenPlayerSenderProvidesInvalidGroup() {
    Player sender = mock(Player.class);
    OfflinePlayer offlineTarget = mock(OfflinePlayer.class);
    Player onlineTarget = mock(Player.class);

    when(offlineTarget.getPlayer()).thenReturn(onlineTarget);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(onlineTarget)).thenReturn(new PlayerEntry());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.resolveAuthorizedGroup(sender, "unknownGroup")).thenReturn(Optional.empty());
    when(translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP_GROUP_NOT_FOUND, "unknownGroup"))
        .thenReturn("group not found");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("TargetPlayer")).thenReturn(offlineTarget);

      boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"TargetPlayer", "unknownGroup"});

      assertTrue(result);
      verify(sender).sendMessage("group not found");
      verify(playerService, never()).updateGroup(offlineTarget, null);
    }
  }

  @Test
  void onCommandReturnsTrueAndUpdatesGroupWhenPlayerSenderProvidesValidGroup() {
    Player sender = mock(Player.class);
    OfflinePlayer offlineTarget = mock(OfflinePlayer.class);
    Player onlineTarget = mock(Player.class);
    GroupEntry groupEntry = mock(GroupEntry.class);

    when(groupEntry.getPrefix()).thenReturn("§a");
    when(groupEntry.getName()).thenReturn("mod");
    when(offlineTarget.getPlayer()).thenReturn(onlineTarget);
    when(offlineTarget.isOnline()).thenReturn(false);
    when(offlineTarget.getName()).thenReturn("TargetPlayer");
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(onlineTarget)).thenReturn(new PlayerEntry());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.resolveAuthorizedGroup(sender, "mod")).thenReturn(Optional.of(groupEntry));
    when(translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP, "§amod", "TargetPlayer"))
        .thenReturn("group set");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("TargetPlayer")).thenReturn(offlineTarget);

      boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"TargetPlayer", "mod"});

      assertTrue(result);
      verify(playerService).updateGroup(offlineTarget, groupEntry);
      verify(sender).sendMessage("group set");
    }
  }

  @Test
  void onCommandNotifiesOnlineTargetWhenPlayerSenderAssignsGroup() {
    Player sender = mock(Player.class);
    OfflinePlayer offlineTarget = mock(OfflinePlayer.class);
    Player onlineTarget = mock(Player.class);
    GroupEntry groupEntry = mock(GroupEntry.class);
    UUID targetUuid = UUID.randomUUID();

    when(groupEntry.getPrefix()).thenReturn("§a");
    when(groupEntry.getName()).thenReturn("mod");
    when(offlineTarget.getPlayer()).thenReturn(onlineTarget);
    when(offlineTarget.isOnline()).thenReturn(true);
    when(offlineTarget.getName()).thenReturn("TargetPlayer");
    when(offlineTarget.getUniqueId()).thenReturn(targetUuid);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(onlineTarget)).thenReturn(new PlayerEntry());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.resolveAuthorizedGroup(sender, "mod")).thenReturn(Optional.of(groupEntry));
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getPlayer(targetUuid)).thenReturn(onlineTarget);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP, "§amod", "TargetPlayer"))
        .thenReturn("group set");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("TargetPlayer")).thenReturn(offlineTarget);

      boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"TargetPlayer", "mod"});

      assertTrue(result);
      verify(playerService).updateGroup(offlineTarget, groupEntry);
      verify(sender).sendMessage("group set");
      verify(onlineTarget).sendMessage("group set");
    }
  }

  @ParameterizedTest
  @MethodSource("provideNonPlayerSenders")
  void onCommandReturnsTrueAndUpdatesGroupWhenNonPlayerSenderProvidesValidGroup(Object rawSender) {
    org.bukkit.command.CommandSender sender = (org.bukkit.command.CommandSender) rawSender;
    OfflinePlayer offlineTarget = mock(OfflinePlayer.class);
    Player onlineTarget = mock(Player.class);
    GroupEntry groupEntry = mock(GroupEntry.class);

    when(groupEntry.getPrefix()).thenReturn("§c");
    when(groupEntry.getName()).thenReturn("admin");
    when(offlineTarget.getPlayer()).thenReturn(onlineTarget);
    when(offlineTarget.isOnline()).thenReturn(false);
    when(offlineTarget.getName()).thenReturn("TargetPlayer");
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(onlineTarget)).thenReturn(new PlayerEntry());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.resolveGroupWithFallback("admin")).thenReturn(groupEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP, "§cadmin", "TargetPlayer"))
        .thenReturn("group set");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("TargetPlayer")).thenReturn(offlineTarget);

      boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"TargetPlayer", "admin"});

      assertTrue(result);
      verify(playerService).updateGroup(offlineTarget, groupEntry);
      verify(sender).sendMessage("group set");
    }
  }

  private static Stream<Arguments> provideNonPlayerSenders() {
    return Stream.of(
        Arguments.of(mock(ConsoleCommandSender.class)),
        Arguments.of(mock(BlockCommandSender.class))
    );
  }

  @Test
  void onCommandNotifiesOnlineTargetWhenConsoleSenderAssignsGroup() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    OfflinePlayer offlineTarget = mock(OfflinePlayer.class);
    Player onlineTarget = mock(Player.class);
    GroupEntry groupEntry = mock(GroupEntry.class);
    UUID targetUuid = UUID.randomUUID();

    when(groupEntry.getPrefix()).thenReturn("§c");
    when(groupEntry.getName()).thenReturn("admin");
    when(offlineTarget.getPlayer()).thenReturn(onlineTarget);
    when(offlineTarget.isOnline()).thenReturn(true);
    when(offlineTarget.getName()).thenReturn("TargetPlayer");
    when(offlineTarget.getUniqueId()).thenReturn(targetUuid);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(onlineTarget)).thenReturn(new PlayerEntry());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.resolveGroupWithFallback("admin")).thenReturn(groupEntry);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getPlayer(targetUuid)).thenReturn(onlineTarget);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP, "§cadmin", "TargetPlayer"))
        .thenReturn("group set");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("TargetPlayer")).thenReturn(offlineTarget);

      boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"TargetPlayer", "admin"});

      assertTrue(result);
      verify(playerService).updateGroup(offlineTarget, groupEntry);
      verify(sender).sendMessage("group set");
      verify(onlineTarget).sendMessage("group set");
    }
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotAuthorized() {
    Player sender = mock(Player.class);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(sender, "mod")).thenReturn(false);

    List<String> result = permissionsGroup.onTabComplete(sender, command, "setGroup", new String[]{"partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanTwoArgsAlreadyEntered() {
    Player sender = mock(Player.class);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(sender, "mod")).thenReturn(true);

    List<String> result = permissionsGroup.onTabComplete(sender, command, "setGroup", new String[]{"Player", "mod", "extra"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenFirstArgIsBeingEntered() {
    Player sender = mock(Player.class);
    Player onlinePlayer1 = mock(Player.class);
    Player onlinePlayer2 = mock(Player.class);

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(sender, "mod")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    doReturn(List.of(onlinePlayer1, onlinePlayer2)).when(serverService).getOnlinePlayers();
    when(onlinePlayer1.getName()).thenReturn("Alice");
    when(onlinePlayer2.getName()).thenReturn("Bob");

    List<String> result = permissionsGroup.onTabComplete(sender, command, "setGroup", new String[]{"Al"});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("Alice"));
    assertTrue(result.contains("Bob"));
  }

  @Test
  void onTabCompleteReturnsGroupNamesWhenSecondArgIsBeingEntered() {
    Player sender = mock(Player.class);
    GroupEntry group1 = mock(GroupEntry.class);
    GroupEntry group2 = mock(GroupEntry.class);

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(sender, "mod")).thenReturn(true);
    when(groupService.findAllGroups()).thenReturn(List.of(group1, group2));

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper> tabCompleterHelperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper.class)) {
      tabCompleterHelperMock.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper.getGroups(List.of(group1, group2)))
          .thenReturn(List.of("mod", "admin"));

      List<String> result = permissionsGroup.onTabComplete(sender, command, "setGroup", new String[]{"Alice", "mo"});

      assertNotNull(result);
      assertEquals(2, result.size());
      assertTrue(result.contains("mod"));
      assertTrue(result.contains("admin"));
    }
  }

  @Test
  void onCommandDoesNotNotifyOfflineTargetWhenServerServiceReturnsNull() {
    Player sender = mock(Player.class);
    OfflinePlayer offlineTarget = mock(OfflinePlayer.class);
    Player onlineTarget = mock(Player.class);
    GroupEntry groupEntry = mock(GroupEntry.class);
    UUID targetUuid = UUID.randomUUID();

    when(groupEntry.getPrefix()).thenReturn("§a");
    when(groupEntry.getName()).thenReturn("mod");
    when(offlineTarget.getPlayer()).thenReturn(onlineTarget);
    when(offlineTarget.isOnline()).thenReturn(true);
    when(offlineTarget.getName()).thenReturn("TargetPlayer");
    when(offlineTarget.getUniqueId()).thenReturn(targetUuid);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(onlineTarget)).thenReturn(new PlayerEntry());

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.resolveAuthorizedGroup(sender, "mod")).thenReturn(Optional.of(groupEntry));
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getPlayer(targetUuid)).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP, "§amod", "TargetPlayer"))
        .thenReturn("group set");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("TargetPlayer")).thenReturn(offlineTarget);

      boolean result = permissionsGroup.onCommand(sender, command, "setGroup", new String[]{"TargetPlayer", "mod"});

      assertTrue(result);
      verify(playerService).updateGroup(offlineTarget, groupEntry);
      verify(sender).sendMessage("group set");
      verify(onlineTarget, never()).sendMessage("group set");
    }
  }

  @Test
  void onCommandReturnsFalseWhenSenderIsUnknownType() {
    CommandSender unknownSender = mock(CommandSender.class);
    OfflinePlayer offlineTarget = mock(OfflinePlayer.class);
    Player onlineTarget = mock(Player.class);

    when(offlineTarget.getPlayer()).thenReturn(onlineTarget);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(onlineTarget)).thenReturn(new PlayerEntry());

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getOfflinePlayer("TargetPlayer")).thenReturn(offlineTarget);

      boolean result = permissionsGroup.onCommand(unknownSender, command, "setGroup", new String[]{"TargetPlayer", "mod"});

      assertFalse(result);
    }
  }
}