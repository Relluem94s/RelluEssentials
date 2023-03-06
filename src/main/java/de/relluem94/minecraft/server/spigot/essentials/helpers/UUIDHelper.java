package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.UUID;

public class UUIDHelper {
    public static UUID dashed(String id) {
        return UUID.fromString(
            id.substring(0, 8) + "-" + 
            id.substring(0, 8) + "-" + 
            id.substring(8, 12) + "-" + 
            id.substring(12, 16) + "-" +
            id.substring(16, 20));
      }
      
      public static String unDashed(UUID id) {
        return id.toString().replace("-", "");
      }
}
