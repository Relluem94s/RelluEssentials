package de.relluem94.minecraft.server.spigot.essentials.enchantment;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.enchantment.interfaces.IEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS_LORE;

/**
 *
 * @author rellu
 */
public class Telekinesis extends Enchantment implements IEnchantment {

    public Telekinesis(NamespacedKey id) {
        super(id);
    }

    @Override
    public String getName() {
        return PLUGIN_ENCHANTMENT_TELEKINESIS.toLowerCase();
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
        return false;
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
        if (im.getLore() != null) {
            lore = im.getLore();
            lore.add(lore.get(lore.size() - 1));
            lore.add(lore.get(lore.size() - 1));
            lore.set(lore.size() - 3, PLUGIN_ENCHANTMENT_COLOR + PLUGIN_ENCHANTMENT_TELEKINESIS);
            lore.set(lore.size() - 2, PLUGIN_ENCHANTMENT_TELEKINESIS_LORE);
        } else {
            lore = new ArrayList();
            lore.add(PLUGIN_ENCHANTMENT_COLOR + PLUGIN_ENCHANTMENT_TELEKINESIS);
            lore.add(PLUGIN_ENCHANTMENT_TELEKINESIS_LORE);
            lore.add(Rarity.RARE.getPrefix() + Rarity.RARE.getDisplayName());
        }

        im.setLore(lore);

        i.setItemMeta(im);
    }

    @Override
    public void removeFrom(ItemStack i) {
        i.removeEnchantment(this);
        ItemMeta im = i.getItemMeta();
        List<String> lore = im.getLore();
        lore.remove(PLUGIN_ENCHANTMENT_COLOR + PLUGIN_ENCHANTMENT_TELEKINESIS);
        lore.remove(PLUGIN_ENCHANTMENT_TELEKINESIS_LORE);
        im.setLore(lore);
        i.setItemMeta(im);
    }
}
