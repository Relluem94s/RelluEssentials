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
import org.bukkit.configuration.InvalidConfigurationException;
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
import de.relluem94.minecraft.server.spigot.essentials.helpers.ConfigHelper;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_REGISTER_ENCHANTMENT;

public class RelluEssentials extends JavaPlugin {

    public static PluginManager pm = Bukkit.getServer().getPluginManager();
    public static ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    public static Scoreboard board;
    public static HashMap<User, Vector2Location> selections = new HashMap<User, Vector2Location>();
    
    public static AutoSmelt autosmelt = new AutoSmelt(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "autosmelt"));
    public static Telekenesis telekenesis = new Telekenesis(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis"));

    public static ConfigHelper players;
    public static ConfigHelper warps;

    public static List<User> users = new ArrayList<User>();
    public static File dataFolder;

    @Override
    public void onEnable() {
        System.out.println(Strings.PLUGIN_NAME_CONSOLE + Strings.PLUGIN_START_MESSAGE);

        dataFolder = this.getDataFolder();

        try {
            configManager(true);
        } catch (IOException e) {
            System.out.println(Strings.PLUGIN_NAME_CONSOLE + e.getMessage());
        }

        commandManager();
        eventManager();
        featureManager();
        skillManager();
        boardManager();
        groupManager();
        enchantmentManager();
    }

    @Override
    public void onDisable() {
        System.out.println(Strings.PLUGIN_NAME_CONSOLE + Strings.PLUGIN_STOP_MESSAGE);
        try {
            configManager(false);
        } catch (IOException e) {
            System.out.println(Strings.PLUGIN_NAME_CONSOLE + e.getMessage());
        }
    }

    private void configManager(boolean enable) throws IOException {
        /*  Config */
        if (enable) {
            this.saveDefaultConfig();
            players = new ConfigHelper("players");
            warps = new ConfigHelper("warps");
        } else {
            saveConfigs();
        }
    }

    public static void reloadConfigs() {
        ((RelluEssentials) Bukkit.getPluginManager().getPlugin(PLUGIN_NAME)).reloadConfig();
        try {
            players.reload();
            warps.reload();
        } catch (IOException | InvalidConfigurationException e) {
            System.out.println(Strings.PLUGIN_NAME_CONSOLE + e.getMessage());
        }
    }

    public static void saveConfigs() {
        ((RelluEssentials) Bukkit.getPluginManager().getPlugin(PLUGIN_NAME)).saveConfig();
        try {
            players.save();
            warps.save();
        } catch (IOException e) {
            System.out.println(Strings.PLUGIN_NAME_CONSOLE + e.getMessage());
        }
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
        Objects.requireNonNull(this.getCommand("afk")).setExecutor(new AFK());
        Objects.requireNonNull(this.getCommand("msg")).setExecutor(new Message());
        Objects.requireNonNull(this.getCommand("r")).setExecutor(new Message());
        Objects.requireNonNull(this.getCommand("title")).setExecutor(new Title());
        Objects.requireNonNull(this.getCommand("where")).setExecutor(new Where());
        Objects.requireNonNull(this.getCommand("print")).setExecutor(new Print());
        Objects.requireNonNull(this.getCommand("broadcast")).setExecutor(new Broadcast());
        Objects.requireNonNull(this.getCommand("rename")).setExecutor(new Rename());
        Objects.requireNonNull(this.getCommand("speed")).setExecutor(new Speed());
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
        Bukkit.getOnlinePlayers().forEach(p -> {
            @SuppressWarnings("unused")
            User u = new User(p, User.getGroup(p));
            //TODO Add Array for Users to Access it directly without the other class. (Maybe?)
        });
    }

    private void registerEnchants(Enchantment ench) {
        try {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                System.out.println(Strings.PLUGIN_NAME_CONSOLE + e.getMessage());
            }
            try {
                Enchantment.registerEnchantment(ench);
                System.out.println(String.format(PLUGIN_REGISTER_ENCHANTMENT, ench.getName(), ench.getKey().toString()));
            } catch (IllegalArgumentException e) {
                System.out.println(Strings.PLUGIN_NAME_CONSOLE + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println(Strings.PLUGIN_NAME_CONSOLE + e.getMessage());
        }
    }
}
