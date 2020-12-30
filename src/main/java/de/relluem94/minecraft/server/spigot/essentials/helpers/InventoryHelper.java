package main.java.de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author rellu
 */
public class InventoryHelper {
    
    /**
     * 
     * @param amount of items in the inventory
     * @return The Size needed for the amount of items.
     */
    public static int inventorySize(int amount){
        
        int[] sizes = new int[6];
        sizes[0] = 9;
        sizes[1] = 18;
        sizes[2] = 27;
        sizes[3] = 36;
        sizes[4] = 45;
        sizes[5] = 54;
        
        int actualSize = 0;
        for(int i = 5; sizes[i] >= amount; i --){
            actualSize = sizes[i];
        }
        
        return actualSize;
    }
    
    public static Inventory createInventory(int size, String name){
        return Bukkit.createInventory(null, size, name);
    }
    
}
