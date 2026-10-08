package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomMobCommandTest {

  @Mock
  ServiceContext serviceContext;

  @Test
  void matchesReturnsTrueForExactCommandName() {
    CustomMobCommand command = new CustomMobCommand(serviceContext);

    boolean result = command.matches(new String[] {DevCommand.Commands.CUSTOM_MOB.getName()});

    assertTrue(result);
  }

  @Test
  void matchesReturnsTrueForCommandNameInUpperCase() {
    CustomMobCommand command = new CustomMobCommand(serviceContext);

    boolean result = command.matches(new String[] {DevCommand.Commands.CUSTOM_MOB.getName().toUpperCase()});

    assertTrue(result);
  }

  @Test
  void matchesReturnsTrueForCommandNameInLowerCase() {
    CustomMobCommand command = new CustomMobCommand(serviceContext);

    boolean result = command.matches(new String[] {DevCommand.Commands.CUSTOM_MOB.getName().toLowerCase()});

    assertTrue(result);
  }

  @Test
  void matchesReturnsFalseForEmptyArgs() {
    CustomMobCommand command = new CustomMobCommand(serviceContext);

    boolean result = command.matches(new String[] {});

    assertFalse(result);
  }

  @Test
  void matchesReturnsFalseForWrongCommandName() {
    CustomMobCommand command = new CustomMobCommand(serviceContext);

    boolean result = command.matches(new String[] {"wrongcommand"});

    assertFalse(result);
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 5})
  void matchesReturnsFalseWhenArgLengthIsNotOne(int argCount) {
    CustomMobCommand command = new CustomMobCommand(serviceContext);
    String[] args = new String[argCount];
    args[0] = DevCommand.Commands.CUSTOM_MOB.getName();
    for (int i = 1; i < argCount; i++) {
      args[i] = "extra";
    }

    boolean result = command.matches(args);

    assertFalse(result);
  }
}