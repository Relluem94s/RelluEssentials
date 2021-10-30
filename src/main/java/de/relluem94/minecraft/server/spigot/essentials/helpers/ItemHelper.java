package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.ItemRarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.ItemType;
import de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces.IItemHelper;
import java.util.ArrayList;
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
    private final ItemRarity ir;

    private List<String> lore;

    /**
     *
     * @param material Bukkit Material
     * @param amount Integer
     * @param displayName String
     * @param itemType ItemType
     * @param itemRarity ItemRarity
     */
    public ItemHelper(Material material, int amount, String displayName, ItemType itemType, ItemRarity itemRarity) {
        this.amount = amount;
        this.material = material;
        this.displayName = displayName;
        this.it = itemType;
        this.ir = itemRarity;

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
     * @param itemRarity ItemRarity
     * @param lore List String
     */
    public ItemHelper(Material material, int amount, String displayName, ItemType itemType, ItemRarity itemRarity, List<String> lore) {
        this.amount = amount;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.it = itemType;
        this.ir = itemRarity;

        is = new ItemStack(this.material, this.amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(this.displayName);
        im.setLore(this.lore);
        is.setItemMeta(im);
    }

    /**
     *
     * @return ItemStack of ItemHelper
     */
    public ItemStack getCustomItem() {
        init();
        addItemRarity();
        postInit();
        return is;
    }
    
    /**
     * 
     * @return ItemStack of ItemHelper
     */
    protected ItemStack getItemStack(){
        return is;
    }

    /**
     *
     * @return int Amount of ItemHelper
     */
    public int getAmount() {
        return amount;
    }

    /**
     *
     * @return String DisplayName of ItemHelper
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @return List of String Lore of ItemHelper
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     *
     * @return Material of ItemHelper
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

    /**
     *
     * @return ItemMeta of ItemStack
     */
    public ItemMeta getItemMeta() {
        return is.getItemMeta();
    }

    /**
     *
     * @param itemmeta ItemMeta sets ItemMeta of ItemStack
     */
    public void setItemMeta(ItemMeta itemmeta) {
        is.setItemMeta(itemmeta);
    }

    /**
     * 
     * @param compare
     * @return boolean
     */
    public boolean equals(ItemStack compare) {
        ItemStack item = this.getCustomItem();
        if (item == null || compare == null) {
            return false;
        }

        if (item.getType() != compare.getType()) {
            return false;
        }

        if (item.hasItemMeta() != compare.hasItemMeta()) {
            return false;
        }

        if (item.hasItemMeta() && !item.getItemMeta().equals(compare.getItemMeta())) {
            return false;
        }

        return true;
    }
    
    /**
     * 
     * @param compare
     * @return boolean
     */
    public boolean almostEquals(ItemStack compare) {
        ItemStack item = this.getCustomItem();
        if (item == null || compare == null) {
            return false;
        }

        if (item.getType() != compare.getType()) {
            return false;
        }

        if (item.hasItemMeta() != compare.hasItemMeta()) {
            return false;
        }

        return true;
    }

    @Override
    public void init() {

    }
    
    @Override
    public void postInit() {
        
    }

    private void addItemRarity() {
        ItemMeta im;
        if (is.hasItemMeta()) {
            im = is.getItemMeta();
            List<String> loc_lore;
            if (im.getLore() != null) {
                loc_lore = im.getLore();
            } else {
                loc_lore = new ArrayList();
            }
            
            loc_lore.remove(ItemRarity.COMMON.getPrefix() + ItemRarity.COMMON.getDisplayName());
            loc_lore.remove(ItemRarity.UNCOMMON.getPrefix() + ItemRarity.UNCOMMON.getDisplayName());
            loc_lore.remove(ItemRarity.RARE.getPrefix() + ItemRarity.RARE.getDisplayName());
            loc_lore.remove(ItemRarity.EPIC.getPrefix() + ItemRarity.EPIC.getDisplayName());
            loc_lore.remove(ItemRarity.LEGENDARY.getPrefix() + ItemRarity.LEGENDARY.getDisplayName());

            loc_lore.add(ir.getPrefix() + "" + ir.getDisplayName());

            im.setLore(loc_lore);
            is.setItemMeta(im);
        }
    }
}