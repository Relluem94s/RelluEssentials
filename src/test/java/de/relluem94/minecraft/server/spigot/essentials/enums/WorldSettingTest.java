package de.relluem94.minecraft.server.spigot.essentials.enums;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WorldSettingTest {

  @Test
  void allEnumValuesShouldBePresent() {
    WorldSetting[] settings = WorldSetting.values();
    assertTrue(settings.length > 0);

    for (WorldSetting setting : settings) {
      assertNotNull(setting);
    }
  }
}