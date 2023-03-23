package de.relluem94.minecraft.server.spigot.essentials;

import de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants;

public class Strings {

    private Strings() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    /*********************************************************************************/
    /*                             PLUGIN   STUFF                                    */
    /*********************************************************************************/

    public static final String PLUGIN_EOL = System.getProperty("line.separator");

    public static final String PLUGIN_COLOR_COMMAND = "§f";
    public static final String PLUGIN_COLOR_COMMAND_NAME = "§b";
    public static final String PLUGIN_COLOR_COMMAND_ARG = "§b";
    public static final String PLUGIN_COLOR_MESSAGE = "§f";
    public static final String PLUGIN_COLOR_CONSOLE = "§c";
    public static final String PLUGIN_COLOR_COMMAND_BLOCK = "§8";
    public static final String PLUGIN_COLOR_MONEY = "§6"; 
    public static final String PLUGIN_COLOR_BROADCAST = "§5";
    public static final String PLUGIN_COLOR_MESSAGE_SPACER = "§7";
    public static final String PLUGIN_COLOR_RESET = "§r";
    public static final String PLUGIN_COLOR_LOGO_RELLU = "§8";
    public static final String PLUGIN_COLOR_LOGO_ESSENTIALS = "§c";
    public static final String PLUGIN_COLOR_POSITIVE = "§a";
    public static final String PLUGIN_COLOR_NEGATIVE = "§c";
    public static final String PLUGIN_COLOR_NEUTRAL = "§6";
    

    public static final String PLUGIN_NAME_RELLU = "Rellu";
    public static final String PLUGIN_NAME_ESSENTIALS = "Essentials";
    public static final String PLUGIN_NAME_INITIAL_RELLU = "R";
    public static final String PLUGIN_NAME_INITIAL_ESSENTIALS = "E";
    public static final String PLUGIN_NAME = PLUGIN_NAME_RELLU + PLUGIN_NAME_ESSENTIALS;
    public static final String PLUGIN_NAME_SHORT = PLUGIN_COLOR_LOGO_RELLU + PLUGIN_NAME_INITIAL_RELLU + PLUGIN_COLOR_LOGO_ESSENTIALS + PLUGIN_NAME_INITIAL_ESSENTIALS + PLUGIN_COLOR_MESSAGE;
    public static final String PLUGIN_NAME_PREFIX = "§o§l" + PLUGIN_COLOR_LOGO_RELLU + PLUGIN_NAME_RELLU + PLUGIN_COLOR_LOGO_ESSENTIALS + PLUGIN_NAME_ESSENTIALS + PLUGIN_COLOR_RESET + PLUGIN_COLOR_MESSAGE;
    public static final String PLUGIN_NAME_CONSOLE = PLUGIN_COLOR_MESSAGE + "[" + PLUGIN_NAME_PREFIX + PLUGIN_COLOR_MESSAGE + "] ";
    public static final String PLUGIN_NAME_BROADCAST = PLUGIN_COLOR_BROADCAST + "Broadcast";
    public static final String PLUGIN_NAME_CHAT_CONSOLE = PLUGIN_COLOR_CONSOLE + "Console";
    public static final String PLUGIN_NAME_MONEY = PLUGIN_COLOR_MONEY + "Coins" + PLUGIN_COLOR_MESSAGE; 

    public static final String PLUGIN_WORLD_LOBBY = "lobby";
    public static final String PLUGIN_WORLD_WORLD = "world";
    public static final String PLUGIN_WORLD_WORLD_NETHER = "world_nether";
    public static final String PLUGIN_WORLD_WORLD_THE_END = "world_the_end";

    public static final String PLUGIN_FORMS_SPACER_CHANNEL = " >> " + PLUGIN_COLOR_COMMAND;
    public static final String PLUGIN_FORMS_SPACER_MESSAGE= PLUGIN_COLOR_MESSAGE_SPACER + " >> " + PLUGIN_COLOR_MESSAGE;
    public static final String PLUGIN_FORMS_BORDER = "<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>";
    public static final String PLUGIN_FORMS_SHORT_BORDER = "<><><><><><><><><><><><><><><><><><>";
    public static final String PLUGIN_FORMS_WHITESPACE = "               ";
    public static final String PLUGIN_FORMS_WHITESPACE_SHORT = "    ";
    public static final String PLUGIN_FORMS_COMMAND_PREFIX = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_COMMAND;

    public static final String PLUGIN_SYMBOL_HEAVY_CHECK_MARK = PLUGIN_COLOR_POSITIVE + "\u2714 ";
    public static final String PLUGIN_SYMBOL_BLACK_LARGE_CIRCLE = PLUGIN_COLOR_NEUTRAL + "\u2B24 ";
    public static final String PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X = PLUGIN_COLOR_NEGATIVE + "\u2716 ";
    public static final String PLUGIN_SYMBOL_RIGHT_POINTING_ANGLE_BRACKET = PLUGIN_COLOR_POSITIVE + "\u232A";
    public static final String PLUGIN_SYMBOL_LEFT_POINTING_ANGLE_BRACKET = PLUGIN_COLOR_NEGATIVE + "\u2329";
    public static final String PLUGIN_SYMBOL_CROSS_MARK = PLUGIN_COLOR_NEGATIVE + "\u274C";
    public static final String PLUGIN_SYMBOL_BLACK_FOUR_POINTED_STAR = PLUGIN_COLOR_POSITIVE + "\u2726";
    // \u274C Cross Mark (U+274C)
    // \u2726 Black Four Pointed Star (U+2726)

    public static final String PLUGIN_SIGN_NAME = PLUGIN_COLOR_MESSAGE + "[" + PLUGIN_NAME_SHORT + "]";
    public static final String PLUGIN_SIGN_CLICK = PLUGIN_COLOR_MESSAGE + "[Click here]";
    
    public static final String PLUGIN_MANAGER_START_MESSAGE = "starts configuring ...";
    public static final String PLUGIN_MANAGER_STOP_MESSAGE = "shutdown();";
    public static final String PLUGIN_MANAGER_STARTTIME_MESSAGE = "wurde in %s ms gestartet!";
    public static final String PLUGIN_MANAGER_REGISTER_SKILLS = "Registriere Skills!";
    public static final String PLUGIN_MANAGER_SKILLS_REGISTERED = "Skills erfolgreich registriert!";
    public static final String PLUGIN_MANAGER_LOADING_CONFIGS = "Configs werden geladen!";
    public static final String PLUGIN_MANAGER_CONFIGS_LOADED = "Configs sind geladen!";
    public static final String PLUGIN_MANAGER_REGISTER_EVENTS = "Registriere Events!";
    public static final String PLUGIN_MANAGER_EVENTS_REGISTERED = "%s Events erfolgreich registriert!";
    public static final String PLUGIN_MANAGER_REGISTER_RECIPE = "Registriere Rezepte!";
    public static final String PLUGIN_MANAGER_RECIPE_REGISTERED = "%s Rezepte erfolgreich registriert!";
    public static final String PLUGIN_MANAGER_REGISTER_COMMANDS = "Registriere Befehle!";
    public static final String PLUGIN_MANAGER_COMMANDS_REGISTERED = "%s Befehle erfolgreich registriert!";
    public static final String PLUGIN_MANAGER_REGISTER_AUTOSAVE = "Registriere Auto Save!";
    public static final String PLUGIN_MANAGER_AUTOSAVE_REGISTERED = "Auto Save erfolgreich registriert!";
    public static final String PLUGIN_MANAGER_REGISTER_ENCHANTMENTS = "Registriere Enchantments!";
    public static final String PLUGIN_MANAGER_ENCHANTMENTS_REGISTERED = "%s Enchantments erfolgreich registriert!";
    public static final String PLUGIN_MANAGER_REGISTER_ENCHANTMENT = "Registered enchantment %s with id %s!";

    public static final String PLUGIN_INTERNAL_UTILITY_CLASS = "This is a Utility Class";

    

    /*********************************************************************************/
    /*                            COMMAND   STUFF                                    */
    /*********************************************************************************/
    public static final String PLUGIN_COMMAND_FLYMODE = PLUGIN_FORMS_COMMAND_PREFIX + "Der Flugmodus von %s" + PLUGIN_COLOR_COMMAND + " wurde " + PLUGIN_COLOR_COMMAND_ARG + "%s!";
    public static final String PLUGIN_COMMAND_FLYMODE_ACTIVATED = "aktiviert";
    public static final String PLUGIN_COMMAND_FLYMODE_DEACTIVATED = "deaktiviert";

    public static final String PLUGIN_COMMAND_ADMIN_PING = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast ein Ping von " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + "ms!";
    public static final String PLUGIN_COMMAND_ADMIN_PING_OTHER = PLUGIN_FORMS_COMMAND_PREFIX + "Der Spieler " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " hast ein Ping von " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + "ms!";
    public static final String PLUGIN_COMMAND_ADMIN_PING_OTHER_NOT_FOUND  = PLUGIN_FORMS_COMMAND_PREFIX + "Der Spieler " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "wurde nicht gefunden";

    public static final String PLUGIN_COMMAND_CRAFTINGBENCH = PLUGIN_FORMS_COMMAND_PREFIX + "Die Werkbank von %s" + PLUGIN_COLOR_COMMAND + " wurde geöffnet!";

    public static final String PLUGIN_COMMAND_COOKIES = PLUGIN_FORMS_COMMAND_PREFIX + "Ein Keks für dich, %s";
    public static final String PLUGIN_COMMAND_COOKIES_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast %s " + PLUGIN_COLOR_COMMAND + "ein Keks geschenkt!";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_1 = PLUGIN_COLOR_COMMAND + "Mit viel Liebe gebacken von %s";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_2 = PLUGIN_COLOR_COMMAND + "Backe doch auch einen Keks!";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_3 = PLUGIN_COLOR_COMMAND + "Wie selbstverliebt!";
    public static final String PLUGIN_COMMAND_COOKIES_DISPLAYNAME = PLUGIN_COLOR_COMMAND + "Schokoladenkeks";

    public static final String PLUGIN_COMMAND_HEAD = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast ein Kopf von " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " erzeugt!";
    public static final String PLUGIN_COMMAND_HEAD_NOT_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "Es konnte kei Kopf erzeugt werden, Spieler nicht gefunden";
    public static final String PLUGIN_COMMAND_VANISH_DISABLE = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast vanish für " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "deaktiviert!";
    public static final String PLUGIN_COMMAND_VANISH_ENABLE = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast vanish für " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "aktiviert!";
    public static final String PLUGIN_COMMAND_VANISH = PLUGIN_FORMS_COMMAND_PREFIX + "Dein vanish wurde umgeschaltet!";

    public static final String PLUGIN_COMMAND_MORE = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast mehr von " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " erzeugt!";
    public static final String PLUGIN_COMMAND_MORE_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast mehr von " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " erhalten!";

    public static final String PLUGIN_COMMAND_REPAIR = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " repariert!";
    public static final String PLUGIN_COMMAND_REPAIR_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " repariert bekommen!";

    public static final String PLUGIN_COMMAND_GAMERULES = PLUGIN_FORMS_COMMAND_PREFIX + "Die Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "hat folgende Einstellungen:";

    public static final String PLUGIN_COMMAND_HOME = PLUGIN_FORMS_COMMAND_PREFIX + "Du wurdest in der Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "an den Bett Spawn teleportiert!";
    public static final String PLUGIN_COMMAND_HOME_TP = PLUGIN_FORMS_COMMAND_PREFIX + "Du wurdest zu " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "teleportiert!";
    public static final String PLUGIN_COMMAND_HOME_NONE = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast kein Home!";

    public static final String PLUGIN_COMMAND_HOME_LIST = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast folgende Homes:";
    public static final String PLUGIN_COMMAND_HOME_LIST_NAME =  PLUGIN_COLOR_COMMAND + "Name: " + PLUGIN_COLOR_COMMAND_ARG + "%s" + " §7(" + PLUGIN_COLOR_COMMAND_ARG + "%s§7)";
    public static final String PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast folgende Todespunkte:";
    public static final String PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS_NAME =  PLUGIN_COLOR_COMMAND + "Todespunkt: " + PLUGIN_COLOR_COMMAND_ARG + "%s" + " §7(" + PLUGIN_COLOR_COMMAND_ARG + "%s§7)";

    public static final String PLUGIN_COMMAND_HOME_NOT_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast kein Home mit dem Namen " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + "!";
    public static final String PLUGIN_COMMAND_HOME_EXISTS = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast bereits ein Home mit dem Namen " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + "!";
    public static final String PLUGIN_COMMAND_HOME_RESERVED = PLUGIN_FORMS_COMMAND_PREFIX + "Du kannst kein Home mit dem Namen " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " speichern!";
    public static final String PLUGIN_COMMAND_HOME_NO_BED = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast kein Bett in der Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " an das du teleportiert werden kannst!";
    public static final String PLUGIN_COMMAND_HOME_SET = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast dein Home " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "gesetzt!";
    public static final String PLUGIN_COMMAND_HOME_DELETE = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast dein Home " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "gelöscht!";
    public static final String PLUGIN_COMMAND_HOME_DEATH_DELETE = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast dein Todespunkt " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "gelöscht!";

    public static final String PLUGIN_COMMAND_SUN = PLUGIN_FORMS_COMMAND_PREFIX + "Du lässt die Sonne in der Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "scheinen!";
    public static final String PLUGIN_COMMAND_SUN_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Die Sonne die Sonne scheint nur für dich!";

    public static final String PLUGIN_COMMAND_RAIN = PLUGIN_FORMS_COMMAND_PREFIX + "Du lässt es in der Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " regnen!";
    public static final String PLUGIN_COMMAND_RAIN_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Es regnet nur für dich!";

    public static final String PLUGIN_COMMAND_STORM = PLUGIN_FORMS_COMMAND_PREFIX + "Du lässt es in der Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " gewittern!";

    public static final String PLUGIN_COMMAND_DAY = PLUGIN_FORMS_COMMAND_PREFIX + "Es ist jetzt Tag in der Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + "!";

    public static final String PLUGIN_COMMAND_GOD_ON = PLUGIN_FORMS_COMMAND_PREFIX + "Du bist der Gott!";
    public static final String PLUGIN_COMMAND_GOD_OFF = PLUGIN_FORMS_COMMAND_PREFIX + "Du bist wieder sterblich!";

    public static final String PLUGIN_COMMAND_HEAL = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast dich geheilt!";

    public static final String PLUGIN_COMMAND_NIGHT = PLUGIN_FORMS_COMMAND_PREFIX + "Es ist jetzt Nacht in der Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + "!";

    public static final String PLUGIN_COMMAND_ENDERCHEST = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast deine Enderchest geöffnet!";
    public static final String PLUGIN_COMMAND_ENDERCHEST_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast die Enderchest von " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "geöffnet!";

    public static final String PLUGIN_COMMAND_INVENTORY = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast dein Inventar geöffnet!";
    public static final String PLUGIN_COMMAND_INVENTORY_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast das Inventar von " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "geöffnet!";

    public static final String PLUGIN_COMMAND_SPAWN = PLUGIN_FORMS_COMMAND_PREFIX + "Du wurdest in der Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "an den Spawn teleportiert!";

    public static final String PLUGIN_COMMAND_NICK = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast den Spieler " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "umbenannt!";

    public static final String PLUGIN_COMMAND_SUICIDE = PLUGIN_FORMS_COMMAND_PREFIX + "Der Spieler " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "hat sich umgebracht!";

    public static final String PLUGIN_COMMAND_WHERE = PLUGIN_FORMS_COMMAND_PREFIX + "Der Spieler " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "befindet sich bei " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + "!";
    public static final String PLUGIN_COMMAND_WHERE_STRING = PLUGIN_COLOR_COMMAND + "X: " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "Y: " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "Z: " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "Welt: " + PLUGIN_COLOR_COMMAND_ARG + "%s";

    public static final String PLUGIN_COMMAND_MSG_PLAYER_OFFLINE = PLUGIN_FORMS_COMMAND_PREFIX + "Spieler ist Offline!";
    public static final String PLUGIN_COMMAND_MSG_NO_ONE_TO_REPLY = PLUGIN_FORMS_COMMAND_PREFIX + "Niemand da der dir antworten könnte!";
    public static final String PLUGIN_COMMAND_MSG_SPACER_IN = "§9 >> §f";
    public static final String PLUGIN_COMMAND_MSG_SPACER_OUT = "§9 << §f";
    public static final String PLUGIN_COMMAND_MSG_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Nutze " + PLUGIN_COLOR_COMMAND_ARG + "/msg " + PLUGIN_COLOR_COMMAND + "<" + PLUGIN_COLOR_COMMAND_ARG + "spieler" + PLUGIN_COLOR_COMMAND + ">" + " " + "<" + PLUGIN_COLOR_COMMAND_ARG + "nachricht" + PLUGIN_COLOR_COMMAND + ">";

    public static final String PLUGIN_COMMAND_PRINT_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Nutze " + PLUGIN_COLOR_COMMAND_ARG + "/print " + "<" + PLUGIN_COLOR_COMMAND_ARG + "nachricht" + PLUGIN_COLOR_COMMAND + ">";

    public static final String PLUGIN_COMMAND_RENAME_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Nutze " + PLUGIN_COLOR_COMMAND_ARG + "/rename " + "<" + PLUGIN_COLOR_COMMAND_ARG + "name" + PLUGIN_COLOR_COMMAND + ">";
    public static final String PLUGIN_COMMAND_RENAME_AIR = PLUGIN_FORMS_COMMAND_PREFIX + "Du kannst Luft nicht umbenennen! ";
    public static final String PLUGIN_COMMAND_RENAME = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast dein Gegenstand umbenannt!";

    public static final String PLUGIN_COMMAND_WORLD = PLUGIN_FORMS_COMMAND_PREFIX + "Folgende Welten gibt es:";
    public static final String PLUGIN_COMMAND_WORLD_NOT_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "Die Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " existiert nicht";

    public static final String PLUGIN_COMMAND_POKE = PLUGIN_FORMS_COMMAND_PREFIX + "Nutze /poke " + PLUGIN_COLOR_COMMAND_ARG + "<Name>" + PLUGIN_COLOR_COMMAND + " um einen Spieler anzustubsen";
    public static final String PLUGIN_COMMAND_POKE_TITLE = "§4Buuuh";
    public static final String PLUGIN_COMMAND_POKE_SUBTITLE = "~~~~~~~~~~~~~~";
    public static final String PLUGIN_COMMAND_POKE_MESSAGE_TARGET = PLUGIN_FORMS_COMMAND_PREFIX + "%s " + PLUGIN_COLOR_COMMAND + "hat dich angestupst!";
    public static final String PLUGIN_COMMAND_POKE_MESSAGE_SENDER = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast " + "%s " + PLUGIN_COLOR_COMMAND + "angestupst!";

    public static final String PLUGIN_COMMAND_SPEED = PLUGIN_FORMS_COMMAND_PREFIX + "Geschwindigkeit wurde auf " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " gesetzt";
    public static final String PLUGIN_COMMAND_SPEED_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Nutze " + PLUGIN_COLOR_COMMAND_ARG + "/speed " + "<" + PLUGIN_COLOR_COMMAND_ARG + "0-10" + PLUGIN_COLOR_COMMAND + ">";

    public static final String PLUGIN_COMMAND_BROADCAST_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Um ein Title Broadcast zu machen nutze " + PLUGIN_COLOR_COMMAND_ARG + "/broadcast title " + PLUGIN_COLOR_COMMAND + "sonst " + PLUGIN_COLOR_COMMAND_ARG + "/broadcast " + PLUGIN_COLOR_COMMAND + "<" + PLUGIN_COLOR_COMMAND_ARG + "message" + PLUGIN_COLOR_COMMAND + ">";

    public static final String PLUGIN_COMMAND_INVALID = PLUGIN_FORMS_COMMAND_PREFIX + "Invalid Data!";
    public static final String PLUGIN_COMMAND_PERMISSION_MISSING = PLUGIN_FORMS_COMMAND_PREFIX + "Dafür hast du leider keine Rechte!";
    public static final String PLUGIN_COMMAND_NOT_A_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Du bist leider kein Spieler!";
    public static final String PLUGIN_COMMAND_TO_LESS_ARGUMENTS = PLUGIN_FORMS_COMMAND_PREFIX + "Zu wenig Argumente!";
    public static final String PLUGIN_COMMAND_TO_MANY_ARGUMENTS = PLUGIN_FORMS_COMMAND_PREFIX + "Zu viele Argumente!";
    public static final String PLUGIN_COMMAND_TARGET_NOT_A_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "%s" + PLUGIN_COLOR_COMMAND + " ist kein Spieler!";

    public static final String PLUGIN_COMMAND_PURSE_GAIN = PLUGIN_FORMS_COMMAND_PREFIX + "You gained " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " and now have " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " in your Purse!";
    public static final String PLUGIN_COMMAND_PURSE_TO_ITEM = PLUGIN_FORMS_COMMAND_PREFIX + "You put out " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY;
    public static final String PLUGIN_COMMAND_PURSE_TO_ITEM_VALUE_TO_HIGH = PLUGIN_FORMS_COMMAND_PREFIX + "The value you entered is to high!";
    public static final String PLUGIN_COMMAND_PURSE_TO_ITEM_NOT_ENOUGH_MONEY = PLUGIN_FORMS_COMMAND_PREFIX + "Not enough " + PLUGIN_NAME_MONEY;
    public static final String PLUGIN_COMMAND_PURSE_TOTAL = PLUGIN_FORMS_COMMAND_PREFIX + "You have " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " in your Purse!";
    public static final String PLUGIN_COMMAND_PURSE_TOTAL_OTHER = PLUGIN_FORMS_COMMAND_PREFIX + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + " has " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " in the Purse!";

    public static final String PLUGIN_COMMAND_PROTECT_COMMAND_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Use " + PLUGIN_COLOR_COMMAND_NAME + "/" + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT + " " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_ADD + PLUGIN_COLOR_MESSAGE + ", " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_REMOVE + PLUGIN_COLOR_MESSAGE + " or " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_FLAG + PLUGIN_COLOR_MESSAGE + ", " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_RIGHT;
    public static final String PLUGIN_COMMAND_PROTECT_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Click a Protected Block to view the Protection Info";
    public static final String PLUGIN_COMMAND_PROTECT_ADD = PLUGIN_FORMS_COMMAND_PREFIX + "Click Protectable Block to create a Protection";
    public static final String PLUGIN_COMMAND_PROTECT_REMOVE = PLUGIN_FORMS_COMMAND_PREFIX + "Click your Protected Block to remove the Protection";
    public static final String PLUGIN_COMMAND_PROTECT_FLAG = PLUGIN_FORMS_COMMAND_PREFIX + "Use " + PLUGIN_COLOR_COMMAND_NAME + "/" + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT + " " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_FLAG + " " + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_FLAG_ADD + PLUGIN_COLOR_MESSAGE + " or " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_FLAG_REMOVE + " flagname";
    public static final String PLUGIN_COMMAND_PROTECT_FLAG_NOT_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "Flag was not found!";
    public static final String PLUGIN_COMMAND_PROTECT_RIGHT = PLUGIN_FORMS_COMMAND_PREFIX + "Use " + PLUGIN_COLOR_COMMAND_NAME + "/" + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT + " " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_RIGHT + " " + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_RIGHT_ADD + PLUGIN_COLOR_MESSAGE + " or " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT_RIGHT_REMOVE + " playername";
    public static final String PLUGIN_COMMAND_PROTECT_FLAG_ADD = PLUGIN_FORMS_COMMAND_PREFIX + "Click your Protected Block to add the Flag";
    public static final String PLUGIN_COMMAND_PROTECT_FLAG_REMOVE = PLUGIN_FORMS_COMMAND_PREFIX + "Click your Protected Block to remove the Flag";
    public static final String PLUGIN_COMMAND_PROTECT_RIGHT_ADD = PLUGIN_FORMS_COMMAND_PREFIX + "Click your Protected Block to add the Player";
    public static final String PLUGIN_COMMAND_PROTECT_RIGHT_PLAYER_NOTFOUND = PLUGIN_FORMS_COMMAND_PREFIX + "No Player found with Name: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_COMMAND_PROTECT_RIGHT_REMOVE = PLUGIN_FORMS_COMMAND_PREFIX + "Click your Protected Block to remove the Player";
    public static final String PLUGIN_COMMAND_PROTECT_WRONG_SUB_COMMAND = PLUGIN_FORMS_COMMAND_PREFIX + "Wrong Sub Command";   

    public static final String PLUGIN_COMMAND_SIGN_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Use " + PLUGIN_COLOR_COMMAND_NAME + "/" + CommandNameConstants.PLUGIN_COMMAND_NAME_SIGN + " " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_SIGN_COPY + PLUGIN_COLOR_MESSAGE + " or " + PLUGIN_COLOR_COMMAND_ARG + CommandNameConstants.PLUGIN_COMMAND_NAME_SIGN_EDIT;
    public static final String PLUGIN_COMMAND_SIGN_COPY = PLUGIN_FORMS_COMMAND_PREFIX + "Click the Sign you want to copy";
    public static final String PLUGIN_COMMAND_SIGN_EDIT = PLUGIN_FORMS_COMMAND_PREFIX + "Click the Sign you want to edit";
    public static final String PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE = PLUGIN_FORMS_COMMAND_PREFIX + "Click the Redstone Lamp you want to Lit. Use this Command again to disable the Light Toogle Mode.";
    public static final String PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE_DISABLED = PLUGIN_FORMS_COMMAND_PREFIX + "Light Toogle Mode is now disabled.";
    public static final String PLUGIN_COMMAND_ADMIN_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Use this command with the following Subcommands: npc, chat, light, afk, top, ping, cleanProtections";
    public static final String PLUGIN_COMMAND_ADMIN_CHAT_CLEARED = PLUGIN_FORMS_COMMAND_PREFIX + "Chat was cleared";
    public static final String PLUGIN_COMMAND_ADMIN_TOP = PLUGIN_FORMS_COMMAND_PREFIX + "Teleported to the highest Block";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS ="ID: " + PLUGIN_COLOR_COMMAND_ARG + "#%s" + PLUGIN_COLOR_COMMAND + " PMat: " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " != LMat: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_START = PLUGIN_FORMS_COMMAND_PREFIX + "Checking " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " Protection Materials against their Location Materials";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP = PLUGIN_FORMS_COMMAND_PREFIX + "Cleaning Up " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " Protections";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_END = PLUGIN_FORMS_COMMAND_PREFIX + "Reduced to " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " Protections";
    public static final String PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE = PLUGIN_FORMS_COMMAND_PREFIX + "No Protections to Clean Up";
    public static final String PLUGIN_COMMAND_ADMIN_WRONG_SUBCOMMAND = PLUGIN_FORMS_COMMAND_PREFIX + "Wrong Subcommand";
    public static final String PLUGIN_COMMAND_WORLD_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Use this command with the following Subcommands: list load unload unloadNoSave";
    public static final String PLUGIN_COMMAND_WORLD_CREATE_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Use this command with the following arguments: name(String) type(WordType String) environment (Environment as String) structures (boolean)";
    public static final String PLUGIN_COMMAND_WORLD_WRONG_SUBCOMMAND = PLUGIN_FORMS_COMMAND_PREFIX + "Wrong Subcommand";
    public static final String PLUGIN_COMMAND_WORLD_WORLD_NOT_LOADED = "World not loaded. Can't unload this World.";
    public static final String PLUGIN_COMMAND_WORLD_UNLOAD_WORLD = PLUGIN_FORMS_COMMAND_PREFIX + "World get's saved and unloaded.";
    public static final String PLUGIN_COMMAND_WORLD_UNLOAD_WORLD_NO_SAVE = PLUGIN_FORMS_COMMAND_PREFIX + "World get's unloaded without saving it.";
    public static final String PLUGIN_COMMAND_WORLD_LOAD_WORLD = PLUGIN_FORMS_COMMAND_PREFIX + "World get's loaded.";
    public static final String PLUGIN_COMMAND_WORLD_CREATE_WORLD = PLUGIN_FORMS_COMMAND_PREFIX + "World get's created.";
    public static final String PLUGIN_COMMAND_WORLD_WRONG_ARGUMENTS = PLUGIN_FORMS_COMMAND_PREFIX + "Can't create World, wrong parameters given!";
    public static final String PLUGIN_COMMAND_SUDO_ACTIVATED = PLUGIN_FORMS_COMMAND_PREFIX + "You are now sudoing %s";
    public static final String PLUGIN_COMMAND_SUDO_DEACTIVATED = PLUGIN_FORMS_COMMAND_PREFIX + "Exited.";
    public static final String PLUGIN_COMMAND_SUDO_PLAYER_NOT_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "Can't Sudo Player " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + ". Player not found!";
    public static final String PLUGIN_COMMAND_EXIT_KICK_MESSAGE = PLUGIN_COLOR_COMMAND + "exited.";
    public static final String PLUGIN_COMMAND_EXIT_SERVER_SHUTTING_DOWN = PLUGIN_COLOR_COMMAND + "Server is shutting down...";

    public static final String PLUGIN_COMMAND_TP_REQUEST = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " eine Teleport Anfrage geschickt";
    public static final String PLUGIN_COMMAND_TP_REQUEST_TARGET = PLUGIN_FORMS_COMMAND_PREFIX + "Teleport Anfrage von " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + "";
    public static final String PLUGIN_COMMAND_TP_REQUEST_EXPIRED = PLUGIN_FORMS_COMMAND_PREFIX + "Teleport Anfrage ist abgelaufen!";
    public static final String PLUGIN_COMMAND_TP = PLUGIN_FORMS_COMMAND_PREFIX + "Du wurdest zu " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "teleportiert!";
    public static final String PLUGIN_COMMAND_TP_TO = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "zu  dir teleportiert!";
    public static final String PLUGIN_COMMAND_TP_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Nutze " + PLUGIN_COLOR_COMMAND_ARG + "/teleport <name> " + PLUGIN_COLOR_COMMAND + "," + PLUGIN_COLOR_COMMAND_ARG + "/teleport accept"+ PLUGIN_COLOR_COMMAND + ", " + PLUGIN_COLOR_COMMAND_ARG + "/teleport to <name>" + PLUGIN_COLOR_COMMAND + " oder " + PLUGIN_COLOR_COMMAND_ARG + "/teleport <x> <y> <z>";
    public static final String PLUGIN_COMMAND_TP_ACCEPT_NO_REQUEST = PLUGIN_FORMS_COMMAND_PREFIX + "Keine offene Teleport Anfrage!";
    public static final String PLUGIN_COMMAND_TP_SEND_REQUEST = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast eine Teleport Anfrage an " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " geschickt!";

    public static final String PLUGIN_BAG_AMOUNT = "Amount: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_BAG_RETRIEVE = "Click to retrieve";

    public static final String PLUGIN_BAGS_SAVED = "%s%s Bag(s) saved!";
    public static final String PLUGIN_PLAYERS_SAVED = "%s%s Player(s) saved!";
    public static final String PLUGIN_COMMAND_BAGS_NOT_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "The searched Bag " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "was not found!";

    public static final String PLUGIN_COMMAND_MARRY_SEND_REQUEST = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast eine Hochzeitsanfrage an " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " geschickt!";
    public static final String PLUGIN_COMMAND_MARRY_MARRIED = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "geheiratet!";
    public static final String PLUGIN_COMMAND_MARRY_REQUEST_EXPIRED = PLUGIN_FORMS_COMMAND_PREFIX + "Hochzeitsanfrage ist abgelaufen!";
    public static final String PLUGIN_COMMAND_MARRY_REQUEST_IS_MAARIED = PLUGIN_FORMS_COMMAND_PREFIX + "Hochzeitsanfrage ist ungültig, Spieler ist bereits verheitratet!";
    public static final String PLUGIN_COMMAND_MARRY_ACCEPT_NO_REQUEST = PLUGIN_FORMS_COMMAND_PREFIX + "Keine offene Hochzeitsanfrage!";
    public static final String PLUGIN_COMMAND_MARRY_DIVORCE_NOT_MARRIED = PLUGIN_FORMS_COMMAND_PREFIX + "Du bist nicht verheiratet!";
    public static final String PLUGIN_COMMAND_MARRY_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Nutze " + PLUGIN_COLOR_COMMAND_ARG + "/marry <name>" + PLUGIN_COLOR_COMMAND + ", " + PLUGIN_COLOR_COMMAND_ARG + "/marry accept"+ PLUGIN_COLOR_COMMAND + " oder " + PLUGIN_COLOR_COMMAND_ARG + "/marry divorce";
    public static final String PLUGIN_COMMAND_MARRY_DIVORCED = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast dich von " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "getrennt!";
    public static final String PLUGIN_COMMAND_MARRY_SELF_MARRIGE = PLUGIN_FORMS_COMMAND_PREFIX + "Du kannst dich nicht selbst heiraten!";

    public static final String PLUGIN_COMMAND_BACK = PLUGIN_FORMS_COMMAND_PREFIX + "Du wurdest zurück teleportiert!";
    public static final String PLUGIN_COMMAND_BACK_NO_LOCATION = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast kein Back Punkt gesetzt!";


    public static final String PLUGIN_COMMAND_WARP = PLUGIN_FORMS_COMMAND_PREFIX + "Du wurdest gewarped!";
    public static final String PLUGIN_COMMAND_WARP_LIST_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Warp Liste:";
    public static final String PLUGIN_COMMAND_WARP_LIST = PLUGIN_COLOR_COMMAND + "Warp Name: " + PLUGIN_COLOR_COMMAND_ARG + "%s ";
    public static final String PLUGIN_COMMAND_WARP_ERROR_WORLD_UNLOADED = PLUGIN_FORMS_COMMAND_PREFIX + "Error, Welt ist nicht geladen!";
    public static final String PLUGIN_COMMAND_WARP_ERROR_NO_WARP_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "Kein Warp mit diesem Namen gefunden!";


    public static final String PLUGIN_COMMAND_SETGROUP = PLUGIN_COLOR_COMMAND + "Group " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " for Player " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " was set!";
    public static final String PLUGIN_COMMAND_SETGROUP_GROUP_NOT_FOUND = PLUGIN_COLOR_COMMAND + "Group " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " not found!";
    public static final String PLUGIN_BANK_INTEREST_NEXT_RUN = PLUGIN_COLOR_COMMAND + "Next Interest Payment Run in: " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " seconds!";

    public static final String PLUGIN_COMMAND_CUSTOMHEADS_TITLE = PLUGIN_FORMS_COMMAND_PREFIX + "Heads";

    public static final String PLUGIN_COMMAND_PLAYERINFO = PLUGIN_FORMS_COMMAND_PREFIX + "Player Information: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
}
