package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */

@Getter
@Setter
public class BagTypeEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DISPLAY_NAME = "displayname";
    public static final String FIELD_COST = "cost";
    public static final String FIELD_SLOT_VAR_NAME = "slot_%s_name";
    
    private int id;
    private String displayName;
    private String name;
    private int cost;
    private String[] slotNames;

    public BagTypeEntry(){
        slotNames = new String[BagHelper.BAG_SIZE];
    }

    public String getSlotName(int slot) {
        return slotNames[slot];
    }

    public void setSlotName(int slot, String name){
        slotNames[slot] = name;
    }
}
