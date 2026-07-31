package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import de.relluem94.minecraft.server.spigot.essentials.registry.SubCommandRegistry;
import java.util.Optional;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

/**
 * Represents the base contract for all command implementations in the plugin.
 * <p>
 * Every command must implement this interface to be registered and handled by the plugin's command
 * system. It combines {@link CommandExecutor} and {@link TabCompleter} to provide both execution
 * and tab-completion behavior.
 * </p>
 */
public interface CommandConstruct extends CommandExecutor, TabCompleter {

  /**
   * Returns all {@link CommandsEnum} entries associated with this command.
   * <p>
   * These entries define the command names and their respective sub-commands that are supported by
   * this implementation.
   * </p>
   *
   * @return an array of {@link CommandsEnum} representing the supported commands
   */
  CommandsEnum[] getCommands();

  /**
   * Returns an {@link Optional} containing the {@link SubCommandRegistry} for this command, if
   * present.
   * <p>
   * By default, this returns an empty {@link Optional}, meaning the command has no sub-commands.
   * Override this method to provide a registry when sub-commands are needed.
   * </p>
   *
   * @return an {@link Optional} wrapping the {@link SubCommandRegistry}, or
   * {@link Optional#empty()} if none exists
   */
  default Optional<SubCommandRegistry<?>> getSubCommandRegistry() {
    return Optional.empty();
  }
}
