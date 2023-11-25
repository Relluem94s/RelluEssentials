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

    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS1 = "scavengers1";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS1_UUID = "ca9a6433-6e91-4d2c-a6ad-d34a577de1a8";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS1_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_WEAPON + "Scavengers I";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS1_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Get 2x Coins for Kills";

    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS2 = "scavengers2";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS2_UUID = "340bbc57-2ae5-4a17-b6d9-912241ed645f";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS2_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_WEAPON + "Scavengers II";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS2_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Get 3x Coins for Kills";

    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS3 = "scavengers3";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS3_UUID = "2c0d4fb2-3ed4-4b81-b1cc-eda6da240ec7";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS3_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_WEAPON + "Scavengers III";
    public static final String PLUGIN_ENCHANTMENT_SCAVENGERS3_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Get 4x Coins for Kills";
}
