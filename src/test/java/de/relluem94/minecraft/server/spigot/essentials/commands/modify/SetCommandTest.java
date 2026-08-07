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
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class SetCommandTest {

  private Player player;
  private SelectionService selectionService;
  private UndoHistoryService undoHistoryService;
  private SetCommand setCommand;

  private MockedStatic<RelluEssentials> mockedRelluEssentials;

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
    selectionService = mock(SelectionService.class);
    undoHistoryService = mock(UndoHistoryService.class);

    RelluEssentials relluEssentialsMock = mock(RelluEssentials.class);
    TranslationService translationServiceMock = mock(TranslationService.class);

    mockedRelluEssentials = mockStatic(RelluEssentials.class);
    mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentialsMock);

    when(translationServiceMock.getWithPrefix(any(), any())).thenReturn("msg");
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getSelectionService()).thenReturn(selectionService);
    when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);
    setCommand = new SetCommand(serviceContext, 2);
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
    verify(selectionService, never()).resolve(any());
  }

  @Test
  void execute_withNoSelection_abortsEarly() {
    when(selectionService.resolve(player)).thenReturn(null);
    String[] args = {"set", "STONE"};

    setCommand.execute(player, args);

    verify(undoHistoryService, never()).addHistory(any(), any());
  }

  @Test
  void execute_withValidMaterialAndSelection_processesBlocks() {
    Selection selection = buildSelection(0, 0, 0, 1, 1, 1);
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
    Selection selection = buildSelection(0, 0, 0, 0, 0, 0);
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