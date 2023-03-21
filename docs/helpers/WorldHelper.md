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

Checks if Command Sender is in World with Name

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

## `public static void createWorld(String worldName, WorldType type, World.Environment world_environment, boolean structures)`

Creates a new World for the Bukkit Server

 * **Parameters:**
   * `worldName` — String
   * `type` — WorldType
   * `world_environment` — World.Environment
   * `structures` — boolean

## `public static boolean worldExists(String worldName)`

Creates a World for the Bukkit Server

 * **Parameters:** `worldName` — String

## `public static void loadWorld(String worldName)`

Creates a World for the Bukkit Server

 * **Parameters:** `worldName` — String

## `public static void unloadWorld(String worldName, boolean save) throws WorldNotLoadedException`

Unloads a World from the Bukkit Server

 * **Parameters:**
   * `worldName` — String
   * `save` — boolean
 * **Exceptions:** `*` — de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException

     if World is not loaded

## `public static void cloneWorld(String worldName, String copyWorldName) throws WorldNotFoundException`

Copies a World from another from the Bukkit Server

 * **Parameters:**
   * `worldName` — String
   * `copyWorldName` — String
 * **Exceptions:** `*` — de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException

     if World was not found

