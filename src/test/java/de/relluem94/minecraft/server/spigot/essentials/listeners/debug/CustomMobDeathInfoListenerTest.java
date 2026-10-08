package de.relluem94.minecraft.server.spigot.essentials.listeners.debug;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsMobDeathEvent;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import de.relluem94.minecraft.server.spigot.essentials.services.MobService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Logger;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomMobDeathInfoListenerTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private MobService mobService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private Plugin plugin;

  @Mock
  private RelluEssentialsMobDeathEvent event;

  @Mock
  private CustomMob customMob;

  @Mock
  private CustomMobDefinition definition;

  @Mock
  private LivingEntity livingEntity;

  @Mock
  private RelluEssentialsNamespacedKey definitionKey;

  private CustomMobDeathInfoListener listener;

  @BeforeEach
  void setUp() {
    listener = new CustomMobDeathInfoListener();
    listener.injectContext(serviceContext);
  }

  private Logger buildSilentLogger() {
    Logger logger = Logger.getLogger(CustomMobDeathInfoListenerTest.class.getName());
    for (Handler handler : logger.getHandlers()) {
      logger.removeHandler(handler);
    }
    logger.setUseParentHandlers(false);
    return logger;
  }

  @Test
  void onCustomMobDeathLogsAllMobAndServiceInfo() {
    UUID mobUuid = UUID.randomUUID();
    Logger logger = buildSilentLogger();

    when(event.getCustomMob()).thenReturn(customMob);
    when(event.getDefinition()).thenReturn(definition);
    when(customMob.getLivingEntity()).thenReturn(livingEntity);
    when(livingEntity.getUniqueId()).thenReturn(mobUuid);
    when(livingEntity.getType()).thenReturn(EntityType.ZOMBIE);
    when(definition.key()).thenReturn(definitionKey);
    when(definitionKey.toString()).thenReturn("rellu:zombie_boss");
    when(serviceContext.getMobService()).thenReturn(mobService);
    when(mobService.countAliveMobs()).thenReturn(3);
    when(mobService.countSpawnedMobs()).thenReturn(10);
    when(mobService.findAllDefinitions()).thenReturn(List.of(definition));
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getLogger()).thenReturn(logger);

    listener.onCustomMobDeath(event);

    verify(mobService).countAliveMobs();
    verify(mobService).countSpawnedMobs();
    verify(mobService).findAllDefinitions();
    verify(plugin).getLogger();
  }

  @Test
  void onCustomMobDeathUsesCorrectUuidFromLivingEntity() {
    UUID expectedUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
    Logger logger = buildSilentLogger();

    when(event.getCustomMob()).thenReturn(customMob);
    when(event.getDefinition()).thenReturn(definition);
    when(customMob.getLivingEntity()).thenReturn(livingEntity);
    when(livingEntity.getUniqueId()).thenReturn(expectedUuid);
    when(livingEntity.getType()).thenReturn(EntityType.SKELETON);
    when(definition.key()).thenReturn(definitionKey);
    when(definitionKey.toString()).thenReturn("rellu:skeleton_boss");
    when(serviceContext.getMobService()).thenReturn(mobService);
    when(mobService.countAliveMobs()).thenReturn(0);
    when(mobService.countSpawnedMobs()).thenReturn(0);
    when(mobService.findAllDefinitions()).thenReturn(List.of());
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getLogger()).thenReturn(logger);

    listener.onCustomMobDeath(event);

    verify(livingEntity, atLeastOnce()).getUniqueId();
  }

  @Test
  void onCustomMobDeathUsesEntityTypeNameFromLivingEntity() {
    Logger logger = buildSilentLogger();

    when(event.getCustomMob()).thenReturn(customMob);
    when(event.getDefinition()).thenReturn(definition);
    when(customMob.getLivingEntity()).thenReturn(livingEntity);
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.getType()).thenReturn(EntityType.CREEPER);
    when(definition.key()).thenReturn(definitionKey);
    when(definitionKey.toString()).thenReturn("rellu:creeper_boss");
    when(serviceContext.getMobService()).thenReturn(mobService);
    when(mobService.countAliveMobs()).thenReturn(1);
    when(mobService.countSpawnedMobs()).thenReturn(5);
    when(mobService.findAllDefinitions()).thenReturn(List.of(definition));
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getLogger()).thenReturn(logger);

    listener.onCustomMobDeath(event);

    verify(livingEntity, atLeastOnce()).getType();
  }

  @Test
  void onCustomMobDeathUsesDefinitionKeyToString() {
    Logger logger = buildSilentLogger();

    when(event.getCustomMob()).thenReturn(customMob);
    when(event.getDefinition()).thenReturn(definition);
    when(customMob.getLivingEntity()).thenReturn(livingEntity);
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.getType()).thenReturn(EntityType.ZOMBIE);
    when(definition.key()).thenReturn(definitionKey);
    when(definitionKey.toString()).thenReturn("rellu:custom_key");
    when(serviceContext.getMobService()).thenReturn(mobService);
    when(mobService.countAliveMobs()).thenReturn(2);
    when(mobService.countSpawnedMobs()).thenReturn(7);
    when(mobService.findAllDefinitions()).thenReturn(List.of(definition, definition));
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getLogger()).thenReturn(logger);

    listener.onCustomMobDeath(event);

    verify(definition, atLeastOnce()).key();
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void onCustomMobDeathPropagatesNullPointerExceptionWhenEventIsNull() {
    org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> listener.onCustomMobDeath(null));
  }

  @Test
  void onCustomMobDeathPropagatesExceptionWhenMobServiceThrows() {
    when(event.getCustomMob()).thenReturn(customMob);
    when(event.getDefinition()).thenReturn(definition);
    when(customMob.getLivingEntity()).thenReturn(livingEntity);
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.getType()).thenReturn(EntityType.ZOMBIE);
    when(definition.key()).thenReturn(definitionKey);
    when(definitionKey.toString()).thenReturn("rellu:zombie_boss");
    when(serviceContext.getMobService()).thenReturn(mobService);
    when(mobService.countAliveMobs()).thenThrow(new RuntimeException("service failure"));

    org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> listener.onCustomMobDeath(event));
  }

  @Test
  void onCustomMobDeathPropagatesExceptionWhenPluginMetadataServiceThrows() {
    when(event.getCustomMob()).thenReturn(customMob);
    when(event.getDefinition()).thenReturn(definition);
    when(customMob.getLivingEntity()).thenReturn(livingEntity);
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.getType()).thenReturn(EntityType.ZOMBIE);
    when(definition.key()).thenReturn(definitionKey);
    when(definitionKey.toString()).thenReturn("rellu:zombie_boss");
    when(serviceContext.getMobService()).thenReturn(mobService);
    when(mobService.countAliveMobs()).thenReturn(1);
    when(mobService.countSpawnedMobs()).thenReturn(5);
    when(mobService.findAllDefinitions()).thenReturn(List.of(definition));
    when(serviceContext.getPluginMetadataService()).thenThrow(new RuntimeException("metadata failure"));

    org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> listener.onCustomMobDeath(event));
  }

  @Test
  void injectContextAssignsServiceContext() {
    ServiceContext newContext = mock(ServiceContext.class);
    MobService newMobService = mock(MobService.class);
    PluginMetadataService newPluginMetadataService = mock(PluginMetadataService.class);
    Plugin newPlugin = mock(Plugin.class);
    Logger logger = buildSilentLogger();

    when(event.getCustomMob()).thenReturn(customMob);
    when(event.getDefinition()).thenReturn(definition);
    when(customMob.getLivingEntity()).thenReturn(livingEntity);
    when(livingEntity.getUniqueId()).thenReturn(UUID.randomUUID());
    when(livingEntity.getType()).thenReturn(EntityType.ZOMBIE);
    when(definition.key()).thenReturn(definitionKey);
    when(definitionKey.toString()).thenReturn("rellu:zombie_boss");
    when(newContext.getMobService()).thenReturn(newMobService);
    when(newMobService.countAliveMobs()).thenReturn(0);
    when(newMobService.countSpawnedMobs()).thenReturn(0);
    when(newMobService.findAllDefinitions()).thenReturn(List.of());
    when(newContext.getPluginMetadataService()).thenReturn(newPluginMetadataService);
    when(newPluginMetadataService.getPlugin()).thenReturn(newPlugin);
    when(newPlugin.getLogger()).thenReturn(logger);

    listener.injectContext(newContext);
    listener.onCustomMobDeath(event);

    verify(newContext, atLeastOnce()).getMobService();
    verify(newContext, atLeastOnce()).getPluginMetadataService();
  }
}