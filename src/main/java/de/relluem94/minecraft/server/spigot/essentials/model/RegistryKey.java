package de.relluem94.minecraft.server.spigot.essentials.model;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

public record RegistryKey(String namespace, String key) {

  private static Plugin registeredPlugin;

  @ApiStatus.Internal
  public static void initializeInternalPlugin(@NonNull Plugin plugin) {
    registeredPlugin = plugin;
  }

  public static RegistryKey of(@NonNull String key) {
    if (registeredPlugin == null) {
      throw new IllegalStateException(
          "Internal plugin not initialized. Call ItemRegistry.initialize() first.");
    }
    return new RegistryKey(registeredPlugin.getName().toLowerCase(), key.toLowerCase());
  }

  public static RegistryKey of(@NonNull Plugin plugin, @NonNull String key) {
    return new RegistryKey(plugin.getName().toLowerCase(), key.toLowerCase());
  }

  public static RegistryKey of(@NonNull String namespace, @NonNull String key) {
    return new RegistryKey(namespace.toLowerCase(), key.toLowerCase());
  }

  public static RegistryKey fromString(@NonNull String fullKey) {
    String[] parts = fullKey.split(":", 2);
    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid RegistryKey format: '" + fullKey + "'");
    }
    return new RegistryKey(parts[0].toLowerCase(), parts[1].toLowerCase());
  }

  @Override
  public @NonNull String toString() {
    return namespace + ":" + key;
  }
}