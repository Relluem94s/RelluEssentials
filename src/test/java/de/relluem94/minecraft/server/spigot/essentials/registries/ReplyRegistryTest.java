package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReplyRegistryTest {

  private ReplyRegistry replyRegistry;
  private Player playerA;
  private Player playerB;
  private Player playerC;

  @BeforeEach
  void setUp() {
    replyRegistry = new ReplyRegistry();
    playerA = mock(Player.class);
    playerB = mock(Player.class);
    playerC = mock(Player.class);
  }

  @Test
  void registerShouldCreateBidirectionalRelationship() {
    replyRegistry.register(playerA, playerB);

    assertTrue(replyRegistry.hasReplyTarget(playerA));
    assertTrue(replyRegistry.hasReplyTarget(playerB));
    assertEquals(playerB, replyRegistry.findReplyTarget(playerA));
    assertEquals(playerA, replyRegistry.findReplyTarget(playerB));
  }

  @Test
  void registerShouldOverwriteExistingRelationships() {
    replyRegistry.register(playerA, playerB);
    replyRegistry.register(playerA, playerC);

    assertEquals(playerC, replyRegistry.findReplyTarget(playerA));
    assertEquals(playerA, replyRegistry.findReplyTarget(playerC));
    assertFalse(replyRegistry.hasReplyTarget(playerB), "Player B should have been removed from the relationship");
    assertEquals(null, replyRegistry.findReplyTarget(playerB));
  }

  @Test
  void findReplyTargetShouldReturnNullIfNoRelationship() {
    assertFalse(replyRegistry.hasReplyTarget(playerA));
    assertNull(replyRegistry.findReplyTarget(playerA));
  }

  @Test
  void unregisterShouldRemoveRelationship() {
    replyRegistry.register(playerA, playerB);
    replyRegistry.unregister(playerA);

    assertFalse(replyRegistry.hasReplyTarget(playerA));
    assertNull(replyRegistry.findReplyTarget(playerA));
    assertFalse(replyRegistry.hasReplyTarget(playerB));
  }
}