package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.entity.Villager.Profession;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC.Type;

/**
 *
 * @author rellu
 */

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profession getProfession(){
        return profession;
    }

    public void setProfession(String profession){
        this.profession = Profession.valueOf(profession);
    }

    public void setProfession(Profession profession){
        this.profession = profession;
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

    public Type getType(){
        return type;
    }

    public void setType(String type){
        this.type = Type.valueOf(type);
    }

    public void setType(Type type){
        this.type = type;
    }
}