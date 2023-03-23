package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public String getUpdated() {
        return updated;
    }

    public int getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(int updatedby) {
        this.updatedby = updatedby;
    }

    public String getDeleted() {
        return deleted;
    }

    public int getDeletedby() {
        return deletedby;
    }

    public void setDeletedby(int deletedby) {
        this.deletedby = deletedby;
    }

    public String getTabheader() {
        return tabHeader;
    }

    public void setTabheader(String tabHeader) {
        this.tabHeader = tabHeader;
    }

    public String getTabfooter() {
        return tabFooter;
    }

    public void setTabfooter(String tabFooter) {
        this.tabFooter = tabFooter;
    }

    public String getMotdMessage() {
        return motdMessage;
    }

    public void setMotdMessage(String motdMessage) {
        this.motdMessage = motdMessage;
    }

    public int getMotdPlayers() {
        return motdPlayers;
    }

    public void setMotdPlayers(int motdPlayers) {
        this.motdPlayers = motdPlayers;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

}
