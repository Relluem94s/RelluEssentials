package de.relluem94.minecraft.server.spigot.essentials.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RelluEssentialsMobSpawnEventTest {

  @Mock
  private CustomMob mockCustomMob;

  @Mock
  private CustomMobDefinition mockCustomMobDefinition;

  @Test
  void constructorShouldCorrectlyInitializeAllFields() {
    RelluEssentialsMobSpawnEvent event = new RelluEssentialsMobSpawnEvent(mockCustomMob, mockCustomMobDefinition);

    assertEquals(mockCustomMob, event.getCustomMob());
    assertEquals(mockCustomMobDefinition, event.getDefinition());
    assertFalse(event.isCancelled());
    assertNotNull(event.getHandlers());
  }

  @Test
  void setCancelledShouldSetCancelledToTrue() {
    RelluEssentialsMobSpawnEvent event = new RelluEssentialsMobSpawnEvent(mockCustomMob, mockCustomMobDefinition);

    event.setCancelled(true);

    assertTrue(event.isCancelled());
  }

  @Test
  void setCancelledShouldSetCancelledToFalse() {
    RelluEssentialsMobSpawnEvent event = new RelluEssentialsMobSpawnEvent(mockCustomMob, mockCustomMobDefinition);
    event.setCancelled(true);

    event.setCancelled(false);

    assertFalse(event.isCancelled());
  }

  @Test
  void getHandlerListShouldReturnNonNullHandlerList() {
    assertNotNull(RelluEssentialsMobSpawnEvent.getHandlerList());
  }

  @Test
  void getHandlersShouldReturnSameInstanceAsGetHandlerList() {
    RelluEssentialsMobSpawnEvent event = new RelluEssentialsMobSpawnEvent(mockCustomMob, mockCustomMobDefinition);

    assertEquals(RelluEssentialsMobSpawnEvent.getHandlerList(), event.getHandlers());
  }
}