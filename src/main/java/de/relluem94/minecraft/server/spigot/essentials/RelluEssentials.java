package de.relluem94.minecraft.server.spigot.essentials;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.Locale;
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
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
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
import de.relluem94.minecraft.server.spigot.essentials.managers.CommandManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.DatabaseManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.EnchantmentManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.EventManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.GroupManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.NPCManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.RecipeManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SkillManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.WorldManager;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

public class RelluEssentials extends JavaPlugin {

    // TODO add Animal Protection (horse cat dog?)
    // TODO add /trade to trade with player (coins and items)
    // TODO change /tp to request teleport (if not mod) 
    // TODO add Marriage
    // TODO Fix Command execution for command Blocks

    public static ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
    public static Scoreboard board;
    public static File dataFolder;
    public static DatabaseHelper dBH;
    public static PluginInformationEntry pie;
    private static long start;
    private static RelluEssentials instance;
    private static ResourceBundle englishProperties;
    private static ResourceBundle germanProperties;
    public static String language;
    public static Banker banker;

    public static HashMap<User, Vector2Location> selections = new HashMap<User, Vector2Location>();
    public static HashMap<UUID, PlayerEntry> sudoers = new HashMap<UUID, PlayerEntry>();
    public static HashMap<UUID, BankAccountEntry> bankInterestMap = new HashMap<UUID, BankAccountEntry>();
    public static HashMap<Material, DoubleStore> dropMap = new HashMap<Material, DoubleStore>(){
        {
        put(Material.DIAMOND, new DoubleStore(1, 2));
        put(Material.EMERALD, new DoubleStore(1, 2));
        put(Material.RAW_IRON, new DoubleStore(1, 3));
        put(Material.RAW_COPPER, new DoubleStore(2, 5));
        put(Material.RAW_GOLD, new DoubleStore(1, 2));
        put(Material.LAPIS_LAZULI, new DoubleStore(2, 5));
        put(Material.COAL, new DoubleStore(1, 3));
        put(Material.QUARTZ, new DoubleStore(1, 3));
        } // TODO into DB
    };
    public static HashMap<Material, Material> crops = new HashMap<Material, Material>() {
        {
        put(Material.CARROT, Material.CARROTS);
        put(Material.NETHER_WART, Material.NETHER_WART);
        put(Material.POTATO, Material.POTATOES);
        put(Material.WHEAT_SEEDS, Material.WHEAT);
        put(Material.BEETROOT_SEEDS, Material.BEETROOTS);
        put(Material.COCOA_BEANS, Material.COCOA);
        } // TODO into DB
    };
    public static Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create() ;

    public static List<User> users = new ArrayList<User>();
    public static List<LocationEntry> locationEntryList = new ArrayList<>();
    public static List<GroupEntry> groupEntryList = new ArrayList<>();
    public static List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
    public static List<BlockHistoryEntry> blockHistoryList = new ArrayList<>();
    public static List<ItemStack> bagBlocks2collect = new ArrayList<>();

    //TODO has to be done in Config (new Table?) #IsComming

    public static String[] ore_respawn = new String[]{"world_nether", "123_nether"};
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
        dataFolder = this.getDataFolder();

        try {
            configManager(true);
            loadLanguage();
        } catch (IOException ex) {
            Logger.getLogger(RelluEssentials.class.getName()).log(Level.SEVERE, null, ex);
        }

        new ScoreBoardManager().manage();
        new CommandManager().manage();
        new DatabaseManager().manage();
        new EnchantmentManager().manage();
        new GroupManager().manage();
        new EventManager().manage();
        new SkillManager().manage();
        new RecipeManager().manage();
        new BlockHistoryManager().manage();
        new AutoSaveManager().manage();
        new NPCManager().manage();
        new WorldManager().manage();
        stopLoading();
    }

    @Override
    public void onDisable() {
        ChatHelper.consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_STOP_MESSAGE);

        for(UUID uuid: sudoers.keySet()){
            Sudo.exitSudo(Bukkit.getPlayer(uuid));
        }

        BagHelper.saveBags();
    
        for (WorldGroupEntry wge : worldsMap.keySet()) {
            for(WorldEntry we: worldsMap.get(wge)){
                try{
                    WorldHelper.unloadWorld(we.getName(), true);
                }
                catch(WorldNotLoadedException e){
                    System.out.println(e.getMessage());
                }
            }
        }
            
        
        locationEntryList.clear();
        groupEntryList.clear();
        locationTypeEntryList.clear();
        blockHistoryList.clear();
        selections.clear();
        
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
}
