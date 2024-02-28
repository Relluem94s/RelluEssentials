package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Villager.Profession;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC.Type;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class NPCEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PROFESSION = "profession";
    public static final String FIELD_TYPE = "type";

    public static final String FIELD_SLOT_VAR_NAME = "slot_%s_name";

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private String name;
    private Profession profession;
    private String[] slotNames;
    private Type type;

    public NPCEntry(){
        slotNames = new String[NPCHelper.INV_SIZE - InventoryHelper.getSkipsSize()];
    }

    public String getSlotName(int slot) {
        return slotNames[slot];
    }

    public void setSlotName(int slot, String name){
        slotNames[slot] = name;
    }
}