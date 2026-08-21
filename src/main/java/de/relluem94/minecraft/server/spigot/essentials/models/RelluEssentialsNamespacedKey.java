package de.relluem94.minecraft.server.spigot.essentials.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RelluEssentialsNamespacedKey {
  private final String namespace;
  private final String key;

  @Override
  public String toString() {
    return namespace + ":" + key;
  }
}
