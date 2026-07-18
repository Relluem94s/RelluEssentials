# Documentation

## `public class PatchHelper`

* **Author:** rellu

## `public PatchHelper(DatabaseHelper databaseHelper)`

Creates a new PatchHelper instance with the given DatabaseHelper

* **Parameters:**
   * `databaseHelper` — DatabaseHelper

## `public void applyPatch(int version)`

Applies all necessary database patches starting from the given version number.
Executes all patches sequentially from the next required version up to the latest and finalizes the patching process.

* **Parameters:**
   * `version` — int — the current database version number; use `-1` or `0` to apply all patches from the beginning