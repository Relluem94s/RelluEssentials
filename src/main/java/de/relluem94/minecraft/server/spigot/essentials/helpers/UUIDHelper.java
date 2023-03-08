package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.UUID;

public class UUIDHelper {
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

      public static String unDashed(UUID id) {
        return id.toString().replace("-", "");
      }
}
