package de.relluem94.minecraft.server.spigot.essentials;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ShapelessRecipe;

import de.relluem94.minecraft.server.spigot.essentials.commands.Cookies;
import de.relluem94.minecraft.server.spigot.essentials.commands.Day;
import de.relluem94.minecraft.server.spigot.essentials.commands.Enderchest;
import de.relluem94.minecraft.server.spigot.essentials.commands.Exit;
import de.relluem94.minecraft.server.spigot.essentials.commands.Fly;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameMode;
import de.relluem94.minecraft.server.spigot.essentials.commands.Inventory;
import de.relluem94.minecraft.server.spigot.essentials.commands.More;
import de.relluem94.minecraft.server.spigot.essentials.commands.Nick;
import de.relluem94.minecraft.server.spigot.essentials.commands.Night;
import de.relluem94.minecraft.server.spigot.essentials.commands.PermissionsGroup;
import de.relluem94.minecraft.server.spigot.essentials.commands.PortableCraftingBench;
import de.relluem94.minecraft.server.spigot.essentials.commands.Repair;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rain;
import de.relluem94.minecraft.server.spigot.essentials.commands.Spawn;
import de.relluem94.minecraft.server.spigot.essentials.commands.Storm;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.commands.Suicide;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sun;
import de.relluem94.minecraft.server.spigot.essentials.commands.Teleport;
import de.relluem94.minecraft.server.spigot.essentials.NPC.BagSalesman;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Banker;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Beekeeper;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Enchanter;
import de.relluem94.minecraft.server.spigot.essentials.NPC.NPC;
import de.relluem94.minecraft.server.spigot.essentials.commands.AFK;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.commands.Bags;
import de.relluem94.minecraft.server.spigot.essentials.commands.Broadcast;
import de.relluem94.minecraft.server.spigot.essentials.commands.God;
import de.relluem94.minecraft.server.spigot.essentials.commands.Heal;
import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.commands.Message;
import de.relluem94.minecraft.server.spigot.essentials.commands.Print;
import de.relluem94.minecraft.server.spigot.essentials.commands.Protect;
import de.relluem94.minecraft.server.spigot.essentials.commands.Purse;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rename;
import de.relluem94.minecraft.server.spigot.essentials.commands.Speed;
import de.relluem94.minecraft.server.spigot.essentials.commands.Title;
import de.relluem94.minecraft.server.spigot.essentials.commands.Where;
import de.relluem94.minecraft.server.spigot.essentials.commands.Head;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rollback;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sign;
import de.relluem94.minecraft.server.spigot.essentials.commands.TestCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.Vanish;
import de.relluem94.minecraft.server.spigot.essentials.commands.Poke;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterBags;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterBlockDrop;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLights;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLock;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterMobs;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerJoin;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerQuit;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSavety;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSoil;
import de.relluem94.minecraft.server.spigot.essentials.events.MOTD;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterNPC;
import de.relluem94.minecraft.server.spigot.essentials.events.NoDeathMessage;
import de.relluem94.minecraft.server.spigot.essentials.events.PlayerMove;
import de.relluem94.minecraft.server.spigot.essentials.events.BlockPlace;
import de.relluem94.minecraft.server.spigot.essentials.events.CloudSailor;
import de.relluem94.minecraft.server.spigot.essentials.events.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.events.SkullInfo;
import de.relluem94.minecraft.server.spigot.essentials.events.ToolCrafting;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.rellulib.stores.DoubleStore;
import de.relluem94.minecraft.server.spigot.essentials.commands.Gamerules;
import de.relluem94.minecraft.server.spigot.essentials.commands.Worlds;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.Vector2Location;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionLockEntry;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessageInChannel;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.*;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_SMELTER_FUEL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_SMELTER_FURNACE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_SMELTER_PICKAXE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_SMELTER_TANK;

import de.relluem94.minecraft.server.spigot.essentials.events.SignActions;
import de.relluem94.minecraft.server.spigot.essentials.events.SignClick;
import de.relluem94.minecraft.server.spigot.essentials.events.SignEdit;

public class RelluEssentials extends JavaPlugin {

    // TODO add Animal Protection (horse cat dog?)

    public static PluginManager pm = Bukkit.getServer().getPluginManager();
    public static ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    public static Scoreboard board;
    public static File dataFolder;
    public static DatabaseHelper dBH;
    public static PluginInformationEntry pie;
    public static final boolean DEBUG = true;
    private static long start;
    private static RelluEssentials instance;
    public static boolean isOreRespawnEnabled = false;
    private static ResourceBundle englishProperties;
    private static ResourceBundle germanProperties;
    public static String language;
    public static Banker banker;

    public static HashMap<User, Vector2Location> selections = new HashMap<User, Vector2Location>();
    public static HashMap<UUID, PlayerEntry> playerEntryList = new HashMap<>();
    public static HashMap<UUID, PlayerEntry> sudoers = new HashMap<>();
    public static HashMap<Material, DoubleStore> dropMap = new HashMap<>();
    public static HashMap<Location, ProtectionEntry> protectionEntries = new HashMap<>();
    public static HashMap<UUID, BankAccountEntry> bankInterestMap = new HashMap<>();
    public static HashMap<Material, Material> crops = new HashMap<>();

    public static List<LocationEntry> locationEntryList = new ArrayList<>();
    public static List<GroupEntry> groupEntryList = new ArrayList<>();
    public static List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
    public static List<BlockHistoryEntry> blockHistoryList = new ArrayList<>();
    public static List<BagTypeEntry> bagTypeEntryList = new ArrayList<BagTypeEntry>();
    public static List<BagEntry> playerBagEntryList = new ArrayList<BagEntry>();
    public static List<ItemStack> bagBlocks2collect = new ArrayList<>();
    public static List<ProtectionLockEntry> protectionLocksEntryList;
    public static List<Material> protectionLocksList = new ArrayList<>();
    public static List<BankTierEntry> bankTiersList = new ArrayList<>();
    public static List<NPCEntry> npcEntryList = new ArrayList<NPCEntry>();
    public static List<ItemStack> npc_itemstack = new ArrayList<>();
    public static List<String> npc_name = new ArrayList<>();
    public static List<String> npc_trader_title = new ArrayList<>();
    public static List<User> users = new ArrayList<User>();
    public static List<NPC> npcs = new ArrayList<>();

    public static RelluEssentials getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        startLoading();
        dataFolder = this.getDataFolder();

        try {
            configManager(true);
            loadLanguage();
        } catch (IOException ex) {
            Logger.getLogger(RelluEssentials.class.getName()).log(Level.SEVERE, null, ex);
        }
        initCrops();
        initDrops();
        boardManager();
        commandManager();
        databaseManager();
        enchantmentManager();
        groupManager();
        eventManager();
        skillManager();
        addRecipes();
        blockHistoryManager();
        autoSave();
        addNPCs();
        stopLoading();
    }

    private void initDrops() {
        dropMap.put(Material.DIAMOND, new DoubleStore(1, 2));
        dropMap.put(Material.EMERALD, new DoubleStore(1, 2));
        dropMap.put(Material.RAW_IRON, new DoubleStore(1, 3));
        dropMap.put(Material.RAW_COPPER, new DoubleStore(2, 5));
        dropMap.put(Material.RAW_GOLD, new DoubleStore(1, 2));
        dropMap.put(Material.LAPIS_LAZULI, new DoubleStore(2, 5));
        dropMap.put(Material.REDSTONE, new DoubleStore(1, 2));
        dropMap.put(Material.COAL, new DoubleStore(1, 3));
        dropMap.put(Material.QUARTZ, new DoubleStore(1, 3));
    }

    private void initCrops() {
        crops.put(Material.CARROT, Material.CARROTS);
        crops.put(Material.NETHER_WART, Material.NETHER_WART);
        crops.put(Material.POTATO, Material.POTATOES);
        crops.put(Material.WHEAT_SEEDS, Material.WHEAT);
        crops.put(Material.BEETROOT_SEEDS, Material.BEETROOTS);
        crops.put(Material.COCOA_BEANS, Material.COCOA);
    }



    private void addNPCs() {
        new BagSalesman();
        banker = new Banker();
        new Beekeeper();
        new Enchanter();
    }

    @Override
    public void onDisable() {
        ChatHelper.consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_STOP_MESSAGE);

        saveBags();

        protectionEntries.clear();
        playerEntryList.clear();
        locationEntryList.clear();
        groupEntryList.clear();
        locationTypeEntryList.clear();
        blockHistoryList.clear();
        selections.clear();
        bagTypeEntryList.clear();
        
        System.gc();

        try {
            configManager(false);
        } catch (IOException ex) {
            Logger.getLogger(RelluEssentials.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadLanguage() throws IOException {
        germanProperties = ResourceBundle.getBundle("lang", new Locale("de", "DE"));
        englishProperties = ResourceBundle.getBundle("lang", new Locale("en", "US"));
    }

    public static String getText(String language, String key) {
        if (language.equals("eng")) {
            return englishProperties.getString(key);
        }
        if (language.equals("de")) {
            return germanProperties.getString(key);
        }
        return null;
    }

    public static String getText(String key) {
        if (language.equals("eng")) {
            return englishProperties.getString(key);
        }
        if (language.equals("de")) {
            return germanProperties.getString(key);
        }
        return null;
    }

    private void saveBags(){
        int updatedBags = 0;

        for(BagEntry be : playerBagEntryList){
            if(be.hasToBeUpdated()){
                dBH.updateBagEntry(be);
                be.setToBeUpdated(false);
                updatedBags++;
            }
        }
        if(updatedBags != 0){
            sendMessageInChannel("#a " + updatedBags + " Bag(s) saved!", Strings.PLUGIN_CONSOLE_NAME, BetterChatFormat.ADMIN_CHANNEL, Groups.getGroup("admin"));
        }
    }

    private void autoSave(){
        new BukkitRunnable() {
            @Override
            public void run() {
                saveBags();
            }
        }.runTaskTimer(this, 0L,  20 * 60 * 2); // last part is minutes * 2 min * 10 min etc
    }

    private void startLoading() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        start = Calendar.getInstance().getTimeInMillis();
        consoleSendMessage(PLUGIN_COMMAND_COLOR, PLUGIN_BORDER);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_START_MESSAGE);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    }

    private void stopLoading() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + String.format(PLUGIN_STARTTIME, Calendar.getInstance().getTimeInMillis() - start));
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
        consoleSendMessage(PLUGIN_COMMAND_COLOR + PLUGIN_BORDER, "");
    }

    private void configManager(boolean enable) throws IOException {
        /*  Config */
        if (enable) {
            consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_LOADING_CONFIGS);
            this.saveDefaultConfig();
            consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_CONFIGS_LOADED);
        } else {
            this.saveConfig();
        }

        language = getConfig().getString("language");
    }

    public static void reloadConfigs() {
        RelluEssentials.reloadConfigs();
    }

    private void commandManager() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_REGISTER_COMMANDS);
        /*	Commands	*/
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_0)).setExecutor(new GameMode());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_1)).setExecutor(new GameMode());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_2)).setExecutor(new GameMode());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_3)).setExecutor(new GameMode());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_FLY)).setExecutor(new Fly());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_PURSE)).setExecutor(new Purse());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_COOCKIE)).setExecutor(new Cookies());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_CRAFT)).setExecutor(new PortableCraftingBench());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SUN)).setExecutor(new Sun());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_RAIN)).setExecutor(new Rain());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_STORM)).setExecutor(new Storm());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SPAWN)).setExecutor(new Spawn());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_HOME)).setExecutor(new Home());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_DAY)).setExecutor(new Day());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_NIGHT)).setExecutor(new Night());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_MORE)).setExecutor(new More());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_REPAIR)).setExecutor(new Repair());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_ENDERCHEST)).setExecutor(new Enderchest());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_INVENTORY)).setExecutor(new Inventory());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SETGROUP)).setExecutor(new PermissionsGroup());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_NICK)).setExecutor(new Nick());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SUICIDE)).setExecutor(new Suicide());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_HEAL)).setExecutor(new Heal());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GOD)).setExecutor(new God());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_ADMIN)).setExecutor(new Admin());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMERULES)).setExecutor(new Gamerules());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_HEAD)).setExecutor(new Head());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_VANISH)).setExecutor(new Vanish());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_AFK)).setExecutor(new AFK());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_MSG)).setExecutor(new Message());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_REPLY)).setExecutor(new Message());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_TITLE)).setExecutor(new Title());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_WHERE)).setExecutor(new Where());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_PRINT)).setExecutor(new Print());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_BROADCAST)).setExecutor(new Broadcast());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_RENAME)).setExecutor(new Rename());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SPEED)).setExecutor(new Speed());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_POKE)).setExecutor(new Poke());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_WORLD)).setExecutor(new Worlds());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_ROLLBACK)).setExecutor(new Rollback());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_TEST_COMMAND)).setExecutor(new TestCommand());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_PROTECT)).setExecutor(new Protect());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SIGN)).setExecutor(new Sign());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_BAGS)).setExecutor(new Bags());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SUDO)).setExecutor(new Sudo());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_EXIT)).setExecutor(new Exit());
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_TELEPORT)).setExecutor(new Teleport());
        // TODO add Warps
        // TODO add Marriage
        // TODO Fix Command execution for command Blocks

        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_COMMANDS_REGISTERED);
    }

    private void enchantmentManager() {
        registerEnchants(CustomEnchants.autosmelt);
        registerEnchants(CustomEnchants.replenishment);
        registerEnchants(CustomEnchants.telekinesis);
        registerEnchants(CustomEnchants.delicate);
        registerEnchants(CustomEnchants.thunderstrike);
    }

    private void eventManager() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_REGISTER_EVENTS);
        /*	Events	*/
        pm.registerEvents(new BetterChatFormat(), this);
        pm.registerEvents(new BetterPlayerJoin(), this);
        pm.registerEvents(new BetterPlayerQuit(), this);
        pm.registerEvents(new BetterBlockDrop(), this);
        pm.registerEvents(new BetterLights(), this);
        pm.registerEvents(new BetterBags(), this);
        pm.registerEvents(new BlockPlace(), this);
        pm.registerEvents(new BetterMobs(), this);
        pm.registerEvents(new BetterSoil(), this);
        pm.registerEvents(new BetterNPC(), this);
        pm.registerEvents(new BetterSavety(), this);
        pm.registerEvents(new BetterLock(), this);
        pm.registerEvents(new SkullInfo(), this);
        pm.registerEvents(new NoDeathMessage(), this);
        pm.registerEvents(new PlayerMove(), this);
        pm.registerEvents(new MOTD(), this);
        pm.registerEvents(new CloudSailor(), this);
        pm.registerEvents(new SignActions(), this);
        pm.registerEvents(new SignClick(), this);
        pm.registerEvents(new SignEdit(), this);
        pm.registerEvents(new ToolCrafting(), this);
        pm.registerEvents(new CustomEnchantment(), this);
    }

    private void skillManager() {
        /*  Skill Events */
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_EVENTS_REGISTERED);
    }

    private void boardManager() {
        if (sm == null) {
            sm = Bukkit.getServer().getScoreboardManager();
        }

        board = sm.getNewScoreboard();
    }

    private void groupManager() {
        List<PlayerEntry> pel = dBH.getPlayers();
        pel.forEach(p -> {
            playerEntryList.put(UUID.fromString(p.getUUID()), p);
        });

        Bukkit.getOnlinePlayers().forEach(p -> {
            User u = new User(p);
            PlayerEntry pe = playerEntryList.get(p.getUniqueId());
            u.setGroup(pe.getGroup());
        });
    }

    private void registerEnchants(Enchantment ench) {
        try {
            Field f;
            f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(ench);
            consoleSendMessage(PLUGIN_NAME_CONSOLE, String.format(PLUGIN_REGISTER_ENCHANTMENT, ench.getKey().getNamespace(), ench.getKey().toString()));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            consoleSendMessage(PLUGIN_NAME_CONSOLE, ex.getMessage() + ": " + ench.getKey().toString());
        }
    }

    private void databaseManager() {
        dBH = new DatabaseHelper(this.getConfig().getString("database.host"), this.getConfig().getString("database.user"), this.getConfig().getString("database.password"), this.getConfig().getInt("database.port"));
        pie = dBH.getPluginInformation();
        dBH.init();
        locationTypeEntryList.addAll(dBH.getLocationTypes());

        this.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            locationEntryList.addAll(dBH.getLocations());
        });

        groupEntryList.addAll(dBH.getGroups());
        protectionEntries = dBH.getProtections();
        bagTypeEntryList = dBH.getBagTypes();
        playerBagEntryList = dBH.getBags();
        npcEntryList = dBH.getNPCs();
        protectionLocksEntryList = dBH.getProtectionLocks();
        bankTiersList = dBH.getBankTiers();

        for(NPCEntry ne : npcEntryList){
            new NPC(ne){
                @Override
                public org.bukkit.inventory.Inventory getMainGUI(){
                    org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(NPCHelper.INV_SIZE, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());
                    int slot = 0;
                    for(int i = 0; i < ne.getSlotNames().length; i++){
                        slot = InventoryHelper.getNextSlot(slot);
                        if(!ne.getSlotName(i).equals("AIR")){
                            inv.setItem(slot, new ItemStack(Material.valueOf(ne.getSlotName(i)),1));
                        }
                        slot++;
                    }
                    inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());
                    return inv;
                }
            };
        }

        for(int i = 0; i < RelluEssentials.bagTypeEntryList.size(); i++){
            ItemStack[] isa = BagHelper.getItemStacks(RelluEssentials.bagTypeEntryList.get(i));
            for(ItemStack is : isa){
                bagBlocks2collect.add(is);
            }
        }

        for(ProtectionLockEntry ple: protectionLocksEntryList){
            protectionLocksList.add(ple.getValue());
        }

        protectionLocksEntryList = null;
        npcEntryList = null;

        System.gc();

    }

    private void blockHistoryManager() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            if (!blockHistoryList.isEmpty()) {
                BlockHistoryEntry bh = blockHistoryList.get(0);
                BlockHelper.setBlock(bh);
                if (bh.getDeleted() == null) {
                    dBH.insertBlockHistory(bh);
                } else {
                    dBH.deleteBlockHistory(bh);
                }
                blockHistoryList.remove(0);
            }
        }, 0L, 2L);
    }

    private void addRecipes() {
        NamespacedKey cloudBootsKey = new NamespacedKey(this, PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS);
        ShapedRecipe cloudBootsRecipe = new ShapedRecipe(cloudBootsKey, CustomItems.cloudBoots.getCustomItem());
        cloudBootsRecipe.shape("F F", "F F");
        cloudBootsRecipe.setIngredient('F', CustomItems.cloudSailor.getMaterial());

        NamespacedKey smelterFurnaceKey = new NamespacedKey(this, PLUGIN_ITEM_NAMESPACE_SMELTER_FURNACE);
        ShapedRecipe smelterFurnaceRecipe = new ShapedRecipe(smelterFurnaceKey, CustomItems.autoSmeltFurnace.getCustomItem());
        smelterFurnaceRecipe.shape("OOO", "CFC", "OGO");
        smelterFurnaceRecipe.setIngredient('O', Material.OBSIDIAN);
        smelterFurnaceRecipe.setIngredient('F', Material.FURNACE);
        smelterFurnaceRecipe.setIngredient('C', Material.COMPARATOR);
        smelterFurnaceRecipe.setIngredient('G', Material.GLASS);

        NamespacedKey smelterTankKey = new NamespacedKey(this, PLUGIN_ITEM_NAMESPACE_SMELTER_TANK);
        ShapedRecipe smelterTankRecipe = new ShapedRecipe(smelterTankKey, CustomItems.autoSmeltTank.getCustomItem());
        smelterTankRecipe.shape("GGG", "GCG", "GGG");
        smelterTankRecipe.setIngredient('G', Material.GLASS);
        smelterTankRecipe.setIngredient('C', Material.CAULDRON);

        NamespacedKey smelterPickaxeKey = new NamespacedKey(this, PLUGIN_ITEM_NAMESPACE_SMELTER_PICKAXE);
        ShapelessRecipe smelterPickaxeRecipe = new ShapelessRecipe(smelterPickaxeKey, CustomItems.autoSmeltNetheritePickAxe.getCustomItem());
        smelterPickaxeRecipe.addIngredient(Material.NETHERITE_PICKAXE);
        smelterPickaxeRecipe.addIngredient(CustomItems.autoSmeltTank.getMaterial());
        smelterPickaxeRecipe.addIngredient(CustomItems.autoSmeltFurnace.getMaterial());

        NamespacedKey smelterFuelKey = new NamespacedKey(this, PLUGIN_ITEM_NAMESPACE_SMELTER_FUEL);
        ShapelessRecipe smelterFuelRecipe = new ShapelessRecipe(smelterFuelKey, CustomItems.autoSmeltNetheritePickAxe.getCustomItem());
        smelterFuelRecipe.addIngredient(CustomItems.autoSmeltNetheritePickAxe.getMaterial());
        smelterFuelRecipe.addIngredient(Material.LAVA_BUCKET);

        Bukkit.addRecipe(smelterFuelRecipe);
        Bukkit.addRecipe(smelterFurnaceRecipe);
        Bukkit.addRecipe(smelterTankRecipe);
        Bukkit.addRecipe(smelterPickaxeRecipe);
        Bukkit.addRecipe(cloudBootsRecipe);
    }

    public void registerPlayerSave(){ // COULD BE USED INSTEAD OF UPDATING ON EACH EVENT
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage("PURSE SAVE");
                
                Bukkit.getOnlinePlayers().forEach(p -> {
                    PlayerEntry pe = playerEntryList.get(p.getUniqueId());
                    dBH.updatePlayer(pe);
                });

                Bukkit.broadcastMessage("PURSE SAVE DONE");
            }
        }.runTaskTimer(this, 0L,  20 * 60 * 2);
    }
}
