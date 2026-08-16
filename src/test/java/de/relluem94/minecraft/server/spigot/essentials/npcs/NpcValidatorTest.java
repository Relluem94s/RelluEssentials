package de.relluem94.minecraft.server.spigot.essentials.npcs;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcValidator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcValidatorTest {

  private NpcValidator npcValidator;

  @BeforeEach
  void setUp() {
    npcValidator = new NpcValidator();
  }

  @Test
  void validateProfileNameReturnsSuccessForValidName() {
    ValidationResult result = npcValidator.validateProfileName("ValidName1");
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForNullName() {
    ValidationResult result = npcValidator.validateProfileName(null);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Profile name must not be empty.", result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForBlankName() {
    ValidationResult result = npcValidator.validateProfileName("   ");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Profile name must not be empty.", result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForEmptyName() {
    ValidationResult result = npcValidator.validateProfileName("");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Profile name must not be empty.", result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForNameTooShort() {
    ValidationResult result = npcValidator.validateProfileName("ab");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Profile name must be between 3 and 16 characters.", result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForNameTooLong() {
    ValidationResult result = npcValidator.validateProfileName("ThisNameIsWayTooLong");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Profile name must be between 3 and 16 characters.", result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsSuccessForNameAtMinLength() {
    ValidationResult result = npcValidator.validateProfileName("abc");
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsSuccessForNameAtMaxLength() {
    ValidationResult result = npcValidator.validateProfileName("ValidName1234567".substring(0, 16));
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForNameWithInvalidCharacters() {
    ValidationResult result = npcValidator.validateProfileName("Invalid-Name!");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Profile name may only contain letters, digits, and underscores.", result.errorMessage())
    );
  }

  @Test
  void validateProfileNameReturnsSuccessForNameWithUnderscores() {
    ValidationResult result = npcValidator.validateProfileName("Valid_Name_1");
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsSuccessForValidCoordinates() {
    ValidationResult result = npcValidator.validateCoordinates(0, 64, 0);
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForXBelowMinimum() {
    ValidationResult result = npcValidator.validateCoordinates(-30_000_001, 64, 0);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("X coordinate is out of bounds (-3.0E7 to 3.0E7).", result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForXAboveMaximum() {
    ValidationResult result = npcValidator.validateCoordinates(30_000_001, 64, 0);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("X coordinate is out of bounds (-3.0E7 to 3.0E7).", result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForYBelowMinimum() {
    ValidationResult result = npcValidator.validateCoordinates(0, -2049, 0);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Y coordinate is out of bounds (-2048.0 to 2048.0).", result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForYAboveMaximum() {
    ValidationResult result = npcValidator.validateCoordinates(0, 2049, 0);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Y coordinate is out of bounds (-2048.0 to 2048.0).", result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForZBelowMinimum() {
    ValidationResult result = npcValidator.validateCoordinates(0, 64, -30_000_001);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Z coordinate is out of bounds (-3.0E7 to 3.0E7).", result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForZAboveMaximum() {
    ValidationResult result = npcValidator.validateCoordinates(0, 64, 30_000_001);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Z coordinate is out of bounds (-3.0E7 to 3.0E7).", result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsSuccessForBoundaryMinValues() {
    ValidationResult result = npcValidator.validateCoordinates(-30_000_000, -2048, -30_000_000);
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.errorMessage())
    );
  }

  @Test
  void validateCoordinatesReturnsSuccessForBoundaryMaxValues() {
    ValidationResult result = npcValidator.validateCoordinates(30_000_000, 2048, 30_000_000);
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.errorMessage())
    );
  }

  @Test
  void validationResultSuccessReturnsValidTrueAndNullMessage() {
    ValidationResult result = ValidationResult.success();
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.errorMessage())
    );
  }

  @Test
  void validationResultFailureReturnsValidFalseAndErrorMessage() {
    ValidationResult result = ValidationResult.failure("Some error");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals("Some error", result.errorMessage())
    );
  }
}