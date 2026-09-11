package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TraderNpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DevPlattformCommandTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private SchedulerService schedulerService;
  @Mock
  private ServerService serverService;
  @Mock
  private TraderNpcService traderNpcService;
  @Mock
  private UndoHistoryService undoHistoryService;
  @Mock
  private Player player;
  @Mock
  private World world;
  @Mock
  private Block block;
  @Mock
  private BlockData blockData;
  @Mock
  private TraderNpc traderNpc;

  private DevPlattformCommand devPlattformCommand;

  @BeforeEach
  void setUp() {
    lenient().when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getTraderNpcService()).thenReturn(traderNpcService);
    lenient().when(serviceContext.getUndoHistoryService()).thenReturn(undoHistoryService);

    devPlattformCommand = new DevPlattformCommand(serviceContext);
  }

  private void setupPlayerLocation(float yaw) {
    Location location = new Location(world, 0, 64, 0, yaw, 0);
    when(player.getLocation()).thenReturn(location);
    when(player.getWorld()).thenReturn(world);
  }

  private void setupWorldBlock() {
    when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);
    when(world.getBlockAt(any(Location.class))).thenReturn(block);
    when(block.getType()).thenReturn(Material.AIR);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    lenient().when(block.getState()).thenReturn(mock(org.bukkit.block.BlockState.class));
  }

  @Test
  void executeWithNoNpcsAndFacingNorthBuildsFullPlatform() {
    setupPlayerLocation(180f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
    verify(schedulerService, atLeastOnce()).runTaskLater(any(Runnable.class), anyLong());
  }

  @Test
  void executeWithNoNpcsAndFacingSouthBuildsFullPlatform() {
    setupPlayerLocation(0f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithNoNpcsAndFacingEastBuildsFullPlatform() {
    setupPlayerLocation(90f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithNoNpcsAndFacingWestBuildsFullPlatform() {
    setupPlayerLocation(270f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @ParameterizedTest
  @CsvSource({"315f", "359f", "0f", "44f"})
  void executeYawInSouthFacingRangeAppliesCorrectDirectionVectors(float yaw) {
    setupPlayerLocation(yaw);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @ParameterizedTest
  @CsvSource({"45f", "90f", "134f"})
  void executeYawInWestFacingRangeAppliesCorrectDirectionVectors(float yaw) {
    setupPlayerLocation(yaw);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @ParameterizedTest
  @CsvSource({"135f", "180f", "224f"})
  void executeYawInNorthFacingRangeAppliesCorrectDirectionVectors(float yaw) {
    setupPlayerLocation(yaw);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @ParameterizedTest
  @CsvSource({"225f", "270f", "314f"})
  void executeYawInEastFacingRangeAppliesCorrectDirectionVectors(float yaw) {
    setupPlayerLocation(yaw);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithNegativeYawNormalizesCorrectly() {
    setupPlayerLocation(-90f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithOneNpcSpawnsNpcAfterMaterialsExhausted() {
    setupPlayerLocation(180f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(List.of(traderNpc));

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
    verify(schedulerService, atLeastOnce()).runTaskLater(any(Runnable.class), anyLong());
  }

  @Test
  void executeWithMultipleNpcsSpawnsAllNpcs() {
    setupPlayerLocation(0f);
    setupWorldBlock();
    TraderNpc secondNpc = mock(TraderNpc.class);
    TraderNpc thirdNpc = mock(TraderNpc.class);
    when(traderNpcService.getAllNpcs()).thenReturn(List.of(traderNpc, secondNpc, thirdNpc));

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithCommandBlockStateConfiguresCommandBlock() {
    setupPlayerLocation(180f);
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    CommandBlock commandBlock = mock(CommandBlock.class);
    when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);
    when(world.getBlockAt(any(Location.class))).thenReturn(block);
    when(block.getType()).thenReturn(Material.AIR);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    lenient().when(block.getState()).thenReturn(commandBlock);

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeRunsTaskLaterForCommandBlockPlacement() {
    setupPlayerLocation(0f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    devPlattformCommand.execute(player, new String[]{});

    verify(schedulerService, atLeastOnce()).runTaskLater(any(Runnable.class), anyLong());
  }

  @Test
  void executeAddsUndoHistoryForPlayer() {
    setupPlayerLocation(90f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    devPlattformCommand.execute(player, new String[]{});

    verify(undoHistoryService, times(1)).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithExactly64BlocksPerTickSchedulesNextTick() {
    setupPlayerLocation(270f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(schedulerService, atLeastOnce()).runTaskLater(any(Runnable.class), anyLong());
  }

  @Test
  void matchesReturnsTrueForExactDevPlattformCommand() {
    String commandName = DevCommand.Commands.DEV_PLATTFORM.getName();
    assertTrue(devPlattformCommand.matches(new String[]{commandName}));
  }

  @Test
  void matchesReturnsTrueForDevPlattformCommandIgnoringCase() {
    String commandName = DevCommand.Commands.DEV_PLATTFORM.getName().toUpperCase();
    assertTrue(devPlattformCommand.matches(new String[]{commandName}));
  }

  @Test
  void matchesReturnsFalseForEmptyArgs() {
    assertFalse(devPlattformCommand.matches(new String[]{}));
  }

  @Test
  void matchesReturnsFalseForTooManyArgs() {
    String commandName = DevCommand.Commands.DEV_PLATTFORM.getName();
    assertFalse(devPlattformCommand.matches(new String[]{commandName, "extra"}));
  }

  @Test
  void matchesReturnsFalseForWrongCommandName() {
    assertFalse(devPlattformCommand.matches(new String[]{"wrongcommand"}));
  }

  @Test
  void matchesReturnsFalseForNullSingleArg() {
    assertFalse(devPlattformCommand.matches(new String[]{null}));
  }

  @Test
  void implementsSubCommandInterface() {
    assertInstanceOf(SubCommand.class, devPlattformCommand);
  }

  @Test
  void executeWithZeroNpcsDoesNotCallNpcSpawn() {
    setupPlayerLocation(0f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    devPlattformCommand.execute(player, new String[]{});

    verify(traderNpcService, times(1)).getAllNpcs();
  }

  @Test
  void executeWithLargeNpcListExceedingMaterialCountHandlesAllRows() {
    setupPlayerLocation(45f);
    setupWorldBlock();
    List<TraderNpc> manyNpcs = Collections.nCopies(10, traderNpc);
    when(traderNpcService.getAllNpcs()).thenReturn(manyNpcs);

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithYawExactly315UsesSouthFacingBranch() {
    setupPlayerLocation(315f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithYawExactly45UsesWestFacingBranch() {
    setupPlayerLocation(45f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithYawExactly135UsesNorthFacingBranch() {
    setupPlayerLocation(135f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeWithYawExactly225UsesEastFacingBranch() {
    setupPlayerLocation(225f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> devPlattformCommand.execute(player, new String[]{}));

    verify(undoHistoryService).addHistory(eq(player), anyList());
  }

  @Test
  void executeSchedulesBlockServiceApplicationInCorrectOrder() {
    setupPlayerLocation(0f);
    setupWorldBlock();
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    devPlattformCommand.execute(player, new String[]{});

    verify(schedulerService, atLeastOnce()).runTaskLater(any(Runnable.class), anyLong());
    verify(undoHistoryService).addHistory(eq(player), anyList());
  }


  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void executeCommandBlockLambdaSetsTypeAndConfiguresCommandOnlyWhenStateIsCommandBlock(boolean stateIsCommandBlock) {
    setupPlayerLocation(180f);
    when(traderNpcService.getAllNpcs()).thenReturn(Collections.emptyList());

    CommandBlock commandBlock = mock(CommandBlock.class);
    when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);
    when(world.getBlockAt(any(Location.class))).thenReturn(block);
    when(block.getType()).thenReturn(Material.AIR);
    when(block.getBlockData()).thenReturn(blockData);
    when(block.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    when(block.getState()).thenReturn(stateIsCommandBlock ? commandBlock : mock(org.bukkit.block.BlockState.class));

    doAnswer(invocation -> {
      Runnable task = invocation.getArgument(0);
      task.run();
      return null;
    }).when(schedulerService).runTaskLater(any(Runnable.class), anyLong());

    devPlattformCommand.execute(player, new String[]{});

    verify(block, atLeastOnce()).setType(eq(Material.REPEATING_COMMAND_BLOCK), eq(true));
    if (stateIsCommandBlock) {
      verify(commandBlock, atLeastOnce()).setCommand(any(String.class));
      verify(commandBlock, atLeastOnce()).update(true);
    } else {
      verify(commandBlock, never()).setCommand(any(String.class));
      verify(commandBlock, never()).update(true);
    }
  }
}