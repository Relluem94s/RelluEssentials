package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.UUID;

import de.relluem94.minecraft.server.spigot.essentials.Strings;

public class UUIDHelper {

  private UUIDHelper() {
    throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
}

    /**
     * converts unDashed UUID (short) to dashed UUID (long)
     * @param id String
     * @return UUID
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
       * converts dashed UUID (long) to unDashed UUID (short)
       * @param id
       * @return String
       */
      public static String unDashed(UUID id) {
        return id.toString().replace("-", "");
      }
}