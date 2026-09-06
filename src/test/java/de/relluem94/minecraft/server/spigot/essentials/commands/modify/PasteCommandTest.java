package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.getBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.normalizeYaw;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.ClipboardService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

class PasteCommandTest {

  private Player player;
  private UndoHistoryService undoHistoryService;
  private ClipboardService clipboardService;
  private SchedulerService schedulerService;
  private PasteCommand pasteCommand;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    undoHistoryService = mock(UndoHistoryService.class);
    clipboardService = new ClipboardService();
    schedulerService = mock(SchedulerService.class);
    ProtectionService protectionService = mock(ProtectionService.class);

    TranslationService translationServiceMock = mock(TranslationService.class);
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");

    doAnswer(invocation -> {
      Runnable task = invocation.getArgument(0);
      task.run();
      return null;
    }).when(schedulerService).runTaskLater(any(Runnable.class), anyLong());

    Location defaultPlayerLocation = buildPlayerLocation();
    when(player.getLocation()).thenReturn(defaultPlayerLocation);

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);
    when(serviceContext.getClipboardService()).thenReturn(clipboardService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    pasteCommand = new PasteCommand(serviceContext, 2);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void execute_withNullClipboardStore_sendsNoClipboardMessage() {
    pasteCommand.execute(player, new String[]{"paste"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withClipboardStoreHavingNullEntries_sendsNoClipboardMessage() {
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = mock();
    when(clipboardStore.getSecondValue()).thenReturn(null);
    clipboardService.setClipboard(player, clipboardStore);

    pasteCommand.execute(player, new String[]{"paste"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withEmptyClipboard_sendsNoClipboardMessage() {
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = mock();
    when(clipboardStore.getSecondValue()).thenReturn(Collections.emptyList());
    clipboardService.setClipboard(player, clipboardStore);

    pasteCommand.execute(player, new String[]{"paste"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withValidClipboard_addsHistoryAndSendsStartedMessage() {
    ModifyClipboardEntry entry = buildClipboardEntry(Material.STONE, 0);
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = buildClipboardStore(
        List.of(entry));
    clipboardService.setClipboard(player, clipboardStore);

    Block targetBlock = buildBlock(Material.AIR, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(entry), anyFloat(), any())).thenReturn(targetBlock);

      pasteCommand.execute(player, new String[]{"paste"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void execute_withMultipleBlocksExceedingBatchSize_incrementsDelayAfterBatchFills() {
    ModifyClipboardEntry firstEntry = buildClipboardEntry(Material.STONE, 0);
    ModifyClipboardEntry secondEntry = buildClipboardEntry(Material.DIRT, 1);
    ModifyClipboardEntry thirdEntry = buildClipboardEntry(Material.GRASS_BLOCK, 2);

    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore =
        buildClipboardStore(List.of(firstEntry, secondEntry, thirdEntry));
    clipboardService.setClipboard(player, clipboardStore);

    Block firstBlock = buildBlock(Material.AIR, 0);
    Block secondBlock = buildBlock(Material.AIR, 1);
    Block thirdBlock = buildBlock(Material.AIR, 2);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(firstEntry), anyFloat(), any())).thenReturn(firstBlock);
      modifyHelper.when(() -> getBlock(eq(secondEntry), anyFloat(), any())).thenReturn(secondBlock);
      modifyHelper.when(() -> getBlock(eq(thirdEntry), anyFloat(), any())).thenReturn(thirdBlock);

      pasteCommand.execute(player, new String[]{"paste"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 3));
      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void execute_withValidClipboard_savesOriginalBlockStateInHistory() {
    ModifyClipboardEntry entry = buildClipboardEntry(Material.STONE, 0);
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = buildClipboardStore(
        List.of(entry));
    clipboardService.setClipboard(player, clipboardStore);

    Material originalMaterial = Material.DIRT;
    Block targetBlock = buildBlock(originalMaterial, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(entry), anyFloat(), any())).thenReturn(targetBlock);

      pasteCommand.execute(player, new String[]{"paste"});

      ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
      verify(undoHistoryService).addHistory(eq(player), historyCaptor.capture());
      ModifyHistoryEntry savedEntry = historyCaptor.getValue().getFirst();
      assert savedEntry.getMaterial() == originalMaterial;
    }
  }

  @Test
  void execute_withValidClipboard_schedulesOneTaskPerBlock() {
    ModifyClipboardEntry entry = buildClipboardEntry(Material.STONE, 0);
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = buildClipboardStore(
        List.of(entry));
    clipboardService.setClipboard(player, clipboardStore);

    Block targetBlock = buildBlock(Material.AIR, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(entry), anyFloat(), any())).thenReturn(targetBlock);

      pasteCommand.execute(player, new String[]{"paste"});

      verify(schedulerService, times(1)).runTaskLater(any(Runnable.class), anyLong());
    }
  }

  @Test
  void execute_withMultipleBlocksExceedingBatchSize_schedulesOneTaskPerBlock() {
    ModifyClipboardEntry firstEntry = buildClipboardEntry(Material.STONE, 0);
    ModifyClipboardEntry secondEntry = buildClipboardEntry(Material.DIRT, 1);
    ModifyClipboardEntry thirdEntry = buildClipboardEntry(Material.GRASS_BLOCK, 2);

    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore =
        buildClipboardStore(List.of(firstEntry, secondEntry, thirdEntry));
    clipboardService.setClipboard(player, clipboardStore);

    Block firstBlock = buildBlock(Material.AIR, 0);
    Block secondBlock = buildBlock(Material.AIR, 1);
    Block thirdBlock = buildBlock(Material.AIR, 2);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(firstEntry), anyFloat(), any())).thenReturn(firstBlock);
      modifyHelper.when(() -> getBlock(eq(secondEntry), anyFloat(), any())).thenReturn(secondBlock);
      modifyHelper.when(() -> getBlock(eq(thirdEntry), anyFloat(), any())).thenReturn(thirdBlock);

      pasteCommand.execute(player, new String[]{"paste"});

      verify(schedulerService, times(3)).runTaskLater(any(Runnable.class), anyLong());
    }
  }

  @Test
  void matches_withCorrectArgs_returnsTrue() {
    assert pasteCommand.matches(new String[]{"paste"});
  }

  @Test
  void matches_withWrongSubCommand_returnsFalse() {
    assert !pasteCommand.matches(new String[]{"set"});
  }

  @Test
  void matches_withTooManyArgs_returnsFalse() {
    assert !pasteCommand.matches(new String[]{"paste", "extra"});
  }

  @Test
  void matches_withNoArgs_returnsFalse() {
    assert !pasteCommand.matches(new String[]{});
  }

  private Location buildPlayerLocation() {
    World world = mock(World.class);
    Location location = mock(Location.class);
    when(location.getBlockX()).thenReturn(0);
    when(location.getBlockY()).thenReturn(64);
    when(location.getBlockZ()).thenReturn(0);
    when(location.getYaw()).thenReturn((float) 0.0);
    when(location.clone()).thenReturn(location);
    when(location.getWorld()).thenReturn(world);
    return location;
  }

  private Block buildBlock(Material material, int x) {
    Block block = mock(Block.class);
    Location location = mock(Location.class);
    BlockData blockData = mock(BlockData.class);
    when(block.getType()).thenReturn(material);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getX()).thenReturn(x);
    when(block.getY()).thenReturn(64);
    when(block.getZ()).thenReturn(0);
    return block;
  }

  private ModifyClipboardEntry buildClipboardEntry(Material material, int relX) {
    ModifyClipboardEntry entry = mock(ModifyClipboardEntry.class);
    BlockData blockData = mock(BlockData.class);
    Location location = mock(Location.class);
    when(entry.getMaterial()).thenReturn(material);
    when(entry.getData()).thenReturn(blockData);
    when(entry.getLocation()).thenReturn(location);
    when(entry.getLocation().getBlockX()).thenReturn(relX);
    when(entry.getLocation().getBlockY()).thenReturn(0);
    when(entry.getLocation().getBlockZ()).thenReturn(0);
    return entry;
  }

  private DoubleStore<Selection, List<ModifyClipboardEntry>> buildClipboardStore(
      List<ModifyClipboardEntry> entries) {
    DoubleStore<Selection, List<ModifyClipboardEntry>> store = mock();
    when(store.getSecondValue()).thenReturn(entries);
    return store;
  }
}