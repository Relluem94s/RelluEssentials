package de.relluem94.minecraft.server.spigot.essentials;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_BORDER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_STARTTIME_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_START_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_STOP_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_LOBBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_WORLD_NETHER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_WORLD_THE_END;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import de.relluem94.minecraft.server.spigot.essentials.commands.*;
import de.relluem94.minecraft.server.spigot.essentials.wrapper.CommandWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.relluem94.minecraft.server.spigot.essentials.api.BagAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.BankAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.NPCAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.ProtectionAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.WarpAPI;
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
import de.relluem94.minecraft.server.spigot.essentials.managers.EventManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.GroupManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.NPCManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.RecipeManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SkillManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.managers.WorldManager;
import de.relluem94.minecraft.server.spigot.essentials.npc.Banker;
import de.relluem94.rellulib.stores.DoubleStore;

public class RelluEssentials extends JavaPlugin {

    private long start;
    private static RelluEssentials instance;

    @Getter
    private DatabaseHelper databaseHelper;
    @Setter
    @Getter
    private PluginInformationEntry pluginInformation;

    private static Banker banker;

    @Getter
    private boolean isUnitTest = false;

    @Setter
    @Getter
    private PlayerAPI playerAPI;
    @Setter
    @Getter
    private ProtectionAPI protectionAPI;
    @Setter
    @Getter
    private NPCAPI npcAPI;
    @Setter
    @Getter
    private BagAPI bagAPI;
    @Setter
    @Getter
    private BankAPI bankAPI;
    @Setter
    @Getter
    private WarpAPI warpAPI;

    public final Map<Integer, Vector2Location> selections = new HashMap<>();
    public final Map<UUID, BankAccountEntry> bankInterestMap = new HashMap<>();
    public final Map<Material, DoubleStore<Integer, Integer>> dropMap = new EnumMap<>(Material.class);
    public final Map<Material, Material> crops = new EnumMap<>(Material.class);
    public final Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create() ;

    public final List<GroupEntry> groupEntryList = new ArrayList<>();
    public final List<LocationTypeEntry> locationTypeEntryList = new ArrayList<>();
    public final List<BlockHistoryEntry> blockHistoryList = new ArrayList<>();
    public final List<ItemStack> bagBlocks2collect = new ArrayList<>();

    public final String[] oreRespawn = new String[]{PLUGIN_WORLD_WORLD_NETHER}; //TODO has to be done in Config (new Table?) #IsComming
    public static final boolean MONEY_LOST_ON_DEATH = true;
    public final String[] worlds = new String[]{PLUGIN_WORLD_WORLD, PLUGIN_WORLD_WORLD_NETHER, PLUGIN_WORLD_WORLD_THE_END, PLUGIN_WORLD_LOBBY};

    public static final List<CommandWrapper> commandWrapperList = List.of(
            new CommandWrapper(new Admin()),
            new CommandWrapper(new AFK()),
            new CommandWrapper(new Back()),
            new CommandWrapper(new Bags()),
            new CommandWrapper(new Broadcast()),
            new CommandWrapper(new Cookies()),
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
            new CommandWrapper(new Head())
    );
    
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
        DatabaseManager dm = new DatabaseManager(
            getConfig().getString("database.host"), 
            getConfig().getString("database.user"), 
            getConfig().getString("database.password"), 
            getConfig().getInt("database.port")
        );

        dm.enable();
        databaseHelper = dm.getDatabaseHelper();
        


        new EventManager().enable();
        new SkillManager().enable();
        new RecipeManager().enable();
        new BlockHistoryManager().enable();
        new AutoSaveManager().enable();
        new BankManager().enable();
        new NPCManager().enable();
        stopLoading();
        new WorldManager().enable();
        new GroupManager().enable();
        dm.afterWorldLoaded();
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
        if (language.equals("de_DE")) {
            return ResourceBundle.getBundle("lang", new Locale("de", "DE")).getString(key);
        }
        return ResourceBundle.getBundle("lang", new Locale("en", "US")).getString(key);
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
        isUnitTest = true;
    }
}