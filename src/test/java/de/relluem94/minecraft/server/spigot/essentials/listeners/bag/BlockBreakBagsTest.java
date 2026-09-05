package de.relluem94.minecraft.server.spigot.essentials.listeners.bag;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.services.EnchantmentService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginManagerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlockBreakBagsTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private EnchantmentService enchantmentService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private PluginManagerService pluginManagerService;

  @Mock
  private Player player;

  @Mock
  private PlayerInventory playerInventory;

  @Mock
  private ItemStack mainHandItem;

  @Mock
  private Block block;

  @Mock
  private EnchantmentHelper delicateHelper;

  @Mock
  private EnchantmentHelper telekinesisHelper;

  private BlockBreakBags listener;

  @BeforeEach
  void setUp() {
    listener = new BlockBreakBags();

    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("RelluEssentials");
  }

  private void injectWithBothEnchantments() {
    when(enchantmentService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenAnswer(invocation -> {
          RelluEssentialsNamespacedKey key = invocation.getArgument(0);
          if (key.getKey().equals(EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE)) {
            return Optional.of(delicateHelper);
          }
          if (key.getKey().equals(EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS)) {
            return Optional.of(telekinesisHelper);
          }
          return Optional.empty();
        });

    listener.injectContext(serviceContext);
  }

  private void injectWithNoEnchantments() {
    listener.injectContext(serviceContext);
  }

  private BlockBreakEvent buildEvent() {
    BlockBreakEvent event = mock(BlockBreakEvent.class);
    when(event.getPlayer()).thenReturn(player);
    when(event.getBlock()).thenReturn(block);
    lenient().when(player.getInventory()).thenReturn(playerInventory);
    lenient().when(playerInventory.getItemInMainHand()).thenReturn(mainHandItem);
    return event;
  }

  @Test
  void injectContextStoresServiceContextAndResolvesEnchantments() {
    injectWithBothEnchantments();

    assertAll(
        () -> assertNotNull(listener),
        () -> assertNotNull(serviceContext)
    );
  }

  @Test
  void injectContextWithMissingEnchantsDoesNotThrow() {
    injectWithNoEnchantments();

    assertNotNull(listener);
  }

  @Test
  void onBlockBreakNoEnchantsDoesNotCancelEvent() {
    injectWithNoEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.STONE);

    listener.onBlockBreak(event);

    verify(event, never()).setCancelled(true);
  }

  @Test
  void onBlockBreakDelicateCancelsPumpkinStemBreak() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.PUMPKIN_STEM);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakDelicateCancelsMelonStemBreak() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.MELON_STEM);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakDelicateCancelsAttachedPumpkinStemBreak() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.ATTACHED_PUMPKIN_STEM);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakDelicateCancelsAttachedMelonStemBreak() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.ATTACHED_MELON_STEM);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakDelicateCancelsImmatureCropBreak() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.WHEAT);

    Ageable ageable = mock(Ageable.class);
    when(ageable.getAge()).thenReturn(0);
    when(ageable.getMaximumAge()).thenReturn(7);
    when(block.getBlockData()).thenReturn(ageable);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakDelicateDoesNotCancelMatureCropBreak() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.WHEAT);

    Ageable ageable = mock(Ageable.class);
    when(ageable.getAge()).thenReturn(7);
    when(ageable.getMaximumAge()).thenReturn(7);
    when(block.getBlockData()).thenReturn(ageable);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakDelicateCancelsTorchBreak() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.TORCH);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakDelicateCancelsLilyPadBreak() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.LILY_PAD);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakDelicateDoesNotCancelSugarCaneEvenIfImmature() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.SUGAR_CANE);

    Ageable ageable = mock(Ageable.class);
    when(ageable.getAge()).thenReturn(0);
    when(ageable.getMaximumAge()).thenReturn(15);
    when(block.getBlockData()).thenReturn(ageable);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakTelekinesisSkipsNonChorusNonCaneNonBambooBlock() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.STONE);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakTelekinesisWithSugarCaneAndNoBlockAboveDropsAndCancels() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.SUGAR_CANE);

    Block blockAbove = mock(Block.class);
    when(block.getRelative(BlockFace.UP)).thenReturn(blockAbove);
    when(blockAbove.getType()).thenReturn(Material.AIR);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);

    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);

    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(world).dropItem(any(), any()),
          () -> verify(pluginManagerService).callEvent(any(EntityPickupItemEvent.class))
      );
    }
  }

  @Test
  void onBlockBreakTelekinesisWithBambooAndNoBlockAboveDropsAndCancels() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.BAMBOO);

    Block blockAbove = mock(Block.class);
    when(block.getRelative(BlockFace.UP)).thenReturn(blockAbove);
    when(blockAbove.getType()).thenReturn(Material.AIR);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);

    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);

    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(world).dropItem(any(), any()),
          () -> verify(pluginManagerService).callEvent(any(EntityPickupItemEvent.class))
      );
    }
  }

  @Test
  void onBlockBreakProcessingBlockGuardPreventsReentryForSugarCane() {
    injectWithBothEnchantments();

    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    when(block.getType()).thenReturn(Material.SUGAR_CANE);

    Block blockAbove = mock(Block.class);
    when(block.getRelative(BlockFace.UP)).thenReturn(blockAbove);
    when(blockAbove.getType()).thenReturn(Material.AIR);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);
    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      doAnswer(_ -> {
        BlockBreakEvent reentrantEvent = mock(BlockBreakEvent.class);
        when(reentrantEvent.getPlayer()).thenReturn(player);
        when(reentrantEvent.getBlock()).thenReturn(block);
        listener.onBlockBreak(reentrantEvent);
        verify(reentrantEvent, never()).setCancelled(true);
        return null;
      }).when(pluginManagerService).callEvent(any(BlockBreakEvent.class));

      BlockBreakEvent event = buildEvent();
      listener.onBlockBreak(event);
    }
  }

  @Test
  void onBlockBreakWithNoDelicateAndNoTelekinesisDoesNothingForAnyMaterial() {
    injectWithNoEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.WHEAT);

    listener.onBlockBreak(event);

    assertAll(
        () -> verify(event, never()).setCancelled(true),
        () -> verify(event, never()).setCancelled(false)
    );
  }

  @Test
  void onBlockBreakDelicateNotActiveWhenToolLacksEnchantment() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.TORCH);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakTelekinesisNotActiveWhenToolLacksEnchantment() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.SUGAR_CANE);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakChorusPlantNotOnEndStoneDoesNotTriggerTelekinesis() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.CHORUS_PLANT);

    Block blockBelow = mock(Block.class);
    when(block.getRelative(BlockFace.DOWN)).thenReturn(blockBelow);
    when(blockBelow.getType()).thenReturn(Material.STONE);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakChorusPlantOnEndStoneWithSmallStructureCancelsAndRemovesBlocks() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.CHORUS_PLANT);

    MultipleFacing multipleFacing = mock(MultipleFacing.class);
    when(multipleFacing.getFaces()).thenReturn(Collections.emptySet());
    when(block.getBlockData()).thenReturn(multipleFacing);

    Block blockBelow = mock(Block.class);
    when(block.getRelative(BlockFace.DOWN)).thenReturn(blockBelow);
    when(blockBelow.getType()).thenReturn(Material.END_STONE);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(event.getBlock()).thenReturn(block);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);

    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);

    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(block).setType(Material.AIR),
          () -> verify(world).dropItem(any(), any()),
          () -> verify(pluginManagerService).callEvent(any(EntityPickupItemEvent.class))
      );
    }
  }

  @Test
  void onBlockBreakSugarCaneWithOneBlockAboveDropsAndCancels() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.SUGAR_CANE);

    Block secondBlock = mock(Block.class);
    when(secondBlock.getType()).thenReturn(Material.SUGAR_CANE);

    Block thirdBlock = mock(Block.class);
    when(thirdBlock.getType()).thenReturn(Material.AIR);

    when(block.getRelative(BlockFace.UP)).thenReturn(secondBlock);
    when(secondBlock.getRelative(BlockFace.UP)).thenReturn(thirdBlock);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);

    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);

    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(world).dropItem(any(), any()),
          () -> verify(pluginManagerService).callEvent(any(EntityPickupItemEvent.class))
      );
    }
  }

  @Test
  void onBlockBreakBambooWithOneBlockAboveDropsAndCancels() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.BAMBOO);

    Block secondBlock = mock(Block.class);
    when(secondBlock.getType()).thenReturn(Material.BAMBOO);

    Block thirdBlock = mock(Block.class);
    when(thirdBlock.getType()).thenReturn(Material.AIR);

    when(block.getRelative(BlockFace.UP)).thenReturn(secondBlock);
    when(secondBlock.getRelative(BlockFace.UP)).thenReturn(thirdBlock);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);

    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);

    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(world).dropItem(any(), any()),
          () -> verify(pluginManagerService).callEvent(any(EntityPickupItemEvent.class))
      );
    }
  }

  @Test
  void onBlockBreakDelicateImmatureCropWithSugarCaneTypeIsNotCanceled() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.SUGAR_CANE);

    Ageable ageable = mock(Ageable.class);
    when(ageable.getAge()).thenReturn(0);
    when(ageable.getMaximumAge()).thenReturn(15);
    when(block.getBlockData()).thenReturn(ageable);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(false);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakBothEnchantsActiveWithStoneDoesNotCancel() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.STONE);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(true);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakChorusPlantWith51BlocksDoesNotCancel() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.CHORUS_PLANT);

    Block[] connectedBlocks = new Block[52];
    for (int i = 0; i < 52; i++) {
      connectedBlocks[i] = mock(Block.class);
      when(connectedBlocks[i].getType()).thenReturn(Material.CHORUS_PLANT);
    }

    MultipleFacing rootFacing = mock(MultipleFacing.class);
    when(rootFacing.getFaces()).thenReturn(Set.of(BlockFace.UP));
    when(block.getBlockData()).thenReturn(rootFacing);
    when(block.getRelative(BlockFace.UP)).thenReturn(connectedBlocks[0]);

    for (int i = 0; i < 51; i++) {
      MultipleFacing childFacing = mock(MultipleFacing.class);
      when(childFacing.getFaces()).thenReturn(Set.of(BlockFace.UP, BlockFace.DOWN));
      when(connectedBlocks[i].getBlockData()).thenReturn(childFacing);
      when(connectedBlocks[i].getRelative(BlockFace.UP)).thenReturn(connectedBlocks[i + 1]);
      when(connectedBlocks[i].getRelative(BlockFace.DOWN)).thenReturn(i == 0 ? block : connectedBlocks[i - 1]);
    }

    MultipleFacing lastFacing = mock(MultipleFacing.class);
    when(lastFacing.getFaces()).thenReturn(Collections.emptySet());
    when(connectedBlocks[51].getBlockData()).thenReturn(lastFacing);

    Block blockBelow = mock(Block.class);
    when(block.getRelative(BlockFace.DOWN)).thenReturn(blockBelow);
    when(blockBelow.getType()).thenReturn(Material.END_STONE);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakChorusPlantRecursionIsGuardedByDeduplication() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.CHORUS_PLANT);

    Block childBlock = mock(Block.class);
    when(childBlock.getType()).thenReturn(Material.CHORUS_PLANT);

    MultipleFacing childFacing = mock(MultipleFacing.class);
    when(childFacing.getFaces()).thenReturn(Collections.emptySet());
    when(childBlock.getBlockData()).thenReturn(childFacing);

    MultipleFacing rootFacing = mock(MultipleFacing.class);
    when(rootFacing.getFaces()).thenReturn(Set.of(BlockFace.UP));
    when(block.getBlockData()).thenReturn(rootFacing);
    when(block.getRelative(BlockFace.UP)).thenReturn(childBlock);

    Block blockBelow = mock(Block.class);
    when(block.getRelative(BlockFace.DOWN)).thenReturn(blockBelow);
    when(blockBelow.getType()).thenReturn(Material.END_STONE);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);
    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(block).setType(Material.AIR),
          () -> verify(childBlock).setType(Material.AIR),
          () -> verify(world).dropItem(any(), any()),
          () -> verify(pluginManagerService).callEvent(any(EntityPickupItemEvent.class))
      );
    }
  }

  @Test
  void onBlockBreakChorusPlantNeighborIsNotChorusPlantSkipsRecursion() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.CHORUS_PLANT);

    Block nonChorusNeighbor = mock(Block.class);
    when(nonChorusNeighbor.getType()).thenReturn(Material.AIR);

    MultipleFacing rootFacing = mock(MultipleFacing.class);
    when(rootFacing.getFaces()).thenReturn(Set.of(BlockFace.UP));
    when(block.getBlockData()).thenReturn(rootFacing);
    when(block.getRelative(BlockFace.UP)).thenReturn(nonChorusNeighbor);

    Block blockBelow = mock(Block.class);
    when(block.getRelative(BlockFace.DOWN)).thenReturn(blockBelow);
    when(blockBelow.getType()).thenReturn(Material.END_STONE);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);
    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      verify(event).setCancelled(true);
    }
  }

  @Test
  void onBlockBreakChorusPlantBlockDataNotMultipleFacingSkipsChildren() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.CHORUS_PLANT);

    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    Block blockBelow = mock(Block.class);
    when(block.getRelative(BlockFace.DOWN)).thenReturn(blockBelow);
    when(blockBelow.getType()).thenReturn(Material.END_STONE);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);
    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(world).dropItem(any(), any())
      );
    }
  }

  @Test
  void onBlockBreakChorusPlantSkipsPreviousFaceDirectionToPreventBacktracking() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.CHORUS_PLANT);

    Block childBlock = mock(Block.class);
    when(childBlock.getType()).thenReturn(Material.CHORUS_PLANT);

    Block grandchildBlock = mock(Block.class);
    when(grandchildBlock.getType()).thenReturn(Material.CHORUS_PLANT);

    MultipleFacing grandchildFacing = mock(MultipleFacing.class);
    when(grandchildFacing.getFaces()).thenReturn(Collections.emptySet());
    when(grandchildBlock.getBlockData()).thenReturn(grandchildFacing);

    MultipleFacing childFacing = mock(MultipleFacing.class);
    when(childFacing.getFaces()).thenReturn(Set.of(BlockFace.WEST, BlockFace.UP));
    when(childBlock.getBlockData()).thenReturn(childFacing);
    when(childBlock.getRelative(BlockFace.WEST)).thenReturn(block);
    when(childBlock.getRelative(BlockFace.UP)).thenReturn(grandchildBlock);

    MultipleFacing rootFacing = mock(MultipleFacing.class);
    when(rootFacing.getFaces()).thenReturn(Set.of(BlockFace.EAST));
    when(block.getBlockData()).thenReturn(rootFacing);
    when(block.getRelative(BlockFace.EAST)).thenReturn(childBlock);

    Block blockBelow = mock(Block.class);
    when(block.getRelative(BlockFace.DOWN)).thenReturn(blockBelow);
    when(blockBelow.getType()).thenReturn(Material.END_STONE);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);

    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(childBlock).setType(Material.AIR),
          () -> verify(grandchildBlock).setType(Material.AIR),
          () -> verify(block, org.mockito.Mockito.times(1)).setType(Material.AIR)
      );
    }
  }

  @Test
  void onBlockBreakChorusPlantDeduplicationPreventsProcessingSameBlockTwice() {
    injectWithBothEnchantments();

    BlockBreakEvent event = buildEvent();
    when(block.getType()).thenReturn(Material.CHORUS_PLANT);

    Block sharedBlock = mock(Block.class);
    when(sharedBlock.getType()).thenReturn(Material.CHORUS_PLANT);

    MultipleFacing sharedFacing = mock(MultipleFacing.class);
    when(sharedFacing.getFaces()).thenReturn(Collections.emptySet());
    when(sharedBlock.getBlockData()).thenReturn(sharedFacing);

    MultipleFacing rootFacing = mock(MultipleFacing.class);
    when(rootFacing.getFaces()).thenReturn(Set.of(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST));
    when(block.getBlockData()).thenReturn(rootFacing);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(sharedBlock);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(sharedBlock);
    when(block.getRelative(BlockFace.EAST)).thenReturn(sharedBlock);

    Block blockBelow = mock(Block.class);
    when(block.getRelative(BlockFace.DOWN)).thenReturn(blockBelow);
    when(blockBelow.getType()).thenReturn(Material.END_STONE);

    World world = mock(World.class);
    Location location = mock(Location.class);
    when(block.getWorld()).thenReturn(world);
    when(block.getLocation()).thenReturn(location);
    Item droppedItem = mock(Item.class);
    when(world.dropItem(any(), any())).thenReturn(droppedItem);
    when(serviceContext.getPluginManagerService()).thenReturn(pluginManagerService);

    try (MockedStatic<EnchantmentHelper> staticMock = mockStatic(EnchantmentHelper.class)) {
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, delicateHelper))
          .thenReturn(false);
      staticMock.when(() -> EnchantmentHelper.hasEnchant(mainHandItem, telekinesisHelper))
          .thenReturn(true);

      listener.onBlockBreak(event);

      assertAll(
          () -> verify(event).setCancelled(true),
          () -> verify(sharedBlock, org.mockito.Mockito.times(1)).setType(Material.AIR)
      );
    }
  }
}