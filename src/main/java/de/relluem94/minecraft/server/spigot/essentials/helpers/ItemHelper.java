package de.relluem94.minecraft.server.spigot.essentials.helpers;

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
    private final Type it;
    private final Rarity ir;

    private List<String> lore;

    /**
     *
     * @param material Bukkit Material
     * @param amount Integer
     * @param displayName String
     * @param itemType ItemType
     * @param itemRarity ItemRarity
     */
    public ItemHelper(Material material, int amount, String displayName, Type itemType, Rarity itemRarity) {
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
    public ItemHelper(Material material, int amount, String displayName, Type itemType, Rarity itemRarity, List<String> lore) {
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
    protected ItemStack getItemStack() {
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
    public Type getItemType() {
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
                loc_lore = new ArrayList<String>();
            }

            loc_lore.remove(Rarity.COMMON.getPrefix() + Rarity.COMMON.getDisplayName());
            loc_lore.remove(Rarity.UNCOMMON.getPrefix() + Rarity.UNCOMMON.getDisplayName());
            loc_lore.remove(Rarity.RARE.getPrefix() + Rarity.RARE.getDisplayName());
            loc_lore.remove(Rarity.EPIC.getPrefix() + Rarity.EPIC.getDisplayName());
            loc_lore.remove(Rarity.LEGENDARY.getPrefix() + Rarity.LEGENDARY.getDisplayName());
            
            if(ir.level != -1){
                loc_lore.add(ir.getPrefix() + "" + ir.getDisplayName());
            }

            im.setLore(loc_lore);
            is.setItemMeta(im);
        }
    }

    public enum Rarity {
        NONE("", "", -1),
        COMMON("Common", "§f§l", 0),
        UNCOMMON("Uncommon", "§a§l", 1),
        RARE("Rare", "§9§l", 2),
        EPIC("Epic", "§5§l", 3),
        LEGENDARY("Legendary", "§6§l", 4);

        private final String displayName;
        private final String prefix;
        private final int level;

        private Rarity(String displayName, String prefix, int level) {
            this.displayName = displayName;
            this.prefix = prefix;
            this.level = level;
        }

        /**
         * Returns Displayname of Rarity
         *
         * @return String displayName
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Returns Prefix of Rarity (Color and Bold)
         *
         * @return String prefix
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * Returns Level of Rarity
         *
         * @return int level
         */
        public int getLevel() {
            return level;
        }
    }

    public enum Type {
        TOOL,
        INGREDIENT,
        GADGET,
        ARMOR,
        WEAPON,
        HUB,
        DECORATION,
        BUILDING,
        NPC,
        NPC_GUI,
        NONE;
    }
}
