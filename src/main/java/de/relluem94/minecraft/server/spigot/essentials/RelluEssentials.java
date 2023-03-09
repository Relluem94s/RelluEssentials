package de.relluem94.minecraft.server.spigot.essentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.relluem94.rellulib.stores.DoubleStore;

import de.relluem94.minecraft.server.spigot.essentials.NPC.Banker;
import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.types.Vector2Location;
import de.relluem94.minecraft.server.spigot.essentials.managers.AutoSaveManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.BlockHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.CleanUpManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ConfigManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.DatabaseManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.EnchantmentManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.EventManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.GroupManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.NPCManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.RecipeManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SkillManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.WorldManager;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

public class RelluEssentials extends JavaPlugin {

    // TODO add Animal Protection (horse cat dog?)
    // TODO add /trade to trade with player (coins and items)
    // TODO change /tp to request teleport (if not mod) 
    // TODO add Marriage (WIP)
    // TODO Fix Command execution for command Blocks

    public static ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    public static Scoreboard board;
    public static DatabaseHelper dBH;
    public static PluginInformationEntry pie;
    private static long start;
    private static RelluEssentials instance;
    public static ResourceBundle englishProperties;
    public static ResourceBundle germanProperties;
    public static String language;
    public static Banker banker;

    public static HashMap<User, Vector2Location> selections = new HashMap<User, Vector2Location>();
    public static HashMap<UUID, PlayerEntry> sudoers = new HashMap<UUID, PlayerEntry>();
    public static HashMap<UUID, BankAccountEntry> bankInterestMap = new HashMap<UUID, BankAccountEntry>();
    public static HashMap<Material, DoubleStore> dropMap = new HashMap<Material, DoubleStore>();
    public static HashMap<Material, Material> crops = new HashMap<Material, Material>();
    public static Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create() ;

    public static List<User> users = new ArrayList<User>();
    public static List<LocationEntry> locationEntryList = new ArrayList<>();
    public static List<GroupEntry> groupEntryList = new ArrayList<>();
    public static List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
    public static List<BlockHistoryEntry> blockHistoryList = new ArrayList<>();
    public static List<ItemStack> bagBlocks2collect = new ArrayList<>();

    public static String[] ore_respawn = new String[]{"world_nether", "123_nether"}; //TODO has to be done in Config (new Table?) #IsComming
    public static boolean moneyLostOnDeath = true;
    public final static String[] worlds = new String[]{"123", "123_nether", "123_the_end", "world", "world_nether", "world_the_end", "lobby"};
    public final String[][] worlds_group = new String[][]{new String[]{"123", "123_nether", "123_the_end"}, new String[]{"world", "world_nether", "world_the_end"}, new String[]{"lobby"}};
    
    public static RelluEssentials getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        startLoading();
        new ConfigManager().enable();
        new ScoreBoardManager().enable();
        new CommandManager().enable();
        new DatabaseManager().enable();
        new EnchantmentManager().enable();
        new GroupManager().enable();
        new EventManager().enable();
        new SkillManager().enable();
        new RecipeManager().enable();
        new BlockHistoryManager().enable();
        new AutoSaveManager().enable();
        new NPCManager().enable();
        stopLoading();
        new WorldManager().enable();
    }

    @Override
    public void onDisable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_STOP_MESSAGE);
        new SudoManager().disable();
        new AutoSaveManager().disable();
        new WorldManager().disable();
        new CleanUpManager().disable();
        new ConfigManager().disable();
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

    private void startLoading() {
        start = Calendar.getInstance().getTimeInMillis();
        consoleSendMessage(PLUGIN_COMMAND_COLOR, PLUGIN_BORDER);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "", 2);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_START_MESSAGE);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    }

    private void stopLoading() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + String.format(PLUGIN_STARTTIME, Calendar.getInstance().getTimeInMillis() - start));
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
        consoleSendMessage(PLUGIN_COMMAND_COLOR + PLUGIN_BORDER, "");
    }
}