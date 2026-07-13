package de.relluem94.minecraft.server.spigot.essentials.constants;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;

/**
 *
 * @author rellu
 */
public interface EventConstants {


    String PLUGIN_EVENT_SKULL_INFO_SPACER = "§8~~~~~~~~~~~~~~~~~~~~~~~";
    String PLUGIN_EVENT_NO_DEATH_MESSAGE = "death_%s";

    String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT = "E MMM d y hh:mm:ss a";

    String PLUGIN_EVENT_PROTECT_RIGHTS = "IDs";
    String PLUGIN_EVENT_PROTECT_FLAGS = "flags";

    String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE = PLUGIN_SYMBOL_RIGHT_POINTING_ANGLE_BRACKET + PLUGIN_COLOR_MESSAGE;
    String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE = PLUGIN_SYMBOL_LEFT_POINTING_ANGLE_BRACKET + PLUGIN_COLOR_MESSAGE;

    //==============================================================================//
    //                             EVENT   STUFF                                    //
    //==============================================================================//
    String PLUGIN_EVENT_JOIN_MESSAGE = PLUGIN_COLOR_POSITIVE + "[" + PLUGIN_SYMBOL_BLACK_FOUR_POINTED_STAR + "] " + PLUGIN_COLOR_MESSAGE + "%s" + PLUGIN_COLOR_COMMAND + " hat den Server betreten.";
    String PLUGIN_EVENT_QUIT_MESSAGE = PLUGIN_COLOR_NEGATIVE + "[" + PLUGIN_SYMBOL_CROSS_MARK + "] " + PLUGIN_COLOR_MESSAGE + "%s" + PLUGIN_COLOR_COMMAND + " hat den Server verlassen.";
    String PLUGIN_COMMAND_WHERE_STRING = PLUGIN_COLOR_COMMAND + "X: " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "Y: " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "Z: " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "Welt: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    String PLUGIN_EVENT_DEATH = PLUGIN_FORMS_COMMAND_PREFIX + "Du starbst bei " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + PLUGIN_COMMAND_WHERE_STRING + PLUGIN_COLOR_COMMAND;
    String PLUGIN_EVENT_DEATH_TP = PLUGIN_FORMS_COMMAND_PREFIX + "Klicke diese Nachricht um dich zum Todespunkt zu teleportieren!";



    String PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT = PLUGIN_FORMS_COMMAND_PREFIX + "No End Point found! Can't teleport.";
    String PLUGIN_EVENT_LIGHTS_TOOGLE = PLUGIN_FORMS_COMMAND_PREFIX + "You toogled the Light";
    String PLUGIN_EVENT_BAG_COLLECT = "Added %sx %s to your Bag";
    String INTEGRATION_REGISTERED = "Integration found: %s  " + PLUGIN_COLOR_COMMAND_ARG + "v%s";
    String INTEGRATION_UNREGISTERED = "Integration removed: ";
    String PLUGIN_GRAPPLING_HOOK_COOLDOWN = PLUGIN_COLOR_NEGATIVE + "Please Slow down..";

    String PLUGIN_EVENT_POSITION_AXE_FIRST_RESET = PLUGIN_FORMS_COMMAND_PREFIX + "First position reset.";
    String PLUGIN_EVENT_POSITION_AXE_SECOND_RESET = PLUGIN_FORMS_COMMAND_PREFIX + "Second position reset.";
    String PLUGIN_EVENT_POSITION_AXE_FIRST_SET = PLUGIN_FORMS_COMMAND_PREFIX + "First position set to " + PLUGIN_COLOR_COMMAND_ARG + "(%d, %d, %d)";
    String PLUGIN_EVENT_POSITION_AXE_SECOND_SET = PLUGIN_FORMS_COMMAND_PREFIX + "Second position set to " + PLUGIN_COLOR_COMMAND_ARG + "(%d, %d, %d)";
}