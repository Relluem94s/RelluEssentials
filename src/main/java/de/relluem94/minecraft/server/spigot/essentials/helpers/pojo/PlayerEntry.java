package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class PlayerEntry {

    public PlayerEntry() {}

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private String uuid;
    private GroupEntry group;
    private boolean afk;
    private boolean fly;
    private String customName;
    private LocationEntry[] homes;

    public int getID() {
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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedby(int createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeleted() {
        return deleted;
    }

    public int getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(int deletedBy) {
        this.deletedBy = deletedBy;
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

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public LocationEntry[] getHomes() {
        return homes;
    }

    public void setHomes(LocationEntry[] homes) {
        this.homes = homes;
    }
}