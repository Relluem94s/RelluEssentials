package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class BankAccountEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_PLAYER_FK = "player_fk";
    public static final String FIELD_BANK_TIER_FK = "bank_tier_fk";

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private int playerFK;
    private double value;
    private BankTierEntry tier;

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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public BankTierEntry getTier() {
        return tier;
    }

    public void setTier(BankTierEntry tier) {
        this.tier = tier;
    }
}