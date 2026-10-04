package de.relluem94.minecraft.server.spigot.essentials.interfaces;

/**
 * Represents a command entry within the plugin's command system.
 *
 * <p>Implementations of this interface are typically enums that define the available commands and
 * their associated sub-command labels.</p>
 *
 * @author rellu
 * @version 2.0
 * @since 4.3.2
 */
public interface CommandsEnum {

  /**
   * Returns the primary name of this command.
   *
   * @return the command name as a {@link String}
   */
  String getName();

  /**
   * Returns the sub-command labels associated with this command.
   *
   * @return an array of sub-command name strings
   */
  String[] getSubCommands();
}
