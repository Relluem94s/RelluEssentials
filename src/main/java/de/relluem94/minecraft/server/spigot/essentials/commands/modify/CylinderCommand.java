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
 * Sub-command that fills the outline of a cylinder within the current selection with a specified
 * material. The cylinder shape is derived from the selection bounds, using an elliptical
 * cross-section on the X/Z plane spanning the full height of the selection.
 */
public class CylinderCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  /**
   * Creates a new CylinderCommand with the given service context and block processing rate.
   *
   * @param serviceContext the context providing access to all required services
   * @param blocksPerTick  the number of blocks to process per server tick
   */
  public CylinderCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

  /**
   * Executes the cylinder fill operation for the given player using the provided arguments.
   * Validates the material and selection, computes the elliptical cylinder shell, and schedules
   * block placement while recording the operation in the undo history.
   *
   * @param player the player who issued the command
   * @param args   the command arguments where {@code args[1]} specifies the target material
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

    double centerX = (selection.getMinX() + selection.getMaxX()) / 2.0;
    double centerZ = (selection.getMinZ() + selection.getMaxZ()) / 2.0;
    double radiusX = (selection.getMaxX() - selection.getMinX()) / 2.0;
    double radiusZ = (selection.getMaxZ() - selection.getMinZ()) / 2.0;

    BlockService blockService = new BlockService(serviceContext.getSchedulerService(), material,
        serviceContext.getPluginMetadataService().getPlugin().getServer());
    BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);
    List<ModifyHistoryEntry> history = new ArrayList<>();

    forEachBlock(selection, block -> {
      double normalizedX = (block.getX() - centerX) / radiusX;
      double normalizedZ = (block.getZ() - centerZ) / radiusZ;

      double distanceFromCenter = normalizedX * normalizedX + normalizedZ * normalizedZ;

      double normalizedInnerX = (block.getX() - centerX) / (radiusX - 1);
      double normalizedInnerZ = (block.getZ() - centerZ) / (radiusZ - 1);
      double distanceFromCenterInner =
          normalizedInnerX * normalizedInnerX + normalizedInnerZ * normalizedInnerZ;

      boolean isInsideOuterEllipse = distanceFromCenter <= 1.0;
      boolean isInsideInnerEllipse = radiusX > 1 && radiusZ > 1 && distanceFromCenterInner <= 1.0;

      if (!isInsideOuterEllipse || isInsideInnerEllipse) {
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
        .getWithPrefix(MessageKey.COMMAND_MODIFY_CYLINDER_STARTED, history.size(),
            material.name()));
  }

  /**
   * Determines whether the given arguments match the cylinder sub-command. Expects exactly two
   * arguments with the first matching the cylinder command name.
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if the arguments represent a valid cylinder command call, {@code false}
   *     otherwise
   */
  @Override
  public boolean matches(String[] args) {
    return args.length == 2 && Modify.Commands.CYLINDER.getName().equalsIgnoreCase(args[0]);
  }
}