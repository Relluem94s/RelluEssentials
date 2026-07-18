# Documentation

## `public static boolean isLockAble(Block b)`

Use this Method to check if Block is protectable

* **Parameters:** `b` — Block
* **Returns:** `true` if the Block is in the protection material list, `false` otherwise

## `public static boolean isOpenAble(Block b)`

Use this Method to check if a Block is an instance of Openable

* **Parameters:** `b` — Block
* **Returns:** `true` if the Block's data is an instance of Openable, `false` otherwise

## `public static boolean hasPermission(Block b, Player p)`

Use this Method to check if Player has Permission for that Block

* **Parameters:**
   * `b` — Block
   * `p` — Player
* **Returns:** `true` if the Player is not the owner of the ProtectionEntry at the Block's Location, `false` otherwise

## `public static boolean hasPermission(ProtectionEntry pre, Player p)`

Use this Method to check if Player has Permission for that ProtectionEntry

* **Parameters:**
   * `pre` — ProtectionEntry
   * `p` — Player
* **Returns:** `true` if the Player is the owner of the ProtectionEntry, `false` otherwise

## `public static boolean hasFlag(ProtectionEntry protection, ProtectionFlags flag)`

Use this with a ProtectionEntry to check if a Protection has a specific Flag

* **Parameters:**
   * `protection` — ProtectionEntry
   * `flag` — ProtectionFlags
* **Returns:** `true` if the ProtectionEntry contains the specified Flag, `false` otherwise

## `public static boolean hasRights(ProtectionEntry protection, int playerId)`

Use this Method to check if Player has Rights to the ProtectionEntry

* **Parameters:**
   * `protection` — ProtectionEntry
   * `playerId` — the database ID of the Player
* **Returns:** `true` if the Player is not listed in the rights of the ProtectionEntry or no rights are defined, `false` otherwise

## `public static Location getLocationFromBlockAlternateForDoor(Block b)`

Use this Method to get the Location of a Block, adjusted to the bottom half if the Block is the top half of a Door

* **Parameters:** `b` — Block
* **Returns:** the adjusted Location of the Block, or `null` if the Block is `null`

## `public static Block getOtherPart(Door door, Block block)`

Use this Method to get the other part of a double Door based on the facing direction and hinge side

* **Parameters:**
   * `door` — Door
   * `block` — Block representing one part of the double Door
* **Returns:** the adjacent Door Block, or `null` if no Openable Block is found at the calculated Location