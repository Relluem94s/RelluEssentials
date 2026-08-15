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

public class ReplaceCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  public ReplaceCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

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

    BlockHelper blockHelper = new BlockHelper(toMaterial);
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
      blockProcessor.process(block, blockHelper);
    });

    blockHelper.setBlocks(0);
    serviceContext.getUndoHistoryService().addHistory(player, history);
    player.sendMessage(
        serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MODIFY_REPLACE_STARTED, history.size(),
                fromMaterial.name(), toMaterial.name()));
  }

  @Override
  public boolean matches(String[] args) {
    return args.length == 3
        && Modify.Commands.REPLACE.getName().equalsIgnoreCase(args[0]);
  }
}