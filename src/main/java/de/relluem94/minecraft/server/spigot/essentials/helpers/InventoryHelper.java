/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author rellu
 */
public class InventoryHelper {
    
    public static int inventorySize(int size){
        
        int[] sizes = new int[7];
        sizes[0] = 9;
        sizes[1] = 18;
        sizes[2] = 27;
        sizes[3] = 36;
        sizes[4] = 45;
        sizes[5] = 54;
        
        int actualSize = 0;
        for(int i = 5; sizes[i] >= size; i --){
            actualSize = sizes[i];
        }
        
        return actualSize;
    }
    
    public static Inventory createInventory(int size, String name){
        return Bukkit.createInventory(null, size, name);
    }
    
}
