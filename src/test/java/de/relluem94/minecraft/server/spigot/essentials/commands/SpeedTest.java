package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
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
class SpeedTest {

  private static final String MESSAGE = "translated-message";

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private ServiceContext serviceContext;

  @Mock
  private Command command;

  @Mock
  private CommandSender nonPlayerSender;

  @Mock
  private Player player;

  private Speed speed;

  @BeforeEach
  void setUp() {
    speed = new Speed();
    speed.injectContext(serviceContext);
  }

  @Test
  void getCommandsReturnsAllTenSpeedValues() {
    CommandsEnum[] commands = speed.getCommands();

    assertNotNull(commands);
    assertEquals(10, commands.length);
  }

  @Test
  void commandsEnumHasCorrectNamesForAllValues() {
    Speed.Commands[] commands = Speed.Commands.values();

    assertEquals("1", commands[0].getName());
    assertEquals("2", commands[1].getName());
    assertEquals("3", commands[2].getName());
    assertEquals("4", commands[3].getName());
    assertEquals("5", commands[4].getName());
    assertEquals("6", commands[5].getName());
    assertEquals("7", commands[6].getName());
    assertEquals("8", commands[7].getName());
    assertEquals("9", commands[8].getName());
    assertEquals("10", commands[9].getName());
  }

  @Test
  void commandsEnumSubCommandsAreEmptyByDefault() {
    for (Speed.Commands cmd : Speed.Commands.values()) {
      assertNotNull(cmd.getSubCommands());
      assertEquals(0, cmd.getSubCommands().length);
    }
  }

  @Test
  void onCommandSendsInfoMessageWhenNoArgumentsProvided() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SPEED_INFO)).thenReturn(MESSAGE);

    boolean result = speed.onCommand(nonPlayerSender, command, "speed", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSendsInfoMessageWhenMoreThanOneArgumentProvided() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SPEED_INFO)).thenReturn(MESSAGE);

    boolean result = speed.onCommand(nonPlayerSender, command, "speed", new String[]{"5", "extra"});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(MESSAGE);

    boolean result = speed.onCommand(nonPlayerSender, command, "speed", new String[]{"5"});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsUnauthorized() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(MESSAGE);

    boolean result = speed.onCommand(player, command, "speed", new String[]{"5"});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).setWalkSpeed(org.mockito.ArgumentMatchers.anyFloat());
    verify(player, never()).setFlySpeed(org.mockito.ArgumentMatchers.anyFloat());
  }

  @Test
  void onCommandSendsInvalidMessageWhenArgumentIsNotNumeric() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_INVALID)).thenReturn(MESSAGE);

    speed.onCommand(player, command, "speed", new String[]{"abc"});

    verify(player).sendMessage(MESSAGE);
    verify(player, never()).setWalkSpeed(org.mockito.ArgumentMatchers.anyFloat());
    verify(player, never()).setFlySpeed(org.mockito.ArgumentMatchers.anyFloat());
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "5", "10"})
  void onCommandSetsWalkSpeedWhenPlayerIsNotFlying(String speedValue) {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(player.isFlying()).thenReturn(false);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SPEED, speedValue)).thenReturn(MESSAGE);

    boolean result = speed.onCommand(player, command, "speed", new String[]{speedValue});

    assertTrue(result);
    float expectedSpeed = (float) Integer.parseInt(speedValue) / 10;
    verify(player).setWalkSpeed(expectedSpeed);
    verify(player, never()).setFlySpeed(org.mockito.ArgumentMatchers.anyFloat());
    verify(player).sendMessage(MESSAGE);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "5", "10"})
  void onCommandSetsFlySpeedWhenPlayerIsFlying(String speedValue) {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(player.isFlying()).thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SPEED, speedValue)).thenReturn(MESSAGE);

    boolean result = speed.onCommand(player, command, "speed", new String[]{speedValue});

    assertTrue(result);
    float expectedSpeed = (float) Integer.parseInt(speedValue) / 10;
    verify(player).setFlySpeed(expectedSpeed);
    verify(player, never()).setWalkSpeed(org.mockito.ArgumentMatchers.anyFloat());
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsUnauthorized() {
    List<String> suggestions = speed.onTabComplete(player, command, "speed", new String[]{"5"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    when(serviceContext.getGroupService().isSenderAuthorized(nonPlayerSender, "mod"))
        .thenReturn(true);

    List<String> suggestions = speed.onTabComplete(nonPlayerSender, command, "speed", new String[]{"5"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgumentProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);

    List<String> suggestions = speed.onTabComplete(player, command, "speed", new String[]{"5", "extra"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1})
  void onTabCompleteReturnsSpeedValuesForSupportedArgumentCounts(int argumentCount) {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod"))
        .thenReturn(true);
    when(serviceContext.getTabCompleterService().getCommands(speed.getCommands()))
        .thenReturn(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

    List<String> suggestions = speed.onTabComplete(player, command, "speed", new String[argumentCount]);

    assertNotNull(suggestions);
    assertEquals(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), suggestions);
  }
}