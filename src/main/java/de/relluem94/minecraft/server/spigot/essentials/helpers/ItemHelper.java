package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces.IItemHelper;

/**
 *
 * @author rellu
 */
public class ItemHelper implements IItemHelper {

    private final ItemStack is;

    @Getter
    private final Material material;

    @Getter
    private final int amount;

    @Getter
    private final String displayName;
    private final Type it;
    private final Rarity ir;

    @Getter
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
        if(im == null){
            return;
        }

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
        if(im == null){
            return;
        }

        im.setDisplayName(this.displayName);
        im.setLore(this.lore);
        is.setItemMeta(im);
    }

     /**
     *
     * @param is ItemStack
     * @param displayName String
     * @param itemType ItemType
     * @param itemRarity ItemRarity
     */
    public ItemHelper(ItemStack is, String displayName, Type itemType, Rarity itemRarity) {
        this.amount = is.getAmount();
        this.material = is.getType();
        this.displayName = displayName;
        this.it = itemType;
        this.ir = itemRarity;

        this.is = is;

        ItemMeta im = is.getItemMeta();
        if(im == null){
            return;
        }

        im.setDisplayName(this.displayName);
        is.setItemMeta(im);
    }

     /**
     *
     * @param is ItemStack
     * @param displayName String
     * @param itemType ItemType
     * @param itemRarity ItemRarity
     * @param lore List String
     */
    public ItemHelper(ItemStack is, String displayName, Type itemType, Rarity itemRarity, List<String> lore) {
        this.amount = is.getAmount();
        this.material = is.getType();
        this.displayName = displayName;
        this.lore = lore;
        this.it = itemType;
        this.ir = itemRarity;

        this.is = is;

        ItemMeta im = is.getItemMeta();
        if(im == null){
            return;
        }

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
        return postInit(is);
    }

    /**
     *
     * @return ItemStack of ItemHelper
     */
    @SuppressWarnings("unused")
    protected ItemStack getItemStack() {
        return is;
    }

    /**
     *
     * @return ItemType of ItemHelper
     */
    @SuppressWarnings("unused")
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
     * @param itemMeta ItemMeta sets ItemMeta of ItemStack
     */
    public void setItemMeta(ItemMeta itemMeta) {
        is.setItemMeta(itemMeta);
    }

    /**
     *
     * @param compare ItemStack
     * @return boolean
     */
    public boolean equalsExact(ItemStack compare) {
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

        if(item.getItemMeta() == null){
            return false;
        }

        return item.hasItemMeta() && item.getItemMeta().equals(compare.getItemMeta());
    }

    /**
     *
     * @param compare ItemStack
     * @return boolean
     */
    public boolean equalsName(ItemStack compare) {
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

        return Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals(Objects.requireNonNull(compare.getItemMeta()).getDisplayName()) ;
    }

    /**
     *
     * @param compare ItemStack
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

        return item.hasItemMeta() == compare.hasItemMeta();
    }

    @Override
    public void init() {
        // has to be overwritten
    }

    @Override
    public ItemStack postInit(ItemStack is) {
        // has to be overwritten
        return is;
    }

    private void addItemRarity() {
        ItemMeta im;
        if (is.hasItemMeta() && is.getItemMeta() != null) {
            im = is.getItemMeta();
            List<String> locLore;
            if (im.getLore() != null) {
                locLore = im.getLore();
            } else {
                locLore = new ArrayList<>();
            }

            locLore.remove(Rarity.COMMON.getPrefix() + Rarity.COMMON.getDisplayName());
            locLore.remove(Rarity.UNCOMMON.getPrefix() + Rarity.UNCOMMON.getDisplayName());
            locLore.remove(Rarity.RARE.getPrefix() + Rarity.RARE.getDisplayName());
            locLore.remove(Rarity.EPIC.getPrefix() + Rarity.EPIC.getDisplayName());
            locLore.remove(Rarity.LEGENDARY.getPrefix() + Rarity.LEGENDARY.getDisplayName());
            
            if(ir.level != -1){
                locLore.add(ir.getPrefix() + ir.getDisplayName());
            }

            im.setLore(locLore);
            is.setItemMeta(im);
        }
    }

    public static List<String> remove(List<String> locLore){
        locLore.remove(Rarity.COMMON.getPrefix() + Rarity.COMMON.getDisplayName());
        locLore.remove(Rarity.UNCOMMON.getPrefix() + Rarity.UNCOMMON.getDisplayName());
        locLore.remove(Rarity.RARE.getPrefix() + Rarity.RARE.getDisplayName());
        locLore.remove(Rarity.EPIC.getPrefix() + Rarity.EPIC.getDisplayName());
        locLore.remove(Rarity.LEGENDARY.getPrefix() + Rarity.LEGENDARY.getDisplayName());
        return locLore;
    }

    @Getter
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

        Rarity(String displayName, String prefix, int level) {
            this.displayName = displayName;
            this.prefix = prefix;
            this.level = level;
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
        ENCHANTMENT,
        MONEY,
        ADMIN_TOOL,
        NONE
    }

    @SuppressWarnings("unused")
    public static ItemStack setDisplayName(ItemStack is, String displayName){
        ItemMeta im = is.getItemMeta();

        Objects.requireNonNull(im).setDisplayName(displayName);

        is.setItemMeta(im);
        return is;
    }

    @Contract("_ -> new")
    public static @NotNull ItemStack getCleanItemStack(@NotNull ItemStack is){
        return new ItemStack(is.getType(), 1);
    }

    @Contract("_ -> new")
    @SuppressWarnings("unused")
    public static @NotNull ItemStack getCleanItemStackWithAmount(@NotNull ItemStack is){
        return new ItemStack(is.getType(), is.getAmount());
    }

    @Contract("_, _ -> param1")
    public static @NotNull ItemStack addBookEnchantment(@NotNull ItemStack item, EnchantmentHelper enchantment) {
        if(item.getItemMeta() instanceof EnchantmentStorageMeta meta){
            meta.getPersistentDataContainer().set(enchantment.getKey(), PersistentDataType.INTEGER, enchantment.getStartLevel());
            item.setItemMeta(meta);
        }
        
        return item;
    }

    public static String getItemName(@NotNull ItemStack is){
        String name = "";
        if(is.hasItemMeta()){
            ItemMeta meta = is.getItemMeta();
            if (Objects.requireNonNull(meta).hasDisplayName()){
                name = meta.getDisplayName();
            }
            else {
                name = "ERROR_404_NAME_NOT_FOUND_EXCEPTION";
            }
        }

        return name;
    }


    public static ItemStack getSmeltedItemStack(ItemStack is){
        ItemStack result = null;
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (!(recipe instanceof FurnaceRecipe)) {
                continue;
            }
            if (((FurnaceRecipe) recipe).getInput().getType() != is.getType()) {
                continue;
            }

            if( recipe.getResult().getType() != is.getType()){
                result = recipe.getResult();
                break;
            }
           
        }
        
        if(result != null){
            result.setAmount(is.getAmount());
            
        }
        return result;
    }


    public static String itemTo64(ItemStack stack) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(stack);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }
   
    public static ItemStack itemFrom64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                return (ItemStack) dataInput.readObject();
            }
        }
        catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}
