package de.relluem94.minecraft.server.spigot.essentials;

import de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants;

public class Strings {

    private Strings() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_CLASS_PRIVATE_CONSTRUCTOR);
    }

    /**
     * *****************************************************************************
     */
    /*                             PLUGIN   STUFF                                   */
    /**
     * *****************************************************************************
     */
    public static final String PLUGIN_NAME = "RelluEssentials";
    public static final String PLUGIN_SPACER_CHANNEL = " >> §f";
    public static final String PLUGIN_COMMAND_COLOR = "§f";
    public static final String PLUGIN_COMMAND_NAME_COLOR = "§b";
    public static final String PLUGIN_COMMAND_ARG_COLOR = "§b";
    public static final String PLUGIN_MESSAGE_COLOR = "§f";
    public static final String PLUGIN_PREFIX = "§o§l§4§8Rellu§cEssentials§r" + PLUGIN_MESSAGE_COLOR;
    public static final String PLUGIN_NAME_SIGN = PLUGIN_MESSAGE_COLOR + "[" + "§8R§cE" + PLUGIN_MESSAGE_COLOR + "]";
    public static final String PLUGIN_CLICK_SIGN = PLUGIN_MESSAGE_COLOR + "[Click here]";
    public static final String PLUGIN_NAME_CONSOLE = PLUGIN_MESSAGE_COLOR + "[" + PLUGIN_PREFIX + PLUGIN_MESSAGE_COLOR + "] ";
    public static final String PLUGIN_SPACER = "§7 >> " + PLUGIN_MESSAGE_COLOR;
    public static final String PLUGIN_COMMAND_PREFIX = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR;
    public static final String PLUGIN_BORDER = "<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>";
    public static final String PLUGIN_BORDER_SHORT = "<><><><><><><><><><><><><><><><><><>";
    public static final String PLUGIN_WHITE_SPACE = "               ";
    public static final String PLUGIN_WHITE_SPACE_SHORT = "    ";
    public static final String PLUGIN_START_MESSAGE = "starts configuring ...";
    public static final String PLUGIN_STOP_MESSAGE = "shutdown();";
    public static final String PLUGIN_STARTTIME = "wurde in %s ms gestartet!";
    public static final String PLUGIN_BROADCAST_NAME = "§5Broadcast";
    public static final String PLUGIN_EOL = System.getProperty("line.separator");
    public static final String PLUGIN_INTERNAL_CLASS_PRIVATE_CONSTRUCTOR = "This is a Utility Class";

    public static final String LANG_REGISTER_SKILLS = "Registriere Skills!";
    public static final String LANG_SKILLS_REGISTERED = "Skills erfolgreich registriert!";
    public static final String LANG_LOADING_CONFIGS = "Configs werden geladen!";
    public static final String LANG_CONFIGS_LOADED = "Configs sind geladen!";
    public static final String LANG_REGISTER_EVENTS = "Registriere Events!";
    public static final String LANG_EVENTS_REGISTERED = "%s Events erfolgreich registriert!";
    public static final String LANG_REGISTER_RECIPE = "Registriere Rezepte!";
    public static final String LANG_RECIPE_REGISTERED = "%s Rezepte erfolgreich registriert!";
    public static final String LANG_REGISTER_COMMANDS = "Registriere Befehle!";
    public static final String LANG_COMMANDS_REGISTERED = "%s Befehle erfolgreich registriert!";
    public static final String LANG_REGISTER_AUTOSAVE = "Registriere Auto Save!";
    public static final String LANG_AUTOSAVE_REGISTERED = "Auto Save erfolgreich registriert!";
    public static final String LANG_REGISTER_ENCHANTMENTS = "Registriere Enchantments!";
    public static final String LANG_ENCHANTMENTS_REGISTERED = "%s Enchantments erfolgreich registriert!";

    public static final String PLUGIN_REGISTER_ENCHANTMENT = "Registered enchantment %s with id %s!";

    public static final String PLUGIN_CONSOLE_COLOR = "§c";
    public static final String PLUGIN_COMMAND_BLOCK_COLOR = "§8";
    public static final String PLUGIN_CONSOLE_NAME = PLUGIN_CONSOLE_COLOR + "Console";
    public static final String PLUGIN_MONEY_COLOR = "§6"; 
    public static final String PLUGIN_MONEY_NAME = PLUGIN_MONEY_COLOR + "Coins" + PLUGIN_MESSAGE_COLOR; 

    /**
     * *****************************************************************************
     */
    /*                            COMMAND   STUFF                                   */
    /**
     * *****************************************************************************
     */
    public static final String PLUGIN_COMMAND_FLYMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Flugmodus von %s" + PLUGIN_COMMAND_COLOR + " wurde " + PLUGIN_COMMAND_ARG_COLOR + "%s!";
    public static final String PLUGIN_COMMAND_FLYMODE_ACTIVATED = "aktiviert";
    public static final String PLUGIN_COMMAND_FLYMODE_DEACTIVATED = "deaktiviert";

    public static final String PLUGIN_COMMAND_ADMIN_PING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast ein Ping von " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "ms!";
    public static final String PLUGIN_COMMAND_ADMIN_PING_OTHER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " hast ein Ping von " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "ms!";
    public static final String PLUGIN_COMMAND_ADMIN_PING_OTHER_NOT_FOUND  = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "wurde nicht gefunden";

    public static final String PLUGIN_COMMAND_CRAFTINGBENCH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Die Werkbank von %s" + PLUGIN_COMMAND_COLOR + " wurde geöffnet!";

    public static final String PLUGIN_COMMAND_COOKIES = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Ein Keks für dich, %s";
    public static final String PLUGIN_COMMAND_COOKIES_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast %s " + PLUGIN_COMMAND_COLOR + "ein Keks geschenkt!";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_1 = PLUGIN_COMMAND_COLOR + "Mit viel Liebe gebacken von %s";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_2 = PLUGIN_COMMAND_COLOR + "Backe doch auch einen Keks!";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_3 = PLUGIN_COMMAND_COLOR + "Wie selbstverliebt!";
    public static final String PLUGIN_COMMAND_COOKIES_DISPLAYNAME = PLUGIN_COMMAND_COLOR + "Schokoladenkeks";

    public static final String PLUGIN_COMMAND_HEAD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast ein Kopf von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " erzeugt!";
    public static final String PLUGIN_COMMAND_HEAD_NOT_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es konnte kei Kopf erzeugt werden, Spieler nicht gefunden";
    public static final String PLUGIN_COMMAND_VANISH_DISABLE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast vanish für " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "deaktiviert!";
    public static final String PLUGIN_COMMAND_VANISH_ENABLE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast vanish für " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "aktiviert!";
    public static final String PLUGIN_COMMAND_VANISH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Dein vanish wurde umgeschaltet!";

    public static final String PLUGIN_COMMAND_MORE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast mehr von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " erzeugt!";
    public static final String PLUGIN_COMMAND_MORE_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast mehr von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " erhalten!";

    public static final String PLUGIN_COMMAND_REPAIR = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " repariert!";
    public static final String PLUGIN_COMMAND_REPAIR_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " repariert bekommen!";

    public static final String PLUGIN_COMMAND_GAMERULES = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Die Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "hat folgende Einstellungen:";

    public static final String PLUGIN_COMMAND_HOME = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "an den Bett Spawn teleportiert!";
    public static final String PLUGIN_COMMAND_HOME_TP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest zu " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "teleportiert!";
    public static final String PLUGIN_COMMAND_HOME_NONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast kein Home!";

    public static final String PLUGIN_COMMAND_HOME_LIST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast folgende Homes:";
    public static final String PLUGIN_COMMAND_HOME_LIST_NAME =  PLUGIN_COMMAND_COLOR + "Name: " + PLUGIN_COMMAND_ARG_COLOR + "%s" + " §7(" + PLUGIN_COMMAND_ARG_COLOR + "%s§7)";
    public static final String PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast folgende Todespunkte:";
    public static final String PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS_NAME =  PLUGIN_COMMAND_COLOR + "Todespunkt: " + PLUGIN_COMMAND_ARG_COLOR + "%s" + " §7(" + PLUGIN_COMMAND_ARG_COLOR + "%s§7)";

    public static final String PLUGIN_COMMAND_HOME_NOT_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast kein Home mit dem Namen " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "!";
    public static final String PLUGIN_COMMAND_HOME_EXISTS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast bereits ein Home mit dem Namen " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "!";
    public static final String PLUGIN_COMMAND_HOME_RESERVED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du kannst kein Home mit dem Namen " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " speichern!";
    public static final String PLUGIN_COMMAND_HOME_NO_BED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast kein Bett in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " an das du teleportiert werden kannst!";
    public static final String PLUGIN_COMMAND_HOME_SET = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dein Home " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "gesetzt!";
    public static final String PLUGIN_COMMAND_HOME_DELETE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dein Home " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "gelöscht!";
    public static final String PLUGIN_COMMAND_HOME_DEATH_DELETE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dein Todespunkt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "gelöscht!";

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

    public static final String PLUGIN_COMMAND_WORLD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Folgende Welten gibt es:";
    public static final String PLUGIN_COMMAND_WORLD_NOT_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Die Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " existiert nicht";

    public static final String PLUGIN_COMMAND_POKE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze /poke " + PLUGIN_COMMAND_ARG_COLOR + "<Name>" + PLUGIN_COMMAND_COLOR + " um einen Spieler anzustubsen";
    public static final String PLUGIN_COMMAND_POKE_TITLE = "§4Buuuh";
    public static final String PLUGIN_COMMAND_POKE_SUBTITLE = "~~~~~~~~~~~~~~";
    public static final String PLUGIN_COMMAND_POKE_MESSAGE_TARGET = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "hat dich angestupst!";
    public static final String PLUGIN_COMMAND_POKE_MESSAGE_SENDER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + "%s " + PLUGIN_COMMAND_COLOR + "angestupst!";

    public static final String PLUGIN_COMMAND_SPEED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Geschwindigkeit wurde auf " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " gesetzt";
    public static final String PLUGIN_COMMAND_SPEED_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze " + PLUGIN_COMMAND_ARG_COLOR + "/speed " + "<" + PLUGIN_COMMAND_ARG_COLOR + "0-10" + PLUGIN_COMMAND_COLOR + ">";

    public static final String PLUGIN_COMMAND_BROADCAST_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Um ein Title Broadcast zu machen nutze " + PLUGIN_COMMAND_ARG_COLOR + "/broadcast title " + PLUGIN_COMMAND_COLOR + "sonst " + PLUGIN_COMMAND_ARG_COLOR + "/broadcast " + PLUGIN_COMMAND_COLOR + "<" + PLUGIN_COMMAND_ARG_COLOR + "message" + PLUGIN_COMMAND_COLOR + ">";

    public static final String PLUGIN_COMMAND_INVALID = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Invalid Data!";
    public static final String PLUGIN_COMMAND_PERMISSION_MISSING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Dafür hast du leider keine Rechte!";
    public static final String PLUGIN_COMMAND_NOT_A_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du bist leider kein Spieler!";
    public static final String PLUGIN_COMMAND_TO_LESS_ARGUMENTS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Zu wenig Argumente!";
    public static final String PLUGIN_COMMAND_TO_MANY_ARGUMENTS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Zu viele Argumente!";
    public static final String PLUGIN_COMMAND_TARGET_NOT_A_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " ist kein Spieler!";

    public static final String PLUGIN_COMMAND_PURSE_GAIN = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You gained " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " and now have " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " in your Purse!";
    public static final String PLUGIN_COMMAND_PURSE_TO_ITEM = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You put out " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_COMMAND_PURSE_TO_ITEM_VALUE_TO_HIGH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "The value you entered is to high!";
    public static final String PLUGIN_COMMAND_PURSE_TO_ITEM_NOT_ENOUGH_MONEY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Not enough " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_COMMAND_PURSE_TOTAL = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You have " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " in your Purse!";
    public static final String PLUGIN_COMMAND_PURSE_TOTAL_OTHER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " has " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " in the Purse!";

    public static final String PLUGIN_COMMAND_PROTECT_COMMAND_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Use " + PLUGIN_COMMAND_NAME_COLOR + "/" + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT + " " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_ADD + PLUGIN_MESSAGE_COLOR + ", " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_REMOVE + PLUGIN_MESSAGE_COLOR + " or " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_FLAG + PLUGIN_MESSAGE_COLOR + ", " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_RIGHT;
    public static final String PLUGIN_COMMAND_PROTECT_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click a Protected Block to view the Protection Info";
    public static final String PLUGIN_COMMAND_PROTECT_ADD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click Protectable Block to create a Protection";
    public static final String PLUGIN_COMMAND_PROTECT_REMOVE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click your Protected Block to remove the Protection";
    public static final String PLUGIN_COMMAND_PROTECT_FLAG = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Use " + PLUGIN_COMMAND_NAME_COLOR + "/" + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT + " " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_FLAG + " " + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_FLAG_ADD + PLUGIN_MESSAGE_COLOR + " or " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_FLAG_REMOVE + " flagname";
    public static final String PLUGIN_COMMAND_PROTECT_FLAG_NOT_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Flag was not found!";
    public static final String PLUGIN_COMMAND_PROTECT_RIGHT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Use " + PLUGIN_COMMAND_NAME_COLOR + "/" + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT + " " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_RIGHT + " " + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_RIGHT_ADD + PLUGIN_MESSAGE_COLOR + " or " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_RIGHT_REMOVE + " playername";
    public static final String PLUGIN_COMMAND_PROTECT_FLAG_ADD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click your Protected Block to add the Flag";
    public static final String PLUGIN_COMMAND_PROTECT_FLAG_REMOVE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click your Protected Block to remove the Flag";
    public static final String PLUGIN_COMMAND_PROTECT_RIGHT_ADD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click your Protected Block to add the Player";
    public static final String PLUGIN_COMMAND_PROTECT_RIGHT_PLAYER_NOTFOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "No Player found with Name: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_COMMAND_PROTECT_RIGHT_REMOVE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click your Protected Block to remove the Player";
    public static final String PLUGIN_COMMAND_PROTECT_WRONG_SUB_COMMAND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Wrong Sub Command";   

    public static final String PLUGIN_COMMAND_SIGN_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Use " + PLUGIN_COMMAND_NAME_COLOR + "/" + CommandNameConstants.PLUGIN_COMMAND_NAME_SIGN + " " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_SIGN_COPY + PLUGIN_MESSAGE_COLOR + " or " + PLUGIN_COMMAND_ARG_COLOR + CommandNameConstants.PLUGIN_COMMAND_NAME_SIGN_EDIT;
    public static final String PLUGIN_COMMAND_SIGN_COPY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click the Sign you want to copy";
    public static final String PLUGIN_COMMAND_SIGN_EDIT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click the Sign you want to edit";
    public static final String PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click the Redstone Lamp you want to Lit. Use this Command again to disable the Light Toogle Mode.";
    public static final String PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE_DISABLED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Light Toogle Mode is now disabled.";
    public static final String PLUGIN_COMMAND_ADMIN_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Use this command with the following Subcommands: npc, chat, light, afk, top, ping, cleanProtections";
    public static final String PLUGIN_COMMAND_ADMIN_CHAT_CLEARED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Chat was cleared";
    public static final String PLUGIN_COMMAND_ADMIN_TOP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Teleported to the highest Block";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS ="ID: " + PLUGIN_COMMAND_ARG_COLOR + "#%s" + PLUGIN_COMMAND_COLOR + " PMat: " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " != LMat: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_START = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Checking " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " Protection Materials against their Location Materials";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Cleaning Up " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " Protections";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_END = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Reduced to " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " Protections";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "No Protections to Clean Up";
    public static final String PLUGIN_COMMAND_ADMIN_WRONG_SUBCOMMAND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Wrong Subcommand";
    public static final String PLUGIN_COMMAND_WORLD_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Use this command with the following Subcommands: list load unload unloadNoSave";
    public static final String PLUGIN_COMMAND_WORLD_CREATE_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Use this command with the following arguments: name(String) type(WordType String) environment (Environment as String) structures (boolean)";
    public static final String PLUGIN_COMMAND_WORLD_WRONG_SUBCOMMAND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Wrong Subcommand";
    public static final String PLUGIN_COMMAND_WORLD_WORLD_NOT_LOADED = "World not loaded. Can't unload this World.";
    public static final String PLUGIN_COMMAND_WORLD_UNLOAD_WORLD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "World get's saved and unloaded.";
    public static final String PLUGIN_COMMAND_WORLD_UNLOAD_WORLD_NO_SAVE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "World get's unloaded without saving it.";
    public static final String PLUGIN_COMMAND_WORLD_LOAD_WORLD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "World get's loaded.";
    public static final String PLUGIN_COMMAND_WORLD_CREATE_WORLD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "World get's created.";
    public static final String PLUGIN_COMMAND_WORLD_WRONG_ARGUMENTS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Can't create World, wrong parameters given!";
    public static final String PLUGIN_COMMAND_SUDO_ACTIVATED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You are now sudoing %s";
    public static final String PLUGIN_COMMAND_SUDO_DEACTIVATED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Exited.";
    public static final String PLUGIN_COMMAND_SUDO_PLAYER_NOT_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Can't Sudo Player " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + ". Player not found!";
    public static final String PLUGIN_COMMAND_EXIT_KICK_MESSAGE = PLUGIN_COMMAND_COLOR + "exited.";
    public static final String PLUGIN_COMMAND_EXIT_SERVER_SHUTTING_DOWN = PLUGIN_COMMAND_COLOR + "Server is shutting down...";

    public static final String PLUGIN_COMMAND_TP_REQUEST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " eine Teleport Anfrage geschickt";
    public static final String PLUGIN_COMMAND_TP_REQUEST_TARGET = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Teleport Anfrage von " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "";
    public static final String PLUGIN_COMMAND_TP_REQUEST_EXPIRED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Teleport Anfrage ist abgelaufen!";
    public static final String PLUGIN_COMMAND_TP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest zu " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "teleportiert!";
    public static final String PLUGIN_COMMAND_TP_TO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "zu  dir teleportiert!";
    public static final String PLUGIN_COMMAND_TP_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze " + PLUGIN_COMMAND_ARG_COLOR + "/teleport <name> " + PLUGIN_COMMAND_COLOR + "," + PLUGIN_COMMAND_ARG_COLOR + "/teleport accept"+ PLUGIN_COMMAND_COLOR + ", " + PLUGIN_COMMAND_ARG_COLOR + "/teleport to <name>" + PLUGIN_COMMAND_COLOR + " oder " + PLUGIN_COMMAND_ARG_COLOR + "/teleport <x> <y> <z>";
    public static final String PLUGIN_COMMAND_TP_ACCEPT_NO_REQUEST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Keine offene Teleport Anfrage!";
    public static final String PLUGIN_COMMAND_TP_SEND_REQUEST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast eine Teleport Anfrage an " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " geschickt!";

    public static final String PLUGIN_BAG_AMOUNT = "Amount: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_BAG_RETRIEVE = "Click to retrieve";

    public static final String PLUGIN_BAGS_SAVED = "%s%s Bag(s) saved!";
    public static final String PLUGIN_PLAYERS_SAVED = "%s%s Player(s) saved!";
    public static final String PLUGIN_COMMAND_BAGS_NOT_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "The searched Bag " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "was not found!";

    public static final String PLUGIN_COMMAND_MARRY_SEND_REQUEST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast eine Hochzeitsanfrage an " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " geschickt!";
    public static final String PLUGIN_COMMAND_MARRY_MARRIED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "geheiratet!";
    public static final String PLUGIN_COMMAND_MARRY_REQUEST_EXPIRED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Hochzeitsanfrage ist abgelaufen!";
    public static final String PLUGIN_COMMAND_MARRY_REQUEST_IS_MAARIED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Hochzeitsanfrage ist ungültig, Spieler ist bereits verheitratet!";
    public static final String PLUGIN_COMMAND_MARRY_ACCEPT_NO_REQUEST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Keine offene Hochzeitsanfrage!";
    public static final String PLUGIN_COMMAND_MARRY_DIVORCE_NOT_MARRIED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du bist nicht verheiratet!";
    public static final String PLUGIN_COMMAND_MARRY_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze " + PLUGIN_COMMAND_ARG_COLOR + "/marry <name>" + PLUGIN_COMMAND_COLOR + ", " + PLUGIN_COMMAND_ARG_COLOR + "/marry accept"+ PLUGIN_COMMAND_COLOR + " oder " + PLUGIN_COMMAND_ARG_COLOR + "/marry divorce";
    public static final String PLUGIN_COMMAND_MARRY_DIVORCED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast dich von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "getrennt!";
    public static final String PLUGIN_COMMAND_MARRY_SELF_MARRIGE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du kannst dich nicht selbst heiraten!";

    public static final String PLUGIN_COMMAND_BACK = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest zurück teleportiert!";
    public static final String PLUGIN_COMMAND_BACK_NO_LOCATION = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast kein Back Punkt gesetzt!";


    public static final String PLUGIN_COMMAND_WARP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest gewarped!";
    public static final String PLUGIN_COMMAND_WARP_LIST_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Warp Liste:";
    public static final String PLUGIN_COMMAND_WARP_LIST = PLUGIN_COMMAND_COLOR + "Warp Name: " + PLUGIN_COMMAND_ARG_COLOR + "%s ";
    public static final String PLUGIN_COMMAND_WARP_ERROR_WORLD_UNLOADED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Error, Welt ist nicht geladen!";
    public static final String PLUGIN_COMMAND_WARP_ERROR_NO_WARP_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Kein Warp mit diesem Namen gefunden!";


    public static final String PLUGIN_COMMAND_SETGROUP = PLUGIN_COMMAND_COLOR + "Group " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " for Player " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " was set!";
    public static final String PLUGIN_COMMAND_SETGROUP_GROUP_NOT_FOUND = PLUGIN_COMMAND_COLOR + "Group " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " not found!";
    public static final String PLUGIN_BANK_INTEREST_NEXT_RUN = PLUGIN_COMMAND_COLOR + "Next Interest Payment Run in: " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " seconds!";

    public static final String PLUGIN_COMMAND_CUSTOMHEADS_TITLE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Heads";

    public static final String PLUGIN_COMMAND_PLAYERINFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Player Information: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
}
