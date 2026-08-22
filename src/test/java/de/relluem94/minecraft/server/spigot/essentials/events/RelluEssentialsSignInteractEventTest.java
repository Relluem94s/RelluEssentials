package de.relluem94.minecraft.server.spigot.essentials.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.SignAction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RelluEssentialsSignInteractEventTest {

  @Mock
  private Player mockPlayer;

  @Mock
  private Block mockBlock;

  @Mock
  private RelluEssentialsNamespacedKey mockRelluEssentialsNamespacedKey;

  @Test
  void shouldCorrectlyInitializeEventWithProvidedValues() {
    SignAction signAction = new SignAction("test", true);
    String customInput = "test-input";

    RelluEssentialsSignInteractEvent event = new RelluEssentialsSignInteractEvent(
        mockPlayer,
        mockBlock,
        mockRelluEssentialsNamespacedKey,
        signAction,
        customInput
    );

    assertEquals(mockPlayer, event.getPlayer());
    assertEquals(mockBlock, event.getClickedBlock());
    assertEquals(mockRelluEssentialsNamespacedKey, event.getActionKey());
    assertEquals(signAction, event.getSignAction());
    assertEquals(customInput, event.getCustomInput());
    assertNotNull(event.getHandlers());
  }

  @Test
  void shouldReturnCorrectHandlerList() {
    assertNotNull(RelluEssentialsSignInteractEvent.getHandlerList());
  }

}