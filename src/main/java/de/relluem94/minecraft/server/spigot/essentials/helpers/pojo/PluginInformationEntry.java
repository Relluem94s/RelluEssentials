package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class PluginInformationEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_TAB_HEADER = "tab_header";
    public static final String FIELD_TAB_FOOTER = "tab_footer";
    public static final String FIELD_MOTD_MESSAGE = "motd_message";
    public static final String FIELD_MOTD_PLAYERS = "motd_players";
    public static final String FIELD_DB_VERSION = "db_version";

    private int id;
    private String created;
    private int createdby;
    private String updated;
    private int updatedby;
    private String deleted;
    private int deletedby;
    private String tabHeader;
    private String tabFooter;
    private String motdMessage;
    private int motdPlayers;
    private int dbVersion;
}