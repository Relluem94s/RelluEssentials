package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.json.JSONObject;

/**
 *
 * @author rellu
 */

public class WorldGroupInventoryEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_PLAYER_FK = "player_fk";
    public static final String FIELD_HEALTH = "health";
    public static final String FIELD_TOTAL_EXPERIENCE = "totalExperience";
    public static final String FIELD_FOOD = "food";
    public static final String FIELD_INVENTORY = "inventory";

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private WorldGroupEntry worldGroupEntry;
    private int playerFK;
    private JSONObject inventory;
    private double health;
    private int food;
    private int totalExperience;

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

    public void setWorldGroup(WorldGroupEntry wge){
        this.worldGroupEntry = wge;
    }

    public WorldGroupEntry getWorldGroupEntry(){
        return worldGroupEntry;
    }

    public int getPlayerId() {
        return playerFK;
    }

    public void setPlayerId(int playerId) {
        this.playerFK = playerId;
    }

    public void setInventory(JSONObject inv){
        this.inventory = inv;
    }

    public JSONObject getInventory(){
        return this.inventory;
    }

    public int getFoodLevel(){
        return food;
    }

    public void setFoodLevel(int level){
        this.food = level;
    }

    public int getTotalExperience(){
        return totalExperience;
    }

    public void setTotalExperience(int level){
        this.totalExperience = level;
    }

    public double getHealth(){
        return health;
    }

    public void setHealth(double health){
        this.health = health;
    }

}