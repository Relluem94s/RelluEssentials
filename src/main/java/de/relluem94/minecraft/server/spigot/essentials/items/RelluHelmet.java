package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class RelluHelmet extends ItemHelper{

    public RelluHelmet() {
        super(Material.LEATHER_HELMET, 1, ItemConstants.PLUGIN_ITEM_RELLU_HELMET, Type.ARMOR, Rarity.LEGENDARY);
    }

    @Override
    public void init() {
        LeatherArmorMeta lam = (LeatherArmorMeta) getItemMeta();
            lam.setColor(Color.fromRGB(243, 125, 0));
            lam.addEnchant(Enchantment.THORNS, 94, true);
            lam.addEnchant(Enchantment.LOOTING, 94, true);
            lam.addEnchant(Enchantment.PROTECTION, 94, true);
            lam.setUnbreakable(true);
            setItemMeta(lam);
    }
}