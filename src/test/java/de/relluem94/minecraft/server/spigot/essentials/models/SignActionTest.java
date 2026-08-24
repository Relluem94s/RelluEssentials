package de.relluem94.minecraft.server.spigot.essentials.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SignActionTest {

  @Test
  void shouldReturnCorrectValues() {
    String name = "teleport";
    boolean requiresInput = true;
    SignAction signAction = new SignAction(name, requiresInput);

    assertEquals(name, signAction.getName());
    assertEquals(name, signAction.getDisplayName());
    assertTrue(signAction.requiresCustomInput());
  }

  @Test
  void shouldReturnCorrectBrackets() {
    SignAction signAction = new SignAction("warp", false);

    assertEquals("[WARP]", signAction.getShorthandBracket());
    assertEquals("[WARP]", signAction.getNameBracket());
  }

  @Test
  void shouldHandleFalseRequiresCustomInput() {
    SignAction signAction = new SignAction("home", false);

    assertFalse(signAction.requiresCustomInput());
  }
}