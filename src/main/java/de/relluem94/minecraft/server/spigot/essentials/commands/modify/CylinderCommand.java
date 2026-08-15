package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CylinderCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  public CylinderCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

  @Override
  public void execute(Player player, String[] args) {
    Material material = Material.getMaterial(args[1].toUpperCase());
    if (material == null) {
      player.sendMessage(
          serviceContext.getTranslationService()
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

    BlockHelper blockHelper = new BlockHelper(material);
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
      blockProcessor.process(block, blockHelper);
    });

    blockHelper.setBlocks(0);
    serviceContext.getUndoHistoryService().addHistory(player, history);
    player.sendMessage(
        serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MODIFY_CYLINDER_STARTED, history.size(),
                material.name()));
  }

  @Override
  public boolean matches(String[] args) {
    return args.length == 2
        && Modify.Commands.CYLINDER.getName().equalsIgnoreCase(args[0]);
  }
}