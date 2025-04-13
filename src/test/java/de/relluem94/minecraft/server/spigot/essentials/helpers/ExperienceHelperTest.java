package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExperienceHelperTest {
    @Test
    public void testGetTotalExperience() {
        int test = ExperienceHelper.getTotalExperience(100);
        Assertions.assertEquals(30970, test);
    }
}
