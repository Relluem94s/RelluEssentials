package de.relluem94.minecraft.server.spigot.essentials;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import org.bukkit.Material;

/**
 * Provides predefined {@link ItemHelper} instances used for NPC GUI interactions.
 *
 * <p>This utility class contains static item definitions that serve as building blocks
 * for NPC-based graphical user interfaces, such as filler panes and close buttons.</p>
 *
 * <p>This class is marked as {@link Deprecated} and should not be used in new implementations.
 * Use a more modern item registry approach instead.</p>
 *
 * @author rellu
 * @deprecated Use a dedicated item registry or configuration-driven approach instead.
 */
@Deprecated
public class CustomItems {

  public static final ItemHelper npc_gui_disabled = new ItemHelper(
      Material.BLACK_STAINED_GLASS_PANE, 1, "   ", Type.NPC_GUI, Rarity.NONE);
  public static final ItemHelper npc_gui_close = new ItemHelper(Material.BARRIER, 1, "Close",
      Type.NPC_GUI, Rarity.NONE);

  private CustomItems() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }
}