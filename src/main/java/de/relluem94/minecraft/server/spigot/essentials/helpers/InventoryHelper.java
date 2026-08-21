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
 *
 * @author rellu
 */
public class InventoryHelper {

  private static final String SLOT_NAME_ITEM_STACK = "itemStack";
  private static final String SLOT_NAME_ID = "id";
  private static final List<Integer> INVENTORY_SKIPS = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
      17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);

  protected InventoryHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   *
   * @param amount of items in the Inventory
   * @return int The Size needed for the amount of items.
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
   *
   * @param size real size of the Inventory
   * @param name of the Inventory
   * @return Inventory
   */
  public static @NotNull Inventory createInventory(int size, String name) {
    return Bukkit.createInventory(null, size, name);
  }

  /**
   *
   * @param sender Updates Inventory for CommandSender / Player
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
   *
   * @param sender Closes Inventory for CommandSender / Player
   */
  public static void closeInventory(CommandSender sender) {
    if (sender instanceof Player p) {
      p.closeInventory();
    }
  }

  /**
   *
   * @param sender Opens Inventory for CommandSender / Player
   * @param inv    Inventory to Open
   */
  public static void openInventory(CommandSender sender, Inventory inv) {
    if (TypeHelper.isPlayer(sender)) {
      Player p = (Player) sender;
      p.openInventory(inv);
    }
  }

  /**
   *
   * @param inv Inventory to fill
   * @param is  ItemStack Item to fill with
   */
  @Contract("_, _ -> param1")
  public static Inventory fillInventory(@NotNull Inventory inv, ItemStack is) {
    for (int i = 0; i < inv.getSize(); i++) {
      inv.setItem(i, is);
    }
    return inv;
  }

  public static int getSkipsSize() {
    return INVENTORY_SKIPS.size();
  }

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


  public static void createInventory(String json, @NotNull Player p) {
    loadInventoryFromJSON(p.getInventory(), new JSONObject(json));
  }

  public static void loadInventoryFromJSON(Inventory inventory, JSONObject inventoryJSON) {
    inventory.clear();

    try {
      for (int i = inventory.getSize() - 1; i >= 0; i--) {
        JSONObject slot = inventoryJSON.getJSONObject(i + "");

        if (slot.has(SLOT_NAME_ITEM_STACK)) {
          int slotID = slot.getInt(SLOT_NAME_ID);
          ItemStack stack = ItemHelper.itemFrom64(slot.getString(SLOT_NAME_ITEM_STACK));

          if (stack != null) {
            inventory.setItem(slotID, stack);
          }
        }
      }
    } catch (IOException e) {
      Bukkit.getConsoleSender().sendMessage(e.getMessage());
    }
  }

  public static @NotNull JSONObject saveInventoryToJSON(@NotNull Player p) {
    return saveInventoryToJSON(p.getInventory());
  }

  public static @NotNull JSONObject saveInventoryToJSON(@NotNull Inventory inventory) {
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

  public static @NotNull Inventory getCustomItemInventory(@NotNull CustomInventory ci) {
    return getCustomItemInventory(ci, null);
  }

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