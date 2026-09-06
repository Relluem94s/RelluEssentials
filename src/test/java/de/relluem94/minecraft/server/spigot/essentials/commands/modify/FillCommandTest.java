package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FillCommandTest {

    private Player player;
    private UndoHistoryService undoHistoryService;
    private ProtectionService protectionService;
    private SchedulerService schedulerService;
    private FillCommand fillCommand;
    private FillCommand fillrCommand;
    private Server server;

    private static final int BLOCKS_PER_TICK = 2;
    private static final int MAX_RADIUS = 10;
    private static final int MAX_ITERATIONS = 1000;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        undoHistoryService = mock(UndoHistoryService.class);
        protectionService = mock(ProtectionService.class);
        schedulerService = mock(SchedulerService.class);
        server = mock(Server.class);

        fillCommand = new FillCommand(buildServiceContext(), false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS);
        fillrCommand = new FillCommand(buildServiceContext(), true, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS);
    }

    private ServiceContext buildServiceContext() {
        TranslationService translationServiceMock = mock(TranslationService.class);
        when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");
        when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");

        PluginMetadataService pluginMetadataService = mock(PluginMetadataService.class);
        Plugin plugin = mock(Plugin.class);
        when(pluginMetadataService.getPlugin()).thenReturn(plugin);
        when(plugin.getServer()).thenReturn(server);

        ServiceContext serviceContext = mock(ServiceContext.class);
        when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);
        when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
        when(serviceContext.getProtectionService()).thenReturn(protectionService);
        when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
        when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);

        return serviceContext;
    }

    @Test
    void execute_fill_withInvalidMaterial_sendsWrongMaterialMessage() {
        fillCommand.execute(player, new String[]{"fill", "NOT_A_REAL_MATERIAL_XYZ", "5"});

        verify(player).sendMessage(any(String.class));
        verify(undoHistoryService, never()).addHistory(any(), any());
    }

    @Test
    void execute_fill_withNonIntegerRadius_sendsInvalidMessage() {
        fillCommand.execute(player, new String[]{"fill", "STONE", "notANumber"});

        verify(player).sendMessage(any(String.class));
        verify(undoHistoryService, never()).addHistory(any(), any());
    }

    @Test
    void execute_fill_withZeroRadius_sendsInvalidMessage() {
        fillCommand.execute(player, new String[]{"fill", "STONE", "0"});

        verify(player).sendMessage(any(String.class));
        verify(undoHistoryService, never()).addHistory(any(), any());
    }

    @Test
    void execute_fill_withNegativeRadius_sendsInvalidMessage() {
        fillCommand.execute(player, new String[]{"fill", "STONE", "-3"});

        verify(player).sendMessage(any(String.class));
        verify(undoHistoryService, never()).addHistory(any(), any());
    }

    @Test
    void execute_fill_withRadiusExceedingMax_sendsRadiusTooHighMessage() {
        World world = mock(World.class);
        Block startBlock = buildSolidBlock(world);
        Location playerLocation = buildLocation(world);
        when(player.getLocation()).thenReturn(playerLocation);
        when(playerLocation.clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        fillCommand.execute(player, new String[]{"fill", "STONE", "99"});

        verify(player, atLeastOnce()).sendMessage(any(String.class));
        verify(undoHistoryService).addHistory(eq(player), any());
    }

    @Test
    void execute_fill_withNonEmptyStartBlock_addsEmptyHistory() {
        World world = mock(World.class);
        Block startBlock = buildSolidBlock(world);
        Location playerLocation = buildLocation(world);
        when(player.getLocation()).thenReturn(playerLocation);
        when(playerLocation.clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        fillCommand.execute(player, new String[]{"fill", "STONE", "5"});

        verify(undoHistoryService).addHistory(eq(player), argThat(java.util.List::isEmpty));
    }

    @Test
    void execute_fill_withEmptyStartBlock_fillsAdjacentAirAndAddsHistory() {
        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0);
        Block solidNeighbor = buildSolidBlock(world);
        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidNeighbor);

        Location playerLocation = buildLocation(world);
        when(player.getLocation()).thenReturn(playerLocation);
        when(playerLocation.clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        fillCommand.execute(player, new String[]{"fill", "STONE", "5"});

        verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }

    @Test
    void execute_fillr_withEmptyStartBlockAndEmptyBelow_spreadsBothDirections() {
        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0);
        Block belowBlock = buildEmptyBlock(world, 0, -1);
        Block solidNeighbor = buildSolidBlock(world);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidNeighbor);
        when(world.getBlockAt(0, -1, 0)).thenReturn(belowBlock);

        Location playerLocation = buildLocation(world);
        when(player.getLocation()).thenReturn(playerLocation);
        when(playerLocation.clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        fillrCommand.execute(player, new String[]{"fillr", "STONE", "5"});

        verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 2));
    }

    @Test
    void matches_fill_withCorrectArgs_returnsTrue() {
        assert fillCommand.matches(new String[]{"fill", "STONE", "5"});
    }

    @Test
    void matches_fillr_withCorrectArgs_returnsTrue() {
        assert fillrCommand.matches(new String[]{"fillr", "STONE", "5"});
    }

    @Test
    void matches_fill_withWrongCommand_returnsFalse() {
        assert !fillCommand.matches(new String[]{"fillr", "STONE", "5"});
    }

    @Test
    void matches_fillr_withWrongCommand_returnsFalse() {
        assert !fillrCommand.matches(new String[]{"fill", "STONE", "5"});
    }

    @Test
    void matches_fill_withTooFewArgs_returnsFalse() {
        assert !fillCommand.matches(new String[]{"fill", "STONE"});
    }

    @Test
    void matches_fill_withTooManyArgs_returnsFalse() {
        assert !fillCommand.matches(new String[]{"fill", "STONE", "5", "extra"});
    }

    @Test
    void execute_fill_whenMaxIterationsReached_stopsProcessingAndAddsPartialHistory() {
        FillCommand limitedFillCommand = new FillCommand(
            buildServiceContext(), false, BLOCKS_PER_TICK, MAX_RADIUS, 2);

        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0);
        Block emptyNeighbor1 = buildEmptyBlock(world, 1, 0);
        Block emptyNeighbor2 = buildEmptyBlock(world, -1, 0);
        Block solidFallback = buildSolidBlock(world);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidFallback);
        when(world.getBlockAt(1, 0, 0)).thenReturn(emptyNeighbor1);
        when(world.getBlockAt(-1, 0, 0)).thenReturn(emptyNeighbor2);

        Location playerLocation = buildLocation(world);
        when(player.getLocation()).thenReturn(playerLocation);
        when(playerLocation.clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        limitedFillCommand.execute(player, new String[]{"fill", "STONE", "5"});

        verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() < 3));
    }

    @Test
    void execute_fill_whenBlockExceedsRadius_skipsBlockAndDoesNotAddToHistory() {
        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0);
        Block farEmptyBlock = buildEmptyBlock(world, 10, 0);
        Block solidFallback = buildSolidBlock(world);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidFallback);
        when(world.getBlockAt(10, 0, 0)).thenReturn(farEmptyBlock);

        Location playerLocation = buildLocation(world);
        when(player.getLocation()).thenReturn(playerLocation);
        when(playerLocation.clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        fillCommand.execute(player, new String[]{"fill", "STONE", "1"});

        verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }

    @Test
    void execute_fill_whenNeighborAlreadyVisited_doesNotProcessNeighborTwice() {
        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0);
        Block leftNeighbor = buildEmptyBlock(world, -1, 0);
        Block rightNeighbor = buildEmptyBlock(world, 1, 0);
        Block solidFallback = buildSolidBlock(world);

        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(solidFallback);
        when(world.getBlockAt(-1, 0, 0)).thenReturn(leftNeighbor);
        when(world.getBlockAt(1, 0, 0)).thenReturn(rightNeighbor);
        when(world.getBlockAt(0, 0, 0)).thenReturn(startBlock);

        Location playerLocation = buildLocation(world);
        when(player.getLocation()).thenReturn(playerLocation);
        when(playerLocation.clone()).thenReturn(playerLocation);
        when(playerLocation.getBlock()).thenReturn(startBlock);

        fillCommand.execute(player, new String[]{"fill", "STONE", "5"});

        verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 4));
    }

    @Test
    void execute_fill_whenNeighborExceedsRadius_skipsNeighborAndDoesNotAddToHistory() {
        World world = mock(World.class);
        Block startBlock = buildEmptyBlock(world, 0, 0);
        Block outOfRadiusNeighbor = buildEmptyBlock(world, 1, 0);
        Block solidFallback = buildSolidBlock(world);

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
        when(startLocation.clone()).thenReturn(startLocation);
        when(startLocation.getBlock()).thenReturn(startBlock);

        fillCommand.execute(player, new String[]{"fill", "STONE", "1"});

        verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }

    private Location buildLocation(World world) {
        Location location = mock(Location.class);
        when(location.getWorld()).thenReturn(world);
        when(location.getBlockX()).thenReturn(0);
        when(location.getBlockY()).thenReturn(0);
        when(location.getBlockZ()).thenReturn(0);
        return location;
    }

    private Block buildEmptyBlock(World world, int x, int y) {
        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);
        Location location = mock(Location.class);
        when(block.getType()).thenReturn(Material.AIR);
        when(block.isEmpty()).thenReturn(true);
        when(block.getBlockData()).thenReturn(blockData);
        when(block.getLocation()).thenReturn(location);
        when(block.getX()).thenReturn(x);
        when(block.getY()).thenReturn(y);
        when(block.getZ()).thenReturn(0);
        when(block.getWorld()).thenReturn(world);
        return block;
    }

    private Block buildSolidBlock(World world) {
        Block block = mock(Block.class);
        BlockData blockData = mock(BlockData.class);
        Location location = mock(Location.class);
        when(block.getType()).thenReturn(Material.STONE);
        when(block.isEmpty()).thenReturn(false);
        when(block.getBlockData()).thenReturn(blockData);
        when(block.getLocation()).thenReturn(location);
        when(block.getX()).thenReturn(0);
        when(block.getY()).thenReturn(0);
        when(block.getZ()).thenReturn(0);
        when(block.getWorld()).thenReturn(world);
        return block;
    }
}