package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.Mockito.*;

class UndoCommandTest {

    private Player player;
    private UndoHistoryManager undoHistoryManager;
    private UndoCommand undoCommand;

    private MockedStatic<RelluEssentials> mockedRelluEssentials;
    private MockedStatic<Bukkit> mockedBukkit;
    private MockedStatic<ModifyHelper> mockedModifyHelper;

    private BukkitScheduler schedulerMock;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        undoHistoryManager = mock(UndoHistoryManager.class);

        LanguageHelper languageHelperMock = mock(LanguageHelper.class);
        when(languageHelperMock.getWithPrefix(any())).thenReturn("msg");
        when(languageHelperMock.getWithPrefix(any(), any())).thenReturn("msg");

        RelluEssentials instanceMock = mock(RelluEssentials.class);
        mockedRelluEssentials = mockStatic(RelluEssentials.class);
        mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(instanceMock);
        RelluEssentials.languageHelper = languageHelperMock;

        schedulerMock = mock(BukkitScheduler.class);
        Server serverMock = mock(Server.class);
        when(serverMock.getScheduler()).thenReturn(schedulerMock);

        mockedBukkit = mockStatic(Bukkit.class);
        mockedBukkit.when(Bukkit::getServer).thenReturn(serverMock);

        mockedModifyHelper = mockStatic(ModifyHelper.class);
        mockedModifyHelper.when(() -> ModifyHelper.undo(any())).thenAnswer(_ -> null);

        undoCommand = new UndoCommand(2, undoHistoryManager);
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
        mockedBukkit.close();
        mockedModifyHelper.close();
    }

    @Test
    void execute_withNoHistory_sendsNoHistoryMessage() {
        when(undoHistoryManager.popLastHistory(player)).thenReturn(null);

        undoCommand.execute(player, new String[]{"undo"});

        verify(player).sendMessage(anyString());
        verifyNoInteractions(schedulerMock);
    }

    @Test
    void execute_withEmptyHistory_sendsNoHistoryMessage() {
        when(undoHistoryManager.popLastHistory(player)).thenReturn(List.of());

        undoCommand.execute(player, new String[]{"undo"});

        verify(player).sendMessage(anyString());
        verifyNoInteractions(schedulerMock);
    }

    @Test
    void execute_withHistoryEntries_schedulesTaskForEachEntry() {
        List<ModifyHistoryEntry> history = List.of(
                buildHistoryEntry(),
                buildHistoryEntry(),
                buildHistoryEntry()
        );
        when(undoHistoryManager.popLastHistory(player)).thenReturn(history);

        undoCommand.execute(player, new String[]{"undo"});

        verify(schedulerMock, times(3)).scheduleSyncDelayedTask(
                any(Plugin.class), any(Runnable.class), anyLong()
        );
        verify(player).sendMessage(anyString());
    }

    @Test
    void execute_withMoreEntriesThanBlocksPerTick_incrementsDelay() {
        List<ModifyHistoryEntry> history = List.of(
                buildHistoryEntry(),
                buildHistoryEntry(),
                buildHistoryEntry(),
                buildHistoryEntry()
        );
        when(undoHistoryManager.popLastHistory(player)).thenReturn(history);

        undoCommand.execute(player, new String[]{"undo"});

        verify(schedulerMock, times(4)).scheduleSyncDelayedTask(
                any(Plugin.class), any(Runnable.class), anyLong()
        );
    }

    @Test
    void matches_withCorrectArgs_returnsTrue() {
        assert undoCommand.matches(new String[]{"undo"});
    }

    @Test
    void matches_withWrongCommand_returnsFalse() {
        assert !undoCommand.matches(new String[]{"set"});
    }

    @Test
    void matches_withTooManyArgs_returnsFalse() {
        assert !undoCommand.matches(new String[]{"undo", "extra"});
    }

    private ModifyHistoryEntry buildHistoryEntry() {
        return new ModifyHistoryEntry(
                mock(Location.class),
                Material.STONE,
                mock(BlockData.class)
        );
    }
}