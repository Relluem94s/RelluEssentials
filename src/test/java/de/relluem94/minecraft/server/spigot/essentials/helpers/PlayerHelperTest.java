package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void getLocationDirection_whenFacingSouth_returnsPositiveZ() {
        Location location = locationWithDirection(0, 0, 1);
        assertEquals(new Vector(0, 0, 1), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenFacingNorth_returnsNegativeZ() {
        Location location = locationWithDirection(0, 0, -1);
        assertEquals(new Vector(0, 0, -1), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenFacingWest_returnsNegativeX() {
        Location location = locationWithDirection(-1, 0, 0);
        assertEquals(new Vector(-1, 0, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenFacingEast_returnsPositiveX() {
        Location location = locationWithDirection(1, 0, 0);
        assertEquals(new Vector(1, 0, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenLookingSteeplyUp_returnsUpwardVector() {
        Location location = locationWithDirection(0, 1, 0);
        assertEquals(new Vector(0, 1, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenLookingSteeplyDown_returnsDownwardVector() {
        Location location = locationWithDirection(0, -1, 0);
        assertEquals(new Vector(0, -1, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenVerticalComponentExactlyAtThreshold_treatsAsHorizontal() {
        Location location = locationWithDirection(0, 0.5, 1);
        Vector result = PlayerHelper.getLocationDirection(location);
        assertNotEquals(new Vector(0, 1, 0), result);
        assertNotEquals(new Vector(0, -1, 0), result);
    }

    @Test
    void getLocationDirection_whenVerticalComponentJustAboveThreshold_returnsVerticalVector() {
        Location location = locationWithDirection(0, 0.51, 0.1);
        assertEquals(new Vector(0, 1, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenYawAtNorthEastBoundary_returnsSouth() {
        Location location = locationWithDirection(0.707, 0, 0.707);
        assertEquals(new Vector(0, 0, 1), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenYawAtSouthWestBoundary_returnsWest() {
        Location location = locationWithDirection(-0.707, 0, 0.707);
        assertEquals(new Vector(-1, 0, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenYawAtSouthEastBoundary_returnsEast() {
        Location location = locationWithDirection(0.707, 0, -0.707);
        assertEquals(new Vector(1, 0, 0), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getLocationDirection_whenYawAtNorthWestBoundary_returnsNorth() {
        Location location = locationWithDirection(-0.707, 0, -0.707);
        assertEquals(new Vector(0, 0, -1), PlayerHelper.getLocationDirection(location));
    }

    @Test
    void getPlayerDirection_delegatesToPlayerLocation() {
        Location southLocation = locationWithDirection(0, 0, 1);
        when(playerOne.getLocation()).thenReturn(southLocation);

        Vector result = PlayerHelper.getPlayerDirection(playerOne);

        assertEquals(new Vector(0, 0, 1), result);
        verify(playerOne).getLocation();
    }

    @Test
    void getPlayerDirection_whenPlayerFacingNorth_returnsNegativeZ() {
        Location northLocation = locationWithDirection(0, 0, -1);
        when(playerOne.getLocation()).thenReturn(northLocation);

        assertEquals(new Vector(0, 0, -1), PlayerHelper.getPlayerDirection(playerOne));
    }

    @Test
    void getPlayerDirection_whenPlayerFacingUp_returnsUpwardVector() {
        Location upLocation = locationWithDirection(0, 1, 0);
        when(playerOne.getLocation()).thenReturn(upLocation);

        assertEquals(new Vector(0, 1, 0), PlayerHelper.getPlayerDirection(playerOne));
    }

    @Test
    void getTargetedPlayer_whenWorldIsNull_returnsNull() {
        Location location = mock(Location.class);
        when(location.getWorld()).thenReturn(null);

        assertNull(PlayerHelper.getTargetedPlayer(location));
    }

    @Test
    void getTargetedPlayer_whenOnePlayerInWorld_returnsThatPlayer() {
        Location sourceLocation = mock(Location.class);
        Location playerLocation = mock(Location.class);

        when(sourceLocation.getWorld()).thenReturn(world);
        when(world.getPlayers()).thenReturn(List.of(playerOne));
        when(playerOne.getLocation()).thenReturn(playerLocation);
        when(sourceLocation.distanceSquared(playerLocation)).thenReturn(4.0);

        assertEquals(playerOne, PlayerHelper.getTargetedPlayer(sourceLocation));
    }

    @Test
    void getTargetedPlayer_whenMultiplePlayers_returnsNearestPlayer() {
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
    void getTargetedPlayer_whenNoPlayersInWorld_returnsNull() {
        Location location = mock(Location.class);
        when(location.getWorld()).thenReturn(world);
        when(world.getPlayers()).thenReturn(List.of());

        assertNull(PlayerHelper.getTargetedPlayer(location));
    }

    @Test
    void constructor_throwsIllegalStateException() {
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