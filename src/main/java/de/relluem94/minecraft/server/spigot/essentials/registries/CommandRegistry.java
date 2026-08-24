package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.wrappers.CommandWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registry responsible for managing and storing registered command wrappers.
 */
public class CommandRegistry {

  private final List<CommandWrapper> registeredCommands = new ArrayList<>();

  /**
   * Registers a new command wrapper into the registry.
   *
   * @param commandWrapper the command wrapper to be registered
   */
  public void register(CommandWrapper commandWrapper) {
    registeredCommands.add(commandWrapper);
  }

  /**
   * Retrieves an unmodifiable list of all registered command wrappers.
   *
   * @return an unmodifiable list containing all registered commands
   */
  public List<CommandWrapper> getAll() {
    return Collections.unmodifiableList(registeredCommands);
  }
}