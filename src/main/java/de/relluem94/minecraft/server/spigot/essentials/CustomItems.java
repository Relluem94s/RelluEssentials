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
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_BOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_CHESTPLATE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_HELMET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_LEGGINGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_PICKAXE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_SHIELD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_SWORD;

import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.BlockStateMeta;
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

    public static final ItemHelper magic_water_bucket = new ItemHelper(Material.WATER_BUCKET, 1, PLUGIN_ITEM_MAGIC_WATER_BUCKET, Type.MONEY, Rarity.EPIC, Arrays.asList(PLUGIN_ITEM_MAGIC_WATER_BUCKET_LORE)){
        @Override
        public void init() {
            ItemMeta im = getItemMeta();
            im.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        }
    };

    public static final ItemHelper coins = new ItemHelper(Material.GOLD_NUGGET, 1, PLUGIN_ITEM_COINS, Type.MONEY, Rarity.COMMON, Arrays.asList(PLUGIN_ITEM_COINS_LORE));

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

    /**
     *
     * Internal Testing Stuff
     *
     *
     */
    public static final ItemHelper relluHelmet = new ItemHelper(Material.LEATHER_HELMET, 1, PLUGIN_ITEM_RELLU_HELMET, Type.ARMOR, Rarity.LEGENDARY) {
        @Override
        public void init() {
            LeatherArmorMeta lam = (LeatherArmorMeta) getItemMeta();
            lam.setColor(Color.fromRGB(243, 125, 0));
            lam.addEnchant(Enchantment.THORNS, 94, true);
            lam.addEnchant(Enchantment.LOOT_BONUS_MOBS, 94, true);
            lam.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 94, true);
            lam.setUnbreakable(true);
            setItemMeta(lam);
        }
    };

    public static final ItemHelper relluChestPlate = new ItemHelper(Material.LEATHER_CHESTPLATE, 1, PLUGIN_ITEM_RELLU_CHESTPLATE, Type.ARMOR, Rarity.LEGENDARY) {
        @Override
        public void init() {
            LeatherArmorMeta lam = (LeatherArmorMeta) getItemMeta();
            lam.setColor(Color.fromRGB(72, 179, 177));
            lam.addEnchant(Enchantment.THORNS, 94, true);
            lam.addEnchant(Enchantment.LOOT_BONUS_MOBS, 94, true);
            lam.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 94, true);
            lam.setUnbreakable(true);
            setItemMeta(lam);
        }
    };

    public static final ItemHelper relluLeggings = new ItemHelper(Material.LEATHER_LEGGINGS, 1, PLUGIN_ITEM_RELLU_LEGGINGS, Type.ARMOR, Rarity.LEGENDARY) {
        @Override
        public void init() {
            LeatherArmorMeta lam = (LeatherArmorMeta) getItemMeta();
            lam.setColor(Color.fromRGB(152, 216, 1));
            lam.addEnchant(Enchantment.THORNS, 94, true);
            lam.addEnchant(Enchantment.LOOT_BONUS_MOBS, 94, true);
            lam.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 94, true);
            lam.setUnbreakable(true);
            setItemMeta(lam);
        }
    };

    public static final ItemHelper relluBoots = new ItemHelper(Material.LEATHER_BOOTS, 1, PLUGIN_ITEM_RELLU_BOOTS, Type.ARMOR, Rarity.LEGENDARY) {
        @Override
        public void init() {
            LeatherArmorMeta lam = (LeatherArmorMeta) getItemMeta();
            lam.setColor(Color.fromRGB(227, 59, 46));
            lam.addEnchant(Enchantment.THORNS, 94, true);
            lam.addEnchant(Enchantment.LOOT_BONUS_MOBS, 94, true);
            lam.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 94, true);
            lam.setUnbreakable(true);
            setItemMeta(lam);
        }
    };

    public static final ItemHelper relluSword = new ItemHelper(Material.NETHERITE_SWORD, 1, PLUGIN_ITEM_RELLU_SWORD, Type.WEAPON, Rarity.LEGENDARY) {
        @Override
        public void init() {
            ItemMeta im = getItemMeta();
            im.addEnchant(Enchantment.FIRE_ASPECT, 94, true);
            im.addEnchant(Enchantment.DAMAGE_ALL, 94, true);
            im.addEnchant(Enchantment.SWEEPING_EDGE, 94, true);
            im.addEnchant(Enchantment.LOOT_BONUS_MOBS, 94, true);
            im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 94, true);
            im.setUnbreakable(true);
            setItemMeta(im);
        }
    };

    public static final ItemHelper relluPickAxe = new ItemHelper(Material.NETHERITE_PICKAXE, 1, PLUGIN_ITEM_RELLU_PICKAXE, Type.TOOL, Rarity.LEGENDARY) {
        @Override
        public void init() {
            ItemMeta im = getItemMeta();
            im.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 94, true);
            im.addEnchant(Enchantment.DIG_SPEED, 94, true);
            im.setUnbreakable(true);
            setItemMeta(im);
        }
    };

    public static final ItemHelper relluShield = new ItemHelper(Material.SHIELD, 1, PLUGIN_ITEM_RELLU_SHIELD, Type.ARMOR, Rarity.LEGENDARY) {
        @Override
        public void init() {
            BlockStateMeta bmeta = (BlockStateMeta) getItemMeta();

            Banner banner = (Banner) bmeta.getBlockState();
            banner.setBaseColor(DyeColor.PURPLE);

            banner.update();

            bmeta.setBlockState(banner);

            bmeta.addEnchant(Enchantment.DAMAGE_ALL, 94, true);
            bmeta.addEnchant(Enchantment.THORNS, 94, true);
            bmeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 94, true);
            bmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 94, true);
            bmeta.setUnbreakable(true);
            setItemMeta(bmeta);
        }
    };


    public static final ItemHelper npc_gui_disabled = new ItemHelper(Material.BLACK_STAINED_GLASS_PANE, 1, "   ", Type.NPC_GUI, Rarity.NONE);
    public static final ItemHelper npc_gui_close = new ItemHelper(Material.BARRIER, 1, "Close", Type.NPC_GUI, Rarity.NONE);
}