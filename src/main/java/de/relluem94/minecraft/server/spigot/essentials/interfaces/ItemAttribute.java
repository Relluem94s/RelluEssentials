package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

/**
 * Represents a pairing of a Bukkit {@link Attribute} and its corresponding
 * {@link AttributeModifier}, used to apply custom attribute modifications to items.
 *
 * @author rellu
 */
public interface ItemAttribute {
  /**
   * Returns the {@link Attribute} that this item attribute targets.
   *
   * @return the {@link Attribute} to be modified
   */
  Attribute getAttribute();

  /**
   * Returns the {@link AttributeModifier} defining how the attribute is modified.
   *
   * @return the {@link AttributeModifier} applied to the attribute
   */
  AttributeModifier getModifier();
}