package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class RelluBoots extends ItemHelper{

    public RelluBoots() {
        super(Material.LEATHER_BOOTS, 1, ItemConstants.PLUGIN_ITEM_RELLU_BOOTS, Type.ARMOR, Rarity.LEGENDARY);
    }

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
}