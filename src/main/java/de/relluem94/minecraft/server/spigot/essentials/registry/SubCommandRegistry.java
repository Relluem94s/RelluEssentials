package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import java.util.List;
import lombok.NonNull;

/**
 * Registry that holds and resolves {@link SubCommand} implementations.
 * <p>
 * Given a list of sub-commands, this registry finds the first one whose
 * {@link SubCommand#matches(String[])} method returns {@code true} for the provided arguments.
 * </p>
 *
 * @param <T> the specific type of {@link SubCommand} managed by this registry
 */
public class SubCommandRegistry<T extends SubCommand> {

  private final List<T> subCommands;

  /**
   * Constructs a new {@code SubCommandRegistry} with the given list of sub-commands.
   *
   * @param subCommands the list of {@link SubCommand} implementations to register
   */
  public SubCommandRegistry(List<T> subCommands) {
    this.subCommands = subCommands;
  }

  /**
   * Finds the first {@link SubCommand} that matches the given arguments.
   *
   * @param args the arguments passed alongside the command; must not be {@code null}
   * @return the matching {@link SubCommand}, or {@code null} if none matches
   */
  public T find(@NonNull String[] args) {
    return subCommands.stream()
        .filter(subCommand -> subCommand.matches(args))
        .findFirst()
        .orElse(null);
  }
}