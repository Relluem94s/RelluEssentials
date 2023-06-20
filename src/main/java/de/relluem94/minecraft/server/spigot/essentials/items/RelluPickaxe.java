package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class RelluPickaxe extends ItemHelper{

    public RelluPickaxe() {
        super(Material.NETHERITE_PICKAXE, 1, ItemConstants.PLUGIN_ITEM_RELLU_PICKAXE, Type.TOOL, Rarity.LEGENDARY);
    }

    @Override
    public void init() {
        ItemMeta im = getItemMeta();
        im.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 94, true);
        im.addEnchant(Enchantment.DIG_SPEED, 94, true);
        im.setUnbreakable(true);
        setItemMeta(im);
    }
    
}
