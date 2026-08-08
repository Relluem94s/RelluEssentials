package de.relluem94.minecraft.server.spigot.essentials;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_BORDER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.AutoSaveManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.BankManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ConfigManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.DatabaseManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.EnchantmentManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.GroupManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ItemManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ListenerManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.NpcManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.PositionHighlightManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.RecipeManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SignManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SkillManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.WorldManager;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcDialogueTracker;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcRepository;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcSpawner;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcValidator;
import de.relluem94.minecraft.server.spigot.essentials.registries.BagRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BagTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.RelluEssentialsRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ReplyRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BackLocationRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagTypeRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BuyBackRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.UndoHistoryRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.BackService;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.ChatService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionActionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

/**
 * Main plugin class for RelluEssentials. Extends {@link JavaPlugin} to integrate with the Spigot
 * plugin lifecycle.
 */
public class RelluEssentials extends JavaPlugin {

  public static final List<SettingEntry> settingEntriesList = new ArrayList<>();
  private static RelluEssentials instance;
  public final Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
  public final Set<String> collectBagWorlds = new HashSet<>();
  public final Set<String> useCloudsailorWorlds = new HashSet<>();
  public final Set<String> deathLoseCoins = new HashSet<>();
  public final Set<String> deathCreateHome = new HashSet<>();
  public final Set<String> oreRespawn = new HashSet<>();
  public final Set<String> scoreboardShow = new HashSet<>();
  public final Map<Player, DoubleStore<Location, Location>> position = new HashMap<>();

  @Getter
  public final List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
  public Map<Player,
      DoubleStore<Selection, List<ModifyClipboardEntry>>> clipboard = new HashMap<>();

  /* Services Repos Registries */
  @Getter
  private ServiceContext serviceContext;
  private long start;
  @Getter
  private DatabaseHelper databaseHelper;
  @Setter
  @Getter
  private PluginInformationEntry pluginInformation;
  @Getter
  private boolean isUnitTest = false;
  @Setter
  @Getter
  private ProtectionRegistry protectionRegistry;
  @Setter
  @Getter
  private TraderNpcRegistry traderNpcRegistry;
  @Getter
  private NpcService npcService;
  @Getter
  private NpcDialogueTracker npcDialogueTracker;


  /* Manager */
  @Getter
  private ListenerManager listenerManager;
  @Getter
  private SkillManager skillManager;
  @Getter
  private RecipeManager recipeManager;
  @Getter
  private AutoSaveManager autoSaveManager;
  @Getter
  private BankManager bankManager;
  @Getter
  private NpcManager npcManager;
  @Getter
  private ConfigManager configManager;
  @Getter
  private CommandManager commandManager;
  @Getter
  private ItemManager itemManager;
  @Getter
  private EnchantmentManager enchantmentManager;
  @Getter
  private WorldManager worldManager;
  @Getter
  private GroupManager groupManager;
  @Getter
  private PositionHighlightManager positionHighlightManager;
  @Getter
  private ScoreBoardManager scoreBoardManager;
  @Getter
  private SignManager signManager;
  @Getter
  private DatabaseManager databaseManager;
  @Getter
  private SudoManager sudoManager;

  /**
   * Default constructor for the RelluEssentials plugin. Used by the Spigot server to instantiate
   * the plugin.
   */
  public RelluEssentials() {
    super();
  }

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
    isUnitTest = true;
  }

  public static synchronized RelluEssentials getInstance() {
    return instance;
  }

  private static synchronized void setInstance(RelluEssentials re) {
    instance = re;
  }

  @Override
  public void onEnable() {
    start = Calendar.getInstance().getTimeInMillis();
    serviceContext = new ServiceContext();
    String lang = getConfig().getString("language", "en_US");

    TranslationService translationService = new TranslationService(this);
    translationService.loadLanguages();
    translationService.setDefaultLanguage(lang);
    serviceContext.setTranslationService(translationService);
    RelluEssentialsRegistry.initialize(translationService);

    startLoading();
    SchedulerService schedulerService = new SchedulerService(this);
    serviceContext.setSchedulerService(schedulerService);
    UndoHistoryRepository undoHistoryRepository = new UndoHistoryRepository();
    UndoHistoryService undoHistoryService = new UndoHistoryService(undoHistoryRepository);
    serviceContext.setUndoHistoryService(undoHistoryService);
    SelectionService selectionService = new SelectionService(translationService);
    serviceContext.setSelectionService(selectionService);

    configManager = new ConfigManager();
    configManager.enable(this);

    enchantmentManager = new EnchantmentManager();
    enchantmentManager.enable(this);
    itemManager = new ItemManager();
    itemManager.enable(this);

    databaseManager = new DatabaseManager(
        serviceContext,
        getConfig().getString("database.host"),
        getConfig().getString("database.user"),
        getConfig().getString("database.password"),
        (getConfig().getInt("database.port"))
    );
    databaseManager.enable(this);
    databaseHelper = databaseManager.getDatabaseHelper();
    serviceContext.setDatabaseHelper(databaseHelper);

    GroupRepository groupRepository = new GroupRepository(databaseHelper.getGroups());
    GroupRegistry groupRegistry = new GroupRegistry(groupRepository);
    GroupService groupService = new GroupService(groupRegistry, groupRepository);
    serviceContext.setGroupService(groupService);
    PlayerRegistry playerRegistry = new PlayerRegistry();
    PlayerService playerService = new PlayerService(serviceContext, playerRegistry);
    serviceContext.setPlayerService(playerService);
    groupService.setPlayerRegistry(playerRegistry);

    BagRepository bagRepository = new BagRepository(databaseHelper.getBags());
    BagTypeRepository bagTypeRepository = new BagTypeRepository(databaseHelper.getBagTypes());
    BagTypeRegistry bagTypeRegistry = new BagTypeRegistry(bagTypeRepository);
    BagRegistry bagRegistry = new BagRegistry(bagRepository);
    BagService bagService = new BagService(serviceContext, bagRegistry, bagTypeRegistry);
    serviceContext.setBagService(bagService);

    BuyBackRepository buyBackRepository = new BuyBackRepository();
    BuyBackService buyBackService = new BuyBackService(buyBackRepository);
    serviceContext.setBuyBackService(buyBackService);
    MessageService messageService = new MessageService(translationService);
    serviceContext.setMessageService(messageService);
    ReplyRegistry replyRegistry = new ReplyRegistry();
    ChatService chatService = new ChatService(serviceContext, replyRegistry);
    serviceContext.setChatService(chatService);
    BankService bankService = new BankService(databaseHelper, playerRegistry,
        new BankTierRegistry(databaseHelper.getBankTiers()), translationService, this);
    serviceContext.setBankService(bankService);

    BackLocationRepository backLocationRepository = new BackLocationRepository();
    BackService backService = new BackService(backLocationRepository);
    serviceContext.setBackService(backService);
    TeleportService teleportService = new TeleportService(translationService, backService);
    serviceContext.setTeleportService(teleportService);

    ProtectionActionService protectionActionService = new ProtectionActionService(
        translationService,
        databaseHelper,
        protectionRegistry,
        playerRegistry
    );
    serviceContext.setProtectionActionService(protectionActionService);

    commandManager = new CommandManager();
    serviceContext.setCommandManager(commandManager);
    commandManager.enable(this);
    signManager = new SignManager();
    signManager.enable(this);
    skillManager = new SkillManager();
    skillManager.enable(this);
    recipeManager = new RecipeManager();
    recipeManager.enable(this);
    bankManager = new BankManager();
    bankManager.enable(this);
    npcManager = new NpcManager();
    npcManager.enable(this);

    listenerManager = new ListenerManager();
    listenerManager.enable(this);
    autoSaveManager = new AutoSaveManager();
    autoSaveManager.enable(this);

    NpcRepository npcRepository = new NpcRepository(databaseHelper);
    NpcSpawner npcSpawner = new NpcSpawner();
    NpcValidator npcValidator = new NpcValidator();
    NpcService npcService = new NpcService(npcRepository, npcSpawner, npcValidator);
    serviceContext.setNpcService(npcService);
    npcDialogueTracker = new NpcDialogueTracker();
    stopLoading();
    worldManager = new WorldManager();
    worldManager.enable(this);
    groupManager = new GroupManager();
    groupManager.enable(this);
    positionHighlightManager = new PositionHighlightManager();
    positionHighlightManager.enable(this);
    scoreBoardManager = new ScoreBoardManager();
    scoreBoardManager.enable(this);
    databaseManager.afterWorldLoaded(this);
    getServiceContext().getSchedulerService()
        .runTaskLater(() -> getNpcService().loadAndSpawnNpcsInLoadedChunks(), 20L);
  }

  @Override
  public void onDisable() {
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        getServiceContext().getTranslationService().get(MessageKey.PLUGIN_MANAGER_STOP_MESSAGE));
    if (npcService != null) {
      npcService.despawnAllNPCs();
    }
    sudoManager = new SudoManager();
    sudoManager.disable(this);
    autoSaveManager.disable(this);
    worldManager.disable(this);
    configManager.disable(this);
  }

  private void startLoading() {
    setInstance(this);
    consoleSendMessage(PLUGIN_COLOR_COMMAND, PLUGIN_FORMS_BORDER);
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "", 2);
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        getServiceContext().getTranslationService().get(MessageKey.PLUGIN_MANAGER_START_MESSAGE));
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
  }

  private void stopLoading() {
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        getServiceContext().getTranslationService()
            .get(MessageKey.PLUGIN_MANAGER_START_TIME_MESSAGE,
                Calendar.getInstance().getTimeInMillis() - start));
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    consoleSendMessage(PLUGIN_COLOR_COMMAND + PLUGIN_FORMS_BORDER, "");
  }
}