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
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.minecraft.server.spigot.essentials.services.tasks.BlockService;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.SoundGroup;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);

    SchedulerService schedulerService = mock(SchedulerService.class);
    TranslationService translationServiceMock = mock(TranslationService.class);
    ProtectionService protectionServiceMock = mock(ProtectionService.class);

    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    serviceContext = mock(ServiceContext.class);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);
    when(serviceContext.getProtectionService()).thenReturn(protectionServiceMock);

    replaceCommand = new ReplaceCommand(serviceContext, 2){
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

  @Test
  void execute_withValidMaterialsAndSelection_addsHistoryAndSendsStartedMessage() {
    Selection selection = mock(Selection.class);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block matchingBlock = buildBlock(Material.DIRT, 0, 64, 0);

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

    Block alreadyToMaterialBlock = buildBlock(Material.STONE, 0, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(
            BlockProcessor.class)) {

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

    Block nonMatchingBlock = buildBlock(Material.GRASS_BLOCK, 0, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(
            BlockProcessor.class)) {

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
    Block firstBlock = buildBlock(originalMaterial, 0, 64, 0);
    Block secondBlock = buildBlock(originalMaterial, 1, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(
            BlockProcessor.class)) {

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

    Block matchingBlock = buildBlock(Material.DIRT, 0, 64, 0);
    Block nonMatchingBlock = buildBlock(Material.GRASS_BLOCK, 1, 64, 0);
    Block alreadyTargetBlock = buildBlock(Material.STONE, 2, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
        MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(
            BlockProcessor.class)) {

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

    Block matchingBlock = buildBlock(Material.DIRT, 0, 64, 0);

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

      BlockService capturedBlockService = blockServiceConstruction.constructed().get(0);
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

    BlockData sharedData = mock(BlockData.class);
    Server mockServer = mock(Server.class);

    try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
      mockedBukkit.when(Bukkit::getServer).thenReturn(mockServer);
      mockedBukkit.when(() -> Bukkit.createBlockData(any(Material.class))).thenReturn(sharedData);

      assert command.shareBlockDataType(Material.STONE, Material.COBBLESTONE);
    }
  }


  @Test
  void shareBlockDataType_returnsFalseForDifferentBlockDataTypeClasses() {
    ReplaceCommand command = new ReplaceCommand(serviceContext, 1) {
      @Override
      protected boolean shareBlockDataType(Material fromMaterial, Material toMaterial) {
        return super.shareBlockDataType(fromMaterial, toMaterial);
      }
    };

    BlockData data1 = new BlockData() {

      @Override
      public @NonNull Material getMaterial() {
        return null;
      }

      @Override
      public @NonNull String getAsString() {
        return "";
      }

      @Override
      public @NonNull String getAsString(boolean hideUnspecified) {
        return "";
      }

      @Override
      public @NonNull BlockData merge(@NonNull BlockData data) {
        return null;
      }

      @Override
      public boolean matches(@Nullable BlockData data) {
        return false;
      }

      @Override
      public @NonNull BlockData clone() {
        return null;
      }

      @Override
      public @NonNull SoundGroup getSoundGroup() {
        return null;
      }

      @Override
      public int getLightEmission() {
        return 0;
      }

      @Override
      public boolean isOccluding() {
        return false;
      }

      @Override
      public boolean requiresCorrectToolForDrops() {
        return false;
      }

      @Override
      public boolean isPreferredTool(@NonNull ItemStack tool) {
        return false;
      }

      @Override
      public @NonNull PistonMoveReaction getPistonMoveReaction() {
        return null;
      }

      @Override
      public boolean isSupported(@NonNull Block block) {
        return false;
      }

      @Override
      public boolean isSupported(@NonNull Location location) {
        return false;
      }

      @Override
      public boolean isFaceSturdy(@NonNull BlockFace face, @NonNull BlockSupport support) {
        return false;
      }

      @Override
      public @NonNull Color getMapColor() {
        return null;
      }

      @Override
      public @NonNull Material getPlacementMaterial() {
        return null;
      }

      @Override
      public void rotate(@NonNull StructureRotation rotation) {

      }

      @Override
      public void mirror(@NonNull Mirror mirror) {

      }

      @Override
      public void copyTo(@NonNull BlockData other) {

      }

      @Override
      public @NonNull BlockState createBlockState() {
        return null;
      }
    };

    BlockData data2 = new BlockData() {
      @Override
      public @NonNull Material getMaterial() {
        return null;
      }

      @Override
      public @NonNull String getAsString() {
        return "";
      }

      @Override
      public @NonNull String getAsString(boolean hideUnspecified) {
        return "";
      }

      @Override
      public @NonNull BlockData merge(@NonNull BlockData data) {
        return null;
      }

      @Override
      public boolean matches(@Nullable BlockData data) {
        return false;
      }

      @Override
      public @NonNull BlockData clone() {
        return null;
      }

      @Override
      public @NonNull SoundGroup getSoundGroup() {
        return null;
      }

      @Override
      public int getLightEmission() {
        return 0;
      }

      @Override
      public boolean isOccluding() {
        return false;
      }

      @Override
      public boolean requiresCorrectToolForDrops() {
        return false;
      }

      @Override
      public boolean isPreferredTool(@NonNull ItemStack tool) {
        return false;
      }

      @Override
      public @NonNull PistonMoveReaction getPistonMoveReaction() {
        return null;
      }

      @Override
      public boolean isSupported(@NonNull Block block) {
        return false;
      }

      @Override
      public boolean isSupported(@NonNull Location location) {
        return false;
      }

      @Override
      public boolean isFaceSturdy(@NonNull BlockFace face, @NonNull BlockSupport support) {
        return false;
      }

      @Override
      public @NonNull Color getMapColor() {
        return null;
      }

      @Override
      public @NonNull Material getPlacementMaterial() {
        return null;
      }

      @Override
      public void rotate(@NonNull StructureRotation rotation) {

      }

      @Override
      public void mirror(@NonNull Mirror mirror) {

      }

      @Override
      public void copyTo(@NonNull BlockData other) {

      }

      @Override
      public @NonNull BlockState createBlockState() {
        return null;
      }
    };

    Server mockServer = mock(Server.class);

    try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
      mockedBukkit.when(Bukkit::getServer).thenReturn(mockServer);

      mockedBukkit.when(() -> Bukkit.createBlockData(Material.STONE)).thenReturn(data1);
      mockedBukkit.when(() -> Bukkit.createBlockData(Material.DIRT)).thenReturn(data2);

      boolean result = command.shareBlockDataType(Material.STONE, Material.DIRT);

      if (result) {
        throw new AssertionError("Expected false because data1 and data2 have different classes, but got true");
      }
    }
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

  private Block buildBlock(Material material, int x, int y, int z) {
    Block block = mock(Block.class);
    org.bukkit.Location location = mock(org.bukkit.Location.class);
    BlockData blockData = mock(BlockData.class);
    World world = mock(World.class);
    when(block.getType()).thenReturn(material);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getX()).thenReturn(x);
    when(block.getY()).thenReturn(y);
    when(block.getZ()).thenReturn(z);
    when(location.getWorld()).thenReturn(world);
    return block;
  }
}