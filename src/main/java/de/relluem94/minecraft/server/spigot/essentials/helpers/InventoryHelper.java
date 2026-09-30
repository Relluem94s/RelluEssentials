package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Utility class providing helper methods for managing Bukkit inventories,
 * including creation, serialization, deserialization, and manipulation of
 * inventories and their contents.
 *
 * @author rellu
 */
public class InventoryHelper {

  private static final String SLOT_NAME_ITEM_STACK = "itemStack";
  private static final String SLOT_NAME_ID = "id";
  private static final List<Integer> INVENTORY_SKIPS = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
      17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);

  /**
   * Private constructor to prevent instantiation of this utility class.
   *
   * @throws IllegalStateException always, since this class must not be instantiated
   */
  protected InventoryHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Calculates the smallest valid Bukkit inventory size that can hold the given number of items.
   *
   * @param amount the number of items to be stored in the inventory
   * @return the smallest valid inventory size capable of holding the given amount of items
   */
  public static int inventorySize(int amount) {

    if (amount <= 9) {
      return 9;
    }

    int[] sizes = new int[6];
    sizes[0] = 9;
    sizes[1] = 18;
    sizes[2] = 27;
    sizes[3] = 36;
    sizes[4] = 45;
    sizes[5] = 54;

    int actualSize = 0;
    for (int i = 5; sizes[i] >= amount; i--) {
      actualSize = sizes[i];
    }

    return actualSize;
  }

  /**
   * Creates a new Bukkit inventory with the specified size and display name.
   *
   * @param size the number of slots the inventory should have
   * @param name the display name of the inventory
   * @return a new {@link Inventory} with the given size and name
   */
  public static @NotNull Inventory createInventory(int size, String name) {
    return Bukkit.createInventory(null, size, name);
  }

  /**
   * Loads an inventory from a JSON string and applies it to the given player's inventory.
   *
   * @param json the JSON string representing the inventory contents
   * @param p    the player whose inventory should be populated
   */
  public static void createInventory(String json, @NotNull Player p) {
    loadInventoryFromJson(p.getInventory(), new JSONObject(json));
  }

  /**
   * Forces a client-side inventory update for the given {@link CommandSender} if they are a {@link Player}.
   *
   * @param sender the command sender whose inventory should be updated
   * @deprecated use Bukkit's built-in mechanisms instead
   */
  @Deprecated
  @ApiStatus.Internal
  @SuppressWarnings("all")
  public static void updateInventory(CommandSender sender) {
    if (sender instanceof Player p) {
      p.updateInventory();
    }
  }

  /**
   * Closes the currently open inventory for the given {@link CommandSender} if they are a {@link Player}.
   *
   * @param sender the command sender whose inventory should be closed
   */
  public static void closeInventory(CommandSender sender) {
    if (sender instanceof Player p) {
      p.closeInventory();
    }
  }

  /**
   * Opens the specified inventory for the given {@link CommandSender} if they are a {@link Player}.
   *
   * @param sender the command sender for whom the inventory should be opened
   * @param inv    the inventory to open
   */
  public static void openInventory(CommandSender sender, Inventory inv) {
    if (TypeHelper.isPlayer(sender)) {
      Player p = (Player) sender;
      p.openInventory(inv);
    }
  }

  /**
   * Fills every slot of the given inventory with the specified {@link ItemStack}.
   *
   * @param inv the inventory to fill
   * @param is  the item stack to place in every slot
   * @return the same inventory instance after being filled
   */
  @Contract("_, _ -> param1")
  public static Inventory fillInventory(@NotNull Inventory inv, ItemStack is) {
    for (int i = 0; i < inv.getSize(); i++) {
      inv.setItem(i, is);
    }
    return inv;
  }

  /**
   * Returns the number of slots that are skipped when navigating a bordered inventory layout.
   *
   * @return the count of skipped inventory slots
   */
  public static int getSkipsSize() {
    return INVENTORY_SKIPS.size();
  }

  /**
   * Returns the next available non-skipped slot starting from the given slot index.
   *
   * @param slot the slot index to start searching from
   * @return the next valid slot index, or {@code -1} if no valid slot exists beyond the given index
   */
  public static int getNextSlot(int slot) {
    if (INVENTORY_SKIPS.contains(slot)) {
      for (int i = slot; i <= 54; i++) {
        if (!INVENTORY_SKIPS.contains(i)) {
          return i;
        }
      }
    } else {
      return slot;
    }

    return -1;
  }

  /**
   * Clears the given inventory and populates it with items deserialized
   * from the provided {@link JSONObject}.
   *
   * @param inventory     the inventory to populate
   * @param inventoryJson the JSON object containing serialized inventory slot data
   */
  public static void loadInventoryFromJson(Inventory inventory, JSONObject inventoryJson) {
    inventory.clear();

    try {
      for (int i = inventory.getSize() - 1; i >= 0; i--) {
        JSONObject slot = inventoryJson.getJSONObject(i + "");

        if (slot.has(SLOT_NAME_ITEM_STACK)) {
          int slotId = slot.getInt(SLOT_NAME_ID);
          ItemStack stack = ItemHelper.itemFrom64(slot.getString(SLOT_NAME_ITEM_STACK));

          if (stack != null) {
            inventory.setItem(slotId, stack);
          }
        }
      }
    } catch (IOException e) {
      Bukkit.getConsoleSender().sendMessage(e.getMessage());
    }
  }

  /**
   * Serializes the inventory of the given player into a {@link JSONObject}.
   *
   * @param p the player whose inventory should be serialized
   * @return a {@link JSONObject} representing the player's inventory contents
   */
  public static @NotNull JSONObject saveInventoryToJson(@NotNull Player p) {
    return saveInventoryToJson(p.getInventory());
  }

  /**
   * Serializes the contents of the given inventory into a {@link JSONObject}.
   *
   * @param inventory the inventory to serialize
   * @return a {@link JSONObject} representing the inventory's contents
   */
  public static @NotNull JSONObject saveInventoryToJson(@NotNull Inventory inventory) {
    JSONObject inv = new JSONObject();

    for (int i = inventory.getSize() - 1; i >= 0; i--) {
      ItemStack stack = inventory.getItem(i);
      JSONObject slot = new JSONObject();
      slot.put(SLOT_NAME_ID, Integer.valueOf(i));
      slot.put(SLOT_NAME_ITEM_STACK, ItemHelper.itemTo64(stack));
      inv.put(i + "", slot);
    }
    return inv;
  }

  /**
   * Creates and returns a Bukkit inventory populated with all items from the
   * given {@link CustomInventory}.
   *
   * @param ci the custom inventory definition containing size, title, and items
   * @return a populated {@link Inventory} containing all custom items
   */
  public static @NotNull Inventory getCustomItemInventory(@NotNull CustomInventory ci) {
    return getCustomItemInventory(ci, null);
  }

  /**
   * Creates and returns a Bukkit inventory populated with items from the
   * given {@link CustomInventory},
   * optionally filtered by the specified {@link CustomItem.Type}.
   *
   * @param ci       the custom inventory definition containing size, title, and items
   * @param itemType the item type to filter by, or {@code null} to include all items
   * @return a populated {@link Inventory} containing the matching custom items
   */
  public static @NotNull Inventory getCustomItemInventory(@NotNull CustomInventory ci,
      CustomItem.Type itemType) {
    Inventory inv = Bukkit.createInventory(null, ci.getSize(), ci.getTitleGui());
    for (CustomItem itemHelper : ci.getCustomItems()) {
      if (itemType == null || itemType.equals(itemHelper.type())) {
        inv.addItem(itemHelper.toItemStack());
      }
    }
    return inv;
  }
}