package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND_BLOCK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SPACER_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrintTest {

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

  private Print print;

  @BeforeEach
  void setUp() {
    print = new Print();
    print.injectContext(serviceContext);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
  }

  @Test
  void onCommandReturnsEmptySubCommands() {
    CommandsEnum[] commands = print.getCommands();
    assertNotNull(commands);
    assertEquals(0, commands.length);
  }

  @Test
  void onTabCompleteReturnsEmptyList() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    List<String> result = print.onTabComplete(sender, command, "print", new String[]{});
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onCommandSendsInfoMessageWhenNoArgsProvided() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PRINT_INFO)).thenReturn("info");

    boolean result = print.onCommand(sender, command, "print", new String[]{});

    assertTrue(result);
    verify(sender).sendMessage("info");
  }

  @Test
  void onCommandBroadcastsMessageFromConsole() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    when(sender.getName()).thenReturn("console");
    when(serviceContext.getServerService()).thenReturn(serverService);

    boolean result = print.onCommand(sender, command, "print", new String[]{"Hello"});

    assertTrue(result);
    verify(serverService).broadcastMessage(
        PLUGIN_COLOR_CONSOLE + "Console" + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + "Hello"
    );
  }

  @Test
  void onCommandBroadcastsMessageFromPlayer() {
    Player sender = mock(Player.class);
    when(sender.getCustomName()).thenReturn("§aPlayerName");
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(sender, "mod")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);

    boolean result = print.onCommand(sender, command, "print", new String[]{"Hello"});

    assertTrue(result);
    verify(serverService).broadcastMessage(
        "§aPlayerName" + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + "Hello"
    );
  }

  @Test
  void onCommandSendsPermissionMissingMessageWhenPlayerIsNotMod() {
    Player sender = mock(Player.class);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(sender, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn("no permission");

    boolean result = print.onCommand(sender, command, "print", new String[]{"Hello"});

    assertTrue(result);
    verify(sender).sendMessage("no permission");
    verify(serviceContext, never()).getServerService();
  }

  @Test
  void onCommandBroadcastsMessageFromCommandBlock() {
    BlockCommandSender sender = mock(BlockCommandSender.class);
    when(sender.getName()).thenReturn("@CommandBlock");
    when(serviceContext.getServerService()).thenReturn(serverService);

    boolean result = print.onCommand(sender, command, "print", new String[]{"Hello"});

    assertTrue(result);
    verify(serverService).broadcastMessage(
        PLUGIN_COLOR_COMMAND_BLOCK + "@CommandBlock" + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + "Hello"
    );
  }

  @Test
  void onCommandReplacesAtPSelectorWithNearestPlayerFromCommandBlock() {
    BlockCommandSender sender = mock(BlockCommandSender.class);
    when(sender.getName()).thenReturn("@CommandBlock");

    Block block = mock(Block.class);
    Location location = mock(Location.class);
    when(sender.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);

    CommandBlock commandBlock = mock(CommandBlock.class);
    when(block.getState()).thenReturn(commandBlock);
    when(commandBlock.getBlock()).thenReturn(block);

    Player nearestPlayer = mock(Player.class);
    when(nearestPlayer.getCustomName()).thenReturn("§aNearestPlayer");

    when(serviceContext.getServerService()).thenReturn(serverService);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> playerHelperMock =
        Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getTargetedPlayer(location)).thenReturn(nearestPlayer);

      boolean result = print.onCommand(sender, command, "print", new String[]{"Hello", "@p"});

      assertTrue(result);
      verify(serverService).broadcastMessage(
          PLUGIN_COLOR_COMMAND_BLOCK + "@CommandBlock" + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + "Hello §aNearestPlayer"
      );
    }
  }

  @Test
  void onCommandSendsNoPlayerInReachMessageWhenAtPSelectorFindsNoPlayer() {
    BlockCommandSender sender = mock(BlockCommandSender.class);
    when(sender.getName()).thenReturn("@CommandBlock");

    Block block = mock(Block.class);
    Location location = mock(Location.class);
    when(sender.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);

    CommandBlock commandBlock = mock(CommandBlock.class);
    when(block.getState()).thenReturn(commandBlock);
    when(commandBlock.getBlock()).thenReturn(block);

    when(translationService.get(MessageKey.COMMAND_NO_PLAYER_IN_REACH)).thenReturn("no player in reach");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "no player in reach"))
        .thenReturn("target error");

    try (MockedStatic<PlayerHelper> playerHelperMock = Mockito.mockStatic(PlayerHelper.class)) {
      playerHelperMock.when(() -> PlayerHelper.getTargetedPlayer(location)).thenReturn(null);

      boolean result = print.onCommand(sender, command, "print", new String[]{"Hello", "@p"});

      assertTrue(result);
      verify(sender).sendMessage("target error");
      verify(serviceContext, never()).getServerService();
    }
  }

  @Test
  void onCommandSendsInvalidMessageForUnknownSenderType() {
    ConsoleCommandSender unknownSender = mock(ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_INVALID)).thenReturn("invalid");

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper> typeHelperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.class)) {
      typeHelperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCmdBlock(unknownSender)).thenReturn(false);
      typeHelperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole(unknownSender)).thenReturn(false);
      typeHelperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer(unknownSender)).thenReturn(false);

      boolean result = print.onCommand(unknownSender, command, "print", new String[]{"Hello"});

      assertTrue(result);
      verify(unknownSender).sendMessage("invalid");
      verify(serverService, never()).broadcastMessage(anyString());
    }
  }

  @Test
  void onCommandBroadcastsMultiWordMessageFromConsole() {
    ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
    when(sender.getName()).thenReturn("console");
    when(serviceContext.getServerService()).thenReturn(serverService);

    boolean result = print.onCommand(sender, command, "print", new String[]{"Hello", "World"});

    assertTrue(result);
    verify(serverService).broadcastMessage(
        PLUGIN_COLOR_CONSOLE + "Console" + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + "Hello World"
    );
  }
}