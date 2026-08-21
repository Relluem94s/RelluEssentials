package de.relluem94.minecraft.server.spigot.essentials.helpers;

import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.EnchantLevel;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.EnchantName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for managing custom enchantments on {@link ItemStack}s. Provides functionality to
 * add, remove and inspect custom enchantments, as well as creating enchanted books.
 *
 * @author rellu
 */
public class EnchantmentHelper extends CustomEnchantment {

  /**
   * Creates an {@link EnchantmentHelper} with only a {@link NamespacedKey}.
   *
   * @param id the unique key identifying this enchantment
   */
  public EnchantmentHelper(NamespacedKey id) {
    super(id);
  }

  /**
   * Creates a fully configured {@link EnchantmentHelper}.
   *
   * @param enchantName the name of the enchantment
   * @param target      the item target this enchantment applies to
   * @param level       the level configuration of this enchantment
   * @param lore        the lore text displayed on items with this enchantment
   * @param rarity      the rarity of this enchantment
   * @param attributes  the attribute modifiers applied by this enchantment
   */
  public EnchantmentHelper(EnchantName enchantName, EnchantmentTarget target, EnchantLevel level,
      String lore, Rarity rarity, Multimap<Attribute, AttributeModifier> attributes, int cost) {
    super(new NamespacedKey(RelluEssentials.getInstance(), enchantName.name()));
    this.enchantName = enchantName;
    this.rarity = rarity;
    this.target = target;
    this.level = level;
    this.lore = lore;
    this.attributes = attributes;
    this.actualLevel = level.startLevel();
    this.cost = cost;
  }

  /**
   * Checks whether the given {@link ItemStack} has the specified {@link CustomEnchantment}.
   *
   * @param is the item to check
   * @param e  the enchantment to look for
   * @return {@code true} if the item has the enchantment, {@code false} otherwise
   */
  public static boolean hasEnchant(ItemStack is, CustomEnchantment e) {
    if (is == null) {
      return false;
    }

    ItemMeta im = is.getItemMeta();

    if (im == null) {
      return false;
    }

    PersistentDataContainer persistentDataContainer = im.getPersistentDataContainer();
    return persistentDataContainer.has(e.getKey());
  }

  /**
   * Returns the attribute multiplier for this enchantment.
   *
   * @return the multiplier value
   */
  @SuppressWarnings("unused")
  public double getMultiplier() {
    return multiply;
  }

  /**
   * Returns the internal name of this enchantment.
   *
   * @return the enchantment name as a string
   */
  @NonNull
  public String getName() {
    return enchantName.name();
  }

  /**
   * Returns the display name of this enchantment shown to players.
   *
   * @return the formatted display name
   */
  public String getDisplayName() {
    return enchantName.displayName();
  }

  /**
   * Returns the maximum level this enchantment can reach.
   *
   * @return the maximum enchantment level
   */
  public int getMaxLevel() {
    return level.maxLevel();
  }

  /**
   * Returns the starting level of this enchantment.
   *
   * @return the starting enchantment level
   */
  public int getStartLevel() {
    return level.startLevel();
  }

  /**
   * Returns the {@link EnchantmentTarget} defining which items this enchantment can be applied to.
   *
   * @return the enchantment target
   */
  @NonNull
  public EnchantmentTarget getItemTarget() {
    return target;
  }

  @Getter
  private int cost;

  /**
   * Creates and returns an enchanted book {@link ItemHelper} containing this enchantment.
   *
   * @return an {@link ItemHelper} representing the enchanted book
   */
  @Deprecated
  public ItemHelper getBook() {
    return new ItemHelper(
        addBookEnchantment(
            new ItemStack(Material.ENCHANTED_BOOK), this
        ),
        enchantName.displayName(),
        ItemHelper.Type.ENCHANTMENT,
        getRarity(),
        cost
    );
  }

  private  @NotNull ItemStack addBookEnchantment(@NotNull ItemStack item,
      EnchantmentHelper enchantment) {
    if (item.getItemMeta() instanceof EnchantmentStorageMeta meta) {
      meta.getPersistentDataContainer()
          .set(enchantment.getKey(), PersistentDataType.INTEGER, enchantment.getStartLevel());
      item.setItemMeta(meta);
    }

    return item;
  }

  /**
   * Applies this enchantment to the given {@link ItemMeta} by adding attribute modifiers, updating
   * the item lore and storing the enchantment level in the persistent data container.
   *
   * @param im the meta to apply the enchantment to
   */
  public void addTo(ItemMeta im) {
    if (im == null) {
      return;
    }

    for (Attribute a : attributes.asMap().keySet()) {
      if (a == null) {
        continue;
      }
      for (AttributeModifier am : attributes.asMap().get(a)) {
        if (am == null) {
          continue;
        }
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
      itemStackLore = new ArrayList<>();
      itemStackLore.add(getDisplayName());
      itemStackLore.add(getLore());
      itemStackLore.add(getRarity().getPrefix() + getRarity().getDisplayName());
    }

    im.setLore(itemStackLore);
    PersistentDataContainer persistentDataContainer = im.getPersistentDataContainer();
    persistentDataContainer.set(super.getKey(), PersistentDataType.INTEGER, actualLevel);
  }

  /**
   * Applies this enchantment to the given {@link ItemStack} by adding attribute modifiers, updating
   * the item lore and storing the enchantment level in the persistent data container.
   *
   * @param i the item to apply the enchantment to
   */
  public void addTo(ItemStack i) {
    ItemMeta im = i.getItemMeta();
    if (im == null) {
      return;
    }
    addTo(im);
    i.setItemMeta(im);
  }

  /**
   * Removes this enchantment from the given {@link ItemStack} by stripping attribute modifiers,
   * cleaning up the item lore and deleting the enchantment entry from the persistent data
   * container.
   *
   * @param i the item to remove the enchantment from
   */
  public void removeFrom(ItemStack i) {
    ItemMeta im = i.getItemMeta();

    if (im == null) {
      return;
    }

    for (Attribute a : attributes.asMap().keySet()) {
      if (a == null) {
        continue;
      }
      for (AttributeModifier am : attributes.asMap().get(a)) {
        if (am == null) {
          continue;
        }
        im.removeAttributeModifier(a, am);
      }
    }

    List<String> itemStackLore = im.getLore();
    if (itemStackLore != null) {
      itemStackLore.remove(getDisplayName());
      itemStackLore.remove(getLore());
      itemStackLore.remove(getRarity().getPrefix() + getRarity().getDisplayName());
    }

    im.setLore(itemStackLore);
    PersistentDataContainer persistentDataContainer = im.getPersistentDataContainer();
    persistentDataContainer.remove(super.getKey());

    i.setItemMeta(im);

  }

  @Override
  public boolean equals(Object o) {
    return o instanceof EnchantmentHelper && o.hashCode() == this.hashCode();
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
}