package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WallCommand implements SubCommand {

  private final int blocksPerTick;
  private final UndoHistoryService undoHistoryService;
  private final TranslationService translationService;
  private final SelectionService selectionService;

  public WallCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.undoHistoryService = serviceContext.getUndoHistoryService();
    this.translationService = serviceContext.getTranslationService();
    this.selectionService = serviceContext.getSelectionService();
  }


  @Override
  public void execute(Player player, String[] args) {
    Material material = Material.getMaterial(args[1].toUpperCase());
    if (material == null) {
      player.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
      return;
    }

    Selection selection = selectionService.resolve(player);
    if (selection == null) {
      return;
    }

    BlockHelper blockHelper = new BlockHelper(material);
    BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);
    List<ModifyHistoryEntry> history = new ArrayList<>();

    forEachBlock(selection, block -> {
      int x = block.getX();
      int z = block.getZ();

      if (x != selection.getMinX() && x != selection.getMaxX()
          && z != selection.getMinZ() && z != selection.getMaxZ()) {
        return;
      }

      checkAndRemoveProtection(block);
      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
      blockProcessor.process(block, blockHelper);
    });

    blockHelper.setBlocks(0);
    undoHistoryService.addHistory(player, history);
    player.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_WALL_STARTED, history.size(),
            material.name()));
  }

  @Override
  public boolean matches(String[] args) {
    return args.length == 2
        && Modify.Commands.WALL.getName().equalsIgnoreCase(args[0]);
  }
}