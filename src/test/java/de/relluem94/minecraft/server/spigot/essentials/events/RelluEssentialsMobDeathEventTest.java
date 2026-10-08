package de.relluem94.minecraft.server.spigot.essentials.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RelluEssentialsMobDeathEventTest {

  @Mock
  private CustomMob mockCustomMob;

  @Mock
  private CustomMobDefinition mockCustomMobDefinition;

  @Test
  void constructorShouldCorrectlyInitializeEventWithProvidedValues() {
    RelluEssentialsMobDeathEvent event = new RelluEssentialsMobDeathEvent(mockCustomMob, mockCustomMobDefinition);

    assertEquals(mockCustomMob, event.getCustomMob());
    assertEquals(mockCustomMobDefinition, event.getDefinition());
    assertNotNull(event.getHandlers());
  }

  @Test
  void getHandlerListShouldReturnNonNullHandlerList() {
    assertNotNull(RelluEssentialsMobDeathEvent.getHandlerList());
  }

}