package de.relluem94.minecraft.server.spigot.essentials;

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
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.autosmelt;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.*;


/**
 *
 * @author rellu
 */
public class CustomItems {

    public static ItemHelper cloudSailor = new ItemHelper(Material.FEATHER, 1, PLUGIN_ITEM_CLOUDSAILOR, Type.GADGET, Rarity.EPIC, Arrays.asList(new String[]{PLUGIN_ITEM_CLOUDSAILOR_LORE1, PLUGIN_ITEM_CLOUDSAILOR_LORE2}));
    public static ItemHelper cloudBoots = new ItemHelper(Material.LEATHER_BOOTS, 1, PLUGIN_ITEM_CLOUDBOOTS, Type.ARMOR, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_CLOUDBOOTS_LORE1, PLUGIN_ITEM_CLOUDBOOTS_LORE2})) {
        @Override
        public void init() {
            LeatherArmorMeta cloudBootsMeta = (LeatherArmorMeta) getItemMeta();
            cloudBootsMeta.setColor(Color.SILVER);
            cloudBootsMeta.setUnbreakable(true);
            cloudBootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
            setItemMeta(cloudBootsMeta);
        }
    };

    public static ItemHelper autoSmeltNetheritePickAxe = new ItemHelper(Material.NETHERITE_PICKAXE, 1, PLUGIN_ITEM_AUTOSMELTER, Type.TOOL, Rarity.LEGENDARY) {
        @Override
        public void init() {
            ItemMeta im = getItemMeta();
            im.setUnbreakable(true);
            setItemMeta(im);
            autosmelt.addTo(getItemStack());
        }
    };

    public static ItemHelper autoSmeltTank = new ItemHelper(Material.GLASS_BOTTLE, 1, PLUGIN_ITEM_AUTOSMELTER_TANK, Type.INGREDIENT, Rarity.RARE, Arrays.asList(new String[]{PLUGIN_ITEM_INGREDIENT}));
    public static ItemHelper autoSmeltFurnace = new ItemHelper(Material.FURNACE, 1, PLUGIN_ITEM_AUTOSMELTER_FURNACE, Type.INGREDIENT, Rarity.RARE, Arrays.asList(new String[]{PLUGIN_ITEM_INGREDIENT}));

    /**
     *
     * Internal Testing Stuff
     *
     *
     */
    public static ItemHelper relluHelmet = new ItemHelper(Material.LEATHER_HELMET, 1, PLUGIN_ITEM_RELLU_HELMET, Type.ARMOR, Rarity.LEGENDARY) {
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

    public static ItemHelper relluChestPlate = new ItemHelper(Material.LEATHER_CHESTPLATE, 1, PLUGIN_ITEM_RELLU_CHESTPLATE, Type.ARMOR, Rarity.LEGENDARY) {
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

    public static ItemHelper relluLeggings = new ItemHelper(Material.LEATHER_LEGGINGS, 1, PLUGIN_ITEM_RELLU_LEGGINGS, Type.ARMOR, Rarity.LEGENDARY) {
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

    public static ItemHelper relluBoots = new ItemHelper(Material.LEATHER_BOOTS, 1, PLUGIN_ITEM_RELLU_BOOTS, Type.ARMOR, Rarity.LEGENDARY) {
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

    public static ItemHelper relluSword = new ItemHelper(Material.NETHERITE_SWORD, 1, PLUGIN_ITEM_RELLU_SWORD, Type.WEAPON, Rarity.LEGENDARY) {
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

    public static ItemHelper relluShield = new ItemHelper(Material.SHIELD, 1, PLUGIN_ITEM_RELLU_SHIELD, Type.ARMOR, Rarity.LEGENDARY) {
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


    public static ItemHelper npcBanker = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, PLUGIN_ITEM_NPC_BANKER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_LORE1}));
    public static ItemHelper npcBanker_gui_deposit = new ItemHelper(Material.GREEN_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_LORE1}));
    public static ItemHelper npcBanker_gui_withdraw = new ItemHelper(Material.RED_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_LORE1}));
    public static ItemHelper npcBanker_gui_balance = new ItemHelper(Material.YELLOW_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_LORE1}));
    public static ItemHelper npcBanker_gui_upgrade = new ItemHelper(Material.DIAMOND_BLOCK, 1, PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE_LORE1}));

    public static ItemHelper npc_gui_disabled = new ItemHelper(Material.BLACK_STAINED_GLASS_PANE, 1, "   ", Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{}));


    public static ItemHelper npcBanker_portable_bank = new ItemHelper(Material.YELLOW_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK, Type.TOOL, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK_LORE1}));


    public static ItemHelper npcFarmer = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, PLUGIN_ITEM_NPC_FARMER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_LORE1}));
    public static ItemHelper npcFisher = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, PLUGIN_ITEM_NPC_FISHER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_LORE1}));
    public static ItemHelper npcMiner = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, PLUGIN_ITEM_NPC_MINER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_LORE1}));
    public static ItemHelper npcAdventurer = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, PLUGIN_ITEM_NPC_ADVENTURER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_LORE1}));
    public static ItemHelper npcSmith = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, PLUGIN_ITEM_NPC_SMITH, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_LORE1}));
}
