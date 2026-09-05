package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.getModifyClipboardEntry;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.getRelativeCopySelection;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.tasks.BlockService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command implementation that handles copying or cutting a selected region into the player's
 * clipboard.
 *
 * <p>When cutting, each block in the selection is removed from the world and recorded in the undo
 * history.
 * In both cases, the resolved block data is stored relative to the player's current position and
 * saved to the clipboard for later use with a paste operation.
 */
public class CopyCommand implements SubCommand {

  private final boolean isCut;
  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  /**
   * Creates a new {@code CopyCommand} instance configured for either copy or cut behavior.
   *
   * @param isCut         {@code true} if the command should remove blocks from the world after
   *                      copying them, {@code false} for a non-destructive copy
   * @param blocksPerTick the maximum number of blocks processed per server tick during the
   *                      operation
   * @param context       the service context providing access to all required services
   */
  public CopyCommand(boolean isCut, int blocksPerTick, ServiceContext context) {
    this.isCut = isCut;
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = context;
  }

  /**
   * Executes the copy or cut operation for the given player.
   *
   * <p>Resolves the player's current selection and builds a clipboard list of all blocks
   * relative to the player's position. If operating in cut mode, each block is erased from the
   * world, its protection is removed, and the full set of original block states is pushed onto the
   * undo history stack.
   *
   * @param player the player who triggered the command
   * @param args   the command arguments passed to this sub-command
   */
  @Override
  public void execute(Player player, String[] args) {
    Selection selection = serviceContext.getSelectionService().resolve(player);
    if (selection == null) {
      return;
    }

    Location playerTargetLoc = player.getLocation().clone();
    playerTargetLoc.setX(playerTargetLoc.getBlockX());
    playerTargetLoc.setY(playerTargetLoc.getBlockY());
    playerTargetLoc.setZ(playerTargetLoc.getBlockZ());

    Selection newSelection = getRelativeCopySelection(selection, playerTargetLoc);
    List<ModifyClipboardEntry> clipboardList = new ArrayList<>();
    List<ModifyHistoryEntry> history = new ArrayList<>();

    BlockService blockService = new BlockService(serviceContext.getSchedulerService(), Material.AIR,
        serviceContext.getPluginMetadataService().getPlugin().getServer());
    BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);
    forEachBlock(selection, block -> {
      ModifyHistoryEntry historyEntry = new ModifyHistoryEntry(block.getLocation(), block.getType(),
          block.getBlockData());
      ModifyClipboardEntry clipboardEntry = getModifyClipboardEntry(block, player, playerTargetLoc);
      clipboardList.add(clipboardEntry);

      if (isCut) {
        history.add(historyEntry);
        serviceContext.getProtectionService().removeBlockProtectionIfExists(block);
        blockProcessor.process(block, blockService);
      }
    });

    if (isCut) {
      blockService.applyBlocks(0);
      serviceContext.getUndoHistoryService().addHistory(player, history);
    }

    serviceContext.getClipboardService()
        .setClipboard(player, new DoubleStore<>(newSelection, clipboardList));
    player.sendMessage(isCut ? serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_MODIFY_CUT_STARTED, clipboardList.size())
        : serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MODIFY_COPY_STARTED, clipboardList.size()));
  }

  /**
   * Checks whether the provided arguments match the copy or cut sub-command signature.
   *
   * <p>Expects exactly one argument that equals either the copy or cut command name,
   * depending on how this instance was configured.
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if the arguments represent a valid copy or cut command call, {@code false}
   *     otherwise
   */
  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && (isCut ? Modify.Commands.CUT.getName().equalsIgnoreCase(args[0])
        : Modify.Commands.COPY.getName().equalsIgnoreCase(args[0]));
  }
}