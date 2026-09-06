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
import org.bukkit.Server;
import org.bukkit.entity.Player;

/**
 * Sub-command implementation that replaces all blocks of a specified material with another material
 * within the player's current selection.
 */
public class ReplaceCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  /**
   * Creates a new ReplaceCommand with the given service context and block processing rate.
   *
   * @param serviceContext the context providing access to all required services
   * @param blocksPerTick  the maximum number of blocks to process per server tick
   */
  public ReplaceCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

  /**
   * Executes the replace operation by iterating over all blocks in the player's selection
   * and replacing every block matching the source material with the target material.
   * Records the affected blocks in the undo history and notifies the player upon completion.
   *
   * @param player the player who issued the command
   * @param args   the command arguments where {@code args[1]} is the source material name
   *               and {@code args[2]} is the target material name
   */
  @Override
  public void execute(Player player, String[] args) {
    Material fromMaterial = Material.getMaterial(args[1].toUpperCase());
    Material toMaterial = Material.getMaterial(args[2].toUpperCase());

    if (fromMaterial == null || toMaterial == null) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
      return;
    }

    Selection selection = serviceContext.getSelectionService().resolve(player);
    if (selection == null) {
      return;
    }

    BlockService blockService = new BlockService(serviceContext.getSchedulerService(), toMaterial,
        serviceContext.getPluginMetadataService().getPlugin().getServer());
    BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);
    List<ModifyHistoryEntry> history = new ArrayList<>();

    forEachBlock(selection, block -> {
      if (block.getType() == toMaterial) {
        return;
      }
      if (block.getType() != fromMaterial) {
        return;
      }

      serviceContext.getProtectionService().removeBlockProtectionIfExists(block);
      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
      blockProcessor.process(block, blockService);
    });

    if (shareBlockDataType(fromMaterial, toMaterial)) {
      blockService.applyMaterial(0);
    } else {
      blockService.applyBlocks(0);
    }

    serviceContext.getUndoHistoryService().addHistory(player, history);
    player.sendMessage(
        serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MODIFY_REPLACE_STARTED, history.size(),
                fromMaterial.name(), toMaterial.name()));
  }

  /**
   * Determines whether two materials share the same {@link org.bukkit.block.data.BlockData} type.
   *
   * @param fromMaterial the source material to compare
   * @param toMaterial   the target material to compare
   * @return {@code true} if both materials produce the same {@code BlockData} implementation,
   *         {@code false} otherwise
   */
  protected boolean shareBlockDataType(Material fromMaterial, Material toMaterial) {
    Server server = serviceContext.getPluginMetadataService().getPlugin().getServer();
    return server.createBlockData(fromMaterial).getClass()
        .equals(server.createBlockData(toMaterial).getClass());
  }

  /**
   * Checks whether the provided arguments match the replace sub-command signature.
   * Expects exactly three arguments where the first argument equals the replace command name.
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if the arguments represent a valid replace command call,
   *         {@code false} otherwise
   */
  @Override
  public boolean matches(String[] args) {
    return args.length == 3
        && Modify.Commands.REPLACE.getName().equalsIgnoreCase(args[0]);
  }
}