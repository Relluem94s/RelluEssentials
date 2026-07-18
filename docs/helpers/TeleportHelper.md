# Documentation

## `public class TeleportHelper`

* **Author:** rellu

## `public static void teleportWorld(Player p, String name)`

Teleports a Player to the Spawn of a World by Name

* **Parameters:**
   * `p` — Player
   * `name` — String

## `public static void teleportWorld(Player p, String name, boolean silent)`

Teleports a Player to the Spawn of a World by Name

* **Parameters:**
   * `p` — Player
   * `name` — String
   * `silent` — boolean — if true suppresses teleport messages

## `public static void teleportBed(Player p)`

Teleports a Player to their Respawn Location

* **Parameters:**
   * `p` — Player

## `public static void teleportBed(Player p, boolean silent)`

Teleports a Player to their Respawn Location

* **Parameters:**
   * `p` — Player
   * `silent` — boolean — if true suppresses teleport messages

## `public static void teleportHome(Player p, LocationEntry locationEntry)`

Teleports a Player to a saved Home Location

* **Parameters:**
   * `p` — Player
   * `locationEntry` — LocationEntry

## `public static void teleportBack(Player p, Location location)`

Teleports a Player back to a previously saved Location without saving a new Back Point

* **Parameters:**
   * `p` — Player
   * `location` — Location

## `public static void teleportWarp(Player p, Location location)`

Teleports a Player to a Warp Location and saves the current Location as Back Point

* **Parameters:**
   * `p` — Player
   * `location` — Location