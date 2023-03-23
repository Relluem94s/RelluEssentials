package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.relluem94.minecraft.server.spigot.essentials.Strings;

public class AttributeHelper {

    private AttributeHelper() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static Multimap<Attribute, AttributeModifier> addAttribute(){
        Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
        return attributes;
    }

    public static Multimap<Attribute, AttributeModifier> addAttribute(String uuid, Attribute attribute, Operation operation, EquipmentSlot slot, String name, double multiplyer){
        Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
        attributes.put(attribute, new AttributeModifier(UUID.fromString(uuid), name, multiplyer, operation, slot));
        return attributes;
    }

    public static Multimap<Attribute, AttributeModifier> addAttribute(Multimap<Attribute, AttributeModifier> attributes, String uuid, Attribute attribute, Operation operation, EquipmentSlot slot, String name, double multiplyer){
        attributes.put(attribute, new AttributeModifier(UUID.fromString(uuid), name, multiplyer, operation, slot));
        return attributes;
    }
}
