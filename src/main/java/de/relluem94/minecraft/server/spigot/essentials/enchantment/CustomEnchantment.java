package de.relluem94.minecraft.server.spigot.essentials.enchantment;

import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class CustomEnchantment {
    @Getter
    private final NamespacedKey key;
    @Getter
    protected String lore;
    @Getter
    protected ItemHelper.Rarity rarity;
    protected EnchantmentTarget target;
    protected EnchantLevel level;
    protected EnchantName enchantName;
    @Getter
    protected Multimap<Attribute, AttributeModifier> attributes;
    @SuppressWarnings("unused")
    protected double multiply;
    protected int actualLevel;

    public CustomEnchantment(NamespacedKey key){
        this.key = key;
    }
}
