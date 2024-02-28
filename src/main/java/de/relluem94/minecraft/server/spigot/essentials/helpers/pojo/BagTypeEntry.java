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
