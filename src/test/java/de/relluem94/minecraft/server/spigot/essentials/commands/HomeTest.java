package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.commands.Home.Commands;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationService;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationTypeService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
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
class HomeTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private TranslationService translationService;
  @Mock
  private GroupService groupService;
  @Mock
  private PlayerService playerService;
  @Mock
  private LocationTypeService locationTypeService;
  @Mock
  private LocationService locationService;
  @Mock
  private TeleportService teleportService;
  @Mock
  private MessageService messageService;
  @Mock
  private Player player;
  @Mock
  private CommandSender nonPlayerSender;
  @Mock
  private Command command;
  @Mock
  private World world;

  private Home homeCommand;
  private PlayerEntry playerEntry;
  private LocationTypeEntry homeTypeEntry;

  @BeforeEach
  void setUp() {
    homeCommand = new Home();
    homeCommand.injectContext(serviceContext);

    playerEntry = new PlayerEntry();
    playerEntry.setId(1);
    playerEntry.setHomes(new ArrayList<>());
    playerEntry.setDeaths(new ArrayList<>());

    homeTypeEntry = new LocationTypeEntry();
    homeTypeEntry.setId(1);
    homeTypeEntry.setType(LocationType.HOME.name());

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(world.getName()).thenReturn("world");
  }

  @Test
  void onCommandSenderIsNotPlayerSendsNotAPlayerMessage() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn("not a player");

    boolean result = homeCommand.onCommand(nonPlayerSender, command, "home", new String[]{});

    verify(nonPlayerSender).sendMessage("not a player");
    assertTrue(result);
  }

  @Test
  void onCommandPlayerNotAuthorizedSendsPermissionMissingMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn("no permission");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{});

    verify(player).sendMessage("no permission");
    assertTrue(result);
  }

  @Test
  void onCommandNoArgsTeleportsToBed() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getTeleportService()).thenReturn(teleportService);

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{});

    verify(teleportService).teleportBed(player);
    assertTrue(result);
  }

  @Test
  void onCommandListArgWithHomesAndDeathsSendsAllMessages() {
    LocationEntry homeEntry = buildMockedLocationEntry("myHome");
    LocationEntry deathEntry = buildMockedLocationEntry("death_1");
    playerEntry.getHomes().add(homeEntry);
    playerEntry.getDeaths().add(deathEntry);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getMessageService()).thenReturn(messageService);
    when(messageService.locationToString(any())).thenReturn("world 0 0 0");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_LIST))).thenReturn("homes:");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_LIST_NAME), anyString(), anyString())).thenReturn("home name");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_LIST_DEATHPOINTS))).thenReturn("deaths:");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_LIST_DEATHPOINTS_NAME), anyString(), anyString())).thenReturn("death name");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"list"});

    verify(player).sendMessage("homes:");
    verify(player).sendMessage("home name");
    verify(player).sendMessage("deaths:");
    verify(player).sendMessage("death name");
    assertTrue(result);
  }

  @Test
  void onCommandListArgWithNoHomesSendsNoneMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_HOME_NONE)).thenReturn("no homes");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"list"});

    verify(player).sendMessage("no homes");
    assertTrue(result);
  }

  @Test
  void onCommandListArgWithHomesButNoDeathsDoesNotSendDeathsHeader() {
    LocationEntry homeEntry = buildMockedLocationEntry("myHome");
    playerEntry.getHomes().add(homeEntry);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getMessageService()).thenReturn(messageService);
    when(messageService.locationToString(any())).thenReturn("world 0 0 0");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_LIST))).thenReturn("homes:");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_LIST_NAME), anyString(), anyString())).thenReturn("home name");

    homeCommand.onCommand(player, command, "home", new String[]{"list"});

    verify(translationService, never()).getWithPrefix(MessageKey.COMMAND_HOME_LIST_DEATHPOINTS);
  }

  @Test
  void onCommandUnknownSingleArgSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn("wrong sub");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"unknown"});

    verify(player).sendMessage("wrong sub");
    assertTrue(result);
  }

  @Test
  void onCommandSetHomeWhenHomeAlreadyExistsSendsExistsMessage() {
    LocationEntry existingHome = buildLocationEntry("myHome");
    playerEntry.getHomes().add(existingHome);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_EXISTS), anyString())).thenReturn("home exists");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"set", "myHome"});

    verify(player).sendMessage("home exists");
    verify(serviceContext, never()).getLocationService();
    assertTrue(result);
  }

  @Test
  void onCommandSetHomeWithReservedNameSendsReservedMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_RESERVED), anyString())).thenReturn("reserved name");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"set", "death_myHome"});

    verify(player).sendMessage("reserved name");
    assertTrue(result);
  }

  @Test
  void onCommandSetHomeSuccessfullySavesAndSendsSetMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_SET), anyString())).thenReturn("home set");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"set", "newHome"});

    verify(locationService).save(any(LocationEntry.class));
    verify(player).sendMessage("home set");
    assertTrue(result);
    assertTrue(playerEntry.getHomes().stream().anyMatch(h -> h.getLocationName().equals("newHome")));
  }

  @Test
  void onCommandSetHomeLocationTypeNotFoundThrowsIllegalStateException() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.empty());

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> homeCommand.onCommand(player, command, "home", new String[]{"set", "newHome"}));
  }

  @Test
  void onCommandDeleteExistingHomeRemovesAndSendsDeleteMessage() {
    LocationEntry existingHome = buildLocationEntry("myHome");
    existingHome.setId(10);
    playerEntry.getHomes().add(existingHome);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_DELETE), anyString())).thenReturn("home deleted");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"delete", "myHome"});

    verify(locationService).delete(existingHome);
    verify(player).sendMessage("home deleted");
    assertTrue(result);
    assertTrue(playerEntry.getHomes().isEmpty());
  }

  @Test
  void onCommandDeleteExistingDeathRemovesAndSendsDeathDeleteMessage() {
    LocationEntry existingDeath = buildLocationEntry("death_1");
    existingDeath.setId(20);
    playerEntry.getDeaths().add(existingDeath);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_DEATH_DELETE), anyString())).thenReturn("death deleted");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"delete", "death_1"});

    verify(locationService).delete(existingDeath);
    verify(player).sendMessage("death deleted");
    assertTrue(result);
    assertTrue(playerEntry.getDeaths().isEmpty());
  }

  @Test
  void onCommandDeleteAllDeathsWithWildcardClearsAllDeaths() {
    LocationEntry death1 = buildLocationEntry("death_1");
    death1.setId(21);
    LocationEntry death2 = buildLocationEntry("death_2");
    death2.setId(22);
    playerEntry.getDeaths().add(death1);
    playerEntry.getDeaths().add(death2);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_DEATH_DELETE), anyString())).thenReturn("death deleted");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"delete", "death_*"});

    verify(locationService).delete(death1);
    verify(locationService).delete(death2);
    assertTrue(playerEntry.getDeaths().isEmpty());
    assertTrue(result);
  }

  @Test
  void onCommandDeleteNonExistentLocationSendsNotFoundMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_NOT_FOUND), anyString())).thenReturn("not found");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"delete", "nonExistent"});

    verify(player).sendMessage("not found");
    assertTrue(result);
  }

  @Test
  void onCommandTpToExistingHomeTeleportsPlayer() {
    LocationEntry existingHome = buildLocationEntry("myHome");
    playerEntry.getHomes().add(existingHome);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getTeleportService()).thenReturn(teleportService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"tp", "myHome"});

    verify(teleportService).teleportHome(player, existingHome);
    assertTrue(result);
  }

  @Test
  void onCommandTpToExistingDeathTeleportsPlayer() {
    LocationEntry existingDeath = buildLocationEntry("death_1");
    playerEntry.getDeaths().add(existingDeath);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getTeleportService()).thenReturn(teleportService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"tp", "death_1"});

    verify(teleportService).teleportHome(player, existingDeath);
    assertTrue(result);
  }

  @Test
  void onCommandTpToNonExistentLocationSendsNotFoundMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_NOT_FOUND), anyString())).thenReturn("not found");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"tp", "ghost"});

    verify(player).sendMessage("not found");
    assertTrue(result);
  }

  @Test
  void onCommandUnknownTwoArgSubCommandReturnsFalse() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(player.getLocation()).thenReturn(buildBukkitLocation());

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"unknown", "myHome"});

    assertFalse(result);
  }

  @Test
  void onCommandMoreThanTwoArgsReturnsFalse() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"set", "myHome", "extra"});

    assertFalse(result);
  }

  @Test
  void onTabCompleteNonPlayerReturnsEmptyList() {
    when(groupService.isSenderAuthorized(nonPlayerSender, "user")).thenReturn(true);

    List<String> result = homeCommand.onTabComplete(nonPlayerSender, command, "home", new String[]{"s"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteUnauthorizedReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);

    List<String> result = homeCommand.onTabComplete(player, command, "home", new String[]{"s"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteFirstArgReturnsAllSubCommands() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = homeCommand.onTabComplete(player, command, "home", new String[]{"s"});

    assertTrue(result.contains("set"));
    assertTrue(result.contains("delete"));
    assertTrue(result.contains("list"));
    assertTrue(result.contains("tp"));
  }

  @Test
  void onTabCompleteSecondArgAfterSetReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = homeCommand.onTabComplete(player, command, "home", new String[]{"set", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteSecondArgAfterListReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = homeCommand.onTabComplete(player, command, "home", new String[]{"list", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteSecondArgAfterDeleteReturnsHomeAndDeathNames() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getHomeAndDeathLocationNames(player)).thenReturn(List.of("myHome", "death_1"));

    List<String> result = homeCommand.onTabComplete(player, command, "home", new String[]{"delete", ""});

    assertTrue(result.contains("myHome"));
    assertTrue(result.contains("death_1"));
  }

  @Test
  void onTabCompleteSecondArgAfterTpReturnsHomeAndDeathNames() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getHomeAndDeathLocationNames(player)).thenReturn(List.of("myHome", "death_1"));

    List<String> result = homeCommand.onTabComplete(player, command, "home", new String[]{"tp", ""});

    assertTrue(result.contains("myHome"));
    assertTrue(result.contains("death_1"));
  }

  @Test
  void onTabCompleteThirdArgReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = homeCommand.onTabComplete(player, command, "home", new String[]{"tp", "myHome", ""});

    assertTrue(result.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"set", "SET", "Set"})
  void onCommandSetIsCaseInsensitive(String subCommand) {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_SET), anyString())).thenReturn("home set");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{subCommand, "newHome"});

    verify(locationService).save(any(LocationEntry.class));
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"delete", "DELETE", "Delete"})
  void onCommandDeleteIsCaseInsensitive(String subCommand) {
    LocationEntry existingHome = buildLocationEntry("myHome");
    existingHome.setId(10);
    playerEntry.getHomes().add(existingHome);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_DELETE), anyString())).thenReturn("home deleted");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{subCommand, "myHome"});

    verify(locationService).delete(existingHome);
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"tp", "TP", "Tp"})
  void onCommandTpIsCaseInsensitive(String subCommand) {
    LocationEntry existingHome = buildLocationEntry("myHome");
    playerEntry.getHomes().add(existingHome);

    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(serviceContext.getTeleportService()).thenReturn(teleportService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{subCommand, "myHome"});

    verify(teleportService).teleportHome(player, existingHome);
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"list", "LIST", "List"})
  void onCommandListIsCaseInsensitive(String subCommand) {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_HOME_NONE)).thenReturn("no homes");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{subCommand});

    verify(player).sendMessage("no homes");
    assertTrue(result);
  }

  @Test
  void getCommandsReturnsAllHomeSubCommands() {
    Home.Commands[] result = (Home.Commands[]) homeCommand.getCommands();

    assertEquals(result.length, Commands.values().length);
    assertTrue(List.of(result).contains(Home.Commands.SET));
    assertTrue(List.of(result).contains(Home.Commands.DELETE));
    assertTrue(List.of(result).contains(Home.Commands.LIST));
    assertTrue(List.of(result).contains(Home.Commands.TP));
  }

  @Test
  void onCommandDeleteNonExistentDeathWithoutWildcardSendsNotFoundMessage() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findByName(LocationType.HOME)).thenReturn(Optional.of(homeTypeEntry));
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_HOME_NOT_FOUND), anyString())).thenReturn("not found");

    boolean result = homeCommand.onCommand(player, command, "home", new String[]{"delete", "death_ghost"});

    verify(player).sendMessage("not found");
    assertTrue(result);
  }

  private LocationEntry buildLocationEntry(String name) {
    LocationEntry entry = new LocationEntry();
    entry.setLocationName(name);
    entry.setLocationType(homeTypeEntry);
    entry.setPlayerId(playerEntry.getId());
    entry.setWorld("world");
    entry.setX(0);
    entry.setY(64);
    entry.setZ(0);
    entry.setYaw(0f);
    entry.setPitch(0f);
    return entry;
  }

  private LocationEntry buildMockedLocationEntry(String name) {
    Location location = buildBukkitLocation();
    LocationEntry entry = org.mockito.Mockito.mock(LocationEntry.class);
    when(entry.getLocationName()).thenReturn(name);
    when(entry.getLocation()).thenReturn(location);
    return entry;
  }

  private Location buildBukkitLocation() {
    return new Location(world, 0, 64, 0, 0f, 0f);
  }
}