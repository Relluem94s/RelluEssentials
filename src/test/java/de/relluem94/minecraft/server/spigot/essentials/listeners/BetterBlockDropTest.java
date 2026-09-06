package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetterBlockDropTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private WorldGroupService worldGroupService;

  @Mock
  private SchedulerService schedulerService;

  @Mock
  private BlockBreakEvent blockBreakEvent;

  @Mock
  private Block block;

  @Mock
  private BlockData blockData;

  @Mock
  private Location location;

  @Mock
  private World world;

  private BetterBlockDrop betterBlockDrop;

  @BeforeEach
  void setUp() {
    betterBlockDrop = new BetterBlockDrop();
    betterBlockDrop.injectContext(serviceContext);
  }

  @Test
  void injectContextStoresServiceContext() {
    BetterBlockDrop listener = new BetterBlockDrop();
    listener.injectContext(serviceContext);
    assertInstanceOf(ListenerConstruct.class, listener);
  }

  @Test
  void onBreakDoesNothingWhenWorldIsNull() {
    when(blockBreakEvent.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);
    when(location.getWorld()).thenReturn(null);

    betterBlockDrop.onBreak(blockBreakEvent);

    verifyNoInteractions(serviceContext);
  }

  @Test
  void onBreakDoesNothingWhenOreRespawnSettingIsInactive() {
    when(blockBreakEvent.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);
    when(location.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.ORE_RESPAWN, "world")).thenReturn(false);

    betterBlockDrop.onBreak(blockBreakEvent);

    verifyNoInteractions(schedulerService);
  }

  @Test
  void onBreakSchedulesBlockRestoreWhenOreRespawnActiveAndBlockIsOre() {
    when(blockBreakEvent.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);
    when(location.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.ORE_RESPAWN, "world")).thenReturn(true);
    when(block.getBlockData()).thenReturn(blockData);
    when(blockData.getMaterial()).thenReturn(Material.DIAMOND_ORE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    betterBlockDrop.onBreak(blockBreakEvent);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(schedulerService).runTaskLater(runnableCaptor.capture(), eq(10000L));

    runnableCaptor.getValue().run();
    verify(block).setType(Material.DIAMOND_ORE);
  }

  @Test
  void onBreakDoesNotScheduleWhenOreRespawnActiveButBlockIsNotOre() {
    when(blockBreakEvent.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);
    when(location.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.ORE_RESPAWN, "world")).thenReturn(true);
    when(block.getBlockData()).thenReturn(blockData);
    when(blockData.getMaterial()).thenReturn(Material.DIRT);

    betterBlockDrop.onBreak(blockBreakEvent);

    verifyNoInteractions(schedulerService);
  }

  @Test
  void onBreakSchedulesBlockRestoreForAllOreTypes() {
    Material[] ores = {
        Material.DIAMOND_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE,
        Material.COAL_ORE, Material.IRON_ORE, Material.COPPER_ORE,
        Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_COPPER_ORE,
        Material.GOLD_ORE, Material.EMERALD_ORE,
        Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE
    };

    for (Material ore : ores) {
      BetterBlockDrop listener = new BetterBlockDrop();
      listener.injectContext(serviceContext);

      when(blockBreakEvent.getBlock()).thenReturn(block);
      when(block.getLocation()).thenReturn(location);
      when(location.getWorld()).thenReturn(world);
      when(world.getName()).thenReturn("world");
      when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
      when(worldGroupService.isSettingActiveForWorld(WorldSetting.ORE_RESPAWN, "world")).thenReturn(true);
      when(block.getBlockData()).thenReturn(blockData);
      when(blockData.getMaterial()).thenReturn(ore);
      when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

      listener.onBreak(blockBreakEvent);

      verify(schedulerService, atLeastOnce()).runTaskLater(any(Runnable.class), eq(10000L));
      clearInvocations(schedulerService);
    }
  }

  @Test
  void onBreakScheduledRunnableSetsCorrectMaterialOnBlock() {
    when(blockBreakEvent.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);
    when(location.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.ORE_RESPAWN, "world")).thenReturn(true);
    when(block.getBlockData()).thenReturn(blockData);
    when(blockData.getMaterial()).thenReturn(Material.EMERALD_ORE);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    betterBlockDrop.onBreak(blockBreakEvent);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(schedulerService).runTaskLater(runnableCaptor.capture(), eq(10000L));

    runnableCaptor.getValue().run();

    assertAll(
        () -> verify(block).setType(Material.EMERALD_ORE)
    );
  }
}