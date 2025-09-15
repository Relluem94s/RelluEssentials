package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class RelluChestplate extends ItemHelper{

    public RelluChestplate() {
        super(Material.LEATHER_CHESTPLATE, 1, ItemConstants.PLUGIN_ITEM_RELLU_CHESTPLATE, Type.ARMOR, Rarity.LEGENDARY);
    }

    @Override
    public void init() {
        LeatherArmorMeta lam = (LeatherArmorMeta) getItemMeta();
        lam.setColor(Color.fromRGB(72, 179, 177));
        lam.addEnchant(Enchantment.THORNS, 94, true);
        lam.addEnchant(Enchantment.LOOTING, 94, true);
        lam.addEnchant(Enchantment.PROTECTION, 94, true);
        lam.setUnbreakable(true);
        setItemMeta(lam);
    } 
}