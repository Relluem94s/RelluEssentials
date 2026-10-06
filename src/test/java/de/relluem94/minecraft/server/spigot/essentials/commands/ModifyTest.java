package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModifyTest {

  private static final String MESSAGE = "translated-message";

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private ServiceContext serviceContext;

  @Mock
  private Command command;

  @Mock
  private CommandSender nonPlayerSender;

  @Mock
  private Player player;

  private Modify modify;

  @BeforeEach
  void setUp() {
    modify = new Modify();
    modify.injectContext(serviceContext);
  }

  @Test
  void constantBlocksPerTickHasExpectedValue() {
    assertEquals(64, Modify.BLOCKS_PER_TICK);
  }

  @Test
  void constantMaxRadiusHasExpectedValue() {
    assertEquals(128, Modify.MAX_RADIUS);
  }

  @Test
  void constantMaxIterationsHasExpectedValue() {
    assertEquals(1048576, Modify.MAX_ITERATIONS);
  }

  @Test
  void getCommandsReturnsAllModifyCommands() {
    CommandsEnum[] commands = modify.getCommands();

    assertNotNull(commands);
    assertEquals(Modify.Commands.values().length, commands.length);
  }

  @Test
  void onCommandReturnsTrueWhenSenderIsUnauthorized() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(MESSAGE);

    boolean result = modify.onCommand(nonPlayerSender, command, "modify", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(MESSAGE);
  }

  @Test
  void onCommandReturnsTrueWhenSenderIsNotAnPlayer() {
    when(serviceContext.getGroupService().isSenderAuthorized(nonPlayerSender, "mod"))
        .thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(MESSAGE);

    boolean result = modify.onCommand(nonPlayerSender, command, "modify", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(MESSAGE);
  }

  @Test
  void onCommandReturnsTrueWhenNoArgumentsAreProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(MESSAGE);

    boolean result = modify.onCommand(player, command, "modify", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onCommandReturnsTrueWhenSubCommandIsUnknown() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(MESSAGE);

    boolean result = modify.onCommand(player, command, "modify", new String[]{"unknown"});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsUnauthorized() {
    List<String> suggestions = modify.onTabComplete(nonPlayerSender, command, "modify", new String[]{"set"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsAllCommandNamesWhenFirstArgumentIsBeingTyped() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTabCompleterService().getCommands(Modify.Commands.values()))
        .thenReturn(List.of("set", "replace", "move"));

    List<String> suggestions = modify.onTabComplete(player, command, "modify", new String[]{"s"});

    assertNotNull(suggestions);
    assertEquals(List.of("set", "replace", "move"), suggestions);
  }

  @Test
  void onTabCompleteReturnsEmptyListForUndoSubCommand() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.UNDO.getName(), ""});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListForMoveSubCommand() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.MOVE.getName(), ""});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsClipboardSubCommandsForClipboardCommand() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.CLIPBOARD.getName(), ""});

    assertNotNull(suggestions);
    assertEquals(List.of(Modify.Commands.CLIPBOARD.getSubCommands()), suggestions);
  }

  @Test
  void onTabCompleteReturnsMaterialsFilteredByInputForSecondArgument() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTabCompleterService().getMaterials("STONE"))
        .thenReturn(List.of("STONE", "STONE_BRICKS"));

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.SET.getName(), "STONE"});

    assertNotNull(suggestions);
    assertEquals(List.of("STONE", "STONE_BRICKS"), suggestions);
  }

  @Test
  void onTabCompleteReturnsMaterialsWithNullWhenSecondArgumentIsEmpty() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTabCompleterService().getMaterials(null))
        .thenReturn(List.of("STONE", "DIRT"));

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.SET.getName(), ""});

    assertNotNull(suggestions);
    assertEquals(List.of("STONE", "DIRT"), suggestions);
    verify(serviceContext.getTabCompleterService()).getMaterials(null);
  }

  @Test
  void onTabCompleteReturnsEmptyListForThirdArgumentWhenNotReplace() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.SET.getName(), "STONE", "DI"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsMaterialsForThirdArgumentWhenReplaceCommand() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTabCompleterService().getMaterials("DIRT"))
        .thenReturn(List.of("DIRT", "DIRT_PATH"));

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.REPLACE.getName(), "STONE", "DIRT"});

    assertNotNull(suggestions);
    assertEquals(List.of("DIRT", "DIRT_PATH"), suggestions);
  }

  @Test
  void onTabCompleteReturnsMaterialsWithNullWhenThirdArgumentIsEmptyForReplace() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTabCompleterService().getMaterials(null))
        .thenReturn(List.of("STONE", "DIRT"));

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.REPLACE.getName(), "STONE", ""});

    assertNotNull(suggestions);
    assertEquals(List.of("STONE", "DIRT"), suggestions);
    verify(serviceContext.getTabCompleterService()).getMaterials(null);
  }

  @Test
  void onTabCompleteReturnsEmptyListForFourthOrMoreArguments() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);

    List<String> suggestions = modify.onTabComplete(player, command, "modify",
        new String[]{Modify.Commands.REPLACE.getName(), "STONE", "DIRT", "extra"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"set", "replace", "move", "copy", "cut", "paste", "clipboard", "undo", "wall", "cylinder", "fill", "fillr", "plant"})
  void commandsEnumContainsExpectedCommandName(String expectedName) {
    boolean found = false;
    for (Modify.Commands cmd : Modify.Commands.values()) {
      if (cmd.getName().equals(expectedName)) {
        found = true;
        break;
      }
    }
    assertTrue(found);
  }

  @Test
  void commandsEnumClipboardHasRotateSubCommand() {
    String[] subCommands = Modify.Commands.CLIPBOARD.getSubCommands();

    assertNotNull(subCommands);
    assertEquals(1, subCommands.length);
    assertEquals("rotate", subCommands[0]);
  }

  @Test
  void commandsEnumNonClipboardCommandsHaveEmptySubCommands() {
    for (Modify.Commands cmd : Modify.Commands.values()) {
      if (cmd == Modify.Commands.CLIPBOARD) {
        continue;
      }
      assertNotNull(cmd.getSubCommands());
      assertEquals(0, cmd.getSubCommands().length);
    }
  }
}