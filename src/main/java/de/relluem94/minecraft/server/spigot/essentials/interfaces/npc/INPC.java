package de.relluem94.minecraft.server.spigot.essentials.interfaces.npc;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.TraderNPC.Type;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

public interface INPC {
    String getName();
    String getTitle();
    ItemHelper getItemHelper();
    Profession getProfession();
    Type getType();
    Inventory getMainGUI();
}
