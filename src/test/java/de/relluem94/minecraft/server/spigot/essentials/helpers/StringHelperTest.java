package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringHelperTest {

    @Test
    public void constructorThrowsIllegalStateException() throws Exception {
        Constructor<StringHelper> constructor = StringHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = Assertions.assertThrows(InvocationTargetException.class, constructor::newInstance);
        Assertions.assertInstanceOf(IllegalStateException.class, thrown.getCause());
        Assertions.assertEquals(Constants.PLUGIN_INTERNAL_UTILITY_CLASS, thrown.getCause().getMessage());
    }

    @Test
    public void replaceColorReplacesAmpersandWithSectionSign() {
        Assertions.assertEquals("§6Test", StringHelper.replaceColor("&6Test"));
    }

    @Test
    public void replaceColorReplacesMultipleAmpersands() {
        Assertions.assertEquals("§a§bTest§c", StringHelper.replaceColor("&a&bTest&c"));
    }

    @Test
    public void replaceColorWithNoAmpersandReturnsUnchanged() {
        Assertions.assertEquals("NoColor", StringHelper.replaceColor("NoColor"));
    }

    @Test
    public void firstCharToUpperCapitalizesFirstChar() {
        Assertions.assertEquals("Test", StringHelper.firstCharToUpper("test"));
    }

    @Test
    public void firstCharToUpperWithAlreadyUppercaseFirstChar() {
        Assertions.assertEquals("Test", StringHelper.firstCharToUpper("Test"));
    }

    @Test
    public void firstCharToUpperWithSingleChar() {
        Assertions.assertEquals("A", StringHelper.firstCharToUpper("a"));
    }

    @Test
    public void firstCharToUpperWithEmptyStringThrowsException() {
        Assertions.assertThrows(StringIndexOutOfBoundsException.class, () -> StringHelper.firstCharToUpper(""));
    }

    @ParameterizedTest
    @CsvSource({
        "12345678901, 12.345678901B, 12,345678901B",
        "1000000000,  1.0B,          1,0B",
        "2000000000,  2.0B,          2,0B",
        "1000000,     1.0M,          1,0M",
        "999999999,   999.999999M,   999,999999M",
        "1000,        1.0K,          1,0K",
        "999999,      999.999K,      999,999K",
        "999,         999,           999",
        "0,           0,             0",
        "1,           1,             1"
    })
    public void formatLongFormatsCorrectly(long input, String expectedDot, String expectedComma) {
        String result = StringHelper.formatLong(input);
        Assertions.assertTrue(result.equals(expectedDot) || result.equals(expectedComma),
            "Expected '" + expectedDot + "' or '" + expectedComma + "' but got '" + result + "'");
    }

    @ParameterizedTest
    @CsvSource({
        "1000000000, 1B",
        "1500000000, 1B",
        "1000000,    1M",
        "1500000,    1M",
        "999999999,  999M",
        "1000,       1K",
        "1500,       1K",
        "999999,     999K",
        "999,        999",
        "0,          0",
        "1,          1"
    })
    public void formatIntFormatsCorrectly(int input, String expected) {
        Assertions.assertEquals(expected, StringHelper.formatInt(input));
    }

    @ParameterizedTest
    @CsvSource({
        "1000000000.0,  1.00B,    '1,00B'",
        "1500000000.0,  1.50B,    '1,50B'",
        "1000000.0,     1.00M,    '1,00M'",
        "1500000.0,     1.50M,    '1,50M'",
        "999999999.0,   1000.00M, '1000,00M'",
        "1000.0,        1.00K,    '1,00K'",
        "1234.12,       1.23K,    '1,23K'",
        "12341.2,       12.34K,   '12,34K'",
        "999.99,        999.99,   '999,99'",
        "0.0,           0.00,     '0,00'",
        "1.5,           1.50,     '1,50'",
        "-999.99,       -999.99,  '-999,99'",
        "-1000.0,       -1.00K,   '-1,00K'",
        "-1500.0,       -1.50K,   '-1,50K'",
        "-1000000.0,    -1.00M,   '-1,00M'",
        "-1500000.0,    -1.50M,   '-1,50M'",
        "-1000000000.0, -1.00B,   '-1,00B'",
        "-1500000000.0, -1.50B,   '-1,50B'"
    })
    public void formatDoubleFormatsCorrectly(double input, String expectedDot, String expectedComma) {
        String result = StringHelper.formatDouble(input);
        Assertions.assertTrue(result.equals(expectedDot) || result.equals(expectedComma),
            "Expected '" + expectedDot + "' or '" + expectedComma + "' but got '" + result + "'");
    }
}