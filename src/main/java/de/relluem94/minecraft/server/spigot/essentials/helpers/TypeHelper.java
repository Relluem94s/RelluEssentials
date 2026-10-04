package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Utility class providing type-checking and material-matching helper methods.
 *
 * @author rellu
 */
public class TypeHelper {

  private TypeHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Checks whether the given string can be parsed as an integer.
   *
   * @param s the string to check
   * @return {@code true} if the string represents a valid integer, {@code false} otherwise
   */
  public static boolean isInt(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException er) {
      return false;
    }
  }

  /**
   * Checks whether the given string can be parsed as a double.
   *
   * @param s the string to check
   * @return {@code true} if the string represents a valid double, {@code false} otherwise
   */
  public static boolean isDouble(String s) {
    try {
      Double.parseDouble(s);
      return true;
    } catch (NumberFormatException er) {
      return false;
    }
  }

  /**
   * Checks whether the given string can be parsed as a float.
   *
   * @param s the string to check
   * @return {@code true} if the string represents a valid float, {@code false} otherwise
   */
  public static boolean isFloat(String s) {
    try {
      Float.parseFloat(s);
      return true;
    } catch (NumberFormatException er) {
      return false;
    }
  }

  /**
   * Checks whether the given string can be parsed as a long.
   *
   * @param s the string to check
   * @return {@code true} if the string represents a valid long, {@code false} otherwise
   */
  public static boolean isLong(String s) {
    try {
      Long.parseLong(s);
      return true;
    } catch (NumberFormatException er) {
      return false;
    }
  }

  /**
   * Checks whether all blocks in the given list are of the specified material.
   *
   * @param blocks   the list of blocks to check
   * @param material the material to compare against
   * @return {@code true} if every block matches the given material, {@code false} otherwise
   */
  public static boolean areBlocksMaterial(List<Block> blocks, Material material) {
    boolean isMat = true;
    for (Block b : blocks) {
      if (!b.getType().equals(material)) {
        isMat = false;
      }
    }
    return isMat;
  }

  /**
   * Checks whether the given block's material matches any material in the provided list.
   *
   * @param block     the block to check
   * @param materials the list of materials to match against
   * @return {@code true} if the block's material is contained in the list, {@code false} otherwise
   */
  public static boolean isBlockOneOfMaterials(Block block, List<Material> materials) {
    return materials.stream().anyMatch(material -> block.getType().equals(material));
  }

  /**
   * Checks whether the given command sender is a player.
   *
   * @param sender the command sender to check
   * @return {@code true} if the sender is an instance of {@link Player}, {@code false} otherwise
   */
  public static boolean isPlayer(CommandSender sender) {
    return sender instanceof Player;
  }

  /**
   * Checks whether the given command sender is a command block.
   *
   * @param sender the command sender to check
   * @return {@code true} if the sender is an instance of {@link BlockCommandSender},
   *     {@code false} otherwise
   */
  public static boolean isCmdBlock(CommandSender sender) {
    return sender instanceof BlockCommandSender;
  }

  /**
   * Checks whether the given command sender is the console.
   *
   * @param sender the command sender to check
   * @return {@code true} if the sender is an instance of {@link ConsoleCommandSender},
   *     {@code false} otherwise
   */
  public static boolean isConsole(CommandSender sender) {
    return sender instanceof ConsoleCommandSender;
  }

  /**
   * Checks whether the given material is contained in the provided list.
   *
   * @param material     the material to look for
   * @param materialList the list of materials to search in
   * @return {@code true} if the material is present in the list, {@code false} otherwise
   */
  public static boolean isMaterialInList(Material material, List<Material> materialList) {
    return materialList.contains(material);
  }

  /**
   * Checks whether the given material is contained in the provided array.
   *
   * @param material      the material to look for
   * @param materialArray the array of materials to search in
   * @return {@code true} if the material is present in the array, {@code false} otherwise
   */
  public static boolean isMaterialInArray(Material material, Material[] materialArray) {
    return isMaterialInList(material, Arrays.asList(materialArray));
  }
}
