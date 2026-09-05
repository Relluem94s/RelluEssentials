package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.tasks.BlockService;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Sub-command implementation that fills only the outer walls of a player's selection with a
 * specified material. Blocks that are not located on the minimum or maximum X or Z boundary of the
 * selection are skipped.
 */
public class WallCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  /**
   * Creates a new WallCommand with the given service context and block processing rate.
   *
   * @param serviceContext the context providing access to all required services
   * @param blocksPerTick  the maximum number of blocks to process per server tick
   */
  public WallCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

  /**
   * Executes the wall-fill operation for the given player using the material specified in the
   * arguments. Only blocks on the outer X and Z boundaries of the player's selection are replaced.
   * The previous block states are saved to the undo history before modification.
   *
   * @param player the player who triggered the command
   * @param args   the command arguments where {@code args[1]} must be a valid
   *               {@link org.bukkit.Material} name
   */
  @Override
  public void execute(Player player, String[] args) {
    Material material = Material.getMaterial(args[1].toUpperCase());
    if (material == null) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
      return;
    }

    Selection selection = serviceContext.getSelectionService().resolve(player);
    if (selection == null) {
      return;
    }

    BlockService blockService = new BlockService(serviceContext.getSchedulerService(), material,
        serviceContext.getPluginMetadataService().getPlugin().getServer());
    BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);
    List<ModifyHistoryEntry> history = new ArrayList<>();

    forEachBlock(selection, block -> {
      int x = block.getX();
      int z = block.getZ();

      if (x != selection.getMinX() && x != selection.getMaxX() && z != selection.getMinZ()
          && z != selection.getMaxZ()) {
        return;
      }

      serviceContext.getProtectionService().removeBlockProtectionIfExists(block);
      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
      blockProcessor.process(block, blockService);
    });

    blockService.applyBlocks(0);
    serviceContext.getUndoHistoryService().addHistory(player, history);
    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_MODIFY_WALL_STARTED, history.size(), material.name()));
  }

  /**
   * Determines whether the given arguments match this sub-command. Matches when exactly two
   * arguments are provided and the first argument equals the wall command name.
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if the arguments represent the wall sub-command, {@code false} otherwise
   */
  @Override
  public boolean matches(String[] args) {
    return args.length == 2 && Modify.Commands.WALL.getName().equalsIgnoreCase(args[0]);
  }
}