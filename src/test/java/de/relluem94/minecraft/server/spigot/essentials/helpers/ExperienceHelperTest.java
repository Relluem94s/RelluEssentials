package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExperienceHelperTest {

    @Mock
    private Player player;

    @Test
    public void constructorThrowsIllegalStateException() throws Exception {
        Constructor<ExperienceHelper> constructor = ExperienceHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrownException = Assertions.assertThrows(InvocationTargetException.class, constructor::newInstance);
        Assertions.assertInstanceOf(IllegalStateException.class, thrownException.getCause());
    }

    @Test
    public void constructorIllegalStateExceptionMessageMatchesConstant() throws Exception {
        Constructor<ExperienceHelper> constructor = ExperienceHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrownException = Assertions.assertThrows(InvocationTargetException.class, constructor::newInstance);
        Assertions.assertEquals(Constants.PLUGIN_INTERNAL_UTILITY_CLASS, thrownException.getCause().getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "1, 7",
        "7, 91",
        "15, 315",
        "16, 352",
        "20, 550",
        "30, 1395",
        "31, 1507",
        "50, 5345",
        "100, 30970"
    })
    public void getTotalExperienceByLevelReturnsCorrectXp(int level, int expectedXp) {
        int result = ExperienceHelper.getTotalExperience(level);
        Assertions.assertEquals(expectedXp, result);
    }

    @Test
    public void getTotalExperienceByLevelReturnsZeroForNegativeLevel() {
        int result = ExperienceHelper.getTotalExperience(-1);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void getTotalExperienceByLevelReturnsZeroForLevelZero() {
        int result = ExperienceHelper.getTotalExperience(0);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void getTotalExperienceByPlayerReturnsCorrectXpForLevelTierOne() {
        Mockito.when(player.getLevel()).thenReturn(7);
        Mockito.when(player.getExpToLevel()).thenReturn(22);
        Mockito.when(player.getExp()).thenReturn(0.5f);

        int result = ExperienceHelper.getTotalExperience(player);

        Assertions.assertEquals(91 + 11, result);
    }

    @Test
    public void getTotalExperienceByPlayerReturnsCorrectXpForLevelTierTwo() {
        Mockito.when(player.getLevel()).thenReturn(20);
        Mockito.when(player.getExpToLevel()).thenReturn(47);
        Mockito.when(player.getExp()).thenReturn(0.0f);

        int result = ExperienceHelper.getTotalExperience(player);

        Assertions.assertEquals(550, result);
    }

    @Test
    public void getTotalExperienceByPlayerReturnsCorrectXpForLevelTierThree() {
        Mockito.when(player.getLevel()).thenReturn(31);
        Mockito.when(player.getExpToLevel()).thenReturn(116);
        Mockito.when(player.getExp()).thenReturn(0.0f);

        int result = ExperienceHelper.getTotalExperience(player);

        Assertions.assertEquals(1507, result);
    }

    @Test
    public void setTotalExperienceSetsCorrectLevelForTierOne() {
        ExperienceHelper.setTotalExperience(player, 91);

        Mockito.verify(player).setLevel(7);
    }

    @Test
    public void setTotalExperienceSetsCorrectExpForTierOne() {
        ExperienceHelper.setTotalExperience(player, 91);

        Mockito.verify(player).setExp(0);
        Mockito.verify(player).giveExp(0);
    }

    @Test
    public void setTotalExperienceSetsCorrectLevelForTierTwo() {
        ExperienceHelper.setTotalExperience(player, 550);

        Mockito.verify(player).setLevel(20);
    }

    @Test
    public void setTotalExperienceSetsCorrectExpForTierTwo() {
        ExperienceHelper.setTotalExperience(player, 550);

        Mockito.verify(player).setExp(0);
        Mockito.verify(player).giveExp(0);
    }

    @Test
    public void setTotalExperienceSetsCorrectLevelForTierThree() {
        ExperienceHelper.setTotalExperience(player, 1458);

        Mockito.verify(player).setLevel(30);
    }

    @Test
    public void setTotalExperienceSetsCorrectExpForTierThree() {
        ExperienceHelper.setTotalExperience(player, 1458);

        Mockito.verify(player).setExp(0);
        Mockito.verify(player).giveExp(1458 - ExperienceHelper.getTotalExperience(30));
    }

    @Test
    public void setTotalExperienceSetsLevelZeroForZeroAmount() {
        ExperienceHelper.setTotalExperience(player, 0);

        Mockito.verify(player).setLevel(0);
        Mockito.verify(player).setExp(0);
        Mockito.verify(player).giveExp(0);
    }

    @Test
    public void setTotalExperienceGrantsRemainingXpWithinLevel() {
        ExperienceHelper.setTotalExperience(player, 100);

        Mockito.verify(player).setLevel(7);
        Mockito.verify(player).setExp(0);
        Mockito.verify(player).giveExp(100 - ExperienceHelper.getTotalExperience(7));
    }

    @Test
    public void setTotalExperienceGrantsRemainingXpWithinTierTwoLevel() {
        ExperienceHelper.setTotalExperience(player, 600);

        Mockito.verify(player).setLevel(20);
        Mockito.verify(player).setExp(0);
        Mockito.verify(player).giveExp(600 - ExperienceHelper.getTotalExperience(20));
    }

    @Test
    public void setTotalExperienceGrantsRemainingXpWithinTierThreeLevel() {
        ExperienceHelper.setTotalExperience(player, 1500);

        Mockito.verify(player).setLevel(30);
        Mockito.verify(player).setExp(0);
        Mockito.verify(player).giveExp(1500 - ExperienceHelper.getTotalExperience(30));
    }
}