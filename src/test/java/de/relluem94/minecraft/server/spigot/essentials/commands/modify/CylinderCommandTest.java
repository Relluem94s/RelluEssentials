package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
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
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
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

class CylinderCommandTest {

  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private CylinderCommand cylinderCommand;

  private MockedStatic<RelluEssentials> mockedRelluEssentials;

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

    RelluEssentials relluEssentialsMock = mock(RelluEssentials.class);

    mockedRelluEssentials = mockStatic(RelluEssentials.class);
    mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentialsMock);

    TranslationService translationServiceMock = mock(TranslationService.class);
    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);

    cylinderCommand = new CylinderCommand(serviceContext, 2);
  }

  @AfterEach
  void tearDown() {
    mockedRelluEssentials.close();
  }

  @Test
  void execute_withInvalidMaterial_sendsWrongMaterialMessage() {
    cylinderCommand.execute(player, new String[]{"cylinder", "NOT_A_REAL_MATERIAL_XYZ"});

    verify(player).sendMessage(anyString());
    verify(selectionService, never()).resolve(any());
  }

  @Test
  void execute_withNoSelection_abortsEarly() {
    when(selectionService.resolve(player)).thenReturn(null);

    cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_skipsBlocksOutsideCylinderEllipse() {
    Selection selection = buildSelection(0, 0, 0, 4, 4, 4);
    when(selectionService.resolve(player)).thenReturn(selection);

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

      verify(undoHistoryService).addHistory(eq(player), argThat(java.util.List::isEmpty));
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
    when(selectionService.resolve(player)).thenReturn(selection);

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
      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
      verify(player).sendMessage((String) null);
    }
  }

  @Test
  void execute_skipsBlocksInsideInnerEllipseHollowCenter() {
    Selection selection = buildSelection(0, 0, 0, 10, 4, 10);
    when(selectionService.resolve(player)).thenReturn(selection);

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
      verify(undoHistoryService).addHistory(eq(player), argThat(java.util.List::isEmpty));
    }
  }

  @Test
  void execute_withRadiusXEqualToOne_skipsInnerEllipseCheck() {
    Selection selection = buildSelection(0, 0, 0, 2, 4, 10);
    when(selectionService.resolve(player)).thenReturn(selection);

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
      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }
  }

  @Test
  void execute_withRadiusZEqualToOne_skipsInnerEllipseCheck() {
    Selection selection = buildSelection(0, 0, 0, 10, 4, 2);
    when(selectionService.resolve(player)).thenReturn(selection);

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
      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }
  }
}