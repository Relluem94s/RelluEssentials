package de.relluem94.minecraft.server.spigot.essentials.constants;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomHeadsTest {

    @ParameterizedTest
    @EnumSource(CustomHeads.class)
    void nameIsNotBlank(CustomHeads head) {
        assertFalse(head.getName().isBlank());
    }

    @ParameterizedTest
    @EnumSource(CustomHeads.class)
    void base64IsNotBlank(CustomHeads head) {
        assertFalse(head.getBase64().isBlank());
    }

    @ParameterizedTest
    @EnumSource(CustomHeads.class)
    void base64IsValidBase64(CustomHeads head) {
        assertDoesNotThrow(() -> Base64.getDecoder().decode(head.getBase64()));
    }

    @ParameterizedTest
    @EnumSource(CustomHeads.class)
    void uuidIsValidUUID(CustomHeads head) {
        assertDoesNotThrow(() -> {
            UUID uuid = head.getUUID();
            assertNotNull(uuid);
        });
    }

    @ParameterizedTest
    @EnumSource(CustomHeads.class)
    void uuidReturnsSameValueOnMultipleCalls(CustomHeads head) {
        assertEquals(head.getUUID(), head.getUUID());
    }

    @Test
    void allNamesAreUnique() {
        List<String> names = Arrays.stream(CustomHeads.values())
                .map(CustomHeads::getName)
                .toList();

        long uniqueCount = names.stream().distinct().count();
        assertEquals(names.size(), uniqueCount);
    }

    @Test
    void enumContainsExpectedEntries() {
        assertNotNull(CustomHeads.valueOf("WHITE_CANDLE"));
        assertNotNull(CustomHeads.valueOf("CYAN_CANDLE"));
        assertNotNull(CustomHeads.valueOf("RED_CANDLE"));
        assertNotNull(CustomHeads.valueOf("BLUE_CANDLE"));
        assertNotNull(CustomHeads.valueOf("GRAY_CANDLE"));
        assertNotNull(CustomHeads.valueOf("LIME_CANDLE"));
        assertNotNull(CustomHeads.valueOf("MAGENTA_CANDLE"));
        assertNotNull(CustomHeads.valueOf("PINK_CANDLE"));
        assertNotNull(CustomHeads.valueOf("BLACK_CANDLE"));
        assertNotNull(CustomHeads.valueOf("GREEN_CANDLE"));
        assertNotNull(CustomHeads.valueOf("ORANGE_CANDLE"));
        assertNotNull(CustomHeads.valueOf("BROWN_CANDLE"));
        assertNotNull(CustomHeads.valueOf("LIGHT_BLUE_CANDLE"));
        assertNotNull(CustomHeads.valueOf("LIGHT_GRAY_CANDLE"));
        assertNotNull(CustomHeads.valueOf("BOOK1"));
        assertNotNull(CustomHeads.valueOf("BOOKS1"));
        assertNotNull(CustomHeads.valueOf("BOOKS2"));
        assertNotNull(CustomHeads.valueOf("MONEY_BAG"));
        assertNotNull(CustomHeads.valueOf("COMPUTER"));
        assertNotNull(CustomHeads.valueOf("ASTRONAUT_HELMET"));
        assertNotNull(CustomHeads.valueOf("DARK_OAK_LOG"));
        assertNotNull(CustomHeads.valueOf("PRISMARINE"));
        assertNotNull(CustomHeads.valueOf("QUARTZ"));
        assertNotNull(CustomHeads.valueOf("BUSH"));
        assertNotNull(CustomHeads.valueOf("DIAMOND_ORE"));
        assertNotNull(CustomHeads.valueOf("PUMPKIN"));
        assertNotNull(CustomHeads.valueOf("OAK_WOOD_PLANKS"));
        assertNotNull(CustomHeads.valueOf("SHARK"));
        assertNotNull(CustomHeads.valueOf("SKULL1"));
        assertNotNull(CustomHeads.valueOf("SKULL2"));
        assertNotNull(CustomHeads.valueOf("ENDERMANN"));
        assertNotNull(CustomHeads.valueOf("LUCKY_BLOCK"));
        assertNotNull(CustomHeads.valueOf("GREEN_ORB"));
        assertNotNull(CustomHeads.valueOf("GOLDEN_CHEST"));
        assertNotNull(CustomHeads.valueOf("BREAD"));
        assertNotNull(CustomHeads.valueOf("TELEVISON"));
        assertNotNull(CustomHeads.valueOf("GUARDIAN"));
        assertNotNull(CustomHeads.valueOf("ROBOT"));
        assertNotNull(CustomHeads.valueOf("HAMBURGER"));
        assertNotNull(CustomHeads.valueOf("CUPCAKE"));
        assertNotNull(CustomHeads.valueOf("COMPANION_CUBE"));
    }

    @Test
    void booksOneHasCorrectName() {
        assertEquals("GoodBook1", CustomHeads.BOOKS1.getName());
    }

    @Test
    void globeHasCorrectUUID() {
        assertEquals(UUID.fromString("bd287f02-7b3b-ffd9-c56c-99cb0fafab3b"), CustomHeads.GLOBE.getUUID());
    }

    @Test
    void woodenHouseUUIDChangesOnRestart() {
        assertNotNull(CustomHeads.WOODEN_HOUSE.getUUID());
    }
}