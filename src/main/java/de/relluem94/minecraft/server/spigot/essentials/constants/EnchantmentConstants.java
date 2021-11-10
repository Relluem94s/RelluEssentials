package de.relluem94.minecraft.server.spigot.essentials.constants;

/**
 *
 * @author rellu
 */
public class EnchantmentConstants {
    
    /********************************************************************************/
    /*                          ENCHANTMENT   STUFF                                 */
    /********************************************************************************/
    
    public static final String PLUGIN_ENCHANTMENT_COLOR = "§8";
    public static final String PLUGIN_ENCHANTMENT_LORE_COLOR = "§7§o  ";

    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT = "Autosmelt";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK = PLUGIN_ENCHANTMENT_LORE_COLOR + "Your Lava Tank is filled: ";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR = "§f/§2";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LOW_COLOR = "§6";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR = "§2";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_COLOR = "§4";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_MESSAGE = "§4Your Lava Tank is empty. You can't smelt any longer";
    public static final int PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX = 1000;
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT = "%s" + PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR + PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX;
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Smelts Ores and Blocks if mined with this Tool";
    public static final String PLUGIN_ENCHANTMENT_TELEKINESIS = "Telekinesis";
    public static final String PLUGIN_ENCHANTMENT_TELEKINESIS_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "All drops from Blocks broken and Mobs killed are teleported directly into your Inventory";
}