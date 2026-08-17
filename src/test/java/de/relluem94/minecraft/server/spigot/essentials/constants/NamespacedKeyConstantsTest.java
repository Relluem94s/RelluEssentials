package de.relluem94.minecraft.server.spigot.essentials.constants;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NamespacedKeyConstantsTest {

    @Test
    void privateConstructorThrowsIllegalStateException() throws Exception {
        Constructor<NamespacedKeyConstants> constructor = NamespacedKeyConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrownException = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        assertInstanceOf(IllegalStateException.class, thrownException.getCause());
    }

    @Test
    void itemCoinsReturnsNamespacedKeyWithCoinsKey() {
        RelluEssentials pluginMock = Mockito.mock(RelluEssentials.class);
        Mockito.when(pluginMock.getName()).thenReturn("relluessentials");

        try (MockedStatic<RelluEssentials> staticMock = Mockito.mockStatic(RelluEssentials.class)) {
            staticMock.when(RelluEssentials::getInstance).thenReturn(pluginMock);

            NamespacedKey result = NamespacedKeyConstants.itemCoins();

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals("coins", result.getKey())
            );
        }
    }

    @Test
    void itemSellPriceReturnsNamespacedKeyWithSellPriceKey() {
        RelluEssentials pluginMock = Mockito.mock(RelluEssentials.class);
        Mockito.when(pluginMock.getName()).thenReturn("relluessentials");

        try (MockedStatic<RelluEssentials> staticMock = Mockito.mockStatic(RelluEssentials.class)) {
            staticMock.when(RelluEssentials::getInstance).thenReturn(pluginMock);

            NamespacedKey result = NamespacedKeyConstants.itemSellPrice();

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals("itemsellprice", result.getKey())
            );
        }
    }

    @Test
    void itemBuyPriceReturnsNamespacedKeyWithBuyPriceKey() {
        RelluEssentials pluginMock = Mockito.mock(RelluEssentials.class);
        Mockito.when(pluginMock.getName()).thenReturn("relluessentials");

        try (MockedStatic<RelluEssentials> staticMock = Mockito.mockStatic(RelluEssentials.class)) {
            staticMock.when(RelluEssentials::getInstance).thenReturn(pluginMock);

            NamespacedKey result = NamespacedKeyConstants.itemBuyPrice();

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals("itembuyprice", result.getKey())
            );
        }
    }

    @Test
    void itemCostReturnsNamespacedKeyWithItemCostKey() {
        RelluEssentials pluginMock = Mockito.mock(RelluEssentials.class);
        Mockito.when(pluginMock.getName()).thenReturn("relluessentials");

        try (MockedStatic<RelluEssentials> staticMock = Mockito.mockStatic(RelluEssentials.class)) {
            staticMock.when(RelluEssentials::getInstance).thenReturn(pluginMock);

            NamespacedKey result = NamespacedKeyConstants.itemCost();

            assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("item_cost", result.getKey())
            );
        }
    }
}