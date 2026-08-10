package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
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

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

class PasteCommandTest {

  private Player player;
  private UndoHistoryService undoHistoryService;
  private PasteCommand pasteCommand;
  private RelluEssentials relluEssentialsMock;
  private MockedStatic<RelluEssentials> mockedRelluEssentials;
  private BukkitScheduler schedulerMock;
  private MockedStatic<Bukkit> mockedBukkit;

  @BeforeAll
  static void setUpServer() {
    if (Bukkit.getServer() != null) {
      return;
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
    undoHistoryService = mock(UndoHistoryService.class);

    relluEssentialsMock = mock(RelluEssentials.class);
    TranslationService translationServiceMock = mock(TranslationService.class);

    mockedRelluEssentials = mockStatic(RelluEssentials.class);
    mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentialsMock);

    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");

    schedulerMock = mock(BukkitScheduler.class);
    Server serverMock = mock(Server.class);
    when(serverMock.getScheduler()).thenReturn(schedulerMock);

    Map<Player, DoubleStore<Selection, List<ModifyClipboardEntry>>> clipboard = new HashMap<>();
    when(relluEssentialsMock.getClipboard()).thenReturn(clipboard);

    doAnswer(invocation -> {
      Runnable task = invocation.getArgument(1);
      task.run();
      return null;
    }).when(schedulerMock).runTaskLater(any(Plugin.class), any(Runnable.class), anyLong());

    mockedBukkit = mockStatic(Bukkit.class);
    mockedBukkit.when(Bukkit::getServer).thenReturn(serverMock);
    mockedBukkit.when(Bukkit::getScheduler).thenReturn(schedulerMock);

    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    Location defaultPlayerLocation = buildPlayerLocation(0, 64, 0, 0f);
    when(player.getLocation()).thenReturn(defaultPlayerLocation);

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);

    pasteCommand = new PasteCommand(serviceContext, 2);
  }

  @AfterEach
  void tearDown() {
    mockedRelluEssentials.close();
    mockedBukkit.close();
  }

  @Test
  void execute_withNullClipboardStore_sendsNoClipboardMessage() {
    pasteCommand.execute(player, new String[]{"paste"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withClipboardStoreHavingNullEntries_sendsNoClipboardMessage() {
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = mock(DoubleStore.class);
    when(clipboardStore.getSecondValue()).thenReturn(null);
    relluEssentialsMock.getClipboard().put(player, clipboardStore);

    pasteCommand.execute(player, new String[]{"paste"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withEmptyClipboard_sendsNoClipboardMessage() {
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = mock(DoubleStore.class);
    when(clipboardStore.getSecondValue()).thenReturn(Collections.emptyList());
    relluEssentialsMock.getClipboard().put(player, clipboardStore);

    pasteCommand.execute(player, new String[]{"paste"});

    verify(player).sendMessage(anyString());
    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withValidClipboard_addsHistoryAndSendsStartedMessage() {
    ModifyClipboardEntry entry = buildClipboardEntry(Material.STONE, 0, 0, 0);
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = buildClipboardStore(
        List.of(entry));
    relluEssentialsMock.getClipboard().put(player, clipboardStore);

    Block targetBlock = buildBlock(Material.AIR, 0, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(entry), anyFloat(), any())).thenReturn(targetBlock);
      modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

      pasteCommand.execute(player, new String[]{"paste"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void execute_withMultipleBlocksExceedingBatchSize_incrementsDelayAfterBatchFills() {
    ModifyClipboardEntry firstEntry = buildClipboardEntry(Material.STONE, 0, 0, 0);
    ModifyClipboardEntry secondEntry = buildClipboardEntry(Material.DIRT, 1, 0, 0);
    ModifyClipboardEntry thirdEntry = buildClipboardEntry(Material.GRASS_BLOCK, 2, 0, 0);

    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore =
        buildClipboardStore(List.of(firstEntry, secondEntry, thirdEntry));
    relluEssentialsMock.getClipboard().put(player, clipboardStore);

    Block firstBlock = buildBlock(Material.AIR, 0, 64, 0);
    Block secondBlock = buildBlock(Material.AIR, 1, 64, 0);
    Block thirdBlock = buildBlock(Material.AIR, 2, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(firstEntry), anyFloat(), any())).thenReturn(firstBlock);
      modifyHelper.when(() -> getBlock(eq(secondEntry), anyFloat(), any())).thenReturn(secondBlock);
      modifyHelper.when(() -> getBlock(eq(thirdEntry), anyFloat(), any())).thenReturn(thirdBlock);
      modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

      pasteCommand.execute(player, new String[]{"paste"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 3));
      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void execute_withValidClipboard_savesOriginalBlockStateInHistory() {
    ModifyClipboardEntry entry = buildClipboardEntry(Material.STONE, 0, 0, 0);
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = buildClipboardStore(
        List.of(entry));
    relluEssentialsMock.getClipboard().put(player, clipboardStore);

    Material originalMaterial = Material.DIRT;
    Block targetBlock = buildBlock(originalMaterial, 0, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(entry), anyFloat(), any())).thenReturn(targetBlock);
      modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

      pasteCommand.execute(player, new String[]{"paste"});

      ArgumentCaptor<List<ModifyHistoryEntry>> historyCaptor = ArgumentCaptor.captor();
      verify(undoHistoryService).addHistory(eq(player), historyCaptor.capture());
      ModifyHistoryEntry savedEntry = historyCaptor.getValue().getFirst();
      assert savedEntry.getMaterial() == originalMaterial;
    }
  }

  @Test
  void execute_withValidClipboard_schedulesOneTaskPerBlock() {
    ModifyClipboardEntry entry = buildClipboardEntry(Material.STONE, 0, 0, 0);
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = buildClipboardStore(
        List.of(entry));
    relluEssentialsMock.getClipboard().put(player, clipboardStore);

    Block targetBlock = buildBlock(Material.AIR, 0, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(entry), anyFloat(), any())).thenReturn(targetBlock);
      modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

      pasteCommand.execute(player, new String[]{"paste"});

      verify(schedulerMock, times(1)).runTaskLater(
          any(Plugin.class), any(Runnable.class), anyLong()
      );
    }
  }

  @Test
  void execute_withMultipleBlocksExceedingBatchSize_schedulesOneTaskPerBlock() {
    ModifyClipboardEntry firstEntry = buildClipboardEntry(Material.STONE, 0, 0, 0);
    ModifyClipboardEntry secondEntry = buildClipboardEntry(Material.DIRT, 1, 0, 0);
    ModifyClipboardEntry thirdEntry = buildClipboardEntry(Material.GRASS_BLOCK, 2, 0, 0);

    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore =
        buildClipboardStore(List.of(firstEntry, secondEntry, thirdEntry));
    relluEssentialsMock.getClipboard().put(player, clipboardStore);

    Block firstBlock = buildBlock(Material.AIR, 0, 64, 0);
    Block secondBlock = buildBlock(Material.AIR, 1, 64, 0);
    Block thirdBlock = buildBlock(Material.AIR, 2, 64, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> normalizeYaw(anyFloat())).thenReturn(0f);
      modifyHelper.when(() -> getBlock(eq(firstEntry), anyFloat(), any())).thenReturn(firstBlock);
      modifyHelper.when(() -> getBlock(eq(secondEntry), anyFloat(), any())).thenReturn(secondBlock);
      modifyHelper.when(() -> getBlock(eq(thirdEntry), anyFloat(), any())).thenReturn(thirdBlock);
      modifyHelper.when(() -> checkAndRemoveProtection(any())).thenAnswer(_ -> null);

      pasteCommand.execute(player, new String[]{"paste"});

      verify(schedulerMock, times(3)).runTaskLater(
          any(Plugin.class), any(Runnable.class), anyLong()
      );
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

  private Location buildPlayerLocation(int x, int y, int z, float yaw) {
    World world = mock(World.class);
    Location location = mock(Location.class);
    when(location.getBlockX()).thenReturn(x);
    when(location.getBlockY()).thenReturn(y);
    when(location.getBlockZ()).thenReturn(z);
    when(location.getYaw()).thenReturn(yaw);
    when(location.clone()).thenReturn(location);
    when(location.getWorld()).thenReturn(world);
    return location;
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

  private ModifyClipboardEntry buildClipboardEntry(Material material, int relX, int relY,
      int relZ) {
    ModifyClipboardEntry entry = mock(ModifyClipboardEntry.class);
    BlockData blockData = mock(BlockData.class);
    Location location = mock(Location.class);
    when(entry.getMaterial()).thenReturn(material);
    when(entry.getData()).thenReturn(blockData);
    when(entry.getLocation()).thenReturn(location);
    when(entry.getLocation().getBlockX()).thenReturn(relX);
    when(entry.getLocation().getBlockY()).thenReturn(relY);
    when(entry.getLocation().getBlockZ()).thenReturn(relZ);
    return entry;
  }

  private DoubleStore<Selection, List<ModifyClipboardEntry>> buildClipboardStore(
      List<ModifyClipboardEntry> entries) {
    DoubleStore<Selection, List<ModifyClipboardEntry>> store = mock(DoubleStore.class);
    when(store.getSecondValue()).thenReturn(entries);
    return store;
  }
}