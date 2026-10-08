package de.relluem94.minecraft.server.spigot.essentials.models.enchantment;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.ItemAttribute;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.List;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;

/**
 * Represents a custom enchantment with configurable attributes, rarity, and level information.
 *
 * @author rellu
 */
public class CustomEnchantment {

  @Getter
  private final NamespacedKey key;
  @Getter
  protected String lore;
  @Getter
  protected CustomItem.Rarity rarity;
  protected EnchantmentTarget target;
  protected EnchantLevel level;
  protected EnchantName enchantName;
  @Getter
  protected List<ItemAttribute> itemAttributes;
  protected double multiply;
  protected int actualLevel;

  /**
   * Creates a new {@link CustomEnchantment} with the given {@link NamespacedKey}.
   *
   * @param key the unique {@link NamespacedKey} identifying this enchantment
   */
  public CustomEnchantment(NamespacedKey key) {
    this.key = key;
  }
}
