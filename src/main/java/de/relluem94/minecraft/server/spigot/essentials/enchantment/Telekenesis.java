package de.relluem94.minecraft.server.spigot.essentials.enchantment;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_TELEKENESIS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_TELEKENESIS_LORE;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.interfaces.IEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.ItemRarity;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author rellu
 */
public class Telekenesis extends Enchantment implements IEnchantment {

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

    @Override
    public void addTo(ItemStack i) {
        i.addUnsafeEnchantment(this, 1);
        ItemMeta im =  i.getItemMeta();
        List<String> lore;
        if(im.getLore() != null){
            lore = im.getLore();
            lore.add(lore.get(lore.size()-1));
            lore.set(lore.size()-2, PLUGIN_ENCHANTMENT_TELEKENESIS_LORE);
        }
        else{
            lore = new ArrayList();
            lore.add(PLUGIN_ENCHANTMENT_TELEKENESIS_LORE);
            lore.add(ItemRarity.RARE.getPrefix() + ItemRarity.RARE.getDisplayName());
        }
        
        im.setLore(lore);
        
        i.setItemMeta(im);
    }
}
