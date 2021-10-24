package de.relluem94.minecraft.server.spigot.essentials;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

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
import de.relluem94.minecraft.server.spigot.essentials.events.BetterBlockDrop;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterMobs;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerJoin;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerQuit;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSavety;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSoil;
import de.relluem94.minecraft.server.spigot.essentials.events.MOTD;
import de.relluem94.minecraft.server.spigot.essentials.events.NoDeathMessage;
import de.relluem94.minecraft.server.spigot.essentials.events.features.RotationTool;
import de.relluem94.minecraft.server.spigot.essentials.events.features.SelectionTool;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_AutoReplant;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_Repair;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_Salvage;
import de.relluem94.minecraft.server.spigot.essentials.helpers.Vector2Location;
import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.commands.AFK;
import de.relluem94.minecraft.server.spigot.essentials.commands.Broadcast;
import de.relluem94.minecraft.server.spigot.essentials.commands.Enchanttest;
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
import de.relluem94.minecraft.server.spigot.essentials.enchantment.AutoSmelt;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.Telekenesis;
import de.relluem94.minecraft.server.spigot.essentials.events.PlayerMove;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_AutoSmelt;
import de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_Telekenesis;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_REGISTER_ENCHANTMENT;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_CONSOLE;
import de.relluem94.minecraft.server.spigot.essentials.commands.ClearChat;
import de.relluem94.minecraft.server.spigot.essentials.commands.Head;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rollback;
import de.relluem94.minecraft.server.spigot.essentials.commands.TestCommand;
import de.relluem94.minecraft.server.spigot.essentials.events.BlockPlace;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.scheduler.BukkitScheduler;

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

    public static AutoSmelt autosmelt = new AutoSmelt(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "autosmelt"));
    public static Telekenesis telekenesis = new Telekenesis(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis"));

    public static List<User> users = new ArrayList<User>();
    public static File dataFolder;

    public static DatabaseHelper dBH;
    public static PluginInformationEntry pie;

    public static final boolean DEBUG = true;

    @Override
    public void onEnable() {
        consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, Strings.PLUGIN_START_MESSAGE);
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
        eventManager();
        featureManager();
        groupManager();
        skillManager();
        blockHistoryManager();
    }

    @Override
    public void onDisable() {
        consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, Strings.PLUGIN_STOP_MESSAGE);

        try {
            configManager(false);
        } catch (IOException ex) {
            Logger.getLogger(RelluEssentials.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void configManager(boolean enable) throws IOException {
        /*  Config */
        if (enable) {
            this.saveDefaultConfig();
        } else {
            //saveConfigs();
            this.saveConfig();
        }
    }

    public static void reloadConfigs() {
        RelluEssentials.reloadConfigs();
    }

    private void commandManager() {
        /*	Commands	*/
        Objects.requireNonNull(this.getCommand("0")).setExecutor(new GameMode());
        Objects.requireNonNull(this.getCommand("1")).setExecutor(new GameMode());
        Objects.requireNonNull(this.getCommand("2")).setExecutor(new GameMode());
        Objects.requireNonNull(this.getCommand("3")).setExecutor(new GameMode());
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(new Fly());
        Objects.requireNonNull(this.getCommand("cookie")).setExecutor(new Cookies());
        Objects.requireNonNull(this.getCommand("craft")).setExecutor(new PortableCraftingBench());
        Objects.requireNonNull(this.getCommand("sun")).setExecutor(new Sun());
        Objects.requireNonNull(this.getCommand("rain")).setExecutor(new Rain());
        Objects.requireNonNull(this.getCommand("storm")).setExecutor(new Storm());
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new Spawn());
        Objects.requireNonNull(this.getCommand("home")).setExecutor(new Home());
        Objects.requireNonNull(this.getCommand("day")).setExecutor(new Day());
        Objects.requireNonNull(this.getCommand("night")).setExecutor(new Night());
        Objects.requireNonNull(this.getCommand("more")).setExecutor(new More());
        Objects.requireNonNull(this.getCommand("repair")).setExecutor(new Repair());
        Objects.requireNonNull(this.getCommand("enderchest")).setExecutor(new Enderchest());
        Objects.requireNonNull(this.getCommand("inv")).setExecutor(new Inventory());
        Objects.requireNonNull(this.getCommand("setGroup")).setExecutor(new PermissionsGroup());
        Objects.requireNonNull(this.getCommand("nick")).setExecutor(new Nick());
        Objects.requireNonNull(this.getCommand("suicide")).setExecutor(new Suicide());
        Objects.requireNonNull(this.getCommand("enchanttest")).setExecutor(new Enchanttest());
        Objects.requireNonNull(this.getCommand("rellu")).setExecutor(new Rellu());
        Objects.requireNonNull(this.getCommand("heal")).setExecutor(new Heal());
        Objects.requireNonNull(this.getCommand("god")).setExecutor(new God());
        Objects.requireNonNull(this.getCommand("head")).setExecutor(new Head());
        Objects.requireNonNull(this.getCommand("cc")).setExecutor(new ClearChat());
        Objects.requireNonNull(this.getCommand("afk")).setExecutor(new AFK());
        Objects.requireNonNull(this.getCommand("msg")).setExecutor(new Message());
        Objects.requireNonNull(this.getCommand("r")).setExecutor(new Message());
        Objects.requireNonNull(this.getCommand("title")).setExecutor(new Title());
        Objects.requireNonNull(this.getCommand("where")).setExecutor(new Where());
        Objects.requireNonNull(this.getCommand("print")).setExecutor(new Print());
        Objects.requireNonNull(this.getCommand("broadcast")).setExecutor(new Broadcast());
        Objects.requireNonNull(this.getCommand("rename")).setExecutor(new Rename());
        Objects.requireNonNull(this.getCommand("speed")).setExecutor(new Speed());
        Objects.requireNonNull(this.getCommand("rollback")).setExecutor(new Rollback());
        Objects.requireNonNull(this.getCommand("AQmNCRXEdwSGU7DvEcXTbBkp2qEaCSSNkQcMhL3m7KSDtmXWaxtbYCaQCFBR96fj")).setExecutor(new TestCommand());
        //TODO add Warps
        //TODO add Marriage
    }

    private void enchantmentManager() {
        registerEnchants(autosmelt);
        registerEnchants(telekenesis);
    }

    private void eventManager() {
        /*	Events	*/
        pm.registerEvents(new BetterChatFormat(), this);
        pm.registerEvents(new BetterPlayerJoin(), this);
        pm.registerEvents(new BetterPlayerQuit(), this);
        pm.registerEvents(new BetterBlockDrop(), this);
        pm.registerEvents(new BlockPlace(), this);
        pm.registerEvents(new BetterMobs(), this);
        pm.registerEvents(new BetterSoil(), this);
        pm.registerEvents(new BetterSavety(), this);
        pm.registerEvents(new NoDeathMessage(), this);
        pm.registerEvents(new PlayerMove(), this);
        pm.registerEvents(new MOTD(), this);
        //pm.registerEvents(new CustomEnchantment(), this); // TODO Enchantments not visible and not enchanted
    }

    private void featureManager() {
        pm.registerEvents(new SelectionTool(), this);
        pm.registerEvents(new RotationTool(), this);
    }

    private void skillManager() {
        /*  Skill Events */
        pm.registerEvents(new Ev_Repair(), this);
        pm.registerEvents(new Ev_Salvage(), this);
        // pm.registerEvents(new Ev_TreeFeller(), this); TODO Big Bug Wrong implementation
        pm.registerEvents(new Ev_AutoReplant(), this);
        pm.registerEvents(new Ev_AutoSmelt(), this);
        pm.registerEvents(new Ev_Telekenesis(), this);
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
            @SuppressWarnings("unused")
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
            consoleSendMessage(PLUGIN_NAME_CONSOLE, String.format(PLUGIN_REGISTER_ENCHANTMENT, ench.getName(), ench.getKey().toString()));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            consoleSendMessage(PLUGIN_NAME_CONSOLE, ex.getMessage());
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
}
