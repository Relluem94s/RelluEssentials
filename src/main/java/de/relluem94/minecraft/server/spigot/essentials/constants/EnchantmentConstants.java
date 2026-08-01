package de.relluem94.minecraft.server.spigot.essentials.constants;

/**
 *
 * @author rellu
 */
public class EnchantmentConstants {

  private EnchantmentConstants() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static final String PLUGIN_ENCHANTMENT_COLOR = "§8";
  public static final String PLUGIN_ENCHANTMENT_LORE_COLOR = "§7§o  ";

  public static final String PLUGIN_ENCHANTMENT_COLOR_WEAPON = "§c";
  public static final String PLUGIN_ENCHANTMENT_COLOR_HOE = "§a";

  public static final String PLUGIN_ENCHANTMENT_AUTOSMELT = "autosmelt";
  public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR + "Autosmelt";
  public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LORE =
      PLUGIN_ENCHANTMENT_LORE_COLOR + "Smelts Ores and Blocks if mined with this Tool";

  public static final String PLUGIN_ENCHANTMENT_TELEKINESIS = "telekinesis";
  public static final String PLUGIN_ENCHANTMENT_TELEKINESIS_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR + "Telekinesis";
  public static final String PLUGIN_ENCHANTMENT_TELEKINESIS_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR
      + "All drops from Blocks broken and Mobs killed are teleported directly into your Inventory";

  public static final String PLUGIN_ENCHANTMENT_REPLENISHMENT = "replenishment";
  public static final String PLUGIN_ENCHANTMENT_REPLENISHMENT_DISPLAYNAME =
      PLUGIN_ENCHANTMENT_COLOR_HOE + "Replenishment";
  public static final String PLUGIN_ENCHANTMENT_REPLENISHMENT_LORE =
      PLUGIN_ENCHANTMENT_LORE_COLOR + "Crops will be replant";

  public static final String  PLUGIN_ENCHANTMENT_DELICATE = "delicate";
  public static final String  PLUGIN_ENCHANTMENT_DELICATE_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_HOE + "Delicate";
  public static final String  PLUGIN_ENCHANTMENT_DELICATE_LORE =
      PLUGIN_ENCHANTMENT_LORE_COLOR + "Only Mature Crops get Harvested";

  public static final String  PLUGIN_ENCHANTMENT_THUNDERSTRIKE = "thunderstrike";
  public static final String  PLUGIN_ENCHANTMENT_THUNDERSTRIKE_DISPLAYNAME =
      PLUGIN_ENCHANTMENT_COLOR_WEAPON + "Thunderstrike";
  public static final String  PLUGIN_ENCHANTMENT_THUNDERSTRIKE_LORE =
      PLUGIN_ENCHANTMENT_LORE_COLOR + "Hits Target with a Thunderstrike";

  public static final String PLUGIN_ENCHANTMENT_SCAVENGERS = "scavengers";
  public static final String PLUGIN_ENCHANTMENT_SCAVENGERS_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR + "Scavengers";
  public static final String PLUGIN_ENCHANTMENT_SCAVENGERS_LORE =
      PLUGIN_ENCHANTMENT_LORE_COLOR + "Mobs drop more coins";

  public static final String PLUGIN_ENCHANTMENT_LIFESTEAL = "lifesteal";
  public static final String PLUGIN_ENCHANTMENT_LIFESTEAL_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR + "Lifesteal";
  public static final String PLUGIN_ENCHANTMENT_LIFESTEAL_LORE =
      PLUGIN_ENCHANTMENT_LORE_COLOR + "Hits can restore Player Health";
}
