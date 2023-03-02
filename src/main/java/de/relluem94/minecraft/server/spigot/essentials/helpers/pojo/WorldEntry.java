package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;


/**
 *
 * @author rellu
 */

public class WorldEntry {

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private String name;
    private WorldGroupEntry worldGroupEntry;
    private GroupEntry groupEntry;

    public WorldEntry(){}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorldGroup(WorldGroupEntry wge){
        this.worldGroupEntry = wge;
    }

    public WorldGroupEntry getWorldGroupEntry(){
        return worldGroupEntry;
    }

    public void setBuildGroup(GroupEntry ge){
        this.groupEntry = ge;
    }

    public GroupEntry getBuildGroup(){
        return groupEntry;
    }
}