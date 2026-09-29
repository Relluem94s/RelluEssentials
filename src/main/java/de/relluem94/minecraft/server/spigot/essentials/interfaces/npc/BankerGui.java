package de.relluem94.minecraft.server.spigot.essentials.interfaces.npc;

import org.bukkit.inventory.Inventory;

/**
 * Defines the contract for creating GUI inventories used by the Banker NPC.
 *
 * @author rellu
 * @version 4.0
 * @since 2.0
 */
public interface BankerGui {

  /**
   * Creates and returns the deposit GUI inventory for the Banker NPC.
   *
   * @param total the current total balance of the player
   * @return the deposit {@link Inventory}
   */
  Inventory getDepositGui(double total);

  /**
   * Creates and returns the withdrawal GUI inventory for the Banker NPC.
   *
   * @param total the current total balance of the player
   * @return the withdraw {@link Inventory}
   */
  Inventory getWithdrawGui(double total);

  /**
   * Creates and returns the balance GUI inventory for the Banker NPC.
   *
   * @return the balance {@link Inventory}
   */
  Inventory getBalanceGui();

  /**
   * Creates and returns the upgrade GUI inventory for the Banker NPC.
   *
   * @return the upgrade {@link Inventory}
   */
  Inventory getUpgradeGui();
}
