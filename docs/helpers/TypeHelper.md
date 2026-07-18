# Documentation

## `public class StringHelper`

* **Author:** rellu

---

## `public static String replaceColor(String message)`

* **Parameters:** `message` — String
* **Returns:** String with `&` replaced by `§` to trigger ChatColor codes

---

## `public static String locationToString(Location l)`

* **Parameters:** `l` — Location
* **Returns:** String representation of the Location

---

## `public static String locationToString(Location l, boolean round)`

* **Parameters:**
    * `l` — Location
    * `round` — boolean, whether to round the coordinates
* **Returns:** String representation of the Location

---

## `public static String firstCharToUpper(String s)`

* **Parameters:** `s` — String
* **Returns:** String with the first character converted to uppercase

---

## `public static String formatLong(long l)`

* **Parameters:** `l` — long
* **Returns:** Formatted String representation:
    * `>= 1,000,000,000` → `#.#B`
    * `>= 1,000,000` → `#.#M`
    * `>= 1,000` → `#.#K`
    * `< 1,000` → `#`

---

## `public static String formatInt(int i)`

* **Parameters:** `i` — int
* **Returns:** Formatted String representation:
    * `>= 1,000,000,000` → `#B`
    * `>= 1,000,000` → `#M`
    * `>= 1,000` → `#K`
    * `< 1,000` → `#`

---

## `public static String formatDouble(double d)`

* **Parameters:** `d` — double
* **Returns:** Formatted String representation with two decimal places:
    * `>= 1,000,000,000` or `<= -1,000,000,000` → `#.##B`
    * `>= 1,000,000` or `<= -1,000,000` → `#.##M`
    * `>= 1,000` or `<= -1,000` → `#.##K`
    * `between -1,000 and 1,000` → `#.##`