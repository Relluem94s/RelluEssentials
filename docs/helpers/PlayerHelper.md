# Documentation

## `public class PlayerHelper`

* **Author:** rellu

---

## `public static void setFlying(Player p)`

Enables flight for a player if they have the VIP permission and their stored player entry has flying enabled.

* **Parameters:** `p` — Player to enable flying for

---

## `public static void setAFK(Player p, boolean join)`

Toggles or initializes the AFK state of a player. Broadcasts a server-wide message when the state changes outside of a join event. Handles `FAKE_AFK_ACTIVE` and `FAKE_AFK_ON` player states. Updates invulnerability and the player list name accordingly.

* **Parameters:**
   * `p` — Player whose AFK state is being updated
   * `join` — `true` if called during player join (suppresses broadcast and database update)

---

## `public static OfflinePlayerEntry getOfflinePlayerByName(String name)`

Fetches an offline player's UUID and name from the Mojang API by username.

* **Parameters:** `name` — Minecraft username to look up
* **Returns:** `OfflinePlayerEntry` with UUID and name, or `null` if not found

---

## `public static OfflinePlayerEntry getOfflinePlayerByUUID(UUID uuid)`

Fetches an offline player's name and skin properties from the Mojang session server by UUID.

* **Parameters:** `uuid` — UUID of the player to look up
* **Returns:** `OfflinePlayerEntry` with name and skin properties, or `null` if not found

---

## `public static void setGroup(Player p, GroupEntry g)`

Applies a group's prefix to the player's custom name and updates the player list name.

* **Parameters:**
   * `p` — Player to update
   * `g` — Group whose prefix is applied

---

## `public static void updateGroup(OfflinePlayer p, GroupEntry g)`

Updates a player's group in the database and, if the player is online, refreshes their custom name and player list name immediately.

* **Parameters:**
   * `p` — Offline or online player to update
   * `g` — New group to assign

---

## `public static String getCustomName(Player p)`

Returns the player's custom display name from their stored entry, falling back to their Minecraft username if no custom name is set.

* **Parameters:** `p` — Player to retrieve the name for
* **Returns:** Custom display name or Minecraft username

---

## `public static GroupEntry getGroup(Player p)`

Returns the group assigned to the player. Falls back to group with ID `1` if no player entry exists.

* **Parameters:** `p` — Player to retrieve the group for
* **Returns:** `GroupEntry` of the player's group

---

## `public static void savePlayers()`

Iterates all loaded player entries and persists any that are marked as requiring an update. Broadcasts the count of updated players to the admin channel.

---

## `public static void savePlayersInv()`

Saves the world group inventory for all currently online players. Broadcasts the count of updated inventories to the admin channel.

---

## `public static void savePlayer(Player p)`

Resolves the player entry for the given online player and delegates to `savePlayer(PlayerEntry)`.

* **Parameters:** `p` — Online player whose entry should be saved

---

## `public static int savePlayer(PlayerEntry pe)`

Persists a player entry to the database if it is marked as requiring an update.

* **Parameters:** `pe` — Player entry to save
* **Returns:** `1` if the entry was saved, `0` if no update was required

---

## `public static OfflinePlayer getOfflinePlayer(String name)`

Searches Bukkit's offline player cache for a player matching the given name.

* **Parameters:** `name` — Minecraft username to search for
* **Returns:** Matching `OfflinePlayer`, or `null` if not found

---

## `public static PlayerEntry getPlayer(String name)`

Searches the loaded player entry map for an entry matching the given name.

* **Parameters:** `name` — Minecraft username to search for
* **Returns:** Matching `PlayerEntry`, or `null` if not found

---

## `public static Player getTargetedPlayer(Location loc)`

Finds the online player closest to the given location within the same world.

* **Parameters:** `loc` — Reference location
* **Returns:** Nearest `Player`, or `null` if the world is invalid or no players are present

---

## `public static void setLobbyItems(Player p)`

Clears the player's armor, off-hand, and any existing lobby items from their inventory, then places the grappling hook, cloud sailor, and world selector into fixed hotbar slots.

* **Parameters:** `p` — Player whose inventory is reset to lobby layout

---

## `public static Location getLookingLocation(Player player, double range)`

Returns the location of the block the player is looking at within the given range, including fluid blocks. Falls back to the player's current location if no block is hit.

* **Parameters:**
   * `player` — Player performing the ray trace
   * `range` — Maximum ray trace distance in blocks
* **Returns:** Location of the targeted block, or the player's location if none

---

## `public static Vector getPlayerDirection(Player p)`

Returns the cardinal or vertical direction the player is currently facing as a normalized unit vector.

* **Parameters:** `p` — Player whose facing direction is evaluated
* **Returns:** Unit `Vector` representing the dominant direction

---

## `public static Vector getLocationDirection(Location l)`

Resolves the dominant facing direction from a location's yaw and pitch into one of six axis-aligned unit vectors.

* **Parameters:** `l` — Location whose direction is evaluated
* **Returns:** Unit `Vector` for north, south, east, west, up, or down