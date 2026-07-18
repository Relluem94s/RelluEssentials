# Documentation

## `public class NPCHelper`

* **Author:** rellu

## `public static final int INV_SIZE`

The default inventory size of 54 slots.

## `public NPCHelper(Location location, NPC npc)`

Creates an NPCHelper based on an NPC configuration object. The entity type is always VILLAGER. If the NPC has no name, the entity type name is used as fallback.

* **Parameters:**
   * `location` — Location
   * `npc` — NPC

## `public NPCHelper(Location location, String customName, Profession profession, boolean isCustomNameVisible)`

Creates an NPCHelper with explicit configuration. The entity type is always VILLAGER. If customName is null, the entity type name is used as fallback.

* **Parameters:**
   * `location` — Location
   * `customName` — String
   * `profession` — Profession
   * `isCustomNameVisible` — boolean

## `public String getCustomName()`

Returns the custom name of the NPC.

* **Returns:** String

## `public void setHealth(double health)`

Sets the health value for the NPC to be applied on spawn.

* **Parameters:** `health` — double

## `public void setInvisible(boolean isInvisible)`

Sets whether the NPC is invisible.

* **Parameters:** `isInvisible` — boolean

## `public void setCollideAble(boolean isCollideAble)`

Sets whether the NPC is collidable.

* **Parameters:** `isCollideAble` — boolean

## `public void spawn()`

Spawns the NPC at the configured location with all configured properties applied. The spawned entity has no AI, cannot pick up items and is persistent. If health is 0 or exceeds the entity max health, the entity default health is used.