package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.registries.CommandRegistry;
import de.relluem94.minecraft.server.spigot.essentials.wrappers.CommandWrapper;
import java.util.List;
import java.util.stream.Collectors;

public class CommandService {

  private final CommandRegistry commandRegistry;

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