package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
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
import de.relluem94.minecraft.server.spigot.essentials.services.tasks.BlockService;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class ReplaceCommandTest {

  private Player player;
  private SelectionService selectionService;
  private ServiceContext serviceContext;
  private UndoHistoryService undoHistoryService;
  private ReplaceCommand replaceCommand;
  private Server server;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);
    server = mock(Server.class);

    SchedulerService schedulerService = mock(SchedulerService.class);
    TranslationService translationServiceMock = mock(TranslationService.class);
    ProtectionService protectionServiceMock = mock(ProtectionService.class);
    PluginMetadataService pluginMetadataService = mock(PluginMetadataService.class);
    Plugin plugin = mock(Plugin.class);

    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    serviceContext = mock(ServiceContext.class);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);
    when(serviceContext.getProtectionService()).thenReturn(protectionServiceMock);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);

    replaceCommand = new ReplaceCommand(serviceContext, 2) {
      @Override
      protected boolean shareBlockDataType(Material fromMaterial, Material toMaterial) {
        return false;
      }
    };
  }

  @Test
  void execute_withInvalidFromMaterial_sendsWrongMaterialMessage() {
    replaceCommand.execute(player, new String[]{"replace", "INVALID_MATERIAL", "STONE"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withInvalidToMaterial_sendsWrongMaterialMessage() {
    replaceCommand.execute(player, new String[]{"replace", "STONE", "INVALID_MATERIAL"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withBothMaterialsInvalid_sendsWrongMaterialMessage() {
    replaceCommand.execute(player, new String[]{"replace", "INVALID_FROM", "INVALID_TO"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withNullSelection_doesNotAddHistory() {
    when(selectionService.resolve(player)).thenReturn(null);

    replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void execute_withValidMaterialsAndSelection_addsHistoryAndSendsStartedMessage() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block matchingBlock = buildBlock(Material.DIRT, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(matchingBlock);
        return null;
      });

      replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
      verify(player).sendMessage((String) null);
    }
  }

  @Test
  void execute_withBlockAlreadyBeingToMaterial_skipsBlock() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block alreadyToMaterialBlock = buildBlock(Material.STONE, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(alreadyToMaterialBlock);
        return null;
      });

      replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(List::isEmpty));
    }
  }

  @Test
  void execute_withBlockNotMatchingFromMaterial_skipsBlock() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block nonMatchingBlock = buildBlock(Material.GRASS_BLOCK, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(nonMatchingBlock);
        return null;
      });

      replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(List::isEmpty));
    }
  }

  @Test
  void execute_withMultipleBlocks_savesOriginalBlockStateInHistory() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Material originalMaterial = Material.DIRT;
    Block firstBlock = buildBlock(originalMaterial, 0);
    Block secondBlock = buildBlock(originalMaterial, 1);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(firstBlock);
        consumer.accept(secondBlock);
        return null;
      });

      replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

      ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
      verify(undoHistoryService).addHistory(eq(player), historyCaptor.capture());
      List<ModifyHistoryEntry> capturedHistory = historyCaptor.getValue();
      assert capturedHistory.size() == 2;
      assert capturedHistory.getFirst().getMaterial() == originalMaterial;
      assert capturedHistory.getLast().getMaterial() == originalMaterial;
    }
  }

  @Test
  void execute_withMixedBlocks_onlyReplacesMatchingFromMaterialBlocks() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block matchingBlock = buildBlock(Material.DIRT, 0);
    Block nonMatchingBlock = buildBlock(Material.GRASS_BLOCK, 1);
    Block alreadyTargetBlock = buildBlock(Material.STONE, 2);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(matchingBlock);
        consumer.accept(nonMatchingBlock);
        consumer.accept(alreadyTargetBlock);
        return null;
      });

      replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }
  }

  @Test
  void execute_whenBlockDataTypeIsShared_callsApplyMaterial() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block matchingBlock = buildBlock(Material.DIRT, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockService> blockServiceConstruction = mockConstruction(BlockService.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
        Consumer<Block> consumer = invocation.getArgument(1);
        consumer.accept(matchingBlock);
        return null;
      });

      ReplaceCommand commandWithSharedData = new ReplaceCommand(serviceContext, 2) {
        @Override
        protected boolean shareBlockDataType(Material fromMaterial, Material toMaterial) {
          return true;
        }
      };

      commandWithSharedData.execute(player, new String[]{"replace", "DIRT", "STONE"});

      BlockService capturedBlockService = blockServiceConstruction.constructed().getFirst();
      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
      verify(capturedBlockService).applyMaterial(0);
    }
  }

  @Test
  void shareBlockDataType_returnsTrueForSameBlockDataTypeClasses() {
    ReplaceCommand command = new ReplaceCommand(serviceContext, 1) {
      @Override
      protected boolean shareBlockDataType(Material fromMaterial, Material toMaterial) {
        return super.shareBlockDataType(fromMaterial, toMaterial);
      }
    };

    BlockData sharedBlockData = mock(BlockData.class);
    when(server.createBlockData(any(Material.class))).thenReturn(sharedBlockData);

    assert command.shareBlockDataType(Material.STONE, Material.COBBLESTONE);
  }

  @Test
  void shareBlockDataType_returnsFalseForDifferentBlockDataTypeClasses() {
    ReplaceCommand command = new ReplaceCommand(serviceContext, 1) {
      @Override
      protected boolean shareBlockDataType(Material fromMaterial, Material toMaterial) {
        return super.shareBlockDataType(fromMaterial, toMaterial);
      }
    };

    BlockData stoneBlockData = mock(BlockData.class);
    org.bukkit.block.data.Orientable dirtBlockData = mock(org.bukkit.block.data.Orientable.class);

    when(server.createBlockData(Material.STONE)).thenReturn(stoneBlockData);
    when(server.createBlockData(Material.DIRT)).thenReturn(dirtBlockData);

    assert !command.shareBlockDataType(Material.STONE, Material.DIRT);
  }

  @Test
  void matches_withCorrectArgs_returnsTrue() {
    assert replaceCommand.matches(new String[]{"replace", "DIRT", "STONE"});
  }

  @Test
  void matches_withWrongSubCommand_returnsFalse() {
    assert !replaceCommand.matches(new String[]{"set", "DIRT", "STONE"});
  }

  @Test
  void matches_withTooFewArgs_returnsFalse() {
    assert !replaceCommand.matches(new String[]{"replace", "DIRT"});
  }

  @Test
  void matches_withTooManyArgs_returnsFalse() {
    assert !replaceCommand.matches(new String[]{"replace", "DIRT", "STONE", "extra"});
  }

  @Test
  void matches_withNoArgs_returnsFalse() {
    assert !replaceCommand.matches(new String[]{});
  }

  private Block buildBlock(Material material, int x) {
    Block block = mock(Block.class);
    org.bukkit.Location location = mock(org.bukkit.Location.class);
    BlockData blockData = mock(BlockData.class);
    World world = mock(World.class);
    when(block.getType()).thenReturn(material);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getX()).thenReturn(x);
    when(block.getY()).thenReturn(64);
    when(block.getZ()).thenReturn(0);
    when(location.getWorld()).thenReturn(world);
    return block;
  }
}