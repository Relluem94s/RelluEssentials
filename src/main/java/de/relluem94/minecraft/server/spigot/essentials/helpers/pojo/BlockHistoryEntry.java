package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class BlockHistoryEntry {
    private int id;
    private LocationEntry location;
    private String material;
    private String created;
    private int createdby;
    private String updated;
    private int updatedby;
    private String deleted;
    private int deletedby;

    public BlockHistoryEntry() {}

    public int getId() {
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }

    public LocationEntry getLocation() {
        return location;
    }

    public void setLocation(LocationEntry location) {
        this.location = location;
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    
    
}
