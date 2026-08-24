package de.relluem94.minecraft.server.spigot.essentials.constants;

public class Constants {

  private Constants() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static final String PLUGIN_EOL = System.lineSeparator();

  public static final String PLUGIN_COLOR_COMMAND = "§f";
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

  public static final String PLUGIN_NAME_RELLU = "Rellu";
  public static final String PLUGIN_NAME_ESSENTIALS = "Essentials";
  public static final String PLUGIN_NAME_INITIAL_RELLU = "R";
  public static final String PLUGIN_NAME_INITIAL_ESSENTIALS = "E";
  public static final String PLUGIN_NAME_SHORT =
      PLUGIN_COLOR_LOGO_RELLU + PLUGIN_NAME_INITIAL_RELLU + PLUGIN_COLOR_LOGO_ESSENTIALS
          + PLUGIN_NAME_INITIAL_ESSENTIALS + PLUGIN_COLOR_MESSAGE;
  public static final String PLUGIN_SIGN_NAME = PLUGIN_COLOR_MESSAGE + "[" + PLUGIN_NAME_SHORT + "]";
  public static final String PLUGIN_NAME_PREFIX =
      PLUGIN_COLOR_LOGO_RELLU + PLUGIN_NAME_RELLU + PLUGIN_COLOR_LOGO_ESSENTIALS
          + PLUGIN_NAME_ESSENTIALS + PLUGIN_COLOR_RESET + PLUGIN_COLOR_MESSAGE;
  public static final String PLUGIN_NAME_CONSOLE =
      PLUGIN_COLOR_MESSAGE + "[" + PLUGIN_NAME_PREFIX + PLUGIN_COLOR_MESSAGE + "] ";
  public static final String PLUGIN_NAME_BROADCAST = PLUGIN_COLOR_BROADCAST + "Broadcast";
  public static final String PLUGIN_NAME_CHAT_CONSOLE = PLUGIN_COLOR_CONSOLE + "Console";
  public static final String PLUGIN_NAME_MONEY = PLUGIN_COLOR_MONEY + "Coins" + PLUGIN_COLOR_MESSAGE;
  public static final String PLUGIN_WORLD_LOBBY = "lobby";
  public static final String PLUGIN_FORMS_SPACER_CHANNEL = " >> " + PLUGIN_COLOR_COMMAND;
  public static final String PLUGIN_FORMS_SPACER_MESSAGE = PLUGIN_COLOR_MESSAGE_SPACER + " >> " + PLUGIN_COLOR_MESSAGE;
  public static final String PLUGIN_FORMS_COMMAND_PREFIX =
      PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_COMMAND;
  public static final String PLUGIN_FORMS_BORDER = "<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>";
  public static final String PLUGIN_FORMS_SCOREBOARD_BORDER = "~*~*~*~*~*~*~*~*~*~*~*~*~*~";
  public static final String PLUGIN_FORMS_MSG_SPACER_IN = "§9 >> §f";
  public static final String PLUGIN_FORMS_MSG_SPACER_OUT = "§9 << §f";
  public static final String PLUGIN_SYMBOL_RIGHT_POINTING_ANGLE_BRACKET = PLUGIN_COLOR_POSITIVE + "〉";
  public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE =
      PLUGIN_SYMBOL_RIGHT_POINTING_ANGLE_BRACKET + PLUGIN_COLOR_MESSAGE;
  public static final String PLUGIN_SYMBOL_LEFT_POINTING_ANGLE_BRACKET = PLUGIN_COLOR_NEGATIVE + "〈";
  public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE =
      PLUGIN_SYMBOL_LEFT_POINTING_ANGLE_BRACKET + PLUGIN_COLOR_MESSAGE;
  public static final String PLUGIN_EVENT_SKULL_INFO_SPACER = "§8~~~~~~~~~~~~~~~~~~~~~~~";
  public static final String PLUGIN_EVENT_NO_DEATH_MESSAGE = "death_%s";
  public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT = "E MMM d y hh:mm:ss a";
  public static final String PLUGIN_EVENT_PROTECT_RIGHTS = "IDs";
  public static final String PLUGIN_EVENT_PROTECT_FLAGS = "flags";
  public static final String PLUGIN_SIGN_CLICK = PLUGIN_COLOR_MESSAGE + "[Click here]";

  public static final String PLUGIN_INTERNAL_UTILITY_CLASS = "This is a Utility Class";
}