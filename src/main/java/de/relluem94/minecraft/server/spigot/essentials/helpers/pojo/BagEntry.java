package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;

/**
 *
 * @author rellu
 */

public class BagEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_BAG_TYPE_FK = "bag_type_fk";
    public static final String FIELD_PLAYER_FK = "player_fk";
    public static final String FIELD_SLOT_VAR_VALUE = "slot_%s_value";

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;


    private int playerFK;
    private int bagTypeFK;


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
        return playerFK;
    }

    public void setPlayerId(int playerId) {
        this.playerFK = playerId;
    }

    public int getBagTypeId() {
        return bagTypeFK;
    }

    public void setBagTypeId(int bagTypeFK) {
        this.bagTypeFK = bagTypeFK;
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
