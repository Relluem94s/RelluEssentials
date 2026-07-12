package de.relluem94.minecraft.server.spigot.essentials.constants;

public interface Constants {
    
    /* ******************************************************************************* */
    /*                               PLUGIN   STUFF                                    */
    /* ******************************************************************************* */

    String PLUGIN_EOL = System.lineSeparator();

    String PLUGIN_COLOR_COMMAND = "§f";
    String PLUGIN_COLOR_COMMAND_NAME = "§b";
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
    String PLUGIN_COLOR_NEUTRAL = "§6";
    

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

    String PLUGIN_SYMBOL_HEAVY_CHECK_MARK = PLUGIN_COLOR_POSITIVE + "✔ ";
    String PLUGIN_SYMBOL_BLACK_LARGE_CIRCLE = PLUGIN_COLOR_NEUTRAL + "⬤ ";
    String PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X = PLUGIN_COLOR_NEGATIVE + "✖ ";
    String PLUGIN_SYMBOL_RIGHT_POINTING_ANGLE_BRACKET = PLUGIN_COLOR_POSITIVE + "〉";
    String PLUGIN_SYMBOL_LEFT_POINTING_ANGLE_BRACKET = PLUGIN_COLOR_NEGATIVE + "〈";
    String PLUGIN_SYMBOL_CROSS_MARK = PLUGIN_COLOR_NEGATIVE + "❌";
    String PLUGIN_SYMBOL_BLACK_FOUR_POINTED_STAR = PLUGIN_COLOR_POSITIVE + "✦";

    String PLUGIN_SIGN_NAME = PLUGIN_COLOR_MESSAGE + "[" + PLUGIN_NAME_SHORT + "]";
    String PLUGIN_SIGN_CLICK = PLUGIN_COLOR_MESSAGE + "[Click here]";

    String PLUGIN_INTERNAL_UTILITY_CLASS = "This is a Utility Class";

    /* ******************************************************************************* */
    /*                              COMMAND   STUFF                                    */
    /* ******************************************************************************* */

    String PLUGIN_COMMAND_WORLD_NOT_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "Die Welt " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " existiert nicht";

    String PLUGIN_COMMAND_SPEED = PLUGIN_FORMS_COMMAND_PREFIX + "Geschwindigkeit wurde auf " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " gesetzt";
    String PLUGIN_COMMAND_SPEED_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Nutze " + PLUGIN_COLOR_COMMAND_ARG + "/speed " + "<" + PLUGIN_COLOR_COMMAND_ARG + "0-10" + PLUGIN_COLOR_COMMAND + ">";

    String PLUGIN_COMMAND_INVALID = PLUGIN_FORMS_COMMAND_PREFIX + "Invalid Data!";
    String PLUGIN_COMMAND_PERMISSION_MISSING = PLUGIN_FORMS_COMMAND_PREFIX + "Dafür hast du leider keine Rechte!";
    String PLUGIN_COMMAND_NOT_A_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "Du bist leider kein Spieler!";
    String PLUGIN_COMMAND_TO_MANY_ARGUMENTS = PLUGIN_FORMS_COMMAND_PREFIX + "Zu viele Argumente!";
    String PLUGIN_COMMAND_TARGET_NOT_A_PLAYER = PLUGIN_FORMS_COMMAND_PREFIX + "%s" + PLUGIN_COLOR_COMMAND + " ist kein Spieler!";

    String PLUGIN_COMMAND_SIGN_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Use " + PLUGIN_COLOR_COMMAND_NAME + "/%s" + " " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_MESSAGE + " or " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    String PLUGIN_COMMAND_SIGN_COPY = PLUGIN_FORMS_COMMAND_PREFIX + "Click the Sign you want to copy";
    String PLUGIN_COMMAND_SIGN_EDIT = PLUGIN_FORMS_COMMAND_PREFIX + "Click the Sign you want to edit";

    String PLUGIN_COMMAND_EXIT_KICK_MESSAGE = PLUGIN_COLOR_COMMAND + "exited.";
    String PLUGIN_COMMAND_EXIT_SERVER_SHUTTING_DOWN = PLUGIN_COLOR_COMMAND + "Server is shutting down...";

    String PLUGIN_BANK_INTEREST_NEXT_RUN = PLUGIN_COLOR_COMMAND + "Next Interest Payment Run in: " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " seconds!";

    String PLUGIN_COMMAND_CUSTOMHEADS_TITLE = PLUGIN_FORMS_COMMAND_PREFIX + "Heads";

    String PLUGIN_COMMAND_LIST_HEADER = PLUGIN_FORMS_COMMAND_PREFIX + "Online Players " + PLUGIN_COLOR_MESSAGE_SPACER + "[" + PLUGIN_COLOR_NEUTRAL + "%s" + PLUGIN_COLOR_MESSAGE_SPACER + "]" + PLUGIN_COLOR_MESSAGE + ":";
    String PLUGIN_COMMAND_LIST_ENTRY = PLUGIN_FORMS_WHITESPACE_SHORT + PLUGIN_COLOR_MESSAGE_SPACER + "- " + "%s%s " + PLUGIN_COLOR_MESSAGE_SPACER + "[" + "%s%s" + PLUGIN_COLOR_MESSAGE_SPACER + "]";

    String PLUGIN_ITEM_SELL_PRICE_MESSAGE = PLUGIN_COLOR_NEGATIVE + "Sell: " + PLUGIN_NAME_MONEY + " per Item: %s " + PLUGIN_NAME_MONEY + " per Stack: %s";
    String PLUGIN_ITEM_BUY_PRICE_MESSAGE = PLUGIN_COLOR_POSITIVE + "Buy: " + PLUGIN_NAME_MONEY + " per Item: %s " + PLUGIN_NAME_MONEY + " per Stack: %s";

    String PLUGIN_POSITION_HIGHLIGHTING_DIFFERENT_WORLDS = PLUGIN_FORMS_COMMAND_PREFIX + "Positions are in different worlds, cannot highlight.";
}