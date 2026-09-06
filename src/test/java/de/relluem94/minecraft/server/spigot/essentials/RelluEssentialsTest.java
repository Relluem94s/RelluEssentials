package de.relluem94.minecraft.server.spigot.essentials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import de.relluem94.minecraft.server.spigot.essentials.managers.RecipeManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ServiceManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SignManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SkillManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.WorldManager;
import de.relluem94.minecraft.server.spigot.essentials.registries.RelluEssentialsRegistry;
import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

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
    org.bukkit.command.ConsoleCommandSender consoleSender = Mockito.mock(
        org.bukkit.command.ConsoleCommandSender.class);
    Mockito.when(server.getConsoleSender()).thenReturn(consoleSender);
    Mockito.when(server.getLogger()).thenReturn(logger);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getServer).thenReturn(server);

      JavaPluginLoader loader = new JavaPluginLoader(server);
      PluginDescriptionFile description = Mockito.mock(PluginDescriptionFile.class);

      plugin = new RelluEssentials(loader, description, new File("target/test-data"),
          new File("target/test.jar"));
    }

    Field serverField = org.bukkit.plugin.java.JavaPlugin.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(plugin, server);
  }


  @AfterEach
  void tearDown() throws Exception {
    plugin = null;
    resetStaticInstance();
  }

  private void resetStaticInstance() throws Exception {
    Field instanceField = RelluEssentials.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, null);
  }

  @Test
  void constructorShouldSetUnitTestFlag() {
    assertTrue(plugin.isUnitTest());
  }

  @Test
  void getInstanceShouldInitiallyReturnNull() {
    assertNull(RelluEssentials.getInstance());
  }

  @Test
  void onEnableShouldInitializePlugin() {
    FileConfiguration configuration = Mockito.mock(FileConfiguration.class);

    Mockito.when(configuration.getString("database.host")).thenReturn("localhost");
    Mockito.when(configuration.getString("database.user")).thenReturn("test");
    Mockito.when(configuration.getString("database.password")).thenReturn("test");
    Mockito.when(configuration.getInt("database.port")).thenReturn(3306);

    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(configuration).when(spyPlugin).getConfig();

    Server server = Mockito.mock(Server.class);
    org.bukkit.scoreboard.ScoreboardManager scoreboardManager = Mockito.mock(
        org.bukkit.scoreboard.ScoreboardManager.class);
    Mockito.when(server.getScoreboardManager()).thenReturn(scoreboardManager);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(
        Bukkit.class); MockedStatic<RelluEssentialsRegistry> _ = Mockito.mockStatic(
        RelluEssentialsRegistry.class); MockedConstruction<ServiceContext> _ = Mockito.mockConstruction(
        ServiceContext.class, (mock, _) -> {
          Mockito.when(mock.getTranslationService()).thenReturn(Mockito.mock(
              de.relluem94.minecraft.server.spigot.essentials.services.TranslationService.class,
              Mockito.RETURNS_DEEP_STUBS));
          Mockito.when(mock.getSchedulerService()).thenReturn(Mockito.mock(
              de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService.class,
              Mockito.RETURNS_DEEP_STUBS));
        }); MockedConstruction<PersistenceContext> _ = Mockito.mockConstruction(
        PersistenceContext.class); MockedConstruction<ServiceManager> serviceManager = Mockito.mockConstruction(
        ServiceManager.class); MockedConstruction<ConfigManager> configManager = Mockito.mockConstruction(
        ConfigManager.class); MockedConstruction<EnchantmentManager> enchantmentManager = Mockito.mockConstruction(
        EnchantmentManager.class); MockedConstruction<ItemManager> itemManager = Mockito.mockConstruction(
        ItemManager.class); MockedConstruction<DatabaseManager> databaseManager = Mockito.mockConstruction(
        DatabaseManager.class); MockedConstruction<CommandManager> commandManager = Mockito.mockConstruction(
        CommandManager.class); MockedConstruction<SignManager> signManager = Mockito.mockConstruction(
        SignManager.class); MockedConstruction<SkillManager> skillManager = Mockito.mockConstruction(
        SkillManager.class); MockedConstruction<RecipeManager> recipeManager = Mockito.mockConstruction(
        RecipeManager.class); MockedConstruction<BankManager> bankManager = Mockito.mockConstruction(
        BankManager.class); MockedConstruction<ListenerManager> listenerManager = Mockito.mockConstruction(
        ListenerManager.class); MockedConstruction<AutoSaveManager> autoSaveManager = Mockito.mockConstruction(
        AutoSaveManager.class); MockedConstruction<ScoreBoardManager> scoreBoardManager = Mockito.mockConstruction(
        ScoreBoardManager.class); MockedConstruction<WorldManager> worldManager = Mockito.mockConstruction(
        WorldManager.class)) {
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
      assertEquals(1, worldManager.constructed().size());

      ServiceManager service = serviceManager.constructed().getFirst();

      Mockito.verify(service).preEnable(spyPlugin);
      Mockito.verify(service).enable(spyPlugin);

      ConfigManager config = configManager.constructed().getFirst();
      Mockito.verify(config).enable(spyPlugin);

      AutoSaveManager autoSave = autoSaveManager.constructed().getFirst();
      Mockito.verify(autoSave).enable(spyPlugin);

      WorldManager world = worldManager.constructed().getFirst();
      Mockito.verify(world).enable(spyPlugin);

      assertEquals(spyPlugin, RelluEssentials.getInstance());
    }
  }

  @Test
  void onDisableShouldDisableManagers() {
    ServiceContext serviceContext = Mockito.mock(ServiceContext.class, Mockito.RETURNS_DEEP_STUBS);

    PersistenceContext persistenceContext = Mockito.mock(PersistenceContext.class);

    RelluEssentials spyPlugin = Mockito.spy(plugin);

    Mockito.doReturn(serviceContext).when(spyPlugin).getServiceContext();

    Mockito.doReturn(persistenceContext).when(spyPlugin).getPersistenceContext();

    org.bukkit.command.ConsoleCommandSender consoleSender = Mockito.mock(
        org.bukkit.command.ConsoleCommandSender.class);

    Server server = spyPlugin.getServer();
    Mockito.when(server.getConsoleSender()).thenReturn(consoleSender);

    try (MockedConstruction<SudoManager> sudoManager = Mockito.mockConstruction(
        SudoManager.class); MockedConstruction<AutoSaveManager> _ = Mockito.mockConstruction(
        AutoSaveManager.class); MockedConstruction<WorldManager> _ = Mockito.mockConstruction(
        WorldManager.class); MockedConstruction<ConfigManager> _ = Mockito.mockConstruction(
        ConfigManager.class)) {
      AutoSaveManager autoSave = Mockito.mock(AutoSaveManager.class);
      WorldManager world = Mockito.mock(WorldManager.class);
      ConfigManager config = Mockito.mock(ConfigManager.class);

      java.lang.reflect.Field autoSaveField = RelluEssentials.class.getDeclaredField(
          "autoSaveManager");
      autoSaveField.setAccessible(true);
      autoSaveField.set(spyPlugin, autoSave);

      java.lang.reflect.Field worldField = RelluEssentials.class.getDeclaredField("worldManager");
      worldField.setAccessible(true);
      worldField.set(spyPlugin, world);

      java.lang.reflect.Field configField = RelluEssentials.class.getDeclaredField("configManager");
      configField.setAccessible(true);
      configField.set(spyPlugin, config);

      spyPlugin.onDisable();

      Mockito.verify(sudoManager.constructed().getFirst()).disable(spyPlugin);
      Mockito.verify(autoSave).disable(spyPlugin);
      Mockito.verify(world).disable(spyPlugin);
      Mockito.verify(config).disable(spyPlugin);
      Mockito.verify(serviceContext.getNpcService()).despawnAllNpcs();

      Mockito.verify(consoleSender).sendMessage(Constants.PLUGIN_NAME_CONSOLE,
          serviceContext.getTranslationService().get(MessageKey.PLUGIN_MANAGER_STOP_MESSAGE));
    } catch (NoSuchFieldException | IllegalAccessException exception) {
      throw new AssertionError(exception);
    }
  }

  @Test
  void instanceShouldBeSetWhenOnEnableStarts() {
    FileConfiguration configuration = Mockito.mock(FileConfiguration.class);

    Mockito.when(configuration.getString("database.host")).thenReturn("localhost");
    Mockito.when(configuration.getString("database.user")).thenReturn("test");
    Mockito.when(configuration.getString("database.password")).thenReturn("test");
    Mockito.when(configuration.getInt("database.port")).thenReturn(3306);

    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(configuration).when(spyPlugin).getConfig();

    try (MockedStatic<RelluEssentialsRegistry> registry = Mockito.mockStatic(
        RelluEssentialsRegistry.class); MockedConstruction<ServiceContext> _ = Mockito.mockConstruction(
        ServiceContext.class, (mock, _) -> {
          Mockito.when(mock.getTranslationService()).thenReturn(Mockito.mock(
              de.relluem94.minecraft.server.spigot.essentials.services.TranslationService.class,
              Mockito.RETURNS_DEEP_STUBS));
          Mockito.when(mock.getSchedulerService()).thenReturn(Mockito.mock(
              de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService.class,
              Mockito.RETURNS_DEEP_STUBS));
        }); MockedConstruction<PersistenceContext> _ = Mockito.mockConstruction(
        PersistenceContext.class); MockedConstruction<ServiceManager> _ = Mockito.mockConstruction(
        ServiceManager.class); MockedConstruction<ConfigManager> _ = Mockito.mockConstruction(
        ConfigManager.class); MockedConstruction<EnchantmentManager> _ = Mockito.mockConstruction(
        EnchantmentManager.class); MockedConstruction<ItemManager> _ = Mockito.mockConstruction(
        ItemManager.class); MockedConstruction<DatabaseManager> _ = Mockito.mockConstruction(
        DatabaseManager.class); MockedConstruction<CommandManager> _ = Mockito.mockConstruction(
        CommandManager.class); MockedConstruction<SignManager> _ = Mockito.mockConstruction(
        SignManager.class); MockedConstruction<SkillManager> _ = Mockito.mockConstruction(
        SkillManager.class); MockedConstruction<RecipeManager> _ = Mockito.mockConstruction(
        RecipeManager.class); MockedConstruction<BankManager> _ = Mockito.mockConstruction(
        BankManager.class); MockedConstruction<ListenerManager> _ = Mockito.mockConstruction(
        ListenerManager.class); MockedConstruction<AutoSaveManager> _ = Mockito.mockConstruction(
        AutoSaveManager.class); MockedConstruction<ScoreBoardManager> _ = Mockito.mockConstruction(
        ScoreBoardManager.class); MockedConstruction<WorldManager> _ = Mockito.mockConstruction(
        WorldManager.class)) {
      spyPlugin.onEnable();

      assertEquals(spyPlugin, RelluEssentials.getInstance());
      assertTrue(spyPlugin.isUnitTest());

      registry.verify(() -> RelluEssentialsRegistry.initialize(
          spyPlugin.getServiceContext().getTranslationService()));
    }
  }


  @Test
  void onEnableShouldScheduleNpcSpawn() {
    FileConfiguration configuration = Mockito.mock(FileConfiguration.class);
    Mockito.when(configuration.getString("database.host")).thenReturn("localhost");
    Mockito.when(configuration.getString("database.user")).thenReturn("test");
    Mockito.when(configuration.getString("database.password")).thenReturn("test");
    Mockito.when(configuration.getInt("database.port")).thenReturn(3306);

    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(configuration).when(spyPlugin).getConfig();

    Server server = Mockito.mock(Server.class);
    org.bukkit.scoreboard.ScoreboardManager scoreboardManager = Mockito.mock(
        org.bukkit.scoreboard.ScoreboardManager.class);
    Mockito.when(server.getScoreboardManager()).thenReturn(scoreboardManager);

    de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService schedulerService =
        Mockito.mock(de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService.class);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(
        Bukkit.class); MockedStatic<RelluEssentialsRegistry> _ = Mockito.mockStatic(
        RelluEssentialsRegistry.class); MockedConstruction<ServiceContext> _ = Mockito.mockConstruction(
        ServiceContext.class, (mock, _) -> {
          Mockito.when(mock.getTranslationService()).thenReturn(Mockito.mock(
              de.relluem94.minecraft.server.spigot.essentials.services.TranslationService.class,
              Mockito.RETURNS_DEEP_STUBS));
          Mockito.when(mock.getSchedulerService()).thenReturn(schedulerService);
          Mockito.when(mock.getNpcService()).thenReturn(Mockito.mock(
              de.relluem94.minecraft.server.spigot.essentials.services.NpcService.class));
        }); MockedConstruction<PersistenceContext> _ = Mockito.mockConstruction(
        PersistenceContext.class); MockedConstruction<ServiceManager> _ = Mockito.mockConstruction(
        ServiceManager.class); MockedConstruction<ConfigManager> _ = Mockito.mockConstruction(
        ConfigManager.class); MockedConstruction<EnchantmentManager> _ = Mockito.mockConstruction(
        EnchantmentManager.class); MockedConstruction<ItemManager> _ = Mockito.mockConstruction(
        ItemManager.class); MockedConstruction<DatabaseManager> _ = Mockito.mockConstruction(
        DatabaseManager.class); MockedConstruction<CommandManager> _ = Mockito.mockConstruction(
        CommandManager.class); MockedConstruction<SignManager> _ = Mockito.mockConstruction(
        SignManager.class); MockedConstruction<SkillManager> _ = Mockito.mockConstruction(
        SkillManager.class); MockedConstruction<RecipeManager> _ = Mockito.mockConstruction(
        RecipeManager.class); MockedConstruction<BankManager> _ = Mockito.mockConstruction(
        BankManager.class); MockedConstruction<ListenerManager> _ = Mockito.mockConstruction(
        ListenerManager.class); MockedConstruction<AutoSaveManager> _ = Mockito.mockConstruction(
        AutoSaveManager.class); MockedConstruction<ScoreBoardManager> _ = Mockito.mockConstruction(
        ScoreBoardManager.class); MockedConstruction<WorldManager> _ = Mockito.mockConstruction(
        WorldManager.class)) {
      bukkit.when(Bukkit::getServer).thenReturn(server);

      spyPlugin.onEnable();

      Mockito.verify(schedulerService).runTaskLater(Mockito.any(), Mockito.eq(20L));
    }
  }

  @Test
  void onDisableShouldSkipNpcDespawnWhenNpcServiceIsNull() {
    ServiceContext serviceContext = Mockito.mock(ServiceContext.class);
    Mockito.when(serviceContext.getTranslationService()).thenReturn(
        Mockito.mock(de.relluem94.minecraft.server.spigot.essentials.services.TranslationService.class,
            Mockito.RETURNS_DEEP_STUBS));
    Mockito.when(serviceContext.getNpcService()).thenReturn(null);

    PersistenceContext persistenceContext = Mockito.mock(PersistenceContext.class);

    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(serviceContext).when(spyPlugin).getServiceContext();
    Mockito.doReturn(persistenceContext).when(spyPlugin).getPersistenceContext();

    org.bukkit.command.ConsoleCommandSender consoleSender = Mockito.mock(
        org.bukkit.command.ConsoleCommandSender.class);
    Server server = spyPlugin.getServer();
    Mockito.when(server.getConsoleSender()).thenReturn(consoleSender);

    try (MockedConstruction<SudoManager> _ = Mockito.mockConstruction(
        SudoManager.class); MockedConstruction<AutoSaveManager> _ = Mockito.mockConstruction(
        AutoSaveManager.class); MockedConstruction<WorldManager> _ = Mockito.mockConstruction(
        WorldManager.class); MockedConstruction<ConfigManager> _ = Mockito.mockConstruction(
        ConfigManager.class)) {
      AutoSaveManager autoSave = Mockito.mock(AutoSaveManager.class);
      WorldManager world = Mockito.mock(WorldManager.class);
      ConfigManager config = Mockito.mock(ConfigManager.class);

      java.lang.reflect.Field autoSaveField = RelluEssentials.class.getDeclaredField("autoSaveManager");
      autoSaveField.setAccessible(true);
      autoSaveField.set(spyPlugin, autoSave);

      java.lang.reflect.Field worldField = RelluEssentials.class.getDeclaredField("worldManager");
      worldField.setAccessible(true);
      worldField.set(spyPlugin, world);

      java.lang.reflect.Field configField = RelluEssentials.class.getDeclaredField("configManager");
      configField.setAccessible(true);
      configField.set(spyPlugin, config);

      spyPlugin.onDisable();

      Mockito.verify(serviceContext).getNpcService();
      Mockito.verify(serviceContext).getTranslationService();
      Mockito.verifyNoMoreInteractions(serviceContext);
      Mockito.verify(autoSave).disable(spyPlugin);
      Mockito.verify(world).disable(spyPlugin);
      Mockito.verify(config).disable(spyPlugin);
    } catch (NoSuchFieldException | IllegalAccessException exception) {
      throw new AssertionError(exception);
    }
  }

  @Test
  void defaultConstructorShouldNotSetUnitTestFlag() throws Exception {
    Field isUnitTestField = RelluEssentials.class.getDeclaredField("isUnitTest");
    isUnitTestField.setAccessible(true);

    boolean valueOnProtectedConstructorInstance = (boolean) isUnitTestField.get(plugin);

    assertTrue(valueOnProtectedConstructorInstance);

    RelluEssentials freshInstance = Mockito.mock(RelluEssentials.class);
    Mockito.when(freshInstance.isUnitTest()).thenCallRealMethod();

    Field freshField = RelluEssentials.class.getDeclaredField("isUnitTest");
    freshField.setAccessible(true);
    freshField.set(freshInstance, false);

    assertTrue(!freshInstance.isUnitTest());
  }

}