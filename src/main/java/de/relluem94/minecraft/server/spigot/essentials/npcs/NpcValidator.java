package de.relluem94.minecraft.server.spigot.essentials.npcs;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;

/**
 * Provides validation logic for NPC-related input data such as profile names and world coordinates.
 *
 * @author rellu
 */
public class NpcValidator {

  private static final int MINECRAFT_USERNAME_MIN_LENGTH = 3;
  private static final int MINECRAFT_USERNAME_MAX_LENGTH = 16;
  private static final double MINECRAFT_MIN_COORDINATE = -30_000_000;
  private static final double MINECRAFT_MAX_COORDINATE = 30_000_000;
  private static final double MINECRAFT_MIN_Y = -2048;
  private static final double MINECRAFT_MAX_Y = 2048;

  /**
   * Validates a Minecraft profile name against length and character constraints.
   *
   * @param profileName the profile name to validate
   * @return a {@link ValidationResult} indicating success or describing the validation failure
   */
  public ValidationResult validateProfileName(String profileName) {
    if (profileName == null || profileName.isBlank()) {
      return ValidationResult.failure(MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_EMPTY);
    }
    if (profileName.length() < MINECRAFT_USERNAME_MIN_LENGTH
        || profileName.length() > MINECRAFT_USERNAME_MAX_LENGTH) {
      return ValidationResult.failure(
          MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_LENGTH,
          MINECRAFT_USERNAME_MIN_LENGTH,
          MINECRAFT_USERNAME_MAX_LENGTH
      );
    }
    if (!profileName.matches("[a-zA-Z0-9_]+")) {
      return ValidationResult.failure(MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_INVALID_CHARACTERS);
    }
    return ValidationResult.success();
  }

  /**
   * Validates that the given world coordinates are within Minecraft's allowed bounds.
   *
   * @param x the X coordinate to validate
   * @param y the Y coordinate to validate
   * @param z the Z coordinate to validate
   * @return a {@link ValidationResult} indicating success or describing the validation failure
   */
  public ValidationResult validateCoordinates(double x, double y, double z) {
    if (x < MINECRAFT_MIN_COORDINATE || x > MINECRAFT_MAX_COORDINATE) {
      return ValidationResult.failure(
          MessageKey.PLUGIN_NPC_VALIDATION_NPC_X_COORDINATE_OUT_OF_BOUNDS,
          MINECRAFT_MIN_COORDINATE,
          MINECRAFT_MAX_COORDINATE
      );
    }
    if (y < MINECRAFT_MIN_Y || y > MINECRAFT_MAX_Y) {
      return ValidationResult.failure(
          MessageKey.PLUGIN_NPC_VALIDATION_NPC_Y_COORDINATE_OUT_OF_BOUNDS,
          MINECRAFT_MIN_Y,
          MINECRAFT_MAX_Y
      );
    }
    if (z < MINECRAFT_MIN_COORDINATE || z > MINECRAFT_MAX_COORDINATE) {
      return ValidationResult.failure(
          MessageKey.PLUGIN_NPC_VALIDATION_NPC_Z_COORDINATE_OUT_OF_BOUNDS,
          MINECRAFT_MIN_COORDINATE,
          MINECRAFT_MAX_COORDINATE
      );
    }
    return ValidationResult.success();
  }

  /**
   * Represents the outcome of a validation check, containing the validity status,
   * an optional {@link MessageKey} describing the failure reason, and optional parameters
   * used to format the failure message.
   *
   * @param valid      whether the validation passed
   * @param messageKey the {@link MessageKey} describing the failure reason, or {@code null} if valid
   * @param params     optional parameters used to format the failure message, or {@code null} if valid
   */
  public record ValidationResult(boolean valid, MessageKey messageKey, Object[] params) {

    /**
     * Creates a {@link ValidationResult} representing a successful validation.
     *
     * @return a {@link ValidationResult} with {@code valid} set to {@code true} and no error message
     */
    public static ValidationResult success() {
      return new ValidationResult(true, null, null);
    }

    /**
     * Creates a {@link ValidationResult} representing a failed validation.
     *
     * @param errorMessage the {@link MessageKey} describing the reason for the failure
     * @param params       optional parameters used to format the failure message
     * @return a {@link ValidationResult} with {@code valid} set to {@code false} and the given error message
     */
    public static ValidationResult failure(MessageKey errorMessage, Object... params) {
      return new ValidationResult(false, errorMessage, params);
    }
  }
}