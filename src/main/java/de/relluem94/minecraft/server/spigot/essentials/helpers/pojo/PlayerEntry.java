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
        setName(pe.getName());
        setCustomName(pe.getCustomName());
        setHasToBeUpdated(pe.isHasToBeUpdated());
        setDeaths(pe.getDeaths());
        setFlying(pe.isFlying());
        setHomes(pe.getHomes());
        setId(pe.getId());
        setPlayerState(pe.getPlayerState());
        setPlayerStateParameter(pe.getPlayerStateParameter());
        setPartner(pe.getPartner());
    }
}