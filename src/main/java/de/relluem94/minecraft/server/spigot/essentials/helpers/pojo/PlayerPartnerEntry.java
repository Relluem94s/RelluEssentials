package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class PlayerPartnerEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_FIRST_PARTNER_FK = "first_partner_fk";
    public static final String FIELD_SECOND_PARTNER_FK = "second_partner_fk";
    public static final String FIELD_SHARE_PROTECTIONS = "shareProtections";

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private boolean shareProtections;
    private int firstPartnerFK;
    private int secondPartnerFK;

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

    public boolean getShareProtections() {
        return shareProtections;
    }

    public void setShareProtections(boolean shareProtections) {
        this.shareProtections = shareProtections;
    }

    public int getFirstPlayerID() {
        return firstPartnerFK;
    }

    public void setFirstPlayerID(int firstPartnerFK) {
        this.firstPartnerFK = firstPartnerFK;
    }

    public int getSecondPlayerID() {
        return secondPartnerFK;
    }

    public void setSecondPlayerID(int secondPartnerFK) {
        this.secondPartnerFK = secondPartnerFK;
    }

}