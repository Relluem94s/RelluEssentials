package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ItemAttribute;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class providing helper methods for building {@link org.bukkit.attribute.AttributeModifier}
 * and {@link de.relluem94.minecraft.server.spigot.essentials.interfaces.ItemAttribute} instances
 * used to apply custom attributes to items.
 *
 * @author rellu
 */
public class AttributeHelper {

  private AttributeHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Builds a single-element list containing an {@link ItemAttribute}
   * that wraps the given {@link Attribute} and a constructed {@link AttributeModifier}.
   *
   * @param attribute  the {@link Attribute} to be modified
   * @param operation  the {@link org.bukkit.attribute.AttributeModifier.Operation} defining how the modifier is applied
   * @param slot       the {@link EquipmentSlotGroup} in which the modifier is active
   * @param name       the unique name used to create the {@link org.bukkit.NamespacedKey} for the modifier
   * @param multiplier the numeric value applied to the attribute by the modifier
   * @return an immutable {@link java.util.List} containing a single {@link ItemAttribute}
   */
  @SuppressWarnings("UnstableApiUsage")
  public static @NotNull List<ItemAttribute> buildSingleAttributeList(
      @NotNull Attribute attribute, Operation operation, EquipmentSlotGroup slot, String name,
      double multiplier) {
    AttributeModifier modifier = new AttributeModifier(
        new NamespacedKey(RelluEssentials.getInstance(), name), multiplier, operation, slot);
    return List.of(new ItemAttribute() {
      @Override
      public Attribute getAttribute() {
        return attribute;
      }

      @Override
      public AttributeModifier getModifier() {
        return modifier;
      }
    });
  }
}