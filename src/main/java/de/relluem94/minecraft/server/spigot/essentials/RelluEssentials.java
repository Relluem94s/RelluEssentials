package de.relluem94.minecraft.server.spigot.essentials;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_BORDER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
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
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcDialogueTracker;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcRepository;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcSpawner;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcValidator;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.registry.BagTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repository.BuyBackRepository;
import de.relluem94.minecraft.server.spigot.essentials.repository.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.repository.UndoHistoryRepository;
import de.relluem94.minecraft.server.spigot.essentials.repository.WarpRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Main plugin class for RelluEssentials. Extends {@link JavaPlugin} to integrate with the Spigot
 * plugin lifecycle.
 */
public class RelluEssentials extends JavaPlugin {

  public static final List<SettingEntry> settingEntriesList = new ArrayList<>();
  public static final Map<Player, Player> reply = new HashMap<>();
  private static RelluEssentials instance;
  private static BankerNpc banker;
  public final Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
  public final Set<String> collectBagWorlds = new HashSet<>();
  public final Set<String> useCloudsailorWorlds = new HashSet<>();
  public final Set<String> deathLoseCoins = new HashSet<>();
  public final Set<String> deathCreateHome = new HashSet<>();
  public final Set<String> oreRespawn = new HashSet<>();
  public final Set<String> scoreboardShow = new HashSet<>();
  public final Map<Player, DoubleStore<Location, Location>> position = new HashMap<>();
  public final Map<UUID, BankAccountEntry> bankInterestMap = new HashMap<>();
  public final Map<Material, DoubleStore<Integer, Integer>> dropMap = new EnumMap<>(Material.class);
  public final Map<Material, Material> crops = new EnumMap<>(Material.class);
  public final List<ItemStack> bagBlocks2collect = new ArrayList<>();

  @Getter
  public final List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
  public Map<Player,
      DoubleStore<Selection, List<ModifyClipboardEntry>>> clipboard = new HashMap<>();

  @Setter
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
  private PlayerRegistry playerRegistry;
  @Setter
  @Getter
  private PlayerService playerService;
  @Setter
  @Getter
  private ProtectionRegistry protectionRegistry;
  @Setter
  @Getter
  private TraderNpcRegistry traderNpcRegistry;
  @Setter
  @Getter
  private BagTypeRegistry bagTypeRegistry;
  @Setter
  @Getter
  private BankTierRegistry bankTierRegistry;
  @Setter
  @Getter
  private WarpRepository warpRepository;
  @Getter
  private BuyBackService buyBackService;
  @Getter
  private NpcService npcService;
  @Getter
  private NpcDialogueTracker npcDialogueTracker;
  @Setter
  @Getter
  private GroupRegistry groupRegistry;
  @Setter
  @Getter
  private GroupService groupService;
  @Getter
  private SchedulerService schedulerService;
  private UndoHistoryRepository undoHistoryRepository;
  @Getter
  private UndoHistoryService undoHistoryService;
  @Getter
  private TranslationService translationService;
  @Getter
  private SelectionService selectionService;

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

  public static BankerNpc getBanker() {
    return RelluEssentials.banker;
  }

  public static void setBanker(BankerNpc banker) {
    RelluEssentials.banker = banker;
  }

  @Override
  public void onEnable() {
    start = Calendar.getInstance().getTimeInMillis();
    String lang = getConfig().getString("language", "en_US");

    translationService = new TranslationService(this);
    translationService.loadLanguages();
    translationService.setDefaultLanguage(lang);

    startLoading();
    schedulerService = new SchedulerService(this);
    undoHistoryRepository = new UndoHistoryRepository();
    undoHistoryService = new UndoHistoryService(undoHistoryRepository);
    selectionService = new SelectionService(translationService);

    serviceContext = new ServiceContext(this);
    configManager = new ConfigManager();
    configManager.enable(this);

    enchantmentManager = new EnchantmentManager();
    enchantmentManager.enable(this);
    itemManager = new ItemManager();
    itemManager.enable(this);

    databaseManager = new DatabaseManager(
        getConfig().getString("database.host"),
        getConfig().getString("database.user"),
        getConfig().getString("database.password"),
        getConfig().getInt("database.port")
    );
    databaseManager.enable(this);
    databaseHelper = databaseManager.getDatabaseHelper();

    GroupRepository groupRepository = new GroupRepository(databaseHelper.getGroups());
    groupRegistry = new GroupRegistry(groupRepository);
    groupService = new GroupService(groupRegistry, groupRepository);
    databaseManager.setGroupService(getGroupService());
    this.playerRegistry = new PlayerRegistry(databaseHelper.getBags());
    this.playerService = new PlayerService(playerRegistry);
    serviceContext.setPlayerService(getPlayerService());
    groupService.setPlayerRegistry(playerRegistry);
    BuyBackRepository buyBackRepository = new BuyBackRepository();
    buyBackService = new BuyBackService(buyBackRepository);



    serviceContext.setGroupRegistry(getGroupRegistry());
    serviceContext.setGroupService(getGroupService());
    serviceContext.setNpcService(getNpcService());

    commandManager = new CommandManager();
    serviceContext.setCommandManager(commandManager);
    commandManager.enable(this);
    signManager = new SignManager();
    signManager.enable(this);
    listenerManager = new ListenerManager();
    listenerManager.enable(this);
    skillManager = new SkillManager();
    skillManager.enable(this);
    recipeManager = new RecipeManager();
    recipeManager.enable(this);
    bankManager = new BankManager();
    bankManager.enable(this);
    npcManager = new NpcManager();
    npcManager.enable(this);
    NpcRepository npcRepository = new NpcRepository(databaseHelper);
    NpcSpawner npcSpawner = new NpcSpawner();
    NpcValidator npcValidator = new NpcValidator();
    npcService = new NpcService(npcRepository, npcSpawner, npcValidator);
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
    autoSaveManager =new AutoSaveManager();
    autoSaveManager.enable(this);
    databaseManager.afterWorldLoaded(this);
    new BukkitRunnable() {
      @Override
      public void run() {
        getNpcService().loadAndSpawnNpcsInLoadedChunks();
      }
    }.runTaskLater(this, 20L);
  }

  @Override
  public void onDisable() {
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_STOP_MESSAGE));
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
        translationService.get(MessageKey.PLUGIN_MANAGER_START_MESSAGE));
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
  }

  private void stopLoading() {
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_START_TIME_MESSAGE,
            Calendar.getInstance().getTimeInMillis() - start));
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    consoleSendMessage(PLUGIN_COLOR_COMMAND + PLUGIN_FORMS_BORDER, "");
  }
}