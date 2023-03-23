package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import java.util.ArrayList;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;

/**
 *
 * @author rellu
 */
public class PlayerEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CUSTOM_NAME = "customname";
    public static final String FIELD_PURSE = "purse";
    public static final String FIELD_FLY = "fly";
    public static final String FIELD_AFK = "afk";
    public static final String FIELD_GROUP_FK = "group_fk";
    public static final String FIELD_UUID = "uuid";

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private double purse;
    private String deleted;
    private int deletedBy;
    private String uuid;
    private GroupEntry group;
    private boolean afk;
    private boolean fly;
    private String name;
    private String customName;
    private List<LocationEntry> homes = new ArrayList<>();
    private List<LocationEntry> deaths = new ArrayList<>();
    private PlayerPartnerEntry partner;
    private PlayerState playerState;
    private Object playerStateParameter;

    private boolean hasToBeUpdated = false;

    public boolean hasToBeUpdated(){
        return hasToBeUpdated;
    }

    public void setToBeUpdated(boolean hasToBeUpdated){
        this.hasToBeUpdated = hasToBeUpdated;
    }

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

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public GroupEntry getGroup() {
        return group;
    }

    public void setGroup(GroupEntry group) {
        this.group = group;
    }

    public boolean isAFK() {
        return afk;
    }

    public void setAFK(boolean afk) {
        this.afk = afk;
    }

    public boolean isFlying() {
        return fly;
    }

    public void setFlying(boolean fly) {
        this.fly = fly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public List<LocationEntry> getHomes() {
        return homes;
    }

    public void setHomes(List<LocationEntry> homes) {
        this.homes = homes;
    }

    public List<LocationEntry> getDeaths() {
        return deaths;
    }

    public void setDeaths(List<LocationEntry> deaths) {
        this.deaths = deaths;
    }

    public double getPurse() {
        return purse;
    }

    public void setPurse(double value) {
        purse = value;
    }

    public PlayerState getPlayerState(){
        return playerState;
    }

    public void setPlayerState(PlayerState playerState){
        this.playerState = playerState;
    }

    public Object getPlayerStateParameter(){
        return this.playerStateParameter;
    }

    public void setPlayerStateParameter(Object o) {
        this.playerStateParameter = o;
    }

    public PlayerPartnerEntry getPartner() {
        return partner;
    }

    public void setPartner(PlayerPartnerEntry partner) {
        this.partner = partner;
    }

    public PlayerEntry(){
        
    }

    public PlayerEntry(PlayerEntry pe){
        PlayerEntry peCopy = new PlayerEntry();
        peCopy.setAFK(pe.afk);
        peCopy.setCreated(pe.created);
        peCopy.setCreatedby(pe.createdBy);
        peCopy.setCustomName(pe.customName);
        peCopy.setDeleted(pe.deleted);
        peCopy.setDeletedBy(pe.deletedBy);
        peCopy.setFlying(pe.fly);
        peCopy.setGroup(pe.group);
        peCopy.setHomes(pe.homes);
        peCopy.setID(pe.id);
        peCopy.setPlayerState(pe.playerState);
        peCopy.setPlayerStateParameter(pe.playerStateParameter);
        peCopy.setPurse(pe.purse);
        peCopy.setUUID(pe.uuid);
        peCopy.setUpdated(pe.updated);
        peCopy.setUpdatedBy(pe.updatedBy);
    }
}