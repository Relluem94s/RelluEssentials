package de.relluem94.minecraft.server.spigot.essentials.npc.interfaces;

import org.bukkit.inventory.Inventory;

public interface IBanker {
    Inventory getDepositGUI(double total);
    Inventory getWithdrawGUI(double total);
    Inventory getBalanceGUI();
    Inventory getUpgradeGUI();
}
