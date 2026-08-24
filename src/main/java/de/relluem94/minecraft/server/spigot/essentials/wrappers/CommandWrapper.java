package de.relluem94.minecraft.server.spigot.essentials.wrappers;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for command implementations that handles initialization and provides
 * metadata about the command structure.
 */
public class CommandWrapper {

  private final CommandConstruct construct;

  private boolean initialised = false;

  /**
   * Creates a new CommandWrapper.
   *
   * @param construct the command implementation to wrap
   */
  public CommandWrapper(@NotNull CommandConstruct construct) {
    this.construct = construct;
  }

  /**
   * Checks if the command contains any sub-commands.
   *
   * @return true if sub-commands are present, false otherwise
   */
  public boolean hasSubCommands() {
    return construct.getSubCommandRegistry().isPresent()
        || construct.getCommands().length != 0;
  }

  /**
   * Retrieves the array of sub-commands associated with this command.
   *
   * @return an array of {@link CommandsEnum} representing the sub-commands
   */
  public CommandsEnum[] getSubCommands() {
    return construct.getCommands();
  }

  /**
   * Retrieves the name of the command based on its class annotation.
   *
   * @return the command name as a string
   */
  public String getCommandName() {
    return AnnotationHelper.getCommandName(this.construct.getClass());
  }

  /**
   * Initializes the command by injecting the service context and registering
   * the executor and tab completer to the plugin command.
   *
   * @param javaPlugin the plugin instance owning the command
   * @param serviceContext the context to inject into the command construct
   */
  public void init(JavaPlugin javaPlugin, ServiceContext serviceContext) {
    if (initialised) {
      return;
    }

    PluginCommand pluginCommand = javaPlugin.getCommand(getCommandName());

    if (pluginCommand == null) {
      return;
    }

    construct.injectContext(serviceContext);
    pluginCommand.setExecutor(construct);
    pluginCommand.setTabCompleter(construct);
    initialised = true;
  }
}