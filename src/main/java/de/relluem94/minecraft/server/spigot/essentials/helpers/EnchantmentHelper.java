package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ItemAttribute;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.EnchantLevel;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.EnchantName;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem.Rarity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

/**
 * Helper class for managing custom enchantments on {@link ItemStack}s. Provides functionality to
 * add, remove and inspect custom enchantments, as well as creating enchanted books.
 *
 * @author rellu
 */
@EqualsAndHashCode(callSuper = false)
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
   * @param itemAttributes  the attribute modifiers applied by this enchantment
   * @param cost        the cost of this enchantment
   */
  public EnchantmentHelper(EnchantName enchantName, EnchantmentTarget target, EnchantLevel level,
      String lore, Rarity rarity, List<ItemAttribute> itemAttributes, int cost) {
    super(new NamespacedKey(RelluEssentials.getInstance(), enchantName.name()));
    this.enchantName = enchantName;
    this.rarity = rarity;
    this.target = target;
    this.level = level;
    this.lore = lore;
    this.itemAttributes = itemAttributes;
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
   * Creates and returns an enchanted book {@link ItemStack} containing this enchantment.
   *
   * @return an {@link ItemStack} representing the enchanted book
   */
  public ItemStack createEnchantedBook() {
    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
    if (book.getItemMeta() instanceof EnchantmentStorageMeta meta) {
      meta.getPersistentDataContainer()
          .set(getKey(), PersistentDataType.INTEGER, getStartLevel());
      meta.setDisplayName(getDisplayName());
      meta.setLore(List.of(getLore()));
      book.setItemMeta(meta);
    }
    return book;
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

    for (ItemAttribute itemAttribute : itemAttributes) {
      if (itemAttribute == null || itemAttribute.getAttribute() == null || itemAttribute.getModifier() == null) {
        continue;
      }
      im.addAttributeModifier(itemAttribute.getAttribute(), itemAttribute.getModifier());
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

    for (ItemAttribute itemAttribute : itemAttributes) {
      if (itemAttribute == null || itemAttribute.getAttribute() == null || itemAttribute.getModifier() == null) {
        continue;
      }
      im.removeAttributeModifier(itemAttribute.getAttribute(), itemAttribute.getModifier());
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
}