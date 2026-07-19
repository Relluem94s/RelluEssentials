package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyClipboardEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ModifyHelperTest {

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.0",
            "90.0, 90.0",
            "180.0, 180.0",
            "270.0, 270.0",
            "360.0, 0.0",
            "45.0, 90.0",
            "44.0, 0.0",
            "-90.0, 270.0",
            "-180.0, 180.0",
            "450.0, 90.0",
            "720.0, 0.0",
    })
    void normalizeYaw_returnsNearestCardinalDirection(float input, float expected) {
        assertEquals(expected, ModifyHelper.normalizeYaw(input));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 0,   1,  0",
            "1, 0, 90,  0,  1",
            "1, 0, 180, -1, 0",
            "1, 0, 270, 0, -1",
            "0, 1, 0,   0,  1",
            "0, 1, 90, -1,  0",
            "0, 1, 180, 0, -1",
            "0, 1, 270, 1,  0",
    })
    void worldToLocal_transformsCoordinatesBasedOnYaw(int dx, int dz, float yaw, int expectedX, int expectedZ) {
        int[] result = ModifyHelper.worldToLocal(dx, dz, yaw);

        assertEquals(expectedX, result[0]);
        assertEquals(expectedZ, result[1]);
    }

    @Test
    void worldToLocal_returnsArrayOfLengthTwo() {
        int[] result = ModifyHelper.worldToLocal(1, 1, 0);
        assertEquals(2, result.length);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 0,   1,  0",
            "1, 0, 90,  0,  1",
            "1, 0, 180, -1, 0",
            "1, 0, 270, 0, -1",
            "0, 1, 0,   0,  1",
            "0, 1, 90, -1,  0",
            "0, 1, 180, 0, -1",
            "0, 1, 270, 1,  0",
    })
    void relativeToWorld_transformsCoordinatesBasedOnYaw(int relX, int relZ, float yaw, int expectedX, int expectedZ) {
        int[] result = ModifyHelper.relativeToWorld(relX, relZ, yaw);

        assertEquals(expectedX, result[0]);
        assertEquals(expectedZ, result[1]);
    }

    @Test
    void relativeToWorld_returnsArrayOfLengthTwo() {
        int[] result = ModifyHelper.relativeToWorld(1, 1, 0);
        assertEquals(2, result.length);
    }

    @Test
    void rotate_returnsRotatedEntriesWithCorrectCoordinates() {
        World world = mock(World.class);

        BlockData blockData = mock(BlockData.class);
        Location locationAt1_0 = new Location(world, 1, 0, 0);
        Location locationAt0_0 = new Location(world, 0, 0, 1);

        List<ModifyClipboardEntry> entries = List.of(
                new ModifyClipboardEntry(locationAt1_0, Material.STONE, blockData),
                new ModifyClipboardEntry(locationAt0_0, Material.DIRT, blockData)
        );

        Location selectionPos1 = new Location(world, 0, 0, 0);
        Location selectionPos2 = new Location(world, 1, 5, 1);
        Selection selection = new Selection(selectionPos1, selectionPos2);

        DoubleStore<Selection, List<ModifyClipboardEntry>> result = ModifyHelper.rotate(entries, selection);

        assertNotNull(result);
        assertNotNull(result.getValue());
        assertNotNull(result.getSecondValue());
        assertEquals(entries.size(), result.getSecondValue().size());
    }

    @Test
    void rotate_preservesYBounds() {
        World world = mock(World.class);
        BlockData blockData = mock(BlockData.class);

        List<ModifyClipboardEntry> entries = List.of(
                new ModifyClipboardEntry(new Location(world, 1, 2, 3), Material.STONE, blockData)
        );

        Location selectionPos1 = new Location(world, 0, 10, 0);
        Location selectionPos2 = new Location(world, 5, 20, 5);
        Selection selection = new Selection(selectionPos1, selectionPos2);

        DoubleStore<Selection, List<ModifyClipboardEntry>> result = ModifyHelper.rotate(entries, selection);

        assertEquals(selection.getMinY(), result.getValue().getMinY());
        assertEquals(selection.getMaxY(), result.getValue().getMaxY());
    }

    @Test
    void rotate_rotatesCoordinatesCorrectly() {
        World world = mock(World.class);
        BlockData blockData = mock(BlockData.class);

        Location originalLocation = new Location(world, 2, 0, 3);
        List<ModifyClipboardEntry> entries = List.of(
                new ModifyClipboardEntry(originalLocation, Material.STONE, blockData)
        );

        Location selectionPos1 = new Location(world, 0, 0, 0);
        Location selectionPos2 = new Location(world, 3, 5, 2);
        Selection selection = new Selection(selectionPos1, selectionPos2);

        DoubleStore<Selection, List<ModifyClipboardEntry>> result = ModifyHelper.rotate(entries, selection);

        ModifyClipboardEntry rotatedEntry = result.getSecondValue().getFirst();
        assertEquals(0, rotatedEntry.getLocation().getBlockX());
        assertEquals(originalLocation.getBlockY(), rotatedEntry.getLocation().getBlockY());
        assertEquals(1, rotatedEntry.getLocation().getBlockZ());
    }
    @Test
    void rotate_preservesMaterialAndBlockData() {
        World world = mock(World.class);
        BlockData blockData = mock(BlockData.class);

        List<ModifyClipboardEntry> entries = List.of(
                new ModifyClipboardEntry(new Location(world, 1, 0, 0), Material.GOLD_BLOCK, blockData)
        );

        Location selectionPos1 = new Location(world, 0, 0, 0);
        Location selectionPos2 = new Location(world, 1, 5, 1);
        Selection selection = new Selection(selectionPos1, selectionPos2);

        DoubleStore<Selection, List<ModifyClipboardEntry>> result = ModifyHelper.rotate(entries, selection);

        ModifyClipboardEntry rotatedEntry = result.getSecondValue().getFirst();
        assertEquals(Material.GOLD_BLOCK, rotatedEntry.getMaterial());
        assertEquals(blockData, rotatedEntry.getData());
    }

    @Test
    void rotate_withEmptyEntries_returnsEmptyList() {
        World world = mock(World.class);

        Location selectionPos1 = new Location(world, 0, 0, 0);
        Location selectionPos2 = new Location(world, 5, 5, 5);
        Selection selection = new Selection(selectionPos1, selectionPos2);

        DoubleStore<Selection, List<ModifyClipboardEntry>> result = ModifyHelper.rotate(List.of(), selection);

        assertTrue(result.getSecondValue().isEmpty());
    }

    @Test
    void constructor_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> {
            var constructor = ModifyHelper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }
}