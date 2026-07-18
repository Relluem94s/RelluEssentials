# ItemHelper

## `public class ItemHelper implements IItemHelper`

A helper class for creating and managing custom Bukkit `ItemStack` objects with metadata such as display name, lore, item type, and rarity.

- **Author:** rellu

---

## Constructors

### `public ItemHelper(Material material, int amount, String displayName, Type itemType, Rarity itemRarity)`

Creates a new `ItemHelper` from a Bukkit `Material`.

| Parameter | Type | Description |
|---|---|---|
| `material` | `Material` | The Bukkit material of the item |
| `amount` | `int` | The stack size |
| `displayName` | `String` | The display name of the item |
| `itemType` | `Type` | The item type category |
| `itemRarity` | `Rarity` | The rarity of the item |

---

### `public ItemHelper(Material material, int amount, String displayName, Type itemType, Rarity itemRarity, List<String> lore)`

Creates a new `ItemHelper` from a Bukkit `Material` with lore.

| Parameter | Type | Description |
|---|---|---|
| `material` | `Material` | The Bukkit material of the item |
| `amount` | `int` | The stack size |
| `displayName` | `String` | The display name of the item |
| `itemType` | `Type` | The item type category |
| `itemRarity` | `Rarity` | The rarity of the item |
| `lore` | `List<String>` | The lore lines of the item |

---

### `public ItemHelper(ItemStack is, String displayName, Type itemType, Rarity itemRarity)`

Creates a new `ItemHelper` from an existing `ItemStack`.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The existing ItemStack to wrap |
| `displayName` | `String` | The display name of the item |
| `itemType` | `Type` | The item type category |
| `itemRarity` | `Rarity` | The rarity of the item |

---

### `public ItemHelper(ItemStack is, String displayName, Type itemType, Rarity itemRarity, List<String> lore)`

Creates a new `ItemHelper` from an existing `ItemStack` with lore.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The existing ItemStack to wrap |
| `displayName` | `String` | The display name of the item |
| `itemType` | `Type` | The item type category |
| `itemRarity` | `Rarity` | The rarity of the item |
| `lore` | `List<String>` | The lore lines of the item |

---

## Instance Methods

### `public ItemStack getCustomItem()`

Calls `init()`, applies the item rarity to the lore, then calls `postInit()`.
Intended to be the primary method for retrieving the fully built item.

- **Returns:** The fully initialized `ItemStack`

---

### `protected ItemStack getItemStack()`

Returns the raw internal `ItemStack` without running `init()` or `postInit()`.

- **Returns:** The internal `ItemStack`

---

### `public int getAmount()`

- **Returns:** The stack size of the item

---

### `public String getDisplayName()`

- **Returns:** The display name of the item

---

### `public List<String> getLore()`

- **Returns:** The lore lines of the item, or `null` if none were set

---

### `public Material getMaterial()`

- **Returns:** The Bukkit `Material` of the item

---

### `public Type getItemType()`

- **Returns:** The `Type` category of the item

---

### `public ItemMeta getItemMeta()`

- **Returns:** The `ItemMeta` of the internal `ItemStack`

---

### `public void setItemMeta(ItemMeta itemMeta)`

Sets the `ItemMeta` on the internal `ItemStack`.

| Parameter | Type | Description |
|---|---|---|
| `itemMeta` | `ItemMeta` | The `ItemMeta` to apply |

---

### `public void setData(NamespacedKey key, String value)`

Stores a `String` value in the item's `PersistentDataContainer`.

| Parameter | Type | Description |
|---|---|---|
| `key` | `NamespacedKey` | The key to store the value under |
| `value` | `String` | The value to store |

---

### `public String getData(NamespacedKey key)`

Retrieves a `String` value from the item's `PersistentDataContainer`.

| Parameter | Type | Description |
|---|---|---|
| `key` | `NamespacedKey` | The key to look up |

- **Returns:** The stored `String`, or `null` if not present

---

### `public boolean hasData(NamespacedKey key)`

Checks whether the item's `PersistentDataContainer` contains the given key.

| Parameter | Type | Description |
|---|---|---|
| `key` | `NamespacedKey` | The key to check |

- **Returns:** `true` if the key exists, otherwise `false`

---

### `public void removeData(NamespacedKey key)`

Removes a key from the item's `PersistentDataContainer`.

| Parameter | Type | Description |
|---|---|---|
| `key` | `NamespacedKey` | The key to remove |

---

### `public boolean equalsExact(ItemStack compare)`

Compares this item to another `ItemStack` by material and full `ItemMeta` equality.

| Parameter | Type | Description |
|---|---|---|
| `compare` | `ItemStack` | The `ItemStack` to compare against |

- **Returns:** `true` if material and `ItemMeta` are identical

---

### `public boolean equalsName(ItemStack compare)`

Compares this item to another `ItemStack` by material and display name.

| Parameter | Type | Description |
|---|---|---|
| `compare` | `ItemStack` | The `ItemStack` to compare against |

- **Returns:** `true` if material and display name match

---

### `public boolean almostEquals(ItemStack compare)`

Compares this item to another `ItemStack` by material and whether both have `ItemMeta`.

| Parameter | Type | Description |
|---|---|---|
| `compare` | `ItemStack` | The `ItemStack` to compare against |

- **Returns:** `true` if material matches and both items either have or lack `ItemMeta`

---

### `public void init()`

Lifecycle hook called before rarity is applied in `getCustomItem()`.
Override in subclasses to apply custom initialization logic.

---

### `public ItemStack postInit(ItemStack is)`

Lifecycle hook called after rarity is applied in `getCustomItem()`.
Override in subclasses to apply post-processing logic.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The item after rarity has been applied |

- **Returns:** The post-processed `ItemStack`

---

## Static Methods

### `public static Object getData(ItemStack is, NamespacedKey key)`

Retrieves a `String` value from the `PersistentDataContainer` of any `ItemStack`.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The `ItemStack` to read from |
| `key` | `NamespacedKey` | The key to look up |

- **Returns:** The stored `String`, or `null` if not present

---

### `public static boolean hasData(ItemStack is, NamespacedKey key)`

Checks whether any `ItemStack`'s `PersistentDataContainer` contains the given key.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The `ItemStack` to check |
| `key` | `NamespacedKey` | The key to check |

- **Returns:** `true` if the key exists, otherwise `false`

---

### `public static List<String> remove(List<String> lore)`

Removes all rarity entries from a lore list.

| Parameter | Type | Description |
|---|---|---|
| `lore` | `List<String>` | The lore list to clean |

- **Returns:** The modified lore list with all rarity lines removed

---

### `public static ItemStack setDisplayName(ItemStack is, String displayName)`

Sets the display name on any `ItemStack`.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The `ItemStack` to modify |
| `displayName` | `String` | The new display name |

- **Returns:** The modified `ItemStack`

---

### `public static ItemStack getCleanItemStack(ItemStack is)`

Returns a new `ItemStack` of the same material with an amount of `1` and no metadata.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The source `ItemStack` |

- **Returns:** A clean `ItemStack` with amount `1`

---

### `public static ItemStack getCleanItemStackWithAmount(ItemStack is)`

Returns a new `ItemStack` of the same material and amount, with no metadata.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The source `ItemStack` |

- **Returns:** A clean `ItemStack` preserving the original amount

---

### `public static ItemStack addBookEnchantment(ItemStack item, EnchantmentHelper enchantment)`

Stores an enchantment's start level in the `PersistentDataContainer` of an `EnchantmentStorageMeta` item.

| Parameter | Type | Description |
|---|---|---|
| `item` | `ItemStack` | The enchanted book `ItemStack` |
| `enchantment` | `EnchantmentHelper` | The enchantment to store |

- **Returns:** The modified `ItemStack`

---

### `public static String getItemName(ItemStack is)`

Returns the display name of an `ItemStack`, or `"ERROR_404_NAME_NOT_FOUND_EXCEPTION"` if none is set.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The `ItemStack` to read from |

- **Returns:** The display name, or an error string if not found

---

### `public static ItemStack getSmeltedItemStack(ItemStack is)`

Looks up the smelting result for the given `ItemStack` using the server's recipe registry.

| Parameter | Type | Description |
|---|---|---|
| `is` | `ItemStack` | The `ItemStack` to smelt |

- **Returns:** The smelted result `ItemStack` with the same amount, or `null` if no recipe exists

---

### `public static String itemTo64(ItemStack stack)`

Serializes an `ItemStack` to a Base64-encoded string.

| Parameter | Type | Description |
|---|---|---|
| `stack` | `ItemStack` | The `ItemStack` to serialize |

- **Returns:** A Base64 `String` representation of the item
- **Throws:** `IllegalStateException` if serialization fails

---

### `public static ItemStack itemFrom64(String data)`

Deserializes an `ItemStack` from a Base64-encoded string.

| Parameter | Type | Description |
|---|---|---|
| `data` | `String` | The Base64 `String` to deserialize |

- **Returns:** The deserialized `ItemStack`
- **Throws:** `IOException` if decoding or deserialization fails

---

## Enum `Rarity`

Defines the rarity tiers of an item. A level of `-1` (used by `NONE`) suppresses the rarity line in the lore.

| Constant | Display Name | Prefix | Level |
|---|---|---|---|
| `NONE` | *(empty)* | *(empty)* | `-1` |
| `COMMON` | `Common` | `§f§l` | `0` |
| `UNCOMMON` | `Uncommon` | `§a§l` | `1` |
| `RARE` | `Rare` | `§9§l` | `2` |
| `EPIC` | `Epic` | `§5§l` | `3` |
| `LEGENDARY` | `Legendary` | `§6§l` | `4` |

### `public String getDisplayName()`

- **Returns:** The display name of the rarity (e.g. `"Rare"`)

### `public String getPrefix()`

- **Returns:** The color and bold formatting prefix (e.g. `"§9§l"`)

### `public int getLevel()`

- **Returns:** The numeric level of the rarity; `-1` for `NONE`

---

## Enum `Type`

Categorizes the intended use of an item.

| Constant | Description |
|---|---|
| `TOOL` | A tool item |
| `INGREDIENT` | A crafting ingredient |
| `GADGET` | A gadget or special-use item |
| `ARMOR` | An armor piece |
| `WEAPON` | A weapon |
| `HUB` | A hub navigation item |
| `DECORATION` | A decorative item |
| `BUILDING` | A building block item |
| `NPC` | An NPC-related item |
| `NPC_GUI` | An item used in NPC GUIs |
| `ENCHANTMENT` | An enchantment-related item |
| `MONEY` | A currency item |
| `ADMIN_TOOL` | An administrative tool |
| `NONE` | No specific type |