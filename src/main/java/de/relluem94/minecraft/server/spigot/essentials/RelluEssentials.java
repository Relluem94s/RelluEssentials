package de.relluem94.minecraft.server.spigot.essentials;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_BORDER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.commands.AFK;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.commands.Back;
import de.relluem94.minecraft.server.spigot.essentials.commands.Bags;
import de.relluem94.minecraft.server.spigot.essentials.commands.Broadcast;
import de.relluem94.minecraft.server.spigot.essentials.commands.Cookies;
import de.relluem94.minecraft.server.spigot.essentials.commands.CraftingBench;
import de.relluem94.minecraft.server.spigot.essentials.commands.CustomHead;
import de.relluem94.minecraft.server.spigot.essentials.commands.Day;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.Enderchest;
import de.relluem94.minecraft.server.spigot.essentials.commands.Exit;
import de.relluem94.minecraft.server.spigot.essentials.commands.Fly;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameModeAdventure;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameModeCreative;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameModeSpectator;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameModeSurvival;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameRules;
import de.relluem94.minecraft.server.spigot.essentials.commands.God;
import de.relluem94.minecraft.server.spigot.essentials.commands.Head;
import de.relluem94.minecraft.server.spigot.essentials.commands.Heal;
import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.commands.Inventory;
import de.relluem94.minecraft.server.spigot.essentials.commands.Marry;
import de.relluem94.minecraft.server.spigot.essentials.commands.Message;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.More;
import de.relluem94.minecraft.server.spigot.essentials.commands.Nick;
import de.relluem94.minecraft.server.spigot.essentials.commands.Night;
import de.relluem94.minecraft.server.spigot.essentials.commands.PermissionsGroup;
import de.relluem94.minecraft.server.spigot.essentials.commands.PlayerInfo;
import de.relluem94.minecraft.server.spigot.essentials.commands.PlayerList;
import de.relluem94.minecraft.server.spigot.essentials.commands.PlayerWeather;
import de.relluem94.minecraft.server.spigot.essentials.commands.Poke;
import de.relluem94.minecraft.server.spigot.essentials.commands.Position;
import de.relluem94.minecraft.server.spigot.essentials.commands.Print;
import de.relluem94.minecraft.server.spigot.essentials.commands.Protect;
import de.relluem94.minecraft.server.spigot.essentials.commands.Purse;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rain;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rename;
import de.relluem94.minecraft.server.spigot.essentials.commands.Repair;
import de.relluem94.minecraft.server.spigot.essentials.commands.Reply;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sign;
import de.relluem94.minecraft.server.spigot.essentials.commands.Spawn;
import de.relluem94.minecraft.server.spigot.essentials.commands.Speed;
import de.relluem94.minecraft.server.spigot.essentials.commands.Storm;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.commands.Suicide;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sun;
import de.relluem94.minecraft.server.spigot.essentials.commands.Team;
import de.relluem94.minecraft.server.spigot.essentials.commands.Teleport;
import de.relluem94.minecraft.server.spigot.essentials.commands.Title;
import de.relluem94.minecraft.server.spigot.essentials.commands.Vanish;
import de.relluem94.minecraft.server.spigot.essentials.commands.Warp;
import de.relluem94.minecraft.server.spigot.essentials.commands.Where;
import de.relluem94.minecraft.server.spigot.essentials.commands.Worlds;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.AutoSaveManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.BankManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ConfigManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.DatabaseManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.EnchantmentManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.EventManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.GroupManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ItemManager;
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
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
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
import de.relluem94.minecraft.server.spigot.essentials.repository.WarpRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.wrapper.CommandWrapper;
import de.relluem94.minecraft.server.spigot.essentials.wrapper.EventWrapper;
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
  public static LanguageHelper languageHelper;
  private static RelluEssentials instance;
  private static BankerNpc banker;
  private static List<CommandWrapper> commandWrapperList;
  private static List<EventWrapper> eventWrapperList;
  public final Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
  public final Set<String> collectBagWorlds = new HashSet<>();
  public final Set<String> useCloudsailorWorlds = new HashSet<>();
  public final Set<String> deathLoseCoins = new HashSet<>();
  public final Set<String> deathCreateHome = new HashSet<>();
  public final Set<String> oreRespawn = new HashSet<>();
  public final Set<String> scoreboardShow = new HashSet<>();
  public final Map<Player, List<List<ModifyHistoryEntry>>> undo = new HashMap<>();
  public final Map<Player, DoubleStore<Location, Location>> position = new HashMap<>();
  public final Map<UUID, BankAccountEntry> bankInterestMap = new HashMap<>();
  public final Map<Material, DoubleStore<Integer, Integer>> dropMap = new EnumMap<>(Material.class);
  public final Map<Material, Material> crops = new EnumMap<>(Material.class);
  public final List<ItemStack> bagBlocks2collect = new ArrayList<>();

  @Getter
  public final List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
  public Map<Player,
      DoubleStore<Selection, List<ModifyClipboardEntry>>> clipboard = new HashMap<>();
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

  /**
   * Returns the list of all registered {@link CommandWrapper} instances.
   *
   * <p>The list is lazily initialized on first access and contains a wrapper
   * for every command provided by this plugin.
   *
   * @return an unmodifiable {@link List} of {@link CommandWrapper} instances
   */
  public static List<CommandWrapper> getCommandWrapperList() {
    if (commandWrapperList == null) {
      commandWrapperList = List.of(
          new CommandWrapper(new Admin()),
          new CommandWrapper(new AFK()),
          new CommandWrapper(new Back()),
          new CommandWrapper(new Bags()),
          new CommandWrapper(new Broadcast()),
          new CommandWrapper(new Cookies()),
          new CommandWrapper(new CraftingBench()),
          new CommandWrapper(new CustomHead()),
          new CommandWrapper(new Day()),
          new CommandWrapper(new Enderchest()),
          new CommandWrapper(new Exit()),
          new CommandWrapper(new Fly()),
          new CommandWrapper(new GameModeAdventure()),
          new CommandWrapper(new GameModeCreative()),
          new CommandWrapper(new GameModeSpectator()),
          new CommandWrapper(new GameModeSurvival()),
          new CommandWrapper(new GameRules()),
          new CommandWrapper(new God()),
          new CommandWrapper(new Head()),
          new CommandWrapper(new Heal()),
          new CommandWrapper(new Home()),
          new CommandWrapper(new Inventory()),
          new CommandWrapper(new Marry()),
          new CommandWrapper(new Message()),
          new CommandWrapper(new Modify()),
          new CommandWrapper(new More()),
          new CommandWrapper(new Nick()),
          new CommandWrapper(new Night()),
          new CommandWrapper(new PermissionsGroup()),
          new CommandWrapper(new PlayerInfo()),
          new CommandWrapper(new PlayerList()),
          new CommandWrapper(new PlayerWeather()),
          new CommandWrapper(new Poke()),
          new CommandWrapper(new Position()),
          new CommandWrapper(new Print()),
          new CommandWrapper(new Protect()),
          new CommandWrapper(new Purse()),
          new CommandWrapper(new Rain()),
          new CommandWrapper(new Rename()),
          new CommandWrapper(new Repair()),
          new CommandWrapper(new Reply()),
          new CommandWrapper(new Sign()),
          new CommandWrapper(new Spawn()),
          new CommandWrapper(new Speed()),
          new CommandWrapper(new Storm()),
          new CommandWrapper(new Sudo()),
          new CommandWrapper(new Suicide()),
          new CommandWrapper(new Sun()),
          new CommandWrapper(new Team()),
          new CommandWrapper(new Teleport()),
          new CommandWrapper(new Title()),
          new CommandWrapper(new Vanish()),
          new CommandWrapper(new Warp()),
          new CommandWrapper(new Where()),
          new CommandWrapper(new Worlds()),

          // THIS IS A DEV COMMAND
          new CommandWrapper(new DevCommand())
      );
    }
    return commandWrapperList;
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
    RelluEssentials.languageHelper = new LanguageHelper(this);
    RelluEssentials.languageHelper.loadLanguages();

    String lang = getConfig().getString("language", "en_US");
    RelluEssentials.languageHelper.setDefaultLanguage(lang);

    startLoading();
    new ConfigManager().enable(this);
    new CommandManager().enable(this);
    new EnchantmentManager().enable(this);
    new ItemManager().enable(this);
    new SignManager().enable(this);
    DatabaseManager dm = new DatabaseManager(
        getConfig().getString("database.host"),
        getConfig().getString("database.user"),
        getConfig().getString("database.password"),
        getConfig().getInt("database.port")
    );

    dm.enable();
    databaseHelper = dm.getDatabaseHelper();
    this.playerRegistry = new PlayerRegistry(databaseHelper.getBags());
    this.playerService = new PlayerService(playerRegistry);
    BuyBackRepository buyBackRepository = new BuyBackRepository();
    buyBackService = new BuyBackService(buyBackRepository);

    new EventManager().enable(this);
    new SkillManager().enable(this);
    new RecipeManager().enable(this);
    new AutoSaveManager().enable(this);
    new BankManager().enable(this);
    new NpcManager().enable(this);
    NpcRepository npcRepository = new NpcRepository(databaseHelper);
    NpcSpawner npcSpawner = new NpcSpawner();
    NpcValidator npcValidator = new NpcValidator();
    this.npcService = new NpcService(npcRepository, npcSpawner, npcValidator);
    this.npcDialogueTracker = new NpcDialogueTracker();
    stopLoading();
    new WorldManager().enable(this);
    new GroupManager().enable(this);
    new PositionHighlightManager().enable(this);
    new ScoreBoardManager().enable(this);

    dm.afterWorldLoaded();
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
        languageHelper.get(MessageKey.PLUGIN_MANAGER_STOP_MESSAGE));
    if (npcService != null) {
      npcService.despawnAllNPCs();
    }
    new SudoManager().disable(this);
    new AutoSaveManager().disable(this);
    new WorldManager().disable(this);
    new ConfigManager().disable(this);
  }

  private void startLoading() {
    setInstance(this);
    start = Calendar.getInstance().getTimeInMillis();
    consoleSendMessage(PLUGIN_COLOR_COMMAND, PLUGIN_FORMS_BORDER);
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "", 2);
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_START_MESSAGE));
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
  }

  private void stopLoading() {
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_START_TIME_MESSAGE,
            Calendar.getInstance().getTimeInMillis() - start));
    consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    consoleSendMessage(PLUGIN_COLOR_COMMAND + PLUGIN_FORMS_BORDER, "");
  }
}