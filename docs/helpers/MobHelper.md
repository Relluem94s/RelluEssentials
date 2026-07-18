# MobHelper

## `public class MobHelper`

**Author:** rellu

A helper class to configure and spawn living entities (`LivingEntity`) into a Minecraft world with customizable properties such as potion effects, equipment, visibility, health, and name.

---

## Constructor

### `MobHelper(Location location, EntityType entityType, String customName, boolean isCustomNameVisible)`

Creates a new `MobHelper` instance with the required base configuration.

| Parameter | Type | Description |
|---|---|---|
| `location` | `Location` | The world location where the entity will be spawned. |
| `entityType` | `EntityType` | The type of entity to spawn. Must not be `null`. |
| `customName` | `String` | A custom display name for the entity. Falls back to the entity type name if `null`. |
| `isCustomNameVisible` | `boolean` | Whether the custom name is visible above the entity. |

---

## Setters

### `setInvisible(boolean isInvisible)`
Sets whether the entity is invisible after spawning.

### `setCanPickupItems(boolean canPickupItems)`
Sets whether the entity can pick up items. Defaults to `true`.

### `setHealth(double health)`
Sets the health of the entity after spawning. Defaults to `0`, which means the entity's default max health is used.

---

## Methods

### `addPotionEffect(PotionEffect potionEffect)`
Adds a single potion effect that will be applied to the entity on spawn.

| Parameter | Type | Description |
|---|---|---|
| `potionEffect` | `PotionEffect` | The potion effect to add. |

---

### `addPotionEffect(Collection<PotionEffect> potionEffects)`
Adds multiple potion effects that will be applied to the entity on spawn.

| Parameter | Type | Description |
|---|---|---|
| `potionEffects` | `Collection<PotionEffect>` | The collection of potion effects to add. |

---

### `spawn()`
Spawns the entity into the world at the configured location and applies all configured properties.

If the world is `null`, the spawn is aborted silently.

**Spawn behavior:**
- Sets the custom name and its visibility.
- Applies health (uses entity default if health is `0` or exceeds the entity's current health).
- Applies all added potion effects.
- Sets invisibility and item pickup capability.

---

### `spawn(ItemStack mainHand, ItemStack offHand, ItemStack helmet, ItemStack chest, ItemStack leggings, ItemStack boots)`
Spawns the entity and additionally equips it with the provided armor and held items.

Calls `spawn()` internally and then sets the equipment if the entity has an equipment slot.

| Parameter | Type | Description |
|---|---|---|
| `mainHand` | `ItemStack` | Item held in the main hand. |
| `offHand` | `ItemStack` | Item held in the off hand. |
| `helmet` | `ItemStack` | Helmet armor piece. |
| `chest` | `ItemStack` | Chestplate armor piece. |
| `leggings` | `ItemStack` | Leggings armor piece. |
| `boots` | `ItemStack` | Boots armor piece. |

---

## Usage Example

```java
MobHelper mobHelper = new MobHelper(player.getLocation(), EntityType.ZOMBIE, "Boss Zombie", true);

mobHelper.setHealth(40.0);
mobHelper.setInvisible(false);
mobHelper.setCanPickupItems(false);
mobHelper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));

mobHelper.spawn(
    new ItemStack(Material.IRON_SWORD),
    null,
    new ItemStack(Material.IRON_HELMET),
    new ItemStack(Material.IRON_CHESTPLATE),
    new ItemStack(Material.IRON_LEGGINGS),
    new ItemStack(Material.IRON_BOOTS)
);