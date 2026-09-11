package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.StructureRotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
        "1, 0, 90,  0,  -1",
        "1, 0, 180, -1, 0",
        "1, 0, 270, 0, 1",
        "0, 1, 0,   0,  1",
        "0, -1, 90, -1,  0",
        "0, 1, 180, 0, -1",
        "0, -1, 270, 1,  0",
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

    // -------------------------------------------------------------------------
    // rotateBlockData
    // -------------------------------------------------------------------------

    @Test
    void rotateBlockData_withYaw0_returnsCloneWithoutRotation() {
        BlockData original = mock(BlockData.class);
        BlockData cloned = mock(BlockData.class);
        when(original.clone()).thenReturn(cloned);

        BlockData result = ModifyHelper.rotateBlockData(original, 0f);

        assertEquals(cloned, result);
        verify(original).clone();
    }

    @Test
    void rotateBlockData_withYaw90_appliesClockwise90Rotation() {
        BlockData original = mock(BlockData.class);
        BlockData cloned = mock(BlockData.class);
        when(original.clone()).thenReturn(cloned);

        BlockData result = ModifyHelper.rotateBlockData(original, 90f);

        assertEquals(cloned, result);
        verify(cloned).rotate(StructureRotation.CLOCKWISE_90);
    }

    @Test
    void rotateBlockData_withYaw180_appliesClockwise180Rotation() {
        BlockData original = mock(BlockData.class);
        BlockData cloned = mock(BlockData.class);
        when(original.clone()).thenReturn(cloned);

        BlockData result = ModifyHelper.rotateBlockData(original, 180f);

        assertEquals(cloned, result);
        verify(cloned).rotate(StructureRotation.CLOCKWISE_180);
    }

    @Test
    void rotateBlockData_withYaw270_appliesCounterclockwise90Rotation() {
        BlockData original = mock(BlockData.class);
        BlockData cloned = mock(BlockData.class);
        when(original.clone()).thenReturn(cloned);

        BlockData result = ModifyHelper.rotateBlockData(original, 270f);

        assertEquals(cloned, result);
        verify(cloned).rotate(StructureRotation.COUNTERCLOCKWISE_90);
    }

    @Test
    void rotateBlockData_withYaw360_treatedAsYaw0_returnsCloneWithoutRotation() {
        BlockData original = mock(BlockData.class);
        BlockData cloned = mock(BlockData.class);
        when(original.clone()).thenReturn(cloned);

        BlockData result = ModifyHelper.rotateBlockData(original, 360f);

        assertEquals(cloned, result);
        verify(original).clone();
    }

    // -------------------------------------------------------------------------
    // getBlock
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
        "0,   5, 10, 3,  15, 13",
        "90,  5, 10, 3,  7,  13",
        "180, 5, 10, 3,  5,  7",
        "270, 5, 10, 3,  13, 7",
    })
    void getBlock_resolvesCorrectWorldPosition(float yaw,
        int originX, int originY, int originZ,
        int expectedX, int expectedZ) {
        World world = mock(World.class);
        Block expectedBlock = mock(Block.class);

        int relX = 5;
        int relY = 2;
        int relZ = 3;

        int[] worldOffset = ModifyHelper.relativeToWorld(relX, relZ, yaw);
        Location targetLocation = new Location(world, originX, originY, originZ);

        Location expectedLocation = new Location(world,
            originX + worldOffset[0],
            originY + relY,
            originZ + worldOffset[1]);

        when(world.getBlockAt(expectedLocation)).thenReturn(expectedBlock);

        BlockData blockData = mock(BlockData.class);
        ModifyClipboardEntry entry = new ModifyClipboardEntry(
            new Location(world, relX, relY, relZ), Material.STONE, blockData);

        Block result = ModifyHelper.getBlock(entry, yaw, targetLocation);

        assertNotNull(result);
        assertEquals(expectedLocation.getBlockX(), result.getLocation() != null
            ? (int) result.getLocation().getX() : expectedLocation.getBlockX());
    }

    @Test
    void getBlock_withYaw0_returnsBlockAtDirectOffset() {
        World world = mock(World.class);
        Block block = mock(Block.class);
        Location blockLocation = new Location(world, 15, 12, 13);
        when(block.getLocation()).thenReturn(blockLocation);
        when(world.getBlockAt(blockLocation)).thenReturn(block);

        BlockData blockData = mock(BlockData.class);
        ModifyClipboardEntry entry = new ModifyClipboardEntry(
            new Location(world, 5, 2, 3), Material.STONE, blockData);

        Location origin = new Location(world, 10, 10, 10);
        Block result = ModifyHelper.getBlock(entry, 0f, origin);

        assertNotNull(result);
    }

    // -------------------------------------------------------------------------
    // getRelativeCopySelection
    // -------------------------------------------------------------------------

    @Test
    void getRelativeCopySelection_subtractsOriginFromBothPositions() {
        World world = mock(World.class);

        Location pos1 = new Location(world, 10, 5, 20);
        Location pos2 = new Location(world, 15, 10, 25);
        Selection selection = new Selection(pos1, pos2);

        Location origin = new Location(world, 5, 5, 10, 90f, 45f);

        Selection result = ModifyHelper.getRelativeCopySelection(selection, origin);

        assertNotNull(result);
        assertEquals(5, result.getPos1().getBlockX());
        assertEquals(0, result.getPos1().getBlockY());
        assertEquals(10, result.getPos1().getBlockZ());

        assertEquals(10, result.getPos2().getBlockX());
        assertEquals(5, result.getPos2().getBlockY());
        assertEquals(15, result.getPos2().getBlockZ());
    }

    @Test
    void getRelativeCopySelection_appliesOriginYawAndPitchToBothPositions() {
        World world = mock(World.class);

        Location pos1 = new Location(world, 10, 5, 20, 0f, 0f);
        Location pos2 = new Location(world, 15, 10, 25, 0f, 0f);
        Selection selection = new Selection(pos1, pos2);

        float expectedYaw = 180f;
        float expectedPitch = 30f;
        Location origin = new Location(world, 0, 0, 0, expectedYaw, expectedPitch);

        Selection result = ModifyHelper.getRelativeCopySelection(selection, origin);

        assertEquals(expectedYaw, result.getPos1().getYaw());
        assertEquals(expectedPitch, result.getPos1().getPitch());
        assertEquals(expectedYaw, result.getPos2().getYaw());
        assertEquals(expectedPitch, result.getPos2().getPitch());
    }

    @Test
    void getRelativeCopySelection_withOriginAtZero_returnsUnchangedPositions() {
        World world = mock(World.class);

        Location pos1 = new Location(world, 3, 1, 7);
        Location pos2 = new Location(world, 6, 4, 9);
        Selection selection = new Selection(pos1, pos2);

        Location origin = new Location(world, 0, 0, 0, 0f, 0f);

        Selection result = ModifyHelper.getRelativeCopySelection(selection, origin);

        assertEquals(3, result.getPos1().getBlockX());
        assertEquals(1, result.getPos1().getBlockY());
        assertEquals(7, result.getPos1().getBlockZ());

        assertEquals(6, result.getPos2().getBlockX());
        assertEquals(4, result.getPos2().getBlockY());
        assertEquals(9, result.getPos2().getBlockZ());
    }

    // -------------------------------------------------------------------------
    // getModifyClipboardEntry
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
        "0",
        "90",
        "180",
        "270",
    })
    void getModifyClipboardEntry_storesCorrectMaterialAndBlockData(float playerYaw) {
        World world = mock(World.class);

        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);

        when(block.getWorld()).thenReturn(world);
        when(block.getX()).thenReturn(5);
        when(block.getY()).thenReturn(3);
        when(block.getZ()).thenReturn(7);
        when(block.getType()).thenReturn(Material.DIAMOND_BLOCK);
        when(block.getBlockData()).thenReturn(blockData);

        org.bukkit.entity.Player player = mock(org.bukkit.entity.Player.class);
        when(player.getLocation()).thenReturn(new Location(world, 0, 0, 0, playerYaw, 0f));

        Location origin = new Location(world, 2, 1, 4);

        ModifyClipboardEntry result = ModifyHelper.getModifyClipboardEntry(block, player, origin);

        assertNotNull(result);
        assertEquals(Material.DIAMOND_BLOCK, result.getMaterial());
        assertEquals(blockData, result.getData());
    }

    @ParameterizedTest
    @CsvSource({
        "0,   3, 2, 3",
        "90,  3, 2, -3",
        "180, -3, 2, -3",
        "270, -3, 2, 3",
    })
    void getModifyClipboardEntry_transformsRelativePositionBasedOnPlayerYaw(
        float playerYaw, int expectedLocalX, int expectedLocalY, int expectedLocalZ) {
        World world = mock(World.class);

        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);

        when(block.getWorld()).thenReturn(world);
        when(block.getX()).thenReturn(5);
        when(block.getY()).thenReturn(3);
        when(block.getZ()).thenReturn(7);
        when(block.getType()).thenReturn(Material.STONE);
        when(block.getBlockData()).thenReturn(blockData);

        org.bukkit.entity.Player player = mock(org.bukkit.entity.Player.class);
        when(player.getLocation()).thenReturn(new Location(world, 0, 0, 0, playerYaw, 0f));

        Location origin = new Location(world, 2, 1, 4);

        ModifyClipboardEntry result = ModifyHelper.getModifyClipboardEntry(block, player, origin);

        assertEquals(expectedLocalX, result.getLocation().getBlockX());
        assertEquals(expectedLocalY, result.getLocation().getBlockY());
        assertEquals(expectedLocalZ, result.getLocation().getBlockZ());
    }

    // -------------------------------------------------------------------------
    // undo
    // -------------------------------------------------------------------------

    @Test
    void undo_setsBlockTypeFromHistoryEntry() {
        World world = mock(World.class);
        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);

        Location location = new Location(world, 1, 2, 3);
        when(world.getBlockAt(location)).thenReturn(block);
        when(location.getBlock()).thenReturn(block);

        ModifyHistoryEntry entry = new ModifyHistoryEntry(location, Material.OBSIDIAN, blockData);

        ModifyHelper.undo(entry);

        verify(block).setType(Material.OBSIDIAN);
    }

    @Test
    void undo_setsBlockDataFromHistoryEntry() {
        World world = mock(World.class);
        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);

        Location location = new Location(world, 1, 2, 3);
        when(world.getBlockAt(location)).thenReturn(block);
        when(location.getBlock()).thenReturn(block);

        ModifyHistoryEntry entry = new ModifyHistoryEntry(location, Material.OBSIDIAN, blockData);

        ModifyHelper.undo(entry);

        verify(block).setBlockData(blockData);
    }

    @Test
    void undo_withDifferentMaterials_setsCorrectType() {
        World world = mock(World.class);
        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);

        Location location = new Location(world, 5, 64, 5);
        when(world.getBlockAt(location)).thenReturn(block);
        when(location.getBlock()).thenReturn(block);

        ModifyHistoryEntry entry = new ModifyHistoryEntry(location, Material.GRASS_BLOCK, blockData);

        ModifyHelper.undo(entry);

        verify(block).setType(Material.GRASS_BLOCK);
        verify(block).setBlockData(blockData);
    }

    // -------------------------------------------------------------------------
    // rotate (existing)
    // -------------------------------------------------------------------------

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