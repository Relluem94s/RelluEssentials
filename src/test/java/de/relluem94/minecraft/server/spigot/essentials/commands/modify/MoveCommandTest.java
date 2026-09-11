package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveCommandTest {

  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private ProtectionService protectionService;
  private SchedulerService schedulerService;
  private MoveCommand moveCommand;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);
    protectionService = mock(ProtectionService.class);
    schedulerService = mock(SchedulerService.class);

    TranslationService translationService = mock(TranslationService.class);
    when(translationService.getWithPrefix(any())).thenReturn("msg");
    when(translationService.getWithPrefix(any(), any())).thenReturn("msg");

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    doAnswer(invocation -> {
      Runnable task = invocation.getArgument(0);
      task.run();
      return null;
    }).when(schedulerService).runTaskLater(any(Runnable.class), anyLong());

    moveCommand = new MoveCommand(serviceContext, 2);
  }

  @Test
  void execute_withNonIntegerOffset_sendsInvalidCommandMessage() {
    moveCommand.execute(player, new String[]{"move", "notANumber"});

    verify(player).sendMessage(anyString());
    verify(selectionService, never()).resolve(any());
  }

  @Test
  void execute_withNoSelection_abortsEarly() {
    when(selectionService.resolve(player)).thenReturn(null);

    moveCommand.execute(player, new String[]{"move", "3"});

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void execute_withValidOffsetAndSelection_movesBlocksAndSavesHistory() {
    Selection selection = buildSelection(2, 2, 2);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block sourceBlock = buildBlock(Material.STONE, 1);
    Block targetBlock = buildBlock(Material.AIR, 2);
    wireTargetBlock(sourceBlock, targetBlock);

    try (var modifyHelper = org.mockito.Mockito.mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        var playerHelper = org.mockito.Mockito.mockStatic(
            de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

      playerHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection(player))
          .thenReturn(new Vector(1, 0, 0));

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(sourceBlock);
            return null;
          });

      moveCommand.execute(player, new String[]{"move", "1"});

      verify(protectionService).removeBlockProtectionIfExists(sourceBlock);
      verify(protectionService).removeBlockProtectionIfExists(targetBlock);
      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 2));
      verify(player).sendMessage((String) null);
    }
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void execute_withMultipleBlocksExceedingBlocksPerTick_savesHistoryForAllBlocks() {
    Selection selection = buildSelection(4, 4, 4);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block firstBlock = buildBlock(Material.STONE, 1);
    Block secondBlock = buildBlock(Material.STONE, 2);
    Block thirdBlock = buildBlock(Material.STONE, 3);

    Block firstTarget = buildBlock(Material.AIR, 2);
    Block secondTarget = buildBlock(Material.AIR, 3);
    Block thirdTarget = buildBlock(Material.AIR, 4);

    wireTargetBlock(firstBlock, firstTarget);
    wireTargetBlock(secondBlock, secondTarget);
    wireTargetBlock(thirdBlock, thirdTarget);

    try (var modifyHelper = org.mockito.Mockito.mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        var playerHelper = org.mockito.Mockito.mockStatic(
            de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

      playerHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection(player))
          .thenReturn(new Vector(1, 0, 0));

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(firstBlock);
            consumer.accept(secondBlock);
            consumer.accept(thirdBlock);
            return null;
          });

      moveCommand.execute(player, new String[]{"move", "1"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 6));
      verify(player).sendMessage((String) null);
    }
  }

  @Test
  void execute_withValidOffsetAndSelection_schedulesOneTaskPerBlock() {
    Selection selection = buildSelection(2, 2, 2);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block sourceBlock = buildBlock(Material.STONE, 1);
    Block targetBlock = buildBlock(Material.AIR, 2);
    wireTargetBlock(sourceBlock, targetBlock);

    try (var modifyHelper = org.mockito.Mockito.mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        var playerHelper = org.mockito.Mockito.mockStatic(
            de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

      playerHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection(player))
          .thenReturn(new Vector(1, 0, 0));

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(sourceBlock);
            return null;
          });

      moveCommand.execute(player, new String[]{"move", "1"});

      verify(schedulerService, times(1)).runTaskLater(any(Runnable.class), anyLong());
    }
  }

  @Test
  void execute_withMultipleBlocksExceedingBlocksPerTick_schedulesOneTaskPerBlock() {
    Selection selection = buildSelection(4, 4, 4);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block firstBlock = buildBlock(Material.STONE, 1);
    Block secondBlock = buildBlock(Material.STONE, 2);
    Block thirdBlock = buildBlock(Material.STONE, 3);

    Block firstTarget = buildBlock(Material.AIR, 2);
    Block secondTarget = buildBlock(Material.AIR, 3);
    Block thirdTarget = buildBlock(Material.AIR, 4);

    wireTargetBlock(firstBlock, firstTarget);
    wireTargetBlock(secondBlock, secondTarget);
    wireTargetBlock(thirdBlock, thirdTarget);

    try (var modifyHelper = org.mockito.Mockito.mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        var playerHelper = org.mockito.Mockito.mockStatic(
            de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

      playerHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection(player))
          .thenReturn(new Vector(1, 0, 0));

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(firstBlock);
            consumer.accept(secondBlock);
            consumer.accept(thirdBlock);
            return null;
          });

      moveCommand.execute(player, new String[]{"move", "1"});

      verify(schedulerService, times(3)).runTaskLater(any(Runnable.class), anyLong());
    }
  }

  @Test
  void matches_withCorrectArgs_returnsTrue() {
    assert moveCommand.matches(new String[]{"move", "3"});
  }

  @Test
  void matches_withWrongSubCommand_returnsFalse() {
    assert !moveCommand.matches(new String[]{"set", "3"});
  }

  @Test
  void matches_withTooFewArgs_returnsFalse() {
    assert !moveCommand.matches(new String[]{"move"});
  }

  @Test
  void matches_withTooManyArgs_returnsFalse() {
    assert !moveCommand.matches(new String[]{"move", "3", "extra"});
  }

  private Selection buildSelection(int x2, int y2, int z2) {
    World world = mock(World.class);
    Location pos1 = mock(Location.class);
    Location pos2 = mock(Location.class);
    when(pos1.getWorld()).thenReturn(world);
    when(pos2.getWorld()).thenReturn(world);
    when(pos1.getBlockX()).thenReturn(0);
    when(pos1.getBlockY()).thenReturn(0);
    when(pos1.getBlockZ()).thenReturn(0);
    when(pos2.getBlockX()).thenReturn(x2);
    when(pos2.getBlockY()).thenReturn(y2);
    when(pos2.getBlockZ()).thenReturn(z2);
    return new Selection(pos1, pos2);
  }

  private Block buildBlock(Material material, int x) {
    Block block = mock(Block.class);
    Location location = mock(Location.class);
    BlockData blockData = mock(BlockData.class);
    when(block.getType()).thenReturn(material);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getX()).thenReturn(x);
    when(block.getY()).thenReturn(1);
    when(block.getZ()).thenReturn(1);
    return block;
  }

  private void wireTargetBlock(Block sourceBlock, Block targetBlock) {
    Location sourceLocation = sourceBlock.getLocation();
    Location clonedLocation = mock(Location.class);
    when(sourceLocation.clone()).thenReturn(clonedLocation);
    when(clonedLocation.add(any(Vector.class))).thenReturn(clonedLocation);
    when(clonedLocation.getBlock()).thenReturn(targetBlock);
  }
}