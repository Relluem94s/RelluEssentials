package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.function.Consumer;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static org.mockito.Mockito.*;

class CylinderCommandTest {

    private Player player;
    private SelectionResolver selectionResolver;
    private UndoHistoryManager undoHistoryManager;
    private CylinderCommand cylinderCommand;

    private MockedStatic<RelluEssentials> mockedRelluEssentials;

    @BeforeAll
    static void setUpServer(){
        if(Bukkit.getServer() != null){
            return;
        }
        org.bukkit.Server serverMock = mock(org.bukkit.Server.class);
        org.bukkit.scheduler.BukkitScheduler schedulerMock = mock(org.bukkit.scheduler.BukkitScheduler.class);
        java.util.logging.Logger silentLogger = java.util.logging.Logger.getLogger("test");
        silentLogger.setUseParentHandlers(false);
        silentLogger.setLevel(java.util.logging.Level.OFF);
        when(serverMock.getScheduler()).thenReturn(schedulerMock);
        when(serverMock.getLogger()).thenReturn(silentLogger);
        org.bukkit.Bukkit.setServer(serverMock);
    }

    @AfterAll
    static void tearDownServer() throws Exception {
        java.lang.reflect.Field serverField = org.bukkit.Bukkit.class.getDeclaredField("server");
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

        when(languageHelperMock.getWithPrefix(any(), any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any())).thenReturn("msg");

        cylinderCommand = new CylinderCommand(2, selectionResolver, undoHistoryManager);
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
    }

    @Test
    void execute_withInvalidMaterial_sendsWrongMaterialMessage() {
        cylinderCommand.execute(player, new String[]{"cylinder", "NOT_A_REAL_MATERIAL_XYZ"});

        verify(player).sendMessage(anyString());
        verify(selectionResolver, never()).resolve(any());
    }

    @Test
    void execute_withNoSelection_abortsEarly() {
        when(selectionResolver.resolve(player)).thenReturn(null);

        cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_skipsBlocksOutsideCylinderEllipse() {
        Selection selection = buildSelection(0, 0, 0, 4, 4, 4);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block cornerBlock = buildBlock(Material.AIR, 0, 0, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(cornerBlock);
                        return null;
                    });

            cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

            verify(undoHistoryManager).add(eq(player), argThat(java.util.List::isEmpty));
        }
    }

    @Test
    void matches_withCorrectArgs_returnsTrue() {
        assert cylinderCommand.matches(new String[]{"cylinder", "STONE"});
    }

    @Test
    void matches_withWrongSubCommand_returnsFalse() {
        assert !cylinderCommand.matches(new String[]{"set", "STONE"});
    }

    @Test
    void matches_withTooFewArgs_returnsFalse() {
        assert !cylinderCommand.matches(new String[]{"cylinder"});
    }

    @Test
    void matches_withTooManyArgs_returnsFalse() {
        assert !cylinderCommand.matches(new String[]{"cylinder", "STONE", "extra"});
    }

    private Selection buildSelection(int x1, int y1, int z1, int x2, int y2, int z2) {
        World world = mock(World.class);
        Location pos1 = mock(Location.class);
        Location pos2 = mock(Location.class);
        when(pos1.getWorld()).thenReturn(world);
        when(pos2.getWorld()).thenReturn(world);
        when(pos1.getBlockX()).thenReturn(x1);
        when(pos1.getBlockY()).thenReturn(y1);
        when(pos1.getBlockZ()).thenReturn(z1);
        when(pos2.getBlockX()).thenReturn(x2);
        when(pos2.getBlockY()).thenReturn(y2);
        when(pos2.getBlockZ()).thenReturn(z2);
        return new Selection(pos1, pos2);
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

    @Test
    void execute_withValidMaterialAndSelection_processesBlocksInsideCylinder() {
        Selection selection = buildSelection(0, 0, 0, 4, 4, 4);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block insideShellBlock = buildBlock(Material.AIR, 2, 2, 0);
        Block outsideBlock = buildBlock(Material.AIR, 0, 2, 0);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(insideShellBlock);
                        consumer.accept(outsideBlock);
                        return null;
                    });

            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

            modifyHelper.verify(() -> checkAndRemoveProtection(insideShellBlock));
            modifyHelper.verify(() -> checkAndRemoveProtection(outsideBlock), never());
            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 1));
            verify(player).sendMessage((String) null);
        }
    }

    @Test
    void execute_skipsBlocksInsideInnerEllipseHollowCenter() {
        Selection selection = buildSelection(0, 0, 0, 10, 4, 10);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block hollowCenterBlock = buildBlock(Material.AIR, 5, 2, 5);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(hollowCenterBlock);
                        return null;
                    });

            cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

            modifyHelper.verify(() -> checkAndRemoveProtection(hollowCenterBlock), never());
            verify(undoHistoryManager).add(eq(player), argThat(java.util.List::isEmpty));
        }
    }

    @Test
    void execute_withRadiusXEqualToOne_skipsInnerEllipseCheck() {
        Selection selection = buildSelection(0, 0, 0, 2, 4, 10);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block shellBlock = buildBlock(Material.AIR, 1, 2, 5);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(shellBlock);
                        return null;
                    });

            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

            modifyHelper.verify(() -> checkAndRemoveProtection(shellBlock));
            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 1));
        }
    }

    @Test
    void execute_withRadiusZEqualToOne_skipsInnerEllipseCheck() {
        Selection selection = buildSelection(0, 0, 0, 10, 4, 2);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block shellBlock = buildBlock(Material.AIR, 5, 2, 1);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(shellBlock);
                        return null;
                    });

            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

            modifyHelper.verify(() -> checkAndRemoveProtection(shellBlock));
            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 1));
        }
    }
}