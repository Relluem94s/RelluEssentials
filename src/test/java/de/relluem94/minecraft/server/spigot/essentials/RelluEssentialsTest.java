package de.relluem94.minecraft.server.spigot.essentials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.PersistenceContext;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.managers.AutoSaveManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.BankManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ConfigManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.DatabaseManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.EnchantmentManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ItemManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ListenerManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.MobManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.RecipeManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ServiceManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SignManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SkillManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.WorldManager;
import de.relluem94.minecraft.server.spigot.essentials.persistence.bukkit.BukkitRegistryAdapter;
import de.relluem94.minecraft.server.spigot.essentials.registries.RelluEssentialsRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scoreboard.ScoreboardManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@SuppressWarnings("ResultOfMethodCallIgnored")
class RelluEssentialsTest {

  private RelluEssentials plugin;

  @BeforeEach
  void setUp() throws Exception {
    resetStaticInstance();

    Logger logger = Logger.getLogger("org.bukkit.plugin.java.JavaPluginLoader");
    logger.setUseParentHandlers(false);
    logger.setLevel(Level.OFF);
    for (Handler handler : logger.getHandlers()) {
      handler.setLevel(Level.OFF);
    }

    Server server = Mockito.mock(Server.class);
    ConsoleCommandSender consoleSender = Mockito.mock(ConsoleCommandSender.class);
    Mockito.when(server.getConsoleSender()).thenReturn(consoleSender);
    Mockito.when(server.getLogger()).thenReturn(logger);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getServer).thenReturn(server);

      @SuppressWarnings("deprecation")
      JavaPluginLoader loader = new JavaPluginLoader(server);
      PluginDescriptionFile description = Mockito.mock(PluginDescriptionFile.class);

      plugin = new RelluEssentials(loader, description, new File("target/test-data"), new File("target/test.jar"));
    }

    Field serverField = org.bukkit.plugin.java.JavaPlugin.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(plugin, server);
  }

  @AfterEach
  void tearDown() throws Exception {
    plugin = null;
    resetStaticInstance();

    Field serverField = Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, null);
  }

  private void resetStaticInstance() throws Exception {
    Field instanceField = RelluEssentials.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, null);
  }

  private FileConfiguration buildDatabaseConfiguration() {
    FileConfiguration configuration = Mockito.mock(FileConfiguration.class);
    Mockito.when(configuration.getString("database.host")).thenReturn("localhost");
    Mockito.when(configuration.getString("database.user")).thenReturn("test");
    Mockito.when(configuration.getString("database.password")).thenReturn("test");
    Mockito.when(configuration.getInt("database.port")).thenReturn(3306);
    return configuration;
  }

  private void stubServiceContextMock(ServiceContext mock, SchedulerService schedulerService, NpcService npcService) {
    Mockito.when(mock.getTranslationService())
        .thenReturn(Mockito.mock(TranslationService.class, Mockito.RETURNS_DEEP_STUBS));
    Mockito.when(mock.getSchedulerService()).thenReturn(schedulerService);
    Mockito.when(mock.getNpcService()).thenReturn(npcService);
    Mockito.when(mock.getPluginMetadataService())
        .thenReturn(Mockito.mock(PluginMetadataService.class, Mockito.RETURNS_DEEP_STUBS));
  }

  private void injectField(Object target, String fieldName, Object value) throws Exception {
    Field field = RelluEssentials.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @Test
  void getInstanceShouldInitiallyReturnNull() {
    assertNull(RelluEssentials.getInstance());
  }

  @Test
  void onEnableShouldInitializePlugin() {
    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(buildDatabaseConfiguration()).when(spyPlugin).getConfig();

    Server server = Mockito.mock(Server.class);
    Mockito.when(server.getScoreboardManager()).thenReturn(Mockito.mock(ScoreboardManager.class));

    SchedulerService schedulerService = Mockito.mock(SchedulerService.class, Mockito.RETURNS_DEEP_STUBS);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class);
        MockedStatic<RelluEssentialsRegistry> _ = Mockito.mockStatic(RelluEssentialsRegistry.class);
        MockedConstruction<ServiceContext> _ = Mockito.mockConstruction(ServiceContext.class,
            (mock, _) -> stubServiceContextMock(mock, schedulerService, Mockito.mock(NpcService.class)));
        MockedConstruction<PersistenceContext> _ = Mockito.mockConstruction(PersistenceContext.class);
        MockedConstruction<ServiceManager> serviceManager = Mockito.mockConstruction(ServiceManager.class);
        MockedConstruction<ConfigManager> configManager = Mockito.mockConstruction(ConfigManager.class);
        MockedConstruction<EnchantmentManager> enchantmentManager = Mockito.mockConstruction(EnchantmentManager.class);
        MockedConstruction<ItemManager> itemManager = Mockito.mockConstruction(ItemManager.class);
        MockedConstruction<DatabaseManager> databaseManager = Mockito.mockConstruction(DatabaseManager.class);
        MockedConstruction<CommandManager> commandManager = Mockito.mockConstruction(CommandManager.class);
        MockedConstruction<SignManager> signManager = Mockito.mockConstruction(SignManager.class);
        MockedConstruction<SkillManager> skillManager = Mockito.mockConstruction(SkillManager.class);
        MockedConstruction<RecipeManager> recipeManager = Mockito.mockConstruction(RecipeManager.class);
        MockedConstruction<BankManager> bankManager = Mockito.mockConstruction(BankManager.class);
        MockedConstruction<ListenerManager> listenerManager = Mockito.mockConstruction(ListenerManager.class);
        MockedConstruction<AutoSaveManager> autoSaveManager = Mockito.mockConstruction(AutoSaveManager.class);
        MockedConstruction<ScoreBoardManager> scoreBoardManager = Mockito.mockConstruction(ScoreBoardManager.class);
        MockedConstruction<BukkitRegistryAdapter> _ = Mockito.mockConstruction(BukkitRegistryAdapter.class);
        MockedConstruction<MobManager> mobManager = Mockito.mockConstruction(MobManager.class);
        MockedConstruction<WorldManager> worldManager = Mockito.mockConstruction(WorldManager.class)) {
      bukkit.when(Bukkit::getServer).thenReturn(server);

      spyPlugin.onEnable();

      assertNotNull(spyPlugin.getServiceContext());
      assertNotNull(spyPlugin.getPersistenceContext());

      assertEquals(1, serviceManager.constructed().size());
      assertEquals(1, configManager.constructed().size());
      assertEquals(1, enchantmentManager.constructed().size());
      assertEquals(1, itemManager.constructed().size());
      assertEquals(1, databaseManager.constructed().size());
      assertEquals(1, commandManager.constructed().size());
      assertEquals(1, signManager.constructed().size());
      assertEquals(1, skillManager.constructed().size());
      assertEquals(1, recipeManager.constructed().size());
      assertEquals(1, bankManager.constructed().size());
      assertEquals(1, listenerManager.constructed().size());
      assertEquals(1, autoSaveManager.constructed().size());
      assertEquals(1, scoreBoardManager.constructed().size());
      assertEquals(1, mobManager.constructed().size());
      assertEquals(1, worldManager.constructed().size());

      ServiceManager service = serviceManager.constructed().getFirst();
      Mockito.verify(service).preEnable(spyPlugin);
      Mockito.verify(service).enable(spyPlugin);

      Mockito.verify(configManager.constructed().getFirst()).enable(spyPlugin);
      Mockito.verify(autoSaveManager.constructed().getFirst()).enable(spyPlugin);
      Mockito.verify(worldManager.constructed().getFirst()).enable(spyPlugin);
      Mockito.verify(mobManager.constructed().getFirst()).enable(spyPlugin);

      assertEquals(spyPlugin, RelluEssentials.getInstance());
    }
  }

  @Test
  void onDisableShouldDisableManagers() throws Exception {
    ServiceContext serviceContext = Mockito.mock(ServiceContext.class, Mockito.RETURNS_DEEP_STUBS);

    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(serviceContext).when(spyPlugin).getServiceContext();
    Mockito.doReturn(Mockito.mock(PersistenceContext.class)).when(spyPlugin).getPersistenceContext();

    ConsoleCommandSender consoleSender = Mockito.mock(ConsoleCommandSender.class);
    Mockito.when(spyPlugin.getServer().getConsoleSender()).thenReturn(consoleSender);

    AutoSaveManager autoSave = Mockito.mock(AutoSaveManager.class);
    WorldManager world = Mockito.mock(WorldManager.class);
    ConfigManager config = Mockito.mock(ConfigManager.class);
    MobManager mob = Mockito.mock(MobManager.class);

    injectField(spyPlugin, "autoSaveManager", autoSave);
    injectField(spyPlugin, "worldManager", world);
    injectField(spyPlugin, "configManager", config);
    injectField(spyPlugin, "mobManager", mob);

    try (MockedConstruction<SudoManager> sudoManager = Mockito.mockConstruction(SudoManager.class)) {
      spyPlugin.onDisable();

      Mockito.verify(sudoManager.constructed().getFirst()).disable(spyPlugin);
      Mockito.verify(autoSave).disable(spyPlugin);
      Mockito.verify(world).disable(spyPlugin);
      Mockito.verify(config).disable(spyPlugin);
      Mockito.verify(mob).disable(spyPlugin);
      Mockito.verify(serviceContext.getNpcService()).despawnAllNpcs();
      Mockito.verify(consoleSender).sendMessage(Constants.PLUGIN_NAME_CONSOLE + serviceContext
          .getTranslationService()
          .get(MessageKey.PLUGIN_MANAGER_STOP_MESSAGE));
    }
  }

  @Test
  void instanceShouldBeSetWhenOnEnableStarts() {
    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(buildDatabaseConfiguration()).when(spyPlugin).getConfig();

    SchedulerService schedulerService = Mockito.mock(SchedulerService.class, Mockito.RETURNS_DEEP_STUBS);

    try (MockedStatic<RelluEssentialsRegistry> registry = Mockito.mockStatic(RelluEssentialsRegistry.class);
        MockedConstruction<ServiceContext> _ = Mockito.mockConstruction(ServiceContext.class,
            (mock, _) -> stubServiceContextMock(mock, schedulerService, Mockito.mock(NpcService.class)));
        MockedConstruction<PersistenceContext> _ = Mockito.mockConstruction(PersistenceContext.class);
        MockedConstruction<ServiceManager> _ = Mockito.mockConstruction(ServiceManager.class);
        MockedConstruction<ConfigManager> _ = Mockito.mockConstruction(ConfigManager.class);
        MockedConstruction<EnchantmentManager> _ = Mockito.mockConstruction(EnchantmentManager.class);
        MockedConstruction<ItemManager> _ = Mockito.mockConstruction(ItemManager.class);
        MockedConstruction<DatabaseManager> _ = Mockito.mockConstruction(DatabaseManager.class);
        MockedConstruction<CommandManager> _ = Mockito.mockConstruction(CommandManager.class);
        MockedConstruction<SignManager> _ = Mockito.mockConstruction(SignManager.class);
        MockedConstruction<SkillManager> _ = Mockito.mockConstruction(SkillManager.class);
        MockedConstruction<RecipeManager> _ = Mockito.mockConstruction(RecipeManager.class);
        MockedConstruction<BankManager> _ = Mockito.mockConstruction(BankManager.class);
        MockedConstruction<ListenerManager> _ = Mockito.mockConstruction(ListenerManager.class);
        MockedConstruction<AutoSaveManager> _ = Mockito.mockConstruction(AutoSaveManager.class);
        MockedConstruction<ScoreBoardManager> _ = Mockito.mockConstruction(ScoreBoardManager.class);
        MockedConstruction<BukkitRegistryAdapter> _ = Mockito.mockConstruction(BukkitRegistryAdapter.class);
        MockedConstruction<MobManager> _ = Mockito.mockConstruction(MobManager.class);
        MockedConstruction<WorldManager> _ = Mockito.mockConstruction(WorldManager.class)) {
      spyPlugin.onEnable();

      assertEquals(spyPlugin, RelluEssentials.getInstance());
      registry.verify(() -> RelluEssentialsRegistry.initialize(spyPlugin.getServiceContext()));
    }
  }

  @Test
  void onEnableShouldScheduleNpcSpawn() {
    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(buildDatabaseConfiguration()).when(spyPlugin).getConfig();

    Server server = Mockito.mock(Server.class);
    Mockito.when(server.getScoreboardManager()).thenReturn(Mockito.mock(ScoreboardManager.class));

    NpcService npcService = Mockito.mock(NpcService.class);
    SchedulerService schedulerService = Mockito.mock(SchedulerService.class);

    Mockito.doAnswer(invocation -> {
      Runnable task = invocation.getArgument(0);
      task.run();
      return null;
    }).when(schedulerService).runTaskLater(Mockito.any(), Mockito.eq(20L));

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class);
        MockedStatic<RelluEssentialsRegistry> _ = Mockito.mockStatic(RelluEssentialsRegistry.class);
        MockedConstruction<ServiceContext> _ = Mockito.mockConstruction(ServiceContext.class,
            (mock, _) -> stubServiceContextMock(mock, schedulerService, npcService));
        MockedConstruction<PersistenceContext> _ = Mockito.mockConstruction(PersistenceContext.class);
        MockedConstruction<ServiceManager> _ = Mockito.mockConstruction(ServiceManager.class);
        MockedConstruction<ConfigManager> _ = Mockito.mockConstruction(ConfigManager.class);
        MockedConstruction<EnchantmentManager> _ = Mockito.mockConstruction(EnchantmentManager.class);
        MockedConstruction<ItemManager> _ = Mockito.mockConstruction(ItemManager.class);
        MockedConstruction<DatabaseManager> _ = Mockito.mockConstruction(DatabaseManager.class);
        MockedConstruction<CommandManager> _ = Mockito.mockConstruction(CommandManager.class);
        MockedConstruction<SignManager> _ = Mockito.mockConstruction(SignManager.class);
        MockedConstruction<SkillManager> _ = Mockito.mockConstruction(SkillManager.class);
        MockedConstruction<RecipeManager> _ = Mockito.mockConstruction(RecipeManager.class);
        MockedConstruction<BankManager> _ = Mockito.mockConstruction(BankManager.class);
        MockedConstruction<ListenerManager> _ = Mockito.mockConstruction(ListenerManager.class);
        MockedConstruction<AutoSaveManager> _ = Mockito.mockConstruction(AutoSaveManager.class);
        MockedConstruction<ScoreBoardManager> _ = Mockito.mockConstruction(ScoreBoardManager.class);
        MockedConstruction<BukkitRegistryAdapter> _ = Mockito.mockConstruction(BukkitRegistryAdapter.class);
        MockedConstruction<MobManager> _ = Mockito.mockConstruction(MobManager.class);
        MockedConstruction<WorldManager> _ = Mockito.mockConstruction(WorldManager.class)) {
      bukkit.when(Bukkit::getServer).thenReturn(server);

      spyPlugin.onEnable();

      Mockito.verify(schedulerService).runTaskLater(Mockito.any(), Mockito.eq(20L));
      Mockito.verify(npcService).loadAndSpawnNpcsInLoadedChunks();
    }
  }

  @Test
  void onDisableShouldSkipNpcDespawnWhenNpcServiceIsNull() throws Exception {
    ServiceContext serviceContext = Mockito.mock(ServiceContext.class);
    Mockito.when(serviceContext.getTranslationService())
        .thenReturn(Mockito.mock(TranslationService.class, Mockito.RETURNS_DEEP_STUBS));
    Mockito.when(serviceContext.getNpcService()).thenReturn(null);

    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(serviceContext).when(spyPlugin).getServiceContext();
    Mockito.doReturn(Mockito.mock(PersistenceContext.class)).when(spyPlugin).getPersistenceContext();

    ConsoleCommandSender consoleSender = Mockito.mock(ConsoleCommandSender.class);
    Mockito.when(spyPlugin.getServer().getConsoleSender()).thenReturn(consoleSender);

    AutoSaveManager autoSave = Mockito.mock(AutoSaveManager.class);
    WorldManager world = Mockito.mock(WorldManager.class);
    ConfigManager config = Mockito.mock(ConfigManager.class);
    MobManager mob = Mockito.mock(MobManager.class);

    injectField(spyPlugin, "autoSaveManager", autoSave);
    injectField(spyPlugin, "worldManager", world);
    injectField(spyPlugin, "configManager", config);
    injectField(spyPlugin, "mobManager", mob);

    try (MockedConstruction<SudoManager> _ = Mockito.mockConstruction(SudoManager.class)) {
      spyPlugin.onDisable();

      Mockito.verify(serviceContext).getNpcService();
      Mockito.verify(serviceContext).getTranslationService();
      Mockito.verifyNoMoreInteractions(serviceContext);
      Mockito.verify(autoSave).disable(spyPlugin);
      Mockito.verify(world).disable(spyPlugin);
      Mockito.verify(config).disable(spyPlugin);
      Mockito.verify(mob).disable(spyPlugin);
    }
  }
}