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

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ShapelessRecipe;

import de.relluem94.minecraft.server.spigot.essentials.commands.Cookies;
import de.relluem94.minecraft.server.spigot.essentials.commands.Day;
import de.relluem94.minecraft.server.spigot.essentials.commands.Enderchest;
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
import de.relluem94.minecraft.server.spigot.essentials.commands.Suicide;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sun;
import de.relluem94.minecraft.server.spigot.essentials.commands.AFK;
import de.relluem94.minecraft.server.spigot.essentials.commands.Broadcast;
import de.relluem94.minecraft.server.spigot.essentials.commands.God;
import de.relluem94.minecraft.server.spigot.essentials.commands.Heal;
import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.commands.Message;
import de.relluem94.minecraft.server.spigot.essentials.commands.Print;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rellu;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rename;
import de.relluem94.minecraft.server.spigot.essentials.commands.Speed;
import de.relluem94.minecraft.server.spigot.essentials.commands.Title;
import de.relluem94.minecraft.server.spigot.essentials.commands.Where;
import de.relluem94.minecraft.server.spigot.essentials.commands.ClearChat;
import de.relluem94.minecraft.server.spigot.essentials.commands.Head;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rollback;
import de.relluem94.minecraft.server.spigot.essentials.commands.TestCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.Vanish;
import de.relluem94.minecraft.server.spigot.essentials.commands.Poke;

import de.relluem94.minecraft.server.spigot.essentials.events.BetterBlockDrop;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterMobs;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerJoin;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerQuit;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSavety;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSoil;
import de.relluem94.minecraft.server.spigot.essentials.events.MOTD;
import de.relluem94.minecraft.server.spigot.essentials.events.NoDeathMessage;
import de.relluem94.minecraft.server.spigot.essentials.events.PlayerMove;
import de.relluem94.minecraft.server.spigot.essentials.events.BlockPlace;
import de.relluem94.minecraft.server.spigot.essentials.events.CloudSailor;
import de.relluem94.minecraft.server.spigot.essentials.events.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.events.SkullInfo;
import de.relluem94.minecraft.server.spigot.essentials.events.ToolCrafting;

import de.relluem94.minecraft.server.spigot.essentials.events.features.RotationTool;
import de.relluem94.minecraft.server.spigot.essentials.events.features.SelectionTool;

import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_AutoReplant;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_Repair;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_Salvage;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_AutoSmelt;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_Telekinesis;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_TreeFeller;

import de.relluem94.minecraft.server.spigot.essentials.enchantment.AutoSmelt;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.Telekinesis;

import de.relluem94.minecraft.server.spigot.essentials.permissions.User;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.commands.Gamerules;
import de.relluem94.minecraft.server.spigot.essentials.commands.Worlds;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.Vector2Location;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import java.util.Calendar;

public class RelluEssentials extends JavaPlugin {

    public static PluginManager pm = Bukkit.getServer().getPluginManager();
    public static ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    public static Scoreboard board;
    public static HashMap<User, Vector2Location> selections = new HashMap<User, Vector2Location>();
    public static HashMap<UUID, PlayerEntry> playerEntryList = new HashMap<>();
    public static List<LocationEntry> locationEntryList = new ArrayList<>();
    public static List<GroupEntry> groupEntryList = new ArrayList<>();
    public static List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
    public static List<BlockHistoryEntry> blockHistoryList = new ArrayList<>();

    public static AutoSmelt autosmelt = new AutoSmelt(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), PLUGIN_ENCHANTMENT_AUTOSMELT.toLowerCase()));
    public static Telekinesis telekinesis = new Telekinesis(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), PLUGIN_ENCHANTMENT_TELEKINESIS.toLowerCase()));

    public static List<User> users = new ArrayList<User>();
    public static File dataFolder;

    public static DatabaseHelper dBH;
    public static PluginInformationEntry pie;

    public static final boolean DEBUG = true;
    private static long start;

    @Override
    public void onEnable() {
        startLoading();
        dataFolder = this.getDataFolder();

        try {
            configManager(true);
        } catch (IOException ex) {
            Logger.getLogger(RelluEssentials.class.getName()).log(Level.SEVERE, null, ex);
        }

        boardManager();
        commandManager();
        databaseManager();
        enchantmentManager();
        groupManager();
        eventManager();
        featureManager();
        skillManager();
        addRecipes();
        blockHistoryManager();
        stopLoading();
    }

    @Override
    public void onDisable() {
        ChatHelper.consoleSendMessage(PLUGIN_PREFIX, PLUGIN_STOP_MESSAGE);
        try {
            configManager(false);
        } catch (IOException ex) {
            Logger.getLogger(RelluEssentials.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void startLoading() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        
        start = Calendar.getInstance().getTimeInMillis();
        consoleSendMessage(PLUGIN_COMMAND_COLOR, PLUGIN_BORDER);
        consoleSendMessage(PLUGIN_PREFIX, "");
        consoleSendMessage(PLUGIN_PREFIX, "");
        consoleSendMessage(PLUGIN_PREFIX, PLUGIN_START_MESSAGE);
        consoleSendMessage(PLUGIN_PREFIX, "");
    }

    private void stopLoading() {
        consoleSendMessage(PLUGIN_PREFIX, "");
        consoleSendMessage(PLUGIN_PREFIX, PLUGIN_COMMAND_COLOR + String.format(PLUGIN_STARTTIME, Calendar.getInstance().getTimeInMillis() - start));
        consoleSendMessage(PLUGIN_PREFIX, "");
        consoleSendMessage(PLUGIN_COMMAND_COLOR + PLUGIN_BORDER, "");
    }

    private void configManager(boolean enable) throws IOException {
        consoleSendMessage(PLUGIN_PREFIX, PLUGIN_COMMAND_COLOR + LANG_LOADING_CONFIGS);
        /*  Config */
        if (enable) {
            this.saveDefaultConfig();
        } else {
            this.saveConfig();
        }
        consoleSendMessage(PLUGIN_PREFIX, PLUGIN_COMMAND_COLOR + LANG_CONFIGS_LOADED);
    }

    public static void reloadConfigs() {
        RelluEssentials.reloadConfigs();
    }

    private void commandManager() {
        consoleSendMessage(PLUGIN_PREFIX, PLUGIN_COMMAND_COLOR + LANG_REGISTER_COMMANDS);
        /*	Commands	*/
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_0   )).setExecutor(new GameMode()               );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_1   )).setExecutor(new GameMode()               );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_2   )).setExecutor(new GameMode()               );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_3   )).setExecutor(new GameMode()               );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_FLY          )).setExecutor(new Fly()                    );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_COOCKIE      )).setExecutor(new Cookies()                );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_CRAFT        )).setExecutor(new PortableCraftingBench()  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SUN          )).setExecutor(new Sun()                    );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_RAIN         )).setExecutor(new Rain()                   );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_STORM        )).setExecutor(new Storm()                  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SPAWN        )).setExecutor(new Spawn()                  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_HOME         )).setExecutor(new Home()                   );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_DAY          )).setExecutor(new Day()                    );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_NIGHT        )).setExecutor(new Night()                  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_MORE         )).setExecutor(new More()                   );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_REPAIR       )).setExecutor(new Repair()                 );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_ENDERCHEST   )).setExecutor(new Enderchest()             );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_INVENTORY    )).setExecutor(new Inventory()              );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SETGROUP     )).setExecutor(new PermissionsGroup()       );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_NICK         )).setExecutor(new Nick()                   );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SUICIDE      )).setExecutor(new Suicide()                );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_RELLU        )).setExecutor(new Rellu()                  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_HEAL         )).setExecutor(new Heal()                   );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GOD          )).setExecutor(new God()                    );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_GAMERULES    )).setExecutor(new Gamerules()              );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_HEAD         )).setExecutor(new Head()                   );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_VANISH       )).setExecutor(new Vanish()                 );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_CLEARCHAT    )).setExecutor(new ClearChat()              );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_AFK          )).setExecutor(new AFK()                    );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_MSG          )).setExecutor(new Message()                );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_REPLY        )).setExecutor(new Message()                );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_TITLE        )).setExecutor(new Title()                  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_WHERE        )).setExecutor(new Where()                  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_PRINT        )).setExecutor(new Print()                  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_BROADCAST    )).setExecutor(new Broadcast()              );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_RENAME       )).setExecutor(new Rename()                 );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_SPEED        )).setExecutor(new Speed()                  );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_POKE         )).setExecutor(new Poke()                   );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_WORLD        )).setExecutor(new Worlds()                 );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_ROLLBACK     )).setExecutor(new Rollback()               );
        Objects.requireNonNull(this.getCommand(PLUGIN_COMMAND_NAME_TEST_COMMAND )).setExecutor(new TestCommand()            );
        // @TODO add Warps
        // @TODO add Marriage
        // @TODO Fix Command execution for command Blocks
        
        consoleSendMessage(PLUGIN_PREFIX, PLUGIN_COMMAND_COLOR + LANG_COMMANDS_REGISTERED);
    }

    private void enchantmentManager() {
        registerEnchants(autosmelt);
        registerEnchants(telekinesis);
    }

    private void eventManager() {
        consoleSendMessage(PLUGIN_PREFIX, PLUGIN_COMMAND_COLOR + LANG_REGISTER_EVENTS);
        /*	Events	*/
        pm.registerEvents(new BetterChatFormat(),   this);
        pm.registerEvents(new BetterPlayerJoin(),   this);
        pm.registerEvents(new BetterPlayerQuit(),   this);
        pm.registerEvents(new BetterBlockDrop(),    this);
        pm.registerEvents(new BlockPlace(),         this);
        pm.registerEvents(new BetterMobs(),         this);
        pm.registerEvents(new BetterSoil(),         this);
        pm.registerEvents(new SkullInfo(),          this);
        pm.registerEvents(new BetterSavety(),       this);
        pm.registerEvents(new NoDeathMessage(),     this);
        pm.registerEvents(new PlayerMove(),         this);
        pm.registerEvents(new MOTD(),               this);
        pm.registerEvents(new CloudSailor(),        this);
        pm.registerEvents(new ToolCrafting(),       this);
        pm.registerEvents(new CustomEnchantment(),  this); // @TODO is enchanted but is lost on anvil use ( like book and pickaxe )
    }

    private void featureManager() {
        pm.registerEvents(new SelectionTool(),      this);
        pm.registerEvents(new RotationTool(),       this);
    }

    private void skillManager() {
        /*  Skill Events */
        pm.registerEvents(new Ev_Repair(),          this);
        pm.registerEvents(new Ev_Salvage(),         this);
        pm.registerEvents(new Ev_TreeFeller(),      this); // @TODO Big Bug Wrong implementation
        pm.registerEvents(new Ev_AutoReplant(),     this);
        pm.registerEvents(new Ev_AutoSmelt(),       this);
        pm.registerEvents(new Ev_Telekinesis(),     this);
        consoleSendMessage(PLUGIN_PREFIX, PLUGIN_COMMAND_COLOR + LANG_EVENTS_REGISTERED);
    }

    private void boardManager() {
        if (sm == null) {
            sm = Bukkit.getServer().getScoreboardManager();
        }

        board = sm.getNewScoreboard();
        Objective o = board.registerNewObjective("player deaths", "death", "player deaths");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private void groupManager() {
        List<PlayerEntry> pel = dBH.getPlayers();
        pel.forEach(p -> {
            playerEntryList.put(UUID.fromString(p.getUUID()), p);
        });

        Bukkit.getOnlinePlayers().forEach(p -> {
            User u = new User(p);
            //TODO Add Array for Users to Access it directly without the other class. (Maybe?)
            //TODO Remove Todo above. Also (Maybe?) remove User thing. could be replaced by the pojo stuff we have.
        });
    }

    private void registerEnchants(Enchantment ench) {
        try {
            Field f;
            f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(ench);
            consoleSendMessage(PLUGIN_PREFIX, String.format(PLUGIN_REGISTER_ENCHANTMENT, ench.getName(), ench.getKey().toString()));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            consoleSendMessage(PLUGIN_PREFIX, ex.getMessage() + ": " + ench.getKey().toString());
        }
    }

    private void databaseManager() {
        dBH = new DatabaseHelper(this.getConfig().getString("database.host"), this.getConfig().getString("database.user"), this.getConfig().getString("database.password"), this.getConfig().getInt("database.port"));
        pie = dBH.getPluginInformation();
        dBH.init();
        locationTypeEntryList.addAll(dBH.getLocationTypes());
        locationEntryList.addAll(dBH.getLocations());
        groupEntryList.addAll(dBH.getGroups());
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
}
