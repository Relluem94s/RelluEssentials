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
import de.relluem94.minecraft.server.spigot.essentials.events.BetterBlockDrop;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLights;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterMobs;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerJoin;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerQuit;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSafety;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSoil;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterWorlds;
import de.relluem94.minecraft.server.spigot.essentials.events.BlockPlace;
import de.relluem94.minecraft.server.spigot.essentials.events.CloudSailor;
import de.relluem94.minecraft.server.spigot.essentials.events.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.events.GrapplingHockEvent;
import de.relluem94.minecraft.server.spigot.essentials.events.IntegrationListener;
import de.relluem94.minecraft.server.spigot.essentials.events.MOTD;
import de.relluem94.minecraft.server.spigot.essentials.events.NoDeathMessage;
import de.relluem94.minecraft.server.spigot.essentials.events.OpenWorldSelectorEvent;
import de.relluem94.minecraft.server.spigot.essentials.events.PlayerMove;
import de.relluem94.minecraft.server.spigot.essentials.events.PositionAxeListener;
import de.relluem94.minecraft.server.spigot.essentials.events.PreventCoinManipulation;
import de.relluem94.minecraft.server.spigot.essentials.events.SignActions;
import de.relluem94.minecraft.server.spigot.essentials.events.SignClick;
import de.relluem94.minecraft.server.spigot.essentials.events.SignEdit;
import de.relluem94.minecraft.server.spigot.essentials.events.SkullInfo;
import de.relluem94.minecraft.server.spigot.essentials.events.ToolCrafting;
import de.relluem94.minecraft.server.spigot.essentials.events.bag.BlockBreakBags;
import de.relluem94.minecraft.server.spigot.essentials.events.bag.BlockDropItemBags;
import de.relluem94.minecraft.server.spigot.essentials.events.bag.EntityPickupItemBags;
import de.relluem94.minecraft.server.spigot.essentials.events.bag.InventoryClickBags;
import de.relluem94.minecraft.server.spigot.essentials.events.npc.DamgeNpc;
import de.relluem94.minecraft.server.spigot.essentials.events.npc.DamgeTraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.events.npc.InteractNpc;
import de.relluem94.minecraft.server.spigot.essentials.events.npc.InteractTraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.events.npc.InventoryClickNpc;
import de.relluem94.minecraft.server.spigot.essentials.events.npc.NpcChunkLoadListener;
import de.relluem94.minecraft.server.spigot.essentials.events.npc.PlaceNpc;
import de.relluem94.minecraft.server.spigot.essentials.events.protect.BetterLock;
import de.relluem94.minecraft.server.spigot.essentials.events.protect.BlockModifyProtect;
import de.relluem94.minecraft.server.spigot.essentials.events.protect.BlockPistonProtect;
import de.relluem94.minecraft.server.spigot.essentials.events.protect.BlockRedstoneProtect;
import de.relluem94.minecraft.server.spigot.essentials.events.protect.EntityBreakDoorProtect;
import de.relluem94.minecraft.server.spigot.essentials.events.protect.EntityExplodeProtect;
import de.relluem94.minecraft.server.spigot.essentials.events.protect.InventoryMoveItemProtect;
import de.relluem94.minecraft.server.spigot.essentials.events.protect.PlayerInteractProtect;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.AutoSaveManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.BankManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.CleanUpManager;
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
import de.relluem94.minecraft.server.spigot.essentials.managers.SkillManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.WorldManager;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
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
import de.relluem94.minecraft.server.spigot.essentials.registry.BagRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.WarpRegistry;
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
  public final List<GroupEntry> groupEntryList = new ArrayList<>();
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
  private BagRegistry bagRegistry;
  @Setter
  @Getter
  private BankTierRegistry bankTierRegistry;
  @Setter
  @Getter
  private WarpRegistry warpRegistry;
  @Getter
  private NpcService npcService;
  @Getter
  private NpcDialogueTracker npcDialogueTracker;

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

  /**
   * Returns the list of all registered {@link EventWrapper} instances.
   *
   * <p>The list is lazily initialized on first access and contains all event
   * wrappers that encapsulate the plugin's feature listeners.
   * </p>
   *
   * @return an unmodifiable {@link List} of {@link EventWrapper} instances
   */
  public static List<EventWrapper> getEventWrapperList() {
    if (eventWrapperList == null) {
      eventWrapperList = List.of(
          new EventWrapper(new BetterChatFormat()),
          new EventWrapper(new BetterWorlds()),
          new EventWrapper(new BetterPlayerJoin()),
          new EventWrapper(new BetterPlayerQuit()),
          new EventWrapper(new BetterBlockDrop()),
          new EventWrapper(new BetterLights()),
          new EventWrapper(new BlockBreakBags()),
          new EventWrapper(new BlockDropItemBags()),
          new EventWrapper(new InventoryClickBags()),
          new EventWrapper(new EntityPickupItemBags()),
          new EventWrapper(new BlockPlace()),
          new EventWrapper(new BetterMobs()),
          new EventWrapper(new BetterSoil()),
          new EventWrapper(new NpcChunkLoadListener()),
          new EventWrapper(new DamgeNpc()),
          new EventWrapper(new DamgeTraderNpc()),
          new EventWrapper(new InteractNpc()),
          new EventWrapper(new InteractTraderNpc()),
          new EventWrapper(new InventoryClickNpc()),
          new EventWrapper(new PlaceNpc()),
          new EventWrapper(new BetterSafety()),
          new EventWrapper(new BlockPistonProtect()),
          new EventWrapper(new EntityBreakDoorProtect()),
          new EventWrapper(new InventoryMoveItemProtect()),
          new EventWrapper(new EntityExplodeProtect()),
          new EventWrapper(new BlockRedstoneProtect()),
          new EventWrapper(new BlockModifyProtect()),
          new EventWrapper(new PlayerInteractProtect()),
          new EventWrapper(new OpenWorldSelectorEvent()),
          new EventWrapper(new BetterLock()),
          new EventWrapper(new SkullInfo()),
          new EventWrapper(new NoDeathMessage()),
          new EventWrapper(new PlayerMove()),
          new EventWrapper(new MOTD()),
          new EventWrapper(new CloudSailor()),
          new EventWrapper(new SignActions()),
          new EventWrapper(new SignClick()),
          new EventWrapper(new SignEdit()),
          new EventWrapper(new ToolCrafting()),
          new EventWrapper(new CustomEnchantment()),
          new EventWrapper(new GrapplingHockEvent()),
          new EventWrapper(new PositionAxeListener()),
          new EventWrapper(new PreventCoinManipulation()),
          new EventWrapper(new IntegrationListener())
      );
    }
    return eventWrapperList;
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
    new ConfigManager().enable();
    new CommandManager().enable();
    DatabaseManager dm = new DatabaseManager(
        getConfig().getString("database.host"),
        getConfig().getString("database.user"),
        getConfig().getString("database.password"),
        getConfig().getInt("database.port")
    );

    dm.enable();
    databaseHelper = dm.getDatabaseHelper();
    new EnchantmentManager(this).enable();
    new ItemManager(this).enable();
    this.playerRegistry = new PlayerRegistry(databaseHelper.getBags());
    this.playerService = new PlayerService(playerRegistry);

    new EventManager().enable();
    new SkillManager().enable();
    new RecipeManager().enable();
    new AutoSaveManager().enable();
    new BankManager().enable();
    new NpcManager().enable();
    NpcRepository npcRepository = new NpcRepository(databaseHelper);
    NpcSpawner npcSpawner = new NpcSpawner();
    NpcValidator npcValidator = new NpcValidator();
    this.npcService = new NpcService(npcRepository, npcSpawner, npcValidator);
    this.npcDialogueTracker = new NpcDialogueTracker();
    stopLoading();
    new WorldManager().enable();
    new GroupManager().enable();
    new PositionHighlightManager().enable();
    new ScoreBoardManager().enable();

    dm.afterWorldLoaded();
    new BukkitRunnable() {
      @Override
      public void run() {
        RelluEssentials.getInstance().getNpcService().loadAndSpawnNpcsInLoadedChunks();
      }
    }.runTaskLater(RelluEssentials.getInstance(), 20L);
  }

  @Override
  public void onDisable() {
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_STOP_MESSAGE));
    if (npcService != null) {
      npcService.despawnAllNPCs();
    }
    new SudoManager().disable();
    new AutoSaveManager().disable();
    new WorldManager().disable();
    new CleanUpManager().disable();
    new ConfigManager().disable();
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