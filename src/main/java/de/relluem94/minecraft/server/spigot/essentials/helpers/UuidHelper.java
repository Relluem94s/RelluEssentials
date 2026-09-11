package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import java.util.UUID;

/**
 * Utility class for converting between dashed and undashed UUID formats.
 *
 * @author rellu
 */
public class UuidHelper {

  private UuidHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Converts an undashed UUID string to a {@link UUID} with dashes.
   *
   * @param id the undashed UUID string to convert
   * @return the corresponding {@link UUID} with dashes
   */
  public static UUID dashed(String id) {
    return UUID.fromString(
        new StringBuilder(id)
            .insert(20, '-')
            .insert(16, '-')
            .insert(12, '-')
            .insert(8, '-')
            .toString()
    );
  }

  /**
   * Converts a {@link UUID} to an undashed UUID string.
   *
   * @param id the {@link UUID} to convert
   * @return the UUID as a string without dashes
   */
  public static String unDashed(UUID id) {
    return id.toString().replace("-", "");
  }
}