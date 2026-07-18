# RecipeHelper

A utility class that simplifies the creation of both shaped and shapeless Minecraft crafting recipes.

---

## Constructors

### `public RecipeHelper(String name, Shaped shape, ItemStack result)`

Creates a helper for a **shaped** crafting recipe.

| Parameter | Type        | Description                                              |
|-----------|-------------|----------------------------------------------------------|
| `name`    | `String`    | Unique identifier used to create the `NamespacedKey`     |
| `shape`   | `Shaped`    | Defines the grid layout and ingredient mapping           |
| `result`  | `ItemStack` | The item produced when the recipe is crafted             |

---

### `public RecipeHelper(String name, Material[] ingredients, ItemStack result)`

Creates a helper for a **shapeless** crafting recipe.

| Parameter      | Type          | Description                                          |
|----------------|---------------|------------------------------------------------------|
| `name`         | `String`      | Unique identifier used to create the `NamespacedKey` |
| `ingredients`  | `Material[]`  | Array of materials required, order does not matter   |
| `result`       | `ItemStack`   | The item produced when the recipe is crafted         |

---

## Methods

### `public Recipe getRecipe()`

Builds and returns a `Recipe` based on the constructor used.

- If constructed with a `Shaped` argument, returns a `ShapedRecipe` with the defined grid layout and ingredient mapping.
- If constructed with a `Material[]` argument, returns a `ShapelessRecipe` with all provided ingredients added.

**Returns:** `Recipe` — either a `ShapedRecipe` or `ShapelessRecipe` instance
