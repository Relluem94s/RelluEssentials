package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.NPC.BagSalesman;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Banker;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Beekeeper;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Enchanter;

public class NPCManager implements IEnable {

    @Override
    public void enable() {
        new BagSalesman();
        RelluEssentials.banker = new Banker();
        new Beekeeper();
        new Enchanter();
    }
    
}
