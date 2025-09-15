package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class RelluSword extends ItemHelper{

    public RelluSword() {
        super(Material.NETHERITE_SWORD, 1, ItemConstants.PLUGIN_ITEM_RELLU_SWORD, Type.WEAPON, Rarity.LEGENDARY);
    }

    @Override
    public void init() {
        ItemMeta im = getItemMeta();
        im.addEnchant(Enchantment.SHARPNESS, 94, true);
        im.addEnchant(Enchantment.SWEEPING_EDGE, 94, true);
        im.addEnchant(Enchantment.LOOTING, 94, true);
        im.addEnchant(Enchantment.PROTECTION, 94, true);
        im.setUnbreakable(true);
        setItemMeta(im);
    }   
    
    @Override
    public ItemStack postInit(ItemStack is){
        CustomEnchants.telekinesis.addTo(is);
        CustomEnchants.thunderstrike.addTo(is);
        return is;
    }
}