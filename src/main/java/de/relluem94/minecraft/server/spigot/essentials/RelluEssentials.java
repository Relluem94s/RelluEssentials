package de.relluem94.minecraft.server.spigot.essentials;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.ResourceBundle;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.relluem94.rellulib.stores.DoubleStore;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.types.Vector2Location;
import de.relluem94.minecraft.server.spigot.essentials.managers.AutoSaveManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.BankManager;
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
import de.relluem94.minecraft.server.spigot.essentials.npc.Banker;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

public class RelluEssentials extends JavaPlugin {

    private long start;
    private static RelluEssentials instance;

    private DatabaseHelper dBH;
    public static PluginInformationEntry pie;
    public static ResourceBundle englishProperties;
    public static ResourceBundle germanProperties;
    public static String language;
    private static Banker banker;

    private boolean isUnitTest = false;

    public static final Map<Integer, Vector2Location> selections = new HashMap<>();
    public static final Map<UUID, BankAccountEntry> bankInterestMap = new HashMap<>();
    public static final Map<Material, DoubleStore> dropMap = new EnumMap<>(Material.class);
    public static final Map<Material, Material> crops = new EnumMap<>(Material.class);
    public static final Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create() ;

    public static final List<GroupEntry> groupEntryList = new ArrayList<>();
    public static final List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
    public static final List<BlockHistoryEntry> blockHistoryList = new ArrayList<>();
    public static final List<ItemStack> bagBlocks2collect = new ArrayList<>();

    public static final String[] ore_respawn = new String[]{PLUGIN_WORLD_WORLD_NETHER}; //TODO has to be done in Config (new Table?) #IsComming
    public static final boolean MONEY_LOST_ON_DEATH = true;
    public static final String[] worlds = new String[]{PLUGIN_WORLD_WORLD, PLUGIN_WORLD_WORLD_NETHER, PLUGIN_WORLD_WORLD_THE_END, PLUGIN_WORLD_LOBBY};
    public final String[][] worldsGroup = new String[][]{new String[]{PLUGIN_WORLD_WORLD, PLUGIN_WORLD_WORLD_NETHER, PLUGIN_WORLD_WORLD_THE_END}, new String[]{PLUGIN_WORLD_LOBBY}};
    
    public static synchronized RelluEssentials getInstance() {
        
        return instance;
    }

    private static synchronized void setInstance(RelluEssentials re){
        instance = re;
    }

    public static void setBanker(Banker banker){
        RelluEssentials.banker = banker;
    }

    public static Banker getBanker(){
        return RelluEssentials.banker;
    }

    @Override
    public void onEnable() {
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
        new BankManager().enable();
        new NPCManager().enable();
        stopLoading();
        new WorldManager().enable();
    }

    @Override
    public void onDisable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_MANAGER_STOP_MESSAGE);
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
        setInstance(this);
        start = Calendar.getInstance().getTimeInMillis();
        consoleSendMessage(PLUGIN_COLOR_COMMAND, PLUGIN_FORMS_BORDER);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "", 2);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_MANAGER_START_MESSAGE);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
    }

    private void stopLoading() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_STARTTIME_MESSAGE, Calendar.getInstance().getTimeInMillis() - start));
        consoleSendMessage(PLUGIN_NAME_CONSOLE, "");
        consoleSendMessage(PLUGIN_COLOR_COMMAND + PLUGIN_FORMS_BORDER, "");
    }

    public RelluEssentials(){
        super();
    }

    protected RelluEssentials(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file){
        super(loader, description, dataFolder, file);
        this.getConfig().set("database.host", "172.17.0.2");
        isUnitTest = true;
    }

    public boolean isUnitTest(){
        return isUnitTest;
    }

    public DatabaseHelper getDatabaseHelper(){
        return dBH;
    }

    public void setDatabaseHelper(DatabaseHelper dBH){
        this.dBH = dBH;
    }
}