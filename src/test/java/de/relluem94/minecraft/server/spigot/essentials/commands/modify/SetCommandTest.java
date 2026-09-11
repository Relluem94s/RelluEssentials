package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
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
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class SetCommandTest {

  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private SetCommand setCommand;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);
    ProtectionService protectionService = mock(ProtectionService.class);
    SchedulerService schedulerService = mock(SchedulerService.class);
    PluginMetadataService pluginMetadataService = mock(PluginMetadataService.class);
    Plugin plugin = mock(Plugin.class);
    Server server = mock(Server.class);

    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);

    TranslationService translationService = mock(TranslationService.class);
    when(translationService.getWithPrefix(any())).thenReturn("msg");
    when(translationService.getWithPrefix(any(), any())).thenReturn("msg");

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);

    doAnswer(invocation -> {
      Runnable task = invocation.getArgument(0);
      task.run();
      return null;
    }).when(schedulerService).runTaskLater(any(Runnable.class), anyLong());

    setCommand = new SetCommand(serviceContext, 2);
  }

  @Test
  void execute_withInvalidMaterial_sendsWrongMaterialMessage() {
    String[] args = {"set", "NOT_A_REAL_MATERIAL_XYZ"};

    setCommand.execute(player, args);

    verify(player).sendMessage(anyString());
    verify(selectionService, never()).resolve(any());
  }

  @Test
  void execute_withNoSelection_abortsEarly() {
    when(selectionService.resolve(player)).thenReturn(null);
    String[] args = {"set", "STONE"};

    setCommand.execute(player, args);

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void execute_withValidMaterialAndSelection_processesBlocks() {
    Selection selection = buildSelection(1, 1, 1);
    when(selectionService.resolve(player)).thenReturn(selection);

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
      setCommand.execute(player, new String[]{"set", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(list -> list.size() == 2));
      verify(player).sendMessage((String) null);
    }
  }

  @Test
  void execute_skipsBlocksAlreadyMatchingTargetMaterial() {
    Selection selection = buildSelection(0, 0, 0);
    when(selectionService.resolve(player)).thenReturn(selection);

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

      setCommand.execute(player, new String[]{"set", "STONE"});

      verify(undoHistoryService).addHistory(eq(player), argThat(List::isEmpty));
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

  private Selection buildSelection(int x2, int y2, int z2) {
    org.bukkit.World world = mock(org.bukkit.World.class);
    org.bukkit.Location pos1 = mock(org.bukkit.Location.class);
    org.bukkit.Location pos2 = mock(org.bukkit.Location.class);
    when(pos1.getWorld()).thenReturn(world);
    when(pos2.getWorld()).thenReturn(world);
    when(pos1.getBlockX()).thenReturn(0);
    when(pos1.getBlockY()).thenReturn(0);
    when(pos1.getBlockZ()).thenReturn(0);
    when(pos2.getBlockX()).thenReturn(x2);
    when(pos2.getBlockY()).thenReturn(y2);
    when(pos2.getBlockZ()).thenReturn(z2);
    return new Selection(pos1, pos2);
  }
}