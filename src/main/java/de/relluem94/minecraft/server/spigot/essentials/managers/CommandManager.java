package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.discovery.AnnotatedClassLoader;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.registration.CommandWrapper;
import de.relluem94.minecraft.server.spigot.essentials.registries.CommandRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.CommandService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.Plugin;

/**
 * Manages the registration and initialization of all plugin commands.
 *
 * <p>Scans the commands package for annotated command classes, wraps them in
 * {@link CommandWrapper} instances, registers them in a {@link CommandRegistry},
 * and exposes them via the {@link CommandService} stored in the {@link ServiceContext}.
 * </p>
 *
 * @author relluem94
 */
public class CommandManager implements Enable {

  /**
   * Enables the command manager by discovering, initializing, and registering all commands.
   *
   * <p>Loads all annotated command classes from the commands package, wraps each one in a
   * {@link CommandWrapper}, initializes them with the plugin and its {@link ServiceContext},
   * and registers them in a new {@link CommandRegistry}. The resulting {@link CommandService}
   * is then stored in the {@link ServiceContext} for use by other components.
   * </p>
   *
   * @param plugin the plugin instance used to access the {@link ServiceContext} and to
   *               determine the total number of registered commands via
   *               {@link PluginCommandYamlParser}
   */
  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    TranslationService translationService = serviceContext.getTranslationService();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_COMMANDS));

    CommandRegistry commandRegistry = new CommandRegistry();

    AnnotatedClassLoader.loadCommands(
            "de.relluem94.minecraft.server.spigot.essentials.commands",
            getClass().getClassLoader()
        )
        .stream()
        .map(CommandWrapper::new)
        .forEach(wrapper -> {
          wrapper.init(relluEssentialsPlugin, serviceContext);
          commandRegistry.register(wrapper);
        });

    CommandService commandService = new CommandService(commandRegistry);
    serviceContext.setCommandService(commandService);

    int commands = PluginCommandYamlParser.parse(plugin).size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_COMMANDS_REGISTERED, commands));
  }
}