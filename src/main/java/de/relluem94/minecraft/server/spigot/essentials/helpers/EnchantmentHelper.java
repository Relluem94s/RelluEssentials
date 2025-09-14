package de.relluem94.minecraft.server.spigot.essentials.helpers;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.CustomEnchantment;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Multimap;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.EnchantLevel;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.EnchantName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


/**
 *
 * @author rellu
 */
public class EnchantmentHelper extends CustomEnchantment {


    public EnchantmentHelper(NamespacedKey id) {
        super(id);
    }

    public EnchantmentHelper(EnchantName enchantName, EnchantmentTarget target, EnchantLevel level, String lore, Rarity rarity, Multimap<Attribute, AttributeModifier> attributes) {
        super(new NamespacedKey(RelluEssentials.getInstance(), enchantName.name()));
        this.enchantName = enchantName;
        this.rarity = rarity;
        this.target = target;
        this.level = level;
        this.lore = lore;
        this.attributes = attributes;
        this.actualLevel = level.startLevel();

        CustomEnchants.customEnchantments.add(this);
    }

    @SuppressWarnings("unused")
    public double getMultiplier() {
        return multiply;
    }

    @NonNull
    public String getName() {
        return enchantName.name();
    }

    public String getDisplayName() {
        return enchantName.displayName();
    }

    public int getMaxLevel() {
        return level.maxLevel();
    }

    public int getStartLevel() {
        return level.startLevel();
    }

    @NonNull
    public EnchantmentTarget getItemTarget() {
        return target;
    }

    public ItemHelper getBook() {
        return new ItemHelper(
                ItemHelper.addBookEnchantment(
                        new ItemStack(Material.ENCHANTED_BOOK), this
                ),
                enchantName.displayName(),
                ItemHelper.Type.ENCHANTMENT,
                getRarity()
        );
    }

    public void addTo(ItemStack i) {
        if (i.hasItemMeta()) {
            ItemMeta im = i.getItemMeta();

            for (Attribute a : attributes.asMap().keySet()) {
                if(a == null){
                    continue;
                }
                for (AttributeModifier am : attributes.asMap().get(a)) {
                    if(am == null){
                        continue;
                    }
                    Objects.requireNonNull(im).addAttributeModifier(a, am);
                }
            }


            List<String> itemStackLore;
            if (Objects.requireNonNull(im).getLore() != null) {
                itemStackLore = im.getLore();
                Collections.reverse(itemStackLore);
                itemStackLore.add(getLore());
                itemStackLore.add(getDisplayName());
                Collections.reverse(itemStackLore);
            } else {
                itemStackLore = new ArrayList<>();
                itemStackLore.add(getDisplayName());
                itemStackLore.add(getLore());
                itemStackLore.add(getRarity().getPrefix() + getRarity().getDisplayName());
            }

            im.setLore(itemStackLore);
            i.setItemMeta(im);

            PersistentDataContainer persistentDataContainer = im.getPersistentDataContainer();
            persistentDataContainer.set(super.getKey(), PersistentDataType.INTEGER, actualLevel);
        }
    }


    public void removeFrom(ItemStack i) {
        if (i.hasItemMeta()) {
            ItemMeta im = i.getItemMeta();

            for (Attribute a : attributes.asMap().keySet()) {
                for (AttributeModifier am : attributes.asMap().get(a)) {
                    if (im != null) {
                        im.removeAttributeModifier(a, am);
                    }
                }
            }

            List<String> itemStackLore = Objects.requireNonNull(im).getLore();
            if (itemStackLore != null) {
                itemStackLore.remove(getDisplayName());
                itemStackLore.remove(getLore());
                itemStackLore.remove(getRarity().getPrefix() + getRarity().getDisplayName());
            }

            im.setLore(itemStackLore);
            i.setItemMeta(im);

            PersistentDataContainer persistentDataContainer = im.getPersistentDataContainer();
            persistentDataContainer.remove(super.getKey());
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof EnchantmentHelper) && o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 19 * hash + actualLevel;

        hash = 31 * hash + (enchantName == null ? 0 : enchantName.hashCode());
        hash = 31 * hash + (rarity == null ? 0 : rarity.hashCode());
        hash = 31 * hash + (target == null ? 0 : target.hashCode());
        hash = 31 * hash + (level == null ? 0 : level.hashCode());
        hash = 31 * hash + (lore == null ? 0 : lore.hashCode());
        hash = 31 * hash + (attributes == null ? 0 : attributes.hashCode());
        hash = 31 * hash + (super.getKey().hashCode());

        return hash;
    }

    public static boolean hasEnchant(ItemStack is, CustomEnchantment e) {
        if (!is.hasItemMeta()) {
            return false;
        }

        ItemMeta im = is.getItemMeta();

        if(im == null){
            return false;
        }

        PersistentDataContainer persistentDataContainer = im.getPersistentDataContainer();
        return persistentDataContainer.has(e.getKey());
    }
}