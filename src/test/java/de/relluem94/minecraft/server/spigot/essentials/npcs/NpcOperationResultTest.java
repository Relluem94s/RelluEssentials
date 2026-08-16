package de.relluem94.minecraft.server.spigot.essentials.npcs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import org.junit.jupiter.api.Test;

class NpcOperationResultTest {

  @Test
  void successResultIsSuccessfulAndContainsNpc() {
    Npc npc = new Npc(-1, null, "TestProfile", 0, 0, 0, 0, 0, "world");

    NpcOperationResult result = NpcOperationResult.success(npc);

    assertTrue(result.isSuccessful());
    assertEquals(npc, result.getNpc());
    assertNull(result.getErrorMessage());
  }

  @Test
  void failureResultIsNotSuccessfulAndContainsErrorMessage() {
    String errorMessage = "Something went wrong";

    NpcOperationResult result = NpcOperationResult.failure(errorMessage);

    assertFalse(result.isSuccessful());
    assertEquals(errorMessage, result.getErrorMessage());
    assertNull(result.getNpc());
  }

  @Test
  void successResultWithNullNpcIsStillSuccessful() {
    NpcOperationResult result = NpcOperationResult.success(null);

    assertTrue(result.isSuccessful());
    assertNull(result.getNpc());
  }

  @Test
  void failureResultWithNullErrorMessageHasNoMessage() {
    NpcOperationResult result = NpcOperationResult.failure(null);

    assertFalse(result.isSuccessful());
    assertNull(result.getErrorMessage());
  }
}