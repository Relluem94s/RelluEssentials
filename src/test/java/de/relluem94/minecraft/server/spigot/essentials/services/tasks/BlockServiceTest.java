package de.relluem94.minecraft.server.spigot.essentials.services.tasks;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class BlockServiceTest {

  private SchedulerService schedulerService;
  private Server server;
  private BlockService blockService;
  private final Material targetMaterial = Material.DIAMOND_BLOCK;

  @BeforeEach
  void setUp() {
    schedulerService = mock(SchedulerService.class);
    server = mock(Server.class);
    blockService = new BlockService(schedulerService, targetMaterial, server);
  }

  @Test
  void testAddAndMergeLocations() {
    Location loc1 = mock(Location.class);
    Location loc2 = mock(Location.class);
    BlockService otherService = new BlockService(schedulerService, Material.STONE, server);

    blockService.addLocation(loc1, 10L);
    otherService.addLocation(loc2, 20L);

    blockService.mergeLocations(otherService);

    blockService.applyBlocks(0);

    verify(schedulerService, times(2)).scheduleSyncDelayedTask(any(Runnable.class), anyLong());
  }

  @Test
  void testApplyBlocks() {
    Location location = mock(Location.class);
    Block block = mock(Block.class);
    when(location.getBlock()).thenReturn(block);

    blockService.addLocation(location, 10L);
    blockService.applyBlocks(5L);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(schedulerService).scheduleSyncDelayedTask(runnableCaptor.capture(), eq(15L));

    runnableCaptor.getValue().run();
    verify(block).setType(targetMaterial);
  }

  @Test
  void testApplyBlocksWithZeroDelay() {
    Location location = mock(Location.class);
    Block block = mock(Block.class);
    when(location.getBlock()).thenReturn(block);

    blockService.addLocation(location, 10L);
    blockService.applyBlocks();

    verify(schedulerService).scheduleSyncDelayedTask(any(Runnable.class), eq(10L));
  }

  @Test
  void testApplyMaterialSuccess() {
    Location location = mock(Location.class);
    Block block = mock(Block.class);
    BlockData blockData = mock(BlockData.class);
    BlockData newBlockData = mock(BlockData.class);

    Material existingMaterial = Material.DIRT;
    String existingDataString = "minecraft:dirt";
    String newDataString = "minecraft:diamond_block";

    when(location.getBlock()).thenReturn(block);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getType()).thenReturn(existingMaterial);
    when(blockData.getAsString()).thenReturn(existingDataString);
    when(server.createBlockData(newDataString)).thenReturn(newBlockData);

    blockService.addLocation(location, 0L);
    blockService.applyMaterial(0L);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(schedulerService).scheduleSyncDelayedTask(runnableCaptor.capture(), anyLong());

    runnableCaptor.getValue().run();

    verify(block).setBlockData(newBlockData);
  }

  @Test
  void testApplyMaterialFallbackToSetTypeOnException() {
    Location location = mock(Location.class);
    Block block = mock(Block.class);
    BlockData blockData = mock(BlockData.class);

    when(location.getBlock()).thenReturn(block);
    when(block.getBlockData()).thenReturn(blockData);
    when(blockData.getAsString()).thenThrow(new IllegalArgumentException("Simulated failure"));

    blockService.addLocation(location, 0L);
    blockService.applyMaterial(0L);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(schedulerService).scheduleSyncDelayedTask(runnableCaptor.capture(), anyLong());

    runnableCaptor.getValue().run();

    verify(block).setType(targetMaterial);
  }

  @Test
  void testIsBlockOfMaterial() {
    Location location = mock(Location.class);
    Block block = mock(Block.class);
    when(location.getBlock()).thenReturn(block);
    when(block.getType()).thenReturn(Material.DIAMOND_BLOCK);

    boolean isDiamondBlock = BlockService.isBlockOfMaterial(location, Material.DIAMOND_BLOCK);
    boolean isStone = BlockService.isBlockOfMaterial(location, Material.STONE);

    org.junit.jupiter.api.Assertions.assertTrue(isDiamondBlock);
    org.junit.jupiter.api.Assertions.assertFalse(isStone);
  }
}