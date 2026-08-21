package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEMHELPER_INVALID_BASE64_DATA;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEMHELPER_NAME_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEMHELPER_UNABLE_TO_DECODE_CLASS_TYPE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEMHELPER_UNABLE_TO_SAVE_ITEMSTACK;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * A utility class for creating and managing custom Minecraft ItemStack objects with extended
 * metadata support.
 *
 * @author rellu
 */
public class ItemHelper {

  @Getter
  private final ItemStack is;

  @Getter
  private final Material material;

  @Getter
  private final int amount;

  @Getter
  private final String displayName;
  @Getter
  private final Type itemType;
  @Getter
  private final Rarity rarity;

  @Getter
  private List<String> lore;

  @Getter
  private Integer cost;

  /**
   * Constructs a new ItemHelper.
   *
   * @param material    Bukkit Material
   * @param amount      Integer
   * @param displayName String
   * @param itemType    ItemType
   * @param itemRarity  ItemRarity
   */
  public ItemHelper(Material material, int amount, String displayName, Type itemType,
      Rarity itemRarity) {
    this(material, amount, displayName, itemType, itemRarity, new ArrayList<>());
  }

  public ItemHelper(Material material, int amount, String displayName, Type itemType,
      Rarity itemRarity, int cost) {
    this(material, amount, displayName, itemType, itemRarity, new ArrayList<>());
    this.cost = cost;
  }

  /**
   * Constructs a new ItemHelper.
   *
   * @param material    Bukkit Material
   * @param amount      Integer
   * @param displayName String
   * @param itemType    ItemType
   * @param itemRarity  ItemRarity
   * @param lore        List String
   */
  public ItemHelper(Material material, int amount, String displayName, Type itemType,
      Rarity itemRarity, List<String> lore) {
    this.amount = amount;
    this.material = material;
    this.displayName = displayName;
    this.lore = lore;
    this.itemType = itemType;
    this.rarity = itemRarity;

    is = new ItemStack(this.material, this.amount);

    ItemMeta im = is.getItemMeta();
    if (im == null) {
      return;
    }

    im.setDisplayName(this.displayName);
    im.setLore(this.lore);
    is.setItemMeta(im);
  }

  /**
   * Constructs a new ItemHelper.
   *
   * @param is          ItemStack
   * @param displayName String
   * @param itemType    ItemType
   * @param itemRarity  ItemRarity
   */
  public ItemHelper(ItemStack is, String displayName, Type itemType, Rarity itemRarity) {
    this(is, displayName, itemType, itemRarity, new ArrayList<>());
  }

  public ItemHelper(Material material, int amount, String displayName, Type itemType,
      Rarity itemRarity, List<String> lore, int cost) {
    this(material, amount, displayName, itemType, itemRarity, lore);
    this.cost = cost;
  }

  /**
   * Constructs a new ItemHelper.
   *
   * @param is          ItemStack
   * @param displayName String
   * @param itemType    ItemType
   * @param itemRarity  ItemRarity
   * @param lore        List String
   */
  public ItemHelper(@NonNull ItemStack is, String displayName, Type itemType, Rarity itemRarity,
      List<String> lore) {
    this.amount = is.getAmount();
    this.material = is.getType();
    this.displayName = displayName;
    this.lore = lore;
    this.itemType = itemType;
    this.rarity = itemRarity;

    this.is = is;

    ItemMeta im = is.getItemMeta();
    if (im == null) {
      return;
    }

    im.setDisplayName(this.displayName);
    im.setLore(this.lore);
    is.setItemMeta(im);
  }

  public ItemHelper(@NotNull ItemStack itemStack, String displayName, Type type, Rarity rarity, int cost) {
    this(itemStack, displayName, type, rarity, new ArrayList<>());
    this.cost = cost;
  }

  @Contract("_ -> new")
  public static @NotNull ItemStack getCleanItemStack(@NotNull ItemStack is) {
    return new ItemStack(is.getType(), 1);
  }


  public static String getItemName(@NotNull ItemStack is) {
    String name = "";
    if (is.hasItemMeta()) {
      ItemMeta meta = is.getItemMeta();
      if (Objects.requireNonNull(meta).hasDisplayName()) {
        name = meta.getDisplayName();
      } else {
        name = PLUGIN_EXCEPTION_ITEMHELPER_NAME_NOT_FOUND;
      }
    }

    return name;
  }

  public static ItemStack getSmeltedItemStack(ItemStack is) {
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

      if (recipe.getResult().getType() != is.getType()) {
        result = recipe.getResult();
        break;
      }

    }

    if (result != null) {
      result.setAmount(is.getAmount());

    }
    return result;
  }

  public static String itemTo64(ItemStack stack) throws IllegalStateException {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
      dataOutput.writeObject(stack);
      dataOutput.close();
      return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException(PLUGIN_EXCEPTION_ITEMHELPER_UNABLE_TO_SAVE_ITEMSTACK, e);
    }
  }

  public static ItemStack itemFrom64(@NonNull String data) throws IOException {
    try {
      String cleaned = data.replaceAll("\\s+", "");

      ByteArrayInputStream inputStream = new ByteArrayInputStream(
          Base64.getDecoder().decode(cleaned));
      try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
        return (ItemStack) dataInput.readObject();
      }
    } catch (ClassNotFoundException e) {
      throw new IOException(PLUGIN_EXCEPTION_ITEMHELPER_UNABLE_TO_DECODE_CLASS_TYPE, e);
    } catch (IllegalArgumentException e) {
      throw new IOException(
          String.format(PLUGIN_EXCEPTION_ITEMHELPER_INVALID_BASE64_DATA, e.getMessage()), e);
    }
  }

  /**
   *
   * @return ItemStack of ItemHelper
   */
  public ItemStack getCustomItem() {
    return is;
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

  public void setData(NamespacedKey key, String value) {
    ItemMeta itemMeta = is.getItemMeta();
      if (itemMeta == null) {
          return;
      }
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    is.setItemMeta(itemMeta);
  }

  public String getData(NamespacedKey key) {
    ItemMeta itemMeta = is.getItemMeta();
      if (itemMeta == null) {
          return null;
      }
    return itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
  }

  public boolean hasData(NamespacedKey key) {
    ItemMeta itemMeta = is.getItemMeta();
      if (itemMeta == null) {
          return false;
      }
    return itemMeta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
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

    return item.isSimilar(compare);
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
    ItemMeta itemMeta = item.getItemMeta();
    ItemMeta compareMeta = compare.getItemMeta();

    if (itemMeta == null || compareMeta == null) {
      return false;
    }

    return itemMeta.getDisplayName().equals(compareMeta.getDisplayName());
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
}