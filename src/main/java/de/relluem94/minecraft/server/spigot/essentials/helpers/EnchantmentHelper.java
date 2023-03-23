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
    private int actualLevel;

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
        this.conflicts = new ArrayList<>();   
        this.conflicts = conflicts;
        this.attributes = attributes;
        this.actualLevel = level.getStartLevel();

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
        actualLevel = level;
        addTo(i);
    }

    @Override
    public void addTo(ItemStack i) {
        i.addEnchantment(this, actualLevel);

        if(i.hasItemMeta()){
            ItemMeta im = i.getItemMeta();

            for(Attribute a : attributes.asMap().keySet()){
                for(AttributeModifier am : attributes.asMap().get(a)){
                    im.addAttributeModifier(a, am);
                }
            }


            List<String> itemStackLore;
            if (im.getLore() != null) {
                itemStackLore = im.getLore();
                Collections.reverse(itemStackLore);
                itemStackLore.add(getLore());
                itemStackLore.add(getDisplayName());
                Collections.reverse(itemStackLore);
            } else {
                itemStackLore = new ArrayList<String>();
                itemStackLore.add(getDisplayName());
                itemStackLore.add(getLore());
                itemStackLore.add(getRarity().getPrefix() + getRarity().getDisplayName());
            }

            im.setLore(itemStackLore);
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

            List<String> itemStackLore = im.getLore();
            itemStackLore.remove(getDisplayName());
            itemStackLore.remove(getLore());
            itemStackLore.remove(getRarity().getPrefix() + getRarity().getDisplayName());
            im.setLore(itemStackLore);
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
            ChatHelper.consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, String.format(Strings.PLUGIN_MANAGER_REGISTER_ENCHANTMENT, ench.getKey().getNamespace(), ench.getKey().toString()));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ChatHelper.consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, ex.getMessage() + ": " + ench.getKey().toString());
        }
    }

    @Override
    public boolean equals(Object o){
        return (o instanceof EnchantmentHelper) && o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode(){
        int hash = 7;

        hash = 19 * hash + actualLevel;

        hash = 31 * hash + (enchantName == null ? 0 : enchantName.hashCode());
        hash = 31 * hash + (rarity == null ? 0 : rarity.hashCode());
        hash = 31 * hash + (target == null ? 0 : target.hashCode());
        hash = 31 * hash + (level == null ? 0 : level.hashCode());
        hash = 31 * hash + (lore == null ? 0 : lore.hashCode());
        hash = 31 * hash + (conflicts == null ? 0 : conflicts.hashCode());
        hash = 31 * hash + (attributes == null ? 0 : attributes.hashCode());
        hash = 31 * hash + (super.getKey() == null ? 0 : super.getKey().hashCode());
        
        return hash;
    }
}