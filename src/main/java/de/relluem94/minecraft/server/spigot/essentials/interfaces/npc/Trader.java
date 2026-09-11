package de.relluem94.minecraft.server.spigot.essentials.interfaces.npc;

import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc.Type;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

/**
 * Represents a trader NPC that can be spawned in the world and interacted with by players.
 * A trader provides a GUI-based shop interface and is associated with a villager
 * profession and functional type.
 *
 * @author rellu
 */
public interface Trader {

  /**
   * Returns the name of this trader NPC.
   *
   * @return the name of the trader NPC
   */
  String getName();

  /**
   * Returns the formatted title of this trader NPC, prefixed with the plugin name.
   *
   * @return the full display title of the trader NPC
   */
  String getTitle();

  /**
   * Returns the custom spawn item associated with this trader NPC.
   *
   * @return the {@link CustomItem} used to spawn this NPC
   */
  CustomItem getCustomItem();

  /**
   * Returns the villager profession of this trader NPC.
   *
   * @return the {@link org.bukkit.entity.Villager.Profession} of this NPC
   */
  Profession getProfession();

  /**
   * Returns the functional type of this trader NPC.
   *
   * @return the {@link Type} of this trader NPC
   */
  Type getType();

  /**
   * Returns the main GUI inventory for this trader NPC.
   *
   * @return the main {@link org.bukkit.inventory.Inventory} GUI
   * @throws UnsupportedOperationException always, as this method is not implemented
   *     in the base class
   */
  Inventory getMainGui();
}
