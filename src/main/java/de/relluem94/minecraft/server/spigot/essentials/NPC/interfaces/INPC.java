package de.relluem94.minecraft.server.spigot.essentials.NPC.interfaces;

import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.NPC.NPC.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public interface INPC {
    public String getName();
    public String getTitle();
    public ItemHelper getItemHelper();
    public Profession getProfession();
    public Type getType();
    public Inventory getMainGUI();
}
