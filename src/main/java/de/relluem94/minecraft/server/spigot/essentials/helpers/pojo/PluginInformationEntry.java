package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class PluginInformationEntry {
    private int id;
    private String created;
    private int createdby;
    private String updated;
    private int updatedby;
    private String deleted;
    private int deletedby;
    private String tab_header;
    private String tab_footer;
    private String motd_message;
    private int motd_players;
    private int db_version;
    
    public PluginInformationEntry(){}

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
        return tab_header;
    }

    public void setTabheader(String tab_header) {
        this.tab_header = tab_header;
    }

    public String getTabfooter() {
        return tab_footer;
    }

    public void setTabfooter(String tab_footer) {
        this.tab_footer = tab_footer;
    }

    public String getMotdMessage() {
        return motd_message;
    }

    public void setMotdMessage(String motd_message) {
        this.motd_message = motd_message;
    }

    public int getMotdPlayers() {
        return motd_players;
    }

    public void setMotdPlayers(int motd_players) {
        this.motd_players = motd_players;
    }

    public int getDbVersion() {
        return db_version;
    }

    public void setDbVersion(int db_version) {
        this.db_version = db_version;
    }
    
    
}
