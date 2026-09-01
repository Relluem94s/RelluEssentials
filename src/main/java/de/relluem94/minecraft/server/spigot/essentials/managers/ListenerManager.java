package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.discovery.AnnotatedClassLoader;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.registration.ListenerWrapper;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.plugin.Plugin;

/**
 * Manages the registration and initialization of all plugin event listeners.
 *
 * <p>Scans the listeners package for annotated listener classes, wraps them in
 * {@link ListenerWrapper} instances, and initializes them with the plugin and its
 * {@link ServiceContext}.
 * </p>
 *
 * @author relluem94
 */
public class ListenerManager implements Enable {

  private ServiceContext serviceContext;

  /**
   * Enables the listener manager by discovering, initializing, and registering all listeners.
   *
   * <p>Loads all annotated listener classes from the listeners package, wraps each one in a
   * {@link ListenerWrapper}, and initializes them with the plugin and its {@link ServiceContext}.
   * </p>
   *
   * @param plugin the plugin instance used to access the {@link ServiceContext} and to register all
   *               discovered listeners against the Bukkit event system
   */
  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    this.serviceContext = relluEssentialsPlugin.getServiceContext();

    TranslationService translationService = serviceContext.getTranslationService();

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_EVENTS));
    List<ListenerWrapper> listenerWrapperList = AnnotatedClassLoader.loadListeners(
            "de.relluem94.minecraft.server.spigot.essentials.listeners",
            getClass().getClassLoader()
        )
        .stream().map(ListenerWrapper::new).toList();

    listenerWrapperList.forEach(
        listenerWrapper -> listenerWrapper.init(relluEssentialsPlugin, serviceContext));

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_EVENTS_REGISTERED,
            listenerWrapperList.size()));
  }
}
