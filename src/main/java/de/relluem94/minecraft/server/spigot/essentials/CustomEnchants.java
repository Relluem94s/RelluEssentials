package de.relluem94.minecraft.server.spigot.essentials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;

import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.EnchantLevel;
import de.relluem94.minecraft.server.spigot.essentials.enchantment.EnchantName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AttributeHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;

public class CustomEnchants {

    private CustomEnchants(){
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static final List<EnchantmentHelper> customEnchantments = new ArrayList<>();

    public static final EnchantmentHelper autosmelt = new EnchantmentHelper(
        new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT, EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_DISPLAYNAME), 
        EnchantmentTarget.TOOL, 
        new EnchantLevel(1, 1), 
        Arrays.asList(Enchantment.SILK_TOUCH), 
        EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LORE, 
        Rarity.LEGENDARY,
        AttributeHelper.addAttribute()
    );

    public static final EnchantmentHelper telekinesis = new EnchantmentHelper(
        new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS, EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS_DISPLAYNAME), 
        EnchantmentTarget.BREAKABLE, 
        new EnchantLevel(1, 1), 
        Arrays.asList(Enchantment.SILK_TOUCH), 
        EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS_LORE, 
        Rarity.LEGENDARY,
        AttributeHelper.addAttribute()
    );

    public static final EnchantmentHelper replenishment = new EnchantmentHelper(
        new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT, EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT_DISPLAYNAME), 
        EnchantmentTarget.TOOL, 
        new EnchantLevel(1, 1), 
        Arrays.asList(Enchantment.SILK_TOUCH), 
        EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT_LORE, 
        Rarity.LEGENDARY,
        AttributeHelper.addAttribute()
    );

    public static final EnchantmentHelper delicate = new EnchantmentHelper(
        new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE, EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE_DISPLAYNAME), 
        EnchantmentTarget.TOOL, 
        new EnchantLevel(1, 1), 
        Arrays.asList(Enchantment.SILK_TOUCH), 
        EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE_LORE, 
        Rarity.LEGENDARY,
        AttributeHelper.addAttribute()
    );

    public static final EnchantmentHelper thunderstrike = new EnchantmentHelper(
        new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE, EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE_DISPLAYNAME), 
        EnchantmentTarget.WEAPON, 
        new EnchantLevel(1,1), 
        Arrays.asList(Enchantment.FIRE_ASPECT), 
        EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE_LORE, 
        Rarity.LEGENDARY,
        AttributeHelper.addAttribute(
            EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE_UUID, 
            Attribute.GENERIC_ATTACK_DAMAGE, 
            Operation.ADD_NUMBER, 
            EquipmentSlot.HAND, 
            EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE, 
            50
        )
    );

    public static final EnchantmentHelper scavengers = new EnchantmentHelper(
        new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS, EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS_DISPLAYNAME), 
        EnchantmentTarget.WEAPON, 
        new EnchantLevel(1, 3), 
        Arrays.asList(Enchantment.VANISHING_CURSE), 
        EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS_LORE, 
        Rarity.EPIC,
        AttributeHelper.addAttribute()
    );
}