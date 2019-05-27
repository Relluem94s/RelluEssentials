package de.relluem94.minecraft.server.spigot.essentials;

public class Strings {
	public static final String PLUGIN_PREFIX = "§5[RelluEssentials]§f";
	public static final String PLUGIN_SPACER = "§8 >> §f";
	public static final String PLUGIN_COMMAND_COLOR = "§6";
	public static final String PLUGIN_COMMAND_NAME_COLOR = "§c";
	public static final String PLUGIN_COMMAND_ARG_COLOR = "§b";
	public static final String PLUGIN_PLAYER_COLOR = "§a";
	
	public static final String PLUGIN_COMMAND_GAMEMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Gamemode von " + PLUGIN_PLAYER_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " wurde zu " + PLUGIN_COMMAND_NAME_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " geändert";
	public static final String PLUGIN_COMMAND_FLYMODE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Der Flugmodus von " + PLUGIN_PLAYER_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " wurde " + PLUGIN_COMMAND_ARG_COLOR + "%s";
}
