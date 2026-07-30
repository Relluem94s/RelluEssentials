package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.function.Consumer;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.*;
import static org.mockito.Mockito.*;

class PlantCommandTest {

    private Player player;
    private SelectionResolver selectionResolver;
    private UndoHistoryManager undoHistoryManager;
    private PlantCommand plantCommand;
    private MockedStatic<RelluEssentials> mockedRelluEssentials;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        selectionResolver = mock(SelectionResolver.class);
        undoHistoryManager = mock(UndoHistoryManager.class);

        LanguageHelper languageHelperMock = mock(LanguageHelper.class);
        when(languageHelperMock.getWithPrefix(any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any(), any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any(), any(), any())).thenReturn("msg");

        mockedRelluEssentials = mockStatic(RelluEssentials.class);
        RelluEssentials.languageHelper = languageHelperMock;

        plantCommand = new PlantCommand(2, selectionResolver, undoHistoryManager);
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
    }

    @Test
    void execute_withInvalidMaterialName_sendsWrongMaterialMessage() {
        plantCommand.execute(player, new String[]{"plant", "INVALID_MATERIAL_XYZ"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_withNonPlantMaterial_sendsWrongMaterialMessage() {
        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.STONE)).thenReturn(false);

            plantCommand.execute(player, new String[]{"plant", "STONE"});

            verify(player).sendMessage(anyString());
            verify(undoHistoryManager, never()).add(any(), any());
        }
    }

    @Test
    void execute_withValidPlantMaterialAndNullSelection_doesNotAddHistory() {
        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
            modifyHelper.when(() -> forEachBlock(any(), any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            when(selectionResolver.resolve(player)).thenReturn(null);

            plantCommand.execute(player, new String[]{"plant", "DANDELION"});

            verify(undoHistoryManager, never()).add(any(), any());
            verify(player, never()).sendMessage(anyString());
        }
    }

    @Test
    void execute_withValidPlantMaterialAndValidSelection_addsHistoryAndSendsStartedMessage() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block block = buildPlantableBlock(Material.AIR, Material.GRASS_BLOCK);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> mockedBlockHelper = mockConstruction(BlockHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(block);
                return null;
            });

            plantCommand.execute(player, new String[]{"plant", "DANDELION"});

            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 1));
            verify(player).sendMessage(anyString());
        }
    }

    @Test
    void execute_withBlockBelowNotSolid_skipsBlock() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block block = buildBlock(Material.AIR);
        Block below = mock(Block.class, RETURNS_DEEP_STUBS);
        when(below.getType().isSolid()).thenReturn(false);
        when(block.getRelative(0, -1, 0)).thenReturn(below);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignored = mockConstruction(BlockHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(block);
                return null;
            });

            plantCommand.execute(player, new String[]{"plant", "DANDELION"});

            verify(undoHistoryManager).add(eq(player), argThat(List::isEmpty));
        }
    }

    @Test
    void execute_withBlockNotEmpty_skipsBlock() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block block = buildBlock(Material.STONE);
        Block below = buildSolidBlock();
        when(block.getRelative(0, -1, 0)).thenReturn(below);
        when(block.isEmpty()).thenReturn(false);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignored = mockConstruction(BlockHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(block);
                return null;
            });

            plantCommand.execute(player, new String[]{"plant", "DANDELION"});

            verify(undoHistoryManager).add(eq(player), argThat(List::isEmpty));
        }
    }

    @Test
    void execute_withBlockAlreadyHasTargetMaterial_skipsBlock() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block block = buildBlock(Material.DANDELION);
        Block below = buildSolidBlock();
        when(block.getRelative(0, -1, 0)).thenReturn(below);
        when(block.isEmpty()).thenReturn(true);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignored = mockConstruction(BlockHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(block);
                return null;
            });

            plantCommand.execute(player, new String[]{"plant", "DANDELION"});

            verify(undoHistoryManager).add(eq(player), argThat(List::isEmpty));
        }
    }

    @Test
    void execute_withValidClipboard_savesOriginalBlockStateInHistory() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Material originalMaterial = Material.AIR;
        Block block = buildPlantableBlock(originalMaterial, Material.GRASS_BLOCK);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignored = mockConstruction(BlockHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(block);
                return null;
            });

            plantCommand.execute(player, new String[]{"plant", "DANDELION"});

            ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
            verify(undoHistoryManager).add(eq(player), historyCaptor.capture());
            ModifyHistoryEntry savedEntry = historyCaptor.getValue().getFirst();
            assert savedEntry.getMaterial() == originalMaterial;
        }
    }

    @Test
    void execute_withMultipleBlocksExceedingBatchSize_incrementsDelayAfterBatchFills() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block firstBlock = buildPlantableBlock(Material.AIR, Material.GRASS_BLOCK);
        Block secondBlock = buildPlantableBlock(Material.AIR, Material.GRASS_BLOCK);
        Block thirdBlock = buildPlantableBlock(Material.AIR, Material.GRASS_BLOCK);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> mockedBlockHelper = mockConstruction(BlockHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(firstBlock);
                consumer.accept(secondBlock);
                consumer.accept(thirdBlock);
                return null;
            });

            plantCommand.execute(player, new String[]{"plant", "DANDELION"});

            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 3));

            BlockHelper capturedBlockHelper = mockedBlockHelper.constructed().getFirst();
            verify(capturedBlockHelper, times(2)).addLocation(any(), eq(0L));
            verify(capturedBlockHelper).addLocation(any(), eq(1L));
        }
    }

    @Test
    void execute_withValidSelection_callsSetBlocksOnBlockHelper() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> mockedBlockHelper = mockConstruction(BlockHelper.class)) {

            modifyHelper.when(() -> isPlantMaterial(Material.DANDELION)).thenReturn(true);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(_ -> null);

            plantCommand.execute(player, new String[]{"plant", "DANDELION"});

            BlockHelper capturedBlockHelper = mockedBlockHelper.constructed().getFirst();
            verify(capturedBlockHelper).setBlocks(0);
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