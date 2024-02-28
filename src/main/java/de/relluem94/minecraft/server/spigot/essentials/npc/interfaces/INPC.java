package de.relluem94.minecraft.server.spigot.essentials.npc.interfaces;

import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC.Type;

public interface INPC {
    String getName();
    String getTitle();
    ItemHelper getItemHelper();
    Profession getProfession();
    Type getType();
    Inventory getMainGUI();
}
