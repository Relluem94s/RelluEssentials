package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ClassDiscoveryHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.registries.CommandRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.CommandService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.wrappers.CommandWrapper;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.Plugin;

public class CommandManager implements Enable {


  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    TranslationService translationService = serviceContext.getTranslationService();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_COMMANDS));

    CommandRegistry commandRegistry = new CommandRegistry();

    discoverAnnotatedCommands().forEach(wrapper -> {
      wrapper.init(relluEssentialsPlugin, serviceContext);
      commandRegistry.register(wrapper);
    });

    CommandService commandService = new CommandService(commandRegistry);
    serviceContext.setCommandService(commandService);

    int commands = PluginCommandYamlParser.parse(plugin).size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_COMMANDS_REGISTERED, commands));
  }

  private List<CommandWrapper> discoverAnnotatedCommands() {
    ClassLoader classLoader = getClass().getClassLoader();
    return ClassDiscoveryHelper.findAnnotatedClasses(
            "de.relluem94.minecraft.server.spigot.essentials.commands",
            CommandName.class,
            CommandConstruct.class,
            classLoader
        )
        .stream()
        .map(this::instantiateCommand)
        .flatMap(Optional::stream)
        .map(CommandWrapper::new)
        .toList();
  }

  private Optional<CommandConstruct> instantiateCommand(Class<?> clazz) {
    try {
      return Optional.of((CommandConstruct) clazz.getDeclaredConstructor().newInstance());
    } catch (Exception e) {
      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          "Failed to instantiate command: " + clazz.getSimpleName());
      return Optional.empty();
    }
  }
}