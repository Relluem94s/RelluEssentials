package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.registration.CommandWrapper;
import de.relluem94.minecraft.server.spigot.essentials.registries.CommandRegistry;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

  @Mock
  private CommandRegistry commandRegistry;

  @Mock
  private CommandWrapper firstCommandWrapper;

  @Mock
  private CommandWrapper secondCommandWrapper;

  private CommandService commandService;

  @BeforeEach
  void setUp() {
    commandService = new CommandService(commandRegistry);
  }

  @Test
  void getAllCommandNamesReturnsAllMappedCommandNames() {
    when(firstCommandWrapper.getCommandName()).thenReturn("fly");
    when(secondCommandWrapper.getCommandName()).thenReturn("heal");
    when(commandRegistry.getAll()).thenReturn(List.of(firstCommandWrapper, secondCommandWrapper));

    List<String> result = commandService.getAllCommandNames();

    assertAll(
        () -> assertEquals(2, result.size()),
        () -> assertTrue(result.contains("fly")),
        () -> assertTrue(result.contains("heal"))
    );
  }

  @Test
  void getAllCommandNamesReturnsEmptyListWhenRegistryIsEmpty() {
    when(commandRegistry.getAll()).thenReturn(List.of());

    List<String> result = commandService.getAllCommandNames();

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void getAllCommandNamesPropagatesExceptionFromRegistry() {
    when(commandRegistry.getAll()).thenThrow(new RuntimeException("Registry failure"));

    assertThrows(RuntimeException.class, () -> commandService.getAllCommandNames());
  }

  @Test
  void getAllCommandNamesPropagatesExceptionFromCommandWrapper() {
    when(commandRegistry.getAll()).thenReturn(List.of(firstCommandWrapper));
    when(firstCommandWrapper.getCommandName()).thenThrow(new RuntimeException("Wrapper failure"));

    assertThrows(RuntimeException.class, () -> commandService.getAllCommandNames());
  }
}