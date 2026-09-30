package de.relluem94.minecraft.server.spigot.essentials.npcs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcValidator.ValidationResult;
import org.junit.jupiter.api.Test;

class NpcOperationResultTest {

  @Test
  void successResultIsSuccessfulAndContainsNpc() {
    Npc npc = new Npc(-1, null, "TestProfile", 0, 0, 0, 0, 0, "world");

    NpcOperationResult result = NpcOperationResult.success(npc);

    assertTrue(result.isSuccessful());
    assertEquals(npc, result.getNpc());
    assertNull(result.getValidationResult());
  }

  @Test
  void failureResultIsNotSuccessfulAndContainsValidationResult() {
    ValidationResult validationResult = ValidationResult.failure(MessageKey.PLUGIN_FOLDER_MKDIR_ERROR);

    NpcOperationResult result = NpcOperationResult.failure(validationResult);

    assertFalse(result.isSuccessful());
    assertEquals(validationResult, result.getValidationResult());
    assertNull(result.getNpc());
  }

  @Test
  void successResultWithNullNpcIsStillSuccessful() {
    NpcOperationResult result = NpcOperationResult.success(null);

    assertTrue(result.isSuccessful());
    assertNull(result.getNpc());
  }

  @Test
  void failureResultWithNullValidationResultHasNoValidationResult() {
    NpcOperationResult result = NpcOperationResult.failure(null);

    assertFalse(result.isSuccessful());
    assertNull(result.getValidationResult());
  }
}