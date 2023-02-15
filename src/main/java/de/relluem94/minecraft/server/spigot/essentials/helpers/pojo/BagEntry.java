package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;

/**
 *
 * @author rellu
 */

public class BagEntry {

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;


    private int player_fk;
    private int bag_type_fk;


    private int[] slotValues;
    private BagTypeEntry bte;

    private boolean hasToBeUpdated = false;

    public BagEntry(){
         slotValues = new int[BagHelper.BAG_SIZE];
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

    public int getPlayerId() {
        return player_fk;
    }

    public void setPlayerId(int playerId) {
        this.player_fk = playerId;
    }

    public int getBagTypeId() {
        return bag_type_fk;
    }

    public void setBagTypeId(int bag_type_fk) {
        this.bag_type_fk = bag_type_fk;
    }

    public BagTypeEntry getBagType(){
        return bte;
    }

    public void setBagType(BagTypeEntry bte){
        this.bte = bte;
    }

    public int[] getSlotValues() {
        return slotValues;
    }
    
    public void setSlotValues(int[] slotValues) {
        this.slotValues = slotValues;
    }

    public void setSlotValue(int slot, int value){
        this.slotValues[slot] = value;
    }

    public int getSlotValue(int slot) {
        return slotValues[slot];
    }

    public boolean hasToBeUpdated(){
        return hasToBeUpdated;
    }

    public void setToBeUpdated(boolean hasToBeUpdated){
        this.hasToBeUpdated = hasToBeUpdated;
    }
}
