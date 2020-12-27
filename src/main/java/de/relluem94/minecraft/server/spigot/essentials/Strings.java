package main.java.de.relluem94.minecraft.server.spigot.essentials;

public class Strings {

    public static final String PLUGIN_NAME = "RelluEssentials";
    public static final String PLUGIN_PREFIX = "§o§l§4" + PLUGIN_NAME + "§r§f";
    public static final String PLUGIN_SPACER = "§7 >> §f";
    public static final String PLUGIN_COMMAND_COLOR = "§f";
    public static final String PLUGIN_COMMAND_NAME_COLOR = "§b";
    public static final String PLUGIN_COMMAND_ARG_COLOR = "§b";
    public static final String PLUGIN_MESSAGE_COLOR = "§f";
    public static final String PLUGIN_START_MESSAGE = " starts configuring ...";
    public static final String PLUGIN_STOP_MESSAGE = " shutdown();";
    public static final String PLUGIN_BROADCAST_NAME = "§5Broadcast";
    
    public static final String PLUGIN_REGISTER_ENCHANTMENT = " Registered enchantment %s with id %s!";

    public static final String PLUGIN_COMMAND_GAMEMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Gamemode von %s" + PLUGIN_COMMAND_COLOR + " wurde zu " + PLUGIN_COMMAND_NAME_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " geändert!";

    public static final String PLUGIN_COMMAND_AFK = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " ist jetzt %s";
    public static final String PLUGIN_COMMAND_AFK_ACTIVATED = "§cafk";
    public static final String PLUGIN_COMMAND_AFK_DEACTIVATED = "§awieder da";
    public static final String PLUGIN_COMMAND_FLYMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Flugmodus von %s" + PLUGIN_COMMAND_COLOR + " wurde " + PLUGIN_COMMAND_ARG_COLOR + "%s!";
    public static final String PLUGIN_COMMAND_FLYMODE_ACTIVATED = "aktiviert";
    public static final String PLUGIN_COMMAND_FLYMODE_DEACTIVATED = "deaktiviert";

    public static final String PLUGIN_COMMAND_RELLU_RELOAD = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Konfiguration wurde neugeladen!";
    public static final String PLUGIN_COMMAND_RELLU_SAVE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Konfiguration wurde gespeichert!";
    public static final String PLUGIN_COMMAND_RELLU_PING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast ein Ping von " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "ms!";
    public static final String PLUGIN_COMMAND_RELLU_PING_OTHER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " hast ein Ping von " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "ms!";
    public static final String PLUGIN_COMMAND_RELLU_WRONG_COMMAND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es wurde kein passender Subbefehl gefunden!";
    public static final String PLUGIN_COMMAND_RELLU_OPTIONS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze: " + PLUGIN_COMMAND_ARG_COLOR + "save" + PLUGIN_COMMAND_COLOR + ", " + PLUGIN_COMMAND_ARG_COLOR + "reload" + PLUGIN_COMMAND_COLOR + ", " + PLUGIN_COMMAND_ARG_COLOR + "ping";

    public static final String PLUGIN_COMMAND_CRAFTINGBENCH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Die Werkbank von %s" + PLUGIN_COMMAND_COLOR + " wurde geöffnet!";

    public static final String PLUGIN_COMMAND_COOKIES = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Ein Keks für dich, %s";
    public static final String PLUGIN_COMMAND_COOKIES_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast %s " + PLUGIN_COMMAND_COLOR + "ein Keks geschenkt!";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_1 = PLUGIN_COMMAND_COLOR + "Mit viel Liebe gebacken von %s";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_2 = PLUGIN_COMMAND_COLOR + "Backe doch auch einen Keks!";
    public static final String PLUGIN_COMMAND_COOKIES_LORE_3 = PLUGIN_COMMAND_COLOR + "Wie selbstverliebt!";
    public static final String PLUGIN_COMMAND_COOKIES_DISPLAYNAME = PLUGIN_COMMAND_COLOR + "Schokoladenkeks";

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
    public static final String PLUGIN_COMMAND_NICK = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du den Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "umbenannt!";

    public static final String PLUGIN_COMMAND_SUICIDE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "hat sich umgebracht!";
    public static final String PLUGIN_COMMAND_WHERE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Spieler " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "befindet sich bei " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + "!";
    public static final String PLUGIN_COMMAND_WHERE_STRING = "X: %s " + "Y: %s " + "Z: %s " + "Welt: %s";
    
    public static final String PLUGIN_COMMAND_MSG_PLAYER_OFFLINE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Spieler ist Offline!";
    public static final String PLUGIN_COMMAND_MSG_NO_ONE_TO_REPLY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Niemand da der dir antworten könnte!";
    public static final String PLUGIN_COMMAND_MSG_SPACER_IN = "§9 >> §f";
    public static final String PLUGIN_COMMAND_MSG_SPACER_OUT = "§9 << §f";
    public static final String PLUGIN_COMMAND_MSG_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Nutze " + PLUGIN_COMMAND_ARG_COLOR + "/msg " + PLUGIN_COMMAND_COLOR +  "<" + PLUGIN_COMMAND_ARG_COLOR + "spieler" + PLUGIN_COMMAND_COLOR + ">" + " " + "<" + PLUGIN_COMMAND_ARG_COLOR + "nachricht" + PLUGIN_COMMAND_COLOR + ">";
    
    public static final String PLUGIN_COMMAND_BROADCAST_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Um ein Title Broadcast zu machen nutze " + PLUGIN_COMMAND_ARG_COLOR + "/broadcast title " + PLUGIN_COMMAND_COLOR + "sonst " + PLUGIN_COMMAND_ARG_COLOR + "/broadcast " + PLUGIN_COMMAND_COLOR +  "<" + PLUGIN_COMMAND_ARG_COLOR + "message" + PLUGIN_COMMAND_COLOR + ">";
    
    public static final String PLUGIN_COMMAND_PERMISSION_MISSING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Dafür hast du leider keine Rechte!";
    public static final String PLUGIN_COMMAND_NOT_A_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du bist leider kein Spieler!";
    public static final String PLUGIN_COMMAND_TO_LESS_ARGUMENTS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Zu wenig Argumente!";

    public static final String PLUGIN_EVENT_JOIN_MESSAGE = "§2[\u2726] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server betreten.";
    public static final String PLUGIN_EVENT_QUIT_MESSAGE = "§4[\u274C] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server verlassen.";
    public static final String PLUGIN_EVENT_DEATH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du stabst bei " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " dein Todespunkt ist gespeichert!";

    
    public static final String PLUGIN_EVENT_SKILL_REPAIR_DONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast den Gegenstand repariert!";
    public static final String PLUGIN_EVENT_SKILL_REPAIR_WARNING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du benötigst mehr" + PLUGIN_COMMAND_ARG_COLOR + " %s " + PLUGIN_COMMAND_COLOR + "um diesen Gegenstand zu reparieren!";
    public static final String PLUGIN_EVENT_SKILL_SALVAGE_DONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast" + PLUGIN_COMMAND_ARG_COLOR + " %s " + PLUGIN_COMMAND_COLOR + "erhalten!";
}
