package de.relluem94.minecraft.server.spigot.essentials.wrappers;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListenerWrapperTest {

  @Mock
  private JavaPlugin javaPlugin;

  @Mock
  private Server server;

  @Mock
  private PluginManager pluginManager;

  @Mock
  private ListenerConstruct listenerConstruct;

  private ListenerWrapper listenerWrapper;

  @BeforeEach
  void setUp() throws Exception {
    Mockito.when(server.getPluginManager()).thenReturn(pluginManager);

    Field serverField = Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, server);

    listenerWrapper = new ListenerWrapper(listenerConstruct);
  }

  @AfterEach
  void tearDown() throws Exception {
    listenerWrapper = null;

    Field serverField = Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, null);
  }

  @Test
  void initInjectsContextAndRegistersEventsAndMarksInitialised() throws Exception {
    ServiceContext serviceContext = new ServiceContext();

    listenerWrapper.init(javaPlugin, serviceContext);

    Field initialisedField = ListenerWrapper.class.getDeclaredField("initialised");
    initialisedField.setAccessible(true);

    Assertions.assertAll(
        () -> Assertions.assertTrue((boolean) initialisedField.get(listenerWrapper)),
        () -> Mockito.verify(listenerConstruct).injectContext(serviceContext),
        () -> Mockito.verify(pluginManager).registerEvents(listenerConstruct, javaPlugin)
    );
  }

  @Test
  void initDoesNothingWhenAlreadyInitialised() {
    ServiceContext serviceContext = new ServiceContext();

    listenerWrapper.init(javaPlugin, serviceContext);
    listenerWrapper.init(javaPlugin, serviceContext);

    Mockito.verify(listenerConstruct, Mockito.times(1)).injectContext(serviceContext);
    Mockito.verify(pluginManager, Mockito.times(1)).registerEvents(listenerConstruct, javaPlugin);
  }
}