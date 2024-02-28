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