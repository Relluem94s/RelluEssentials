package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class WallCommandTest {

  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private WallCommand wallCommand;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);
    Server server = mock(Server.class);

    SchedulerService schedulerService = mock(SchedulerService.class);
    TranslationService translationService = mock(TranslationService.class);
    ProtectionService protectionService = mock(ProtectionService.class);
    PluginMetadataService pluginMetadataService = mock(PluginMetadataService.class);
    Plugin plugin = mock(Plugin.class);

    when(translationService.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationService.getWithPrefix(any(), any(), any())).thenReturn("msg");
    when(translationService.getWithPrefix(any())).thenReturn("msg");
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);

    wallCommand = new WallCommand(serviceContext, 2);
  }

  @Test
  void executeWithInvalidMaterialSendsWrongMaterialMessage() {
    wallCommand.execute(player, new String[]{"wall", "NOTA_MATERIAL"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void executeWithNullSelectionDoesNothing() {
    when(selectionService.resolve(player)).thenReturn(null);

    wallCommand.execute(player, new String[]{"wall", "STONE"});

    verify(undoHistoryService, never()).addHistory(any(), any());
    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void executeWithValidMaterialAndSelectionOnlyProcessesWallBlocks() {
    Selection selection = buildSelection(2, 2);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block wallBlock = buildBlock(Material.AIR, 0, 64, 0);
    Block innerBlock = buildBlock(Material.AIR, 1, 65, 1);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(wallBlock);
            consumer.accept(innerBlock);
            return null;
          });

      wallCommand.execute(player, new String[]{"wall", "STONE"});

      ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
      verify(undoHistoryService).addHistory(eq(player), historyCaptor.capture());
      assert historyCaptor.getValue().size() == 1;
    }
  }

  @Test
  void executeWithValidMaterialAndSelectionSavesOriginalBlockStateInHistory() {
    Selection selection = buildSelection(2, 2);
    when(selectionService.resolve(player)).thenReturn(selection);

    Material originalMaterial = Material.DIRT;
    Block wallBlock = buildBlock(originalMaterial, 0, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(wallBlock);
            return null;
          });

      wallCommand.execute(player, new String[]{"wall", "STONE"});

      ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
      verify(undoHistoryService).addHistory(eq(player), historyCaptor.capture());
      ModifyHistoryEntry savedEntry = historyCaptor.getValue().getFirst();
      assert savedEntry.getMaterial() == originalMaterial;
    }
  }

  @Test
  void executeWithAllWallBlocksAddsAllToHistory() {
    Selection selection = buildSelection(2, 2);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block firstWallBlock = buildBlock(Material.AIR, 0, 64, 0);
    Block secondWallBlock = buildBlock(Material.AIR, 2, 65, 0);
    Block thirdWallBlock = buildBlock(Material.AIR, 0, 66, 2);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(firstWallBlock);
            consumer.accept(secondWallBlock);
            consumer.accept(thirdWallBlock);
            return null;
          });

      wallCommand.execute(player, new String[]{"wall", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 3));
    }
  }

  @Test
  void executeWithValidMaterialAndSelectionSendsStartedMessage() {
    Selection selection = buildSelection(2, 2);
    when(selectionService.resolve(player)).thenReturn(selection);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(_ -> null);

      wallCommand.execute(player, new String[]{"wall", "STONE"});

      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void executeWithInnerBlockOnlyAddsNothingToHistory() {
    Selection selection = buildSelection(4, 4);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block innerBlock = buildBlock(Material.AIR, 2, 65, 2);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(innerBlock);
            return null;
          });

      wallCommand.execute(player, new String[]{"wall", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(List::isEmpty));
    }
  }

  @Test
  void executeWithBlockOnMinZWallAddsToHistory() {
    Selection selection = buildSelection(4, 4);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block minZWallBlock = buildBlock(Material.AIR, 2, 65, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(minZWallBlock);
            return null;
          });

      wallCommand.execute(player, new String[]{"wall", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }
  }

  @Test
  void executeWithBlockOnMaxZWallAddsToHistory() {
    Selection selection = buildSelection(4, 4);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block maxZWallBlock = buildBlock(Material.AIR, 2, 65, 4);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() ->
              de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(maxZWallBlock);
            return null;
          });

      wallCommand.execute(player, new String[]{"wall", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }
  }

  @Test
  void matchesWithCorrectArgsReturnsTrue() {
    assert wallCommand.matches(new String[]{"wall", "STONE"});
  }

  @Test
  void matchesWithWrongSubCommandReturnsFalse() {
    assert !wallCommand.matches(new String[]{"set", "STONE"});
  }

  @Test
  void matchesWithTooManyArgsReturnsFalse() {
    assert !wallCommand.matches(new String[]{"wall", "STONE", "extra"});
  }

  @Test
  void matchesWithTooFewArgsReturnsFalse() {
    assert !wallCommand.matches(new String[]{"wall"});
  }

  @Test
  void matchesWithNoArgsReturnsFalse() {
    assert !wallCommand.matches(new String[]{});
  }

  private Selection buildSelection(int maxX, int maxZ) {
    Selection selection = mock(Selection.class);
    when(selection.getMinX()).thenReturn(0);
    when(selection.getMinY()).thenReturn(64);
    when(selection.getMinZ()).thenReturn(0);
    when(selection.getMaxX()).thenReturn(maxX);
    when(selection.getMaxY()).thenReturn(66);
    when(selection.getMaxZ()).thenReturn(maxZ);
    return selection;
  }

  private Block buildBlock(Material material, int x, int y, int z) {
    Block block = mock(Block.class);
    Location location = mock(Location.class);
    BlockData blockData = mock(BlockData.class);
    when(block.getType()).thenReturn(material);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getX()).thenReturn(x);
    when(block.getY()).thenReturn(y);
    when(block.getZ()).thenReturn(z);
    return block;
  }
}