package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.BlockStateMeta;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class RelluShield extends ItemHelper{

    public RelluShield() {
        super(Material.SHIELD, 1, ItemConstants.PLUGIN_ITEM_RELLU_SHIELD, Type.ARMOR, Rarity.LEGENDARY);
    }

    @Override
    public void init() {
        BlockStateMeta bmeta = (BlockStateMeta) getItemMeta();

        Banner banner = (Banner) bmeta.getBlockState();
        banner.setBaseColor(DyeColor.PURPLE);

        banner.update();

        bmeta.setBlockState(banner);

        bmeta.addEnchant(Enchantment.SHARPNESS, 94, true);
        bmeta.addEnchant(Enchantment.THORNS, 94, true);
        bmeta.addEnchant(Enchantment.LOOTING, 94, true);
        bmeta.addEnchant(Enchantment.PROTECTION, 94, true);
        bmeta.setUnbreakable(true);
        setItemMeta(bmeta);
    }
    
}
