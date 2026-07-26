package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import org.bukkit.Material;
import org.bukkit.World;
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

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static org.mockito.Mockito.*;

class ReplaceCommandTest {

    private Player player;
    private SelectionResolver selectionResolver;
    private UndoHistoryManager undoHistoryManager;
    private ReplaceCommand replaceCommand;
    private MockedStatic<de.relluem94.minecraft.server.spigot.essentials.RelluEssentials> mockedRelluEssentials;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        selectionResolver = mock(SelectionResolver.class);
        undoHistoryManager = mock(UndoHistoryManager.class);

        de.relluem94.minecraft.server.spigot.essentials.RelluEssentials relluEssentialsMock =
                mock(de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.class);
        LanguageHelper languageHelperMock = mock(LanguageHelper.class);

        mockedRelluEssentials = mockStatic(de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.class);
        mockedRelluEssentials.when(de.relluem94.minecraft.server.spigot.essentials.RelluEssentials::getInstance)
                .thenReturn(relluEssentialsMock);
        de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper = languageHelperMock;

        when(languageHelperMock.getWithPrefix(any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any(), any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any(), any(), any(), any())).thenReturn("msg");

        replaceCommand = new ReplaceCommand(2, selectionResolver, undoHistoryManager);
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
    }

    @Test
    void execute_withInvalidFromMaterial_sendsWrongMaterialMessage() {
        replaceCommand.execute(player, new String[]{"replace", "INVALID_MATERIAL", "STONE"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_withInvalidToMaterial_sendsWrongMaterialMessage() {
        replaceCommand.execute(player, new String[]{"replace", "STONE", "INVALID_MATERIAL"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_withBothMaterialsInvalid_sendsWrongMaterialMessage() {
        replaceCommand.execute(player, new String[]{"replace", "INVALID_FROM", "INVALID_TO"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_withNullSelection_doesNotAddHistory() {
        when(selectionResolver.resolve(player)).thenReturn(null);

        replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_withValidMaterialsAndSelection_addsHistoryAndSendsStartedMessage() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block matchingBlock = buildBlock(Material.DIRT, 0, 64, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(matchingBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 1));
            verify(player).sendMessage(anyString());
        }
    }

    @Test
    void execute_withBlockAlreadyBeingToMaterial_skipsBlock() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block alreadyToMaterialBlock = buildBlock(Material.STONE, 0, 64, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(alreadyToMaterialBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

            verify(undoHistoryManager).add(eq(player), argThat(List::isEmpty));
        }
    }

    @Test
    void execute_withBlockNotMatchingFromMaterial_skipsBlock() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block nonMatchingBlock = buildBlock(Material.GRASS_BLOCK, 0, 64, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(nonMatchingBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

            verify(undoHistoryManager).add(eq(player), argThat(List::isEmpty));
        }
    }

    @Test
    void execute_withMultipleBlocks_savesOriginalBlockStateInHistory() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Material originalMaterial = Material.DIRT;
        Block firstBlock = buildBlock(originalMaterial, 0, 64, 0);
        Block secondBlock = buildBlock(originalMaterial, 1, 64, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(firstBlock);
                consumer.accept(secondBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

            ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
            verify(undoHistoryManager).add(eq(player), historyCaptor.capture());
            List<ModifyHistoryEntry> capturedHistory = historyCaptor.getValue();
            assert capturedHistory.size() == 2;
            assert capturedHistory.getFirst().getMaterial() == originalMaterial;
            assert capturedHistory.getLast().getMaterial() == originalMaterial;
        }
    }

    @Test
    void execute_withMixedBlocks_onlyReplacesMatchingFromMaterialBlocks() {
        Selection selection = mock(Selection.class);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block matchingBlock = buildBlock(Material.DIRT, 0, 64, 0);
        Block nonMatchingBlock = buildBlock(Material.GRASS_BLOCK, 1, 64, 0);
        Block alreadyTargetBlock = buildBlock(Material.STONE, 2, 64, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(matchingBlock);
                consumer.accept(nonMatchingBlock);
                consumer.accept(alreadyTargetBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            replaceCommand.execute(player, new String[]{"replace", "DIRT", "STONE"});

            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 1));
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