package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;

/**
 *
 * @author rellu
 */

public class BagTypeEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DISPLAY_NAME = "displayname";
    public static final String FIELD_COST = "cost";
    public static final String FIELD_SLOT_VAR_NAME = "slot_%s_name";
    
    private int id;
    private String displayname;
    private String name;
    private int cost;
    private String[] slotNames;

    public BagTypeEntry(){
        slotNames = new String[BagHelper.BAG_SIZE];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayname;
    }

    public void setDisplayName(String displayname) {
        this.displayname = displayname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String[] getSlotNames() {
        return slotNames;
    }

    public String getSlotName(int slot) {
        return slotNames[slot];
    }

    public void setSlotName(int slot, String name){
        slotNames[slot] = name;
    }
}
