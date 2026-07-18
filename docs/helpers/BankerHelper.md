# Documentation

## `public class BankerHelper`

* **Author:** rellu

---

## Fields

### `public static final ItemHelper npc_portable_bank`

Represents the portable bank item (Yellow Shulker Box) with legendary rarity that players can carry.

---

### `public static final ItemHelper npc_gui_deposit`

Represents the deposit action item (Green Shulker Box) shown in the banker GUI.

---

### `public static final ItemHelper npc_gui_withdraw`

Represents the withdraw action item (Red Shulker Box) shown in the banker GUI.

---

### `public static final ItemHelper npc_gui_balance`

Represents the balance overview item (Yellow Shulker Box) shown in the banker GUI.

---

### `public static final ItemHelper npc_gui_upgrade`

Represents the account upgrade item (Diamond Block) shown in the banker GUI.

---

### `public static final ItemHelper npc_gui_deposit_all`

Represents the deposit-all action item (Gold Block) shown in the deposit GUI.

---

### `public static final ItemHelper npc_gui_deposit_5_percent`

Represents the deposit-5-percent action item (Gold Nugget) shown in the deposit GUI.

---

### `public static final ItemHelper npc_gui_deposit_20_percent`

Represents the deposit-20-percent action item (Gold Ingot) shown in the deposit GUI.

---

### `public static final ItemHelper npc_gui_deposit_50_percent`

Represents the deposit-50-percent action item (Gold Ingot) shown in the deposit GUI.

---

### `public static final ItemHelper npc_gui_withdraw_all`

Represents the withdraw-all action item (Gold Block) shown in the withdraw GUI.

---

### `public static final ItemHelper npc_gui_withdraw_5_percent`

Represents the withdraw-5-percent action item (Gold Nugget) shown in the withdraw GUI.

---

### `public static final ItemHelper npc_gui_withdraw_20_percent`

Represents the withdraw-20-percent action item (Gold Ingot) shown in the withdraw GUI.

---

### `public static final ItemHelper npc_gui_withdraw_50_percent`

Represents the withdraw-50-percent action item (Gold Ingot) shown in the withdraw GUI.

---

### `public static final ItemHelper npc_gui_balance_total`

Represents the total balance display item (Yellow Shulker Box) shown in the balance GUI.

---

### `public static final ItemHelper npc_gui_balance_transactions`

Represents the transaction history item (Map) shown in the balance GUI.

---

### `public static final Material UPGRADE_MATERIAL`

The material used to represent bank tier upgrade items in the upgrade GUI. Set to `AMETHYST_SHARD`.

---

## Methods

## `public static ItemStack addLoreLine(ItemStack is, String line)`

Adds or replaces the second lore line of an ItemStack.

* **Parameters:**
  * `is` — ItemStack — the item to modify
  * `line` — String — the lore line to add or replace
* **Returns:** ItemStack — the modified ItemStack, or the original if no ItemMeta is present

---

## `public static List<ItemHelper> getBankTiers()`

Builds and returns a list of `ItemHelper` objects representing all available bank tiers, including cost, interest rate, and deposit limit as lore lines. Each item also stores the tier cost as persistent data.

* **Returns:** List of ItemHelper — one entry per configured bank tier

---

## `public static void deposit(PlayerEntry pe, Player p, BankAccountEntry bae, float percentage)`

Deposits a percentage of the player's purse into their bank account. If the deposit would exceed the account limit, only the remaining capacity is deposited. Closes the inventory after the transaction.

* **Parameters:**
  * `pe` — PlayerEntry — the player's data entry
  * `p` — Player — the Bukkit player
  * `bae` — BankAccountEntry — the player's bank account
  * `percentage` — float — the percentage of the purse to deposit (use `100` for all)

---

## `public static void withdraw(PlayerEntry pe, Player p, BankAccountEntry bae, float percentage)`

Withdraws a percentage of the player's bank account into their purse. Closes the inventory after the transaction.

* **Parameters:**
  * `pe` — PlayerEntry — the player's data entry
  * `p` — Player — the Bukkit player
  * `bae` — BankAccountEntry — the player's bank account
  * `percentage` — float — the percentage of the bank balance to withdraw (use `100` for all)

---

## `public static void upgradeAccount(ItemStack itemStack, Player p, PlayerEntry pe, BankAccountEntry bae)`

Attempts to upgrade the player's bank account tier based on the selected upgrade item. Payment is deducted from the purse first, then the bank account, then a combination of both if neither alone is sufficient.

* **Parameters:**
  * `itemStack` — ItemStack — the upgrade item clicked by the player
  * `p` — Player — the Bukkit player
  * `pe` — PlayerEntry — the player's data entry
  * `bae` — BankAccountEntry — the player's current bank account

---

## `public static void doInterest()`

Processes interest for all currently online players. Calls `checkInterest` with midnight mode enabled for each player, then distributes the interest.

---

## `public static void doInterest(Player p)`

Pays out pending interest to a specific online player based on their bank account value and tier interest rate.

* **Parameters:**
  * `p` — Player — the Bukkit player to receive interest

---

## `public static void checkInterest(UUID uuid, boolean midnight)`

Checks whether a player is eligible for interest and queues them in the interest map if so. In midnight mode, eligibility is not checked and the player is always queued. Otherwise, the player is only queued if their last played time was before the start of the current day.

* **Parameters:**
  * `uuid` — UUID — the unique ID of the player to check
  * `midnight` — boolean — if `true`, skips the last-played check and always queues the player

---

## `public static BankTierEntry getBankTierEntryByCost(long costs)`

Looks up and returns the `BankTierEntry` whose cost matches the given value.

* **Parameters:**
  * `costs` — long — the cost value to search for
* **Returns:** BankTierEntry or null — the matching tier, or `null` if none is found