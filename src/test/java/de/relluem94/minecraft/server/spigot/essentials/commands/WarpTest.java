package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TabCompleterService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.WarpService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WarpTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private TranslationService translationService;
  @Mock
  private GroupService groupService;
  @Mock
  private PlayerService playerService;
  @Mock
  private WarpService warpService;
  @Mock
  private TeleportService teleportService;
  @Mock
  private TabCompleterService tabCompleterService;
  @Mock
  private Player player;
  @Mock
  private CommandSender nonPlayerSender;
  @Mock
  private Command command;
  @Mock
  private World world;

  private Warp warpCommand;
  private PlayerEntry playerEntry;

  @BeforeEach
  void setUp() {
    warpCommand = new Warp();
    warpCommand.injectContext(serviceContext);

    playerEntry = new PlayerEntry();
    playerEntry.setId(1);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getWarpService()).thenReturn(warpService);
    lenient().when(player.getWorld()).thenReturn(world);
  }

  @Test
  void onCommandSenderIsNotPlayerSendsNotaPlayerMessage() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(
        "not a player");

    boolean result = warpCommand.onCommand(nonPlayerSender, command, "warp", new String[]{});

    verify(nonPlayerSender).sendMessage("not a player");
    assertTrue(result);
  }

  @Test
  void onCommandPlayerNotAuthorizedSendsPermissionMissingMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(
        "no permission");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{});

    verify(player).sendMessage("no permission");
    assertTrue(result);
  }

  @Test
  void onCommandNoArgsSendsWarpListInfo() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(warpService.findWarpsByWorld(world)).thenReturn(new ArrayList<>());
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_LIST_INFO)).thenReturn(
        "warp list:");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{});

    verify(player).sendMessage("warp list:");
    assertTrue(result);
  }

  @Test
  void onCommandNoArgsSendsEachWarpName() {
    LocationEntry warp1 = buildLocationEntry("spawn");
    LocationEntry warp2 = buildLocationEntry("market");

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(warpService.findWarpsByWorld(world)).thenReturn(List.of(warp1, warp2));
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_LIST_INFO)).thenReturn(
        "warp list:");
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_LIST, "spawn")).thenReturn(
        "- spawn");
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_LIST, "market")).thenReturn(
        "- market");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{});

    verify(player).sendMessage("- spawn");
    verify(player).sendMessage("- market");
    assertTrue(result);
  }

  @Test
  void onCommandOneArgWarpsToExistingWarp() {
    Location location = buildBukkitLocation();
    LocationEntry warpEntry = buildMockedLocationEntry(location);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(warpService.findWarpByNameAndWorld("spawn", world)).thenReturn(Optional.of(warpEntry));
    when(serviceContext.getTeleportService()).thenReturn(teleportService);

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{"spawn"});

    verify(teleportService).teleportWarp(player, location);
    assertTrue(result);
  }

  @Test
  void onCommandOneArgWarpNotFoundSendsNoWarpFoundMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(warpService.findWarpByNameAndWorld("ghost", world)).thenReturn(Optional.empty());
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ERROR_NO_WARP_FOUND)).thenReturn(
        "no warp found");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{"ghost"});

    verify(player).sendMessage("no warp found");
    assertTrue(result);
  }

  @Test
  void onCommandOneArgWarpLocationNullSendsWorldUnloadedMessage() {
    LocationEntry warpEntry = buildMockedLocationEntry(null);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(warpService.findWarpByNameAndWorld("spawn", world)).thenReturn(Optional.of(warpEntry));
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ERROR_WORLD_UNLOADED)).thenReturn(
        "world unloaded");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{"spawn"});

    verify(player).sendMessage("world unloaded");
    assertTrue(result);
  }

  @Test
  void onCommandOneArgWarpLocationWorldNullSendsWorldUnloadedMessage() {
    Location locationWithNullWorld = new Location(null, 0, 64, 0);
    LocationEntry warpEntry = buildMockedLocationEntry(locationWithNullWorld);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(warpService.findWarpByNameAndWorld("spawn", world)).thenReturn(Optional.of(warpEntry));
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ERROR_WORLD_UNLOADED)).thenReturn(
        "world unloaded");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{"spawn"});

    verify(player).sendMessage("world unloaded");
    assertTrue(result);
  }

  @Test
  void onCommandTwoArgsAddNotAdminSendsPermissionMissingMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(
        "no permission");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{"add", "spawn"});

    verify(player).sendMessage("no permission");
    assertTrue(result);
  }

  @Test
  void onCommandTwoArgsAddAsAdminSuccessfullyAddsWarp() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(warpService.addWarp("spawn", player, playerEntry.getId())).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ADD, "spawn")).thenReturn(
        "warp added");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{"add", "spawn"});

    verify(player).sendMessage("warp added");
    assertTrue(result);
  }

  @Test
  void onCommandTwoArgsAddAsAdminWarpAlreadyExistsSendsAlreadyExistsMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(warpService.addWarp("spawn", player, playerEntry.getId())).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ERROR_ALREADY_EXISTS,
        "spawn")).thenReturn("already exists");

    boolean result = warpCommand.onCommand(player, command, "warp", new String[]{"add", "spawn"});

    verify(player).sendMessage("already exists");
    assertTrue(result);
  }

  @Test
  void onCommandTwoArgsRemoveAsAdminSuccessfullyRemovesWarp() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(warpService.removeWarp("spawn")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_REMOVE, "spawn")).thenReturn(
        "warp removed");

    boolean result = warpCommand.onCommand(player, command, "warp",
        new String[]{"remove", "spawn"});

    verify(player).sendMessage("warp removed");
    assertTrue(result);
  }

  @Test
  void onCommandTwoArgsRemoveAsAdminWarpNotFoundSendsNotDeletedMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(warpService.removeWarp("ghost")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ERROR_WARP_NOT_DELETED_NOT_FOUND,
        "ghost")).thenReturn("not deleted");

    boolean result = warpCommand.onCommand(player, command, "warp",
        new String[]{"remove", "ghost"});

    verify(player).sendMessage("not deleted");
    assertTrue(result);
  }

  @Test
  void onCommandTwoArgsUnknownSubCommandSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(
        "wrong sub command");

    boolean result = warpCommand.onCommand(player, command, "warp",
        new String[]{"unknown", "spawn"});

    verify(player).sendMessage("wrong sub command");
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"add", "ADD", "Add"})
  void onCommandAddIsCaseInsensitive(String subCommand) {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(warpService.addWarp("spawn", player, playerEntry.getId())).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ADD, "spawn")).thenReturn(
        "warp added");

    boolean result = warpCommand.onCommand(player, command, "warp",
        new String[]{subCommand, "spawn"});

    verify(warpService).addWarp("spawn", player, playerEntry.getId());
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"remove", "REMOVE", "Remove"})
  void onCommandRemoveIsCaseInsensitive(String subCommand) {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(warpService.removeWarp("spawn")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WARP_REMOVE, "spawn")).thenReturn(
        "warp removed");

    boolean result = warpCommand.onCommand(player, command, "warp",
        new String[]{subCommand, "spawn"});

    verify(warpService).removeWarp("spawn");
    assertTrue(result);
  }

  @Test
  void onTabCompleteUnauthorizedReturnsEmptyList() {
    when(groupService.isSenderAuthorized(nonPlayerSender, "user")).thenReturn(false);

    List<String> result = warpCommand.onTabComplete(nonPlayerSender, command, "warp",
        new String[]{"s"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteNonPlayerReturnsEmptyList() {
    when(groupService.isSenderAuthorized(nonPlayerSender, "user")).thenReturn(true);

    List<String> result = warpCommand.onTabComplete(nonPlayerSender, command, "warp",
        new String[]{"s"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteFirstArgAsNonAdminReturnsOnlyWarpNames() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(false);
    when(warpService.getWarpNamesByWorld(world)).thenReturn(List.of("spawn", "market"));

    List<String> result = warpCommand.onTabComplete(player, command, "warp", new String[]{"s"});

    assertTrue(result.contains("spawn"));
    assertTrue(result.contains("market"));
    assertTrue(result.stream().noneMatch(s -> s.equals("add") || s.equals("remove")));
  }

  @Test
  void onTabCompleteFirstArgAsAdminReturnsSubCommandsAndWarpNames() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getTabCompleterService()).thenReturn(tabCompleterService);
    when(tabCompleterService.getCommands(Warp.Commands.values())).thenReturn(List.of("add", "remove"));

    List<String> result = warpCommand.onTabComplete(player, command, "warp", new String[]{"s"});

    assertTrue(result.contains("add"));
    assertTrue(result.contains("remove"));
    assertFalse(result.contains("spawn"));
  }

  @Test
  void onTabCompleteSecondArgAfterRemoveAsAdminReturnsWarpNames() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(warpService.getWarpNamesByWorld(world)).thenReturn(List.of("spawn", "market"));

    List<String> result = warpCommand.onTabComplete(player, command, "warp",
        new String[]{"remove", ""});

    assertTrue(result.contains("spawn"));
    assertTrue(result.contains("market"));
  }

  @Test
  void onTabCompleteSecondArgAfterNonRemoveSubCommandReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);

    List<String> result = warpCommand.onTabComplete(player, command, "warp",
        new String[]{"add", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteSecondArgAsNonAdminReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(false);

    List<String> result = warpCommand.onTabComplete(player, command, "warp",
        new String[]{"remove", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteThirdArgReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = warpCommand.onTabComplete(player, command, "warp",
        new String[]{"remove", "spawn", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void getCommandsReturnsAllWarpSubCommands() {
    Warp.Commands[] result = (Warp.Commands[]) warpCommand.getCommands();

    assertEquals(Warp.Commands.values().length, result.length);
    assertTrue(List.of(result).contains(Warp.Commands.ADD));
    assertTrue(List.of(result).contains(Warp.Commands.REMOVE));
  }

  @Test
  void onCommandMoreThanTwoArgsReturnsFalse() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    boolean result = warpCommand.onCommand(player, command, "warp",
        new String[]{"add", "spawn", "extra"});

    verify(player, never()).sendMessage(anyString());
    assertFalse(result);
  }

  private LocationEntry buildMockedLocationEntry(Location location) {
    LocationEntry entry = org.mockito.Mockito.mock(LocationEntry.class);
    lenient().when(entry.getLocationName()).thenReturn("spawn");
    when(entry.getLocation()).thenReturn(location);
    return entry;
  }

  private LocationEntry buildLocationEntry(String name) {
    LocationEntry entry = new LocationEntry();
    entry.setLocationName(name);
    entry.setWorld("world");
    entry.setX(0);
    entry.setY(64);
    entry.setZ(0);
    entry.setYaw(0f);
    entry.setPitch(0f);
    return entry;
  }

  private Location buildBukkitLocation() {
    return new Location(world, 0, 64, 0, 0f, 0f);
  }
}