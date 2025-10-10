package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.CustomInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;

import static de.relluem94.minecraft.server.spigot.essentials.CustomItems.CUSTOM_ITEMS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_DUMMY;

/**
 *
 * @author rellu
 */
public class InventoryHelper {
    protected InventoryHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    private static final String SLOT_NAME_ITEM_STACK = "itemStack";
    private static final String SLOT_NAME_ID = "id";

    /**
     *
     * @param amount of items in the Inventory
     * @return int The Size needed for the amount of items.
     */
    public static int inventorySize(int amount) {

        if(amount <= 9){
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
    @Deprecated @ApiStatus.Internal @SuppressWarnings("all")
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
     * @param inv Inventory to Open
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
     * @param is ItemStack Item to fill with
     */
    @Contract("_, _ -> param1")
    public static Inventory fillInventory(@NotNull Inventory inv, ItemStack is) {
        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, is);
        }
        return inv;
    }

    private static final List<Integer> INVENTORY_SKIPS = Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53);

    public static int getSkipsSize(){
        return INVENTORY_SKIPS.size();
    }

    public static int getNextSlot(int slot){
        if(INVENTORY_SKIPS.contains(slot)){
            for(int i = slot; i <= 54; i++){
                if(!INVENTORY_SKIPS.contains(i)){
                    return i;
                }
            }     
        }
        else{
            return slot;
        }

        return -1;
    }


    public static void createInventory(String json, @NotNull Player p){
        p.getInventory().clear();

        try {
            JSONObject invJson = new JSONObject(json);
            for (int i=p.getInventory().getSize(); i >= 0; i--) {
                JSONObject slot = invJson.getJSONObject(i+"");
                                
                if (slot.has(SLOT_NAME_ITEM_STACK)) {
                    int slotID = slot.getInt(SLOT_NAME_ID);
                    ItemStack stack = ItemHelper.itemFrom64(slot.getString(SLOT_NAME_ITEM_STACK));
                    
                    if(stack != null){
                        p.getInventory().setItem(slotID, stack);
                    }

                  
                }
            }
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
    }

    public static @NotNull JSONObject saveInventoryToJSON(@NotNull Player p){
        PlayerInventory inventory = p.getInventory();
        JSONObject inv = new JSONObject();

        for (int i=inventory.getSize(); i >= 0; i--) {
            ItemStack stack = inventory.getItem(i);
            JSONObject slot = new JSONObject();
            slot.put(SLOT_NAME_ID ,Integer.valueOf(i));
            slot.put(SLOT_NAME_ITEM_STACK, ItemHelper.itemTo64(stack));
            inv.put(i + "", slot);
        }
        return inv;
    }

    @ApiStatus.Internal
    public static @NotNull Inventory getCustomItemInventory(@NotNull CustomInventory ci) {
        return getCustomItemInventory(CUSTOM_ITEMS, ci.getTitleGUI(), ci.getSize(), ci.getType());
    }

    @SuppressWarnings("unused")
    public static @NotNull Inventory getCustomItemInventory(List<ItemHelper> customItems, @NotNull CustomInventory ci) {
        return getCustomItemInventory(customItems, ci.getTitleGUI(), ci.getSize(), ci.getType());
    }

    public static @NotNull Inventory getCustomItemInventory(@NotNull List<ItemHelper> customItems, String guiTitle, int guiSize, ItemHelper.Type itemType) {
        Inventory inv = Bukkit.createInventory(null, guiSize, guiTitle);
        for (ItemHelper itemHelper : customItems) {
            if(!itemType.equals(itemHelper.getItemType())){
                continue;
            }
            inv.addItem(itemHelper.getCustomItem());
        }

        return inv;
    }

    @SuppressWarnings("unused")
    public static boolean isCustomItemInMainHand(@NotNull Player player, @NotNull ItemHelper customItem) {
        return player.getInventory().getItemInMainHand().equals(customItem.getCustomItem());
    }

}