package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static org.mockito.Mockito.*;

class WallCommandTest {

    private Player player;
    private SelectionResolver selectionResolver;
    private UndoHistoryManager undoHistoryManager;
    private WallCommand wallCommand;
    private MockedStatic<RelluEssentials> mockedRelluEssentials;
    private MockedStatic<Bukkit> mockedBukkit;

    @BeforeAll
    static void setUpServer() {
        if (Bukkit.getServer() != null) return;
        org.bukkit.Server serverMock = mock(org.bukkit.Server.class);
        org.bukkit.scheduler.BukkitScheduler schedulerMock = mock(org.bukkit.scheduler.BukkitScheduler.class);
        java.util.logging.Logger silentLogger = java.util.logging.Logger.getLogger("test");
        silentLogger.setUseParentHandlers(false);
        silentLogger.setLevel(java.util.logging.Level.OFF);
        when(serverMock.getScheduler()).thenReturn(schedulerMock);
        when(serverMock.getLogger()).thenReturn(silentLogger);
        Bukkit.setServer(serverMock);
    }

    @AfterAll
    static void tearDownServer() throws Exception {
        java.lang.reflect.Field serverField = Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        serverField.set(null, null);
    }

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        selectionResolver = mock(SelectionResolver.class);
        undoHistoryManager = mock(UndoHistoryManager.class);

        RelluEssentials relluEssentialsMock = mock(RelluEssentials.class);
        LanguageHelper languageHelperMock = mock(LanguageHelper.class);

        mockedRelluEssentials = mockStatic(RelluEssentials.class);
        mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentialsMock);
        RelluEssentials.languageHelper = languageHelperMock;

        when(languageHelperMock.getWithPrefix(any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any(), any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any(), any(), any())).thenReturn("msg");

        mockedBukkit = mockStatic(Bukkit.class);

        wallCommand = new WallCommand(2, selectionResolver, undoHistoryManager);
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
        mockedBukkit.close();
    }

    @Test
    void execute_withInvalidMaterial_sendsWrongMaterialMessage() {
        wallCommand.execute(player, new String[]{"wall", "NOT_A_MATERIAL"});

        verify(player).sendMessage(anyString());
        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_withNullSelection_doesNothing() {
        when(selectionResolver.resolve(player)).thenReturn(null);

        wallCommand.execute(player, new String[]{"wall", "STONE"});

        verify(undoHistoryManager, never()).add(any(), any());
        verify(player, never()).sendMessage(anyString());
    }

    @Test
    void execute_withValidMaterialAndSelection_onlyProcessesWallBlocks() {
        Selection selection = buildSelection(0, 64, 0, 2, 66, 2);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block wallBlock = buildBlock(Material.AIR, 0, 64, 0);
        Block innerBlock = buildBlock(Material.AIR, 1, 65, 1);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                java.util.function.Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(wallBlock);
                consumer.accept(innerBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            wallCommand.execute(player, new String[]{"wall", "STONE"});

            ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
            verify(undoHistoryManager).add(eq(player), historyCaptor.capture());
            assert historyCaptor.getValue().size() == 1;
        }
    }

    @Test
    void execute_withValidMaterialAndSelection_savesOriginalBlockStateInHistory() {
        Selection selection = buildSelection(0, 64, 0, 2, 66, 2);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Material originalMaterial = Material.DIRT;
        Block wallBlock = buildBlock(originalMaterial, 0, 64, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                java.util.function.Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(wallBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            wallCommand.execute(player, new String[]{"wall", "STONE"});

            ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
            verify(undoHistoryManager).add(eq(player), historyCaptor.capture());
            ModifyHistoryEntry savedEntry = historyCaptor.getValue().getFirst();
            assert savedEntry.getMaterial() == originalMaterial;
        }
    }

    @Test
    void execute_withAllWallBlocks_addsAllToHistory() {
        Selection selection = buildSelection(0, 64, 0, 2, 66, 2);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block firstWallBlock = buildBlock(Material.AIR, 0, 64, 0);
        Block secondWallBlock = buildBlock(Material.AIR, 2, 65, 0);
        Block thirdWallBlock = buildBlock(Material.AIR, 0, 66, 2);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                java.util.function.Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(firstWallBlock);
                consumer.accept(secondWallBlock);
                consumer.accept(thirdWallBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            wallCommand.execute(player, new String[]{"wall", "STONE"});

            ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
            verify(undoHistoryManager).add(eq(player), historyCaptor.capture());
            assert historyCaptor.getValue().size() == 3;
        }
    }

    @Test
    void execute_withValidMaterialAndSelection_sendsStartedMessage() {
        Selection selection = buildSelection(0, 64, 0, 2, 66, 2);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            wallCommand.execute(player, new String[]{"wall", "STONE"});

            verify(player).sendMessage(anyString());
        }
    }

    @Test
    void execute_withInnerBlockOnly_addsNothingToHistory() {
        Selection selection = buildSelection(0, 64, 0, 4, 66, 4);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block innerBlock = buildBlock(Material.AIR, 2, 65, 2);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                java.util.function.Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(innerBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            wallCommand.execute(player, new String[]{"wall", "STONE"});

            ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
            verify(undoHistoryManager).add(eq(player), historyCaptor.capture());
            assert historyCaptor.getValue().isEmpty();
        }
    }

    @Test
    void execute_withBlockOnMinZWall_addsToHistory() {
        Selection selection = buildSelection(0, 64, 0, 4, 66, 4);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block minZWallBlock = buildBlock(Material.AIR, 2, 65, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                java.util.function.Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(minZWallBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            wallCommand.execute(player, new String[]{"wall", "STONE"});

            ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
            verify(undoHistoryManager).add(eq(player), historyCaptor.capture());
            assert historyCaptor.getValue().size() == 1;
        }
    }

    @Test
    void execute_withBlockOnMaxZWall_addsToHistory() {
        Selection selection = buildSelection(0, 64, 0, 4, 66, 4);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block maxZWallBlock = buildBlock(Material.AIR, 2, 65, 4);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedConstruction<BlockHelper> ignoredBlockHelper = mockConstruction(BlockHelper.class);
             MockedConstruction<BlockProcessor> ignoredBlockProcessor = mockConstruction(BlockProcessor.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any())).thenAnswer(invocation -> {
                java.util.function.Consumer<Block> consumer = invocation.getArgument(1);
                consumer.accept(maxZWallBlock);
                return null;
            });
            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            wallCommand.execute(player, new String[]{"wall", "STONE"});

            ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
            verify(undoHistoryManager).add(eq(player), historyCaptor.capture());
            assert historyCaptor.getValue().size() == 1;
        }
    }

    @Test
    void matches_withCorrectArgs_returnsTrue() {
        assert wallCommand.matches(new String[]{"wall", "STONE"});
    }

    @Test
    void matches_withWrongSubCommand_returnsFalse() {
        assert !wallCommand.matches(new String[]{"set", "STONE"});
    }

    @Test
    void matches_withTooManyArgs_returnsFalse() {
        assert !wallCommand.matches(new String[]{"wall", "STONE", "extra"});
    }

    @Test
    void matches_withTooFewArgs_returnsFalse() {
        assert !wallCommand.matches(new String[]{"wall"});
    }

    @Test
    void matches_withNoArgs_returnsFalse() {
        assert !wallCommand.matches(new String[]{});
    }

    private Selection buildSelection(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        Selection selection = mock(Selection.class);
        when(selection.getMinX()).thenReturn(minX);
        when(selection.getMinY()).thenReturn(minY);
        when(selection.getMinZ()).thenReturn(minZ);
        when(selection.getMaxX()).thenReturn(maxX);
        when(selection.getMaxY()).thenReturn(maxY);
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