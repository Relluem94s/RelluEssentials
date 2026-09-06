package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
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

class CylinderCommandTest {

  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private ProtectionService protectionService;
  private CylinderCommand cylinderCommand;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);
    protectionService = mock(ProtectionService.class);
    SchedulerService schedulerService = mock(SchedulerService.class);
    Server server = mock(Server.class);

    TranslationService translationService = mock(TranslationService.class);
    when(translationService.getWithPrefix(any())).thenReturn("msg");
    when(translationService.getWithPrefix(any(), any())).thenReturn("msg");

    PluginMetadataService pluginMetadataService = mock(PluginMetadataService.class);
    Plugin plugin = mock(Plugin.class);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);

    doAnswer(invocation -> {
      Runnable task = invocation.getArgument(0);
      task.run();
      return null;
    }).when(schedulerService).runTaskLater(any(Runnable.class), anyLong());

    cylinderCommand = new CylinderCommand(serviceContext, 2);
  }

  @Test
  void execute_withInvalidMaterial_sendsInvalidMaterialMessage() {
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

  @SuppressWarnings("DataFlowIssue")
  @Test
  void execute_withValidMaterialAndSelection_processesBlocksInsideCylinder() {
    Selection selection = buildSelection(4, 4);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block insideShellBlock = buildBlock(2, 2, 0);
    Block outsideBlock = buildBlock(0, 2, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(insideShellBlock);
            consumer.accept(outsideBlock);
            return null;
          });

      cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

      verify(protectionService).removeBlockProtectionIfExists(insideShellBlock);
      verify(protectionService, never()).removeBlockProtectionIfExists(outsideBlock);
      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
      verify(player).sendMessage((String) null);
    }
  }

  @Test
  void execute_skipsBlocksOutsideCylinderEllipse() {
    Selection selection = buildSelection(4, 4);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block cornerBlock = buildBlock(0, 0, 0);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(cornerBlock);
            return null;
          });

      cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

      verify(protectionService, never()).removeBlockProtectionIfExists(cornerBlock);
      verify(undoHistoryService).addHistory(eq(player), argThat(java.util.List::isEmpty));
    }
  }

  @Test
  void execute_skipsBlocksInsideInnerEllipseHollowCenter() {
    Selection selection = buildSelection(10, 10);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block hollowCenterBlock = buildBlock(5, 2, 5);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(hollowCenterBlock);
            return null;
          });

      cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

      verify(protectionService, never()).removeBlockProtectionIfExists(hollowCenterBlock);
      verify(undoHistoryService).addHistory(eq(player), argThat(java.util.List::isEmpty));
    }
  }

  @Test
  void execute_withRadiusXEqualToOne_skipsInnerEllipseCheck() {
    Selection selection = buildSelection(2, 10);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block shellBlock = buildBlock(1, 2, 5);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(shellBlock);
            return null;
          });

      cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

      verify(protectionService).removeBlockProtectionIfExists(shellBlock);
      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
    }
  }

  @Test
  void execute_withRadiusZEqualToOne_skipsInnerEllipseCheck() {
    Selection selection = buildSelection(10, 2);
    when(selectionService.resolve(player)).thenReturn(selection);

    Block shellBlock = buildBlock(5, 2, 1);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      modifyHelper.when(() -> forEachBlock(eq(selection), any()))
          .thenAnswer(invocation -> {
            Consumer<Block> consumer = invocation.getArgument(1);
            consumer.accept(shellBlock);
            return null;
          });

      cylinderCommand.execute(player, new String[]{"cylinder", "STONE"});

      verify(protectionService).removeBlockProtectionIfExists(shellBlock);
      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 1));
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

  private Selection buildSelection(int x2, int z2) {
    World world = mock(World.class);
    Location pos1 = mock(Location.class);
    Location pos2 = mock(Location.class);
    when(pos1.getWorld()).thenReturn(world);
    when(pos2.getWorld()).thenReturn(world);
    when(pos1.getBlockX()).thenReturn(0);
    when(pos1.getBlockY()).thenReturn(0);
    when(pos1.getBlockZ()).thenReturn(0);
    when(pos2.getBlockX()).thenReturn(x2);
    when(pos2.getBlockY()).thenReturn(4);
    when(pos2.getBlockZ()).thenReturn(z2);
    return new Selection(pos1, pos2);
  }

  private Block buildBlock(int x, int y, int z) {
    Block block = mock(Block.class);
    Location location = mock(Location.class);
    BlockData blockData = mock(BlockData.class);
    when(block.getType()).thenReturn(Material.AIR);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getX()).thenReturn(x);
    when(block.getY()).thenReturn(y);
    when(block.getZ()).thenReturn(z);
    return block;
  }
}