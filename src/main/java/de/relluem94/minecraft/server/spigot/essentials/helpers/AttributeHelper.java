package de.relluem94.minecraft.server.spigot.essentials.helpers;

import javax.annotation.Nonnull;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unstable")
public class AttributeHelper {

    private AttributeHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    @Contract(" -> new")
    public static @NotNull Multimap<Attribute, AttributeModifier> addAttribute(){
        return ArrayListMultimap.create();
    }

    @SuppressWarnings("UnstableApiUsage")
    public static @NotNull Multimap<Attribute, AttributeModifier> addAttribute(@Nonnull Attribute attribute, Operation operation, EquipmentSlotGroup slot, String name, double multiplier){
        Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
        attributes.put(attribute, new AttributeModifier(new NamespacedKey(RelluEssentials.getInstance(), name), multiplier, operation, slot));
        return attributes;
    }

    @Contract("_, _, _, _, _, _ -> param1")
    @SuppressWarnings({"UnstableApiUsage", "unused"})
    public static @NotNull Multimap<Attribute, AttributeModifier> addAttribute(@NotNull Multimap<Attribute, AttributeModifier> attributes, @Nonnull Attribute attribute, Operation operation, EquipmentSlotGroup slot, String name, double multiplier){
        attributes.put(attribute, new AttributeModifier(new NamespacedKey(RelluEssentials.getInstance(), name), multiplier, operation, slot));
        return attributes;
    }
}
