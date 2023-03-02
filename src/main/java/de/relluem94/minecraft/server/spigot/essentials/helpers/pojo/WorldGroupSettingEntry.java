package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.json.JSONObject;

import de.relluem94.rellulib.stores.DoubleStore;

/**
 *
 * @author rellu
 */

public class WorldGroupSettingEntry {

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private String name;
    private WorldGroupEntry worldGroupEntry;
    private int setting_fk;
    private JSONObject value;

    public WorldGroupSettingEntry(){

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

    public void setSetting(int setting){
        this.setting_fk = setting;
        this.value = null;
        DoubleStore ds = new DoubleStore(setting_fk, value);
        ds.toString(); // TODO remove this nonsense and add pojo for setting
    }
}