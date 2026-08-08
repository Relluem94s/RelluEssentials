package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.wrappers.CommandWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRegistry {

  private final List<CommandWrapper> registeredCommands = new ArrayList<>();

  public void register(CommandWrapper commandWrapper) {
    registeredCommands.add(commandWrapper);
  }

  public List<CommandWrapper> getAll() {
    return Collections.unmodifiableList(registeredCommands);
  }
}