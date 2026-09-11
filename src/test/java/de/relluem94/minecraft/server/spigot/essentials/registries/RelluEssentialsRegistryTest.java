package de.relluem94.minecraft.server.spigot.essentials.registries;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_COMMAND_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.RelluEssentialsIntegration;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.lang.reflect.Field;
import org.bukkit.command.ConsoleCommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RelluEssentialsRegistryTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private ServerService serverService;

  @Mock
  private ConsoleCommandSender consoleCommandSender;

  @Mock
  private RelluEssentialsIntegration integration;

  @BeforeEach
  void resetSingletonInstance() throws Exception {
    Field instanceField = RelluEssentialsRegistry.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, null);
  }

  @Test
  void getInstanceThrowsIllegalStateExceptionWhenNotInitialized() {
    assertThrows(IllegalStateException.class, RelluEssentialsRegistry::getInstance);
  }

  @Test
  void initializeCreatesSingletonInstance() {
    RelluEssentialsRegistry.initialize(serviceContext);

    assertNotNull(RelluEssentialsRegistry.getInstance());
  }

  @Test
  void initializeReplacesExistingInstance() {
    RelluEssentialsRegistry.initialize(serviceContext);
    RelluEssentialsRegistry firstInstance = RelluEssentialsRegistry.getInstance();

    RelluEssentialsRegistry.initialize(serviceContext);
    RelluEssentialsRegistry secondInstance = RelluEssentialsRegistry.getInstance();

    assertNotSame(firstInstance, secondInstance);
  }

  @Test
  void getIntegrationsReturnsEmptyListAfterInitialization() {
    RelluEssentialsRegistry.initialize(serviceContext);

    assertTrue(RelluEssentialsRegistry.getInstance().getIntegrations().isEmpty());
  }

  @Test
  void registerIntegrationAddsIntegrationToList() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getConsoleSender()).thenReturn(consoleCommandSender);
    when(integration.getPluginName()).thenReturn("TestPlugin");
    when(integration.getPluginVersion()).thenReturn("1.0.0");
    when(translationService.get(MessageKey.INTEGRATION_REGISTERED, "TestPlugin", "1.0.0"))
        .thenReturn("TestPlugin 1.0.0 registered");

    RelluEssentialsRegistry.initialize(serviceContext);
    RelluEssentialsRegistry.getInstance().registerIntegration(integration);

    assertEquals(1, RelluEssentialsRegistry.getInstance().getIntegrations().size());
    assertTrue(RelluEssentialsRegistry.getInstance().getIntegrations().contains(integration));
  }

  @Test
  void registerIntegrationSendsRegistrationMessageToConsole() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getConsoleSender()).thenReturn(consoleCommandSender);
    when(integration.getPluginName()).thenReturn("TestPlugin");
    when(integration.getPluginVersion()).thenReturn("1.0.0");
    when(translationService.get(MessageKey.INTEGRATION_REGISTERED, "TestPlugin", "1.0.0"))
        .thenReturn("TestPlugin 1.0.0 registered");

    RelluEssentialsRegistry.initialize(serviceContext);
    RelluEssentialsRegistry.getInstance().registerIntegration(integration);

    verify(consoleCommandSender).sendMessage(
        PLUGIN_FORMS_COMMAND_PREFIX + " " + "TestPlugin 1.0.0 registered");
  }

  @Test
  void registerIntegrationTriggersOnRelluEssentialsInit() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getConsoleSender()).thenReturn(consoleCommandSender);
    when(integration.getPluginName()).thenReturn("TestPlugin");
    when(integration.getPluginVersion()).thenReturn("1.0.0");
    when(translationService.get(MessageKey.INTEGRATION_REGISTERED, "TestPlugin", "1.0.0"))
        .thenReturn("TestPlugin 1.0.0 registered");

    RelluEssentialsRegistry.initialize(serviceContext);
    RelluEssentialsRegistry registry = RelluEssentialsRegistry.getInstance();
    registry.registerIntegration(integration);

    verify(integration).onRelluEssentialsInit(registry);
  }

  @Test
  void unregisterIntegrationRemovesIntegrationFromList() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getConsoleSender()).thenReturn(consoleCommandSender);
    when(integration.getPluginName()).thenReturn("TestPlugin");
    when(integration.getPluginVersion()).thenReturn("1.0.0");
    when(translationService.get(MessageKey.INTEGRATION_REGISTERED, "TestPlugin", "1.0.0"))
        .thenReturn("TestPlugin 1.0.0 registered");
    when(translationService.get(MessageKey.INTEGRATION_UNREGISTERED, "TestPlugin"))
        .thenReturn("TestPlugin unregistered");

    RelluEssentialsRegistry.initialize(serviceContext);
    RelluEssentialsRegistry registry = RelluEssentialsRegistry.getInstance();
    registry.registerIntegration(integration);
    registry.unregisterIntegration(integration);

    assertTrue(registry.getIntegrations().isEmpty());
  }

  @Test
  void unregisterIntegrationSendsUnregistrationMessageToConsole() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getConsoleSender()).thenReturn(consoleCommandSender);
    when(integration.getPluginName()).thenReturn("TestPlugin");
    when(integration.getPluginVersion()).thenReturn("1.0.0");
    when(translationService.get(MessageKey.INTEGRATION_REGISTERED, "TestPlugin", "1.0.0"))
        .thenReturn("TestPlugin 1.0.0 registered");
    when(translationService.get(MessageKey.INTEGRATION_UNREGISTERED, "TestPlugin"))
        .thenReturn("TestPlugin unregistered");

    RelluEssentialsRegistry.initialize(serviceContext);
    RelluEssentialsRegistry registry = RelluEssentialsRegistry.getInstance();
    registry.registerIntegration(integration);
    registry.unregisterIntegration(integration);

    verify(consoleCommandSender).sendMessage(PLUGIN_FORMS_COMMAND_PREFIX,
        "TestPlugin unregistered");
  }

  @Test
  void unregisterIntegrationTriggersOnRelluEssentialsShutdown() {
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getConsoleSender()).thenReturn(consoleCommandSender);
    when(integration.getPluginName()).thenReturn("TestPlugin");
    when(integration.getPluginVersion()).thenReturn("1.0.0");
    when(translationService.get(MessageKey.INTEGRATION_REGISTERED, "TestPlugin", "1.0.0"))
        .thenReturn("TestPlugin 1.0.0 registered");
    when(translationService.get(MessageKey.INTEGRATION_UNREGISTERED, "TestPlugin"))
        .thenReturn("TestPlugin unregistered");

    RelluEssentialsRegistry.initialize(serviceContext);
    RelluEssentialsRegistry registry = RelluEssentialsRegistry.getInstance();
    registry.registerIntegration(integration);
    registry.unregisterIntegration(integration);

    verify(integration).onRelluEssentialsShutdown();
  }
}