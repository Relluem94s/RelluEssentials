package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ClassDiscoveryHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.wrappers.ListenerWrapper;
import java.util.List;
import java.util.Optional;
import org.bukkit.plugin.Plugin;

public class ListenerManager implements Enable {

  private List<ListenerWrapper> listenerWrapperList;
  private ServiceContext serviceContext;

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    this.serviceContext = relluEssentialsPlugin.getServiceContext();




    TranslationService translationService = serviceContext.getTranslationService();

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_EVENTS));
    listenerWrapperList = discoverAnnotatedListeners()
        .stream()
        .map(this::instantiateListener)
        .flatMap(Optional::stream)
        .map(ListenerWrapper::new)
        .toList();

    listenerWrapperList.forEach(
        listenerWrapper -> listenerWrapper.init(relluEssentialsPlugin, serviceContext));

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_EVENTS_REGISTERED,
            listenerWrapperList.size()));
  }


  private List<Class<? extends ListenerConstruct>> discoverAnnotatedListeners() {
    ClassLoader classLoader = getClass().getClassLoader();
    return ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.listeners",
        ListenerName.class,
        ListenerConstruct.class,
        classLoader
    );
  }

  private Optional<ListenerConstruct> instantiateListener(Class<? extends ListenerConstruct> clazz) {
    try {
      return Optional.of(clazz.getDeclaredConstructor().newInstance());
    } catch (Exception e) {
      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          "Failed to instantiate listener: " + clazz.getSimpleName());
      return Optional.empty();
    }
  }

}
