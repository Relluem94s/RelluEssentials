package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class PlayerEntry {

    public PlayerEntry() {
    }

    private int id;
    private String created;
    private int createdby;
    private String updated;
    private int updatedby;
    private String deleted;
    private int deletedby;
    private String uuid;
    private GroupEntry group;
    private boolean afk;
    private boolean fly;
    private String customname;
    private LocationEntry[] homes;

    public int getId() {
        return id;
    }

    public void setID(int id) {
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

    public void setUpdated(String updated) {
        this.updated = updated;
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

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public GroupEntry getGroup() {
        return group;
    }

    public void setGroup(GroupEntry group) {
        this.group = group;
    }

    public boolean isAFK() {
        return afk;
    }

    public void setAFK(boolean afk) {
        this.afk = afk;
    }

    public boolean isFlying() {
        return fly;
    }

    public void setFlying(boolean fly) {
        this.fly = fly;
    }

    public String getCustomname() {
        return customname;
    }

    public void setCustomname(String customname) {
        this.customname = customname;
    }

    public LocationEntry[] getHomes() {
        return homes;
    }

    public void setHomes(LocationEntry[] homes) {
        this.homes = homes;
    }
}
