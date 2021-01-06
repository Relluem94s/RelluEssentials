package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */

public class PlayerEntry {
    public PlayerEntry(){}
    
    private int id;
    private String created;
    private int createdby;
    private String updated;
    private int updatedby;
    private String deleted;
    private int deletedby;
    private String uuid;
    private int groupID;
    private boolean afk;
    private boolean fly;
    private String customname;
    private HomeEntry[] homes;
    
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

    public String getUUID() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int group_id) {
        this.groupID = group_id;
    }

    public boolean isAfk() {
        return afk;
    }

    public void setAfk(boolean afk) {
        this.afk = afk;
    }

    public boolean isFly() {
        return fly;
    }

    public void setFly(boolean fly) {
        this.fly = fly;
    }

    public String getCustomname() {
        return customname;
    }

    public void setCustomname(String customname) {
        this.customname = customname;
    }

    public HomeEntry[] getHomes() {
        return homes;
    }

    public void setHomes(HomeEntry[] homes) {
        this.homes = homes;
    }
}
