# Documentation

## `public class SignHelper`

* **Author:** rellu

### Description
A helper class for creating and validating plugin signs in a Minecraft Spigot server.
It encapsulates sign line content based on a given `ActionType` and optional custom input.

---

### Constructors

#### `SignHelper(ActionType signActionType, String customInput)`
Creates a `SignHelper` with a given action type and a custom input string for line 2.

| Parameter       | Type         | Description                              |
|----------------|--------------|------------------------------------------|
| `signActionType` | `ActionType` | The action type that defines the sign behavior |
| `customInput`   | `String`     | Custom value placed on line 2 of the sign |

---

#### `SignHelper(ActionType signActionType)`
Creates a `SignHelper` without custom input. Throws an exception if the given action type requires custom input.

| Parameter       | Type         | Description                              |
|----------------|--------------|------------------------------------------|
| `signActionType` | `ActionType` | The action type that defines the sign behavior |

**Throws:** `SignMissingCustomInputException` — if the `ActionType` requires custom input but none was provided.

---

### Methods

#### `String getLine0()`
Returns the fixed plugin sign identifier placed on line 0.

**Returns:** The constant `PLUGIN_SIGN_NAME`.

---

#### `String getLine1()`
Returns the display name of the configured `ActionType`.

**Returns:** The display name string of the action type.

---

#### `String getLine2()`
Returns the custom input value or an empty string if none was provided.

**Returns:** The custom input string or `""`.

---

#### `String getLine3()`
Returns the fixed click prompt placed on line 3 of the sign.

**Returns:** The constant `PLUGIN_SIGN_CLICK`.

---

#### `static boolean isSign(SignHelper sh, String line0, String line1, String line3)`
Validates whether a sign matches the expected plugin format by comparing lines 0, 1, and 3.

| Parameter | Type         | Description                        |
|----------|--------------|------------------------------------|
| `sh`     | `SignHelper` | The reference sign helper instance |
| `line0`  | `String`     | The text on line 0 of the sign     |
| `line1`  | `String`     | The text on line 1 of the sign     |
| `line3`  | `String`     | The text on line 3 of the sign     |

**Returns:** `true` if all three lines match the expected values, otherwise `false`.

---

#### `static boolean isSign(SignHelper sh, String line1)`
Validates whether a sign matches by comparing line 1 against the action type's shorthand or name.

| Parameter | Type         | Description                                      |
|----------|--------------|--------------------------------------------------|
| `sh`     | `SignHelper` | The reference sign helper instance               |
| `line1`  | `String`     | The text on line 1 to match against the action type |

**Returns:** `true` if `line1` matches either the shorthand (e.g. `[1]`) or the bracketed name (e.g. `[COMMAND]`), otherwise `false`.

---

#### `static boolean isBlockASign(Block b)`
Checks whether a given block is any supported sign type.

| Parameter | Type    | Description              |
|----------|---------|--------------------------|
| `b`      | `Block` | The block to be checked  |

**Returns:** `true` if the block is a `WallSign`, `Sign`, `WallHangingSign`, or `HangingSign`, otherwise `false`.

---

### Enum `ActionType`

Defines the supported sign action types with their associated metadata.

| Constant  | ID | Display Name | Requires Custom Input |
|-----------|----|--------------|-----------------------|
| `COMMAND` | 1  | Command      | ✅ Yes                |
| `TELEPORT`| 2  | Teleport     | ✅ Yes                |
| `SPAWN`   | 3  | Spawn        | ❌ No                 |
| `UP`      | 4  | Up           | ❌ No                 |
| `DOWN`    | 5  | Down         | ❌ No                 |
| `HOME`    | 6  | Home         | ✅ Yes                |

#### Methods

##### `String getShorthand()`
Returns the numeric shorthand identifier, e.g. `[1]`.

##### `String getName()`
Returns the bracketed enum name, e.g. `[COMMAND]`.

##### `int getId()`
Returns the numeric ID of the action type.

##### `String getDisplayName()`
Returns the human-readable display name.

##### `boolean isCustomInput()`
Returns whether this action type requires a custom input value on line 2.