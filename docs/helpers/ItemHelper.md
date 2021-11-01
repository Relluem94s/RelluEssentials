# Documentation

## `public class ItemHelper implements IItemHelper`

 * **Author:** rellu

## `public ItemHelper(Material material, int amount, String displayName, ItemType itemType, ItemRarity itemRarity)`

 * **Parameters:**
   * `material` — Bukkit Material
   * `amount` — Integer
   * `displayName` — String
   * `itemType` — ItemType
   * `itemRarity` — ItemRarity

## `public ItemHelper(Material material, int amount, String displayName, ItemType itemType, ItemRarity itemRarity, List<String> lore)`

 * **Parameters:**
   * `material` — Bukkit Material
   * `amount` — Integer
   * `displayName` — String
   * `itemType` — ItemType
   * `itemRarity` — ItemRarity
   * `lore` — List String

## `public ItemStack getCustomItem()`

 * **Returns:** ItemStack of ItemHelper

## `protected ItemStack getItemStack()`

 * **Returns:** ItemStack of ItemHelper

## `public int getAmount()`

 * **Returns:** int Amount of ItemHelper

## `public String getDisplayName()`

 * **Returns:** String DisplayName of ItemHelper

## `public List<String> getLore()`

 * **Returns:** List of String Lore of ItemHelper

## `public Material getMaterial()`

 * **Returns:** Material of ItemHelper

## `public ItemType getItemType()`

 * **Returns:** ItemType of ItemHelper

## `public ItemMeta getItemMeta()`

 * **Returns:** ItemMeta of ItemStack

## `public void setItemMeta(ItemMeta itemmeta)`

 * **Parameters:** `itemmeta` — ItemMeta sets ItemMeta of ItemStack

## `public boolean equals(ItemStack compare)`

 * **Parameters:** `compare` — 
 * **Returns:** boolean

## `public boolean almostEquals(ItemStack compare)`

 * **Parameters:** `compare` — 
 * **Returns:** boolean
