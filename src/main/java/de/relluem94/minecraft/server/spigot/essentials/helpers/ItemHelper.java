package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.ItemType;
import de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces.IItemHelper;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author rellu
 */
public class ItemHelper implements IItemHelper {

    private final ItemStack is;
    private final Material material;
    private final int amount;
    private final String displayName;
    private final ItemType it;

    private List<String> lore;

    /**
     *
     * @param material Bukkit Material
     * @param amount Integer
     * @param displayName String
     * @param itemType ItemType
     */
    public ItemHelper(Material material, int amount, String displayName, ItemType itemType) {
        this.amount = amount;
        this.material = material;
        this.displayName = displayName;
        this.it = itemType;

        is = new ItemStack(this.material, this.amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(this.displayName);
        is.setItemMeta(im);
    }

    /**
     *
     * @param material Bukkit Material
     * @param amount Integer
     * @param displayName String
     * @param itemType ItemType
     * @param lore List String
     */
    public ItemHelper(Material material, int amount, String displayName, ItemType itemType, List<String> lore) {
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

    /**
     *
     * @return Bukkit ItemStack
     */
    public ItemStack getCustomItem() {
        init();
        return is;
    }

    /**
     *
     * @return Amount of ItemHelper
     */
    public int getAmount() {
        return amount;
    }

    /**
     *
     * @return DisplayName of ItemHelper
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @return Lore (String List) of ItemHelper
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     *
     * @return Bukkit Material of ItemHelper
     */
    public Material getMaterial() {
        return material;
    }

    /**
     *
     * @return ItemType of ItemHelper
     */
    public ItemType getItemType() {
        return it;
    }
    
    public ItemMeta getItemMeta(){
        return is.getItemMeta();
    }
    
    public void setItemMeta(ItemMeta itemmeta){
        is.setItemMeta(itemmeta);
    }
    
    public boolean equals(ItemStack compare){
    if(is == null || compare == null){
        return false;
    }

    if(is.getType() != compare.getType()){
        return false;
    }
        
    if(is.hasItemMeta() != compare.hasItemMeta()){
        return false;
    }
 
    if(is.hasItemMeta() && !is.getItemMeta().equals(compare.getItemMeta())){
        return false;
    }
    
    return true;
}

    @Override
    public void init() {}
}
