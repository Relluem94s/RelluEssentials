package de.relluem94.minecraft.server.spigot.essentials.constants;

import lombok.Getter;

@Getter
public enum MessageKey {
    PLUGIN_MANAGER_START_MESSAGE("plugin.manager.start_message"),
    PLUGIN_MANAGER_STOP_MESSAGE("plugin.manager.stop_message"),
    PLUGIN_MANAGER_START_TIME_MESSAGE("plugin.manager.start_time_message"),
    PLUGIN_MANAGER_REGISTER_SKILLS("plugin.manager.register_skills"),
    PLUGIN_MANAGER_SKILLS_REGISTERED("plugin.manager.skills_registered"),
    PLUGIN_MANAGER_LOADING_CONFIGS("plugin.manager.loading_configs"),
    PLUGIN_MANAGER_CONFIGS_LOADED("plugin.manager.configs_loaded"),
    PLUGIN_MANAGER_REGISTER_EVENTS("plugin.manager.register_events"),
    PLUGIN_MANAGER_EVENTS_REGISTERED("plugin.manager.events_registered"),
    PLUGIN_MANAGER_REGISTER_RECIPE("plugin.manager.register_recipe"),
    PLUGIN_MANAGER_RECIPE_REGISTERED("plugin.manager.recipe_registered"),
    PLUGIN_MANAGER_REGISTER_COMMANDS("plugin.manager.register_commands"),
    PLUGIN_MANAGER_COMMANDS_REGISTERED("plugin.manager.commands_registered"),
    PLUGIN_MANAGER_REGISTER_AUTOSAVE("plugin.manager.register_autosave"),
    PLUGIN_MANAGER_AUTOSAVE_REGISTERED("plugin.manager.autosave_registered"),
    PLUGIN_FOLDER_MKDIR_ERROR("plugin.folder.mkdir_error"),


    COMMAND_INVALID("command.invalid"),
    COMMAND_PERMISSION_MISSING("command.permission_missing"),
    COMMAND_NOT_A_PLAYER("command.not_a_player"),
    COMMAND_TO_LESS_ARGUMENTS("command.to_less_arguments"),
    COMMAND_TO_MANY_ARGUMENTS("command.to_many_arguments"),
    COMMAND_WRONG_SUB_COMMAND("command.wrong_sub_command"),

    COMMAND_FLYMODE("command.flymode"),
    COMMAND_FLYMODE_ACTIVATED("command.flymode.activated"),
    COMMAND_FLYMODE_DEACTIVATED("command.flymode.deactivated"),

    COMMAND_GAMEMODE("command.gamemode"),
    COMMAND_GAMEMODE_SURVIVAL("command.gamemode.survival"),
    COMMAND_GAMEMODE_CREATIVE("command.gamemode.creative"),
    COMMAND_GAMEMODE_ADVENTURE("command.gamemode.adventure"),
    COMMAND_GAMEMODE_SPECTATOR("command.gamemode.spectator"),

    COMMAND_AFK_ACTIVATED("command.afk.activated"),
    COMMAND_AFK_DEACTIVATED("command.afk.deactivated"),

    COMMAND_HOME("command.home"),
    COMMAND_HOME_TP("command.home.tp"),
    COMMAND_HOME_NONE("command.home.none"),
    COMMAND_HOME_SET("command.home.set"),
    COMMAND_HOME_DELETE("command.home.delete"),
    COMMAND_HOME_NOT_FOUND("command.home.not_found"),
    COMMAND_HOME_EXISTS("command.home.exists"),
    COMMAND_HOME_RESERVED("command.home.reserved"),
    COMMAND_HOME_LIST("command.home.list"),
    COMMAND_HOME_LIST_NAME("command.home.list_name"),
    COMMAND_HOME_LIST_DEATHPOINTS("command.home.list_deathpoints"),
    COMMAND_HOME_LIST_DEATHPOINTS_NAME("command.home.list_deathpoints_name"),
    COMMAND_HOME_DEATH_DELETE("command.home.death_delete"),
    COMMAND_HOME_NO_BED("command.home.no_bed"),

    COMMAND_TP("command.tp"),
    COMMAND_TP_TO("command.tp.to"),
    COMMAND_TP_SEND_REQUEST("command.tp.send_request"),
    COMMAND_TP_ACCEPT_NO_REQUEST("command.tp.accept_no_request"),
    COMMAND_TP_REQUEST_EXPIRED("command.tp.request_expired"),

    COMMAND_MARRY_SEND_REQUEST("command.marry.send_request"),
    COMMAND_MARRY_RECEIVE_REQUEST("command.marry.receive_request"),
    COMMAND_MARRY_MARRIED("command.marry.married"),
    COMMAND_MARRY_DIVORCED("command.marry.divorced"),
    COMMAND_MARRY_SELF_MARRIAGE("command.marry.self_marriage"),
    COMMAND_MARRY_DIVORCE_NOT_MARRIED("command.marry.divorce_not_married"),
    COMMAND_MARRY_ACCEPT_NO_REQUEST("command.marry.accept_no_request"),

    COMMAND_WARP("command.warp"),
    COMMAND_WARP_LIST_INFO("command.warp.list_info"),
    COMMAND_WARP_ERROR_WORLD_UNLOADED("command.warp.error_world_unloaded"),
    COMMAND_WARP_ERROR_NO_WARP_FOUND("command.warp.error_no_warp_found"),
    COMMAND_WARP_ADD("command.warp.add"),
    COMMAND_WARP_REMOVE("command.warp.remove");


    private final String key;
    MessageKey(String key) {
        this.key = key;
    }
}
