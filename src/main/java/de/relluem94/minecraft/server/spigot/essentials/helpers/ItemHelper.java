package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.ItemType;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author rellu
 */
public class ItemHelper{
       
    private final ItemStack is;
    private final Material material;
    private final int amount;
    private final String displayName;
    private List<String> lore;
    private ItemType it;
    
    public ItemHelper(Material material, int amount, String displayName, ItemType itemType){
        this.amount = amount;
        this.material = material;
        this.displayName = displayName;
        this.it = itemType;
        
        is = new ItemStack(this.material, this.amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(this.displayName);
        is.setItemMeta(im);
    }
    
    public ItemHelper(Material material, int amount, String displayName, ItemType itemType, List<String> lore){
        this.amount = amount;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.it = itemType;
        
        is = new ItemStack(this.material, this.amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(this.displayName);
        im.setLore(this.lore);
        is.setItemMeta(im);
    }
    
    public ItemStack getCustomItem(){
        return is;
    }

    public int getAmount() {
        return amount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemType getItemType() {
        return it;
    }
    
    
}
