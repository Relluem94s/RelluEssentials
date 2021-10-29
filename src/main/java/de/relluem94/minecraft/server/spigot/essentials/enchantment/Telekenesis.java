package de.relluem94.minecraft.server.spigot.essentials.enchantment;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_TELEKENESIS;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author rellu
 */
public class Telekenesis extends Enchantment {

    public Telekenesis(NamespacedKey id) {
        super(id);
    }

    @Override
    public String getName() {
        return PLUGIN_ENCHANTMENT_TELEKENESIS.toLowerCase();
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
}
