package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */

@Setter
@Getter
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
    private int playerId;
    private int bagTypeId;
    private int[] slotValues;
    private BagTypeEntry bagType;
    private boolean hasToBeUpdated = false;

    public BagEntry(){
         slotValues = new int[BagHelper.BAG_SIZE];
    }

    public void setSlotValue(int slot, int value){
        this.slotValues[slot] = value;
    }

    public int getSlotValue(int slot) {
        return slotValues[slot];
    }
}