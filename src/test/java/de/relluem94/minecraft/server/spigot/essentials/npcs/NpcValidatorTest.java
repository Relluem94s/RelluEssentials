package de.relluem94.minecraft.server.spigot.essentials.npcs;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
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
        () -> assertNull(result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForNullName() {
    ValidationResult result = npcValidator.validateProfileName(null);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_EMPTY, result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForBlankName() {
    ValidationResult result = npcValidator.validateProfileName("   ");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_EMPTY, result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForEmptyName() {
    ValidationResult result = npcValidator.validateProfileName("");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_EMPTY, result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForNameTooShort() {
    ValidationResult result = npcValidator.validateProfileName("ab");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_LENGTH, result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForNameTooLong() {
    ValidationResult result = npcValidator.validateProfileName("ThisNameIsWayTooLong");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_LENGTH, result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsSuccessForNameAtMinLength() {
    ValidationResult result = npcValidator.validateProfileName("abc");
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsSuccessForNameAtMaxLength() {
    ValidationResult result = npcValidator.validateProfileName("ValidName1234567".substring(0, 16));
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsFailureForNameWithInvalidCharacters() {
    ValidationResult result = npcValidator.validateProfileName("Invalid-Name!");
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_PROFILE_NAME_INVALID_CHARACTERS, result.messageKey())
    );
  }

  @Test
  void validateProfileNameReturnsSuccessForNameWithUnderscores() {
    ValidationResult result = npcValidator.validateProfileName("Valid_Name_1");
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsSuccessForValidCoordinates() {
    ValidationResult result = npcValidator.validateCoordinates(0, 64, 0);
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForXaxisBelowMinimum() {
    ValidationResult result = npcValidator.validateCoordinates(-30_000_001, 64, 0);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_NPC_X_COORDINATE_OUT_OF_BOUNDS, result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForXaxisAboveMaximum() {
    ValidationResult result = npcValidator.validateCoordinates(30_000_001, 64, 0);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_NPC_X_COORDINATE_OUT_OF_BOUNDS, result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForYaxisBelowMinimum() {
    ValidationResult result = npcValidator.validateCoordinates(0, -2049, 0);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_NPC_Y_COORDINATE_OUT_OF_BOUNDS, result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForYaxisAboveMaximum() {
    ValidationResult result = npcValidator.validateCoordinates(0, 2049, 0);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_NPC_Y_COORDINATE_OUT_OF_BOUNDS, result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForZaxisBelowMinimum() {
    ValidationResult result = npcValidator.validateCoordinates(0, 64, -30_000_001);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_NPC_Z_COORDINATE_OUT_OF_BOUNDS, result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsFailureForZaxisAboveMaximum() {
    ValidationResult result = npcValidator.validateCoordinates(0, 64, 30_000_001);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_NPC_VALIDATION_NPC_Z_COORDINATE_OUT_OF_BOUNDS, result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsSuccessForBoundaryMinValues() {
    ValidationResult result = npcValidator.validateCoordinates(-30_000_000, -2048, -30_000_000);
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.messageKey())
    );
  }

  @Test
  void validateCoordinatesReturnsSuccessForBoundaryMaxValues() {
    ValidationResult result = npcValidator.validateCoordinates(30_000_000, 2048, 30_000_000);
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.messageKey())
    );
  }

  @Test
  void validationResultSuccessReturnsValidTrueAndNullMessage() {
    ValidationResult result = ValidationResult.success();
    assertAll(
        () -> assertTrue(result.valid()),
        () -> assertNull(result.messageKey())
    );
  }

  @Test
  void validationResultFailureReturnsValidFalseAndErrorMessage() {
    ValidationResult result = ValidationResult.failure(MessageKey.PLUGIN_FOLDER_MKDIR_ERROR);
    assertAll(
        () -> assertFalse(result.valid()),
        () -> assertEquals(MessageKey.PLUGIN_FOLDER_MKDIR_ERROR, result.messageKey())
    );
  }
}