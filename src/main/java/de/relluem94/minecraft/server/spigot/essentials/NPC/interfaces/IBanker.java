package de.relluem94.minecraft.server.spigot.essentials.NPC.interfaces;

import org.bukkit.inventory.Inventory;

public interface IBanker {
    public Inventory getDepositGUI();
    public Inventory getWithdrawGUI();
    public Inventory getBalanceGUI();
    public Inventory getUpgradeGUI();
}
