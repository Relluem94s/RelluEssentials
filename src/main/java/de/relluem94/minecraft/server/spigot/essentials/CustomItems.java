package de.relluem94.minecraft.server.spigot.essentials;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDBOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDBOOTS_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDBOOTS_LORE2;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDSAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDSAILOR_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDSAILOR_LORE2;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_COINS_LORE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_MAGIC_WATER_BUCKET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_MAGIC_WATER_BUCKET_LORE;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;


/**
 *
 * @author rellu
 */
public class CustomItems {

    private CustomItems() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static final ItemHelper magic_water_bucket = new ItemHelper(Material.WATER_BUCKET, 1, PLUGIN_ITEM_MAGIC_WATER_BUCKET, Type.GADGET, Rarity.EPIC, List.of(PLUGIN_ITEM_MAGIC_WATER_BUCKET_LORE)){
        @Override
        public void init() {
            ItemMeta im = getItemMeta();
            im.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        }
    };

    public static final ItemHelper coins = new ItemHelper(Material.GOLD_NUGGET, 1, PLUGIN_ITEM_COINS, Type.MONEY, Rarity.COMMON, List.of(PLUGIN_ITEM_COINS_LORE));

    public static final ItemHelper cloudSailor = new ItemHelper(Material.FEATHER, 1, PLUGIN_ITEM_CLOUDSAILOR, Type.GADGET, Rarity.EPIC, Arrays.asList(PLUGIN_ITEM_CLOUDSAILOR_LORE1, PLUGIN_ITEM_CLOUDSAILOR_LORE2));
    public static final ItemHelper cloudBoots = new ItemHelper(Material.LEATHER_BOOTS, 1, PLUGIN_ITEM_CLOUDBOOTS, Type.ARMOR, Rarity.LEGENDARY, Arrays.asList(PLUGIN_ITEM_CLOUDBOOTS_LORE1, PLUGIN_ITEM_CLOUDBOOTS_LORE2)) {
        @Override
        public void init() {
            LeatherArmorMeta cloudBootsMeta = (LeatherArmorMeta) getItemMeta();
            cloudBootsMeta.setColor(Color.SILVER);
            cloudBootsMeta.setUnbreakable(true);
            cloudBootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
            
            setItemMeta(cloudBootsMeta);
        }
    };

    public static final ItemHelper npc_gui_disabled = new ItemHelper(Material.BLACK_STAINED_GLASS_PANE, 1, "   ", Type.NPC_GUI, Rarity.NONE);
    public static final ItemHelper npc_gui_close = new ItemHelper(Material.BARRIER, 1, "Close", Type.NPC_GUI, Rarity.NONE);
}