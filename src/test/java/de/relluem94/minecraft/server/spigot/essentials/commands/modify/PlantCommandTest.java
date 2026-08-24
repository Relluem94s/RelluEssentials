package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.isPlantMaterial;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.minecraft.server.spigot.essentials.services.tasks.BlockService;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class PlantCommandTest {

  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private PlantCommand plantCommand;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);
    ProtectionService protectionService = mock(ProtectionService.class);
    TranslationService translationService = mock(TranslationService.class);
    SchedulerService schedulerService = mock(SchedulerService.class);

    when(translationService.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationService.getWithPrefix(any())).thenReturn("msg");

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);

    plantCommand = new PlantCommand(serviceContext, 2);
  }

  @Test
  void execute_withInvalidMaterialName_sendsWrongMaterialMessage() {
    plantCommand.execute(player, new String[]{"plant", "INVALID_MATERIAL_XYZ"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withNonPlantMaterial_sendsWrongMaterialMessage() {
    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.STONE)).thenReturn(false);

      plantCommand.execute(player, new String[]{"plant", "STONE"});

      verify(player).sendMessage(anyString());
      verify(undoHistoryService, never()).addHistory(any(), any());
    }
  }

  @Test
  void execute_withValidPlantMaterialAndNullSelection_doesNotAddHistory() {
    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> ignored = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
      modifyHelper.when(() -> forEachBlock(any(), any())).thenAnswer(_ -> null);

      when(selectionService.resolve(player)).thenReturn(null);

      plantCommand.execute(player, new String[]{"plant", "DANDELION"});

      verify(undoHistoryService, never()).addHistory(any(), any());
      verify(player, never()).sendMessage(anyString());
    }
  }

  @Test
  void execute_withValidPlantMaterialAndValidSelection_addsHistoryAndSendsStartedMessage() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block block = buildPlantableBlock(Material.AIR, Material.GRASS_BLOCK);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> ignored = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(block);
        return null;
      });

      plantCommand.execute(player, new String[]{"plant", "DANDELION"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
      verify(player).sendMessage((String) null);
    }
  }

  @Test
  void execute_withBlockBelowNotSolid_skipsBlock() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block block = buildBlock(Material.AIR);
    Block below = mock(Block.class, RETURNS_DEEP_STUBS);
    when(below.getType().isSolid()).thenReturn(false);
    when(block.getRelative(0, -1, 0)).thenReturn(below);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> ignored = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(block);
        return null;
      });

      plantCommand.execute(player, new String[]{"plant", "DANDELION"});

      verify(undoHistoryService).addHistory(eq(player), argThat(List::isEmpty));
    }
  }

  @Test
  void execute_withBlockNotEmpty_skipsBlock() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block block = buildBlock(Material.STONE);
    Block below = buildSolidBlock();
    when(block.getRelative(0, -1, 0)).thenReturn(below);
    when(block.isEmpty()).thenReturn(false);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> ignored = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(block);
        return null;
      });

      plantCommand.execute(player, new String[]{"plant", "DANDELION"});

      verify(undoHistoryService).addHistory(eq(player), argThat(List::isEmpty));
    }
  }

  @Test
  void execute_withBlockAlreadyHasTargetMaterial_skipsBlock() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block block = buildBlock(Material.DANDELION);
    Block below = buildSolidBlock();
    when(block.getRelative(0, -1, 0)).thenReturn(below);
    when(block.isEmpty()).thenReturn(true);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> ignored = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(block);
        return null;
      });

      plantCommand.execute(player, new String[]{"plant", "DANDELION"});

      verify(undoHistoryService).addHistory(eq(player), argThat(List::isEmpty));
    }
  }

  @Test
  void execute_withValidClipboard_savesOriginalBlockStateInHistory() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Material originalMaterial = Material.AIR;
    Block block = buildPlantableBlock(originalMaterial, Material.GRASS_BLOCK);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> ignored = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(block);
        return null;
      });

      plantCommand.execute(player, new String[]{"plant", "DANDELION"});

      ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
      verify(undoHistoryService).addHistory(eq(player), historyCaptor.capture());
      ModifyHistoryEntry savedEntry = historyCaptor.getValue().getFirst();
      assert savedEntry.getMaterial() == originalMaterial;
    }
  }

  @Test
  void execute_withMultipleBlocksExceedingBatchSize_incrementsDelayAfterBatchFills() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block firstBlock = buildPlantableBlock(Material.AIR, Material.GRASS_BLOCK);
    Block secondBlock = buildPlantableBlock(Material.AIR, Material.GRASS_BLOCK);
    Block thirdBlock = buildPlantableBlock(Material.AIR, Material.GRASS_BLOCK);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> mockedBlockService = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(firstBlock);
        consumer.accept(secondBlock);
        consumer.accept(thirdBlock);
        return null;
      });

      plantCommand.execute(player, new String[]{"plant", "DANDELION"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 3));

      BlockService capturedBlockService = mockedBlockService.constructed().getFirst();
      verify(capturedBlockService, times(2)).addLocation(any(), eq(0L));
      verify(capturedBlockService).addLocation(any(), eq(1L));
    }
  }

  @Test
  void execute_withValidSelection_callsApplyBlocksOnBlockService() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> mockedBlockService = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(_ -> null);

      plantCommand.execute(player, new String[]{"plant", "DANDELION"});

      BlockService capturedBlockService = mockedBlockService.constructed().getFirst();
      verify(capturedBlockService).applyBlocks(0);
    }
  }

  @Test
  void matches_withCorrectArgs_returnsTrue() {
    assert plantCommand.matches(new String[]{"plant", "DANDELION"});
  }

  @Test
  void matches_withWrongSubCommand_returnsFalse() {
    assert !plantCommand.matches(new String[]{"set", "DANDELION"});
  }

  @Test
  void matches_withTooManyArgs_returnsFalse() {
    assert !plantCommand.matches(new String[]{"plant", "DANDELION", "extra"});
  }

  @Test
  void matches_withTooFewArgs_returnsFalse() {
    assert !plantCommand.matches(new String[]{"plant"});
  }

  @Test
  void matches_withNoArgs_returnsFalse() {
    assert !plantCommand.matches(new String[]{});
  }

  @Test
  void matches_withCaseInsensitiveSubCommand_returnsTrue() {
    assert plantCommand.matches(new String[]{"PLANT", "DANDELION"});
  }

  private Block buildBlock(Material material) {
    Block block = mock(Block.class);
    BlockData blockData = mock(BlockData.class);
    when(block.getType()).thenReturn(material);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getLocation()).thenReturn(mock(org.bukkit.Location.class));
    when(block.isEmpty()).thenReturn(material == Material.AIR);
    return block;
  }

  private Block buildSolidBlock() {
    Block block = mock(Block.class, RETURNS_DEEP_STUBS);
    when(block.getType().isSolid()).thenReturn(true);
    return block;
  }

  private Block buildPlantableBlock(Material blockMaterial, Material belowMaterial) {
    Block block = buildBlock(blockMaterial);
    Block below = mock(Block.class, RETURNS_DEEP_STUBS);
    when(below.getType().isSolid()).thenReturn(true);
    when(block.getRelative(0, -1, 0)).thenReturn(below);
    when(block.isEmpty()).thenReturn(true);
    return block;
  }
}