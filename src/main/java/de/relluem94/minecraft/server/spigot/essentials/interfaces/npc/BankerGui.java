package de.relluem94.minecraft.server.spigot.essentials.interfaces.npc;

import org.bukkit.inventory.Inventory;

public interface BankerGui {

  Inventory getDepositGUI(double total);

  Inventory getWithdrawGUI(double total);

  Inventory getBalanceGUI();

  Inventory getUpgradeGUI();
}
