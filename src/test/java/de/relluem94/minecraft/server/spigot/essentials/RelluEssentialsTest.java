package de.relluem94.minecraft.server.spigot.essentials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.PersistenceContext;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
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
  void setUp() {
    Logger.getLogger("org.bukkit.plugin.java.JavaPluginLoader")
        .setLevel(Level.OFF);
    Server server = Mockito.mock(Server.class);
    Logger logger = Logger.getLogger("Minecraft");

    Mockito.when(server.getLogger()).thenReturn(logger);

    try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getServer).thenReturn(server);

      JavaPluginLoader loader = new JavaPluginLoader(server);

      PluginDescriptionFile description = Mockito.mock(PluginDescriptionFile.class);

      plugin = new RelluEssentials(
          loader,
          description,
          new File("target/test-data"),
          new File("target/test.jar")
      );
    }
  }

  @AfterEach
  void tearDown() {
    plugin = null;
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
    org.bukkit.scoreboard.ScoreboardManager scoreboardManager =
        Mockito.mock(org.bukkit.scoreboard.ScoreboardManager.class);
    Mockito.when(server.getScoreboardManager()).thenReturn(scoreboardManager);

    try (
        MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class);
        MockedStatic<ChatHelper> chatHelper = Mockito.mockStatic(ChatHelper.class);
        MockedStatic<RelluEssentialsRegistry> registry =
            Mockito.mockStatic(RelluEssentialsRegistry.class);
        MockedConstruction<ServiceManager> serviceManager =
            Mockito.mockConstruction(ServiceManager.class);
        MockedConstruction<ConfigManager> configManager =
            Mockito.mockConstruction(ConfigManager.class);
        MockedConstruction<EnchantmentManager> enchantmentManager =
            Mockito.mockConstruction(EnchantmentManager.class);
        MockedConstruction<ItemManager> itemManager =
            Mockito.mockConstruction(ItemManager.class);
        MockedConstruction<DatabaseManager> databaseManager =
            Mockito.mockConstruction(DatabaseManager.class);
        MockedConstruction<CommandManager> commandManager =
            Mockito.mockConstruction(CommandManager.class);
        MockedConstruction<SignManager> signManager =
            Mockito.mockConstruction(SignManager.class);
        MockedConstruction<SkillManager> skillManager =
            Mockito.mockConstruction(SkillManager.class);
        MockedConstruction<RecipeManager> recipeManager =
            Mockito.mockConstruction(RecipeManager.class);
        MockedConstruction<BankManager> bankManager =
            Mockito.mockConstruction(BankManager.class);
        MockedConstruction<ListenerManager> listenerManager =
            Mockito.mockConstruction(ListenerManager.class);
        MockedConstruction<AutoSaveManager> autoSaveManager =
            Mockito.mockConstruction(AutoSaveManager.class);
        MockedConstruction<ScoreBoardManager> scoreBoardManager =
            Mockito.mockConstruction(ScoreBoardManager.class);
        MockedConstruction<WorldManager> worldManager =
            Mockito.mockConstruction(WorldManager.class)
    ) {
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

      ServiceManager service = serviceManager.constructed().get(0);

      Mockito.verify(service).preEnable(spyPlugin);
      Mockito.verify(service).enable(spyPlugin);

      ConfigManager config = configManager.constructed().get(0);
      Mockito.verify(config).enable(spyPlugin);

      AutoSaveManager autoSave = autoSaveManager.constructed().get(0);
      Mockito.verify(autoSave).enable(spyPlugin);

      WorldManager world = worldManager.constructed().get(0);
      Mockito.verify(world).enable(spyPlugin);

      assertEquals(spyPlugin, RelluEssentials.getInstance());
    }
  }

  @Test
  void onDisableShouldDisableManagers() {
    ServiceContext serviceContext =
        Mockito.mock(ServiceContext.class, Mockito.RETURNS_DEEP_STUBS);

    PersistenceContext persistenceContext =
        Mockito.mock(PersistenceContext.class);

    RelluEssentials spyPlugin = Mockito.spy(plugin);

    Mockito.doReturn(serviceContext)
        .when(spyPlugin)
        .getServiceContext();

    Mockito.doReturn(persistenceContext)
        .when(spyPlugin)
        .getPersistenceContext();

    try (
        MockedStatic<ChatHelper> chatHelper = Mockito.mockStatic(ChatHelper.class);
        MockedConstruction<SudoManager> sudoManager =
            Mockito.mockConstruction(SudoManager.class);
        MockedConstruction<AutoSaveManager> autoSaveManager =
            Mockito.mockConstruction(AutoSaveManager.class);
        MockedConstruction<WorldManager> worldManager =
            Mockito.mockConstruction(WorldManager.class);
        MockedConstruction<ConfigManager> configManager =
            Mockito.mockConstruction(ConfigManager.class)
    ) {
      /*
       * Die privaten Manager-Felder werden hier gesetzt, damit onDisable()
       * die tatsächlich erzeugten Mockito-Mocks verwendet.
       */
      java.lang.reflect.Field autoSaveField =
          RelluEssentials.class.getDeclaredField("autoSaveManager");
      autoSaveField.setAccessible(true);
      autoSaveField.set(spyPlugin, autoSaveManager.constructed().isEmpty()
          ? Mockito.mock(AutoSaveManager.class)
          : autoSaveManager.constructed().get(0));

      java.lang.reflect.Field worldField =
          RelluEssentials.class.getDeclaredField("worldManager");
      worldField.setAccessible(true);
      worldField.set(spyPlugin, worldManager.constructed().isEmpty()
          ? Mockito.mock(WorldManager.class)
          : worldManager.constructed().get(0));

      java.lang.reflect.Field configField =
          RelluEssentials.class.getDeclaredField("configManager");
      configField.setAccessible(true);
      configField.set(spyPlugin, configManager.constructed().isEmpty()
          ? Mockito.mock(ConfigManager.class)
          : configManager.constructed().get(0));

      /*
       * Die Konstruktor-Mocks müssen vor onDisable() erzeugt werden.
       */
      SudoManager sudo = Mockito.mock(SudoManager.class);
      AutoSaveManager autoSave = Mockito.mock(AutoSaveManager.class);
      WorldManager world = Mockito.mock(WorldManager.class);
      ConfigManager config = Mockito.mock(ConfigManager.class);

      autoSaveField.set(spyPlugin, autoSave);
      worldField.set(spyPlugin, world);
      configField.set(spyPlugin, config);

      spyPlugin.onDisable();

      Mockito.verify(sudoManager.constructed().get(0))
          .disable(spyPlugin);

      Mockito.verify(autoSave)
          .disable(spyPlugin);

      Mockito.verify(world)
          .disable(spyPlugin);

      Mockito.verify(config)
          .disable(spyPlugin);

      Mockito.verify(serviceContext.getNpcService())
          .despawnAllNPCs();

      chatHelper.verify(() ->
          ChatHelper.consoleSendMessage(
              Constants.PLUGIN_NAME_CONSOLE,
              serviceContext.getTranslationService()
                  .get(MessageKey.PLUGIN_MANAGER_STOP_MESSAGE)
          ));
    } catch (NoSuchFieldException | IllegalAccessException exception) {
      throw new AssertionError(exception);
    }
  }

  @Test
  void instanceShouldBeSetWhenOnEnableStarts() {
    FileConfiguration configuration = Mockito.mock(FileConfiguration.class);

    Mockito.when(configuration.getString("database.host"))
        .thenReturn("localhost");
    Mockito.when(configuration.getString("database.user"))
        .thenReturn("test");
    Mockito.when(configuration.getString("database.password"))
        .thenReturn("test");
    Mockito.when(configuration.getInt("database.port"))
        .thenReturn(3306);

    RelluEssentials spyPlugin = Mockito.spy(plugin);
    Mockito.doReturn(configuration)
        .when(spyPlugin)
        .getConfig();

    try (
        MockedStatic<ChatHelper> chatHelper = Mockito.mockStatic(ChatHelper.class);
        MockedStatic<RelluEssentialsRegistry> registry =
            Mockito.mockStatic(RelluEssentialsRegistry.class);
        MockedConstruction<ServiceManager> serviceManager =
            Mockito.mockConstruction(ServiceManager.class);
        MockedConstruction<ConfigManager> configManager =
            Mockito.mockConstruction(ConfigManager.class);
        MockedConstruction<EnchantmentManager> enchantmentManager =
            Mockito.mockConstruction(EnchantmentManager.class);
        MockedConstruction<ItemManager> itemManager =
            Mockito.mockConstruction(ItemManager.class);
        MockedConstruction<DatabaseManager> databaseManager =
            Mockito.mockConstruction(DatabaseManager.class);
        MockedConstruction<CommandManager> commandManager =
            Mockito.mockConstruction(CommandManager.class);
        MockedConstruction<SignManager> signManager =
            Mockito.mockConstruction(SignManager.class);
        MockedConstruction<SkillManager> skillManager =
            Mockito.mockConstruction(SkillManager.class);
        MockedConstruction<RecipeManager> recipeManager =
            Mockito.mockConstruction(RecipeManager.class);
        MockedConstruction<BankManager> bankManager =
            Mockito.mockConstruction(BankManager.class);
        MockedConstruction<ListenerManager> listenerManager =
            Mockito.mockConstruction(ListenerManager.class);
        MockedConstruction<AutoSaveManager> autoSaveManager =
            Mockito.mockConstruction(AutoSaveManager.class);
        MockedConstruction<ScoreBoardManager> scoreBoardManager =
            Mockito.mockConstruction(ScoreBoardManager.class);
        MockedConstruction<WorldManager> worldManager =
            Mockito.mockConstruction(WorldManager.class)
    ) {
      spyPlugin.onEnable();

      assertEquals(spyPlugin, RelluEssentials.getInstance());
      assertTrue(spyPlugin.isUnitTest());

      registry.verify(() ->
          RelluEssentialsRegistry.initialize(
              spyPlugin.getServiceContext().getTranslationService()
          ));
    }
  }
}