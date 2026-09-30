package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class providing helper methods for string manipulation,
 * Minecraft color code conversion, and human-readable number scaling.
 *
 * @author rellu
 */
public class StringHelper {

  private StringHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Replaces all occurrences of {@code &} with the section sign {@code §}
   * to activate Minecraft chat color codes.
   *
   * @param message the raw message containing {@code &} color codes
   * @return the message with {@code &} replaced by {@code §}
   */
  public static String replaceColor(String message) {
    return message.replace("&", "§");
  }

  /**
   * Converts the first character of the given string to uppercase.
   *
   * @param s the input string
   * @return the input string with its first character converted to uppercase
   */
  public static @NotNull String firstCharToUpper(@NotNull String s) {
    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
  }

  /**
   * Formats a {@code long} value into a human-readable scaled string.
   *
   * <p>Values are abbreviated using the following suffixes:
   * <ul>
   *   <li>{@code B} for billions (≥ 1,000,000,000)</li>
   *   <li>{@code M} for millions (≥ 1,000,000)</li>
   *   <li>{@code K} for thousands (≥ 1,000)</li>
   * </ul>
   * Values below 1,000 are returned as-is.
   *
   * @param l the {@code long} value to format
   * @return a human-readable scaled string representation of the value
   */
  public static @NotNull String formatLong(long l) {
    if (l >= 1000000000) {
      return String.format("%sB", l / 1000000000.0);
    } else if (l >= 1000000) {
      return String.format("%sM", l / 1000000.0);
    } else if (l >= 1000) {
      return String.format("%sK", l / 1000.0);
    } else {
      return String.format("%s", l);
    }
  }

  /**
   * Formats an {@code int} value into a human-readable scaled string.
   *
   * <p>Values are abbreviated using the following suffixes:
   * <ul>
   *   <li>{@code B} for billions (≥ 1,000,000,000)</li>
   *   <li>{@code M} for millions (≥ 1,000,000)</li>
   *   <li>{@code K} for thousands (≥ 1,000)</li>
   * </ul>
   * Values below 1,000 are returned as-is.
   *
   * @param i the {@code int} value to format
   * @return a human-readable scaled string representation of the value
   */
  public static @NotNull String formatInt(int i) {
    if (i >= 1000000000) {
      return String.format("%sB", i / 1000000000);
    } else if (i >= 1000000) {
      return String.format("%sM", i / 1000000);
    } else if (i >= 1000) {
      return String.format("%sK", i / 1000);
    } else {
      return String.format("%s", i);
    }
  }

  /**
   * Formats a {@code double} value into a human-readable scaled string with two decimal places.
   *
   * <p>Values are abbreviated using the following suffixes:
   * <ul>
   *   <li>{@code B} for billions (≥ 1,000,000,000 or ≤ -1,000,000,000)</li>
   *   <li>{@code M} for millions (≥ 1,000,000 or ≤ -1,000,000)</li>
   *   <li>{@code K} for thousands (≥ 1,000 or ≤ -1,000)</li>
   * </ul>
   * Values between -1,000 and 1,000 (exclusive) are formatted with two decimal places
   * without a suffix.
   *
   * @param d the {@code double} value to format
   * @return a human-readable scaled string representation of the value with two decimal places
   */
  public static @NotNull String formatDouble(double d) {
    if (d >= 1000000000 || d <= -1000000000) {
      return String.format("%.2fB", d / 1000000000.0);
    } else if (d >= 1000000 || d <= -1000000) {
      return String.format("%.2fM", d / 1000000.0);
    } else if (d >= 1000 || d <= -1000) {
      return String.format("%.2fK", d / 1000.0);
    } else {
      return String.format("%.2f", d);
    }
  }
}