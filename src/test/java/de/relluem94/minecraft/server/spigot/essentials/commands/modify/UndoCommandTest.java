package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class UndoCommandTest {

  private Player player;
  private UndoHistoryService undoHistoryService;
  private UndoCommand undoCommand;

  private MockedStatic<RelluEssentials> mockedRelluEssentials;
  private MockedStatic<Bukkit> mockedBukkit;
  private MockedStatic<ModifyHelper> mockedModifyHelper;

  private BukkitScheduler schedulerMock;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    undoHistoryService = mock(UndoHistoryService.class);

    de.relluem94.minecraft.server.spigot.essentials.RelluEssentials relluEssentialsMock =
        mock(de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.class);
    TranslationService translationServiceMock = mock(TranslationService.class);

    mockedRelluEssentials = mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.class);
    mockedRelluEssentials.when(
            de.relluem94.minecraft.server.spigot.essentials.RelluEssentials::getInstance)
        .thenReturn(relluEssentialsMock);

    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);

    undoCommand = new UndoCommand(serviceContext, 2);
  }

  @AfterEach
  void tearDown() {
    mockedRelluEssentials.close();
    mockedBukkit.close();
    mockedModifyHelper.close();
  }

  @Test
  void execute_withNoHistory_sendsNoHistoryMessage() {
    when(undoHistoryService.popLastHistory(player)).thenReturn(null);

    undoCommand.execute(player, new String[]{"undo"});

    verify(player).sendMessage(anyString());
    verifyNoInteractions(schedulerMock);
  }

  @Test
  void execute_withEmptyHistory_sendsNoHistoryMessage() {
    when(undoHistoryService.popLastHistory(player)).thenReturn(List.of());

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
    when(undoHistoryService.popLastHistory(player)).thenReturn(history);

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
    when(undoHistoryService.popLastHistory(player)).thenReturn(history);

    undoCommand.execute(player, new String[]{"undo"});

    verify(schedulerMock, times(4)).scheduleSyncDelayedTask(
        any(Plugin.class), any(Runnable.class), anyLong()
    );
  }

  @Test
  void execute_withHistoryEntries_callsUndoWithCorrectEntry() {
    ModifyHistoryEntry entry = buildHistoryEntry();
    List<ModifyHistoryEntry> history = List.of(entry);
    when(undoHistoryService.popLastHistory(player)).thenReturn(history);

    doAnswer(invocation -> {
      Runnable task = invocation.getArgument(1);
      task.run();
      return 0;
    }).when(schedulerMock)
        .scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class), anyLong());

    undoCommand.execute(player, new String[]{"undo"});

    mockedModifyHelper.verify(() -> ModifyHelper.undo(entry), times(1));
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