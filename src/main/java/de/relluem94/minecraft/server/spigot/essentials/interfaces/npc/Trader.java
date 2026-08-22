package de.relluem94.minecraft.server.spigot.essentials.interfaces.npc;

import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc.Type;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

public interface Trader {

  String getName();

  String getTitle();

  CustomItem getCustomItem();

  Profession getProfession();

  Type getType();

  Inventory getMainGUI();
}
