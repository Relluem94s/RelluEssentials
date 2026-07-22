package de.relluem94.minecraft.server.spigot.essentials.constants;

import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class EntityCoinsTest {

    @Test
    void allEntityCoinsHaveNonNegativeCoins() {
        for (EntityCoins entityCoins : EntityCoins.values()) {
            assertTrue(entityCoins.getCoins() >= 0);
        }
    }

    @ParameterizedTest
    @EnumSource(EntityCoins.class)
    void eachEntityCoinHasNonNegativeCoins(EntityCoins entityCoins) {
        assertTrue(entityCoins.getCoins() >= 0);
    }

    @Test
    void fromReturnsUnknownForUnmappedEntityType() {
        EntityCoins result = EntityCoins.from(EntityType.UNKNOWN);
        assertEquals(EntityCoins.UNKNOWN, result);
    }

    @Test
    void fromReturnsCorrectEntityCoinForKnownEntityType() {
        EntityCoins result = EntityCoins.from(EntityType.CREEPER);
        assertEquals(EntityCoins.CREEPER, result);
        assertEquals(25, result.getCoins());
    }

    @Test
    void unknownEntityCoinHasZeroCoins() {
        assertEquals(0, EntityCoins.UNKNOWN.getCoins());
    }

    @Test
    void highValueEntitiesHaveExpectedCoins() {
        assertEquals(500000, EntityCoins.ENDER_DRAGON.getCoins());
        assertEquals(250000, EntityCoins.WITHER.getCoins());
        assertEquals(25000, EntityCoins.WARDEN.getCoins());
    }

    @Test
    void passiveMobsHaveZeroCoins() {
        assertEquals(0, EntityCoins.COW.getCoins());
        assertEquals(0, EntityCoins.PIG.getCoins());
        assertEquals(0, EntityCoins.CHICKEN.getCoins());
    }

    @Test
    void allEntityTypesAreMappable() {
        for (EntityType entityType : EntityType.values()) {
            EntityCoins result = EntityCoins.from(entityType);
            assertNotNull(result);
        }
    }
}