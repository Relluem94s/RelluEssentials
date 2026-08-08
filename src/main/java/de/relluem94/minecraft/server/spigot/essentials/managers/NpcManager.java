package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BagSalesmanNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BeekeeperNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.EnchanterNpc;
import org.bukkit.plugin.Plugin;

public class NpcManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    relluEssentialsPlugin.getServiceContext().getTraderNpcRegistry()
        .addNPC(new BagSalesmanNpc(relluEssentialsPlugin.getServiceContext()));
    BankerNpc bankerNpc = new BankerNpc(relluEssentialsPlugin.getServiceContext());

    relluEssentialsPlugin.getServiceContext().setBankerNpc(bankerNpc);
    relluEssentialsPlugin.getServiceContext().getTraderNpcRegistry().addNPC(bankerNpc);

    relluEssentialsPlugin.getServiceContext().getTraderNpcRegistry()
        .addNPC(new BeekeeperNpc(relluEssentialsPlugin.getServiceContext()));

    relluEssentialsPlugin.getServiceContext().getTraderNpcRegistry()
        .addNPC(new EnchanterNpc(relluEssentialsPlugin.getServiceContext()));
  }
}