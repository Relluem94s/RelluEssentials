# Documentation

## `public class BlockHelper`

A utility class for scheduling and applying block changes at specified locations with optional delays.

- **Author:** Relluem94

### Fields

| Field | Type | Description |
|---|---|---|
| `type` | `Material` | The material to set when applying block changes. Can be updated via `setType(Material)`. |
| `locations` | `HashMap<Location, Long>` | Internal map of locations and their associated delays. |

---

## Constructor

## `public BlockHelper(Material type)`

Creates a new `BlockHelper` instance with the given material type.

- **Parameters:** `type` — The `Material` to use when setting blocks.

---

## Methods

## `public void addLocation(Location location, Long delay)`

Registers a location with an associated delay for a scheduled block change.

- **Parameters:**
    - `location` — The `Location` where the block should be set.
    - `delay` — The delay in ticks before the block is set.

---

## `public void putAll(@NotNull BlockHelper setBlockHelper)`

Merges all locations and delays from another `BlockHelper` instance into this one.

- **Parameters:** `setBlockHelper` — The `BlockHelper` whose entries are added to this instance.

---

## `public void setBlocks(long addDelay)`

Schedules all registered block changes using the Bukkit scheduler. Each block is set to the configured `Material` after its individual delay plus the given `addDelay`.

- **Parameters:** `addDelay` — An additional delay in ticks added to each location's delay. The absolute value of the combined delay is used.

---

## `public void setBlocks()`

Schedules all registered block changes with no additional delay. Equivalent to calling `setBlocks(0)`.

---

## `public static boolean checkBlockAt(@NotNull Location location, Material mat)`

Checks whether the block at the given location matches the specified material.

- **Parameters:**
    - `location` — The `Location` to check.
    - `mat` — The `Material` to compare against.
- **Returns:** `true` if the block at the location is of the given material, `false` otherwise.