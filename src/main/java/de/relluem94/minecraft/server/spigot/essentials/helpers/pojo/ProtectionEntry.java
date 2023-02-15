package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.json.JSONObject;

/**
 *
 * @author rellu
 */
public class ProtectionEntry {

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private LocationEntry location;
    private String material_name;

    private JSONObject flags;
    private JSONObject rights;

    public ProtectionEntry() {
    }

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

    public LocationEntry getLocation() {
        return location;
    }

    public void setLocationEntry(LocationEntry location) {
        this.location = location;
    }

    public String getMaterialName() {
        return material_name;
    }

    public void setMaterialName(String material_name) {
        this.material_name = material_name;
    }

    public JSONObject getRights() {
        return rights;
    }

    public void setRights(JSONObject rights) {
        this.rights = rights;
    }

    public JSONObject getFlags() {
        return flags;
    }

    public void setFlags(JSONObject flags) {
        this.flags = flags;
    }
}
