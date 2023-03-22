package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.npc.BagSalesman;
import de.relluem94.minecraft.server.spigot.essentials.npc.Banker;
import de.relluem94.minecraft.server.spigot.essentials.npc.Beekeeper;
import de.relluem94.minecraft.server.spigot.essentials.npc.Enchanter;

public class NPCManager implements IEnable {

    @Override
    public void enable() {
        new BagSalesman();
        RelluEssentials.setBanker(new Banker());
        new Beekeeper();
        new Enchanter();
    }
    
}
