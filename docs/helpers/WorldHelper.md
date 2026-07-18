# Documentation

## `public class WorldHelper`

* **Author:** rellu

## `public static boolean isInWorld(Player player, String worldName)`

Checks if Player is in World with Name

* **Parameters:**
   * `player` — Player
   * `worldName` — String
* **Returns:** boolean

## `public static boolean isInWorld(CommandSender sender, String worldName)`

Checks if CommandSender is in World with Name

* **Parameters:**
   * `sender` — CommandSender
   * `worldName` — String
* **Returns:** boolean

## `public static boolean isInWorld(Player player, List<String> worlds)`

Checks a List of Strings (World Names) if Player is in one of it

* **Parameters:**
   * `player` — Player
   * `worlds` — List of Strings
* **Returns:** boolean

## `public static boolean isInWorld(CommandSender sender, World world)`

Checks if CommandSender is in World

* **Parameters:**
   * `sender` — CommandSender
   * `world` — World
* **Returns:** boolean

## `public static boolean isInWorld(Block block, World world)`

Checks if Block is in World

* **Parameters:**
   * `block` — Block
   * `world` — World
* **Returns:** boolean

## `public static boolean isInWorld(Entity entity, World world)`

Checks if Entity is in World

* **Parameters:**
   * `entity` — Entity
   * `world` — World
* **Returns:** boolean

## `public static void createWorld(String worldName, WorldType type, World.Environment worldEnvironment, boolean structures)`

Creates a new World for the Bukkit Server

* **Parameters:**
   * `worldName` — String
   * `type` — WorldType
   * `worldEnvironment` — World.Environment
   * `structures` — boolean

## `public static void createWorld(String worldName, WorldType type, World.Environment worldEnvironment, boolean structures, long seed)`

Creates a new World with a specific Seed for the Bukkit Server

* **Parameters:**
   * `worldName` — String
   * `type` — WorldType
   * `worldEnvironment` — World.Environment
   * `structures` — boolean
   * `seed` — long

## `public static boolean worldExists(String worldName)`

Checks if a World exists on the Bukkit Server

* **Parameters:** `worldName` — String
* **Returns:** boolean

## `public static void loadWorld(String worldName)`

Loads a World on the Bukkit Server

* **Parameters:** `worldName` — String

## `public static void unloadWorld(String worldName, boolean save) throws WorldNotLoadedException`

Unloads a World from the Bukkit Server

* **Parameters:**
   * `worldName` — String
   * `save` — boolean
* **Exceptions:** `*` — de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException

  if World is not loaded

## `public static void cloneWorld(String clonedWorldName, String originalWorldName) throws WorldNotFoundException`

Clones a World from an existing World on the Bukkit Server

* **Parameters:**
   * `clonedWorldName` — String
   * `originalWorldName` — String
* **Exceptions:** `*` — de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException

  if World was not found

## `public static WorldGroupEntry getWorldGroupEntry(Player player)`

Returns the WorldGroupEntry for the World the Player is currently in

* **Parameters:** `player` — Player
* **Returns:** WorldGroupEntry or null

## `public static WorldEntry getWorldEntry(Player player)`

Returns the WorldEntry for the World the Player is currently in

* **Parameters:** `player` — Player
* **Returns:** WorldEntry or null

## `public static void loadWorldGroupInventory(Player player)`

Loads the WorldGroup Inventory for the Player based on the World the Player is currently in

* **Parameters:** `player` — Player

## `public static boolean saveWorldGroupInventory(Player player, boolean clear)`

Saves the WorldGroup Inventory for the Player based on the World the Player is currently in

* **Parameters:**
   * `player` — Player
   * `clear` — boolean — if true clears the Player Inventory and Experience after saving
* **Returns:** boolean — true if an existing entry was updated, false if a new entry was created

## `public static boolean saveWorldGroupInventory(Player player, World world, boolean clear)`

Saves the WorldGroup Inventory for the Player based on the given World

* **Parameters:**
   * `player` — Player
   * `world` — World
   * `clear` — boolean — if true clears the Player Inventory and Experience after saving
* **Returns:** boolean — true if an existing entry was updated, false if a new entry was created

## `public static boolean hasWorldGroupInventory(Player player, World world)`

Checks if the Player has a saved WorldGroup Inventory for the given World

* **Parameters:**
   * `player` — Player
   * `world` — World
* **Returns:** boolean