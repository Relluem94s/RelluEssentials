package de.relluem94.minecraft.server.spigot.essentials.npc;

public class NpcValidator {

  private static final int MINECRAFT_USERNAME_MIN_LENGTH = 3;
  private static final int MINECRAFT_USERNAME_MAX_LENGTH = 16;
  private static final double MINECRAFT_MIN_COORDINATE = -30_000_000;
  private static final double MINECRAFT_MAX_COORDINATE = 30_000_000;
  private static final double MINECRAFT_MIN_Y = -2048;
  private static final double MINECRAFT_MAX_Y = 2048;

  public ValidationResult validateProfileName(String profileName) {
    if (profileName == null || profileName.isBlank()) {
      return ValidationResult.failure("Profile name must not be empty.");
    }
    if (profileName.length() < MINECRAFT_USERNAME_MIN_LENGTH
        || profileName.length() > MINECRAFT_USERNAME_MAX_LENGTH) {
      return ValidationResult.failure(
          "Profile name must be between " + MINECRAFT_USERNAME_MIN_LENGTH + " and "
              + MINECRAFT_USERNAME_MAX_LENGTH + " characters."
      );
    }
    if (!profileName.matches("[a-zA-Z0-9_]+")) {
      return ValidationResult.failure(
          "Profile name may only contain letters, digits, and underscores.");
    }
    return ValidationResult.success();
  }

  public ValidationResult validateCoordinates(double x, double y, double z) {
    if (x < MINECRAFT_MIN_COORDINATE || x > MINECRAFT_MAX_COORDINATE) {
      return ValidationResult.failure(
          "X coordinate is out of bounds (" + MINECRAFT_MIN_COORDINATE + " to "
              + MINECRAFT_MAX_COORDINATE + ").");
    }
    if (y < MINECRAFT_MIN_Y || y > MINECRAFT_MAX_Y) {
      return ValidationResult.failure(
          "Y coordinate is out of bounds (" + MINECRAFT_MIN_Y + " to " + MINECRAFT_MAX_Y + ").");
    }
    if (z < MINECRAFT_MIN_COORDINATE || z > MINECRAFT_MAX_COORDINATE) {
      return ValidationResult.failure(
          "Z coordinate is out of bounds (" + MINECRAFT_MIN_COORDINATE + " to "
              + MINECRAFT_MAX_COORDINATE + ").");
    }
    return ValidationResult.success();
  }

  public record ValidationResult(boolean valid, String errorMessage) {

    public static ValidationResult success() {
      return new ValidationResult(true, null);
    }

    public static ValidationResult failure(String errorMessage) {
      return new ValidationResult(false, errorMessage);
    }
  }
}