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

    String PLUGIN_EVENT_POSITION_AXE_FIRST_RESET = PLUGIN_FORMS_COMMAND_PREFIX + "First position reset.";
    String PLUGIN_EVENT_POSITION_AXE_SECOND_RESET = PLUGIN_FORMS_COMMAND_PREFIX + "Second position reset.";
    String PLUGIN_EVENT_POSITION_AXE_FIRST_SET = PLUGIN_FORMS_COMMAND_PREFIX + "First position set to " + PLUGIN_COLOR_COMMAND_ARG + "(%d, %d, %d)";
    String PLUGIN_EVENT_POSITION_AXE_SECOND_SET = PLUGIN_FORMS_COMMAND_PREFIX + "Second position set to " + PLUGIN_COLOR_COMMAND_ARG + "(%d, %d, %d)";
}