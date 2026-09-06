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
 * Sub-command implementation that sets all blocks within a player's selection to a specified
 * material. Processes blocks in configurable tick-based batches and records the changes in the undo
 * history.
 */
public class SetCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  /**
   * Creates a new SetCommand with the given service context and batch size.
   *
   * @param serviceContext the context providing access to all required services
   * @param blocksPerTick  the number of blocks to process per server tick
   */
  public SetCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

  /**
   * Executes the set command by replacing all blocks in the player's selection with the specified
   * material. Validates the material and selection before processing. Skips blocks that already
   * match the target material, removes existing protections, records the original block states in
   * the undo history, and notifies the player upon completion.
   *
   * @param player the player who issued the command
   * @param args   the command arguments where {@code args[1]} contains the target material name
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
      if (material.equals(block.getType())) {
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
        .getWithPrefix(MessageKey.COMMAND_MODIFY_SET_STARTED, history.size(), material.name()));
  }

  /**
   * Determines whether the given arguments match the set sub-command. Expects exactly two arguments
   * where the first argument equals the set command name.
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if the arguments represent a valid set sub-command call, {@code false}
   *     otherwise
   */
  @Override
  public boolean matches(String[] args) {
    return args.length == 2 && Modify.Commands.SET.getName().equalsIgnoreCase(args[0]);
  }
}