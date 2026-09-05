package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.isPlantMaterial;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.tasks.BlockService;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command implementation that places a specified plant material on top of solid blocks
 * within the player's current selection.
 *
 * <p>Only blocks that are empty, have a solid block directly below them, and do not already
 * contain the target material are considered for placement. Each placed block is recorded
 * in the undo history, allowing the operation to be reverted.</p>
 */
public class PlantCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  /**
   * Creates a new {@code PlantCommand} with the given service context and batch size.
   *
   * @param serviceContext the context providing access to all required services
   * @param blocksPerTick  the maximum number of blocks to place per scheduler tick
   */
  public PlantCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

  /**
   * Executes the plant command for the given player using the provided arguments.
   *
   * <p>Resolves the target material from {@code args[1]} and validates that it is a plantable
   * material. Iterates over all blocks in the player's selection, placing the material on
   * top of solid blocks that are currently empty. Placement is distributed across scheduler
   * ticks according to the configured blocks-per-tick limit. All affected blocks are saved
   * to the undo history.</p>
   *
   * @param player the player executing the command
   * @param args   the command arguments, where {@code args[1]} is the material name
   */
  @Override
  public void execute(Player player, String[] args) {
    Material material = Material.getMaterial(args[1].toUpperCase());
    if (material == null || !isPlantMaterial(material)) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
      return;
    }

    Selection selection = serviceContext.getSelectionService().resolve(player);
    if (selection == null) {
      return;
    }

    BlockService blockService = new BlockService(serviceContext.getSchedulerService(), material,
        serviceContext.getPluginMetadataService().getPlugin().getServer());
    List<ModifyHistoryEntry> history = new ArrayList<>();

    final long[] currentDelay = {0};
    final int[] counter = {0};

    forEachBlock(selection, block -> {
      Block below = block.getRelative(0, -1, 0);
      if (!below.getType().isSolid()) {
        return;
      }
      if (!block.isEmpty()) {
        return;
      }
      if (material.equals(block.getType())) {
        return;
      }

      serviceContext.getProtectionService().removeBlockProtectionIfExists(block);
      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
      blockService.addLocation(block.getLocation(), currentDelay[0]);
      counter[0]++;
      if (counter[0] >= blocksPerTick) {
        currentDelay[0]++;
        counter[0] = 0;
      }
    });

    blockService.applyBlocks(0);
    serviceContext.getUndoHistoryService().addHistory(player, history);

    player.sendMessage(
        serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MODIFY_PLANT_STARTED, history.size(),
                material.name()));
  }

  /**
   * Returns {@code true} if the given arguments represent a valid plant sub-command invocation.
   *
   * <p>Expects exactly two arguments where the first argument matches the plant command name,
   * case-insensitively.</p>
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if this sub-command should handle the given arguments, {@code false}
   *     otherwise
   */
  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 2 && Modify.Commands.PLANT.getName().equalsIgnoreCase(args[0]);
  }
}