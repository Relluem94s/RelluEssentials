package de.relluem94.minecraft.server.spigot.essentials.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a namespaced key used in RelluEssentials.
 *
 * @author rellu
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RelluEssentialsNamespacedKey {
  private final String namespace;
  private final String key;

  @Override
  public String toString() {
    return namespace + ":" + key;
  }
}
