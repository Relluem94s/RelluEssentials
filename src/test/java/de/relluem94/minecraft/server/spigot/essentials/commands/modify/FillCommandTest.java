package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.translationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class FillCommandTest {

    private Player player;
    private UndoHistoryService undoHistoryService;

    private MockedStatic<RelluEssentials> mockedRelluEssentials;

    private static final int BLOCKS_PER_TICK = 2;
    private static final int MAX_RADIUS = 10;
    private static final int MAX_ITERATIONS = 1000;

    @BeforeAll
    static void setUpServer(){
        if(Bukkit.getServer() != null){
            return;
        }

        org.bukkit.Server serverMock = mock(org.bukkit.Server.class);
        org.bukkit.scheduler.BukkitScheduler schedulerMock = mock(org.bukkit.scheduler.BukkitScheduler.class);
        java.util.logging.Logger silentLogger = java.util.logging.Logger.getLogger("test");
        silentLogger.setUseParentHandlers(false);
        silentLogger.setLevel(java.util.logging.Level.OFF);
        when(serverMock.getScheduler()).thenReturn(schedulerMock);
        when(serverMock.getLogger()).thenReturn(silentLogger);
        org.bukkit.Bukkit.setServer(serverMock);
    }

    @AfterAll
    static void tearDownServer() throws Exception {
        java.lang.reflect.Field serverField = org.bukkit.Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        serverField.set(null, null);
    }

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        undoHistoryService = mock(UndoHistoryService.class);

        RelluEssentials relluEssentialsMock = mock(RelluEssentials.class);
        translationService translationServiceMock = mock(translationService.class);

        mockedRelluEssentials = mockStatic(RelluEssentials.class);
        mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentialsMock);
        RelluEssentials.translationService = translationServiceMock;

        when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
        when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
    }

    @Test
    void execute_fill_withInvalidMaterial_sendsWrongMaterialMessage() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        fillCommand.execute(player, new String[]{"fill", "NOT_A_REAL_MATERIAL_XYZ", "5"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryService, never()).add(any(), any());
    }

    @Test
    void execute_fill_withNonIntegerRadius_sendsInvalidMessage() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        fillCommand.execute(player, new String[]{"fill", "STONE", "notANumber"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryService, never()).add(any(), any());
    }

    @Test
    void execute_fill_withZeroRadius_sendsInvalidMessage() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        fillCommand.execute(player, new String[]{"fill", "STONE", "0"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryService, never()).add(any(), any());
    }

    @Test
    void execute_fill_withNegativeRadius_sendsInvalidMessage() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        fillCommand.execute(player, new String[]{"fill", "STONE", "-3"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryService, never()).add(any(), any());
    }

    @Test
    void execute_fill_withRadiusExceedingMax_sendsRadiusTooHighMessage() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        World world = mock(World.class);
        Block startBlock = buildSolidBlock(world, 0, 0, 0);
        Location playerLocation = buildLocation(world, 0, 0, 0);
        when(player.getLocation()).thenReturn(playerLocation);
        when(player.getLocation().clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        fillCommand.execute(player, new String[]{"fill", "STONE", "99"});

        verify(player, atLeastOnce()).sendMessage(anyString());
        verify(undoHistoryService).add(eq(player), any());
    }

    @Test
    void execute_fill_withNonEmptyStartBlock_addsEmptyHistory() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        World world = mock(World.class);
        Block startBlock = buildSolidBlock(world, 0, 0, 0);
        Location playerLocation = buildLocation(world, 0, 0, 0);
        when(player.getLocation()).thenReturn(playerLocation);
        when(player.getLocation().clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        fillCommand.execute(player, new String[]{"fill", "STONE", "5"});

        verify(undoHistoryService).add(eq(player), argThat(java.util.List::isEmpty));
    }

    @Test
    void execute_fill_withEmptyStartBlock_fillsAdjacentAirAndAddsHistory() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0, 0);
        Block solidNeighbor = buildSolidBlock(world, 0, 0, 0);
        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidNeighbor);

        Location playerLocation = buildLocation(world, 0, 0, 0);
        when(player.getLocation()).thenReturn(playerLocation);
        when(player.getLocation().clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection(any()))
                    .thenAnswer(_ -> null);

            fillCommand.execute(player, new String[]{"fill", "STONE", "5"});

            verify(undoHistoryService).add(eq(player), argThat(list -> list.size() == 1));
            verify(player).sendMessage((String) null);
        }
    }

    @Test
    void execute_fillr_withEmptyStartBlockAndEmptyBelow_spreadsBothDirections() {
        FillCommand fillrCommand = new FillCommand(true, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0, 0);
        Block belowBlock = buildEmptyBlock(world, 0, -1, 0);
        Block solidNeighbor = buildSolidBlock(world, 0, 0, 0);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidNeighbor);
        when(world.getBlockAt(0, -1, 0)).thenReturn(belowBlock);

        Location playerLocation = buildLocation(world, 0, 0, 0);
        when(player.getLocation()).thenReturn(playerLocation);
        when(player.getLocation().clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection(any()))
                    .thenAnswer(_ -> null);

            fillrCommand.execute(player, new String[]{"fillr", "STONE", "5"});

            verify(undoHistoryService).add(eq(player), argThat(list -> list.size() == 2));
            verify(player).sendMessage((String) null);
        }
    }

    @Test
    void matches_fill_withCorrectArgs_returnsTrue() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);
        assert fillCommand.matches(new String[]{"fill", "STONE", "5"});
    }

    @Test
    void matches_fillr_withCorrectArgs_returnsTrue() {
        FillCommand fillrCommand = new FillCommand(true, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);
        assert fillrCommand.matches(new String[]{"fillr", "STONE", "5"});
    }

    @Test
    void matches_fill_withWrongCommand_returnsFalse() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);
        assert !fillCommand.matches(new String[]{"fillr", "STONE", "5"});
    }

    @Test
    void matches_fillr_withWrongCommand_returnsFalse() {
        FillCommand fillrCommand = new FillCommand(true, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);
        assert !fillrCommand.matches(new String[]{"fill", "STONE", "5"});
    }

    @Test
    void matches_fill_withTooFewArgs_returnsFalse() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);
        assert !fillCommand.matches(new String[]{"fill", "STONE"});
    }

    @Test
    void matches_fill_withTooManyArgs_returnsFalse() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);
        assert !fillCommand.matches(new String[]{"fill", "STONE", "5", "extra"});
    }


    @Test
    void execute_fill_whenMaxIterationsReached_stopsProcessingAndAddsPartialHistory() {
        int maxIterations = 2;
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, maxIterations,
            undoHistoryService);

        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0, 0);

        Block emptyNeighbor1 = buildEmptyBlock(world, 1, 0, 0);
        Block emptyNeighbor2 = buildEmptyBlock(world, -1, 0, 0);
        Block solidFallback = buildSolidBlock(world, 0, 0, 0);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidFallback);
        when(world.getBlockAt(1, 0, 0)).thenReturn(emptyNeighbor1);
        when(world.getBlockAt(-1, 0, 0)).thenReturn(emptyNeighbor2);

        Location playerLocation = buildLocation(world, 0, 0, 0);
        when(player.getLocation()).thenReturn(playerLocation);
        when(player.getLocation().clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection(any()))
                    .thenAnswer(_ -> null);

            fillCommand.execute(player, new String[]{"fill", "STONE", "5"});

            verify(undoHistoryService).add(eq(player), argThat(list -> list.size() < 3));
        }
    }

    @Test
    void execute_fill_whenBlockExceedsRadius_skipsBlockAndDoesNotAddToHistory() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0, 0);

        Block farEmptyBlock = buildEmptyBlock(world, 10, 0, 0);
        Block solidFallback = buildSolidBlock(world, 0, 0, 0);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidFallback);
        when(world.getBlockAt(10, 0, 0)).thenReturn(farEmptyBlock);

        Location playerLocation = buildLocation(world, 0, 0, 0);
        when(player.getLocation()).thenReturn(playerLocation);
        when(player.getLocation().clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection(any()))
                    .thenAnswer(_ -> null);

            fillCommand.execute(player, new String[]{"fill", "STONE", "1"});

            verify(undoHistoryService).add(eq(player), argThat(list -> list.size() == 1));
        }
    }


    @Test
    void execute_fill_whenNeighborAlreadyVisited_doesNotProcessNeighborTwice() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0, 0);
        Block leftNeighbor = buildEmptyBlock(world, -1, 0, 0);
        Block rightNeighbor = buildEmptyBlock(world, 1, 0, 0);
        Block solidFallback = buildSolidBlock(world, 0, 0, 0);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidFallback);
        when(world.getBlockAt(-1, 0, 0)).thenReturn(leftNeighbor);
        when(world.getBlockAt(1, 0, 0)).thenReturn(rightNeighbor);
        when(world.getBlockAt(0, 0, 0)).thenReturn(startBlock);

        Location playerLocation = buildLocation(world, 0, 0, 0);
        when(player.getLocation()).thenReturn(playerLocation);
        when(player.getLocation().clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection(any()))
                    .thenAnswer(_ -> null);

            fillCommand.execute(player, new String[]{"fill", "STONE", "5"});

            verify(undoHistoryService).add(eq(player), argThat(list -> list.size() == 4));
        }
    }


    @Test
    void execute_fill_whenNeighborExceedsRadius_skipsNeighborAndDoesNotAddToHistory() {
        FillCommand fillCommand = new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS,
            undoHistoryService);

        World world = mock(World.class);

        Block startBlock = buildEmptyBlock(world, 0, 0, 0);
        Block outOfRadiusNeighbor = buildEmptyBlock(world, 1, 0, 0);
        Block solidFallback = buildSolidBlock(world, 0, 0, 0);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidFallback);
        when(world.getBlockAt(0, 0, 0)).thenReturn(startBlock);
        when(world.getBlockAt(1, 0, 0)).thenReturn(outOfRadiusNeighbor);

        Location startLocation = mock(Location.class);
        when(startLocation.getWorld()).thenReturn(world);
        when(startLocation.getBlockX()).thenReturn(0);
        when(startLocation.getBlockY()).thenReturn(0);
        when(startLocation.getBlockZ()).thenReturn(0);
        when(startLocation.distance(any(Location.class))).thenReturn(0.0);

        Location neighborLocation = mock(Location.class);
        when(neighborLocation.getWorld()).thenReturn(world);
        when(neighborLocation.getBlockX()).thenReturn(1);
        when(neighborLocation.getBlockY()).thenReturn(0);
        when(neighborLocation.getBlockZ()).thenReturn(0);
        when(neighborLocation.distance(any(Location.class))).thenReturn(2.0);

        when(startBlock.getLocation()).thenReturn(startLocation);
        when(outOfRadiusNeighbor.getLocation()).thenReturn(neighborLocation);

        when(player.getLocation()).thenReturn(startLocation);
        when(player.getLocation().clone()).thenReturn(startLocation);
        when(startLocation.getBlock()).thenReturn(startBlock);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection(any()))
                    .thenAnswer(_ -> null);

            fillCommand.execute(player, new String[]{"fill", "STONE", "1"});

            verify(undoHistoryService).add(eq(player), argThat(list -> list.size() == 1));
        }
    }

    private Location buildLocation(World world, int x, int y, int z) {
        Location location = mock(Location.class);
        when(location.getWorld()).thenReturn(world);
        when(location.getBlockX()).thenReturn(x);
        when(location.getBlockY()).thenReturn(y);
        when(location.getBlockZ()).thenReturn(z);
        return location;
    }

    private Block buildEmptyBlock(World world, int x, int y, int z) {
        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);
        Location location = mock(Location.class);
        when(block.getType()).thenReturn(Material.AIR);
        when(block.isEmpty()).thenReturn(true);
        when(block.getBlockData()).thenReturn(blockData);
        when(block.getLocation()).thenReturn(location);
        when(block.getX()).thenReturn(x);
        when(block.getY()).thenReturn(y);
        when(block.getZ()).thenReturn(z);
        when(block.getWorld()).thenReturn(world);
        return block;
    }

    private Block buildSolidBlock(World world, int x, int y, int z) {
        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);
        Location location = mock(Location.class);
        when(block.getType()).thenReturn(Material.STONE);
        when(block.isEmpty()).thenReturn(false);
        when(block.getBlockData()).thenReturn(blockData);
        when(block.getLocation()).thenReturn(location);
        when(block.getX()).thenReturn(x);
        when(block.getY()).thenReturn(y);
        when(block.getZ()).thenReturn(z);
        when(block.getWorld()).thenReturn(world);
        return block;
    }
}