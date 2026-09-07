package de.relluem94.minecraft.server.spigot.essentials;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_BORDER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;

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
import java.util.Calendar;
import lombok.Getter;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

/**
 * Main plugin class for RelluEssentials. Extends {@link JavaPlugin} to integrate with the Spigot
 * plugin lifecycle.
 */
public class RelluEssentials extends JavaPlugin {

  private static RelluEssentials instance;

  @Getter
  private ServiceContext serviceContext;

  @Getter
  private PersistenceContext persistenceContext;

  /* Manager */
  private AutoSaveManager autoSaveManager;
  private ConfigManager configManager;
  private WorldManager worldManager;

  /**
   * Constructor for unit testing purposes. Allows injecting a custom loader, description, data
   * folder, and file without requiring a running Spigot server.
   *
   * @param loader      the plugin loader used to load this plugin
   * @param description the plugin description file containing metadata
   * @param dataFolder  the folder where the plugin stores its data
   * @param file        the plugin jar file
   */
  protected RelluEssentials(JavaPluginLoader loader, PluginDescriptionFile description,
      File dataFolder, File file) {
    super(loader, description, dataFolder, file);
  }

  public static synchronized RelluEssentials getInstance() {
    return instance;
  }

  private static synchronized void setInstance(RelluEssentials re) {
    instance = re;
  }

  @Override
  public void onEnable() {
    final long start = System.currentTimeMillis();
    persistenceContext = new PersistenceContext();
    serviceContext = new ServiceContext();
    ServiceManager serviceManager = new ServiceManager();
    serviceManager.preEnable(this);
    ConsoleCommandSender sender = getServer().getConsoleSender();
    setInstance(this);
    sender.sendMessage(PLUGIN_COLOR_COMMAND, PLUGIN_FORMS_BORDER);
    sender.sendMessage(PLUGIN_NAME_CONSOLE, "");
    sender.sendMessage(PLUGIN_NAME_CONSOLE, "");
    sender.sendMessage(PLUGIN_NAME_CONSOLE,
        serviceContext.getTranslationService().get(MessageKey.PLUGIN_MANAGER_START_MESSAGE));
    sender.sendMessage(PLUGIN_NAME_CONSOLE, "");

    RelluEssentialsRegistry.initialize(getServiceContext().getTranslationService());

    configManager = new ConfigManager();
    configManager.enable(this);
    EnchantmentManager enchantmentManager = new EnchantmentManager();
    enchantmentManager.enable(this);
    ItemManager itemManager = new ItemManager();
    itemManager.enable(this);
    DatabaseManager databaseManager = new DatabaseManager(getConfig().getString("database.host"),
        getConfig().getString("database.user"), getConfig().getString("database.password"),
        getConfig().getInt("database.port"));
    databaseManager.enable(this);
    serviceManager.enable(this);
    SignManager signManager = new SignManager();
    signManager.enable(this);
    SkillManager skillManager = new SkillManager();
    skillManager.enable(this);
    RecipeManager recipeManager = new RecipeManager();
    recipeManager.enable(this);
    BankManager bankManager = new BankManager();
    bankManager.enable(this);
    CommandManager commandManager = new CommandManager();
    commandManager.enable(this);
    ListenerManager listenerManager = new ListenerManager();
    listenerManager.enable(this);
    autoSaveManager = new AutoSaveManager();
    autoSaveManager.enable(this);
    ScoreBoardManager scoreBoardManager = new ScoreBoardManager();
    scoreBoardManager.enable(this);

    sender.sendMessage(PLUGIN_NAME_CONSOLE, "");
    sender.sendMessage(PLUGIN_NAME_CONSOLE,
        serviceContext.getTranslationService().get(MessageKey.PLUGIN_MANAGER_START_TIME_MESSAGE,
            Calendar.getInstance().getTimeInMillis() - start));
    sender.sendMessage(PLUGIN_NAME_CONSOLE, "");
    sender.sendMessage(PLUGIN_COLOR_COMMAND + PLUGIN_FORMS_BORDER, "");

    worldManager = new WorldManager();
    worldManager.enable(this);
    getServiceContext().getSchedulerService()
        .runTaskLater(() -> getServiceContext().getNpcService().loadAndSpawnNpcsInLoadedChunks(),
            20L);
  }

  @Override
  public void onDisable() {
    getServer().getConsoleSender().sendMessage(PLUGIN_NAME_CONSOLE,
        getServiceContext().getTranslationService().get(MessageKey.PLUGIN_MANAGER_STOP_MESSAGE));
    if (getServiceContext().getNpcService() != null) {
      getServiceContext().getNpcService().despawnAllNpcs();
    }
    SudoManager sudoManager = new SudoManager();
    sudoManager.disable(this);
    autoSaveManager.disable(this);
    worldManager.disable(this);
    configManager.disable(this);
  }
}