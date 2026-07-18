# InventoryHelper

Utility class providing static helper methods for managing Bukkit inventories.

> **Note:** This class cannot be instantiated (utility class).

- **Author:** rellu

---

## Methods

### `public static int inventorySize(int amount)`

Calculates the smallest valid inventory size (multiple of 9) for the given number of items.

| Parameter | Type | Description |
|-----------|------|-------------|
| `amount`  | `int` | Number of items in the inventory |

**Returns:** `int` – Required inventory size (9, 18, 27, 36, 45 or 54)

**Example:**
```java
int size = InventoryHelper.inventorySize(15); // returns 18
```

---

### `public static Inventory createInventory(int size, String name)`

Creates a new Bukkit inventory with the given size and name.

| Parameter | Type | Description |
|-----------|------|-------------|
| `size`    | `int` | Size of the inventory (must be a multiple of 9) |
| `name`    | `String` | Display name of the inventory |

**Returns:** `Inventory` – The created Bukkit inventory

---

### `public static void createInventory(String json, Player p)`

Populates a player's inventory based on a JSON string.  
The inventory is cleared before being populated.

| Parameter | Type | Description |
|-----------|------|-------------|
| `json`    | `String` | JSON representation of the inventory |
| `p`       | `Player` | Player whose inventory will be populated |

> ⚠️ **Note:** Slots missing from the JSON or containing invalid ItemStacks are skipped. Errors are printed to the console.

---

### `public static JSONObject saveInventoryToJSON(Player p)`

Serializes a player's inventory into a `JSONObject`.

| Parameter | Type | Description |
|-----------|------|-------------|
| `p`       | `Player` | Player whose inventory will be saved |

**Returns:** `JSONObject` – JSON representation of the inventory

---

### `public static void updateInventory(CommandSender sender)`

> ⚠️ **Deprecated – internal use only.**  
> Updates a player's inventory. Should not be used in new code.

| Parameter | Type | Description |
|-----------|------|-------------|
| `sender`  | `CommandSender` | Player whose inventory will be updated |

---

### `public static void closeInventory(CommandSender sender)`

Closes the currently open inventory of a player.  
No effect if `sender` is not a `Player`.

| Parameter | Type | Description |
|-----------|------|-------------|
| `sender`  | `CommandSender` | Player whose inventory will be closed |

---

### `public static void openInventory(CommandSender sender, Inventory inv)`

Opens an inventory for a player.  
No effect if `sender` is not a `Player`.

| Parameter | Type | Description |
|-----------|------|-------------|
| `sender`  | `CommandSender` | Player for whom the inventory will be opened |
| `inv`     | `Inventory` | The inventory to open |

---

### `public static Inventory fillInventory(Inventory inv, ItemStack is)`

Fills all slots of an inventory with the given ItemStack.

| Parameter | Type | Description |
|-----------|------|-------------|
| `inv`     | `Inventory` | The inventory to fill |
| `is`      | `ItemStack` | Item to fill the inventory with |

**Returns:** `Inventory` – The filled inventory (same instance as `inv`)

---

### `public static int getSkipsSize()`

Returns the number of slots that are skipped during slot navigation (e.g. border slots).

**Returns:** `int` – Number of skip slots

---

### `public static int getNextSlot(int slot)`

Returns the next usable slot that is not contained in the skip list.

| Parameter | Type | Description |
|-----------|------|-------------|
| `slot`    | `int` | Starting slot for the search |

**Returns:** `int` – Next available slot, or `-1` if none was found

---

### `public static Inventory getCustomItemInventory(CustomInventory ci)`

> 🔒 **Internal use only** (`@ApiStatus.Internal`)  
> Creates an inventory with custom items based on a `CustomInventory` configuration.

| Parameter | Type | Description |
|-----------|------|-------------|
| `ci`      | `CustomInventory` | Configuration object for the inventory |

**Returns:** `Inventory` – Populated Bukkit inventory

---

### `public static Inventory getCustomItemInventory(List<ItemHelper> customItems, CustomInventory ci)`

Creates an inventory with a custom item list based on a `CustomInventory` configuration.

| Parameter | Type | Description |
|-----------|------|-------------|
| `customItems` | `List<ItemHelper>` | List of available custom items |
| `ci`          | `CustomInventory` | Configuration object for the inventory |

**Returns:** `Inventory` – Populated Bukkit inventory

---

### `public static Inventory getCustomItemInventory(List<ItemHelper> customItems, String guiTitle, int guiSize, ItemHelper.Type itemType)`

Creates an inventory and populates it with custom items of the specified type.

| Parameter     | Type | Description |
|---------------|------|-------------|
| `customItems` | `List<ItemHelper>` | List of available custom items |
| `guiTitle`    | `String` | Title of the inventory |
| `guiSize`     | `int` | Size of the inventory |
| `itemType`    | `ItemHelper.Type` | Type of items to display |

**Returns:** `Inventory` – Filtered and populated Bukkit inventory

---

### `public static boolean isCustomItemInMainHand(Player player, ItemHelper customItem)`

Checks whether a player is holding a specific custom item in their main hand.

| Parameter     | Type | Description |
|---------------|------|-------------|
| `player`      | `Player` | The player to check |
| `customItem`  | `ItemHelper` | The expected custom item |

**Returns:** `boolean` – `true` if the item is held in the main hand, otherwise `false`