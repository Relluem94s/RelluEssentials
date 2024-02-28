package de.relluem94.minecraft.server.spigot.essentials.npc.interfaces;

import org.bukkit.inventory.Inventory;

public interface IBanker {
    Inventory getDepositGUI();
    Inventory getWithdrawGUI();
    Inventory getBalanceGUI();
    Inventory getUpgradeGUI();
}
