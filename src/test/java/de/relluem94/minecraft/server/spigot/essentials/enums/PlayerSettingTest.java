package de.relluem94.minecraft.server.spigot.essentials.enums;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PlayerSettingTest {

  @Test
  void allEnumValuesShouldBePresent() {
    PlayerSetting[] settings = PlayerSetting.values();
    assertTrue(settings.length > 0);

    for (PlayerSetting setting : settings) {
      assertNotNull(setting);
    }
  }
}
