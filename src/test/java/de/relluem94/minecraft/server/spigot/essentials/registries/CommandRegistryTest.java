package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.relluem94.minecraft.server.spigot.essentials.registration.CommandWrapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandRegistryTest {

  private CommandRegistry commandRegistry;

  @Mock
  private CommandWrapper commandWrapperOne;

  @Mock
  private CommandWrapper commandWrapperTwo;

  @BeforeEach
  void setUp() {
    commandRegistry = new CommandRegistry();
  }

  @Test
  void registerAddsCommandToRegistry() {
    commandRegistry.register(commandWrapperOne);

    List<CommandWrapper> allCommands = commandRegistry.getAll();

    assertEquals(1, allCommands.size());
    assertEquals(commandWrapperOne, allCommands.getFirst());
  }

  @Test
  void registerMultipleCommandsAddsAllToRegistry() {
    commandRegistry.register(commandWrapperOne);
    commandRegistry.register(commandWrapperTwo);

    List<CommandWrapper> allCommands = commandRegistry.getAll();

    assertEquals(2, allCommands.size());
    assertEquals(commandWrapperOne, allCommands.get(0));
    assertEquals(commandWrapperTwo, allCommands.get(1));
  }

  @Test
  void getAllReturnsUnmodifiableList() {
    commandRegistry.register(commandWrapperOne);
    List<CommandWrapper> allCommands = commandRegistry.getAll();

    assertThrows(UnsupportedOperationException.class, () -> allCommands.add(commandWrapperTwo));
  }

  @Test
  void getAllReturnsEmptyListWhenNoCommandsRegistered() {
    List<CommandWrapper> allCommands = commandRegistry.getAll();

    assertEquals(0, allCommands.size());
  }
}