package de.relluem94.minecraft.server.spigot.essentials.constants;

import lombok.Getter;

@Getter
public enum MessageKey {
    PLUGIN_MANAGER_START_MESSAGE("plugin.manager.start_message"),
    PLUGIN_MANAGER_STOP_MESSAGE("plugin.manager.stop_message"),
    PLUGIN_MANAGER_START_TIME_MESSAGE("plugin.manager.start_time_message"),

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

    COMMAND_AFK_ACTIVATED("command.afk.activated"),
    COMMAND_AFK_DEACTIVATED("command.afk.deactivated"),

    COMMAND_HOME("command.home"),
    COMMAND_HOME_TP("command.home.tp"),
    COMMAND_HOME_NONE("command.home.none"),
    COMMAND_HOME_SET("command.home.set"),
    COMMAND_HOME_DELETE("command.home.delete"),
    COMMAND_HOME_NOT_FOUND("command.home.not_found"),
    COMMAND_HOME_EXISTS("command.home.exists"),

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
