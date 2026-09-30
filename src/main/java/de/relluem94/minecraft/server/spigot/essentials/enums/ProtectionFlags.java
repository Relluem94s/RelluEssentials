package de.relluem94.minecraft.server.spigot.essentials.enums;

import lombok.Getter;

/**
 * Represents the different logical states a player can be in within the plugin,
 * such as protection configuration, sign interaction, light toggling, fake AFK,
 * and damage information display.
 *
 * @author rellu
 * @version 3.0
 * @since 2.0
 */
@Getter
public enum ProtectionFlags {
  ALLOW_HOPPER(false),
  ALLOW_REDSTONE(false),
  ALLOW_PUBLIC(false),
  AUTO_CLOSE(false);

  private final boolean isDefault;

  /**
   * Creates a protection flag with the given default activation state.
   *
   * @param isDefault true if the flag should be active by default, false otherwise
   */
  ProtectionFlags(boolean isDefault) {
    this.isDefault = isDefault;
  }
}