# Documentation

## `public class BagHelper`

* **Author:** rellu

## Constants

### `public static final int BAG_SIZE`

The number of slots in a bag.

* **Value:** `28`

### `public static final String MAIN_GUI`

The title of the main bag GUI, loaded from the language configuration.

---

## `public static Inventory getBag(int type, PlayerEntry pe)`

Returns the inventory GUI for a specific bag of a player.

* **Parameters:**
   * `type` — int — the bag type ID
   * `pe` — PlayerEntry
* **Returns:** Inventory or null if the player does not own the bag

---

## `public static Inventory getBags(boolean npc, String title)`

Returns an inventory GUI listing all available bag types.

* **Parameters:**
   * `npc` — boolean — if true, items show buy information; if false, items show open information
   * `title` — String — the title of the inventory
* **Returns:** Inventory

---

## `public static Inventory getBags(PlayerEntry pe)`

Returns an inventory GUI listing only the bags the player owns.

* **Parameters:**
   * `pe` — PlayerEntry
* **Returns:** Inventory

---

## `public static ItemHelper getItem(BagTypeEntry bte, boolean npc)`

Creates an ItemHelper representing a bag type for display in a GUI.

* **Parameters:**
   * `bte` — BagTypeEntry
   * `npc` — boolean — if true, lore shows buy information; if false, lore shows open information
* **Returns:** ItemHelper

---

## `public static ItemStack[] getItemStacks(BagTypeEntry bte)`

Returns an array of ItemStacks representing all slots of the given bag type.

* **Parameters:**
   * `bte` — BagTypeEntry
* **Returns:** ItemStack[] — array of size `BAG_SIZE`

---

## `public static int getSlotByItemStack(BagEntry be, ItemStack is)`

Returns the slot index within a bag that matches the given ItemStack.

* **Parameters:**
   * `be` — BagEntry
   * `is` — ItemStack — amount is ignored during comparison
* **Returns:** int — the matching slot index, or `-1` if not found

---

## `public static boolean hasBag(int type, PlayerEntry pe)`

Checks if a player owns a bag of the given type by querying the database.

* **Parameters:**
   * `type` — int — the bag type ID
   * `pe` — PlayerEntry
* **Returns:** boolean

---

## `public static boolean hasBag(int playerFK, int bagType)`

Checks if a player owns a bag of the given type using the in-memory player bag map.

* **Parameters:**
   * `playerFK` — int — the player's database ID
   * `bagType` — int — the bag type ID
* **Returns:** boolean

---

## `public static boolean hasBags(int playerFK)`

Checks if a player owns any bags.

* **Parameters:**
   * `playerFK` — int — the player's database ID
* **Returns:** boolean

---

## `public static BagEntry getBag(int playerFK, int bagType)`

Returns the BagEntry for a specific player and bag type from the in-memory player bag map.

* **Parameters:**
   * `playerFK` — int — the player's database ID
   * `bagType` — int — the bag type ID
* **Returns:** BagEntry or null if not found

---

## `public static BagTypeEntry getBagTypeByName(String name)`

Returns the BagTypeEntry whose display name or internal name matches the given string.

* **Parameters:**
   * `name` — String — partial or full name to match against
* **Returns:** BagTypeEntry or null if not found

---

## `public static Collection<BagEntry> getBags(int playerFK)`

Returns all bags owned by a player from the in-memory player bag map.

* **Parameters:**
   * `playerFK` — int — the player's database ID
* **Returns:** Collection of BagEntry — empty collection if the player owns no bags

---

## `public static BagTypeEntry getBagTypeById(int id)`

Returns the BagTypeEntry with the given ID.

* **Parameters:**
   * `id` — int — the bag type ID
* **Returns:** BagTypeEntry or null if not found

---

## `public static List<Item> collectItems(List<Item> li, Player p, PlayerEntry pe)`

Collects a list of dropped Item entities into the player's bags if the items match tracked bag slots.

* **Parameters:**
   * `li` — List of Item — dropped item entities to evaluate
   * `p` — Player — the player collecting the items
   * `pe` — PlayerEntry
* **Returns:** List of Item — items that were successfully collected into a bag

---

## `public static List<ItemStack> collectItemStacks(List<ItemStack> li, Player p, PlayerEntry pe)`

Collects a list of ItemStacks into the player's bags if the items match tracked bag slots.

* **Parameters:**
   * `li` — List of ItemStack — item stacks to evaluate
   * `p` — Player — the player collecting the items
   * `pe` — PlayerEntry
* **Returns:** List of ItemStack — item stacks that were successfully collected into a bag

---

## `public static boolean collectItem(Item item, Player p, PlayerEntry pe)`

Attempts to collect a single dropped Item entity into the player's bags.

* **Parameters:**
   * `item` — Item — the dropped item entity to evaluate
   * `p` — Player — the player collecting the item
   * `pe` — PlayerEntry
* **Returns:** boolean — true if the item was collected into a bag, false otherwise

---

## `public static void saveBags()`

Persists all bag entries that have been modified since the last save to the database and notifies admins of the number of updated bags.