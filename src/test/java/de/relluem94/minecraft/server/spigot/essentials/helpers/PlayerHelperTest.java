package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerHelperTest {

    @Mock
    private World world;

    @Mock
    private Player playerOne;

    @Mock
    private Player playerTwo;

    private Location locationWithDirection(double x, double y, double z) {
        Location location = mock(Location.class);
        Vector direction = new Vector(x, y, z);
        when(location.getDirection()).thenReturn(direction);
        return location;
    }

    @Test
    void getLocationDirectionWhenFacingSouthReturnsPositiveZ() {
        Location location = locationWithDirection(0, 0, 1);
        assertEquals(new Vector(0, 0, 1), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenFacingNorthReturnsNegativeZ() {
        Location location = locationWithDirection(0, 0, -1);
        assertEquals(new Vector(0, 0, -1), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenFacingWestReturnsNegativeX() {
        Location location = locationWithDirection(-1, 0, 0);
        assertEquals(new Vector(-1, 0, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenFacingEastReturnsPositiveX() {
        Location location = locationWithDirection(1, 0, 0);
        assertEquals(new Vector(1, 0, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenLookingSteeplyUpReturnsUpwardVector() {
        Location location = locationWithDirection(0, 1, 0);
        assertEquals(new Vector(0, 1, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenLookingSteeplyDownReturnsDownwardVector() {
        Location location = locationWithDirection(0, -1, 0);
        assertEquals(new Vector(0, -1, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenVerticalComponentExactlyAtThresholdTreatsAsHorizontal() {
        Location location = locationWithDirection(0, 0.5, 1);
        Vector result = PlayerHelper.getLocationDirection(location);
        assertNotEquals(new Vector(0, 1, 0), result);
        assertNotEquals(new Vector(0, -1, 0), result);
    }

    @Test
    void getLocationDirectionWhenVerticalComponentJustAboveThresholdReturnsVerticalVector() {
        Location location = locationWithDirection(0, 0.51, 0.1);
        assertEquals(new Vector(0, 1, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenYawAtNorthEastBoundaryReturnsSouth() {
        Location location = locationWithDirection(0.707, 0, 0.707);
        assertEquals(new Vector(0, 0, 1), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenYawAtSouthWestBoundaryReturnsWest() {
        Location location = locationWithDirection(-0.707, 0, 0.707);
        assertEquals(new Vector(-1, 0, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenYawAtSouthEastBoundaryReturnsEast() {
        Location location = locationWithDirection(0.707, 0, -0.707);
        assertEquals(new Vector(1, 0, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirectionWhenYawAtNorthWestBoundaryReturnsNorth() {
        Location location = locationWithDirection(-0.707, 0, -0.707);
        assertEquals(new Vector(0, 0, -1), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getPlayerDirectionDelegatesToPlayerLocation() {
        Location southLocation = locationWithDirection(0, 0, 1);
        when(playerOne.getLocation()).thenReturn(southLocation);

        Vector result = PlayerHelper.getPlayerDirection(playerOne);

        assertEquals(new Vector(0, 0, 1), result);
        verify(playerOne).getLocation();
    }

    @Test
    void getPlayerDirectionWhenPlayerFacingNorthReturnsNegativeZ() {
        Location northLocation = locationWithDirection(0, 0, -1);
        when(playerOne.getLocation()).thenReturn(northLocation);

        assertEquals(new Vector(0, 0, -1), PlayerHelper.getPlayerDirection(playerOne));
    }

    @Test
    void getPlayerDirectionWhenPlayerFacingUpReturnsUpwardVector() {
        Location upLocation = locationWithDirection(0, 1, 0);
        when(playerOne.getLocation()).thenReturn(upLocation);

        assertEquals(new Vector(0, 1, 0), PlayerHelper.getPlayerDirection(playerOne));
    }

    @Test
    void getTargetedPlayerWhenWorldIsNullReturnsNull() {
        Location location = mock(Location.class);
        when(location.getWorld()).thenReturn(null);

        assertNull(PlayerHelper.getTargetedPlayer(location));
    }

    @Test
    void getTargetedPlayerWhenOnePlayerInWorldReturnsThatPlayer() {
        Location sourceLocation = mock(Location.class);
        Location playerLocation = mock(Location.class);

        when(sourceLocation.getWorld()).thenReturn(world);
        when(world.getPlayers()).thenReturn(List.of(playerOne));
        when(playerOne.getLocation()).thenReturn(playerLocation);
        when(sourceLocation.distanceSquared(playerLocation)).thenReturn(4.0);

        assertEquals(playerOne, PlayerHelper.getTargetedPlayer(sourceLocation));
    }

    @Test
    void getTargetedPlayerWhenMultiplePlayersReturnsNearestPlayer() {
        Location sourceLocation = mock(Location.class);
        Location nearLocation = mock(Location.class);
        Location farLocation = mock(Location.class);

        when(sourceLocation.getWorld()).thenReturn(world);
        when(world.getPlayers()).thenReturn(List.of(playerOne, playerTwo));
        when(playerOne.getLocation()).thenReturn(nearLocation);
        when(playerTwo.getLocation()).thenReturn(farLocation);
        when(sourceLocation.distanceSquared(nearLocation)).thenReturn(2.0);
        when(sourceLocation.distanceSquared(farLocation)).thenReturn(10.0);

        assertEquals(playerOne, PlayerHelper.getTargetedPlayer(sourceLocation));
    }

    @Test
    void getTargetedPlayerWhenNoPlayersInWorldReturnsNull() {
        Location location = mock(Location.class);
        when(location.getWorld()).thenReturn(world);
        when(world.getPlayers()).thenReturn(List.of());

        assertNull(PlayerHelper.getTargetedPlayer(location));
    }

    @Test
    void constructorThrowsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> {
            var constructor = PlayerHelper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }
}