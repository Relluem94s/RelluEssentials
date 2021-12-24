package de.relluem94.minecraft.server.spigot.essentials.constants;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ARG_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WHERE_STRING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_MESSAGE_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_SPACER;

/**
 *
 * @author rellu
 */
public class EventConstants {

    //==============================================================================//
    //                             EVENT   STUFF                                    //
    //==============================================================================//
    public static final String PLUGIN_EVENT_JOIN_MESSAGE = "§2[\u2726] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server betreten.";
    public static final String PLUGIN_EVENT_QUIT_MESSAGE = "§4[\u274C] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server verlassen.";
    public static final String PLUGIN_EVENT_DEATH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du starbst bei " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + PLUGIN_COMMAND_WHERE_STRING + PLUGIN_COMMAND_COLOR;
    public static final String PLUGIN_EVENT_DEATH_TP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Klicke diese Nachricht um dich zum Todespunkt zu teleportieren!";

    public static final String PLUGIN_EVENT_SKILL_REPAIR_DONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast den Gegenstand repariert!";
    public static final String PLUGIN_EVENT_SKILL_REPAIR_WARNING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du benötigst mehr" + PLUGIN_COMMAND_ARG_COLOR + " %s " + PLUGIN_COMMAND_COLOR + "um diesen Gegenstand zu reparieren!";
    public static final String PLUGIN_EVENT_SKILL_SALVAGE_DONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast" + PLUGIN_COMMAND_ARG_COLOR + " %s " + PLUGIN_COMMAND_COLOR + "erhalten!";
    
    public static final String PLUGIN_EVENT_SKULL_INFO_SPACER = "§8~~~~~~~~~~~~~~~~~~~~~~~";
}
