package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class RelluLeggings extends ItemHelper{

    public RelluLeggings() {
        super(Material.LEATHER_LEGGINGS, 1, ItemConstants.PLUGIN_ITEM_RELLU_LEGGINGS, Type.ARMOR, Rarity.LEGENDARY);
    }

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
}