package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.registration.CommandWrapper;
import de.relluem94.minecraft.server.spigot.essentials.registries.CommandRegistry;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for managing and retrieving command information.
 */
public class CommandService {

  private final CommandRegistry commandRegistry;

  /**
   * Constructs a new CommandService.
   *
   * @param commandRegistry the registry containing the commands
   */
  public CommandService(CommandRegistry commandRegistry) {
    this.commandRegistry = commandRegistry;
  }

  public List<String> getAllCommandNames() {
    return commandRegistry.getAll()
        .stream()
        .map(CommandWrapper::getCommandName)
        .collect(Collectors.toList());
  }
}