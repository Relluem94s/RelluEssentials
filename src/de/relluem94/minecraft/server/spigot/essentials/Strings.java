package de.relluem94.minecraft.server.spigot.essentials;

public class Strings {
	public static final String PLUGIN_PREFIX = "§5[RelluEssentials]§f";
	public static final String PLUGIN_SPACER = "§7 >> §f";
	public static final String PLUGIN_COMMAND_COLOR = "§6";
	public static final String PLUGIN_COMMAND_NAME_COLOR = "§c";
	public static final String PLUGIN_COMMAND_ARG_COLOR = "§b";
	public static final String PLUGIN_PLAYER_COLOR = "§a";
	public static final String PLUGIN_MESSAGE_COLOR = "§f";
	
	public static final String PLUGIN_COMMAND_GAMEMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Gamemode von " + PLUGIN_PLAYER_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " wurde zu " + PLUGIN_COMMAND_NAME_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " geändert";
	public static final String PLUGIN_COMMAND_FLYMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Flugmodus von " + PLUGIN_PLAYER_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " wurde " + PLUGIN_COMMAND_ARG_COLOR + "%s";
	public static final String PLUGIN_COMMAND_CRAFTINGBENCH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Die Werkbank von " + PLUGIN_PLAYER_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " wurde geöffnet!";
	
	public static final String PLUGIN_COMMAND_COOKIES = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Ein Keks für dich, " + PLUGIN_PLAYER_COLOR + "%s";
	public static final String PLUGIN_COMMAND_COOKIES_LORE_1 = PLUGIN_COMMAND_COLOR + "Mit viel Liebe gebacken von " + PLUGIN_PLAYER_COLOR + "%s";
	public static final String PLUGIN_COMMAND_COOKIES_LORE_2 = PLUGIN_COMMAND_COLOR + "Backe doch auch einen Keks!";
	public static final String PLUGIN_COMMAND_COOKIES_LORE_3 = PLUGIN_COMMAND_COLOR + "Wie selbstverliebt!";
	public static final String PLUGIN_COMMAND_COOKIES_DISPLAYNAME = PLUGIN_COMMAND_COLOR + "Schokoladenkeks";
	
	public static final String PLUGIN_COMMAND_SUN = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du lässt die Sonne in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "scheinen!";
	public static final String PLUGIN_COMMAND_SUN_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Die Sonne die Sonne scheint nur für dich!";
	
	public static final String PLUGIN_COMMAND_RAIN = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du lässt es in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " regnen!";
	public static final String PLUGIN_COMMAND_RAIN_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es regnet nur für dich!";
	public static final String PLUGIN_COMMAND_STORM = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du lässt es in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + " gewittern!";
	public static final String PLUGIN_COMMAND_DAY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es ist jetzt Tag in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "!";
	public static final String PLUGIN_COMMAND_NIGHT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Es ist jetzt Nacht in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "!";

	public static final String PLUGIN_COMMAND_ENDERCHEST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast deine Enderchest geöffnet!";
	public static final String PLUGIN_COMMAND_ENDERCHEST_PLAYER = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast die Enderchest von " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "geöffnet!";
	
	
	public static final String PLUGIN_COMMAND_SPAWN = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du wurdest in der Welt " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "an den Spawn teleportiert!";
	
	public static final String PLUGIN_EVENT_JOIN_MESSAGE = "§2[\u2726] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server betreten.";
	public static final String PLUGIN_EVENT_QUIT_MESSAGE = "§4[\u274C] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server verlassen.";
}
