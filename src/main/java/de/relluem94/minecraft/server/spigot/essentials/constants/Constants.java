package de.relluem94.minecraft.server.spigot.essentials.constants;

public interface Constants {
    
    /* ******************************************************************************* */
    /*                               PLUGIN   STUFF                                    */
    /* ******************************************************************************* */

    String PLUGIN_EOL = System.lineSeparator();

    String PLUGIN_COLOR_COMMAND = "§f";
    String PLUGIN_COLOR_COMMAND_ARG = "§b";
    String PLUGIN_COLOR_MESSAGE = "§f";
    String PLUGIN_COLOR_CONSOLE = "§c";
    String PLUGIN_COLOR_COMMAND_BLOCK = "§8";
    String PLUGIN_COLOR_MONEY = "§6"; 
    String PLUGIN_COLOR_BROADCAST = "§5";
    String PLUGIN_COLOR_MESSAGE_SPACER = "§7";
    String PLUGIN_COLOR_RESET = "§r";
    String PLUGIN_COLOR_LOGO_RELLU = "§8";
    String PLUGIN_COLOR_LOGO_ESSENTIALS = "§c";
    String PLUGIN_COLOR_POSITIVE = "§a";
    String PLUGIN_COLOR_NEGATIVE = "§c";

    String PLUGIN_NAME_RELLU = "Rellu";
    String PLUGIN_NAME_ESSENTIALS = "Essentials";
    String PLUGIN_NAME_INITIAL_RELLU = "R";
    String PLUGIN_NAME_INITIAL_ESSENTIALS = "E";
    String PLUGIN_NAME_SHORT = PLUGIN_COLOR_LOGO_RELLU + PLUGIN_NAME_INITIAL_RELLU + PLUGIN_COLOR_LOGO_ESSENTIALS + PLUGIN_NAME_INITIAL_ESSENTIALS + PLUGIN_COLOR_MESSAGE;
    String PLUGIN_NAME_PREFIX = PLUGIN_COLOR_LOGO_RELLU + PLUGIN_NAME_RELLU + PLUGIN_COLOR_LOGO_ESSENTIALS + PLUGIN_NAME_ESSENTIALS + PLUGIN_COLOR_RESET + PLUGIN_COLOR_MESSAGE;
    String PLUGIN_NAME_CONSOLE = PLUGIN_COLOR_MESSAGE + "[" + PLUGIN_NAME_PREFIX + PLUGIN_COLOR_MESSAGE + "] ";
    String PLUGIN_NAME_BROADCAST = PLUGIN_COLOR_BROADCAST + "Broadcast";
    String PLUGIN_NAME_CHAT_CONSOLE = PLUGIN_COLOR_CONSOLE + "Console";
    String PLUGIN_NAME_MONEY = PLUGIN_COLOR_MONEY + "Coins" + PLUGIN_COLOR_MESSAGE;

    String PLUGIN_WORLD_LOBBY = "lobby";
    String PLUGIN_WORLD_WORLD = "world";
    String PLUGIN_WORLD_WORLD_NETHER = "world_nether";
    String PLUGIN_WORLD_WORLD_THE_END = "world_the_end";

    String PLUGIN_FORMS_SPACER_CHANNEL = " >> " + PLUGIN_COLOR_COMMAND;
    String PLUGIN_FORMS_SPACER_MESSAGE = PLUGIN_COLOR_MESSAGE_SPACER + " >> " + PLUGIN_COLOR_MESSAGE;
    String PLUGIN_FORMS_BORDER = "<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>";
    String PLUGIN_FORMS_WHITESPACE_SHORT = "    ";
    String PLUGIN_FORMS_COMMAND_PREFIX = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_COMMAND;

    String PLUGIN_FORMS_MSG_SPACER_IN = "§9 >> §f";
    String PLUGIN_FORMS_MSG_SPACER_OUT = "§9 << §f";

    String PLUGIN_SYMBOL_RIGHT_POINTING_ANGLE_BRACKET = PLUGIN_COLOR_POSITIVE + "〉";
    String PLUGIN_SYMBOL_LEFT_POINTING_ANGLE_BRACKET = PLUGIN_COLOR_NEGATIVE + "〈";
    String PLUGIN_SYMBOL_CROSS_MARK = PLUGIN_COLOR_NEGATIVE + "❌";
    String PLUGIN_SYMBOL_BLACK_FOUR_POINTED_STAR = PLUGIN_COLOR_POSITIVE + "✦";

    String PLUGIN_SIGN_NAME = PLUGIN_COLOR_MESSAGE + "[" + PLUGIN_NAME_SHORT + "]";
    String PLUGIN_SIGN_CLICK = PLUGIN_COLOR_MESSAGE + "[Click here]";

    String PLUGIN_INTERNAL_UTILITY_CLASS = "This is a Utility Class";
}