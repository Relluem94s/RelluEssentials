package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class PlayerPartnerEntry {
    // `created`,`createdby`,`first_partner_fk`,`first_partner_fk`,`shareProtections`
    public PlayerPartnerEntry() {}

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private boolean shareProtections;
    private int first_partner_fk;
    private int second_partner_fk;

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
        return first_partner_fk;
    }

    public void setFirstPlayerID(int first_partner_fk) {
        this.first_partner_fk = first_partner_fk;
    }

    public int getSecondPlayerID() {
        return second_partner_fk;
    }

    public void setSecondPlayerID(int second_partner_fk) {
        this.second_partner_fk = second_partner_fk;
    }

}