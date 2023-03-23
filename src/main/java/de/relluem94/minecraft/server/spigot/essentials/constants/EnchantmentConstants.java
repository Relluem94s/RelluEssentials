package de.relluem94.minecraft.server.spigot.essentials.constants;

import de.relluem94.minecraft.server.spigot.essentials.Strings;

/**
 *
 * @author rellu
 */
public class EnchantmentConstants {

    private EnchantmentConstants() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    //==============================================================================//
    //                          ENCHANTMENT   STUFF                                 //
    //==============================================================================//
    public static final String PLUGIN_ENCHANTMENT_COLOR = "§8";
    public static final String PLUGIN_ENCHANTMENT_LORE_COLOR = "§7§o  ";

    public static final String PLUGIN_ENCHANTMENT_COLOR_WEAPON = "§c";
    public static final String PLUGIN_ENCHANTMENT_COLOR_HOE = "§a";

    
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK = PLUGIN_ENCHANTMENT_LORE_COLOR + "Your Lava Tank is filled: ";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR = "§f/§2";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LOW_COLOR = "§6";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR = "§2";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_COLOR = "§4";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_MESSAGE = "§4Your Lava Tank is empty. You can't smelt any longer";
    public static final int PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX = 1000;
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT = "%s" + PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR + PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX;



    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT = "autosmelt";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_UUID = "6e4cb9cf-69fb-4a2a-8a9e-787882bdc92d";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR + "Autosmelt";
    public static final String PLUGIN_ENCHANTMENT_AUTOSMELT_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Smelts Ores and Blocks if mined with this Tool";

    public static final String PLUGIN_ENCHANTMENT_TELEKINESIS = "telekinesis";
    public static final String PLUGIN_ENCHANTMENT_TELEKINESIS_UUID = "bfa1f803-1e3f-4a3f-ba19-83c1283d5ddb";
    public static final String PLUGIN_ENCHANTMENT_TELEKINESIS_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR + "Telekinesis";
    public static final String PLUGIN_ENCHANTMENT_TELEKINESIS_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "All drops from Blocks broken and Mobs killed are teleported directly into your Inventory";

    public static final String PLUGIN_ENCHANTMENT_REPLENISHMENT = "replenishment";
    public static final String PLUGIN_ENCHANTMENT_REPLENISHMENT_UUID = "a131665e-dbc7-4929-b5bc-552862b6837a";
    public static final String PLUGIN_ENCHANTMENT_REPLENISHMENT_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_HOE + "Replenishment";
    public static final String PLUGIN_ENCHANTMENT_REPLENISHMENT_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Crops will be replant";

    public static final String PLUGIN_ENCHANTMENT_DELICATE = "delicate";
    public static final String PLUGIN_ENCHANTMENT_DELICATE_UUID = "e945dbb1-277d-4d25-a299-425b5882337a";
    public static final String PLUGIN_ENCHANTMENT_DELICATE_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_HOE + "Delicate";
    public static final String PLUGIN_ENCHANTMENT_DELICATE_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Only Mature Crops get Harvested";

    public static final String PLUGIN_ENCHANTMENT_THUNDERSTRIKE = "thunderstrike";
    public static final String PLUGIN_ENCHANTMENT_THUNDERSTRIKE_UUID = "01277108-fca1-452c-8cf7-de3968f8a636";
    public static final String PLUGIN_ENCHANTMENT_THUNDERSTRIKE_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_WEAPON + "Thunderstrike";
    public static final String PLUGIN_ENCHANTMENT_THUNDERSTRIKE_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Hits Target with a Thunderstrike";
}
