# Documentation

## `public class ChatHelper`

- **Author:** rellu
- Utility class for sending messages to players, console, channels, and action bars.
- Cannot be instantiated (throws `IllegalStateException`).

---

## `public static void sendMessage(CommandSender sender, String message)`

Sends a message to a `CommandSender`. If the sender is a player, the message is sent via `Player#sendMessage`. Otherwise, it is forwarded to the console without a prefix.

- **Parameters:**
   - `sender` — The recipient of the message
   - `message` — The message to send

---

## `public static void consoleSendMessage(String type, String message)`

Sends a prefixed message to the console.

- **Parameters:**
   - `type` — Prefix to prepend before the message (e.g. `[INFO]`)
   - `message` — The message to send

---

## `public static void consoleSendMessage(String type, String message, int repeat)`

Sends a prefixed message to the console multiple times.

- **Parameters:**
   - `type` — Prefix to prepend before the message
   - `message` — The message to send
   - `repeat` — Number of times the message is sent (inclusive, i.e. `repeat + 1` total)

---

## `public static void sendMessageInChannel(String message, Player p, String channel, GroupEntry group)`

Sends a message in a permission-restricted channel. The channel prefix is stripped from the message before sending. Only online players authorized for the group receive the message.

- **Parameters:**
   - `message` — The raw message including the channel prefix
   - `p` — The sending player
   - `channel` — The channel prefix to strip from the message
   - `group` — The `GroupEntry` defining the required permission and display prefix

---

## `public static void sendMessageInChannel(String message, String sender, String channel, GroupEntry group)`

Overload of `sendMessageInChannel` for non-player senders (e.g. console).

- **Parameters:**
   - `message` — The raw message including the channel prefix
   - `sender` — Display name of the sender
   - `channel` — The channel prefix to strip from the message
   - `group` — The `GroupEntry` defining the required permission and display prefix

---

## `public static void sendMessageInActionBar(@NonNull Player p, String message)`

Sends a message to a player's action bar.

- **Parameters:**
   - `p` — The target player (must not be `null`)
   - `message` — The message to display in the action bar

---

## `public static void msg(CommandSender sender, Player target, String[] args, int start)`

Sends a private message from a `CommandSender` to a target player.

- Console senders receive an error (`COMMAND_NOT_A_PLAYER`).
- VIP players may use color codes and special symbols.
- Requires at least `user` group permission; otherwise an error is sent to the sender.

- **Parameters:**
   - `sender` — The sender of the private message (must be a `Player`)
   - `target` — The recipient of the private message
   - `args` — Command arguments array
   - `start` — Index in `args` from which the message text begins