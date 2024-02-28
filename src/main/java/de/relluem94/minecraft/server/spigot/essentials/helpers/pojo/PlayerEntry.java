package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import java.util.ArrayList;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
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
    private String deleted;
    private int deletedBy;
    private double purse;
    private String uuid;
    private GroupEntry group;
    private boolean afk;
    private boolean flying;
    private String name;
    private String customName;
    private List<LocationEntry> homes = new ArrayList<>();
    private List<LocationEntry> deaths = new ArrayList<>();
    private PlayerPartnerEntry partner;
    private PlayerState playerState;
    private Object playerStateParameter;

    private boolean hasToBeUpdated = false;

    public PlayerEntry(){}

    public PlayerEntry(PlayerEntry pe){
        setAfk(pe.isAfk());
        setCreated(pe.getCreated());
        setCreatedBy(pe.getCreatedBy());
        setUpdated(pe.getUpdated());
        setUpdatedBy(pe.getUpdatedBy());
        setCustomName(pe.getCustomName());
        setDeleted(pe.getDeleted());
        setDeletedBy(pe.getDeletedBy());
        setPurse(pe.getPurse());
        setUuid(pe.getUuid());
        setGroup(pe.getGroup());
        setAfk(pe.isAfk());

        setFlying(pe.isFlying());
        setHomes(pe.getHomes());
        setId(pe.getId());
        setPlayerState(pe.getPlayerState());
        setPlayerStateParameter(pe.getPlayerStateParameter());
    }
}