package de.relluem94.minecraft.server.spigot.essentials.wrappers;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandWrapper {

  private final CommandConstruct construct;

  private boolean initialised = false;

  public CommandWrapper(@NotNull CommandConstruct construct) {
    this.construct = construct;
  }

  public boolean hasSubCommands() {
    return construct.getSubCommandRegistry().isPresent()
        || construct.getCommands().length != 0;
  }

  public CommandsEnum[] getSubCommands() {
    return construct.getCommands();
  }

  public String getCommandName() {
    return AnnotationHelper.getCommandName(this.construct.getClass());
  }

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