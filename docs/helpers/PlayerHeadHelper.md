# Documentation

## `public class PlayerHeadHelper`

* **Author:** rellu

## `public static void createSkull(String name, Plugin plugin, Consumer<ItemStack> callback)`

Creates a Player Skull ItemStack for the given Player Name asynchronously and returns it via callback

* **Parameters:**
   * `name` — String — the name of the Player
   * `plugin` — Plugin — the Plugin instance used for scheduling the callback on the main thread
   * `callback` — Consumer<ItemStack> — called with the resulting ItemStack once the skull is ready
* **Notes:** If no Player is found for the given name, the callback is called with a plain Player Head

## `public static ItemStack getCustomSkull(CustomHeads ch)`

Creates a custom Player Skull ItemStack based on a Base64 encoded texture from a CustomHeads entry

* **Parameters:**
   * `ch` — CustomHeads — the CustomHeads entry containing UUID, name and Base64 texture
* **Returns:** ItemStack — the custom skull ItemStack, or a plain Player Head if the texture could not be applied