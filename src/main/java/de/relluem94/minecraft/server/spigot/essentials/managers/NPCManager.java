package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.BagSalesman;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.Banker;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.Beekeeper;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.Enchanter;

public class NPCManager implements IEnable {

    @Override
    public void enable() {
        new BagSalesman();
        RelluEssentials.setBanker(new Banker());
        new Beekeeper();
        new Enchanter();
    }
    
}
