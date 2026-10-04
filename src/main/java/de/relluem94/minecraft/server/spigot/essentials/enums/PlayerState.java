package de.relluem94.minecraft.server.spigot.essentials.enums;

/**
 * Represents the different logical states a player can be in within the plugin,
 * such as protection configuration, sign interaction, light toggling, fake AFK,
 * and damage information display.
 *
 * @author rellu
 * @version 3.0
 * @since 2.0
 */
public enum PlayerState {
  PROTECTION_INFO,
  PROTECTION_ADD,
  PROTECTION_REMOVE,
  PROTECTION_FLAG_ADD,
  PROTECTION_FLAG_REMOVE,
  PROTECTION_RIGHT_ADD,
  PROTECTION_RIGHT_REMOVE,
  LIGHT_TOGGLE,
  SIGN_COPY,
  SIGN_PASTE,
  SIGN_EDIT,
  FAKE_AFK_ON,
  FAKE_AFK_ACTIVE,
  DAMAGE_INFO,
  DEFAULT
}