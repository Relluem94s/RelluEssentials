# Documentation

## `public class ExperienceHelper`

* **Author:** rellu

## `public static int getTotalExperience(int level)`

Calculates the total experience points required to reach the given level

* **Parameters:**
   * `level` — int
* **Returns:** int — total experience points for the given level, or 0 if level is negative

## `public static int getTotalExperience(Player player)`

Calculates the total experience points the Player currently has

* **Parameters:**
   * `player` — Player
* **Returns:** int — total experience points of the Player

## `public static void setTotalExperience(Player player, int amount)`

Sets the total experience points of the Player to the given amount

* **Parameters:**
   * `player` — Player
   * `amount` — int — total experience points to set