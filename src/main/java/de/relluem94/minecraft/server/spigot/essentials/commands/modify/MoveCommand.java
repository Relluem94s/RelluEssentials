package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isInt;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MoveCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  public MoveCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!isInt(args[1])) {
      player.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_INVALID));
      return;
    }

    int offset = Integer.parseInt(args[1]);
    Selection selection = serviceContext.getSelectionService().resolve(player);
    if (selection == null) {
      return;
    }

    List<ModifyHistoryEntry> history = new ArrayList<>();
    final long[] currentDelay = {0};
    final int[] counter = {0};

    Vector direction = getPlayerDirection(player).multiply(offset);

    forEachBlock(selection, block -> {
      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));

      Location targetLocation = block.getLocation().clone().add(direction);
      Block targetBlock = targetLocation.getBlock();

      history.add(new ModifyHistoryEntry(targetBlock.getLocation(), targetBlock.getType(),
          targetBlock.getBlockData()));

      serviceContext.getProtectionService().removeBlockProtectionIfExists(block);
      serviceContext.getProtectionService().removeBlockProtectionIfExists(targetBlock);

      serviceContext.getSchedulerService().runTaskLater(() -> {
          targetBlock.setType(block.getType());
          targetBlock.setBlockData(block.getBlockData());
          block.setType(Material.AIR);
        }, currentDelay[0]);

      counter[0]++;
      if (counter[0] >= blocksPerTick) {
        currentDelay[0]++;
        counter[0] = 0;
      }
    });

    serviceContext.getUndoHistoryService().addHistory(player, history);
    player.sendMessage(
        serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MODIFY_MOVE_STARTED, history.size(),
                offset));
  }

  @Override
  public boolean matches(String[] args) {
    return args.length == 2
        && Modify.Commands.MOVE.getName().equalsIgnoreCase(args[0]);
  }
}