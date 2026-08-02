package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.BagSalesmanNpc;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.BeekeeperNpc;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.EnchanterNpc;

public class NpcManager implements Enable {

  @Override
  public void enable(RelluEssentials plugin) {
    new BagSalesmanNpc();
    RelluEssentials.setBanker(new BankerNpc());
    new BeekeeperNpc();
    new EnchanterNpc();
  }
}