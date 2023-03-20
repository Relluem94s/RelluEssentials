package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.junit.Assert;
import org.junit.Test;

public class ExperienceHelperTest {
    @Test
    public void testGetTotalExperience() {
        int test = ExperienceHelper.getTotalExperience(100);
        Assert.assertEquals(30970, test);
    }
}
