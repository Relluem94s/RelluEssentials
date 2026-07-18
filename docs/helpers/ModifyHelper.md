# Documentation

## `public class ModifyHelper`

* **Author:** rellu

## `public static float normalizeYaw(float yaw)`

Normalizes a yaw value to the nearest cardinal direction (0, 90, 180, 270)

* **Parameters:** `yaw` — float
* **Returns:** float — normalized yaw value between 0 and 359

## `public static int[] worldToLocal(int dx, int dz, float yaw)`

Converts world-relative coordinates to local coordinates based on the given yaw

* **Parameters:**
   * `dx` — int — world-relative X offset
   * `dz` — int — world-relative Z offset
   * `yaw` — float — player yaw used for rotation
* **Returns:** int[] — local [x, z] coordinate pair

## `public static int[] relativeToWorld(int relX, int relZ, float yaw)`

Converts local-relative coordinates to world coordinates based on the given yaw

* **Parameters:**
   * `relX` — int — local X offset
   * `relZ` — int — local Z offset
   * `yaw` — float — player yaw used for rotation
* **Returns:** int[] — world [x, z] coordinate pair

## `public static Block getBlock(ModifyClipboardEntry entry, float yaw, Location playerTargetLoc)`

Resolves the world Block for a clipboard entry relative to a target location and yaw

* **Parameters:**
   * `entry` — ModifyClipboardEntry — clipboard entry containing relative location and block data
   * `yaw` — float — player yaw used for rotation
   * `playerTargetLoc` — Location — target location in the world
* **Returns:** Block — the resolved world block

## `public static Selection getRelativeCopySelection(Selection selection, Location playerTargetLoc)`

Returns a new Selection with positions relative to the given target location

* **Parameters:**
   * `selection` — Selection — the original world selection
   * `playerTargetLoc` — Location — the reference location to subtract from
* **Returns:** Selection — selection with relative positions

## `public static boolean isPlantMaterial(Material material)`

Checks if a Material is a plant-type block

* **Parameters:** `material` — Material
* **Returns:** boolean — true if the material is a plant

## `public static ModifyClipboardEntry getModifyClipboardEntry(Block block, Player p, Location playerTargetLoc)`

Creates a ModifyClipboardEntry for a block relative to the player target location, rotated by the player yaw

* **Parameters:**
   * `block` — Block — the block to capture
   * `p` — Player — the player whose yaw is used for rotation
   * `playerTargetLoc` — Location — the reference location for relative positioning
* **Returns:** ModifyClipboardEntry — clipboard entry with local coordinates and block data

## `public static DoubleStore<Selection, List<ModifyClipboardEntry>> rotate(List<ModifyClipboardEntry> entries, Selection selection)`

Rotates a list of clipboard entries 90 degrees clockwise and returns the updated entries with a recalculated Selection

* **Parameters:**
   * `entries` — List of ModifyClipboardEntry — the clipboard entries to rotate
   * `selection` — Selection — the current selection used to preserve Y bounds
* **Returns:** DoubleStore — containing the rotated Selection and the normalized list of ModifyClipboardEntry

## `public static void undo(ModifyHistoryEntry entry)`

Restores a block to its previous state using a history entry

* **Parameters:** `entry` — ModifyHistoryEntry — the history entry containing the original block state

## `public static void addUndoHistory(Player p, List<ModifyHistoryEntry> history)`

Appends a list of history entries to the undo stack of the given player

* **Parameters:**
   * `p` — Player
   * `history` — List of ModifyHistoryEntry — the block changes to register as undoable

## `public static void checkAndRemoveProtection(Block block)`

Removes the protection entry for a block if it is lockable and currently protected

* **Parameters:** `block` — Block — the block to check and unprotect

## `public static void forEachBlock(Selection selection, Consumer<Block> action)`

Iterates over every block within the given selection and applies the provided action

* **Parameters:**
   * `selection` — Selection — the region to iterate over
   * `action` — Consumer of Block — the action to apply to each block