package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.getModifyClipboardEntry;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.getRelativeCopySelection;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.ClipboardService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class CopyCommandTest {

  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private ClipboardService clipboardService;
  private ServiceContext serviceContext;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);
    clipboardService = new ClipboardService();
    PluginMetadataService pluginMetadataService = mock(PluginMetadataService.class);
    Plugin plugin = mock(Plugin.class);
    Server server = mock(Server.class);

    TranslationService translationServiceMock = mock(TranslationService.class);
    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    serviceContext = mock(ServiceContext.class);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);
    when(serviceContext.getClipboardService()).thenReturn(clipboardService);

    SchedulerService schedulerService = mock(SchedulerService.class);
    ProtectionService protectionServiceMock = mock(ProtectionService.class);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getProtectionService()).thenReturn(protectionServiceMock);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);

    Location playerLocation = mock(Location.class);
    Location clonedLocation = mock(Location.class);
    when(player.getLocation()).thenReturn(playerLocation);
    when(playerLocation.clone()).thenReturn(clonedLocation);
    when(clonedLocation.getBlockX()).thenReturn(0);
    when(clonedLocation.getBlockY()).thenReturn(0);
    when(clonedLocation.getBlockZ()).thenReturn(0);
  }

  @Test
  void executeCopyWithNoSelectionAbortsEarly() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    when(selectionService.resolve(player)).thenReturn(null);

    copyCommand.execute(player, new String[]{"copy"});

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void executeCutWithNoSelectionAbortsEarly() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    when(selectionService.resolve(player)).thenReturn(null);

    cutCommand.execute(player, new String[]{"cut"});

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void executeCopyWithValidSelectionStoresClipboardAndSendsMessage() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    Selection selectionMock = mock(Selection.class);
    ModifyClipboardEntry entryMock = mock(ModifyClipboardEntry.class);
    List<ModifyClipboardEntry> clipboardList = List.of(entryMock);
    clipboardService.setClipboard(player, new DoubleStore<>(selectionMock, clipboardList));

    Selection selection = buildSelection();
    when(selectionService.resolve(player)).thenReturn(selection);

    Block blockA = buildBlock(Material.STONE, 0, 0, 0);
    Block blockB = buildBlock(Material.DIRT, 1, 1, 1);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(blockA);
            consumer.accept(blockB);
            return null;
          });

      modifyHelper.when(() -> getRelativeCopySelection(any(), any())).thenReturn(selection);
      modifyHelper.when(() -> getModifyClipboardEntry(any(), any(), any()))
          .thenReturn(mock(ModifyClipboardEntry.class));

      copyCommand.execute(player, new String[]{"copy"});

      verify(undoHistoryService, never()).addHistory(any(), any());
      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void executeCutWithValidSelectionClearsBlocksAndAddsHistory() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    Selection selectionMock = mock(Selection.class);
    ModifyClipboardEntry entryMock = mock(ModifyClipboardEntry.class);
    List<ModifyClipboardEntry> clipboardList = List.of(entryMock);
    clipboardService.setClipboard(player, new DoubleStore<>(selectionMock, clipboardList));

    Selection selection = buildSelection();
    when(selectionService.resolve(player)).thenReturn(selection);

    Block blockA = buildBlock(Material.STONE, 0, 0, 0);
    Block blockB = buildBlock(Material.DIRT, 1, 1, 1);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(blockA);
            consumer.accept(blockB);
            return null;
          });

      modifyHelper.when(() -> getRelativeCopySelection(any(), any())).thenReturn(selection);
      modifyHelper.when(() -> getModifyClipboardEntry(any(), any(), any()))
          .thenReturn(mock(ModifyClipboardEntry.class));

      cutCommand.execute(player, new String[]{"cut"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 2));
      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void matchesCopyWithCorrectArgsReturnsTrue() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    assert copyCommand.matches(new String[]{"copy"});
  }

  @Test
  void matchesCutWithCorrectArgsReturnsTrue() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    assert cutCommand.matches(new String[]{"cut"});
  }

  @Test
  void matchesCopyWithWrongCommandReturnsFalse() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    assert !copyCommand.matches(new String[]{"cut"});
  }

  @Test
  void matchesCutWithWrongCommandReturnsFalse() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    assert !cutCommand.matches(new String[]{"copy"});
  }

  @Test
  void matchesCopyWithTooManyArgsReturnsFalse() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    assert !copyCommand.matches(new String[]{"copy", "extra"});
  }

  @Test
  void matchesCutWithTooManyArgsReturnsFalse() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    assert !cutCommand.matches(new String[]{"cut", "extra"});
  }

  private Selection buildSelection() {
    World world = mock(World.class);
    Location pos1 = mock(Location.class);
    Location pos2 = mock(Location.class);
    when(pos1.getWorld()).thenReturn(world);
    when(pos2.getWorld()).thenReturn(world);
    when(pos1.getBlockX()).thenReturn(0);
    when(pos1.getBlockY()).thenReturn(0);
    when(pos1.getBlockZ()).thenReturn(0);
    when(pos2.getBlockX()).thenReturn(1);
    when(pos2.getBlockY()).thenReturn(1);
    when(pos2.getBlockZ()).thenReturn(1);
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
}