package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TabCompleterService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldMenuService;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorldsTest {

  private static final Logger SILENT_LOGGER = Logger.getLogger("test-silent");
  @SuppressWarnings("LoggerInitializedWithForeignClass")
  private static final Logger WORLDS_CLASS_LOGGER = Logger.getLogger(Worlds.class.getName());
  @Mock
  private ServiceContext serviceContext;
  @Mock
  private TranslationService translationService;
  @Mock
  private GroupService groupService;
  @Mock
  private TeleportService teleportService;
  @Mock
  private WorldMenuService worldMenuService;
  @Mock
  private ServerService serverService;
  @Mock
  private TabCompleterService tabCompleterService;
  @Mock
  private Player player;
  @Mock
  private Command command;
  private Worlds worlds;

  @BeforeAll
  static void setUpServer() {
    SILENT_LOGGER.setLevel(Level.OFF);
    WORLDS_CLASS_LOGGER.setLevel(Level.OFF);
    Server server = mock(Server.class);
    when(server.getLogger()).thenReturn(SILENT_LOGGER);
    when(server.getWorld(anyString())).thenReturn(null);
    Bukkit.setServer(server);
  }

  @AfterAll
  static void tearDownBukkitServer() throws ReflectiveOperationException {
    resetBukkitServer();
  }

  private static void resetBukkitServer() throws ReflectiveOperationException {
    Field serverField = Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, (Server) null);
  }

  @BeforeEach
  void setUp() {
    worlds = new Worlds();
    worlds.injectContext(serviceContext);

    lenient()
        .when(serviceContext.getTranslationService())
        .thenReturn(translationService);
    lenient()
        .when(serviceContext.getGroupService())
        .thenReturn(groupService);
  }

  @Test
  void onCommandSenderIsNotPlayerSendsNotaPlayerMessage() {
    org.bukkit.command.ConsoleCommandSender consoleSender = mock(org.bukkit.command.ConsoleCommandSender.class);
    String expectedMessage = "not a player";
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(consoleSender, command, "world", new String[] {});

    assertTrue(result);
    verify(consoleSender).sendMessage(expectedMessage);
  }

  @Test
  void onCommandPlayerLacksUserPermissionSendsPermissionMissingMessage() {
    String expectedMessage = "permission missing";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(player, command, "world", new String[] {});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandNoArgsOpensWorldMenuAndSendsInfoMessage() {
    String expectedMessage = "world info";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_WORLD_INFO), anyString(), anyString(), anyString(),
        anyString(), anyString())).thenReturn(expectedMessage);
    when(serviceContext.getWorldMenuService()).thenReturn(worldMenuService);

    boolean result = worlds.onCommand(player, command, "world", new String[] {});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
    verify(worldMenuService).openWorldMenu(player);
  }

  @Test
  void onCommandOneArgNotListTeleportsPlayer() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getTeleportService()).thenReturn(teleportService);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"someWorld"});

    assertTrue(result);
    verify(teleportService).teleportWorld(player, "someWorld");
  }

  @Test
  void onCommandOneArgListWithoutModPermissionSendsPermissionMissingMessage() {
    String expectedMessage = "permission missing";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"list"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandOneArgListWithModPermissionListsWorlds() {
    String expectedMessage = "world list info";
    World world1 = mock(World.class);
    World world2 = mock(World.class);
    when(world1.getName()).thenReturn("world");
    when(world2.getName()).thenReturn("world_nether");

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_INFO)).thenReturn(expectedMessage);

    de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService pluginMetadataService =
        mock(de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService.class);
    org.bukkit.plugin.Plugin plugin = mock(org.bukkit.plugin.Plugin.class);
    org.bukkit.Server server = mock(org.bukkit.Server.class);

    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getWorlds()).thenReturn(List.of(world1, world2));

    boolean result = worlds.onCommand(player, command, "world", new String[] {"list"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
    verify(player).sendMessage("world");
    verify(player).sendMessage("world_nether");
  }

  @Test
  void onCommandTwoArgsWithoutAdminPermissionSendsPermissionMissingMessage() {
    String expectedMessage = "permission missing";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"load", "someWorld"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandTwoArgsUnloadWithSaveSendsUnloadMessage() throws WorldNotLoadedException {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    doNothing().when(serverService).unloadWorld("nonexistentWorld", true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_UNLOAD)).thenReturn("world unloaded");

    boolean result = worlds.onCommand(player, command, "world", new String[] {"unload", "nonexistentWorld"});

    assertTrue(result);
  }

  @Test
  void onCommandTwoArgsUnloadNoSaveSendsUnloadNoSaveMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"unloadNoSave", "nonexistentWorld"});

    assertTrue(result);
  }

  @Test
  void unloadWorldWithSaveAndLoadedWorldSendsUnloadMessage() throws WorldNotLoadedException {
    String expectedMessage = "world unloaded";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_UNLOAD)).thenReturn(expectedMessage);
    doNothing().when(serverService).unloadWorld("loadedWorld", true);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"unload", "loadedWorld"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void unloadWorldWithoutSaveAndLoadedWorldSendsUnloadNoSaveMessage() throws WorldNotLoadedException {
    String expectedMessage = "world unloaded no save";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_UNLOAD_NO_SAVE)).thenReturn(expectedMessage);
    doNothing().when(serverService).unloadWorld("loadedWorld", false);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"unloadNoSave", "loadedWorld"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void unloadWorldWithNonexistentWorldLogsWorldNotLoadedAndDoesNotMessagePlayer() throws WorldNotLoadedException {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_NOT_LOADED)).thenReturn("world not loaded");
    doThrow(new de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException("not loaded"))
        .when(serverService).unloadWorld("nonexistentWorld", true);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"unload", "nonexistentWorld"});

    assertTrue(result);
    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void unloadWorldNoSaveWithNonexistentWorldLogsWorldNotLoadedAndDoesNotMessagePlayer() throws WorldNotLoadedException {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_NOT_LOADED)).thenReturn("world not loaded");
    doThrow(new de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException("not loaded"))
        .when(serverService).unloadWorld("nonexistentWorld", false);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"unloadNoSave", "nonexistentWorld"});

    assertTrue(result);
    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void onCommandTwoArgsCreateSendsCreateInfoMessage() {
    String expectedMessage = "create info";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_CREATE_INFO)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"create", "myWorld"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandTwoArgsUnknownSubCommandSendsWrongSubCommandMessage() {
    String expectedMessage = "wrong sub command";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"unknown", "someWorld"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandMoreThanFiveArgsSendsToLessArgumentsMessage() {
    String expectedMessage = "too less arguments";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(expectedMessage);

    boolean result =
        worlds.onCommand(player, command, "world", new String[] {"create", "n", "FLAT", "NORMAL", "true", "extra"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandFiveArgsNotCreateSubCommandSendsWrongSubCommandMessage() {
    String expectedMessage = "wrong sub command";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(expectedMessage);

    boolean result =
        worlds.onCommand(player, command, "world", new String[] {"load", "myWorld", "FLAT", "NORMAL", "true"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandFiveArgsCreateWithInvalidArgumentsSendsWrongArgumentsMessage() {
    String expectedMessage = "wrong arguments";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_WRONG_ARGUMENTS)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(player, command, "world",
        new String[] {"create", "myWorld", "INVALID_TYPE", "INVALID_ENV", "notABoolean"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onTabCompleteWithoutModPermissionReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);

    List<String> result = worlds.onTabComplete(player, command, "world", new String[] {"li"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteMoreThanFiveStringsReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = worlds.onTabComplete(player, command, "world",
        new String[] {"create", "name", "FLAT", "NORMAL", "true", "extra"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteFirstArgReturnsCommandsAndWorldNames() {
    World world = mock(World.class);
    when(world.getName()).thenReturn("world");
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serviceContext.getTabCompleterService()).thenReturn(tabCompleterService);
    when(serverService.getWorlds()).thenReturn(List.of(world));
    when(tabCompleterService.getCommands(Worlds.Commands.values()))
        .thenReturn(List.of(
            Worlds.Commands.CREATE.getName(),
            Worlds.Commands.LOAD.getName(),
            Worlds.Commands.LIST.getName(),
            Worlds.Commands.UNLOAD.getName(),
            Worlds.Commands.UNLOAD_NO_SAVE.getName()
        ));

    List<String> result = worlds.onTabComplete(player, command, "world", new String[] {"w"});

    assertFalse(result.isEmpty());
    assertTrue(result.contains("world"));
    assertTrue(result.contains(Worlds.Commands.CREATE.getName()));
    assertTrue(result.contains(Worlds.Commands.LOAD.getName()));
    assertTrue(result.contains(Worlds.Commands.LIST.getName()));
    assertTrue(result.contains(Worlds.Commands.UNLOAD.getName()));
    assertTrue(result.contains(Worlds.Commands.UNLOAD_NO_SAVE.getName()));
  }

  @Test
  void onTabCompleteSecondArgUnloadReturnsLoadedWorldNames() {
    World world = mock(World.class);
    when(world.getName()).thenReturn("world");
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getWorlds()).thenReturn(List.of(world));

    List<String> result = worlds.onTabComplete(player, command, "world", new String[] {"unload", ""});

    assertTrue(result.contains("world"));
  }

  @Test
  void onTabCompleteSecondArgCreateReturnsNamePlaceholder() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = worlds.onTabComplete(player, command, "world", new String[] {"create", ""});

    assertTrue(result.contains("<enter name>"));
  }

  @Test
  void onTabCompleteThirdArgCreateReturnsWorldTypes() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getTabCompleterService()).thenReturn(tabCompleterService);
    when(tabCompleterService.getWorldTypes()).thenReturn(List.of("NORMAL", "FLAT", "LARGE_BIOMES", "AMPLIFIED"));

    List<String> result = worlds.onTabComplete(player, command, "world", new String[] {"create", "myWorld", ""});

    assertFalse(result.isEmpty());
  }

  @Test
  void onTabCompleteFourthArgCreateReturnsEnvironmentTypes() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getTabCompleterService()).thenReturn(tabCompleterService);
    when(tabCompleterService.getWorldEnvironmentTypes()).thenReturn(List.of("NORMAL", "NETHER", "THE_END"));

    List<String> result =
        worlds.onTabComplete(player, command, "world", new String[] {"create", "myWorld", "FLAT", ""});

    assertFalse(result.isEmpty());
  }

  @Test
  void onTabCompleteFifthArgCreateReturnsBooleanOptions() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result =
        worlds.onTabComplete(player, command, "world", new String[] {"create", "myWorld", "FLAT", "NORMAL", ""});

    assertEquals(2, result.size());
    assertTrue(result.contains("true"));
    assertTrue(result.contains("false"));
  }

  @Test
  void onTabCompleteSecondArgNeitherUnloadNorCreateReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = worlds.onTabComplete(player, command, "world", new String[] {"list", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void getCommandsReturnsAllWorldCommands() {
    Worlds.Commands[] commands = (Worlds.Commands[]) worlds.getCommands();

    assertEquals(Worlds.Commands.values().length, commands.length);
  }

  @ParameterizedTest
  @ValueSource(strings = {"create", "load", "list", "unload", "unloadNoSave"})
  void commandsEnumGetNameReturnsCorrectName(String expectedName) {
    Worlds.Commands found = java.util.Arrays
        .stream(Worlds.Commands.values())
        .filter(c -> c
            .getName()
            .equals(expectedName))
        .findFirst()
        .orElseThrow();

    assertEquals(expectedName, found.getName());
  }

  @Test
  void commandsEnumSubCommandsDefaultToEmpty() {
    for (Worlds.Commands cmd : Worlds.Commands.values()) {
      assertEquals(0, cmd.getSubCommands().length);
    }
  }

  @Test
  void onCommandReturnsTrue() {
    org.bukkit.command.ConsoleCommandSender consoleSender = mock(org.bukkit.command.ConsoleCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn("msg");

    boolean result = worlds.onCommand(consoleSender, command, "world", new String[] {});

    assertTrue(result);
  }

  @Test
  void onCommandPlayerWithUserPermissionAndNoArgsDoesNotTeleport() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getWorldMenuService()).thenReturn(worldMenuService);
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_WORLD_INFO), anyString(), anyString(), anyString(),
        anyString(), anyString())).thenReturn("info");

    worlds.onCommand(player, command, "world", new String[] {});

    verify(teleportService, never()).teleportWorld(any(), anyString());
  }

  @Test
  void onCommandFiveArgsCreateWithValidArgumentsCreatesWorldAndSendsCreateMessage() {
    String expectedMessage = "world created";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_CREATE)).thenReturn(expectedMessage);

    boolean result =
        worlds.onCommand(player, command, "world", new String[] {"create", "myNewWorld", "FLAT", "NORMAL", "true"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandFiveArgsCreateWithInvalidWorldTypeAndValidEnvironmentSendsWrongArgumentsMessage() {
    String expectedMessage = "wrong arguments";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_WRONG_ARGUMENTS)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(player, command, "world",
        new String[] {"create", "myWorld", "INVALID_TYPE", "NORMAL", "true"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandFiveArgsCreateWithValidWorldTypeAndInvalidEnvironmentSendsWrongArgumentsMessage() {
    String expectedMessage = "wrong arguments";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_WRONG_ARGUMENTS)).thenReturn(expectedMessage);

    boolean result =
        worlds.onCommand(player, command, "world", new String[] {"create", "myWorld", "FLAT", "INVALID_ENV", "true"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandFiveArgsCreateWithValidArgumentsButFalseBooleanSendsWrongArgumentsMessage() {
    String expectedMessage = "wrong arguments";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_WRONG_ARGUMENTS)).thenReturn(expectedMessage);

    boolean result =
        worlds.onCommand(player, command, "world", new String[] {"create", "myWorld", "FLAT", "NORMAL", "false"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandTwoArgsLoadWithExistingWorldSendsWorldLoadMessage() {
    String expectedMessage = "world loaded";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_LOAD)).thenReturn(expectedMessage);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"load", "existingWorld"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandTwoArgsLoadWithNonExistentWorldDoesNotSendLoadMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);

    boolean result = worlds.onCommand(player, command, "world", new String[] {"load", "nonExistentWorld"});

    assertTrue(result);
    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void onCommandFiveArgsCreateWithNetherEnvironmentCreatesWorldAndSendsCreateMessage() {
    String expectedMessage = "world created";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_CREATE)).thenReturn(expectedMessage);

    boolean result =
        worlds.onCommand(player, command, "world", new String[] {"create", "myNether", "FLAT", "NETHER", "true"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandFiveArgsCreateWithTheEndEnvironmentCreatesWorldAndSendsCreateMessage() {
    String expectedMessage = "world created";
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_CREATE)).thenReturn(expectedMessage);

    boolean result =
        worlds.onCommand(player, command, "world", new String[] {"create", "myEnd", "FLAT", "THE_END", "true"});

    assertTrue(result);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void onCommandCmdBlockWithAtpAndNoPlayerInReachSendsTargetNotaPlayerMessage() {
    BlockCommandSender blockCommandSender = mock(BlockCommandSender.class);
    Block block = mock(Block.class);
    CommandBlock commandBlock = mock(CommandBlock.class);
    Location location = mock(Location.class);
    World world = mock(World.class);

    when(blockCommandSender.getBlock()).thenReturn(block);
    when(block.getState()).thenReturn(commandBlock);
    when(commandBlock.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);
    when(location.getWorld()).thenReturn(world);
    when(world.getPlayers()).thenReturn(List.of());
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER)).thenReturn("target: %s");

    boolean result = worlds.onCommand(blockCommandSender, command, "world", new String[] {"someWorld", "@p"});

    assertTrue(result);
    verify(blockCommandSender).sendMessage("target: No Player in Reach");
  }

  @Test
  void onCommandCmdBlockWithAtpAndPlayerInReachTeleportsNearestPlayer() {
    BlockCommandSender blockCommandSender = mock(BlockCommandSender.class);
    Block block = mock(Block.class);
    CommandBlock commandBlock = mock(CommandBlock.class);
    Location commandBlockLocation = mock(Location.class);
    Location playerLocation = mock(Location.class);
    World world = mock(World.class);
    Player nearestPlayer = mock(Player.class);

    when(blockCommandSender.getBlock()).thenReturn(block);
    when(block.getState()).thenReturn(commandBlock);
    when(commandBlock.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(commandBlockLocation);
    when(commandBlockLocation.getWorld()).thenReturn(world);
    when(world.getPlayers()).thenReturn(List.of(nearestPlayer));
    when(nearestPlayer.getLocation()).thenReturn(playerLocation);
    when(commandBlockLocation.distanceSquared(playerLocation)).thenReturn(4.0);
    when(serviceContext.getTeleportService()).thenReturn(teleportService);

    boolean result = worlds.onCommand(blockCommandSender, command, "world", new String[] {"someWorld", "@p"});

    assertTrue(result);
    verify(teleportService).teleportWorld(nearestPlayer, "someWorld");
  }

  @Test
  void onCommandCmdBlockWithListSubCommandFallsThroughToNotaPlayerMessage() {
    BlockCommandSender blockCommandSender = mock(BlockCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn("not a player");

    boolean result = worlds.onCommand(blockCommandSender, command, "world", new String[] {"list", "@p"});

    assertTrue(result);
    verify(blockCommandSender).sendMessage("not a player");
  }

  @Test
  void onCommandCmdBlockWithNonAtpSecondArgFallsThroughToNotaPlayerMessage() {
    BlockCommandSender blockCommandSender = mock(BlockCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn("not a player");

    boolean result = worlds.onCommand(blockCommandSender, command, "world", new String[] {"someWorld", "notAtP"});

    assertTrue(result);
    verify(blockCommandSender).sendMessage("not a player");
  }

  @Test
  void onCommandCmdBlockWithWrongArgCountFallsThroughToNotaPlayerMessage() {
    BlockCommandSender blockCommandSender = mock(BlockCommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn("not a player");

    boolean result = worlds.onCommand(blockCommandSender, command, "world", new String[] {"someWorld"});

    assertTrue(result);
    verify(blockCommandSender).sendMessage("not a player");
  }

  @Test
  void onTabCompleteThirdArgNotCreateReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = worlds.onTabComplete(player, command, "world", new String[] {"load", "myWorld", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteFourthArgNotCreateReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = worlds.onTabComplete(player, command, "world", new String[] {"load", "myWorld", "FLAT", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteFifthArgNotCreateReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result =
        worlds.onTabComplete(player, command, "world", new String[] {"load", "myWorld", "FLAT", "NORMAL", ""});

    assertTrue(result.isEmpty());
  }

}