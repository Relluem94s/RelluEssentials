package de.relluem94.minecraft.server.spigot.essentials.models.items;

import de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * An immutable data model representing the blueprint of a custom item.
 * This class contains no Bukkit-specific instances (like ItemStack or Enchantment)
 * to ensure high testability and easy serialization.
 *
 * @param material The base material of the item.
 * @param amount The quantity of the item.
 * @param displayName The display name of the item.
 * @param lore The list of lore lines.
 * @param type The custom item type.
 * @param rarity The rarity level.
 * @param cost The price of the item.
 * @param enchantments A list of enchantment data (key and level).
 * @param persistentData A map of persistent data (NamespacedKey and value).
 * @param metaModifiers A list of functions to apply custom logic to the ItemMeta.
 * @param relluEssentialsNamespacedKey The unique identifier for this item in the registry.
 */
public record CustomItem(
    Material material,
    int amount,
    String displayName,
    List<String> lore,
    Type type,
    Rarity rarity,
    Integer cost,
    List<EnchantmentData> enchantments,
    Map<String, Object> persistentData,
    List<Consumer<ItemMeta>> metaModifiers,
    RelluEssentialsNamespacedKey relluEssentialsNamespacedKey
) {

  /**
   * Data holder for enchantments to avoid using Bukkit's Enchantment class directly.
   *
   * @param key   The NamespacedKey of the enchantment as a string.
   * @param level The enchantment level.
   */
  public record EnchantmentData(String key, int level) {}

  public enum Type {
    TOOL, INGREDIENT, GADGET, ARMOR, WEAPON, HUB, DECORATION,
    BUILDING, NPC, NPC_GUI, ENCHANTMENT, MONEY, ADMIN_TOOL, NONE
  }

  @Getter
  public enum Rarity {
    NONE("", "", -1),
    COMMON("Common", "§f§l", 0),
    UNCOMMON("Uncommon", "§a§l", 1),
    RARE("Rare", "§9§l", 2),
    EPIC("Epic", "§5§l", 3),
    LEGENDARY("Legendary", "§6§l", 4);

    private final String displayName;
    private final String prefix;
    private final int level;

    Rarity(String displayName, String prefix, int level) {
      this.displayName = displayName;
      this.prefix = prefix;
      this.level = level;
    }
  }

  /**
   * Converts this data model into a Bukkit ItemStack, applying all properties including
   * enchantments and persistent data.
   *
   * @return A new ItemStack representing this custom item.
   */
  public ItemStack toItemStack() {
    ItemStack itemStack = new ItemStack(material, amount);
    ItemMeta meta = itemStack.getItemMeta();

    if (meta != null) {
      if (displayName != null && !displayName.isEmpty()) {
        meta.setDisplayName(displayName);
      }

      if (!lore.isEmpty()) {
        meta.setLore(lore);
      }

      if (rarity.level != -1) {
        List<String> currentLore = meta.getLore();
        if (currentLore != null) {
          currentLore.remove(Rarity.COMMON.getPrefix() + Rarity.COMMON.getDisplayName());
          currentLore.remove(Rarity.UNCOMMON.getPrefix() + Rarity.UNCOMMON.getDisplayName());
          currentLore.remove(Rarity.RARE.getPrefix() + Rarity.RARE.getDisplayName());
          currentLore.remove(Rarity.EPIC.getPrefix() + Rarity.EPIC.getDisplayName());
          currentLore.remove(Rarity.LEGENDARY.getPrefix() + Rarity.LEGENDARY.getDisplayName());
          currentLore.add(rarity.getPrefix() + rarity.getDisplayName());
          meta.setLore(currentLore);
        } else {
          meta.setLore(List.of(rarity.getPrefix() + rarity.getDisplayName()));
        }
      }

      for (EnchantmentData enchantment : enchantments) {
        NamespacedKey enchantmentKey = NamespacedKey.fromString(enchantment.key());
        if (enchantmentKey != null) {
          Enchantment bukkitEnchantment = Registry.ENCHANTMENT.get(enchantmentKey);
          if (bukkitEnchantment != null) {
            meta.addEnchant(bukkitEnchantment, enchantment.level(), true);
          }
        }
      }

      if (cost != null) {
        meta.getPersistentDataContainer().set(NamespacedKeyConstants.itemCost(), PersistentDataType.INTEGER, cost);
      }

      for (Map.Entry<String, Object> entry : persistentData.entrySet()) {
        NamespacedKey dataKey = NamespacedKey.fromString(entry.getKey());
        if (dataKey != null) {
          Object value = entry.getValue();
          if (value instanceof String stringValue) {
            meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, stringValue);
          } else if (value instanceof Integer integerValue) {
            meta.getPersistentDataContainer().set(dataKey, PersistentDataType.INTEGER, integerValue);
          } else if (value instanceof Boolean booleanValue) {
            meta.getPersistentDataContainer().set(dataKey, PersistentDataType.BYTE, (byte) (booleanValue ? 1 : 0));
          } else if (value instanceof Double doubleValue) {
            meta.getPersistentDataContainer().set(dataKey, PersistentDataType.DOUBLE, doubleValue);
          }
        }
      }

      for (Consumer<ItemMeta> modifier : metaModifiers) {
        modifier.accept(meta);
      }

      itemStack.setItemMeta(meta);
    }

    return itemStack;
  }
}