package de.relluem94.minecraft.server.spigot.essentials;

public class Strings {

    /**
     * ****************************************************************************
     */
    /*                             PLUGIN   STUFF                                  */
    /**
     * ****************************************************************************
     */
    public static final String PLUGIN_NAME = "RelluEssentials";
    public static final String PLUGIN_NAME_CONSOLE = "[" + PLUGIN_NAME + "] ";
    public static final String PLUGIN_PREFIX = "§o§l§4§8Rellu§cEssentials§r§f";
    public static final String PLUGIN_SPACER = "§7 >> §f";
    public static final String PLUGIN_SPACER_CHANNEL = " >> §f";
    public static final String PLUGIN_COMMAND_COLOR = "§f";
    public static final String PLUGIN_COMMAND_NAME_COLOR = "§b";
    public static final String PLUGIN_COMMAND_ARG_COLOR = "§b";
    public static final String PLUGIN_MESSAGE_COLOR = "§f";
    public static final String PLUGIN_START_MESSAGE = "starts configuring ...";
    public static final String PLUGIN_STOP_MESSAGE = "shutdown();";
    public static final String PLUGIN_BROADCAST_NAME = "§5Broadcast";

    public static final String PLUGIN_REGISTER_ENCHANTMENT = "Registered enchantment %s with id %s!";

    /**
     * ****************************************************************************
     */
    /*                              ITEM   STUFF                                   */
    /**
     * ****************************************************************************
     */
    public static final String PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS = "cloud_boots";
    public static final String PLUGIN_ITEM_NAMESPACE_SMELTER_PICKAXE = "smelter_pickaxe";
    public static final String PLUGIN_ITEM_NAMESPACE_SMELTER_TANK = "smelter_tank";
    public static final String PLUGIN_ITEM_NAMESPACE_SMELTER_FURNACE = "smelter_furnace";
    public static final String PLUGIN_ITEM_NAMESPACE_SMELTER_FUEL = "smelter_fuel";

    public static final String PLUGIN_ITEM_RELLU_HELMET = "§6Rellu's Helmet";
    public static final String PLUGIN_ITEM_RELLU_CHESTPLATE = "§3Rellu's Chestplate";
    public static final String PLUGIN_ITEM_RELLU_LEGGINGS = "§aRellu's Leggings";
    public static final String PLUGIN_ITEM_RELLU_BOOTS = "§cRellu's Boots";
    public static final String PLUGIN_ITEM_RELLU_SWORD = "§eRellu's Sword";
    public static final String PLUGIN_ITEM_RELLU_SHIELD = "§5Rellu's Shield";

    public static final String PLUGIN_ITEM_INGREDIENT = "§4§l§oThis is a crafting ingredient";

    public static final String PLUGIN_ITEM_AUTOSMELTER = "§cSmelter Pickaxe";
    public static final String PLUGIN_ITEM_AUTOSMELTER_TANK = "§cSmelter Tank";
    public static final String PLUGIN_ITEM_AUTOSMELTER_FURNACE = "§cSmelter Furnace";

    public static final String PLUGIN_ITEM_CLOUDBOOTS = "§bCloud Boots";
    public static final String PLUGIN_ITEM_CLOUDBOOTS_LORE1 = "§bGrants gliding abillity if worn.";
    public static final String PLUGIN_ITEM_CLOUDBOOTS_LORE2 = "§bAlso reduces Fall Damage by 100%";
    public static final String PLUGIN_ITEM_CLOUDSAILOR = "§bCloud Sailor";
    public static final String PLUGIN_ITEM_CLOUDSAILOR_LORE1 = "§bHeld in Off Hand grants gliding abillity.";
    public static final String PLUGIN_ITEM_CLOUDSAILOR_LORE2 = "§bAlso reduces Fall Damage by 50%";

    /**
     * ****************************************************************************
     */
    /*                          ENCHANTMENT   STUFF                                */
    /**
     * ****************************************************************************
     */
    public static final String PLUGIN_ENCHANTMENT_COLOR = "§8";
    public static final String PLUGIN_ENCHANTMENT_LORE_COLOR = "§7§o  ";

    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT = "Autosmelt";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK = PLUGIN_ENCHANTMENT_LORE_COLOR + "Your Lava Tank is filled: ";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR = "§f/§2";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LOW_COLOR = "§6";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR = "§2";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_COLOR = "§4";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_MESSAGE = "§4Your Lava Tank is empty. You can't smelt any longer";
    public static final int PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX = 1000;
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT = "%s" + PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR + PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX;
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Smelts Ores and Blocks if mined with this Tool";
    public static final String PLUGIN_ENCHANTMENT_TELEKENESIS = "Telekenesis";
    public static final String PLUGIN_ENCHANTMENT_TELEKENESIS_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "All drops from Blocks broken and Mobs killed are teleported directly into your Inventory";

    /**
     * ****************************************************************************
     */
    /*                           DATABASE   STUFF                                  */
    /**
     * ****************************************************************************
     */
    public static final String PLUGIN_DATABASE_VERSION = "";
    public static final String PLUGIN_DATABASE_NAME = "rellu_essentials";

    /**
     * ****************************************************************************
     */
    /*                            COMMAND   STUFF                                  */
    /**
     * ****************************************************************************
     */
    
    public static final String PLUGIN_COMMAND_NAME_AFK = "afk";
    public static final String PLUGIN_COMMAND_NAME_BROADCAST = "broadcast";
    public static final String PLUGIN_COMMAND_NAME_BROADCAST_TITLE = "title";
    public static final String PLUGIN_COMMAND_NAME_CLEARCHAT = "cc";
    public static final String PLUGIN_COMMAND_NAME_COOCKIE = "cookie";
    public static final String PLUGIN_COMMAND_NAME_CRAFT = "craft";
    public static final String PLUGIN_COMMAND_NAME_DAY = "day";
    public static final String PLUGIN_COMMAND_NAME_ENDERCHEST = "enderchest";
    public static final String PLUGIN_COMMAND_NAME_FLY = "fly";
    public static final String PLUGIN_COMMAND_NAME_GAMEMODE_0 = "0";
    public static final String PLUGIN_COMMAND_NAME_GAMEMODE_0_NAME = "Survival";
    public static final String PLUGIN_COMMAND_NAME_GAMEMODE_1 = "1";
    public static final String PLUGIN_COMMAND_NAME_GAMEMODE_1_NAME = "Creative";
    public static final String PLUGIN_COMMAND_NAME_GAMEMODE_2 = "2";    
    public static final String PLUGIN_COMMAND_NAME_GAMEMODE_2_NAME = "Adventure";    
    public static final String PLUGIN_COMMAND_NAME_GAMEMODE_3 = "3";
    public static final String PLUGIN_COMMAND_NAME_GAMEMODE_3_NAME = "Spectator";
    public static final String PLUGIN_COMMAND_NAME_GOD = "god";
    public static final String PLUGIN_COMMAND_NAME_HEAD = "head";        
    public static final String PLUGIN_COMMAND_NAME_HEAL = "heal";        
    public static final String PLUGIN_COMMAND_NAME_HOME = "home"; 
    public static final String PLUGIN_COMMAND_NAME_HOME_SET = "set"; 
    public static final String PLUGIN_COMMAND_NAME_HOME_DELETE = "delete";
    public static final String PLUGIN_COMMAND_NAME_HOME_LIST = "list";
    public static final String PLUGIN_COMMAND_NAME_INVENTORY = "inv";        
    public static final String PLUGIN_COMMAND_NAME_MSG = "msg";        
    public static final String PLUGIN_COMMAND_NAME_REPLY = "r";        
    public static final String PLUGIN_COMMAND_NAME_MORE = "more";        
    public static final String PLUGIN_COMMAND_NAME_NICK = "nick";        
    public static final String PLUGIN_COMMAND_NAME_NIGHT = "night";        
    public static final String PLUGIN_COMMAND_NAME_SETGROUP = "setGroup";
    public static final String PLUGIN_COMMAND_NAME_POKE = "poke";
    public static final String PLUGIN_COMMAND_NAME_PRINT = "print";
    public static final String PLUGIN_COMMAND_NAME_RAIN = "rain";
    public static final String PLUGIN_COMMAND_NAME_RELLU = "rellu";
    public static final String PLUGIN_COMMAND_NAME_RELLU_PING = "ping";
    public static final String PLUGIN_COMMAND_NAME_RENAME = "rename";
    public static final String PLUGIN_COMMAND_NAME_REPAIR = "repair";
    public static final String PLUGIN_COMMAND_NAME_ROLLBACK = "rollback";
    public static final String PLUGIN_COMMAND_NAME_ROLLBACK_PLAYER = "player";
    public static final String PLUGIN_COMMAND_NAME_ROLLBACK_UNDO = "undo";
    public static final String PLUGIN_COMMAND_NAME_ROLLBACK_UNDO_PLAYER = "player";
    public static final String PLUGIN_COMMAND_NAME_SPAWN = "spawn";
    public static final String PLUGIN_COMMAND_NAME_SPEED = "speed";
    public static final String PLUGIN_COMMAND_NAME_STORM = "storm";
    public static final String PLUGIN_COMMAND_NAME_SUICIDE = "suicide";
    public static final String PLUGIN_COMMAND_NAME_SUN = "sun";
    public static final String PLUGIN_COMMAND_NAME_TEST_COMMAND = "ZAQmNCRXEdwSGU7DvEcXTbBkp2qEaCSSNkQcMhL3m7KSDtmXWaxtbYCaQCFBR96fj";
    public static final String PLUGIN_COMMAND_NAME_TEST_COMMAND_CUSTOMMOB = "cm";
    public static final String PLUGIN_COMMAND_NAME_TEST_COMMAND_CLOUDSAILOR = "cs";
    public static final String PLUGIN_COMMAND_NAME_TEST_COMMAND_RELLU = "rellu";
    public static final String PLUGIN_COMMAND_NAME_TEST_COMMAND_SMELT = "smelt";
    public static final String PLUGIN_COMMAND_NAME_TEST_COMMAND_TELE = "tele";
    public static final String PLUGIN_COMMAND_NAME_TEST_COMMAND_NOENCHANT = "noenchant";
    public static final String PLUGIN_COMMAND_NAME_TEST_COMMAND_WORLDS = "worlds";
    public static final String PLUGIN_COMMAND_NAME_TITLE = "title";
    public static final String PLUGIN_COMMAND_NAME_VANISH = "vanish";
    public static final String PLUGIN_COMMAND_NAME_WHERE = "where";
            
    public static final String PLUGIN_COMMAND_GAMEMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Gamemode von %s" + PLUGIN_COMMAND_COLOR + " wurde zu " + PLUGIN_COMMAND_NAME_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " geändert!";

    public static final String PLUGIN_COMMAND_AFK = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " ist jetzt %s";
    public static final String PLUGIN_COMMAND_AFK_ACTIVATED = "§cafk";
    public static final String PLUGIN_COMMAND_AFK_DEACTIVATED = "§awieder da";

    public static final String PLUGIN_COMMAND_FLYMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Flugmodus von %s" + PLUGIN_COMMAND_COLOR + " wurde " + PLUGIN_COMMAND_ARG_COLOR + "%s!";
    public static final String PLUGIN_COMMAND_FLYMODE_ACTIVATED = "aktiviert";
    public static final String PLUGIN_COMMAND_FLYMODE_DEACTIVATED = "deaktiviert";

    public static final String PLUGIN_COMMAND_RELLU_PING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast ein Ping von " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "ms!";
    public static final String PLUGIN_COMMAND_RELLU_PING_OTHER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " hast ein Ping von " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "ms!";
    public static final String PLUGIN_COMMAND_RELLU_WRONG_COMMAND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es wurde kein passender Subbefehl gefunden!";
    public static final String PLUGIN_COMMAND_RELLU_OPTIONS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze: " + PLUGIN_COMMAND_ARG_COLOR + "ping";

    public static final String PLUGIN_COMMAND_CRAFTINGBENCH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Die Werkbank von %s" + PLUGIN_COMMAND_COLOR + " wurde geöffnet!";

    public static final String PLUGIN_COMMAND_COOKIES = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Ein Keks für dich, %s";
    public static final String PLUGIN_COMMAND_COOKIES_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast %s " + PLUGIN_COMMAND_COLOR + "ein Keks geschenkt!";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_1 = PLUGIN_COMMAND_COLOR + "Mit viel Liebe gebacken von %s";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_2 = PLUGIN_COMMAND_COLOR + "Backe doch auch einen Keks!";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_3 = PLUGIN_COMMAND_COLOR + "Wie selbstverliebt!";
    public static final String PLUGIN_COMMAND_COOKIES_DISPLAYNAME = PLUGIN_COMMAND_COLOR + "Schokoladenkeks";

    public static final String PLUGIN_COMMAND_HEAD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast ein Kopf von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " erzeugt!";
    public static final String PLUGIN_COMMAND_VANISH_DISABLE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast vanish für " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "deaktiviert!";
    public static final String PLUGIN_COMMAND_VANISH_ENABLE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast vanish für " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "aktiviert!";
    public static final String PLUGIN_COMMAND_VANISH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Dein vanish wurde umgeschaltet!";

    public static final String PLUGIN_COMMAND_MORE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast mehr von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " erzeugt!";
    public static final String PLUGIN_COMMAND_MORE_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast mehr von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " erhalten!";

    public static final String PLUGIN_COMMAND_REPAIR = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " repariert!";
    public static final String PLUGIN_COMMAND_REPAIR_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " repariert bekommen!";

    public static final String PLUGIN_COMMAND_HOME = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "an den Bett Spawn teleportiert!";
    public static final String PLUGIN_COMMAND_HOME_TP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest zu " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "teleportiert!";
    public static final String PLUGIN_COMMAND_HOME_NONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast kein Home!";
    public static final String PLUGIN_COMMAND_HOME_NOT_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast kein Home mit dem Namen " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "!";
    public static final String PLUGIN_COMMAND_HOME_EXISTS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast bereits ein Home mit dem Namen " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "!";
    public static final String PLUGIN_COMMAND_HOME_RESERVED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du kannst kein Home mit dem Namen " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " speichern!";
    public static final String PLUGIN_COMMAND_HOME_NO_BED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast kein Bett in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " an das du teleportiert werden kannst!";
    public static final String PLUGIN_COMMAND_HOME_LIST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast folgende Homes:";
    public static final String PLUGIN_COMMAND_HOME_SET = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dein Home " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "gesetzt!";
    public static final String PLUGIN_COMMAND_HOME_DELETE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dein Home " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "gelöscht!";

    public static final String PLUGIN_COMMAND_SUN = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du lässt die Sonne in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "scheinen!";
    public static final String PLUGIN_COMMAND_SUN_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Die Sonne die Sonne scheint nur für dich!";

    public static final String PLUGIN_COMMAND_RAIN = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du lässt es in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " regnen!";
    public static final String PLUGIN_COMMAND_RAIN_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es regnet nur für dich!";

    public static final String PLUGIN_COMMAND_STORM = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du lässt es in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " gewittern!";

    public static final String PLUGIN_COMMAND_DAY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es ist jetzt Tag in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "!";

    public static final String PLUGIN_COMMAND_GOD_ON = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du bist der Gott!";
    public static final String PLUGIN_COMMAND_GOD_OFF = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du bist wieder sterblich!";

    public static final String PLUGIN_COMMAND_HEAL = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dich geheilt!";

    public static final String PLUGIN_COMMAND_NIGHT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es ist jetzt Nacht in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "!";

    public static final String PLUGIN_COMMAND_ENDERCHEST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast deine Enderchest geöffnet!";
    public static final String PLUGIN_COMMAND_ENDERCHEST_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast die Enderchest von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "geöffnet!";

    public static final String PLUGIN_COMMAND_INVENTORY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dein Inventar geöffnet!";
    public static final String PLUGIN_COMMAND_INVENTORY_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast das Inventar von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "geöffnet!";

    public static final String PLUGIN_COMMAND_SPAWN = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "an den Spawn teleportiert!";

    public static final String PLUGIN_COMMAND_NICK = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast den Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "umbenannt!";

    public static final String PLUGIN_COMMAND_SUICIDE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "hat sich umgebracht!";

    public static final String PLUGIN_COMMAND_WHERE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "befindet sich bei " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "!";
    public static final String PLUGIN_COMMAND_WHERE_STRING = PLUGIN_COMMAND_COLOR + "X: " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "Y: " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "Z: " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "Welt: " + PLUGIN_COMMAND_ARG_COLOR + "%s";

    public static final String PLUGIN_COMMAND_MSG_PLAYER_OFFLINE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Spieler ist Offline!";
    public static final String PLUGIN_COMMAND_MSG_NO_ONE_TO_REPLY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Niemand da der dir antworten könnte!";
    public static final String PLUGIN_COMMAND_MSG_SPACER_IN = "§9 >> §f";
    public static final String PLUGIN_COMMAND_MSG_SPACER_OUT = "§9 << §f";
    public static final String PLUGIN_COMMAND_MSG_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze " + PLUGIN_COMMAND_ARG_COLOR + "/msg " + PLUGIN_COMMAND_COLOR + "<" + PLUGIN_COMMAND_ARG_COLOR + "spieler" + PLUGIN_COMMAND_COLOR + ">" + " " + "<" + PLUGIN_COMMAND_ARG_COLOR + "nachricht" + PLUGIN_COMMAND_COLOR + ">";

    public static final String PLUGIN_COMMAND_PRINT_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze " + PLUGIN_COMMAND_ARG_COLOR + "/print " + "<" + PLUGIN_COMMAND_ARG_COLOR + "nachricht" + PLUGIN_COMMAND_COLOR + ">";

    public static final String PLUGIN_COMMAND_RENAME_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze " + PLUGIN_COMMAND_ARG_COLOR + "/rename " + "<" + PLUGIN_COMMAND_ARG_COLOR + "name" + PLUGIN_COMMAND_COLOR + ">";
    public static final String PLUGIN_COMMAND_RENAME_AIR = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du kannst Luft nicht umbenennen! ";
    public static final String PLUGIN_COMMAND_RENAME = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dein Gegenstand umbenannt!";

    public static final String PLUGIN_COMMAND_POKE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze /poke " + PLUGIN_COMMAND_ARG_COLOR + "<Name>" + PLUGIN_COMMAND_COLOR + " um einen Spieler anzustubsen";
    public static final String PLUGIN_COMMAND_POKE_TITLE = "§4Buuuh";
    public static final String PLUGIN_COMMAND_POKE_SUBTITLE = "~~~~~~~~~~~~~~";
    public static final String PLUGIN_COMMAND_POKE_MESSAGE_TARGET = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "hat dich angestupst!";
    public static final String PLUGIN_COMMAND_POKE_MESSAGE_SENDER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + "%s " + PLUGIN_COMMAND_COLOR + "angestupst!";
    
    
    public static final String PLUGIN_COMMAND_SPEED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Geschwindigkeit wurde auf " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " gesetzt";
    public static final String PLUGIN_COMMAND_SPEED_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze " + PLUGIN_COMMAND_ARG_COLOR + "/speed " + "<" + PLUGIN_COMMAND_ARG_COLOR + "0-10" + PLUGIN_COMMAND_COLOR + ">";

    public static final String PLUGIN_COMMAND_BROADCAST_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Um ein Title Broadcast zu machen nutze " + PLUGIN_COMMAND_ARG_COLOR + "/broadcast title " + PLUGIN_COMMAND_COLOR + "sonst " + PLUGIN_COMMAND_ARG_COLOR + "/broadcast " + PLUGIN_COMMAND_COLOR + "<" + PLUGIN_COMMAND_ARG_COLOR + "message" + PLUGIN_COMMAND_COLOR + ">";

    public static final String PLUGIN_COMMAND_PERMISSION_MISSING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Dafür hast du leider keine Rechte!";
    public static final String PLUGIN_COMMAND_NOT_A_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du bist leider kein Spieler!";
    public static final String PLUGIN_COMMAND_TO_LESS_ARGUMENTS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Zu wenig Argumente!";

    /**
     * ****************************************************************************
     */
    /*                             EVENT   STUFF                                   */
    /**
     * ****************************************************************************
     */
    public static final String PLUGIN_EVENT_JOIN_MESSAGE = "§2[\u2726] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server betreten.";
    public static final String PLUGIN_EVENT_QUIT_MESSAGE = "§4[\u274C] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server verlassen.";
    public static final String PLUGIN_EVENT_DEATH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du starbst bei " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + PLUGIN_COMMAND_WHERE_STRING + PLUGIN_COMMAND_COLOR;
    public static final String PLUGIN_EVENT_DEATH_TP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Klicke diese Nachricht um dich zum Todespunkt zu teleportieren!";

    public static final String PLUGIN_EVENT_SKILL_REPAIR_DONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast den Gegenstand repariert!";
    public static final String PLUGIN_EVENT_SKILL_REPAIR_WARNING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du benötigst mehr" + PLUGIN_COMMAND_ARG_COLOR + " %s " + PLUGIN_COMMAND_COLOR + "um diesen Gegenstand zu reparieren!";
    public static final String PLUGIN_EVENT_SKILL_SALVAGE_DONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast" + PLUGIN_COMMAND_ARG_COLOR + " %s " + PLUGIN_COMMAND_COLOR + "erhalten!";

    //TODO Have to do a Language Pack to load german and english translations via config. Maybe default en and user customizable
}
