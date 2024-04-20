package de.relluem94.minecraft.server.spigot.essentials.constants;

/**
 *
 * @author rellu
 */
public interface EnchantmentConstants {

    //==============================================================================//
    //                          ENCHANTMENT   STUFF                                 //
    //==============================================================================//
    String PLUGIN_ENCHANTMENT_COLOR = "§8";
    String PLUGIN_ENCHANTMENT_LORE_COLOR = "§7§o  ";

    String PLUGIN_ENCHANTMENT_COLOR_WEAPON = "§c";
    String PLUGIN_ENCHANTMENT_COLOR_HOE = "§a";

    String PLUGIN_ENCHANTMENT_AUTOSMELT = "autosmelt";
    String PLUGIN_ENCHANTMENT_AUTOSMELT_UUID = "6e4cb9cf-69fb-4a2a-8a9e-787882bdc92d";
    String PLUGIN_ENCHANTMENT_AUTOSMELT_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR + "Autosmelt";
    String PLUGIN_ENCHANTMENT_AUTOSMELT_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Smelts Ores and Blocks if mined with this Tool";

    String PLUGIN_ENCHANTMENT_TELEKINESIS = "telekinesis";
    String PLUGIN_ENCHANTMENT_TELEKINESIS_UUID = "bfa1f803-1e3f-4a3f-ba19-83c1283d5ddb";
    String PLUGIN_ENCHANTMENT_TELEKINESIS_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR + "Telekinesis";
    String PLUGIN_ENCHANTMENT_TELEKINESIS_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "All drops from Blocks broken and Mobs killed are teleported directly into your Inventory";

    String PLUGIN_ENCHANTMENT_REPLENISHMENT = "replenishment";
    String PLUGIN_ENCHANTMENT_REPLENISHMENT_UUID = "a131665e-dbc7-4929-b5bc-552862b6837a";
    String PLUGIN_ENCHANTMENT_REPLENISHMENT_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_HOE + "Replenishment";
    String PLUGIN_ENCHANTMENT_REPLENISHMENT_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Crops will be replant";

    String PLUGIN_ENCHANTMENT_DELICATE = "delicate";
    String PLUGIN_ENCHANTMENT_DELICATE_UUID = "e945dbb1-277d-4d25-a299-425b5882337a";
    String PLUGIN_ENCHANTMENT_DELICATE_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_HOE + "Delicate";
    String PLUGIN_ENCHANTMENT_DELICATE_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Only Mature Crops get Harvested";

    String PLUGIN_ENCHANTMENT_THUNDERSTRIKE = "thunderstrike";
    String PLUGIN_ENCHANTMENT_THUNDERSTRIKE_UUID = "01277108-fca1-452c-8cf7-de3968f8a636";
    String PLUGIN_ENCHANTMENT_THUNDERSTRIKE_DISPLAYNAME = PLUGIN_ENCHANTMENT_COLOR_WEAPON + "Thunderstrike";
    String PLUGIN_ENCHANTMENT_THUNDERSTRIKE_LORE = PLUGIN_ENCHANTMENT_LORE_COLOR + "Hits Target with a Thunderstrike";
}
