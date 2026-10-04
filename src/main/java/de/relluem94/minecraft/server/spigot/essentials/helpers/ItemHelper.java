package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEMHELPER_INVALID_BASE64_DATA;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEMHELPER_NAME_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEMHELPER_UNABLE_TO_DECODE_CLASS_TYPE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEMHELPER_UNABLE_TO_SAVE_ITEMSTACK;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * A utility class providing static helper methods for creating, converting, and managing
 * Minecraft {@link ItemStack} objects, including Base64 serialization and smelting result lookup.
 *
 * @author rellu
 */
public class ItemHelper {

  /**
   * Creates a new {@link ItemStack} of the same material as the given stack with a quantity of one,
   * stripping all metadata such as enchantments, display names, and lore.
   *
   * @param is the source {@link ItemStack} whose material type is used; must not be {@code null}
   * @return a new clean {@link ItemStack} with amount 1 and no metadata
   */
  @Contract("_ -> new")
  public static @NotNull ItemStack getCleanItemStack(@NotNull ItemStack is) {
    return new ItemStack(is.getType(), 1);
  }

  /**
   * Retrieves the display name of the given {@link ItemStack}
   * if one is set in its {@link ItemMeta}.
   *
   * <p>Returns the display name when present, a fallback constant defined in
   * {@code ExceptionConstants} when the meta exists but no display name is set,
   * or an empty string when the item has no meta at all.
   *
   * @param is the {@link ItemStack} whose display name is retrieved; must not be {@code null}
   * @return the display name, a not-found placeholder, or an empty string
   */
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

  /**
   * Searches the provided recipe iterator for a {@link FurnaceRecipe} that accepts the given
   * {@link ItemStack}'s material as input and returns a result of a different material.
   *
   * <p>When a matching recipe is found, the result's amount is set to match the amount of the
   * input stack before it is returned.
   *
   * @param is       the {@link ItemStack} to find a smelting result for
   * @param iterator an {@link Iterator} over all available {@link Recipe} instances to search
   * @return the smelted result {@link ItemStack} with its amount adjusted to match {@code is},
   *         or {@code null} if no matching furnace recipe is found
   */
  public static ItemStack getSmeltedItemStack(ItemStack is, Iterator<Recipe> iterator) {
    ItemStack result = null;
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

  /**
   * Serializes the given {@link ItemStack} into a Base64-encoded string using
   * {@link BukkitObjectOutputStream}.
   *
   * @param stack the {@link ItemStack} to serialize
   * @return a Base64-encoded string representing the serialized {@link ItemStack}
   * @throws IllegalStateException if the {@link ItemStack} cannot be serialized
   */
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

  /**
   * Deserializes a {@link ItemStack} from the given Base64-encoded string produced by
   * {@link #itemTo64(ItemStack)}.
   *
   * <p>Whitespace characters are stripped from {@code data} before decoding to tolerate
   * line-wrapped or formatted Base64 input.
   *
   * @param data the Base64-encoded string representing a serialized {@link ItemStack};
   *             must not be {@code null}
   * @return the deserialized {@link ItemStack}
   * @throws IOException if the Base64 data is invalid or the contained class cannot be resolved
   */
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
}