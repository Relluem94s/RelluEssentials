package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.function.Consumer;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection;
import static org.mockito.Mockito.*;

class MoveCommandTest {

    private Player player;
    private SelectionResolver selectionResolver;
    private UndoHistoryManager undoHistoryManager;
    private MoveCommand moveCommand;
    private BukkitScheduler schedulerMock;

    private MockedStatic<RelluEssentials> mockedRelluEssentials;
    private MockedStatic<Bukkit> mockedBukkit;

    @BeforeAll
    static void setUpServer() {
        if (Bukkit.getServer() != null) {
            return;
        }
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

        when(languageHelperMock.getWithPrefix(any(), any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any())).thenReturn("msg");

        schedulerMock = mock(BukkitScheduler.class);
        Server serverMock = mock(Server.class);
        when(serverMock.getScheduler()).thenReturn(schedulerMock);

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(1);
            task.run();
            return null;
        }).when(schedulerMock).runTaskLater(any(Plugin.class), any(Runnable.class), anyLong());

        mockedBukkit = mockStatic(Bukkit.class);
        mockedBukkit.when(Bukkit::getServer).thenReturn(serverMock);
        mockedBukkit.when(Bukkit::getScheduler).thenReturn(schedulerMock);

        moveCommand = new MoveCommand(2, selectionResolver, undoHistoryManager);
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
        mockedBukkit.close();
    }

    @Test
    void execute_withNonIntegerOffset_sendsInvalidCommandMessage() {
        moveCommand.execute(player, new String[]{"move", "notANumber"});

        verify(player).sendMessage(anyString());
        verify(selectionResolver, never()).resolve(any());
    }

    @Test
    void execute_withNoSelection_abortsEarly() {
        when(selectionResolver.resolve(player)).thenReturn(null);

        moveCommand.execute(player, new String[]{"move", "3"});

        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_withValidOffsetAndSelection_movesBlocksAndSavesHistory() {
        Selection selection = buildSelection(0, 0, 0, 2, 2, 2);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block sourceBlock = buildBlock(Material.STONE, 1, 1, 1);
        Block targetBlock = buildBlock(Material.AIR, 2, 1, 1);

        Location sourceLocation = sourceBlock.getLocation();
        Location targetLocation = mock(Location.class);
        when(sourceLocation.clone()).thenReturn(sourceLocation);
        when(sourceLocation.add(any(Vector.class))).thenReturn(targetLocation);
        when(targetLocation.getBlock()).thenReturn(targetBlock);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> playerHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

            playerHelper.when(() -> getPlayerDirection(player)).thenReturn(new Vector(1, 0, 0));

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(sourceBlock);
                        return null;
                    });

            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            moveCommand.execute(player, new String[]{"move", "1"});

            modifyHelper.verify(() -> checkAndRemoveProtection(sourceBlock));
            modifyHelper.verify(() -> checkAndRemoveProtection(targetBlock));
            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 2));
            verify(player).sendMessage((String) null);
        }
    }

    @Test
    void execute_withMultipleBlocksExceedingBlocksPerTick_incrementsDelay() {
        Selection selection = buildSelection(0, 0, 0, 4, 4, 4);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block firstBlock = buildBlock(Material.STONE, 1, 1, 1);
        Block secondBlock = buildBlock(Material.STONE, 2, 1, 1);
        Block thirdBlock = buildBlock(Material.STONE, 3, 1, 1);

        Block firstTarget = buildBlock(Material.AIR, 2, 1, 1);
        Block secondTarget = buildBlock(Material.AIR, 3, 1, 1);
        Block thirdTarget = buildBlock(Material.AIR, 4, 1, 1);

        wireTargetBlock(firstBlock, firstTarget, new Vector(1, 0, 0));
        wireTargetBlock(secondBlock, secondTarget, new Vector(1, 0, 0));
        wireTargetBlock(thirdBlock, thirdTarget, new Vector(1, 0, 0));

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> playerHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

            playerHelper.when(() -> getPlayerDirection(player)).thenReturn(new Vector(1, 0, 0));

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(firstBlock);
                        consumer.accept(secondBlock);
                        consumer.accept(thirdBlock);
                        return null;
                    });

            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            moveCommand.execute(player, new String[]{"move", "1"});

            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 6));
            verify(player).sendMessage((String) null);
        }
    }

    @Test
    void execute_withValidOffsetAndSelection_schedulesOneTaskPerBlock() {
        Selection selection = buildSelection(0, 0, 0, 2, 2, 2);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block sourceBlock = buildBlock(Material.STONE, 1, 1, 1);
        Block targetBlock = buildBlock(Material.AIR, 2, 1, 1);

        wireTargetBlock(sourceBlock, targetBlock, new Vector(1, 0, 0));

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> playerHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

            playerHelper.when(() -> getPlayerDirection(player)).thenReturn(new Vector(1, 0, 0));

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(sourceBlock);
                        return null;
                    });

            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            moveCommand.execute(player, new String[]{"move", "1"});

            verify(schedulerMock, times(1)).runTaskLater(
                    any(Plugin.class), any(Runnable.class), anyLong()
            );
        }
    }

    @Test
    void execute_withMultipleBlocksExceedingBlocksPerTick_schedulesOneTaskPerBlock() {
        Selection selection = buildSelection(0, 0, 0, 4, 4, 4);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block firstBlock = buildBlock(Material.STONE, 1, 1, 1);
        Block secondBlock = buildBlock(Material.STONE, 2, 1, 1);
        Block thirdBlock = buildBlock(Material.STONE, 3, 1, 1);

        Block firstTarget = buildBlock(Material.AIR, 2, 1, 1);
        Block secondTarget = buildBlock(Material.AIR, 3, 1, 1);
        Block thirdTarget = buildBlock(Material.AIR, 4, 1, 1);

        wireTargetBlock(firstBlock, firstTarget, new Vector(1, 0, 0));
        wireTargetBlock(secondBlock, secondTarget, new Vector(1, 0, 0));
        wireTargetBlock(thirdBlock, thirdTarget, new Vector(1, 0, 0));

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class);
             MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> playerHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

            playerHelper.when(() -> getPlayerDirection(player)).thenReturn(new Vector(1, 0, 0));

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(firstBlock);
                        consumer.accept(secondBlock);
                        consumer.accept(thirdBlock);
                        return null;
                    });

            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

            moveCommand.execute(player, new String[]{"move", "1"});

            verify(schedulerMock, times(3)).runTaskLater(
                    any(Plugin.class), any(Runnable.class), anyLong()
            );
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

    private void wireTargetBlock(Block sourceBlock, Block targetBlock, Vector direction) {
        Location sourceLocation = sourceBlock.getLocation();
        Location clonedLocation = mock(Location.class);
        when(sourceLocation.clone()).thenReturn(clonedLocation);
        when(clonedLocation.add(any(Vector.class))).thenReturn(clonedLocation);
        when(clonedLocation.getBlock()).thenReturn(targetBlock);
    }
}