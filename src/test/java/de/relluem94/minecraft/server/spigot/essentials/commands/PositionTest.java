package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.commands.Position.Commands;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.PositionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PositionTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private TranslationService translationService;
  @Mock
  private GroupService groupService;
  @Mock
  private PositionService positionService;
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
  @Mock
  private DoubleStore<Location, Location> positionStore;

  private Position positionCommand;

  @BeforeEach
  void setUp() {
    positionCommand = new Position();
    positionCommand.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getPositionService()).thenReturn(positionService);
    lenient().when(world.getName()).thenReturn("world");
  }

  @Test
  void onCommandUnauthorizedSenderSendsPermissionMissingMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(
        "no permission");

    boolean result = positionCommand.onCommand(player, command, "position", new String[]{});

    verify(player).sendMessage("no permission");
    assertTrue(result);
  }

  @Test
  void onCommandSenderIsNotPlayerSendsNotAPlayerMessage() {
    when(groupService.isSenderAuthorized(nonPlayerSender, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(
        "not a player");

    boolean result = positionCommand.onCommand(nonPlayerSender, command, "position",
        new String[]{});

    verify(nonPlayerSender).sendMessage("not a player");
    assertTrue(result);
  }

  @Test
  void onCommandNoArgsWithNoPositionsSendsNoPositionsMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.hasPositions(player)).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_NO_POSITIONS)).thenReturn(
        "no positions");

    boolean result = positionCommand.onCommand(player, command, "position", new String[]{});

    verify(player).sendMessage("no positions");
    assertTrue(result);
  }

  @Test
  void onCommandNoArgsWithBothPositionsSendsInfoMessages() {
    Location first = buildBukkitLocation();
    Location second = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.hasPositions(player)).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(positionStore.getSecondValue()).thenReturn(second);
    when(serviceContext.getMessageService()).thenReturn(messageService);
    when(messageService.locationToString(first)).thenReturn("0 64 0");
    when(messageService.locationToString(second)).thenReturn("0 64 0");
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_INFO_1)).thenReturn("info1");
    when(
        translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_INFO_2), any())).thenReturn(
        "info2");
    when(
        translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_INFO_3), any())).thenReturn(
        "info3");

    boolean result = positionCommand.onCommand(player, command, "position", new String[]{});

    verify(player).sendMessage("info1");
    verify(player).sendMessage("info2");
    verify(player).sendMessage("info3");
    assertTrue(result);
  }

  @Test
  void onCommandNoArgsWithFirstPositionNullShowsNotAvailableForFirst() {
    Location second = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.hasPositions(player)).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(null);
    when(positionStore.getSecondValue()).thenReturn(second);
    when(serviceContext.getMessageService()).thenReturn(messageService);
    when(messageService.locationToString(second)).thenReturn("0 64 0");
    when(translationService.get(MessageKey.COMMAND_POSITION_NO_POSITIONS)).thenReturn("N/A");
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_INFO_1)).thenReturn("info1");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_INFO_2),
        eq("N/A"))).thenReturn("info2 N/A");
    when(
        translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_INFO_3), any())).thenReturn(
        "info3");

    boolean result = positionCommand.onCommand(player, command, "position", new String[]{});

    verify(player).sendMessage("info2 N/A");
    assertTrue(result);
  }

  @Test
  void onCommandNoArgsWithSecondPositionNullShowsNotAvailableForSecond() {
    Location first = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.hasPositions(player)).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(positionStore.getSecondValue()).thenReturn(null);
    when(serviceContext.getMessageService()).thenReturn(messageService);
    when(messageService.locationToString(first)).thenReturn("0 64 0");
    when(translationService.get(MessageKey.COMMAND_POSITION_NO_POSITIONS)).thenReturn("N/A");
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_INFO_1)).thenReturn("info1");
    when(
        translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_INFO_2), any())).thenReturn(
        "info2");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_INFO_3),
        eq("N/A"))).thenReturn("info3 N/A");

    boolean result = positionCommand.onCommand(player, command, "position", new String[]{});

    verify(player).sendMessage("info3 N/A");
    assertTrue(result);
  }

  @Test
  void onCommandClearWithExtraArgsSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(
        "wrong sub");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"clear", "extra"});

    verify(player).sendMessage("wrong sub");
    verify(positionService, never()).clearPositions(any());
    assertTrue(result);
  }

  @Test
  void onCommandClearClearsPositionsAndSendsClearMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_CLEAR)).thenReturn("cleared");

    boolean result = positionCommand.onCommand(player, command, "position", new String[]{"clear"});

    verify(positionService).ensurePositionsExist(player);
    verify(positionService).clearPositions(player);
    verify(player).sendMessage("cleared");
    assertTrue(result);
  }

  @Test
  void onCommandSetWithWrongArgCountSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(
        "wrong sub");

    boolean result = positionCommand.onCommand(player, command, "position", new String[]{"set"});

    verify(player).sendMessage("wrong sub");
    assertTrue(result);
  }

  @Test
  void onCommandSetFirstSetsFirstPositionAndSendsMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getMessageService()).thenReturn(messageService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(messageService.locationToString(any())).thenReturn("0 64 0");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_SET_FIRST),
        any())).thenReturn("first set");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"set", "first"});

    verify(positionService).setFirstPosition(eq(player), any(Location.class));
    verify(player).sendMessage("first set");
    assertTrue(result);
  }

  @Test
  void onCommandSetSecondSetsSecondPositionAndSendsMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getMessageService()).thenReturn(messageService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(messageService.locationToString(any())).thenReturn("0 64 0");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_SET_SECOND),
        any())).thenReturn("second set");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"set", "second"});

    verify(positionService).setSecondPosition(eq(player), any(Location.class));
    verify(player).sendMessage("second set");
    assertTrue(result);
  }

  @Test
  void onCommandSetUnknownSubCommandSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(
        "wrong sub");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"set", "unknown"});

    verify(player).sendMessage("wrong sub");
    assertTrue(result);
  }

  @Test
  void onCommandRemoveWithWrongArgCountSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(
        "wrong sub");

    boolean result = positionCommand.onCommand(player, command, "position", new String[]{"remove"});

    verify(player).sendMessage("wrong sub");
    assertTrue(result);
  }

  @Test
  void onCommandRemoveFirstRemovesFirstPositionAndSendsMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_REMOVE_FIRST)).thenReturn(
        "first removed");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"remove", "first"});

    verify(positionService).removeFirstPosition(player);
    verify(player).sendMessage("first removed");
    assertTrue(result);
  }

  @Test
  void onCommandRemoveSecondRemovesSecondPositionAndSendsMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_REMOVE_SECOND)).thenReturn(
        "second removed");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"remove", "second"});

    verify(positionService).removeSecondPosition(player);
    verify(player).sendMessage("second removed");
    assertTrue(result);
  }

  @Test
  void onCommandRemoveUnknownSubCommandSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(
        "wrong sub");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"remove", "unknown"});

    verify(player).sendMessage("wrong sub");
    assertTrue(result);
  }

  @Test
  void onCommandShiftWithInvalidAmountSendsInvalidAmountMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_INVALID_AMOUNT)).thenReturn(
        "invalid amount");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"shift", "notANumber"});

    verify(player).sendMessage("invalid amount");
    assertTrue(result);
  }

  @Test
  void onCommandShiftWithNoPositionsSendsNoPositionsMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(null);
    when(positionStore.getSecondValue()).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_NO_POSITIONS)).thenReturn(
        "no positions");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"shift", "5"});

    verify(player).sendMessage("no positions");
    verify(positionService, never()).shiftPositions(any(), any(), anyInt());
    assertTrue(result);
  }

  @Test
  void onCommandShiftWithFirstPositionOnlyShiftsAndSendsMessage() {
    Location first = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_SHIFT), eq(5))).thenReturn(
        "shifted");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"shift", "5"});

    verify(positionService).shiftPositions(eq(player), any(Vector.class), eq(5));
    verify(player).sendMessage("shifted");
    assertTrue(result);
  }

  @Test
  void onCommandShiftWithBothPositionsShiftsAndSendsMessage() {
    Location first = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(
        translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_SHIFT), eq(10))).thenReturn(
        "shifted");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"shift", "10"});

    verify(positionService).shiftPositions(eq(player), any(Vector.class), eq(10));
    verify(player).sendMessage("shifted");
    assertTrue(result);
  }

  @Test
  void onCommandExpandWithMissingSecondPositionSendsNeedBothPositionsMessage() {
    Location first = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(positionStore.getSecondValue()).thenReturn(null);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_POSITION_NEED_BOTH_POSITIONS)).thenReturn("need both");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"expand", "5"});

    verify(player).sendMessage("need both");
    verify(positionService, never()).expandOrDecreasePositions(any(), any(), anyInt(),
        any(Boolean.class));
    assertTrue(result);
  }

  @Test
  void onCommandExpandWithMissingFirstPositionSendsNeedBothPositionsMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(null);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_POSITION_NEED_BOTH_POSITIONS)).thenReturn("need both");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"expand", "5"});

    verify(player).sendMessage("need both");
    assertTrue(result);
  }

  @Test
  void onCommandExpandWithBothPositionsExpandsAndSendsMessage() {
    Location first = buildBukkitLocation();
    Location second = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(positionStore.getSecondValue()).thenReturn(second);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(
        translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_EXPAND), eq(5))).thenReturn(
        "expanded");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"expand", "5"});

    verify(positionService).expandOrDecreasePositions(eq(player), any(Vector.class), eq(5),
        eq(true));
    verify(player).sendMessage("expanded");
    assertTrue(result);
  }

  @Test
  void onCommandDecreaseWithBothPositionsDecreasesAndSendsMessage() {
    Location first = buildBukkitLocation();
    Location second = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(positionStore.getSecondValue()).thenReturn(second);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_DECREASE),
        eq(3))).thenReturn("decreased");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"decrease", "3"});

    verify(positionService).expandOrDecreasePositions(eq(player), any(Vector.class), eq(3),
        eq(false));
    verify(player).sendMessage("decreased");
    assertTrue(result);
  }

  @Test
  void onCommandUnknownSubCommandSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(
        "wrong sub");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"unknown", "5"});

    verify(player).sendMessage("wrong sub");
    assertTrue(result);
  }

  @Test
  void onCommandUnknownSubCommandWithoutAmountSendsWrongSubCommandMessage() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(
        "wrong sub");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"unknown"});

    verify(player).sendMessage("wrong sub");
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"clear", "CLEAR", "Clear"})
  void onCommandClearIsCaseInsensitive(String subCommand) {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_CLEAR)).thenReturn("cleared");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{subCommand});

    verify(positionService).clearPositions(player);
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"set", "SET", "Set"})
  void onCommandSetIsCaseInsensitive(String subCommand) {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getMessageService()).thenReturn(messageService);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(messageService.locationToString(any())).thenReturn("0 64 0");
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_SET_FIRST),
        any())).thenReturn("first set");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{subCommand, "first"});

    verify(positionService).setFirstPosition(eq(player), any(Location.class));
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"remove", "REMOVE", "Remove"})
  void onCommandRemoveIsCaseInsensitive(String subCommand) {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_POSITION_REMOVE_FIRST)).thenReturn(
        "first removed");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{subCommand, "first"});

    verify(positionService).removeFirstPosition(player);
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"shift", "SHIFT", "Shift"})
  void onCommandShiftIsCaseInsensitive(String subCommand) {
    Location first = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_SHIFT), eq(5))).thenReturn(
        "shifted");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{subCommand, "5"});

    verify(positionService).shiftPositions(eq(player), any(Vector.class), eq(5));
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"expand", "EXPAND", "Expand"})
  void onCommandExpandIsCaseInsensitive(String subCommand) {
    Location first = buildBukkitLocation();
    Location second = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(positionStore.getSecondValue()).thenReturn(second);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(
        translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_EXPAND), eq(5))).thenReturn(
        "expanded");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{subCommand, "5"});

    verify(positionService).expandOrDecreasePositions(eq(player), any(Vector.class), eq(5),
        eq(true));
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"decrease", "DECREASE", "Decrease"})
  void onCommandDecreaseIsCaseInsensitive(String subCommand) {
    Location first = buildBukkitLocation();
    Location second = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(first);
    when(positionStore.getSecondValue()).thenReturn(second);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_DECREASE),
        eq(3))).thenReturn("decreased");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{subCommand, "3"});

    verify(positionService).expandOrDecreasePositions(eq(player), any(Vector.class), eq(3),
        eq(false));
    assertTrue(result);
  }

  @Test
  void onTabCompleteUnauthorizedReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);

    List<String> result = positionCommand.onTabComplete(player, command, "position",
        new String[]{"c"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteFirstArgReturnsAllSubCommands() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = positionCommand.onTabComplete(player, command, "position",
        new String[]{"c"});

    assertTrue(result.contains("set"));
    assertTrue(result.contains("remove"));
    assertTrue(result.contains("shift"));
    assertTrue(result.contains("expand"));
    assertTrue(result.contains("decrease"));
    assertTrue(result.contains("clear"));
  }

  @Test
  void onTabCompleteSecondArgAfterSetReturnsFirstAndSecond() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = positionCommand.onTabComplete(player, command, "position",
        new String[]{"set", ""});

    assertTrue(result.contains("first"));
    assertTrue(result.contains("second"));
  }

  @Test
  void onTabCompleteSecondArgAfterRemoveReturnsFirstAndSecond() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = positionCommand.onTabComplete(player, command, "position",
        new String[]{"remove", ""});

    assertTrue(result.contains("first"));
    assertTrue(result.contains("second"));
  }

  @Test
  void onTabCompleteSecondArgAfterShiftReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = positionCommand.onTabComplete(player, command, "position",
        new String[]{"shift", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteSecondArgAfterClearReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = positionCommand.onTabComplete(player, command, "position",
        new String[]{"clear", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteThirdArgReturnsEmptyList() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = positionCommand.onTabComplete(player, command, "position",
        new String[]{"set", "first", ""});

    assertTrue(result.isEmpty());
  }

  @Test
  void getCommandsReturnsAllPositionSubCommands() {
    Commands[] result = (Commands[]) positionCommand.getCommands();

    assertEquals(Commands.values().length, result.length);
    assertTrue(List.of(result).contains(Commands.SET));
    assertTrue(List.of(result).contains(Commands.REMOVE));
    assertTrue(List.of(result).contains(Commands.SHIFT));
    assertTrue(List.of(result).contains(Commands.EXPAND));
    assertTrue(List.of(result).contains(Commands.DECREASE));
    assertTrue(List.of(result).contains(Commands.CLEAR));
  }

  @Test
  void commandsEnumSetHasCorrectNameAndSubCommands() {
    assertEquals("set", Commands.SET.getName());
    assertEquals(2, Commands.SET.getSubCommands().length);
    assertEquals("first", Commands.SET.getSubCommands()[0]);
    assertEquals("second", Commands.SET.getSubCommands()[1]);
  }

  @Test
  void commandsEnumRemoveHasCorrectNameAndSubCommands() {
    assertEquals("remove", Commands.REMOVE.getName());
    assertEquals(2, Commands.REMOVE.getSubCommands().length);
    assertEquals("first", Commands.REMOVE.getSubCommands()[0]);
    assertEquals("second", Commands.REMOVE.getSubCommands()[1]);
  }

  @Test
  void commandsEnumShiftHasCorrectNameAndNoSubCommands() {
    assertEquals("shift", Commands.SHIFT.getName());
    assertEquals(0, Commands.SHIFT.getSubCommands().length);
  }

  @Test
  void commandsEnumExpandHasCorrectNameAndNoSubCommands() {
    assertEquals("expand", Commands.EXPAND.getName());
    assertEquals(0, Commands.EXPAND.getSubCommands().length);
  }

  @Test
  void commandsEnumDecreaseHasCorrectNameAndNoSubCommands() {
    assertEquals("decrease", Commands.DECREASE.getName());
    assertEquals(0, Commands.DECREASE.getSubCommands().length);
  }

  @Test
  void commandsEnumClearHasCorrectNameAndNoSubCommands() {
    assertEquals("clear", Commands.CLEAR.getName());
    assertEquals(0, Commands.CLEAR.getSubCommands().length);
  }

  @Test
  void onCommandShiftWithSecondPositionOnlyShiftsAndSendsMessage() {
    Location second = buildBukkitLocation();

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positionStore);
    when(positionStore.getValue()).thenReturn(null);
    when(positionStore.getSecondValue()).thenReturn(second);
    when(player.getLocation()).thenReturn(buildBukkitLocation());
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_POSITION_SHIFT), eq(5))).thenReturn(
        "shifted");

    boolean result = positionCommand.onCommand(player, command, "position",
        new String[]{"shift", "5"});

    verify(positionService).shiftPositions(eq(player), any(Vector.class), eq(5));
    verify(player).sendMessage("shifted");
    assertTrue(result);
  }

  private Location buildBukkitLocation() {
    return new Location(world, 0, 64, 0, 0f, 0f);
  }
}