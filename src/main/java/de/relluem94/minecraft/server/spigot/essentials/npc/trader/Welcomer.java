package de.relluem94.minecraft.server.spigot.essentials.npc.trader;

import de.relluem94.minecraft.server.spigot.essentials.npc.trader.interfaces.IChat;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

public class Welcomer extends TraderNPC implements IChat {

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