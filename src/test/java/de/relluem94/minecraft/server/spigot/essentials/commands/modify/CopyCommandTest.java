package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
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

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class CopyCommandTest {


  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private ServiceContext serviceContext;

  private MockedStatic<RelluEssentials> mockedRelluEssentials;
  private RelluEssentials relluEssentialsMock;

  @BeforeAll
  static void setUpServer() throws Exception {
    if (Bukkit.getServer() != null) {
      tearDownServer();
    }
    org.bukkit.Server serverMock = mock(org.bukkit.Server.class);
    org.bukkit.scheduler.BukkitScheduler schedulerMock = mock(
        org.bukkit.scheduler.BukkitScheduler.class);
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
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);

    relluEssentialsMock = mock(RelluEssentials.class);

    mockedRelluEssentials = mockStatic(RelluEssentials.class);
    mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentialsMock);

    TranslationService translationServiceMock = mock(TranslationService.class);
    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    serviceContext = mock(ServiceContext.class);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);

    Location playerLocation = mock(Location.class);
    Location clonedLocation = mock(Location.class);
    when(player.getLocation()).thenReturn(playerLocation);
    when(playerLocation.clone()).thenReturn(clonedLocation);
    when(clonedLocation.getBlockX()).thenReturn(0);
    when(clonedLocation.getBlockY()).thenReturn(0);
    when(clonedLocation.getBlockZ()).thenReturn(0);
  }

  @AfterEach
  void tearDown() {
    mockedRelluEssentials.close();
  }

  @Test
  void execute_copy_withNoSelection_abortsEarly() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    when(selectionService.resolve(player)).thenReturn(null);

    copyCommand.execute(player, new String[]{"copy"});

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_cut_withNoSelection_abortsEarly() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    when(selectionService.resolve(player)).thenReturn(null);

    cutCommand.execute(player, new String[]{"cut"});

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_copy_withValidSelection_storesClipboardAndSendsMessage() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    relluEssentialsMock.clipboard = new HashMap<>();
    Selection selectionMock = mock(Selection.class);
    ModifyClipboardEntry entryMock = mock(ModifyClipboardEntry.class);
    List<ModifyClipboardEntry> clipboardList = List.of(entryMock);
    relluEssentialsMock.clipboard.put(player, new DoubleStore<>(selectionMock, clipboardList));

    Selection selection = buildSelection(0, 0, 0, 1, 1, 1);
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
  void execute_cut_withValidSelection_clearsBlocksAndAddsHistory() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    relluEssentialsMock.clipboard = new HashMap<>();
    Selection selectionMock = mock(Selection.class);
    ModifyClipboardEntry entryMock = mock(ModifyClipboardEntry.class);
    List<ModifyClipboardEntry> clipboardList = List.of(entryMock);
    relluEssentialsMock.clipboard.put(player, new DoubleStore<>(selectionMock, clipboardList));

    Selection selection = buildSelection(0, 0, 0, 1, 1, 1);
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
      modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

      cutCommand.execute(player, new String[]{"cut"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 2));
      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void matches_copy_withCorrectArgs_returnsTrue() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    assert copyCommand.matches(new String[]{"copy"});
  }

  @Test
  void matches_cut_withCorrectArgs_returnsTrue() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    assert cutCommand.matches(new String[]{"cut"});
  }

  @Test
  void matches_copy_withWrongCommand_returnsFalse() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    assert !copyCommand.matches(new String[]{"cut"});
  }

  @Test
  void matches_cut_withWrongCommand_returnsFalse() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    assert !cutCommand.matches(new String[]{"copy"});
  }

  @Test
  void matches_copy_withTooManyArgs_returnsFalse() {
    CopyCommand copyCommand = new CopyCommand(false, 2, serviceContext);
    assert !copyCommand.matches(new String[]{"copy", "extra"});
  }

  @Test
  void matches_cut_withTooManyArgs_returnsFalse() {
    CopyCommand cutCommand = new CopyCommand(true, 2, serviceContext);
    assert !cutCommand.matches(new String[]{"cut", "extra"});
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
}