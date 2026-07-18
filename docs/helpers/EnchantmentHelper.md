# Documentation

## `public class EnchantmentHelper extends CustomEnchantment`

* **Author:** rellu

## `public EnchantmentHelper(NamespacedKey id)`

Creates an EnchantmentHelper with a NamespacedKey

* **Parameters:**
   * `id` — NamespacedKey

## `public EnchantmentHelper(EnchantName enchantName, EnchantmentTarget target, EnchantLevel level, String lore, Rarity rarity, Multimap<Attribute, AttributeModifier> attributes)`

Creates an EnchantmentHelper with all required properties and registers it to the custom enchantments list

* **Parameters:**
   * `enchantName` — EnchantName
   * `target` — EnchantmentTarget
   * `level` — EnchantLevel
   * `lore` — String
   * `rarity` — Rarity
   * `attributes` — Multimap of Attribute and AttributeModifier

## `public double getMultiplier()`

Returns the multiplier of the enchantment

* **Returns:** double

## `public String getName()`

Returns the name of the enchantment

* **Returns:** String

## `public String getDisplayName()`

Returns the display name of the enchantment

* **Returns:** String

## `public int getMaxLevel()`

Returns the maximum level of the enchantment

* **Returns:** int

## `public int getStartLevel()`

Returns the start level of the enchantment

* **Returns:** int

## `public EnchantmentTarget getItemTarget()`

Returns the enchantment target of the enchantment

* **Returns:** EnchantmentTarget

## `public ItemHelper getBook()`

Returns an ItemHelper representing an Enchanted Book with this enchantment applied

* **Returns:** ItemHelper

## `public void addTo(ItemStack i)`

Adds the enchantment with its attributes and lore to the given ItemStack and stores the enchantment level in the PersistentDataContainer

* **Parameters:**
   * `i` — ItemStack

## `public void removeFrom(ItemStack i)`

Removes the enchantment with its attributes and lore from the given ItemStack and removes the enchantment level from the PersistentDataContainer

* **Parameters:**
   * `i` — ItemStack

## `public static boolean hasEnchant(ItemStack is, CustomEnchantment e)`

Checks if an ItemStack has a specific CustomEnchantment applied

* **Parameters:**
   * `is` — ItemStack
   * `e` — CustomEnchantment
* **Returns:** boolean