package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static org.mockito.Mockito.*;

class SetCommandTest {

    private Player player;
    private SelectionResolver selectionResolver;
    private UndoHistoryManager undoHistoryManager;
    private SetCommand setCommand;

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

        setCommand = new SetCommand(2, selectionResolver, undoHistoryManager);
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
    }

    @Test
    void execute_withInvalidMaterial_sendsWrongMaterialMessage() {
        String[] args = {"set", "NOT_A_REAL_MATERIAL_XYZ"};

        setCommand.execute(player, args);

        verify(player).sendMessage(anyString());
        verify(selectionResolver, never()).resolve(any());
    }

    @Test
    void execute_withNoSelection_abortsEarly() {
        when(selectionResolver.resolve(player)).thenReturn(null);
        String[] args = {"set", "STONE"};

        setCommand.execute(player, args);

        verify(undoHistoryManager, never()).add(any(), any());
    }

    @Test
    void execute_withValidMaterialAndSelection_processesBlocks() {
        Selection selection = buildSelection(0, 0, 0, 1, 1, 1);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block blockA = mock(Block.class);
        Block blockB = mock(Block.class);
        when(blockA.getType()).thenReturn(Material.AIR);
        when(blockB.getType()).thenReturn(Material.AIR);
        when(blockA.getLocation()).thenReturn(mock(org.bukkit.Location.class));
        when(blockB.getLocation()).thenReturn(mock(org.bukkit.Location.class));
        when(blockA.getBlockData()).thenReturn(mock(org.bukkit.block.data.BlockData.class));
        when(blockB.getBlockData()).thenReturn(mock(org.bukkit.block.data.BlockData.class));

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        java.util.function.Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(blockA);
                        consumer.accept(blockB);
                        return null;
                    });

            modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);
            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.addUndoHistory(any(), any())).thenAnswer(_ -> null);
            setCommand.execute(player, new String[]{"set", "STONE"});

            verify(undoHistoryManager).add(eq(player), argThat(list -> list.size() == 2));
            verify(player).sendMessage((String) null);
        }
    }

    @Test
    void execute_skipsBlocksAlreadyMatchingTargetMaterial() {
        Selection selection = buildSelection(0, 0, 0, 0, 0, 0);
        when(selectionResolver.resolve(player)).thenReturn(selection);

        Block stoneBlock = mock(Block.class);
        when(stoneBlock.getType()).thenReturn(Material.STONE);

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            modifyHelper.when(() -> forEachBlock(eq(selection), any()))
                    .thenAnswer(invocation -> {
                        java.util.function.Consumer<Block> consumer = invocation.getArgument(1);
                        consumer.accept(stoneBlock);
                        return null;
                    });

            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.addUndoHistory(any(), any())).thenAnswer(_ -> null);

            setCommand.execute(player, new String[]{"set", "STONE"});

            verify(undoHistoryManager).add(eq(player), argThat(List::isEmpty));
        }
    }

    @Test
    void matches_withCorrectArgs_returnsTrue() {
        assert setCommand.matches(new String[]{"set", "STONE"});
    }

    @Test
    void matches_withWrongSubCommand_returnsFalse() {
        assert !setCommand.matches(new String[]{"wall", "STONE"});
    }

    @Test
    void matches_withTooFewArgs_returnsFalse() {
        assert !setCommand.matches(new String[]{"set"});
    }

    private Selection buildSelection(int x1, int y1, int z1, int x2, int y2, int z2) {
        org.bukkit.World world = mock(org.bukkit.World.class);
        org.bukkit.Location pos1 = mock(org.bukkit.Location.class);
        org.bukkit.Location pos2 = mock(org.bukkit.Location.class);
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
}