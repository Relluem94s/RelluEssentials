package de.relluem94.minecraft.server.spigot.essentials.npc;

import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.npc.interfaces.IChat;

public class Welcomer extends NPC implements IChat {

    public Welcomer() {
        super("Gustav", Profession.CARTOGRAPHER, Type.CHAT);
    }

    @Override
    public String[] getTexts() {
        return new String[]{"Welcome to this Server!", "Have fun digging"};
    }

    @Override
    public Inventory getMainGUI() {
        return null;
    }
}