package de.relluem94.minecraft.server.spigot.essentials.enums;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LocationTypeTest {

  @Test
  void allEnumValuesShouldBePresent() {
    LocationType[] settings = LocationType.values();
    assertTrue(settings.length > 0);

    for (LocationType setting : settings) {
      assertNotNull(setting);
    }
  }
}
