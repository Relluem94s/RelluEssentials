package de.relluem94.minecraft.server.spigot.essentials.enchantment;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.enchantment.interfaces.IEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.ItemRarity;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LORE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_COLOR;

/**
 *
 * @author rellu
 */
public class AutoSmelt extends Enchantment implements IEnchantment {

    public AutoSmelt(NamespacedKey id) {
        super(id);
    }

    @Override
    public String getName() {
        return PLUGIN_ENCHANTMENT_AUTOSMELT.toLowerCase();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other.equals(Enchantment.SILK_TOUCH);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;
    }

    @Override
    public void addTo(ItemStack i) {
        i.addUnsafeEnchantment(this, 1);
        ItemMeta im = i.getItemMeta();
        List<String> lore;
        if (im.getLore() != null && !im.getLore().contains(PLUGIN_ENCHANTMENT_COLOR + PLUGIN_ENCHANTMENT_AUTOSMELT)) {
            lore = im.getLore();
            lore.add(lore.get(lore.size() - 1));
            lore.add(lore.get(lore.size() - 1));
            lore.add(lore.get(lore.size() - 1));
            lore.set(lore.size() - 4, PLUGIN_ENCHANTMENT_COLOR + PLUGIN_ENCHANTMENT_AUTOSMELT);
            lore.set(lore.size() - 3, PLUGIN_ENCHANTMENT_AUTOSMELT_LORE);
            lore.set(lore.size() - 2, PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK + String.format(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT, 0));
        } else {
            lore = new ArrayList();
            lore.add(PLUGIN_ENCHANTMENT_COLOR + PLUGIN_ENCHANTMENT_AUTOSMELT);
            lore.add(PLUGIN_ENCHANTMENT_AUTOSMELT_LORE);
            lore.add(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK + String.format(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT, 0));
            lore.add(ItemRarity.EPIC.getPrefix() + ItemRarity.EPIC.getDisplayName());
        }

        im.setLore(lore);

        i.setItemMeta(im);
    }

    @Override
    public void removeFrom(ItemStack i) {
        i.removeEnchantment(this);
        ItemMeta im = i.getItemMeta();
        List<String> lore = im.getLore();
        lore.remove(PLUGIN_ENCHANTMENT_COLOR + PLUGIN_ENCHANTMENT_AUTOSMELT);

        for (int o = 0; o < lore.size(); o++) {
            if (lore.get(o).startsWith(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK)) {
                lore.remove(o);
            }
        }

        lore.remove(PLUGIN_ENCHANTMENT_AUTOSMELT_LORE);
        im.setLore(lore);
        i.setItemMeta(im);
    }
}
