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