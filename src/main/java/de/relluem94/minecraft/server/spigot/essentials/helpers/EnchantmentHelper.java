package de.relluem94.minecraft.server.spigot.essentials.helpers;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.google.common.collect.Multimap;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.EnchantLevel;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.EnchantName;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.interfaces.IEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;

/**
 *
 * @author rellu
 */
public class EnchantmentHelper extends Enchantment implements IEnchantment {

    private String lore;
    private Rarity rarity;
    private List<Enchantment> conflicts;
    private EnchantmentTarget target;
    private EnchantLevel level;
    private EnchantName enchantName;
    private Multimap<Attribute, AttributeModifier> attributes;
    private double multiply;
    private int actual_level;

    public EnchantmentHelper(NamespacedKey id) {
        super(id);
    }

    public EnchantmentHelper(EnchantName enchantName, EnchantmentTarget target, EnchantLevel level, List<Enchantment> conflicts, String lore, Rarity rarity, Multimap<Attribute, AttributeModifier> attributes) {
        super(new NamespacedKey(RelluEssentials.getInstance(), enchantName.getName()));
        this.enchantName = enchantName;
        this.rarity = rarity;
        this.target = target;
        this.level = level;
        this.lore = lore;   
        this.conflicts = new ArrayList<Enchantment>();   
        this.conflicts = conflicts;
        this.attributes = attributes;
        this.actual_level = level.getStartLevel();

        CustomEnchants.customEnchantments.add(this);
    }

    public double getMultiplyer(){
        return multiply;
    }

    @Override
    public String getName() {
        return enchantName.getName();
    }

    public String getDisplayName() {
        return enchantName.getDisplayName();
    }

    public String getLore(){
        return lore;
    }

    public Rarity getRarity(){
        return rarity;
    }

    @Override
    public int getMaxLevel() {
        return level.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return level.getStartLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return target;
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
        return conflicts.contains(other);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {

        boolean canEnchantItem = target.includes(item);
        for(Enchantment e : item.getEnchantments().keySet()){
            if(conflictsWith(e)){
                canEnchantItem = false;
                System.out.println(e.getKey());
            }
        }

        return canEnchantItem;
    }

    public Multimap<Attribute, AttributeModifier> getAttributes(){
        return attributes;
    }

    public ItemHelper getBook(){
        return new ItemHelper(
            ItemHelper.addBookEnchantment(
                new ItemStack(Material.ENCHANTED_BOOK), Enchantment.getByKey(
                    super.getKey()
                ),
                level.getStartLevel()
            ),
            enchantName.getDisplayName(),
            ItemHelper.Type.ENCHANTMENT,
            getRarity()
        );
    }

    public void addTo(ItemStack i, int level){
        actual_level = level;
        addTo(i);
    }

    @Override
    public void addTo(ItemStack i) {
        i.addEnchantment(this, actual_level);

        if(i.hasItemMeta()){
            ItemMeta im = i.getItemMeta();

            for(Attribute a : attributes.asMap().keySet()){
                for(AttributeModifier am : attributes.asMap().get(a)){
                    im.addAttributeModifier(a, am);
                }
            }


            List<String> lore;
            if (im.getLore() != null) {
                lore = im.getLore();
                Collections.reverse(lore);
                lore.add(getLore());
                lore.add(getDisplayName());
                Collections.reverse(lore);
            } else {
                lore = new ArrayList<String>();
                lore.add(getDisplayName());
                lore.add(getLore());
                lore.add(getRarity().getPrefix() + getRarity().getDisplayName());
            }

            im.setLore(lore);

            i.setItemMeta(im);
        }
    }

    @Override
    public void removeFrom(ItemStack i) {
        i.removeEnchantment(this);
        if(i.hasItemMeta()){
            ItemMeta im = i.getItemMeta();

            for(Attribute a : attributes.asMap().keySet()){
                for(AttributeModifier am : attributes.asMap().get(a)){
                    im.removeAttributeModifier(a, am);
                }
            }

            List<String> lore = im.getLore();
            lore.remove(getDisplayName());
            lore.remove(getLore());
            lore.remove(getRarity().getPrefix() + getRarity().getDisplayName());
            im.setLore(lore);
            i.setItemMeta(im);
        }
    }

    public static void registerEnchants(Enchantment ench) {
        try {
            Field f;
            f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(ench);
            ChatHelper.consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, String.format(Strings.PLUGIN_REGISTER_ENCHANTMENT, ench.getKey().getNamespace(), ench.getKey().toString()));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ChatHelper.consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, ex.getMessage() + ": " + ench.getKey().toString());
        }
    }
}
